package com.yin.pojo.dataflowdb;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yin.pojo.PagePojo;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sql_params")
public class SqlParams {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String scheduledId;
    private String paramName;
    private String paramValue;
    private String paramType;
    private String description;
    private Date createTime;
    @TableLogic(delval = "1", value = "0")
    private String delFlag;
    @TableField("is_mr")
    private String isMr;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
}
