package com.yin.controller.dataflowdb;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.config.error.ExceptionEnum;
import com.yin.pojo.dataflowdb.Scheduled;
import com.yin.pojo.dataflowdb.SqlParams;
import com.yin.service.dataflowdb.impl.SqlParamsServiceImpl;
import com.yin.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sqlParams")
public class SqlParamsController {

    @Autowired
    private SqlParamsServiceImpl sqlParamsService;

    /**
     * 新增SQL参数
     *
     * @param sqlParams SQL参数实体
     * @return Result
     */
    @PostMapping("/add")
    @Transactional
    public Result<SqlParams> add(@RequestBody SqlParams sqlParams) {
        boolean save = sqlParamsService.save(sqlParams);
        if (!save) {
            return Result.errInsert(sqlParams);
        }
        return Result.okInsert(sqlParams);
    }

    /**
     * 删除SQL参数
     *
     * @param sqlParams SQL参数实体
     * @return Result
     */
    @PostMapping("/delete")
    @Transactional
    public Result<SqlParams> delete(@RequestBody SqlParams sqlParams) {
        boolean b = sqlParamsService.removeById(sqlParams.getId());
        if (!b) {
            return Result.errDelete(sqlParams);
        }
        return Result.okDelete(sqlParams);
    }

    /**
     * 修改SQL参数
     *
     * @param sqlParams SQL参数实体
     * @return Result
     */
    @PostMapping("/update")
    @Transactional
    public Result<SqlParams> update(@RequestBody SqlParams sqlParams) {
        boolean b = sqlParamsService.updateById(sqlParams);
        if (!b) {
            return Result.errUpdate(sqlParams);
        }
        return Result.okUpdate(sqlParams);
    }

    /**
     * 查询
     */
    @PostMapping("/get")
    public Result<SqlParams> get(@RequestBody SqlParams sqlParams) {
        LambdaQueryWrapper<SqlParams> wrapper = new LambdaQueryWrapper<>();
        if (sqlParams != null && sqlParams.getId() != null) {
            wrapper.eq(SqlParams::getId, sqlParams.getId());
        }
        if (sqlParams != null && sqlParams.getScheduledId() != null) {
            wrapper.eq(SqlParams::getScheduledId, sqlParams.getScheduledId());
        }
        if (sqlParams != null && sqlParams.getParamName() != null) {
            wrapper.eq(SqlParams::getParamName, sqlParams.getParamName());
        }
        if (sqlParams != null && sqlParams.getIsMr() != null) {
            wrapper.eq(SqlParams::getIsMr, sqlParams.getIsMr());
        }
        SqlParams result = sqlParamsService.getOne(wrapper);
        return Result.okSel(result);
    }

    /**
     * 查询列表
     */
    @PostMapping("/list")
    public Result<List<SqlParams>> list(@RequestBody SqlParams sqlParams) {
        LambdaQueryWrapper<SqlParams> wrapper = new LambdaQueryWrapper<>();
        if (sqlParams.getScheduledId() != null) {
            wrapper.eq(SqlParams::getScheduledId, sqlParams.getScheduledId());
        }
        List<SqlParams> list = sqlParamsService.list(wrapper);
        return Result.okSel(list);
    }

    /**
     * 分页
     */
    @PostMapping("/page")
    public Result<Page<SqlParams>> page(@RequestBody SqlParams sqlParams) {
        //转换为page类
        Page<SqlParams> page = PageUtils.convertPage(sqlParams.getPagePojo(), SqlParams.class);
        if (page == null) {
            return Result.error(ExceptionEnum.CODE_VERIFY_PAGE, null);
        }
        //分页查询
        Page<SqlParams> result = sqlParamsService.page(page);
        return Result.okSel(result);
    }

    /**
     * 進行sql的参数填入，然后进行测试成功的话，插入参数
     */
    @PostMapping("/testSql")
    public Result<?> testSql(@RequestBody Scheduled task) {
        System.out.println(task);
        return sqlParamsService.testSql(task);
    }
}
