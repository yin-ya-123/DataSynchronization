package com.yin.service.dataflowdb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.dataflowdb.Scheduled;
import com.yin.pojo.dataflowdb.SqlParams;

public interface SqlParamsService extends IService<SqlParams> {

    public Result<?> testSql(Scheduled task);
}
