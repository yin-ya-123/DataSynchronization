package com.yin.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.pojo.PagePojo;

/**
 * 分页帮助类
 */
public class PageUtils {

    /**
     * 判断分页参数是否合法
     *
     * @param pagePojo
     * @return
     */
    public static boolean pageFlag(PagePojo pagePojo) {
        if (pagePojo == null) {
            return false;
        }
        if (pagePojo.getPageNumber() != null && pagePojo.getPageSize() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static <T> Page<T> convertPage(PagePojo pagePojo,Class<T> tClass) {
        if (pageFlag(pagePojo)) {
            return new Page<>(pagePojo.getPageNumber(), pagePojo.getPageSize(), tClass.getModifiers());
        }
        return null;
    }
    /**
     * 判断分页参数是否合法
     *
     * @param pagePojo
     * @return
     */
    public static <T> Page<T> pageFlag(PagePojo pagePojo,Class<T> tClass) {
        boolean b = pageFlag(pagePojo);
        if (b) {
            return new Page<>(pagePojo.getPageNumber(), pagePojo.getPageSize(), tClass.getModifiers());
        }
        return null;
    }

}
