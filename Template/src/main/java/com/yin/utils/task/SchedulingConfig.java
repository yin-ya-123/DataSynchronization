package com.yin.utils.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling  // 必须添加此注解启用调度功能
public class SchedulingConfig {

    /**
     * 显式定义TaskScheduler的Bean
     */
    @Bean
    public TaskScheduler taskScheduler() {
        // 使用Spring提供的线程池任务调度器
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        
        // 核心线程池大小
        scheduler.setPoolSize(20);
        
        // 线程名称前缀，便于日志排查
        scheduler.setThreadNamePrefix("MyTaskScheduler-");
        
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待任务完成的超时时间
        scheduler.setAwaitTerminationSeconds(30);
        
        // 初始化调度器
        scheduler.initialize();
        
        return scheduler;
    }
}
    