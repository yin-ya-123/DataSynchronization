package com.yin.mapper.dataflowdb;

/*
字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则mapper接口
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.dataflowdb.FieldMappingPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FieldMappingMapper extends BaseMapper<FieldMappingPojo> {
}