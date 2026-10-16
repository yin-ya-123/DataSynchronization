package com.yin.utils.dataBase;

import com.yin.pojo.dataflowdb.SqlParams;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlPlaceholderUtil {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("#\\{([^}]*)\\}");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_ONLY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 使用SqlParams列表替换SQL中的占位符
     *
     * @param sql        原始SQL语句
     * @param paramsList SqlParams参数列表
     * @return 替换后的SQL
     */
    public static String replacePlaceholdersWithSqlParams(String sql, List<SqlParams> paramsList) {
        if (isEmpty(sql)) {
            return sql;
        }

        if (paramsList == null || paramsList.isEmpty()) {
            return sql;
        }

        // 将SqlParams列表转换为Map
        Map<String, Object> paramMap = new HashMap<>();
        for (SqlParams param : paramsList) {
            if (param.getParamName() != null && !param.getParamName().isEmpty()) {
                // 根据paramType转换参数值
                Object value = convertParamValue(param.getParamValue(), param.getParamType());
                paramMap.put(param.getParamName(), value);
            }
        }

        return replaceByMap(sql, paramMap);
    }

    /**
     * 使用SqlParams列表替换SQL中的占位符（支持多个相同占位符）
     *
     * @param sql        原始SQL语句
     * @param paramsList SqlParams参数列表
     * @param escapeSql  是否转义SQL特殊字符
     * @return 替换后的SQL
     */
    public static String replacePlaceholdersWithSqlParams(String sql, List<SqlParams> paramsList, boolean escapeSql) {
        if (isEmpty(sql)) {
            return sql;
        }

        if (paramsList == null || paramsList.isEmpty()) {
            return sql;
        }

        Map<String, Object> paramMap = new HashMap<>();
        for (SqlParams param : paramsList) {
            if (param.getParamName() != null && !param.getParamName().isEmpty()) {
                Object value = convertParamValue(param.getParamValue(), param.getParamType(), escapeSql);
                paramMap.put(param.getParamName(), value);
            }
        }

        return replaceByMap(sql, paramMap);
    }

    /**
     * 使用Map参数替换占位符
     */
    public static String replaceByMap(String sql, Map<String, Object> paramMap) {
        if (isEmpty(sql) || paramMap == null || paramMap.isEmpty()) {
            return sql;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(sql);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String placeholderName = matcher.group(1).trim();
            Object value = paramMap.get(placeholderName);
            String replacement = value != null ? value.toString() : matcher.group(0);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 转换参数值（根据参数类型）
     */
    private static Object convertParamValue(String paramValue, String paramType) {
        return convertParamValue(paramValue, paramType, true);
    }

    /**
     * 转换参数值（根据参数类型）
     *
     * @param paramValue 参数值
     * @param paramType  参数类型
     * @param escapeSql  是否转义SQL
     */
    private static Object convertParamValue(String paramValue, String paramType, boolean escapeSql) {
        if (paramValue == null) {
            return "NULL";
        }

        // 判断参数类型
        if (paramType == null || paramType.isEmpty()) {
            // 默认当作字符串处理
            return formatString(paramValue, escapeSql);
        }

        switch (paramType.trim().toLowerCase()) {
            case "字符":
                return formatString(paramValue, escapeSql);
            case "数字":
            case "整数":
            case "int":
            case "integer":
            case "long":
            case "number":
                try {
                    // 尝试转换为数字
                    if (paramValue.contains(".")) {
                        return paramValue; // 保留小数
                    } else {
                        return paramValue;
                    }
                } catch (Exception e) {
                    return formatString(paramValue, escapeSql);
                }

            case "日期":
            case "日期(天)":
            case "日期(时分秒)":
            case "date":
            case "datetime":
            case "timestamp":
                return formatDate(paramValue);

            case "布尔":
            case "boolean":
                return formatBoolean(paramValue);

            case "json":
            case "对象":
                return formatJson(paramValue, escapeSql);

            default:
                // 默认当作字符串处理
                return paramValue;
        }
    }

    /**
     * 格式化字符串
     */
    private static String formatString(String value, boolean escapeSql) {
        if (value == null) {
            return "NULL";
        }
        if (escapeSql) {
            return "'" + escapeSql(value) + "'";
        } else {
            return "'" + value + "'";
        }
    }

    /**
     * 格式化日期
     */
    private static String formatDate(String value) {
        if (value == null || value.isEmpty()) {
            return "NULL";
        }
        try {
            // 尝试解析日期
            // 这里简单处理，实际可以根据需要扩展
            return "'" + value + "'";
        } catch (Exception e) {
            return "'" + value + "'";
        }
    }

    /**
     * 格式化布尔值
     */
    private static String formatBoolean(String value) {
        if (value == null) {
            return "NULL";
        }
        String lowerValue = value.trim().toLowerCase();
        if ("true".equals(lowerValue) || "1".equals(lowerValue) || "是".equals(lowerValue)) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 格式化JSON/对象
     */
    private static String formatJson(String value, boolean escapeSql) {
        if (value == null) {
            return "NULL";
        }
        if (escapeSql) {
            return "'" + escapeSql(value) + "'";
        } else {
            return "'" + value + "'";
        }
    }

    /**
     * 转义SQL字符串中的特殊字符
     */
    private static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''")
                .replace("\\", "\\\\");
    }

    /**
     * 解析SQL中的占位符
     */
    public static List<String> parsePlaceholders(String sql) {
        List<String> placeholders = new ArrayList<>();
        if (isEmpty(sql)) {
            return placeholders;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(sql);
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            if (!name.isEmpty()) {
                placeholders.add(name);
            }
        }
        return placeholders;
    }

    /**
     * 获取去重后的占位符列表
     */
    public static Set<String> getUniquePlaceholders(String sql) {
        return new HashSet<>(parsePlaceholders(sql));
    }

    /**
     * 获取占位符总数
     */
    public static int countPlaceholders(String sql) {
        return parsePlaceholders(sql).size();
    }

    /**
     * 验证参数是否存在value为空了
     */
    public static String validateParamsIsNull(List<SqlParams> paramsList) {
        StringBuilder res = new StringBuilder();
        if (paramsList == null || paramsList.isEmpty())
            return "参数为空";
        for (SqlParams sqlParams : paramsList) {
            if (sqlParams.getParamValue().isEmpty()) {
                res.append("参数为空:" + sqlParams.getParamName());
            }
        }
        return res.toString();
    }

    /**
     * 验证参数是否完整
     */
    public static boolean validateParams(String sql, List<SqlParams> paramsList) {
        if (isEmpty(sql)) {
            return false;
        }

        if (paramsList == null || paramsList.isEmpty()) {
            return false;
        }

        // 获取SQL中的所有占位符
        Set<String> placeholders = getUniquePlaceholders(sql);

        // 获取参数名称集合
        Set<String> paramNames = paramsList.stream()
                .map(SqlParams::getParamName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 检查所有占位符是否都有对应的参数
        for (String placeholder : placeholders) {
            if (!paramNames.contains(placeholder)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取缺失的参数
     */
    public static List<String> getMissingParams(String sql, List<SqlParams> paramsList) {
        List<String> missingParams = new ArrayList<>();
        if (isEmpty(sql)) {
            return missingParams;
        }

        Set<String> placeholders = getUniquePlaceholders(sql);
        Set<String> paramNames = paramsList.stream()
                .map(SqlParams::getParamName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (String placeholder : placeholders) {
            if (!paramNames.contains(placeholder)) {
                missingParams.add(placeholder);
            }
        }

        return missingParams;
    }

    /**
     * 判断字符串是否为空
     */
    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}