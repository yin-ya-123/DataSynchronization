package com.yin.service.dataflowdb.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.config.error.BizException;
import com.yin.mapper.dataflowdb.FieldMappingMapper;
import com.yin.mapper.dataflowdb.ScheduledDetailsMapper;
import com.yin.mapper.dataflowdb.ScheduledMapper;
import com.yin.mapper.dataflowdb.SqlParamsMapper;
import com.yin.pojo.dataflowdb.*;
import com.yin.service.dataflowdb.DataMappingService;
import com.yin.utils.dataBase.MethodService;
import com.yin.utils.dataBase.SqlLoggerDynamic;
import com.yin.utils.dataBase.SyncFailureLogger;
import com.yin.utils.dataBase.SqlPlaceholderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 数据映射服务实现类
 * 根据任务查询出对应的明细，然后根据字段映射进行数据插入
 */
@Service
@Slf4j
@DS("master")
public class DataMappingServiceImpl implements DataMappingService {

    @Autowired
    private ScheduledDetailsMapper scheduledDetailsMapper;
    @Autowired
    private ScheduledMapper scheduledMapper;
    @Autowired
    private SqlParamsMapper sqlParamsMapper;
    @Autowired
    private FieldMappingMapper fieldMappingMapper;

    @Autowired
    private MethodService methodService;
    @Autowired
    private ErrorLogServiceImpl errorLogService;
    @Autowired
    private SysDatasourceServiceImpl sysDatasourceService;

    /** 数据源熔断器 */
    @Autowired
    private com.yin.utils.dataBase.DataSourceCircuitBreaker circuitBreaker;
    /** 失败明细本地日志记录器 */
    @Autowired
    private com.yin.utils.dataBase.SyncFailureLogger syncFailureLogger;

    /**
     * 单批写入行数
     */
    private static final int INSERT_CHUNK_SIZE = 100;
    /**
     * 单条任务明细连续失败多少个批次后中断本明细
     */
    private static final int MAX_FAIL_CHUNK = 3;
    /**
     * 数据源探活超时（秒）
     */
    private static final int PING_TIMEOUT_SECONDS = 3;
    /**
     * 防止同一任务被 cron 重复触发（慢查询时上一轮可能尚未结束）
     */
    private final java.util.Set<String> runningTasks = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /**
     * 根据任务ID查询任务明细
     *
     * @param scheduledId 任务ID
     * @return 任务明细列表
     */
    @Override
    public List<ScheduledDetails> getScheduledDetailsByTaskId(String scheduledId) {
        if (!StringUtils.hasText(scheduledId)) {
            log.error("任务ID不能为空");
            return new ArrayList<>();
        }

        LambdaQueryWrapper<ScheduledDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduledDetails::getScheduledId, scheduledId);
        return scheduledDetailsMapper.selectList(queryWrapper);
    }

    /**
     * 根据任务ID查询任务
     *
     * @param scheduledId 任务ID
     * @return 任务列表
     */
    @Override
    public List<Scheduled> getScheduledByTaskId(String scheduledId) {
        if (!StringUtils.hasText(scheduledId)) {
            log.error("任务ID不能为空");
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Scheduled> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Scheduled::getCronId, scheduledId);
        return scheduledMapper.selectList(queryWrapper);
    }

    /**
     * 根据任务ID查询任务
     *
     * @param scheduledId 任务ID
     * @return 任务列表
     */
    @Override
    public List<SqlParams> getSqlParamsByScheduledId(String scheduledId, String isMr) {
        if (!StringUtils.hasText(scheduledId)) {
            log.error("任务ID不能为空");
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SqlParams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SqlParams::getScheduledId, scheduledId);
        queryWrapper.eq(SqlParams::getIsMr, isMr);
        return sqlParamsMapper.selectList(queryWrapper);
    }

    /**
     * 根据任务明细查询字段映射关系
     *
     * @param scheduledDetails 任务明细
     * @return 字段映射关系列表
     */
    @Override
    public List<FieldMappingPojo> getFieldMappingByScheduledDetails(ScheduledDetails scheduledDetails) {
        if (scheduledDetails == null || !StringUtils.hasText(scheduledDetails.getScheduledId())) {
            log.error("任务明细或任务ID不能为空");
            return new ArrayList<>();
        }
        LambdaQueryWrapper<FieldMappingPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FieldMappingPojo::getScheduledId, scheduledDetails.getScheduledId());
        queryWrapper.isNotNull(FieldMappingPojo::getTargetField);
        queryWrapper.orderByAsc(FieldMappingPojo::getSort);
        return fieldMappingMapper.selectList(queryWrapper);
    }

    /**
     * 根据任务明细查询源数据，主数据源
     *
     * @param scheduledDetails 任务明细
     * @return 源数据列表
     */

    public List<Map<String, Object>> querySourceData(ScheduledDetails scheduledDetails) {
        return querySourceData(scheduledDetails, null);
    }

    @Override
    public List<Map<String, Object>> querySourceData(ScheduledDetails scheduledDetails, List<SqlParams> sqlParamsByTaskId) {
        if (scheduledDetails == null || !StringUtils.hasText(scheduledDetails.getZsjSql())
                || !StringUtils.hasText(scheduledDetails.getZsjKs())) {
            log.error("任务明细、源SQL或源数据库标识不能为空");
            return new ArrayList<>();
        }
        try {
            //进行SQL占位符转换
            if (null == sqlParamsByTaskId || !sqlParamsByTaskId.isEmpty()) {
                String newSql = SqlPlaceholderUtil.replacePlaceholdersWithSqlParams(scheduledDetails.getZsjSql(), sqlParamsByTaskId);
                return methodService.queryData(scheduledDetails.getZsjKs(), newSql);
            } else {
                return methodService.queryData(scheduledDetails.getZsjKs(), scheduledDetails.getZsjSql());
            }

        } catch (Exception e) {
            log.error("查询源数据失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据字段映射关系转换数据,传入源集合数据，根据源字段获取后，在put集合中去后，目标字段赋值
     *
     * @param sourceData    源数据
     * @param fieldMappings 字段映射关系
     * @return 转换后的数据
     */
    @Override
    public List<Map<String, Object>> transformData(List<Map<String, Object>> sourceData, List<FieldMappingPojo> fieldMappings) {
        if (CollectionUtils.isEmpty(sourceData) || CollectionUtils.isEmpty(fieldMappings)) {
            log.error("源数据或字段映射关系不能为空");
            return new ArrayList<>();
        }

        List<Map<String, Object>> targetDataList = new ArrayList<>();

        for (Map<String, Object> sourceItem : sourceData) {
            Map<String, Object> targetItem = new HashMap<>();

            for (FieldMappingPojo mapping : fieldMappings) {
                //获取每一行数据的映射关系，不会出现不一致问题
                String sourceField = mapping.getSourceField();
                String targetField = mapping.getTargetField();
                String mappingRule = mapping.getMappingRule();

                if (!StringUtils.hasText(sourceField) || !StringUtils.hasText(targetField)) {
                    continue;
                }

                Object value = sourceItem.get(sourceField);
                // 如果有映射规则，则应用映射规则
                if (StringUtils.hasText(mappingRule) && value != null) {
                    // 这里可以根据实际需求实现更复杂的映射规则
                    // 简单示例：如果规则是"UPPER"，则将字符串转为大写
                    if ("UPPER".equalsIgnoreCase(mappingRule) && value instanceof String) {
                        value = ((String) value).toUpperCase();
                    }
                    // 可以添加更多的规则处理...
                }

                targetItem.put(targetField, value);
            }

            targetDataList.add(targetItem);
        }

        return targetDataList;
    }

    /**
     * 将转换后的数据插入目标表
     * 调整点：
     * 1. 逐行 INSERT -> 分块批量 INSERT（chunk = INSERT_CHUNK_SIZE 行）
     * 2. 统一列顺序（不能依赖 HashMap 的 entrySet 迭代顺序）
     * 3. 批量失败 -> 精确定位到行，写本地失败日志
     * 4. 连续失败 MAX_FAIL_CHUNK 个批次 -> 中断本明细，不影响其它明细
     * 5. 熔断打开时直接跳过
     *
     * @param targetData       转换后的数据
     * @param scheduledDetails 任务明细
     * @param sysDatasource    目标数据源
     * @param fieldMappings    字段映射
     * @return 是否全部成功
     */
    @Override
    public boolean insertTargetData(List<Map<String, Object>> targetData, ScheduledDetails scheduledDetails,
                                    SysDatasource sysDatasource, List<FieldMappingPojo> fieldMappings) {
        if (CollectionUtils.isEmpty(targetData) || scheduledDetails == null
                || !StringUtils.hasText(scheduledDetails.getToSql())
                || !StringUtils.hasText(scheduledDetails.getToKs())) {
            log.error("目标数据、任务明细、目标表名或目标数据库标识不能为空");
            return false;
        }
        // 表名未带 schema 前缀时，用数据源配置的 schema_name 补全
        String targetTable = resolveTargetTable(scheduledDetails.getToSql(), sysDatasource);
        String targetDs = scheduledDetails.getToKs();

        // 1) 熔断判断：目标库不可用时直接跳过，不做任何写入
        if (circuitBreaker.isOpen(targetDs)) {
            log.warn("目标数据源 {} 处于熔断状态，跳过任务明细 {}", targetDs, scheduledDetails.getId());
            return false;
        }

        // 2) 探活：避免不可用时逐批撞连接超时
        if (!methodService.ping(sysDatasource, PING_TIMEOUT_SECONDS)) {
            circuitBreaker.onFailure(targetDs, sysDatasource.getIp(),
                    new java.sql.SQLTransientConnectionException("数据源不可用: " + targetDs));
            log.warn("目标数据源 {} 不可用，跳过任务明细 {}", targetDs, scheduledDetails.getId());
            return false;
        }

        // 3) 统一列顺序 + 计算列日期策略 + 生成 SQL（只生成一次，所有批次复用）
        // 获取映射字段与日期类型
        Map<String, String> stringStringMap = convertToDataTypeMappingMap(fieldMappings);
        // 获取（转换后的目标数据）的列名
        List<String> columns = new ArrayList<>(targetData.get(0).keySet());
        // 设置转换后的列名对应的日期转换类型配置
        Map<String, DateStrategy> strategies = resolveDateStrategies(columns, targetData, sysDatasource, stringStringMap);
        // 获取生成式sql插入语句
        String sql = buildInsertSql(targetTable, columns, strategies);
        System.out.println(sql);
        // 4) 组装参数行（按 columns 顺序取值，并按策略做日期转换）
        List<Object[]> rows = buildRows(targetData, columns, strategies);

        // 5) 分块批量写入
        boolean allSuccess = true;
        int failChunk = 0;
        for (int c = 0; c * INSERT_CHUNK_SIZE < rows.size(); c++) {
            int from = c * INSERT_CHUNK_SIZE;
            int to = Math.min(from + INSERT_CHUNK_SIZE, rows.size());
            List<Object[]> chunk = new ArrayList<>(rows.subList(from, to));
            try {
                methodService.batchUpdate(targetDs, sql, chunk);
                circuitBreaker.onSuccess(targetDs);
                log.info("目标表 {} 批次写入成功, 批次={}, 行数={}", targetTable, c, chunk.size());
            } catch (java.sql.BatchUpdateException e) {
                allSuccess = false;
                failChunk++;
                log.error("目标表 {} 批次写入失败, 批次={}, 行数={}", targetTable, c, chunk.size(), e);
                locateFailedRows(targetDs, targetTable, sql, chunk, from, c, scheduledDetails, e);
                circuitBreaker.onFailure(targetDs, sysDatasource.getIp(), e);
                if (failChunk >= MAX_FAIL_CHUNK) {
                    log.error("任务明细 {} 连续 {} 个批次失败，中断本明细（其它明细继续执行）",
                            scheduledDetails.getId(), failChunk);
                    break;
                }
            } catch (Exception e) {
                allSuccess = false;
                failChunk++;
                log.error("目标表 {} 批次写入异常, 批次={}", targetTable, c, e);
                circuitBreaker.onFailure(targetDs, sysDatasource.getIp(), e);
                if (failChunk >= MAX_FAIL_CHUNK) {
                    log.error("任务明细 {} 连续 {} 个批次失败，中断本明细", scheduledDetails.getId(), failChunk);
                    break;
                }
            }
        }
        return allSuccess;
    }

    /**
     * 解析目标表名：未带 schema 前缀时，用数据源配置的 schema_name 补全。
     *
     * @param toSql          任务明细里配置的目标表名（可能已带 schema）
     * @param sysDatasource  目标数据源配置
     * @return 最终表名
     */
    private String resolveTargetTable(String toSql, SysDatasource sysDatasource) {
        if (!StringUtils.hasText(toSql)) {
            return toSql;
        }
        if (toSql.contains(".")) {
            // 已带 schema，直接使用
            return toSql;
        }
        if (sysDatasource != null && StringUtils.hasText(sysDatasource.getSchemaName())) {
            return sysDatasource.getSchemaName().trim() + "." + toSql;
        }
        return toSql;
    }

    /** 列的日期处理策略 */
    private enum DateStrategy {
        /** 不做日期处理，原样传值 */
        NONE,
        /** Oracle: to_date(?, 'yyyy-mm-dd hh24:mi:ss') + 字符串值 */
        ORACLE_DATE_TIME,
        /** Oracle: to_date(?, 'yyyy-mm-dd') + 字符串值 */
        ORACLE_DATE,
        /** 非 Oracle: ? + java.sql.Timestamp 值 */
        PARAM_TIMESTAMP
    }

    /**
     * 计算每一列的日期处理策略（按列统一，整批共用一条 SQL）。
     *
     * 规则：
     * - ORACLE    + dataTypeMapping="年月日时分秒" -> ORACLE_DATE_TIME
     * - ORACLE    + dataTypeMapping="年月日"     -> ORACLE_DATE
     * - ORACLE    + dataTypeMapping="无"         -> NONE
     * - ORACLE    + 未配置/其它值                -> 整列采样：任一行是日期则 ORACLE_DATE_TIME，否则 NONE
     * - SQLSERVER / MYSQL                       -> 整列采样：任一行是日期则 PARAM_TIMESTAMP，否则 NONE
     */
    private Map<String, DateStrategy> resolveDateStrategies(List<String> columns,
                                                             List<Map<String, Object>> targetData,
                                                             SysDatasource sysDatasource,
                                                             Map<String, String> stringStringMap) {
        boolean oracle = "ORACLE".equalsIgnoreCase(sysDatasource.getDsLx());
        Map<String, DateStrategy> strategies = new LinkedHashMap<>();
        for (String col : columns) {
            DateStrategy s;
            String mapping = stringStringMap.get(col);
            if (oracle) {
                if ("年月日时分秒".equals(mapping)) {
                    s = DateStrategy.ORACLE_DATE_TIME;
                } else if ("年月日".equals(mapping)) {
                    s = DateStrategy.ORACLE_DATE;
                } else if ("无".equals(mapping)) {
                    s = DateStrategy.NONE;
                } else {
                    s = DateStrategy.NONE;
                    for (Map<String, Object> row : targetData) {
                        if (SqlLoggerDynamic.isDateArg(row.get(col))) {
                            s = DateStrategy.ORACLE_DATE_TIME;
                            break;
                        }
                    }
                }
            } else {
                s = DateStrategy.NONE;
                for (Map<String, Object> row : targetData) {
                    if (SqlLoggerDynamic.isDateArg(row.get(col))) {
                        s = DateStrategy.PARAM_TIMESTAMP;
                        break;
                    }
                }
            }
            strategies.put(col, s);
        }
        return strategies;
    }

    /**
     * 生成 INSERT 语句。列顺序由外部固定，避免每行各自遍历 HashMap 导致列序不一致。
     */
    private String buildInsertSql(String targetTable, List<String> columns,
                                  Map<String, DateStrategy> strategies) {
        StringBuilder sqlColumns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sqlColumns.append(", ");
                placeholders.append(", ");
            }
            String col = columns.get(i);
            sqlColumns.append(col);
            DateStrategy s = strategies.get(col);
            if (s == DateStrategy.ORACLE_DATE_TIME) {
                placeholders.append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
            } else if (s == DateStrategy.ORACLE_DATE) {
                placeholders.append("to_date(?, 'yyyy-mm-dd')");
            } else {
                placeholders.append("?");
            }
        }
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                targetTable, sqlColumns.toString(), placeholders.toString());
    }

    /**
     * 按列顺序组装参数行，并按日期策略转换值。
     */
    private List<Object[]> buildRows(List<Map<String, Object>> targetData, List<String> columns,
                                     Map<String, DateStrategy> strategies) {
        List<Object[]> rows = new ArrayList<>(targetData.size());
        for (Map<String, Object> item : targetData) {
            Object[] vals = new Object[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                String col = columns.get(i);
                Object v = item.get(col);
                DateStrategy s = strategies.get(col);
                if (s == DateStrategy.ORACLE_DATE_TIME) {
                    v = SqlLoggerDynamic.formatDate(v, "yyyy-MM-dd HH:mm:ss");
                } else if (s == DateStrategy.ORACLE_DATE) {
                    v = SqlLoggerDynamic.formatDate(v, "yyyy-MM-dd");
                } else if (s == DateStrategy.PARAM_TIMESTAMP) {
                    v = toTimestamp(v);
                }
                vals[i] = v;
            }
            rows.add(vals);
        }
        return rows;
    }

    /**
     * 把日期字符串 / java.util.Date 统一转成 java.sql.Timestamp。
     *
     * 供 SQL Server / MySQL 目标使用：避免把日期字符串交给服务端做隐式转换
     * （SQL Server 的隐式转换受 dateformat / 语言设置影响，易报错或丢精度）。
     */
    private Object toTimestamp(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) v;
        }
        if (v instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) v).getTime());
        }
        String s = String.valueOf(v).trim();
        String[] patterns = {"yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
        for (String p : patterns) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(p);
                sdf.setLenient(false);
                return new java.sql.Timestamp(sdf.parse(s).getTime());
            } catch (Exception ignore) {
                // 试下一个格式
            }
        }
        // 无法识别则原样返回，由数据库报错，失败行定位机制会记录到本地日志
        return v;
    }

    /**
     * 精确定位批次内失败的行，并把失败明细写入本地日志。
     *
     * 规则（**不同驱动语义不同**，详见 §5.4）：
     * - counts 中值为 Statement.EXECUTE_FAILED(-3) 的位置 = 失败行
     *   （Oracle ojdbc8、MySQL connector-j 会填 -3）
     * - counts.length == 批次数  -> 该行失败，其余行已成功，不重试
     * - counts.length <  批次数  -> 驱动在失败处停止，其后的行未执行，可安全续跑
     *   （SQL Server mssql-jdbc 属于此类，且**不会**填 -3 标记）
     * - counts 为空或无 -3 标记   -> 退化为逐行定位（覆盖 SQL Server）
     */
    private void locateFailedRows(String targetDs, String targetTable, String sql,
                                  List<Object[]> chunk, int chunkStartRow, int chunkNo,
                                  ScheduledDetails scheduledDetails, java.sql.BatchUpdateException e) {
        int[] counts = e.getUpdateCounts();
        int failIdx = -1;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] == java.sql.Statement.EXECUTE_FAILED) {
                failIdx = i;
                break;
            }
        }

        // 情况 A：拿到了失败标记
        if (failIdx >= 0) {
            recordRowFailure(targetDs, targetTable, sql, chunk, failIdx, chunkStartRow, chunkNo,
                    scheduledDetails, e);
            // 驱动执行了整个批次（counts 覆盖全部行）-> 其余行已成功，不重试
            if (counts.length >= chunk.size()) {
                log.info("批次 {} 失败行定位完成: 行号={}, 其余 {} 行已成功，不再重试",
                        chunkNo, chunkStartRow + failIdx, chunk.size() - 1);
                return;
            }
            // 驱动在失败处停止 -> 其后的行未执行，继续写入（下一轮批次）
            int from = failIdx + 1;
            if (from < chunk.size()) {
                log.warn("批次 {} 失败行定位完成: 行号={}, 剩余 {} 行未执行，继续写入",
                        chunkNo, chunkStartRow + failIdx, chunk.size() - from);
                retryRemainder(targetDs, targetTable, sql, chunk, from, chunkStartRow, chunkNo,
                        scheduledDetails, 0);
            }
            return;
        }

        // 情况 B：没拿到失败标记（counts 为空 / 长度异常 / 无 -3）
        // SQL Server mssql-jdbc 属于此类：遇错即停且不填 -3
        // 保守处理：整块逐行定位，精确到行
        log.warn("批次 {} 无法从 getUpdateCounts() 定位失败行，退化为逐行定位", chunkNo);
        for (int i = 0; i < chunk.size(); i++) {
            try {
                methodService.batchUpdate(targetDs, sql,
                        new ArrayList<>(chunk.subList(i, i + 1)));
            } catch (Exception ex) {
                recordRowFailure(targetDs, targetTable, sql, chunk, i, chunkStartRow, chunkNo,
                        scheduledDetails, ex);
            }
        }
    }

    /**
     * 续跑失败行之后未执行的行（最多递归 5 层，防止无限递归）。
     */
    private void retryRemainder(String targetDs, String targetTable, String sql, List<Object[]> chunk,
                                int from, int chunkStartRow, int chunkNo,
                                ScheduledDetails scheduledDetails, int depth) {
        if (from >= chunk.size() || depth >= 5) {
            return;
        }
        List<Object[]> remain = new ArrayList<>(chunk.subList(from, chunk.size()));
        try {
            methodService.batchUpdate(targetDs, sql, remain);
            log.info("批次 {} 剩余 {} 行补写成功", chunkNo, remain.size());
        } catch (java.sql.BatchUpdateException e2) {
            locateFailedRows(targetDs, targetTable, sql, remain, chunkStartRow + from, chunkNo,
                    scheduledDetails, e2);
        } catch (Exception e2) {
            log.error("批次 {} 剩余行补写异常, 起始行号={}", chunkNo, chunkStartRow + from, e2);
        }
    }

    /**
     * 记录单行失败明细到本地日志。
     */
    private void recordRowFailure(String targetDs, String targetTable, String sql, List<Object[]> chunk,
                                  int idx, int chunkStartRow, int chunkNo,
                                  ScheduledDetails scheduledDetails, Throwable error) {
        Map<String, Object> rowValues = new LinkedHashMap<>();
        Object[] vals = chunk.get(idx);
        for (int i = 0; i < vals.length; i++) {
            rowValues.put("col_" + i, vals[i]);
        }
        // 从目标数据源配置中取 ip / schema，便于回溯
        String targetIp = null;
        String targetSchema = null;
        try {
            LambdaQueryWrapper<SysDatasource> w = new LambdaQueryWrapper<>();
            w.eq(SysDatasource::getDsKey, targetDs);
            SysDatasource ds = sysDatasourceService.getOne(w);
            if (ds != null) {
                targetIp = ds.getIp();
                targetSchema = ds.getSchemaName();
            }
        } catch (Exception ignore) {
            // 取不到配置不影响失败记录
        }
        Map<String, Object> record = SyncFailureLogger.buildRecord(
                scheduledDetails.getScheduledId(),
                scheduledDetails.getId(),
                scheduledDetails.getToMc(),
                targetDs,
                targetIp,
                targetSchema,
                targetTable,
                chunkNo,
                idx,
                chunkStartRow + idx,
                rowValues,
                sql,
                error);
        syncFailureLogger.record(record);
        log.error("目标表 {} 第 {} 行写入失败，已记录到 logs/data-sync-error.log: {}",
                targetTable, chunkStartRow + idx, error.getMessage());
    }


    /**
     * 执行数据映射流程
     *
     * @param scheduledId 任务ID
     * @return 执行结果
     */
    @Override
    public boolean executeDataMapping(String scheduledId, Object... par) {
        if (!StringUtils.hasText(scheduledId)) {
            log.error("任务ID不能为空");
            return false;
        }
        // 防重入：慢查询场景下 cron 可能在上轮未结束时再次触发，
        // 导致同一张目标表被并发写入（死锁 / 重复插入）
        if (!runningTasks.add(scheduledId)) {
            log.warn("任务正在运行中，跳过本次触发, scheduledId={}", scheduledId);
            return false;
        }
        try {
            //1. 查询任务对象
            List<Scheduled> scheduledByTaskId = getScheduledByTaskId(scheduledId);
            if (CollectionUtils.isEmpty(scheduledByTaskId)) {
                log.error("未找到任务，任务ID: {}", scheduledId);
                return false;
            }

            // 1. 查询任务明细
            List<ScheduledDetails> detailsList = getScheduledDetailsByTaskId(scheduledId);
            if (CollectionUtils.isEmpty(detailsList)) {
                log.error("未找到任务明细，任务ID: {}", scheduledId);
                return false;
            }
            //2. 查询任务的占位符参数,par[0].toString() 是否默认
            List<SqlParams> sqlParamsByTaskId = getSqlParamsByScheduledId(scheduledId, par[0].toString());
            Scheduled scheduled = scheduledByTaskId.get(0);
            if (null != scheduled.getCronType() && scheduled.getCronType().equals("定时任务")) {
                return executeDataRw(detailsList);
            } else {
                return executedDataTb(detailsList, sqlParamsByTaskId);
            }
        } catch (Exception e) {
            log.error("执行数据映射流程失败: {}", e.getMessage(), e);
            errorLogService.errorLogSave(e);
            throw new BizException("00001", e.getMessage()); // 抛出异常以触发事务回滚
        } finally {
            runningTasks.remove(scheduledId);
        }
    }

    /**
     * 执行定时任务 定时任务是直接执行主数据源的语句的
     * @param detailsList 任务明细
     * @return 执行结果
     */
    private boolean executeDataRw(List<ScheduledDetails> detailsList) {
        //定时任务直接执行对应的本表语句
        ScheduledDetails scheduledDetails = detailsList.get(0);
        //失败后进行错误日志插入数据库
        ErrorLogPojo errorLogPojo = new ErrorLogPojo();
        try {
            methodService.executeUpdate(scheduledDetails.getZsjKs(), scheduledDetails.getZsjSql());
//            errorLogPojo.setScheduledId(scheduledDetails.getScheduledId());
//            errorLogPojo.setErrorType("成功");
//            errorLogPojo.setDelFlag("0");
//            errorLogPojo.setErrorData("");
//            errorLogPojo.setErrorDescription("");
//            errorLogService.save(errorLogPojo);
            log.info("定时任务执行成功");
        } catch (Exception e) {
            log.error("插入目标数据失败: {}", e.getMessage(), e);
            errorLogPojo.setScheduledId(scheduledDetails.getScheduledId());
            errorLogPojo.setErrorType("插入失败");
            errorLogPojo.setDelFlag("0");
            errorLogPojo.setErrorData("");
            errorLogPojo.setErrorDescription(e.getMessage());
            errorLogService.save(errorLogPojo);
        }
        return false;
    }

    /**
     * 执行数据同步
     *
     * @param detailsList       任务明细列表
     * @param sqlParamsByTaskId 任务参数列表
     * @return 是否所有任务明细执行成功
     */
    private boolean executedDataTb(List<ScheduledDetails> detailsList, List<SqlParams> sqlParamsByTaskId) {
        boolean allSuccess = true;

        // 2. 遍历每个任务明细进行处理
        for (ScheduledDetails details : detailsList) {
            // 明细级隔离 —— 一条明细失败不影响其它明细，也不再向上冒泡终止整个任务
            try {
                // 3. 查询字段映射关系
                List<FieldMappingPojo> fieldMappings = getFieldMappingByScheduledDetails(details);
                if (CollectionUtils.isEmpty(fieldMappings)) {
                    log.warn("未找到字段映射关系，跳过任务明细: {}", details.getId());
                    continue;
                }
                log.info("查询字段映射成功");

                // 4. 查询源数据,根据明细任务进行数据查询
                List<Map<String, Object>> sourceData = querySourceData(details, sqlParamsByTaskId);
                if (CollectionUtils.isEmpty(sourceData)) {
                    log.warn("未查询到源数据，跳过任务明细: {}", details.getId());
                    continue;
                }
                log.info("查询源数据成功");
                // 5. 转换数据，源数据list然后转化为列名为目标名称的list
                List<Map<String, Object>> targetData = transformData(sourceData, fieldMappings);
                if (CollectionUtils.isEmpty(targetData)) {
                    log.warn("数据转换结果为空，跳过任务明细: {}", details.getId());
                    continue;
                }
                log.info("数据转换成功{}", JSONUtil.toJsonStr(targetData.get(0)));
                LambdaQueryWrapper<SysDatasource> sysDatasourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
                sysDatasourceLambdaQueryWrapper.eq(SysDatasource::getDsKey, details.getToKs());
                SysDatasource sysDatasource = sysDatasourceService.getOne(sysDatasourceLambdaQueryWrapper);
                // 6. 插入目标数据
                boolean insertResult = insertTargetData(targetData, details, sysDatasource, fieldMappings);
                if (!insertResult) {
                    log.error("插入目标数据失败，任务明细: {}", details.getId());
                    allSuccess = false;
                } else {
                    log.info("同步数据任务执行成功");
                }
            } catch (Exception e) {
                // 单条明细的任何异常都不再向上冒泡，继续处理下一条明细
                allSuccess = false;
                log.error("任务明细 {} 执行失败，继续处理下一条明细", details.getId(), e);
            }
        }
        return allSuccess;
    }

    /**
     * 定时任务调用
     * 定时任务调用会传入1
     */
    @Async("customThreadPool")
    public void executeDataMappingTask(String scheduledId, Object... par) {
        // @Async 方法内异常逃逸会杀死线程池 worker 并静默丢弃任务
        try {
            executeDataMapping(scheduledId, par);
        } catch (Throwable t) {
            log.error("数据同步任务执行异常, scheduledId={}", scheduledId, t);
        }
    }


}