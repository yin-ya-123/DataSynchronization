package com.yin.utils.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLRecoverableException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransientConnectionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源熔断器（按 dsKey 维度）
 *
 * 触发条件：连续 FAIL_THRESHOLD 次"连接类"异常 -> 打开熔断 OPEN_MILLIS 毫秒
 * 熔断期间：对该数据源的所有操作直接跳过（秒级返回），不再发起建连、不再刷 ERROR 堆栈
 * 半开：熔断期结束后放行一次，成功则完全恢复，失败则重新计时
 */
@Slf4j
@Component
public class DataSourceCircuitBreaker {

    /** 连续失败多少次后打开熔断 */
    private static final int FAIL_THRESHOLD = 3;
    /** 熔断持续时间（毫秒） */
    private static final long OPEN_MILLIS = 60_000L;

    private final ConcurrentHashMap<String, State> states = new ConcurrentHashMap<>();

    private static class State {
        final AtomicInteger fails = new AtomicInteger();
        volatile long openUntil = 0L;
    }

    /**
     * 熔断是否处于打开状态。打开时调用方应直接跳过本轮任务。
     */
    public boolean isOpen(String dsKey) {
        State s = states.get(dsKey);
        if (s == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (now < s.openUntil) {
            return true;
        }
        if (s.fails.get() > 0) {
            // 半开：放一个请求试水
            s.openUntil = 0L;
            s.fails.set(0);
            log.info("数据源 {} 熔断期结束，进入半开状态", dsKey);
        }
        return false;
    }

    /**
     * 记录一次失败。仅连接类异常计入熔断。
     *
     * @param dsKey 数据源标识
     * @param ip    数据库服务器 IP（可为 null，仅用于日志定位到具体机器）
     * @param t     异常
     */
    public void onFailure(String dsKey, String ip, Throwable t) {
        if (!isConnectionError(t)) {
            return;
        }
        State s = states.computeIfAbsent(dsKey, k -> new State());
        int n = s.fails.incrementAndGet();
        log.warn("数据源 {} (ip={}) 连接失败(连续第 {} 次): {}", dsKey, ip, n, rootMessage(t));
        if (n >= FAIL_THRESHOLD && System.currentTimeMillis() >= s.openUntil) {
            s.openUntil = System.currentTimeMillis() + OPEN_MILLIS;
            log.error("数据源 {} (ip={}) 连续失败 {} 次，熔断 {} 秒，期间相关任务将快速跳过",
                    dsKey, ip, n, OPEN_MILLIS / 1000);
        }
    }

    /** 兼容调用：不含 ip */
    public void onFailure(String dsKey, Throwable t) {
        onFailure(dsKey, null, t);
    }

    /**
     * 记录一次成功，重置熔断计数。
     */
    public void onSuccess(String dsKey) {
        State s = states.get(dsKey);
        if (s != null && s.fails.get() > 0) {
            s.fails.set(0);
            s.openUntil = 0L;
            log.info("数据源 {} 恢复成功，熔断计数已重置", dsKey);
        }
    }

    /**
     * 判断是否为"连接类"异常（可重试 / 可熔断）。
     * 数据类异常（SQL 语法错误、唯一约束冲突、字段超长）返回 false。
     */
    public static boolean isConnectionError(Throwable t) {
        for (Throwable c = t; c != null; c = c.getCause()) {
            if (c instanceof SQLTimeoutException) {
                // 查询超时属于负载/数据问题，不熔断
                return false;
            }
            if (c instanceof org.springframework.jdbc.CannotGetJdbcConnectionException) {
                return true;
            }
            if (c instanceof SQLTransientConnectionException
                    || c instanceof SQLRecoverableException
                    || c instanceof SQLNonTransientConnectionException
                    || c instanceof ConnectException
                    || c instanceof SocketTimeoutException) {
                return true;
            }
            if (c.getCause() == c) {
                break;
            }
        }
        return false;
    }

    /**
     * 取最底层异常信息，便于日志定位。
     */
    public static String rootMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null && c.getCause() != c) {
            c = c.getCause();
        }
        String msg = c.getMessage();
        return c.getClass().getSimpleName() + (msg == null ? "" : ": " + msg);
    }
}
