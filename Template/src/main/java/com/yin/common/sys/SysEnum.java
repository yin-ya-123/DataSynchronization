package com.yin.common.sys;


/**
 * @author yin
 * @date 2024-11-20
 * @Description 全局枚举使用参数
 */
public enum SysEnum implements SysEnumInterface {
    //定义操作类型枚举
    SAVE("0","新增类型"),
    DELETE("1","删除类型"),
    UPDATE("2","修改类型"),
    TOKEN("3","token")
    ;

    private final String sysCode;
    private final String sysValue;

    SysEnum(String sysCode, String sysValue) {
        this.sysCode = sysCode;
        this.sysValue = sysValue;
    }


    @Override
    public String getSysCode() {
        return sysCode;
    }

    @Override
    public Integer getSysCodeInt() {
        return Integer.valueOf(sysCode);
    }

    @Override
    public String getSysValue() {
        return sysValue;
    }


}
