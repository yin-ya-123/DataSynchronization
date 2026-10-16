package com.yin.init;

import com.yin.service.dataflowdb.SysDatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动时加载数据源配置
 */
@Component
public class DatasourceInitializer implements CommandLineRunner {

    @Autowired
    private SysDatasourceService sysDatasourceService;

    @Override
    public void run(String... args) throws Exception {
        // 应用启动时加载所有数据源
        sysDatasourceService.loadAllDatasources();
    }
}
