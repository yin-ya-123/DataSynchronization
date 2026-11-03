package com.yin.service.dataflowdb.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.mapper.dataflowdb.SysDatasourceMapper;
import com.yin.pojo.dataflowdb.SysDatasource;
import com.yin.service.dataflowdb.SysDatasourceService;
import com.yin.utils.dataBase.DataSourceUrlBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@DS("master")
public class SysDatasourceServiceImpl extends ServiceImpl<SysDatasourceMapper, SysDatasource>
        implements SysDatasourceService {

    @Autowired
    private SysDatasourceMapper sysDatasourceMapper;

    @Autowired
    private DataSource dataSource;

    @Resource
    private DataSourceCreator hikariDataSourceCreator;

    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Override
    public List<SysDatasource> getAllEnabledDatasources() {
        //根据数据源key查询数据源
        Map<String, DataSource> dataSources = ((DynamicRoutingDataSource) dataSource).getDataSources();
        List<SysDatasource> sysDatasources = sysDatasourceMapper.selectList(null);
        for (SysDatasource sysDatasource : sysDatasources) {
            DataSource dataSource1 = dataSources.get(sysDatasource.getDsKey());
            if (dataSource1 == null) {
                sysDatasource.setDelFlag("1");
            }
        }
        return sysDatasources;
    }

    @Override
    public SysDatasource getDefaultDatasource() {
        //获取默认数据源
        LambdaQueryWrapper<SysDatasource> sysDatasourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDatasourceLambdaQueryWrapper.eq(SysDatasource::getIsDefault, 1);
        return sysDatasourceMapper.selectOne(sysDatasourceLambdaQueryWrapper);
    }

    @Override
    public HikariDataSource createHikariDataSource(SysDatasource datasource) {
        try {
            HikariConfig config = new HikariConfig();
config.setJdbcUrl(DataSourceUrlBuilder.build(datasource));
            config.setUsername(datasource.getUsername());
            config.setPassword(datasource.getPassword());
            config.setDriverClassName(datasource.getDriverClass());
            // 与 addOrUpdateDatasource 保持一致，避免"测试连接通过但正式任务失败"
            config.setPoolName(datasource.getDsKey());
            config.setMaximumPoolSize(50);
            config.setMinimumIdle(3);
            config.setConnectionTimeout(15000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(900000);
            config.setValidationTimeout(5000);
            config.setLeakDetectionThreshold(600000);

            return new HikariDataSource(config);
        } catch (Exception e) {
            log.error("数据源配置错误: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void loadAllDatasources() {
        log.info("开始数据库动态配置添加");
        // 查询所有启用的数据源配置
        List<SysDatasource> datasources = sysDatasourceMapper.selectList(null);
        DynamicRoutingDataSource dynamicDataSource = (DynamicRoutingDataSource) dataSource;
        dynamicRoutingDataSource = dynamicDataSource;
        for (SysDatasource ds : datasources) {
            if (ds.getStatus() == 1) { // 只添加启用状态的数据源
                if (ds.getDsKey().equals("master") || ds.getDsKey().equals("initMysql")) {
                    continue;
                }
                // 移除已存在的数据源
                dynamicDataSource.removeDataSource(ds.getDsKey());
                log.info("移除数据源成功: {}", ds.getDsKey());
                addOrUpdateDatasource(ds);
                log.info("添加数据源成功: {}", ds.getDsKey());
            }
        }
    }

//    @Override
//    public void addOrUpdateDatasource(SysDatasource datasource) {
//        // 验证数据源是否可用
//        try (HikariDataSource tempDs = createHikariDataSource(datasource);
//             Connection connection = tempDs.getConnection()) {
//            // 连接成功，添加或更新数据源
//            DataSourceProperty property = new DataSourceProperty();
//            property.setUrl(datasource.getUrl());
//            property.setUsername(datasource.getUsername());
//            property.setPassword(datasource.getPassword());
//            property.setDriverClassName(datasource.getDriverClass());
//            DataSource dataSource = hikariDataSourceCreator.createDataSource(property);
//            dynamicRoutingDataSource.addDataSource(datasource.getDsKey(), dataSource);
//            // 如果是默认数据源，设置为主数据源
//            if (datasource.getIsDefault() == 1) {
//                dynamicRoutingDataSource.setPrimary(datasource.getDsKey());
//            }
//        } catch (Exception e) {
//            log.error("数据源配置无效，无法连接: {}", e.getMessage());
//        }
//    }
    @Override
    public void addOrUpdateDatasource(SysDatasource datasource) {
        HikariDataSource newDs = null;
        try {
            // 1. 创建带完整连接池配置的 HikariDataSource
            HikariConfig config = new HikariConfig();
            // 把 connection_params 里的附加参数按库类型拼接到 url 后面
            config.setJdbcUrl(DataSourceUrlBuilder.build(datasource));
            config.setUsername(datasource.getUsername());
            config.setPassword(datasource.getPassword());
            config.setDriverClassName(datasource.getDriverClass());
            config.setPoolName(datasource.getDsKey());   // 池名，方便日志排查

            // 连接池核心参数
            config.setMaximumPoolSize(50);               // 根据数据库承受能力调整
            config.setMinimumIdle(3);
            config.setConnectionTimeout(15000);          // 获取连接超时 15 秒（取连接失败要快速暴露）
            config.setIdleTimeout(600000);               // 空闲 10 分钟回收
            config.setMaxLifetime(900000);               // 连接最长存活 15 分钟
            config.setValidationTimeout(5000);
            config.setLeakDetectionThreshold(600000);     // 泄漏检测 10 分钟（源查询耗时1~5分钟，30s会误报）

            // 2. 先创建临时实例验证连接
            HikariDataSource tempDs = new HikariDataSource(config);
            try (Connection connection = tempDs.getConnection()) {
                // 连接成功，tempDs 可以作为正式数据源使用
                newDs = tempDs;
            } catch (Exception e) {
                tempDs.close();
                throw e;
            }

            // 3. 注册到动态路由
            dynamicRoutingDataSource.addDataSource(datasource.getDsKey(), newDs);

            // 4. 如果是默认数据源，设置为主数据源
            if (datasource.getIsDefault() == 1) {
                dynamicRoutingDataSource.setPrimary(datasource.getDsKey());
            }

        } catch (Exception e) {
            log.error("数据源配置无效，无法连接: {}", e.getMessage());
            if (newDs != null) {
                newDs.close();
            }
        }
    }

    @Override
    public void removeDatasource(String dsKey) {
        dynamicRoutingDataSource.removeDataSource(dsKey);
    }

    /**
     * 重建指定数据源的连接池（用于连接池僵死时的自愈）。
     * 复用已有的 removeDataSource + addDataSource 能力。
     */
    public void rebuildDataSource(String dsKey) {
        SysDatasource ds = sysDatasourceMapper.selectOne(
                new LambdaQueryWrapper<SysDatasource>().eq(SysDatasource::getDsKey, dsKey));
        if (ds == null) {
            log.warn("重建数据源失败: 未找到配置 dsKey={}", dsKey);
            return;
        }
        DynamicRoutingDataSource routing = (DynamicRoutingDataSource) dataSource;
        routing.removeDataSource(dsKey);
        log.info("已移除数据源 {}, 开始重建", dsKey);
        addOrUpdateDatasource(ds);
        log.info("数据源 {} 重建完成", dsKey);
    }


}
