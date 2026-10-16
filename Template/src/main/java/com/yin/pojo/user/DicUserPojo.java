package com.yin.pojo.user;
/*
用户信息表实体类
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
@TableName("dataflowdb.DIC_USER")
public class DicUserPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    主键ID
    */
    @TableId("ID")
    @JsonProperty
    private String id;
    /*
    用户kenno
    */
    @TableField("USERKENNO")
    @JsonProperty
    private String userkenno;
    /*
    职工号
    */
    @TableField("ZGH")
    @JsonProperty
    private String zgh;
    /*
    姓名
    */
    @TableField("XM")
    @JsonProperty
    private String xm;
    /*
    密码
    */
    @TableField("PASSWORD")
    @JsonProperty
    private String password;
    /*
    创建时间
    */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @JsonProperty
    private LocalDateTime createTime;
    /*
    更新时间
    */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    @JsonProperty
    private LocalDateTime updateTime;
    /*
    角色id
    */
    @TableField("ROLEID")
    @JsonProperty
    private String roleid;
    /*
    删除标志
    */
    @TableLogic(delval = "1", value = "0")
    @JsonProperty
    private String delFlag;
    /*
    创建人
    */
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    @JsonProperty
    private String createUser;
    /*
    更新人
    */
    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    @JsonProperty
    private String updateUser;

    @TableField(exist = false)
    @JsonProperty
    private PagePojo pagePojo;
}