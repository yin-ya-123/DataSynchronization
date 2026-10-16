package com.yin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagePojo {

    private Integer pageNumber;

    private Integer pageSize;
}
