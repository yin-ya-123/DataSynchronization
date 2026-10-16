package com.yin.service.dataflowdb;

/*
简化的错误日志表，记录错误ID、类型、相关数据和问题描述service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.dataflowdb.ErrorLogPojo;

import java.util.List;


public interface ErrorLogService extends IService<ErrorLogPojo> {
    //新增
    public Result<?> addErrorLogPojo(ErrorLogPojo errorLogPojo);

    //删除
    public Result<?> deleteErrorLogPojo(ErrorLogPojo errorLogPojo);

    //修改
    public Result<?> updateErrorLogPojo(ErrorLogPojo errorLogPojo);

    //查询
    public Result<List<ErrorLogPojo>> getErrorLogPojoList(ErrorLogPojo errorLogPojo);

    //分页查询
    public Result<Page<ErrorLogPojo>> getErrorLogPojoListPage(ErrorLogPojo errorLogPojo);
}