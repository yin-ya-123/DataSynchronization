package com.yin.service.dataflowdb.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.common.Result;
import com.yin.mapper.dataflowdb.ScheduledDetailsMapper;
import com.yin.mapper.dataflowdb.ScheduledMapper;
import com.yin.mapper.dataflowdb.SqlParamsMapper;
import com.yin.pojo.dataflowdb.Scheduled;
import com.yin.pojo.dataflowdb.ScheduledDetails;
import com.yin.pojo.dataflowdb.SqlParams;
import com.yin.service.dataflowdb.SqlParamsService;
import com.yin.utils.dataBase.MethodService;
import com.yin.utils.dataBase.SqlPlaceholderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DS("master")
@Slf4j
public class SqlParamsServiceImpl extends ServiceImpl<SqlParamsMapper, SqlParams> implements SqlParamsService {
    @Autowired
    private ScheduledDetailsMapper scheduledDetailsMapper;
    @Autowired
    private ScheduledMapper scheduledMapper;
    @Autowired
    private MethodService methodService;
    @Autowired
    private SqlParamsMapper sqlParamsMapper;


    @Override
    public Result<?> testSql(Scheduled task) {
        if (task == null) return Result.error("任务不存在", null);
        LambdaQueryWrapper<Scheduled> scheduledLambdaQueryWrapper = new LambdaQueryWrapper<>();
        scheduledLambdaQueryWrapper.eq(Scheduled::getCronId, task.getCronId());
        Scheduled scheduled = scheduledMapper.selectOne(scheduledLambdaQueryWrapper);
        if (scheduled == null) {
            log.error("任务不存在");
            return Result.error("任务不存在", null);
        }
        LambdaQueryWrapper<ScheduledDetails> scheduledDetailsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        scheduledDetailsLambdaQueryWrapper.eq(ScheduledDetails::getScheduledId, scheduled.getCronId());
        ScheduledDetails scheduledDetails = scheduledDetailsMapper.selectOne(scheduledDetailsLambdaQueryWrapper);
        if (scheduledDetails == null) {
            log.error("任务明细不存在");
            return Result.error("任务明细不存在", null);
        }
        //进行SQL中占位符的替换
        boolean b = SqlPlaceholderUtil.validateParams(scheduledDetails.getZsjSql(), task.getSqlParams());
        if (!b) {
            log.error("参数不完整");
            List<String> missingParams = SqlPlaceholderUtil.getMissingParams(scheduledDetails.getZsjSql(), task.getSqlParams());
            System.out.println(JSONUtil.toJsonStr(missingParams));
            return Result.error("参数不完整:" + missingParams, missingParams);
        }
        String validateParamsIsNull = SqlPlaceholderUtil.validateParamsIsNull(task.getSqlParams());
        if (!validateParamsIsNull.isEmpty()) {
            log.error("参数为空:{}", validateParamsIsNull);
            return Result.error(validateParamsIsNull, null);
        }
        //进行SQL的执行
        String newSql = SqlPlaceholderUtil.replacePlaceholdersWithSqlParams(scheduledDetails.getZsjSql(), task.getSqlParams());
        log.info("替换参数的SQL:{}", newSql);
        List<Map<String, Object>> maps = methodService.queryData(scheduledDetails.getZsjKs(), newSql);
        if (maps == null || maps.isEmpty()) {
            log.error("SQL执行失败");
            return Result.error("SQL执行失败", null);
        }
        if (null == task.getSqlParams().get(0).getIsMr() && task.getSqlParams().get(0).getIsMr().isEmpty()) {
            log.error("是否为mr参数为空");
            return Result.error("是否为mr参数为空", null);
        }
        //先参数全部删掉，再全部新增
        sqlParamsMapper.delete(new LambdaQueryWrapper<SqlParams>().eq(SqlParams::getScheduledId, scheduled.getCronId()).eq(SqlParams::getIsMr, task.getSqlParams().get(0).getIsMr()));
        //插入
        for (SqlParams sqlParam : task.getSqlParams()) {
            sqlParam.setId("");
            sqlParam.setScheduledId(scheduled.getCronId());
            sqlParam.setIsMr(task.getSqlParams().get(0).getIsMr());
            sqlParamsMapper.insert(sqlParam);
        }
        return Result.ok("SQL执行成功", newSql);
    }


    /**
     * 进行sql的参数替换
     */
}
