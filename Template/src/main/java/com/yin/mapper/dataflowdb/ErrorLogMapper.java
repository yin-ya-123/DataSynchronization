package com.yin.mapper.dataflowdb;

/*
简化的错误日志表，记录错误ID、类型、相关数据和问题描述mapper接口
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.dataflowdb.ErrorLogPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorLogMapper extends BaseMapper<ErrorLogPojo> {
}