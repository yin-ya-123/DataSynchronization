package com.yin.config.error;

import com.yin.common.Result;
import com.yin.pojo.dataflowdb.ErrorLogPojo;
import com.yin.service.dataflowdb.ErrorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

//全局异常处理，捕获controller层异常
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private ErrorLogService errorLogService;
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result<?> bizException(HttpServletRequest req, BizException e) {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return Result.error(e.getErrorMsg(), null);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<?> exception(HttpServletRequest req, Exception e) {
//        log.error("发生运行时异常！原因是：{}", e.getMessage());
        // 获取异常堆栈信息
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
        errorLogService.save(errorLogPojo);
        log.error("发生运行时异常！原因是：{}", e.getMessage());
        return Result.error(e.getMessage(), null);
    }
}
