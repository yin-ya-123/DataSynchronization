
/*
简化的错误日志表，记录错误ID、类型、相关数据和问题描述实体类
*/
package com.yin.pojo.dataflowdb;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yin.pojo.PagePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@TableName("ERROR_LOG")
public class ErrorLogPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    错误记录唯一标识
    */
    @TableId(value = "ERROR_ID", type = IdType.ASSIGN_ID)
    @JsonProperty
    private String errorId;

    /** 任务ID */
    @TableField(value = "scheduled_id")
    private String scheduledId;

    /*
    错误类型分类
    */
    @TableField("ERROR_TYPE")
    @JsonProperty
    private String errorType;

    /*
    发生错误时涉及的数据
    */
    @TableField("ERROR_DATA")
    @JsonProperty
    private String errorData;

    /*
    错误问题的详细描述
    */
    @TableField("ERROR_DESCRIPTION")
    @JsonProperty
    private String errorDescription;

    /*
    删除标志(0 未删除 1 删除)
    */
    @TableLogic(delval = "1", value = "0")
    @JsonProperty
    private String delFlag;

    /*
    创建时间
    */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @JsonProperty
    private LocalDateTime createTime;

    /*
    创建人
    */
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    @JsonProperty
    private String createUser;

    /*
    修改日期
    */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    @JsonProperty
    private LocalDateTime updateTime;

    /*
    修改人
    */
    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    @JsonProperty
    private String updateUser;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
}