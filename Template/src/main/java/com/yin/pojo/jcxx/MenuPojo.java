package com.yin.pojo.jcxx;
/*
菜单信息表实体类
*/

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yin.pojo.PagePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@TableName("dataflowdb.MENU")
public class MenuPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    ID
    */
    @TableId("ID")
    @JsonProperty
    private String id;
    /*
    菜单名称
    */
    @TableField("LABEL")
    @JsonProperty
    private String label;
    /*
    是否有子
    */
    @TableField("ISCHILDREN")
    @JsonProperty
    private String isChildren;
    /*
    路由地址
    */
    @TableField("PATH")
    @JsonProperty
    private String path;
    /*
    图标名称
    */
    @TableField("ICON")
    @JsonProperty
    private String icon;
    /*
    父地址ID
    */
    @TableField("PARENTID")
    @JsonProperty
    private String parentId;
    /*
    删除标志(1删除0使用)
    */
    @TableField("DELFLAG")
    @TableLogic(delval = "1", value = "0")
    @JsonProperty
    private String delFlag;
    /*
    模块id
    */
    @TableField("MODULE_ID")
    @JsonProperty
    private String moduleId;
    /*
    菜单等级
    */
    @TableField("MENU_LV")
    @JsonProperty
    private String menuLv;

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
    /*
    排序号
    */
    @TableField("SORT")
    @JsonProperty
    private String sort;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;

    @TableField(exist = false)
    @JsonProperty
    private List<MenuPojo> children;
}