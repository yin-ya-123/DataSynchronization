package com.yin.common.sys;

/**
 * @author yin
 * @date 2024-11-20
 * @Description
 */
public interface SysEnumInterface {
    /**
     *  错误码
     * @return
     */
    String getSysCode();

    Integer getSysCodeInt();

    /**
     * 获取枚举值
     * @return
     */
    String getSysValue();

}
