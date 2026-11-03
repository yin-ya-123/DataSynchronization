package com.yin.service.dataflowdb;

import com.yin.pojo.dataflowdb.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据映射服务
 * 根据任务查询出对应的明细，然后根据字段映射进行数据插入
 */
public interface DataMappingService {

    /**
     * 根据任务ID查询任务明细
     *
     * @param scheduledId 任务ID
     * @return 任务明细列表
     */
    List<ScheduledDetails> getScheduledDetailsByTaskId(String scheduledId);

    /**
     * 根据任务ID查询任务
     *
     * @param scheduledId 任务ID
     * @return 任务列表
     */
    List<Scheduled> getScheduledByTaskId(String scheduledId);

    /**
     * 根据任务ID查询默认的参数信息
     * @param scheduledId 任务ID
     * @return 参数信息列表
     */
    List<SqlParams> getSqlParamsByScheduledId(String scheduledId,String isMr);

    /**
     * 根据任务明细查询字段映射关系
     *
     * @param scheduledDetails 任务明细
     * @return 字段映射关系列表
     */
    List<FieldMappingPojo> getFieldMappingByScheduledDetails(ScheduledDetails scheduledDetails);

    /**
     * 根据任务明细查询源数据
     *
     * @param scheduledDetails 任务明细
     * @return 源数据列表
     */
    List<Map<String, Object>> querySourceData(ScheduledDetails scheduledDetails, List<SqlParams> sqlParamsByTaskId);

    /**
     * 根据字段映射关系转换数据
     *
     * @param sourceData    源数据
     * @param fieldMappings 字段映射关系
     * @return 转换后的数据
     */
    List<Map<String, Object>> transformData(List<Map<String, Object>> sourceData, List<FieldMappingPojo> fieldMappings);

    /**
     * 将转换后的数据插入目标表
     *
     * @param targetData       转换后的数据
     * @param scheduledDetails 任务明细
     * @param sysDatasource    字段映射对象
     * @return 插入结果
     */
    boolean insertTargetData(List<Map<String, Object>> targetData, ScheduledDetails scheduledDetails, SysDatasource sysDatasource, List<FieldMappingPojo> fieldMappings);

    /**
     * 执行数据映射流程
     *
     * @param scheduledId 任务ID
     *@param par 参数
     * @return 执行结果
     */
    boolean executeDataMapping(String scheduledId,Object... par);

    /**
     * 将字段映射列表转换为以目标字段名为key的Map
     *
     * @param fieldMappings 字段映射列表
     * @return Map<目标字段名, FieldMappingPojo>
     */
    default Map<String, FieldMappingPojo> convertToTargetFieldMap(List<FieldMappingPojo> fieldMappings) {
        return fieldMappings.stream()
                .filter(mapping -> mapping.getTargetField() != null)
                .collect(Collectors.toMap(
                        FieldMappingPojo::getTargetField,
                        mapping -> mapping,
                        (existing, replacement) -> existing // 如果有重复的目标字段，保留第一个
                ));
    }

    /**
     * 将字段映射列表转换为以目标字段名为key，dataTypeMapping为value的Map
     *
     * @param fieldMappings 字段映射列表
     * @return Map<目标字段名, dataTypeMapping>
     */
    default Map<String, String> convertToDataTypeMappingMap(List<FieldMappingPojo> fieldMappings) {
        return fieldMappings.stream()
                // 过滤掉 targetField 或 dataTypeMapping 为 null 的元素
                .filter(mapping -> mapping.getTargetField() != null && mapping.getDataTypeMapping() != null)
                .collect(Collectors.toMap(
                        FieldMappingPojo::getTargetField,  // key：目标字段名
                        FieldMappingPojo::getDataTypeMapping, // value：数据类型映射
                        (existing, replacement) -> existing // 如果有重复的目标字段，保留第一个
                ));
    }
}