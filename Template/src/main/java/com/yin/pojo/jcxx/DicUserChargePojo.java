package com.yin.pojo.jcxx;
/*
用户对应角色信息中间表实体类
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
@TableName("dataflowdb.DIC_USER_CHARGE")
public class DicUserChargePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    ID
    */
    @TableId("ID")
    @JsonProperty
    private String id;
    /*
    用户ID
    */
    @TableField("USER_ID")
    @JsonProperty
    private String userId;

    /*
    角色id
    */
    @TableField("ROLE_ID")
    @JsonProperty
    private String roleId;

    /*
    拼音码
    */
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
    创建日期
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
    /**
     * 角色名称
     */
    @TableField(exist = false)
    @JsonProperty
    private String jssm;
    /**
     * 角色对应模块名称
     */
    @TableField(exist = false)
    @JsonProperty
    private String moduleMc;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
}