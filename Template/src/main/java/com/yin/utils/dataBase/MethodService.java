package com.yin.utils.dataBase;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
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
}
