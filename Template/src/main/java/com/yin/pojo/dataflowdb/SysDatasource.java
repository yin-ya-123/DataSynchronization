package com.yin.pojo.dataflowdb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_datasource")
public class SysDatasource {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String dsKey;
    private String url;
    private String username;
    private String password;
    private String driverClass;
    private Integer isDefault;
    private Integer status;
    private String dsLx;
    @TableLogic(delval = "1", value = "0")
    private String delFlag;

}
