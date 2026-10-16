package com.yin.common.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 耗时一些其他统一都有的返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    /**
     * 耗时
     */
    private String time;
}
