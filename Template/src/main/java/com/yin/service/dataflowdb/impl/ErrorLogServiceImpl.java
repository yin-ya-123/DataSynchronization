package com.yin.service.dataflowdb.impl;

/*
简化的错误日志表，记录错误ID、类型、相关数据和问题描述serviceImpl接口
*/

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.dataflowdb.ErrorLogMapper;
import com.yin.pojo.dataflowdb.ErrorLogPojo;
import com.yin.service.dataflowdb.ErrorLogService;
import com.yin.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Service
@Slf4j
@DS("master")
public class ErrorLogServiceImpl extends ServiceImpl<ErrorLogMapper, ErrorLogPojo> implements ErrorLogService {
    @Autowired
    private ErrorLogMapper errorLogMapper;
    @Autowired
    private ServiceUtils<ErrorLogPojo> serviceUtils;

    /*
    简化的错误日志表，记录错误ID、类型、相关数据和问题描述条件参数
    */
    public LambdaQueryWrapper<ErrorLogPojo> errorLogLambdaQueryWrapper(ErrorLogPojo errorLog) {
        LambdaQueryWrapper<ErrorLogPojo> errorLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (errorLog.getErrorId() != null && !errorLog.getErrorId().isEmpty()) {
            errorLogLambdaQueryWrapper.eq(ErrorLogPojo::getErrorId, errorLog.getErrorId());
        }
        if (errorLog.getErrorType() != null && !errorLog.getErrorType().isEmpty()) {
            errorLogLambdaQueryWrapper.eq(ErrorLogPojo::getErrorType, errorLog.getErrorType());
        }
        if (errorLog.getScheduledId() != null && !errorLog.getScheduledId().isEmpty()) {
            errorLogLambdaQueryWrapper.eq(ErrorLogPojo::getScheduledId, errorLog.getScheduledId());
        }
        if (errorLog.getDelFlag() != null && !errorLog.getDelFlag().isEmpty()) {
            errorLogLambdaQueryWrapper.eq(ErrorLogPojo::getDelFlag, errorLog.getDelFlag());
        }
        errorLogLambdaQueryWrapper.orderByDesc(ErrorLogPojo::getCreateTime);
        return errorLogLambdaQueryWrapper;
    }

    /**
     * 简化的错误日志表，记录错误ID、类型、相关数据和问题描述参数校验
     *
     * @param errorLog 实体
     * @param type     操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String errorLogVerifyS(ErrorLogPojo errorLog, SysEnum type) {

        if ((errorLog.getErrorId() == null || errorLog.getErrorId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "错误记录唯一标识为空";
        }

        if ((errorLog.getErrorType() == null || errorLog.getErrorType().isEmpty())) {
            return "错误类型分类为空";
        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addErrorLogPojo(ErrorLogPojo errorLogPojo) {
        errorLogPojo.setDelFlag("0");
        return serviceUtils.insert(errorLogMapper, errorLogPojo, errorLogVerifyS(errorLogPojo, SysEnum.SAVE), errorLogLambdaQueryWrapper(errorLogPojo));
    }

    //删除
    public Result<?> deleteErrorLogPojo(ErrorLogPojo errorLogPojo) {
        return serviceUtils.delete(errorLogMapper, errorLogPojo, errorLogVerifyS(errorLogPojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateErrorLogPojo(ErrorLogPojo errorLogPojo) {
        return serviceUtils.update(errorLogMapper, errorLogPojo, errorLogVerifyS(errorLogPojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<ErrorLogPojo>> getErrorLogPojoList(ErrorLogPojo errorLogPojo) {
        return serviceUtils.selectList(errorLogMapper, errorLogLambdaQueryWrapper(errorLogPojo));
    }

    //分页查询
    public Result<Page<ErrorLogPojo>> getErrorLogPojoListPage(ErrorLogPojo errorLogPojo) {
        return serviceUtils.selectPage(errorLogMapper, ErrorLogPojo.class, errorLogPojo.getPagePojo(), errorLogLambdaQueryWrapper(errorLogPojo));
    }

    /**
     * 异常错误日志写入
     * @param e
     * @return
     */
    public Result<Object> errorLogSave(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String errorStackTrace = sw.toString();
        // 获取出错的方法信息
        StackTraceElement[] stackTrace = e.getStackTrace();
        String errorMethod = "";
        if (stackTrace.length > 0) {
            StackTraceElement element = stackTrace[0];
            errorMethod = element.getClassName() + "." + element.getMethodName() +
                    "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
        }
        //失败后进行错误日志插入数据库
        ErrorLogPojo errorLogPojo = new ErrorLogPojo();
        errorLogPojo.setErrorType("运行时异常");
        errorLogPojo.setDelFlag("0");
        errorLogPojo.setErrorDescription("出错方法: " + errorMethod + "\n堆栈信息: " + errorStackTrace);
        errorLogPojo.setErrorData(e.getMessage());
        this.save(errorLogPojo);
        return Result.error(e.getMessage(), null);
    }
}