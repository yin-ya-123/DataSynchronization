package com.yin.utils.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * 数据库字段信息获取服务
 */
@Component
@Slf4j
public class DBFieldInfoService {

    public enum DbType {
        MYSQL, ORACLE, SQLSERVER
    }

    /**
     * 获取源 SQL 字段信息（只取第一条数据）
     *
     * @param jdbcTemplate 数据库连接对象
     * @param sql          查询语句
     * @return 字段信息
     * @throws SQLException
     */
    public List<Map<String, Object>> getSourceFieldInfo(JdbcTemplate jdbcTemplate, String sql) throws SQLException {
        List<Map<String, Object>> fieldInfoList = new ArrayList<>();
        // 拼接只取一条的 SQL
        String limitSql = appendLimitSql(jdbcTemplate, sql);
//        log.info("查询语句:{}",limitSql);
        // 用 JdbcTemplate 拿连接
        Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        try (PreparedStatement ps = conn.prepareStatement(limitSql);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                Map<String, Object> fieldInfo = new LinkedHashMap<>();
//                fieldInfo.put("name", metaData.getColumnName(i));
                // 获取别名不过去实际列名
                fieldInfo.put("name", metaData.getColumnLabel(i));
                fieldInfo.put("type", metaData.getColumnTypeName(i));
                fieldInfo.put("size", metaData.getColumnDisplaySize(i));
                fieldInfo.put("nullable", metaData.isNullable(i) == ResultSetMetaData.columnNullable);
                fieldInfoList.add(fieldInfo);
            }
        } finally {
            conn.close();
        }
        return fieldInfoList;
    }

    /**
     * 获取目标表字段信息
     *
     * @param jdbcTemplate 数据库连接对象
     * @param dbType       数据库类型
     * @param tableName    表名
     * @param schemaName   数据源配置的 schema 名（表名自带 schema 时优先用表名上的）
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> getTargetTableFieldInfo(JdbcTemplate jdbcTemplate, DbType dbType,
                                                              String tableName, String schemaName) throws SQLException {
        List<Map<String, Object>> fieldInfoList = new ArrayList<>();

        String sql = getTableColumnsQuery(dbType, tableName, schemaName);
//        log.info("查询语句:{}",sql);
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fieldInfo = new LinkedHashMap<>();
                fieldInfo.put("name", rs.getString("COLUMN_NAME"));
                fieldInfo.put("type", rs.getString("DATA_TYPE"));
                if (hasColumn(rs, "DATA_LENGTH")) {
                    fieldInfo.put("size", rs.getInt("DATA_LENGTH"));
                }
                fieldInfoList.add(fieldInfo);
            }
        }
        return fieldInfoList;
    }

    /**
     * 不同数据库的取字段 SQL
     *
     * @param dbType      数据库类型
     * @param tableName   表名（可带 schema 前缀）
     * @param dsSchema    数据源配置的 schema_name（表名自带 schema 时优先用表名上的）
     */
    private String getTableColumnsQuery(DbType dbType, String tableName, String dsSchema) {
        String schemaFromTable = null;
        String pureTableName = tableName;

        // 判断表名是否包含模式名（schema）
        if (tableName != null && tableName.contains(".")) {
            String[] parts = tableName.split("\\.", 2);
            schemaFromTable = parts[0];
            pureTableName = parts[1];
        }
        // 表名自带 schema 优先，否则用数据源配置的 schema_name
        String schema = (schemaFromTable != null && !schemaFromTable.trim().isEmpty())
                ? schemaFromTable.trim()
                : (dsSchema == null ? null : dsSchema.trim());
        boolean hasSchema = schema != null && !schema.isEmpty();

        switch (dbType) {
            case MYSQL:
            case SQLSERVER:
                // MySQL 的 TABLE_SCHEMA 即 database 名；SQL Server 的 TABLE_SCHEMA 即 dbo 等
                return "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH AS DATA_LENGTH " +
                        "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + pureTableName + "'" +
                        (hasSchema ? " AND TABLE_SCHEMA = '" + schema + "'" : "");
            case ORACLE:
                // 指定 schema 时查 ALL_TAB_COLUMNS（可跨 schema 读取），否则保持 USER_TAB_COLUMNS
                if (hasSchema) {
                    return "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH FROM ALL_TAB_COLUMNS " +
                            "WHERE OWNER = '" + schema.toUpperCase() + "' " +
                            "AND TABLE_NAME = '" + pureTableName.toUpperCase() + "'";
                }
                return "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH FROM USER_TAB_COLUMNS " +
                        "WHERE TABLE_NAME = '" + pureTableName.toUpperCase() + "'";
            default:
                throw new IllegalArgumentException("不支持的数据库类型");
        }
    }

    /**
     * 限制取一条记录
     */
    private String appendLimitSql(JdbcTemplate jdbcTemplate, String sql) throws SQLException {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            String dbProductName = conn.getMetaData().getDatabaseProductName().toLowerCase();
            if (dbProductName.contains("mysql")) {
                return sql + " LIMIT 1";
            } else if (dbProductName.contains("oracle")) {
                return "SELECT * FROM (\n" + sql + "\n) WHERE ROWNUM = 1";
            } else if (dbProductName.contains("sql server")) {
                return "SELECT TOP 1 * FROM (\n" + sql + "\n) t";
            }
            return sql;
        }
    }

    /**
     * 判断结果集是否有某列
     */
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnName(i))) {
                return true;
            }
        }
        return false;
    }
}
