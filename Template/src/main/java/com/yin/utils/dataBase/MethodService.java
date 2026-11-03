package com.yin.utils.dataBase;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.yin.pojo.dataflowdb.SysDatasource;
import com.yin.service.dataflowdb.impl.ErrorLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 任务处理
 */
@Component
@Slf4j
public class MethodService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DBFieldInfoService dbFieldInfoService;
    @Autowired
    private ErrorLogServiceImpl errorLogService;
    /** 探活等需要直接取连接的场景使用（路由数据源） */
    @Autowired
    private javax.sql.DataSource dataSource;

    @Value("${retry.max-attempts:3}")
    private int maxRetryAttempts;

    @Value("${retry.initial-interval:1000}")
    private int initialInterval;

    /**
     * 公共重试方法
     *
     * @param dsName 数据源名称
     * @param supplier 执行操作的函数式接口
     * @param <T> 返回值类型
     * @return 操作结果
     */
    private <T> T executeWithRetry(String dsName, Supplier<T> supplier) {
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetryAttempts) {
            attempt++;
            DynamicDataSourceContextHolder.push(dsName);
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e;
                log.warn("执行失败，第 {} 次尝试，错误信息: {}", attempt, e.getMessage());
                if (attempt < maxRetryAttempts) {
                    try {
                        // 重试延迟：避免立即重试，给数据库恢复时间
                        Thread.sleep(initialInterval);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("重试被中断", ie);
                    }
                }
            } finally {
                DynamicDataSourceContextHolder.poll();
            }
        }
        log.error("执行失败，已达到最大重试次数: {}", maxRetryAttempts);
        throw new RuntimeException("执行失败，已达到最大重试次数: " + maxRetryAttempts, lastException);
    }

    //数据同步其他数据库
    public void dataSync() {

    }

    //查询指定数据库数据返回
    public List<Map<String, Object>> queryData(String dsName, String sql, Object... arg) {
        DynamicDataSourceContextHolder.push(dsName);
        try {
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, arg);
            return maps;
        } catch (Exception e) {
            //写入日志
            errorLogService.errorLogSave(e);
            throw new RuntimeException(e);
        } finally {
            DynamicDataSourceContextHolder.clear();
            // 恢复默认超时时间（可选，避免影响其他查询）
            jdbcTemplate.setQueryTimeout(0);
        }
    }

    /**
     * 获取字段信息
     *
     * @param dsName 数据源名称
     * @param sql    SQL语句
     * @param dsLx   数据源类型
     * @param flag   操作标记1 标识sql 2 标识表
     * @return
     */
    public List<Map<String, Object>> getFieldInfo(String dsName, String sql, String dsLx, int flag) {
        log.info("数据源: {}", dsName);
        DynamicDataSourceContextHolder.push(dsName);
        try {
            List<Map<String, Object>> fileInfo = new ArrayList<>();
            if (flag == 1) {
                fileInfo = dbFieldInfoService.getSourceFieldInfo(jdbcTemplate, sql);
            } else if (flag == 2) {
                //数据源类型判断
                DBFieldInfoService.DbType dbType = null;
                System.out.println(dsLx);
                if (dsLx.equalsIgnoreCase("MYSQL")) {
                    dbType = DBFieldInfoService.DbType.MYSQL;
                } else if (dsLx.equalsIgnoreCase("ORACLE")) {
                    dbType = DBFieldInfoService.DbType.ORACLE;
                } else if (dsLx.equalsIgnoreCase("SQLSERVER")) {
                    dbType = DBFieldInfoService.DbType.SQLSERVER;
                }
                //获取字段信息
                fileInfo = dbFieldInfoService.getTargetTableFieldInfo(jdbcTemplate,
                        dbType, sql);
            }
            log.info("获取完毕");
            return fileInfo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DynamicDataSourceContextHolder.clear();
        }

    }

    /**
     * 执行更新操作（插入、更新、删除）
     *
     * @param dsName 数据源名称
     * @param sql    SQL语句
     * @param args   参数
     * @return 影响的行数
     */
    public int executeUpdate(String dsName, String sql, Object... args) {
//        log.info("数据源: {}", dsName);
//        log.info("执行SQL: {}", sql);
//        for (Object arg : args) {
//            log.info("参数: {}", arg);
//        }
        /**
         * 核心功能
         * 作用: 将指定的数据源名称(dsName)压入到动态数据源的上下文中
         * 目的: 让后续的数据库操作使用指定的数据源,而不是默认数据源
         * 工作原理
         * 线程隔离: DynamicDataSourceContextHolder 使用 ThreadLocal 机制,确保每个线程有独立的数据源上下文
         * 数据源切换: push(dsName) 方法将数据源名称设置到当前线程的上下文中
         * 自动路由: 当执行数据库操作时(如 jdbcTemplate.update()),动态数据源组件会从上下文中获取当前数据源名称,并路由到对应的数据库
         */
        return executeWithRetry(dsName, () -> {
            int result = jdbcTemplate.update(sql, args);
            log.info("执行结果: {}", result);
            return result;
        });
    }

    /**
     * 批量插入操作
     *
     * @param dsName   数据源名称
     * @param sql      SQL语句
     * @param argsList 参数列表，每个元素是一个Object数组，代表一条记录的参数
     * @return 影响的行数数组
     */
    public int[] batchUpdate(String dsName, String sql, List<Object[]> argsList) {
        return executeWithRetry(dsName, () -> {
            int[] results = jdbcTemplate.batchUpdate(sql, argsList);
            log.info("批量插入执行结果: {}", Arrays.stream(results).sum());
            return results;
        });
    }

    /**
     * 数据源探活：借一次连接执行一条最简查询。
     *
     * ⚠️ 探活 SQL **必须按库类型区分**（已在 Oracle 11g / SQL Server 2022 / MySQL 8.0 上实测）：
     *   - ORACLE    : 必须 "SELECT 1 FROM DUAL"
     *                  裸 "SELECT 1" 在 Oracle 11g 上报 ORA-00923: 未找到要求的 FROM 关键字
     *   - SQLSERVER : 必须 "SELECT 1"
     *                  带 "FROM DUAL" 报 "'DUAL' 不是有效的表名"
     *   - MYSQL     : 两种都支持，统一用 "SELECT 1"
     *
     * 用于任务开始前的快速可用性判断，避免不可用时逐行撞连接超时。
     * 依赖各库 URL 的连接超时属性兜底（见 §5.1 / §6）。
     *
     * @param ds             数据源配置（含 ip，便于日志定位到具体机器）
     * @param timeoutSeconds 语句超时秒数
     * @return true 表示可用
     */
    public boolean ping(SysDatasource ds, int timeoutSeconds) {
        if (ds == null) {
            return false;
        }
        // 探活 SQL 按库类型分支，不能用"通用"写法
        String probeSql = "ORACLE".equalsIgnoreCase(ds.getDsLx())
                ? "SELECT 1 FROM DUAL"
                : "SELECT 1";
        DynamicDataSourceContextHolder.push(ds.getDsKey());
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(probeSql)) {
            ps.setQueryTimeout(timeoutSeconds);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            // 带上 ip，排查第三方库多实例时能直接定位到"哪台机器不通"
            log.warn("数据源探活失败: dsKey={}, ip={}, schema={}, 原因: {}",
                    ds.getDsKey(), ds.getIp(), ds.getSchemaName(), e.getMessage());
            return false;
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
