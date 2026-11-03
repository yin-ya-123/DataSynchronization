package com.yin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean("customThreadPool")
    public Executor customThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setKeepAliveSeconds(60); // 空闲线程存活时间
        executor.setThreadNamePrefix("myThreadTask-"); // 线程名前缀
        // 拒绝策略：CallerRunsPolicy 会让"提交方线程"（Tomcat 请求线程 / cron 调度线程）去执行这个慢任务，
        // 导致整个服务假死。改为 AbortPolicy：超出容量直接拒绝，由调用方捕获记录，任务快速失败。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 优雅停机：等待在跑的任务结束
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize(); // 初始化
        return executor;
    }
}
