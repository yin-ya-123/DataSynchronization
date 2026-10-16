package com.yin.controller.dataflowdb;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.config.error.ExceptionEnum;
import com.yin.pojo.dataflowdb.ScheduledDetails;
import com.yin.service.dataflowdb.impl.ScheduledDetailsServiceImpl;
import com.yin.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/scheduledDetails")
public class ScheduledDetailsController {

    @Autowired
    private ScheduledDetailsServiceImpl scheduledDetailsService;
    /**
     * 新增定时任务详情
     * @param scheduledDetails 定时任务详情实体
     * @return Result
     */
    @PostMapping("/add")
    @Transactional
    public Result<ScheduledDetails> add(@RequestBody ScheduledDetails scheduledDetails) {
        boolean save = scheduledDetailsService.save(scheduledDetails);
        if (!save) {
            return Result.errInsert(scheduledDetails);
        }
        // TODO: 实现新增逻辑
        return Result.okInsert(scheduledDetails);
    }

    /**
     * 删除定时任务详情
     * @param scheduledDetails 定时任务详情实体
     * @return Result
     */
    @PostMapping("/delete")
    @Transactional
    public Result<ScheduledDetails> delete(@RequestBody ScheduledDetails scheduledDetails) {
        boolean b = scheduledDetailsService.removeById(scheduledDetails.getId());
        if (!b) {
            return Result.errDelete(scheduledDetails);
        }
        return Result.okDelete(scheduledDetails);
    }

    /**
     * 修改定时任务详情
     * @param scheduledDetails 定时任务详情实体
     * @return Result
     */
    @PostMapping("/update")
    @Transactional
    public Result<ScheduledDetails> update(@RequestBody ScheduledDetails scheduledDetails) {
        boolean b = scheduledDetailsService.updateById(scheduledDetails);
        if (!b) {
            return Result.errUpdate(scheduledDetails);
        }
        return Result.okUpdate(scheduledDetails);
    }
    /**
     * 查询
     */
    @PostMapping("/get")
    public Result<ScheduledDetails> get(@RequestBody ScheduledDetails scheduledDetails) {
        LambdaQueryWrapper<ScheduledDetails> scheduledDetailsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        scheduledDetailsLambdaQueryWrapper.eq(ScheduledDetails::getScheduledId, scheduledDetails.getScheduledId());
        ScheduledDetails scheduledDetails1 = scheduledDetailsService.getOne(scheduledDetailsLambdaQueryWrapper);
        return Result.okSel(scheduledDetails1);
    }
    /**
     * 查询列表
     */
    @PostMapping("/list")
    public Result<List<ScheduledDetails>> list(@RequestBody ScheduledDetails scheduledDetails) {
        List<ScheduledDetails> list = scheduledDetailsService.list();
        return Result.okSel(list);
    }
    /**
     * 分页
     */
    @PostMapping("/page")
    public Result<Page<ScheduledDetails>> page(@RequestBody ScheduledDetails scheduledDetails) {
        //转换为page类
        Page<ScheduledDetails> scheduledDetailsPage = PageUtils.convertPage(scheduledDetails.getPagePojo(), ScheduledDetails.class);
        if(scheduledDetailsPage==null){
            return Result.error(ExceptionEnum.CODE_VERIFY_PAGE,null);
        }
        //分页查询
        Page<ScheduledDetails> page = scheduledDetailsService.page(scheduledDetailsPage);
        return Result.okSel(page);
    }
}
