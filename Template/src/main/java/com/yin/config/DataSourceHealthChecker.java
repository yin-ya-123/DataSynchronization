package com.yin.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.yin.service.dataflowdb.impl.SysDatasourceServiceImpl;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源连接池健康检查器。
 *
 * 判定僵死：连续 STUCK_THRESHOLD 次检测到"空闲连接为 0 且有线程在等待连接"，
 * 即认为该池已无法正常提供连接 -> 重建该数据源的连接池（removeDataSource + addDataSource）。
 */
@Slf4j
@Component
public class DataSourceHealthChecker {

    /** 连续几次判定僵死才重建，避免误判 */
    private static final int STUCK_THRESHOLD = 3;
    /** 检查间隔（毫秒），默认 30 秒 */
    private static final long CHECK_INTERVAL = 30_000L;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SysDatasourceServiceImpl sysDatasourceService;

    private final Map<String, Integer> stuckCounter = new java.util.concurrent.ConcurrentHashMap<>();

    @Scheduled(fixedDelay = CHECK_INTERVAL, initialDelay = CHECK_INTERVAL)
    public void check() {
        if (!(dataSource instanceof DynamicRoutingDataSource)) {
            return;
        }
        DynamicRoutingDataSource routing = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> map = routing.getDataSources();
        if (map == null) {
            return;
        }
        for (Map.Entry<String, DataSource> e : map.entrySet()) {
            String dsKey = e.getKey();
            if (!(e.getValue() instanceof HikariDataSource)) {
                continue;
            }
            HikariDataSource hds = (HikariDataSource) e.getValue();
            HikariPoolMXBean mx;
            try {
                mx = hds.getHikariPoolMXBean();
            } catch (Exception ex) {
                continue;
            }
            if (mx == null) {
                continue;
            }
            int idle = mx.getIdleConnections();
            int awaiting = mx.getThreadsAwaitingConnection();
            if (idle == 0 && awaiting > 0) {
                int n = stuckCounter.merge(dsKey, 1, Integer::sum);
                log.warn("数据源 {} 连接池疑似僵死: 空闲连接={}, 等待线程={}, 连续次数={}",
                        dsKey, idle, awaiting, n);
                if (n >= STUCK_THRESHOLD) {
                    stuckCounter.put(dsKey, 0);
                    log.error("数据源 {} 连接池僵死，开始重建", dsKey);
                    try {
                        sysDatasourceService.rebuildDataSource(dsKey);
                        log.info("数据源 {} 连接池重建完成", dsKey);
                    } catch (Exception ex) {
                        log.error("数据源 {} 连接池重建失败: {}", dsKey, ex.getMessage(), ex);
                    }
                }
            } else {
                if (stuckCounter.containsKey(dsKey)) {
                    stuckCounter.remove(dsKey);
                    log.info("数据源 {} 连接池恢复正常", dsKey);
                }
            }
        }
    }
}
