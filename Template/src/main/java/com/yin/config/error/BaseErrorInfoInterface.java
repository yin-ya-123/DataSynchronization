package com.yin.config.error;

/**
 * @description: 服务接口类
 */
public interface BaseErrorInfoInterface {

    /**
     *  错误码
     * @return
     */
    String getResultCode();

    Integer getResultCodeInt();

    /**
     * 错误描述
     * @return
     */
    String getResultMsg();


}