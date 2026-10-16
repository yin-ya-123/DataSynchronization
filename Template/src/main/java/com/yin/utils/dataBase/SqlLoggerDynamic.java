package com.yin.utils.dataBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 数据同步任务中使用，替换sql的?
 */
public class SqlLoggerDynamic {

    /**
     * 替换sql语句中的?为实际参数
     * @param sql sql
     * @param map 参数
     * @return 返回新的sql
     */
    public static String fillSql(String sql, Map<String, Object> map) {
        // 提取字段名（SQL 中括号里的部分）
        int startIdx = sql.indexOf('(');
        int endIdx = sql.indexOf(')');
        String fieldPart = sql.substring(startIdx + 1, endIdx);
        String[] fields = fieldPart.split(",");

        // 直接使用数组，避免 ArrayList 扩容开销
        Object[] values = new Object[fields.length];

        // 逐个字段取值
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();
            values[i] = map.get(field);
        }
        // 使用 StringBuilder 单次遍历构建结果
        StringBuilder result = new StringBuilder(sql.length() * 2);
        int sqlLen = sql.length();
        int valueIdx = 0;

        for (int i = 0; i < sqlLen; i++) {
            char c = sql.charAt(i);
            if (c == '?' && valueIdx < values.length) {
                Object val = values[valueIdx++];
                if (val == null) {
                    result.append("NULL");
                } else if (val instanceof Number) {
                    result.append(val);
                } else {
                    result.append('\'');
                    String str = val.toString();
                    int strLen = str.length();
                    for (int j = 0; j < strLen; j++) {
                        char ch = str.charAt(j);
                        if (ch == '\'') {
                            result.append("''");
                        } else {
                            result.append(ch);
                        }
                    }
                    result.append('\'');
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 判断数据是否可以转换为日期 yyyy-MM-dd HH:mm:ss
     * @param arg 待判断的参数
     * @return 是否可以转换为日期
     */
    public static boolean isDateArg(Object arg) {
        if (arg instanceof java.util.Date) return true;
        if (arg instanceof String) {
            try {
                String[] patterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
                for (String pattern : patterns) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        sdf.setLenient(false); // 严格匹配
                        sdf.parse((String) arg);
                        return true;
                    } catch (Exception ignore) {
                        // 尝试下一个格式
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 转换数据到日期字符串
     * @param arg 待转换的参数
     * @param pattern 日期格式
     * @return 转换后的日期字符串
     */
    public static String formatDate(Object arg, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (arg instanceof java.util.Date) {
            return sdf.format((Date) arg);
        }
        return arg.toString();
    }

    public static void main(String[] args) throws Exception {
        String values = "{\"开始时间\":1754755200000,\"证件号码\":\"445381198703170020\",\"慢特病名称\":\"精神分裂症\",\"PSN_NO\":\"44190000133089018036\",\"鉴定医药机构名称\":\"东莞市厚街医院\",\"诊断医生代码\":\"0536\",\"姓名\":\"王蕾\",\"鉴定医药机构编码\":\"H44190100053\",\"诊断医生姓名\":\"陈旭生\",\"UNIQUEID\":18939,\"慢特病编码\":\"M02101\",\"结束时间\":1756137600000}";
        String sql = "INSERT INTO YIN.MI_OPSPDISE_INFO_CS (流水号, 开始时间, 证件号码, 慢特病名称, PSN_NO, 鉴定医药机构名称, 诊断医生代码, 姓名, 鉴定医药机构编码, 诊断医生姓名, UNIQUEID, 慢特病编码, 结束时间) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        String logSql = fillSql(sql, values, 1001); // 1001 → 流水号
//        System.out.println(logSql);
    }
}
