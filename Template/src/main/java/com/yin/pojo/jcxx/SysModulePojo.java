package com.yin.pojo.jcxx;
/*
模块信息表实体类
*/

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yin.pojo.PagePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import oracle.sql.BLOB;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@TableName("dataflowdb.SYS_MODULE")
public class SysModulePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId("MODULE_ID")
    @JsonProperty
    private String moduleId;
    @TableField("MOUDLE_MC")
    @JsonProperty
    private String moudleMc;
    @TableField("MOUDLE_PATH")
    @JsonProperty
    private String moudlePath;
    @TableField("MOUDLE_SORT")
    @JsonProperty
    private String moudleSort;
    @TableField("MOUDLE_IMAGE")
    @JsonProperty
    private BLOB moudleImage;
    @TableField("MOUDLE_IMAGE_TYPE")
    @JsonProperty
    private String moudleImageType;
    @TableLogic(delval = "1", value = "0")
    @JsonProperty
    private String delFlag;
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @JsonProperty
    private LocalDateTime createTime;
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    @JsonProperty
    private String createUser;
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    @JsonProperty
    private LocalDateTime updateTime;
    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    @JsonProperty
    private String updateUser;
    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
    @TableField(exist = false)
    @JsonProperty
    private List<MenuPojo> children;
}