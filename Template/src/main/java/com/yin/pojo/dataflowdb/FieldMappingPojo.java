
/*
字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则实体类
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
@TableName("FIELD_MAPPING")
public class FieldMappingPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    映射记录的唯一标识符，自增主键
    */
    @TableId("MAPPING_ID")
    @JsonProperty
    private String mappingId;

    /*
    任务ID
    */
    @TableField("SCHEDULED_ID")
    @JsonProperty
    private String scheduledId;

    /*
    源数据所在的系统名称
    */
    @TableField("SOURCE_SYSTEM")
    @JsonProperty
    private String sourceSystem;

    /*
    源数据所在的表名
    */
    @TableField("SOURCE_TABLE")
    @JsonProperty
    private String sourceTable;

    /*
    源数据的字段名
    */
    @TableField("SOURCE_FIELD")
    @JsonProperty
    private String sourceField;
    /*
    源数据的字段名
    */
    @TableField("SOURCE_FIELD_type")
    @JsonProperty
    private String sourceFieldType;

    /*
    目标数据所在的系统名称
    */
    @TableField("TARGET_SYSTEM")
    @JsonProperty
    private String targetSystem;

    /*
    目标数据所在的表名
    */
    @TableField("TARGET_TABLE")
    @JsonProperty
    private String targetTable;

    /*
    目标数据的字段名
    */
    @TableField("TARGET_FIELD")
    @JsonProperty
    private String targetField;
    /*
   目标数据的字段名
   */
    @TableField("TARGET_FIELD_type")
    @JsonProperty
    private String targetFieldType;

    /*
    源字段与目标字段的数据类型映射关系
    */
    @TableField("DATA_TYPE_MAPPING")
    @JsonProperty
    private String dataTypeMapping;

    /*
    字段值的转换规则，可包含SQL函数或业务逻辑
    */
    @TableField("MAPPING_RULE")
    @JsonProperty
    private String mappingRule;

    @TableField("PYM")
    @JsonProperty
    private String pym;

    /*
    排序
    */
    @TableField("SORT")
    @JsonProperty
    private String sort;

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