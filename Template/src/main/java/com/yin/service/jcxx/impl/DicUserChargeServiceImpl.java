package com.yin.service.jcxx.impl;
/*
用户对应角色信息中间表serviceImpl接口
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.jcxx.DicUserChargeMapper;
import com.yin.pojo.jcxx.DicUserChargePojo;
import com.yin.service.jcxx.DicUserChargeService;
import com.yin.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DicUserChargeServiceImpl extends ServiceImpl<DicUserChargeMapper, DicUserChargePojo> implements DicUserChargeService {
    @Autowired
    private DicUserChargeMapper dicUserChargeMapper;
    @Autowired
    private ServiceUtils<DicUserChargePojo> serviceUtils;

    /*
    用户对应角色信息中间表条件参数
    */
    public LambdaQueryWrapper<DicUserChargePojo> dicUserChargeLambdaQueryWrapper(DicUserChargePojo dicUserCharge) {
        LambdaQueryWrapper<DicUserChargePojo> dicUserChargeLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (dicUserCharge.getUserId() != null && !dicUserCharge.getUserId().isEmpty()) {
            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getUserId, dicUserCharge.getUserId());
        }

        if (dicUserCharge.getRoleId() != null && !dicUserCharge.getRoleId().isEmpty()) {
            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getRoleId, dicUserCharge.getRoleId());
        }

        if (dicUserCharge.getPym() != null && !dicUserCharge.getPym().isEmpty()) {
            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getPym, dicUserCharge.getPym());
        }

        if (dicUserCharge.getSort() != null && !dicUserCharge.getSort().isEmpty()) {
            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getSort, dicUserCharge.getSort());
        }

        if (dicUserCharge.getDelFlag() != null && !dicUserCharge.getDelFlag().isEmpty()) {
            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getDelFlag, dicUserCharge.getDelFlag());
        }

//        if (dicUserCharge.getCreateTime() != null) {
//            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getCreateTime, dicUserCharge.getCreateTime());
//        }
//
//        if (dicUserCharge.getCreateUser() != null && !dicUserCharge.getCreateUser().isEmpty()) {
//            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getCreateUser, dicUserCharge.getCreateUser());
//        }
//
//        if (dicUserCharge.getUpdateTime() != null) {
//            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getUpdateTime, dicUserCharge.getUpdateTime());
//        }
//
//        if (dicUserCharge.getUpdateUser() != null && !dicUserCharge.getUpdateUser().isEmpty()) {
//            dicUserChargeLambdaQueryWrapper.eq(DicUserChargePojo::getUpdateUser, dicUserCharge.getUpdateUser());
//        }
        return dicUserChargeLambdaQueryWrapper;
    }

    /**
     * 用户对应角色信息中间表参数校验
     *
     * @param dicUserCharge 实体
     * @param type          操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String dicUserChargeVerifyS(DicUserChargePojo dicUserCharge, SysEnum type) {

        if ((dicUserCharge.getUserId() == null || dicUserCharge.getUserId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "用户ID为空";
        }

        if ((dicUserCharge.getRoleId() == null || dicUserCharge.getRoleId().isEmpty())) {
            return "角色id为空";
        }

//        if ((dicUserCharge.getPym() == null || dicUserCharge.getPym().isEmpty())) {
//            return "拼音码为空";
//        }

//        if ((dicUserCharge.getSort() == null || dicUserCharge.getSort().isEmpty())) {
//            return "排序为空";
//        }

        if ((dicUserCharge.getDelFlag() == null || dicUserCharge.getDelFlag().isEmpty())) {
            return "删除标志(0 未删除 1 删除)为空";
        }

//        if ((dicUserCharge.getCreateUser() == null || dicUserCharge.getCreateUser().isEmpty())) {
//            return "创建人为空";
//        }
//
//        if ((dicUserCharge.getUpdateUser() == null || dicUserCharge.getUpdateUser().isEmpty())) {
//            return "修改人为空";
//        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addDicUserChargePojo(DicUserChargePojo dicUserChargePojo) {
        return serviceUtils.insert(dicUserChargeMapper, dicUserChargePojo, dicUserChargeVerifyS(dicUserChargePojo, SysEnum.SAVE), dicUserChargeLambdaQueryWrapper(dicUserChargePojo));
    }


    //删除
    public Result<?> deleteDicUserChargePojo(DicUserChargePojo dicUserChargePojo) {
        return serviceUtils.delete(dicUserChargeMapper, dicUserChargePojo, dicUserChargeVerifyS(dicUserChargePojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateDicUserChargePojo(DicUserChargePojo dicUserChargePojo) {
        return serviceUtils.update(dicUserChargeMapper, dicUserChargePojo, dicUserChargeVerifyS(dicUserChargePojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<DicUserChargePojo>> getDicUserChargePojoList(DicUserChargePojo dicUserChargePojo) {
        return serviceUtils.selectList(dicUserChargeMapper, dicUserChargeLambdaQueryWrapper(dicUserChargePojo));
    }

    @Override
    public Result<List<DicUserChargePojo>> getDicUserChargeAsRole(DicUserChargePojo dicUserChargePojo) {
        List<DicUserChargePojo> dicUserChargeAsRole = dicUserChargeMapper.getDicUserChargeAsRole(dicUserChargePojo.getUserId());
        return Result.okSel(dicUserChargeAsRole);
    }

    //分页查询
    public Result<Page<DicUserChargePojo>> getDicUserChargePojoListPage(DicUserChargePojo dicUserChargePojo) {
        return serviceUtils.selectPage(dicUserChargeMapper, DicUserChargePojo.class, dicUserChargePojo.getPagePojo(), dicUserChargeLambdaQueryWrapper(dicUserChargePojo));
    }

}