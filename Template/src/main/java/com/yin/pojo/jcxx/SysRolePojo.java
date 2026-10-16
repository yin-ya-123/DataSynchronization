package com.yin.pojo.jcxx;
/*
角色信息实体类
*/

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
@TableName("dataflowdb.SYS_ROLE")
public class SysRolePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    角色ID
    */
    @TableId("ID")
    @JsonProperty
    private String id;

    /*
    菜单id
    */
    @TableField("MENU_ID")
    @JsonProperty
    private String menuId;

    /*
    删除标志
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
    修改时间
    */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    @JsonProperty
    private LocalDateTime updateTime;

    /*
    角色说明
    */
    @TableField("JSSM")
    @JsonProperty
    private String jssm;

    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    @JsonProperty
    private String createUser;

    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    @JsonProperty
    private String updateUser;

    @TableField("MODULE_ID")
    @JsonProperty
    private String moduleId;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;

    @TableField(exist = false)
    @JsonProperty
    private SysModulePojo sysModulePojo;

}