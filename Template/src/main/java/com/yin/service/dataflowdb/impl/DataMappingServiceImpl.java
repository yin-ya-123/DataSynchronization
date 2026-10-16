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
     *
     * @param targetData       转换后的数据，集合MAP中key为目标字段，value为值
     * @param scheduledDetails 任务明细
     * @return 插入结果
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean insertTargetData(List<Map<String, Object>> targetData, ScheduledDetails scheduledDetails, SysDatasource sysDatasource, List<FieldMappingPojo> fieldMappings) {
        if (CollectionUtils.isEmpty(targetData) || scheduledDetails == null
                || !StringUtils.hasText(scheduledDetails.getToSql())
                || !StringUtils.hasText(scheduledDetails.getToKs())) {
            log.error("目标数据、任务明细、目标表名或目标数据库标识不能为空");
            return false;
        }
        //转换数据类型
        Map<String, String> stringStringMap = convertToDataTypeMappingMap(fieldMappings);
        String targetTable = scheduledDetails.getToSql();
        String targetDs = scheduledDetails.getToKs();
        HashMap<String, Object> insertErrorInfo = new HashMap<>();
        for (Map<String, Object> item : targetData) {
            //失败后进行错误日志插入数据库
            ErrorLogPojo errorLogPojo = new ErrorLogPojo();
            try {
                if (item.isEmpty()) {
                    continue;
                }
                StringBuilder columns = new StringBuilder();
                StringBuilder placeholders = new StringBuilder();
                List<Object> values = new ArrayList<>();

                for (Map.Entry<String, Object> entry : item.entrySet()) {
                    if (columns.length() > 0) {
                        columns.append(", ");
                        placeholders.append(", ");
                    }
                    columns.append(entry.getKey());
                    //进行oracle日期格式转换，因为oracle数据库的日期格式为yyyy-mm-dd hh24:mi:ss
                    Object value = entry.getValue();
                    if (sysDatasource.getDsLx().equalsIgnoreCase("ORACLE")) {
                        //进行日期格式转换,根据类型判断
                        /**
                         * 无
                         *年月日时分秒
                         *年月日
                         */
                        String dataTypeMapping = stringStringMap.get(entry.getKey());
                        if (null != dataTypeMapping) {
                            switch (dataTypeMapping) {
                                case "无":
                                    placeholders.append("?");
                                    break;
                                case "年月日时分秒":
                                    placeholders.append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
                                    value = SqlLoggerDynamic.formatDate(entry.getValue(), "yyyy-MM-dd HH:mm:ss");
                                    break;
                                case "年月日":
                                    placeholders.append("to_date(?, 'yyyy-mm-dd')");
                                    value = SqlLoggerDynamic.formatDate(entry.getValue(), "yyyy-MM-dd");
                                    break;
                                default:
                                    if (SqlLoggerDynamic.isDateArg(value)) {
                                        placeholders.append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
                                        value = SqlLoggerDynamic.formatDate(entry.getValue(), "yyyy-MM-dd HH:mm:ss");
                                    } else {
                                        placeholders.append("?");
                                    }
                                    break;
                            }
                        }
                    } else {
                        placeholders.append("?");
                    }
                    values.add(value);
                    // 将经过转换的value数据，重新替换map对应key
                    item.put(entry.getKey(), value);
                }
                /**
                 * targetTable 目标表
                 * columns 目标字段
                 * placeholders 占位符
                 */
                String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                        targetTable, columns.toString(), placeholders.toString());
                String newLogSql = SqlLoggerDynamic.fillSql(sql, item);
                insertErrorInfo.put("sql", newLogSql);
                insertErrorInfo.put("values", item.toString());
                // 使用MethodService执行插入操作
                methodService.executeUpdate(targetDs, sql, values.toArray());
//                errorLogPojo.setScheduledId(scheduledDetails.getScheduledId());
//                errorLogPojo.setErrorType("成功");
//                errorLogPojo.setDelFlag("0");
//                errorLogPojo.setErrorData(JSONUtil.toJsonStr(insertErrorInfo));
//                errorLogPojo.setErrorDescription("");
//                errorLogService.save(errorLogPojo);
            } catch (Exception e) {
                log.error("插入目标数据失败: {}", e.getMessage(), e);
                errorLogPojo.setScheduledId(scheduledDetails.getScheduledId());
                errorLogPojo.setErrorType("插入失败");
                errorLogPojo.setDelFlag("0");
                errorLogPojo.setErrorData(JSONUtil.toJsonStr(insertErrorInfo));
                errorLogPojo.setErrorDescription(e.getMessage());
                errorLogService.save(errorLogPojo);
            }
        }
        return true;
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
            List<SqlParams> sqlParamsByTaskId = getSqlParamsByScheduledId(scheduledId,par[0].toString());
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
        }
    }

    /**
     * 执行定时任务
     *
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
            // 5. 转换数据，
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
            }
            log.info("同步数据任务执行成功");

        }
        return allSuccess;
    }

    /**
     * 定时任务调用
     * 定时任务调用会传入1
     */
    @Async("customThreadPool")
    public void executeDataMappingTask(String scheduledId, Object... par) {
        executeDataMapping(scheduledId, par);
    }


}