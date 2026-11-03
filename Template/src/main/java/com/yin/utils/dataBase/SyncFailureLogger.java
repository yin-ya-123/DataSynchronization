package com.yin.utils.dataBase;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据同步失败明细的本地日志记录器。
 *
 * 落盘位置由 logback-spring.xml 中的 SYNC_ERROR logger 决定：
 *   logs/data-sync-error.log（按天滚动，保留 30 天）
 *
 * 不依赖任何数据库，数据库不可用时依然可以记录。
 */
@Slf4j
@Component
public class SyncFailureLogger {

    /** 独立 logger，路由到独立文件，不污染主日志 */
    private static final Logger FAIL_LOG = LoggerFactory.getLogger("SYNC_ERROR");

    /** 是否同时写 error_log 表（默认关闭，避免数据库不可用时二次失败） */
    @Value("${sync.failure.write-db-error-log:false}")
    private boolean writeDbErrorLog;

    public boolean isWriteDbErrorLog() {
        return writeDbErrorLog;
    }

    /**
     * 记录一条失败明细（行级）。
     *
     * @param record 失败明细，字段见 buildRecord
     */
    public void record(Map<String, Object> record) {
        try {
            FAIL_LOG.error(JSONUtil.toJsonStr(record));
        } catch (Exception e) {
            // 写本地日志本身失败不能影响主流程
            log.error("写入同步失败本地日志异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录一条汇总信息（批次级）。
     */
    public void recordSummary(Map<String, Object> record) {
        try {
            FAIL_LOG.error("SUMMARY " + JSONUtil.toJsonStr(record));
        } catch (Exception e) {
            log.error("写入同步失败本地日志异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 构造标准结构的失败记录。
     */
    public static Map<String, Object> buildRecord(String scheduledId, String detailId, String cronName,
                                                  String targetDs, String targetIp, String targetSchema,
                                                  String targetTable,
                                                  int chunkNo, int rowIndexInChunk, int globalRowNo,
                                                  Map<String, Object> rowValues,
                                                  String sql, Throwable error) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("scheduledId", scheduledId);
        r.put("detailId", detailId);
        r.put("cronName", cronName);
        r.put("targetDs", targetDs);
        r.put("targetIp", targetIp);
        r.put("targetSchema", targetSchema);
        r.put("targetTable", targetTable);
        r.put("chunkNo", chunkNo);
        r.put("rowIndexInChunk", rowIndexInChunk);
        r.put("globalRowNo", globalRowNo);
        r.put("rowValues", rowValues);
        r.put("sql", sql);
        r.put("errorType", error == null ? null : error.getClass().getSimpleName());
        r.put("errorCode", extractErrorCode(error));
        r.put("sqlState", extractSqlState(error));
        r.put("errorMsg", error == null ? null : error.getMessage());
        r.put("rootMsg", error == null ? null : DataSourceCircuitBreaker.rootMessage(error));
        r.put("connectionError", error != null && DataSourceCircuitBreaker.isConnectionError(error));
        r.put("occurTime", java.time.LocalDateTime.now().toString());
        return r;
    }

    /**
     * 提取数据库原生错误码（不含方言前缀）。
     * Oracle: 1 -> ORA-00001（由调用方按 dsLx 拼前缀）
     * SQL Server: 2627 -> 违反唯一约束；8152 -> 字符串截断
     * MySQL: 1062 -> Duplicate entry
     */
    private static String extractErrorCode(Throwable error) {
        for (Throwable c = error; c != null; c = c.getCause()) {
            if (c instanceof java.sql.SQLException) {
                int code = ((java.sql.SQLException) c).getErrorCode();
                if (code != 0) {
                    return String.valueOf(code);
                }
            }
            if (c.getCause() == c) {
                break;
            }
        }
        return null;
    }

    /**
     * 提取 SQLState（跨库统一，23000 = 完整性约束冲突，22000 = 数据类型/长度问题）。
     */
    private static String extractSqlState(Throwable error) {
        for (Throwable c = error; c != null; c = c.getCause()) {
            if (c instanceof java.sql.SQLException) {
                String state = ((java.sql.SQLException) c).getSQLState();
                if (state != null) {
                    return state;
                }
            }
            if (c.getCause() == c) {
                break;
            }
        }
        return null;
    }
}
