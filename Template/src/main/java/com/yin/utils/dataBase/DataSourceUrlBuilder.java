package com.yin.utils.dataBase;

import com.yin.pojo.dataflowdb.SysDatasource;
import org.springframework.util.StringUtils;

/**
 * 数据源 URL 拼装器。
 *
 * 职责：把 sys_datasource.connection_params 中的附加连接参数拼接到 url 后面。
 * 分隔符按库类型区分：
 *   - SQL Server : ";"   （jdbc:sqlserver://host:1433;database=db;encrypt=true;...）
 *   - Oracle/MySQL: "?" 起头，已带 "?" 时用 "&"
 */
public final class DataSourceUrlBuilder {

    private DataSourceUrlBuilder() {
    }

    /**
     * 拼装最终使用的 JDBC URL。
     *
     * @param ds 数据源配置
     * @return 最终 JDBC URL；url 为空时返回 null
     */
    public static String build(SysDatasource ds) {
        if (ds == null || !StringUtils.hasText(ds.getUrl())) {
            return ds == null ? null : ds.getUrl();
        }
        String url = ds.getUrl().trim();
        String params = ds.getConnectionParams();
        if (!StringUtils.hasText(params)) {
            return url;
        }
        // 去掉用户可能误填的前导 ? 或 ;
        String p = params.trim();
        while (p.startsWith("?") || p.startsWith(";")) {
            p = p.substring(1);
        }
        if (!StringUtils.hasText(p)) {
            return url;
        }
        return url + separator(ds.getDsLx(), url) + p;
    }

    /**
     * 计算追加参数时使用的分隔符。
     */
    private static String separator(String dsLx, String url) {
        if (isSqlServer(dsLx)) {
            // SQL Server 连接串恒用 ";" 分隔
            return ";";
        }
        // Oracle / MySQL：URL 已带 "?" 时用 "&"，否则用 "?"
        return url.contains("?") ? "&" : "?";
    }

    public static boolean isSqlServer(String dsLx) {
        return dsLx != null && "SQLSERVER".equalsIgnoreCase(dsLx.trim());
    }
}