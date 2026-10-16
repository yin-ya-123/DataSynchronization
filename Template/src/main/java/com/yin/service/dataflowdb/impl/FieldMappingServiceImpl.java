package com.yin.service.dataflowdb.impl;

/*
字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则serviceImpl接口
*/

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.dataflowdb.FieldMappingMapper;
import com.yin.pojo.dataflowdb.FieldMappingPojo;
import com.yin.pojo.dataflowdb.ScheduledDetails;
import com.yin.pojo.dataflowdb.SqlParams;
import com.yin.pojo.dataflowdb.SysDatasource;
import com.yin.service.dataflowdb.FieldMappingService;
import com.yin.service.dataflowdb.SqlParamsService;
import com.yin.utils.ServiceUtils;
import com.yin.utils.dataBase.MethodService;
import com.yin.utils.dataBase.SqlPlaceholderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@DS("master")
public class FieldMappingServiceImpl extends ServiceImpl<FieldMappingMapper, FieldMappingPojo> implements FieldMappingService {
    @Autowired
    private FieldMappingMapper fieldMappingMapper;
    @Autowired
    private ServiceUtils<FieldMappingPojo> serviceUtils;
    @Autowired
    private MethodService methodService;
    @Autowired
    private ScheduledDetailsServiceImpl scheduledDetailsService;
    @Autowired
    private SysDatasourceServiceImpl sysDatasourceService;
    @Autowired
    private SqlParamsService sqlParamsService;

    /*
    字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则条件参数
    */
    public LambdaQueryWrapper<FieldMappingPojo> fieldMappingLambdaQueryWrapper(FieldMappingPojo fieldMapping) {
        LambdaQueryWrapper<FieldMappingPojo> fieldMappingLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (fieldMapping.getMappingId() != null && !fieldMapping.getMappingId().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getMappingId, fieldMapping.getMappingId());
        }

        if (fieldMapping.getScheduledId() != null && !fieldMapping.getScheduledId().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getScheduledId, fieldMapping.getScheduledId());
        }

        if (fieldMapping.getSourceSystem() != null && !fieldMapping.getSourceSystem().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getSourceSystem, fieldMapping.getSourceSystem());
        }

        if (fieldMapping.getSourceField() != null && !fieldMapping.getSourceField().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getSourceField, fieldMapping.getSourceField());
        }

        if (fieldMapping.getTargetSystem() != null && !fieldMapping.getTargetSystem().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getTargetSystem, fieldMapping.getTargetSystem());
        }
        if (fieldMapping.getTargetField() != null && !fieldMapping.getTargetField().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getTargetField, fieldMapping.getTargetField());
        }
        if (fieldMapping.getDelFlag() != null && !fieldMapping.getDelFlag().isEmpty()) {
            fieldMappingLambdaQueryWrapper.eq(FieldMappingPojo::getDelFlag, fieldMapping.getDelFlag());
        }
        return fieldMappingLambdaQueryWrapper;
    }

    /**
     * 字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则参数校验
     *
     * @param fieldMapping 实体
     * @param type         操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String fieldMappingVerifyS(FieldMappingPojo fieldMapping, SysEnum type) {

        if ((fieldMapping.getMappingId() == null || fieldMapping.getMappingId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "映射记录的唯一标识符，自增主键为空";
        }

        if ((fieldMapping.getScheduledId() == null || fieldMapping.getScheduledId().isEmpty())) {
            return "任务ID为空";
        }

        if ((fieldMapping.getSourceSystem() == null || fieldMapping.getSourceSystem().isEmpty())) {
            return "源数据所在的系统名称为空";
        }

        if ((fieldMapping.getSourceField() == null || fieldMapping.getSourceField().isEmpty())) {
            return "源数据的字段名为空";
        }

        if ((fieldMapping.getTargetSystem() == null || fieldMapping.getTargetSystem().isEmpty())) {
            return "目标数据所在的系统名称为空";
        }
        if ((fieldMapping.getTargetField() == null || fieldMapping.getTargetField().isEmpty())) {
            return "目标数据的字段名为空";
        }
        if ((fieldMapping.getDelFlag() == null || fieldMapping.getDelFlag().isEmpty())) {
            return "删除标志(0 未删除 1 删除)为空";
        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addFieldMappingPojo(FieldMappingPojo fieldMappingPojo) {
        return serviceUtils.insert(fieldMappingMapper, fieldMappingPojo, fieldMappingVerifyS(fieldMappingPojo, SysEnum.SAVE), fieldMappingLambdaQueryWrapper(fieldMappingPojo));
    }

    //删除
    public Result<?> deleteFieldMappingPojo(FieldMappingPojo fieldMappingPojo) {
        return serviceUtils.delete(fieldMappingMapper, fieldMappingPojo, fieldMappingVerifyS(fieldMappingPojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateFieldMappingPojo(FieldMappingPojo fieldMappingPojo) {
        return serviceUtils.update(fieldMappingMapper, fieldMappingPojo, fieldMappingVerifyS(fieldMappingPojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<FieldMappingPojo>> getFieldMappingPojoList(FieldMappingPojo fieldMappingPojo) {
        return serviceUtils.selectList(fieldMappingMapper, fieldMappingLambdaQueryWrapper(fieldMappingPojo));
    }

    //分页查询
    public Result<Page<FieldMappingPojo>> getFieldMappingPojoListPage(FieldMappingPojo fieldMappingPojo) {
        return serviceUtils.selectPage(fieldMappingMapper, FieldMappingPojo.class, fieldMappingPojo.getPagePojo(), fieldMappingLambdaQueryWrapper(fieldMappingPojo));
    }

    /**
     * 数据库映射处理
     */
    public void dataMapping(ScheduledDetails scheduledDetails) {
        log.info("开始执行字段映射处理");
        //查询详细信息
        ScheduledDetails scheduledDetailsServiceById = scheduledDetailsService.getById(scheduledDetails.getId());
        //查询源数据源
        LambdaQueryWrapper<SysDatasource> sysDatasourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDatasourceLambdaQueryWrapper.eq(SysDatasource::getDsKey, scheduledDetailsServiceById.getZsjKs());
        //主数据源数据
        SysDatasource one = sysDatasourceService.getOne(sysDatasourceLambdaQueryWrapper);
        sysDatasourceLambdaQueryWrapper.clear();
        sysDatasourceLambdaQueryWrapper.eq(SysDatasource::getDsKey, scheduledDetailsServiceById.getToKs());
        //目标数据源数据
        SysDatasource two = sysDatasourceService.getOne(sysDatasourceLambdaQueryWrapper);
        //查询参数
        List<SqlParams> list = sqlParamsService.list(new LambdaQueryWrapper<SqlParams>().eq(SqlParams::getScheduledId, scheduledDetailsServiceById.getScheduledId()));
        String newSql = scheduledDetailsServiceById.getZsjSql();
        if (!list.isEmpty()) {
            newSql = SqlPlaceholderUtil.replacePlaceholdersWithSqlParams(scheduledDetails.getZsjSql(), list);
        }
        //获取主数据字段信息
        List<Map<String, Object>> sourceFields = methodService.getFieldInfo(one.getDsKey(), newSql, one.getDsLx(), 1);
        //获取目标数据字段信息
        List<Map<String, Object>> targetFields = methodService.getFieldInfo(two.getDsKey(), scheduledDetailsServiceById.getToSql(), two.getDsLx(), 2);
        // 先作废原先的字段映射关系
        LambdaQueryWrapper<FieldMappingPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FieldMappingPojo::getScheduledId, scheduledDetailsServiceById.getScheduledId());
        int delete = fieldMappingMapper.delete(queryWrapper);
        // 创建新的字段映射关系（仅处理名称相同的字段）
        for (Map<String, Object> sourceField : sourceFields) {
            log.info("开始映射:{}", sourceField);
            // 检查目标表中是否存在相同名称的字段
            String sourceFieldName = sourceField.get("name").toString().toUpperCase();
            String sourceOldFieldName = sourceField.get("name").toString();
            String sourceOldFieldType = sourceField.get("type").toString();
            FieldMappingPojo fieldMappingPojo = getFieldMappingPojo(scheduledDetailsServiceById, sourceOldFieldName, "", sourceOldFieldType, "");
            for (Map<String, Object> targetField : targetFields) {
                String targetFieldName = targetField.get("name").toString().toUpperCase();
                String targetOldFieldName = targetField.get("name").toString();
                String targetOldFieldType = targetField.get("type").toString();
                if (sourceFieldName.equals(targetFieldName)) {
                    fieldMappingPojo = getFieldMappingPojo(scheduledDetailsServiceById, sourceOldFieldName, targetOldFieldName, sourceOldFieldType, targetOldFieldType);
                    break;
                }
            }
            fieldMappingMapper.insert(fieldMappingPojo);
        }
    }

    /**
     * 设置字段映射关系
     *
     * @param scheduledDetails 任务明细数据
     * @param sourceField      数据源字段名称
     * @param targetField      目标字段名称
     * @return 字段映射关系
     */
    private static FieldMappingPojo getFieldMappingPojo(ScheduledDetails scheduledDetails, String sourceField, String targetField, String sourceFiledType, String targetFieldType) {
//        log.info("数据源：{}", JSONObject.toJSONString(scheduledDetails));
        FieldMappingPojo fieldMappingPojo = new FieldMappingPojo();
        fieldMappingPojo.setScheduledId(scheduledDetails.getScheduledId());
        fieldMappingPojo.setSourceSystem(scheduledDetails.getZsjMc());
        fieldMappingPojo.setSourceTable("");
        fieldMappingPojo.setSourceField(sourceField);
        fieldMappingPojo.setSourceFieldType(sourceFiledType);
        fieldMappingPojo.setTargetSystem(scheduledDetails.getToMc());
        fieldMappingPojo.setTargetTable("");
        fieldMappingPojo.setTargetField(targetField);
        fieldMappingPojo.setTargetFieldType(targetFieldType);
        fieldMappingPojo.setDataTypeMapping("");
        fieldMappingPojo.setMappingRule("");
        fieldMappingPojo.setDelFlag("0");
        return fieldMappingPojo;
    }

}