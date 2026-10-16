package com.yin.controller.dataflowdb;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.dataflowdb.ScheduledMapper;
import com.yin.pojo.dataflowdb.Scheduled;
import com.yin.service.dataflowdb.SqlParamsService;
import com.yin.service.dataflowdb.impl.DataMappingServiceImpl;
import com.yin.utils.PageUtils;
import com.yin.utils.task.TaskSchedulerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskSchedulerConfig taskSchedulerConfig;
    @Autowired
    private ScheduledMapper scheduledMapper;
    @Autowired
    private DataMappingServiceImpl dataMappingService;
    @Autowired
    private SqlParamsService sqlParamsService;

    /**
     * 重新加载所有任务
     */
    @GetMapping("/reload")
    public Result<?> reloadTasks() {
        try {
            taskSchedulerConfig.initTasks();
            return Result.ok("任务重新加载成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage(), null);
        }
    }

    /**
     * 新增任务
     */
    @PostMapping("/addScheduled")
    @Transactional
    public Result<?> addTask(@RequestBody Scheduled task) throws Exception {
        scheduledMapper.insert(task);
        return Result.okInsert(task);
    }

    /**
     * 查询任务
     */
    @GetMapping("/getScheduled")
    public Result<List<Scheduled>> getTask() {
        List<Scheduled> scheduleds = scheduledMapper.selectList(null);
        return Result.okSel(scheduleds);
    }

    /**
     * 分页
     */
    @PostMapping("/page")
    public Result<Page<Scheduled>> page(@RequestBody Scheduled scheduled) {
        //转换为page类
        Page<Scheduled> scheduledPage = PageUtils.convertPage(scheduled.getPagePojo(), Scheduled.class);
        if (scheduledPage == null) {
            return Result.error(ExceptionEnum.CODE_VERIFY_PAGE, null);
        }
        //分页查询
        LambdaQueryWrapper<Scheduled> scheduledLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (scheduled.getCronId() != null && !scheduled.getCronId().isEmpty()) {
            scheduledLambdaQueryWrapper.eq(Scheduled::getCronId, scheduled.getCronId());
        }
        if (scheduled.getCronName() != null && !scheduled.getCronName().isEmpty()) {
            scheduledLambdaQueryWrapper.like(Scheduled::getCronName, scheduled.getCronName());
        }
        scheduledLambdaQueryWrapper.orderByAsc(Scheduled::getSort);
        Page<Scheduled> scheduledPage1 = scheduledMapper.selectPage(scheduledPage, scheduledLambdaQueryWrapper);
        return Result.okSel(scheduledPage1);
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/deleteScheduled")
    @Transactional
    public Result<?> deleteTask(@RequestBody Scheduled task) throws Exception {
        int i = scheduledMapper.deleteById(task.getCronId());
        if (i > 0) {
            taskSchedulerConfig.stopTask(task.getCronId());
            return Result.okDelete(task);
        } else {
            return Result.errDelete(task);
        }
    }

    /**
     * 更新单个任务
     */
    @PostMapping("/updateScheduled")
    @Transactional
    public Result<?> updateTask(@RequestBody Scheduled task) throws Exception {
        scheduledMapper.updateById(task);
        taskSchedulerConfig.updateTask(task);
        return Result.okUpdate(task);
    }

    /**
     * 立即启动任务一次
     */
    @PostMapping("/startScheduled")
    public Result<?> startTask(@RequestBody Scheduled task) throws Exception {
        //立即执行传入的参数不为空则验证参数
        if (null != task.getSqlParams() && !task.getSqlParams().isEmpty()) {
            //立即執行验证参数
            Result<?> result = sqlParamsService.testSql(task);
            if (!result.getCode().equals("200")) {
                return result;
            }
            //有参数的进行执行
            dataMappingService.executeDataMappingTask(task.getCronId(), task.getSqlParams().get(0).getIsMr());
            return Result.ok("任务执行成功", task);
        }
        //无参数的进行执行,
        dataMappingService.executeDataMappingTask(task.getCronId(), "1");
        return Result.ok("任务执行成功", task);
    }
}
