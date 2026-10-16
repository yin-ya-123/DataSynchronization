package com.yin.controller.dataflowdb;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.pojo.dataflowdb.ErrorLogPojo;
import com.yin.service.dataflowdb.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
简化的错误日志表，记录错误ID、类型、相关数据和问题描述Controller接口
*/
@RestController
@RequestMapping("/errorLog")
public class ErrorLogController {
    //简化的错误日志表，记录错误ID、类型、相关数据和问题描述
    @Autowired
    private ErrorLogService errorLogService;

    //新增
    @PostMapping("addErrorLog")
    public Result<?> addErrorLogPojo(@RequestBody ErrorLogPojo errorLogPojo) {
        return errorLogService.addErrorLogPojo(errorLogPojo);
    }

    //删除
    @PostMapping("deleteErrorLog")
    public Result<?> deleteErrorLogPojo(@RequestBody ErrorLogPojo errorLogPojo) {
        return errorLogService.deleteErrorLogPojo(errorLogPojo);
    }

    //修改
    @PostMapping("updateErrorLog")
    public Result<?> updateErrorLogPojo(@RequestBody ErrorLogPojo errorLogPojo) {
        return errorLogService.updateErrorLogPojo(errorLogPojo);
    }

    //查询
    @PostMapping("getErrorLogList")
    public Result<List<ErrorLogPojo>> getErrorLogPojoList(@RequestBody ErrorLogPojo errorLogPojo) {
        return errorLogService.getErrorLogPojoList(errorLogPojo);
    }

    //分页查询
    @PostMapping("getErrorLogListPage")
    public Result<Page<ErrorLogPojo>> getErrorLogPojoListPage(@RequestBody ErrorLogPojo errorLogPojo) {
        return errorLogService.getErrorLogPojoListPage(errorLogPojo);
    }
}