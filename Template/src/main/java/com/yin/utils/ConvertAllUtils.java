package com.yin.utils;

import com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型转换帮助类
 */
public class ConvertAllUtils<T> {

    public static<T> ArrayList<T> convertListAll(List list,Class<T> clazz){
        ArrayList<T> ts = new ArrayList<>();
        for (Object o : list) {
            ts.add(JSONObject.parseObject(JSONObject.toJSONString(o),clazz));
        }
        return ts;
    }
    /**
     * 转换实体类到map
     * @param entity 实体类
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> convertEntityToMap(Object entity) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = entity.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(entity);
            if(fieldName.equals("serialVersionUID")){
                continue;
            }
            map.put(fieldName, (String) value);
        }
        return map;
    }
}
