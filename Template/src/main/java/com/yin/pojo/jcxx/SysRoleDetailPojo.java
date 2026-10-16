package com.yin.pojo.jcxx;
/*
角色详细信息实体类
*/

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("dataflowdb.SYS_ROLE_DETAIL")
public class SysRoleDetailPojo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("ID")
    @JsonProperty
    private String id;

    @TableField("ROLE_ID")
    @JsonProperty
    private String roleId;

//    @TableLogic(delval = "1", value = "0")
    @TableField("DEL_FLAG")
    @JsonProperty
    private String delFlag;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @JsonProperty
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    @JsonProperty
    private LocalDateTime updateTime;

    @TableField("JSSM")
    @JsonProperty
    private String jssm;

    @TableField("MENU_ID")
    @JsonProperty
    private String menuId;

    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    @JsonProperty
    private String createUser;

    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    @JsonProperty
    private String updateUser;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
}