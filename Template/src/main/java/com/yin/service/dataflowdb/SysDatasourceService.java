package com.yin.service.dataflowdb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.pojo.dataflowdb.SysDatasource;
import com.zaxxer.hikari.HikariDataSource;

import java.util.List;

public interface SysDatasourceService extends IService<SysDatasource> {

    // 获取所有启用的数据源
    List<SysDatasource> getAllEnabledDatasources();

    // 获取默认数据源
    SysDatasource getDefaultDatasource();

    // 将数据源配置转换为HikariDataSource
    HikariDataSource createHikariDataSource(SysDatasource datasource);

    // 加载所有数据源到动态数据源中
    void loadAllDatasources();

    // 添加或更新数据源
    void addOrUpdateDatasource(SysDatasource datasource);

    // 移除数据源
    void removeDatasource(String dsKey);
}
