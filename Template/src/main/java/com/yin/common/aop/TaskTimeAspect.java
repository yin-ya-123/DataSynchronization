package com.yin.common.aop;

import com.yin.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Aspect 标注是一个切面
 */
@Aspect
@Component
@Slf4j
public class TaskTimeAspect {
    long startTime;
    long endTime;

    /**
     * 获取那些方法使用了这个注解，进行一个联系
     */
    @Pointcut("@annotation(com.yin.common.aop.TaskTime)")
    public void taskTimeAspect()  {

    }
    @Before("taskTimeAspect()")
    public void doBefore(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
//        log.info("doBefore:{}",startTime);
    }
    @After("taskTimeAspect()")
    public void doAfter(JoinPoint joinPoint) {
        endTime = System.currentTimeMillis();
//        log.info("doAfter:{}",endTime);
    }

    /**
     * 切面的业务实现
     * @param joinPoint 连接点
     * @return
     * @throws Throwable
     */
    @Around("taskTimeAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //进行统一耗时返回
        Result proceed = (Result) joinPoint.proceed();
        //给每一个业务进行耗时的返回处理
        long elapsedTime = endTime - startTime;
        proceed.setRTime(new R(elapsedTime/1000+"s"));
//        System.out.println(proceed);
        return proceed;
    }
}
