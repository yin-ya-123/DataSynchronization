package com.yin.service.dataflowdb;

/*
字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.dataflowdb.FieldMappingPojo;
import com.yin.pojo.dataflowdb.ScheduledDetails;

import java.util.List;

public interface FieldMappingService extends IService<FieldMappingPojo> {
    //新增
    public Result<?> addFieldMappingPojo(FieldMappingPojo fieldMappingPojo);

    //删除
    public Result<?> deleteFieldMappingPojo(FieldMappingPojo fieldMappingPojo);

    //修改
    public Result<?> updateFieldMappingPojo(FieldMappingPojo fieldMappingPojo);

    //查询
    public Result<List<FieldMappingPojo>> getFieldMappingPojoList(FieldMappingPojo fieldMappingPojo);

    //分页查询
    public Result<Page<FieldMappingPojo>> getFieldMappingPojoListPage(FieldMappingPojo fieldMappingPojo);

    //初始化映射
    public void dataMapping(ScheduledDetails scheduledDetails);
}