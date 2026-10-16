package com.yin.service.jcxx;
/*
用户对应角色信息中间表service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.jcxx.DicUserChargePojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DicUserChargeService extends IService<DicUserChargePojo> {
    //新增
    public Result<?> addDicUserChargePojo(DicUserChargePojo dicUserChargePojo);

    //删除
    public Result<?> deleteDicUserChargePojo(DicUserChargePojo dicUserChargePojo);

    //修改
    public Result<?> updateDicUserChargePojo(DicUserChargePojo dicUserChargePojo);

    //查询
    public Result<List<DicUserChargePojo>> getDicUserChargePojoList(DicUserChargePojo dicUserChargePojo);

    /**
     * 获取用户对应的角色信息
     * @param dicUserChargePojo 角色
     * @return 实体信息
     */
    public Result<List<DicUserChargePojo>> getDicUserChargeAsRole(DicUserChargePojo dicUserChargePojo);

    //分页查询
    public Result<Page<DicUserChargePojo>> getDicUserChargePojoListPage(DicUserChargePojo dicUserChargePojo);
}