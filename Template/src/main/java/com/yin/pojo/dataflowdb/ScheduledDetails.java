package com.yin.pojo.dataflowdb;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yin.pojo.PagePojo;
import lombok.Data;

/**
 * 表名：scheduled_detatil
 * 描述：任务明细表
 */
@Data
@TableName(value = "scheduled_detatil")
public class ScheduledDetails {

    /** 主键 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 主数据名称 */
    @TableField(value = "zsj_mc")
    private String zsjMc;

    /** 主数据sql */
    @TableField(value = "zsj_sql")
    private String zsjSql;

    /** 主数据库标记 */
    @TableField(value = "zsj_ks")
    private String zsjKs;

    /** 对方表名称 */
    @TableField(value = "to_mc")
    private String toMc;

    /** 对方sql */
    @TableField(value = "to_sql")
    private String toSql;

    /** 对方数据库标记 */
    @TableField(value = "to_ks")
    private String toKs;

    /** 操作类型 */
    @TableField(value = "cz_lx")
    private String czLx;

    /** 任务ID */
    @TableField(value = "scheduled_id")
    private String scheduledId;

    @TableLogic(delval = "1", value = "0")
    private String delFlag;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
}