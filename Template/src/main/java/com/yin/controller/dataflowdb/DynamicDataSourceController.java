package com.yin.controller.dataflowdb;

import com.yin.common.Result;
import com.yin.pojo.dataflowdb.SysDatasource;
import com.yin.utils.dataBase.MethodService;
import com.yin.service.dataflowdb.SysDatasourceService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/datasource")

public class DynamicDataSourceController {

    @Autowired
    private SysDatasourceService sysDatasourceService;
    @Autowired
    private MethodService methodService;

    /**
     * 获取所有数据源配置
     */
    @GetMapping("/list")
    public Result<List<SysDatasource>> listAll() {
        if (sysDatasourceService.list() == null) {
            return Result.errSel(null);
        }
        if (sysDatasourceService.list().isEmpty()) {
            return Result.errSel(null);
        }
        return Result.okSel(sysDatasourceService.list());
    }

    /**
     * 获取所有启用的数据源配置
     */
    @GetMapping("/listEnabled")
    public Result<List<SysDatasource>> listAllEnabled() {
//        System.out.println("123");
        List<SysDatasource> allEnabledDatasources = sysDatasourceService.getAllEnabledDatasources();
        if (allEnabledDatasources == null) {
            return Result.errSel(null);
        }
        if (allEnabledDatasources.isEmpty()) {
            return Result.errSel(null);
        }
        return Result.okSel(allEnabledDatasources);
    }

    /**
     * 添加数据源
     */
    @PostMapping("/dataSource")
    public Result<?> addDataSource(@RequestBody SysDatasource datasource) {
        return Result.res(null, sysDatasourceService.saveOrUpdate(datasource));
    }


    /**
     * 刷新数据源，重新加载所有数据源
     */
    @GetMapping("/refresh")
    public Result<?> refresh() {
        sysDatasourceService.loadAllDatasources();
        return Result.res(null, true);
    }

    /**
     * 测试连接
     */
    @PostMapping("/dataSourceCon")
    public Result<?> testConnection(@RequestBody SysDatasource datasource) {
        // 验证数据源是否可用
        try (HikariDataSource tempDs = sysDatasourceService.createHikariDataSource(datasource);
             Connection connection = tempDs.getConnection()) {
            return Result.ok("连接成功", null);
        } catch (Exception e) {
            return Result.error("连接失败", null);
        }
    }

    /**
     * 测试sql查询
     */
    @PostMapping("/dataSourceSql")
    public Result<?> dataSourceSql(@RequestBody Map<String, String> datasource) {
        String ds = datasource.get("ds");
        String sql = datasource.get("sql");
        List<Map<String, Object>> maps = methodService.queryData(ds, sql);
        return Result.res(maps, true);
    }

}
