package com.yin.pojo.dataflowdb;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yin.pojo.PagePojo;
import lombok.Data;

import java.util.List;

@Data
@TableName("scheduled")
public class Scheduled {
    @TableId(value = "cron_id", type = IdType.ASSIGN_ID)
    private String cronId;
    private String cronName;
    private String cronType;
    private String cron;
    private String status;
    @TableLogic(delval = "1", value = "0")
    private String delFlag;
    private String sort;
    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
    @TableField(exist = false)
    @JsonProperty
    private List<SqlParams> sqlParams;
}