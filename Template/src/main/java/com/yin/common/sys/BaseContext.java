package com.yin.common.sys;

public class BaseContext {
    // 使用ThreadLocal来存储用户ID
    private static final ThreadLocal<String> userIdThreadLocal = new ThreadLocal<>();
 
    // 设置用户ID
    public static void setUserId(String userId) {
        userIdThreadLocal.set(userId);
    }
 
    // 获取用户ID
    public static String getUserId() {
        return userIdThreadLocal.get();
    }
 
    // 清除用户ID（通常在请求处理完毕后调用）
    public static void clearUserId() {
        userIdThreadLocal.remove();
    }
}