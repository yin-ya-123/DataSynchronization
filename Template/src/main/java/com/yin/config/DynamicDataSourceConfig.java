package com.yin.config;


import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DynamicDataSourceConfig {

    // 动态数据源核心配置属性
    @Autowired
    private DynamicDataSourceProperties properties;

    // 注册动态数据源
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        /**
         *  DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
         *     dataSource.setPrimary(properties.getPrimary());
         *     dataSource.setStrict(properties.getStrict());
         *     dataSource.setStrategy(properties.getStrategy());
         *     dataSource.setP6spy(properties.getP6spy());
         *     dataSource.setSeata(properties.getSeata());
         *     return dataSource;
         *
         */
        // 创建动态路由数据源
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }

}
