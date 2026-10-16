package com.yin.service.user;
/*
用户信息表service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.user.DicUserPojo;

import java.util.List;

public interface DicUserService extends IService<DicUserPojo> {
    //新增
    public Result<?> addDicUserPojo(DicUserPojo dicUserPojo);

    //删除
    public Result<?> deleteDicUserPojo(DicUserPojo dicUserPojo);

    //修改
    public Result<?> updateDicUserPojo(DicUserPojo dicUserPojo);

    //查询
    public Result<List<DicUserPojo>> getDicUserPojoList(DicUserPojo dicUserPojo);

    //分页查询
    public Result<Page<DicUserPojo>> getDicUserPojoListPage(DicUserPojo dicUserPojo);
}