package com.yin.utils.task;

import com.yin.mapper.dataflowdb.ScheduledMapper;
import com.yin.pojo.dataflowdb.Scheduled;
import com.yin.service.dataflowdb.impl.DataMappingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * @Description
 * @ClassName MyTask
 * @Author User
 * @date 2020.06.07 15:23
 */
@Component
@EnableScheduling
@Slf4j
public class TaskSchedulerConfig implements SchedulingConfigurer {

    @Autowired
    protected ScheduledMapper scheduledMapper;

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private DataMappingServiceImpl dataMappingService;
    // 用于存放正在执行的任务
    private Map<String, ScheduledFuture<?>> scheduledFutures = new HashMap<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        log.info("开始初始化定时任务...");
        initTasks();
    }

    /**
     * 初始化所有启用的任务
     */
    public void initTasks() {
        // 清除所有正在执行的任务
        clearAllTasks();
        // 获取所有启用的任务(状态为1)
        List<Scheduled> tasks = scheduledMapper.selectList(null);
        for (Scheduled task : tasks) {
            try {
                addTask(task);
            } catch (Exception e) {
                log.error("初始化任务失败: {}", task.getCronName(), e);
            }
        }
    }

    /**
     * 添加单个任务
     */
    public void addTask(Scheduled task) throws Exception {
        // 创建Cron任务
        CronTask cronTask = new CronTask(() -> process(task), task.getCron());
        // 调度任务并保存Future对象
        ScheduledFuture<?> future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        scheduledFutures.put(task.getCronId(), future);
        log.info("ID:{} 添加定时任务成功: {}, Cron表达式: {}", task.getCronId(), task.getCronName(), task.getCron());
    }

    /**
     * 停止单个任务
     */
    public void stopTask(String cronId) {
        ScheduledFuture<?> future = scheduledFutures.get(cronId);
        if (future != null) {
            future.cancel(true);
            scheduledFutures.remove(cronId);
            log.info("停止定时任务: {}", cronId);
        }
    }

    /**
     * 清除所有任务
     */
    public void clearAllTasks() {
        Set<String> cronIds = scheduledFutures.keySet();
        for (String cronId : cronIds) {
            stopTask(cronId);
        }
    }

    /**
     * 更新任务
     */
    public void updateTask(Scheduled task) throws Exception {
        // 先停止旧任务
        stopTask(task.getCronId());
        // 添加新任务
        addTask(task);
        log.info("更新定时任务成功: {}", task.getCronName());
    }

    /**
     * 处理任务
     * @param scheduled
     */
    public void process(Scheduled scheduled) {
        if (scheduled.getStatus().equals("0")) {
            log.info("线程:{}开始执行定时任务: {}", Thread.currentThread().getName(), scheduled.getCronName());
            dataMappingService.executeDataMappingTask(scheduled.getCronId(),"1");
        }
    }
}