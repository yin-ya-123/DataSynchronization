package com.yin.service.jcxx.impl;
/*
模块信息表serviceImpl接口
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.jcxx.SysModuleMapper;
import com.yin.pojo.jcxx.SysModulePojo;
import com.yin.service.jcxx.SysModuleService;
import com.yin.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SysModuleServiceImpl extends ServiceImpl<SysModuleMapper, SysModulePojo> implements SysModuleService {
    @Autowired
    private SysModuleMapper sysModuleMapper;
    @Autowired
    private ServiceUtils<SysModulePojo> serviceUtils;

    /*
    模块信息表条件参数
    */
    public LambdaQueryWrapper<SysModulePojo> sysModuleLambdaQueryWrapper(SysModulePojo sysModule) {
        LambdaQueryWrapper<SysModulePojo> sysModuleLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (sysModule.getModuleId() != null && !sysModule.getModuleId().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getModuleId, sysModule.getModuleId());
        }

        if (sysModule.getMoudleMc() != null && !sysModule.getMoudleMc().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getMoudleMc, sysModule.getMoudleMc());
        }

        if (sysModule.getMoudlePath() != null && !sysModule.getMoudlePath().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getMoudlePath, sysModule.getMoudlePath());
        }

        if (sysModule.getMoudleSort() != null && !sysModule.getMoudleSort().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getMoudleSort, sysModule.getMoudleSort());
        }

        if (sysModule.getMoudleImageType() != null && !sysModule.getMoudleImageType().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getMoudleImageType, sysModule.getMoudleImageType());
        }

        if (sysModule.getDelFlag() != null && !sysModule.getDelFlag().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getDelFlag, sysModule.getDelFlag());
        }

        if (sysModule.getCreateTime() != null) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getCreateTime, sysModule.getCreateTime());
        }

        if (sysModule.getCreateUser() != null && !sysModule.getCreateUser().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getCreateUser, sysModule.getCreateUser());
        }

        if (sysModule.getUpdateTime() != null) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getUpdateTime, sysModule.getUpdateTime());
        }

        if (sysModule.getUpdateUser() != null && !sysModule.getUpdateUser().isEmpty()) {
            sysModuleLambdaQueryWrapper.eq(SysModulePojo::getUpdateUser, sysModule.getUpdateUser());
        }
        sysModuleLambdaQueryWrapper.orderByAsc(SysModulePojo::getMoudleSort);
        return sysModuleLambdaQueryWrapper;
    }

    /**
     * 模块信息表参数校验
     *
     * @param sysModule 实体
     * @param type      操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String sysModuleVerifyS(SysModulePojo sysModule, SysEnum type) {

        if ((sysModule.getModuleId() == null || sysModule.getModuleId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "模块id为空";
        }

        if ((sysModule.getMoudleMc() == null || sysModule.getMoudleMc().isEmpty())) {
            return "模块名称为空";
        }

        if ((sysModule.getMoudlePath() == null || sysModule.getMoudlePath().isEmpty())) {
            return "模块路径为空";
        }

        if ((sysModule.getDelFlag() == null || sysModule.getDelFlag().isEmpty())) {
            return "删除标志为空";
        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addSysModulePojo(SysModulePojo sysModulePojo) {
        return serviceUtils.insert(sysModuleMapper, sysModulePojo, sysModuleVerifyS(sysModulePojo, SysEnum.SAVE), sysModuleLambdaQueryWrapper(sysModulePojo));
    }

    //删除
    public Result<?> deleteSysModulePojo(SysModulePojo sysModulePojo) {
        return serviceUtils.delete(sysModuleMapper, sysModulePojo, sysModuleVerifyS(sysModulePojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateSysModulePojo(SysModulePojo sysModulePojo) {
        return serviceUtils.update(sysModuleMapper, sysModulePojo, sysModuleVerifyS(sysModulePojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<SysModulePojo>> getSysModulePojoList(SysModulePojo sysModulePojo) {
        return serviceUtils.selectList(sysModuleMapper, sysModuleLambdaQueryWrapper(sysModulePojo));
    }

    //分页查询
    public Result<Page<SysModulePojo>> getSysModulePojoListPage(SysModulePojo sysModulePojo) {
        return serviceUtils.selectPage(sysModuleMapper, SysModulePojo.class, sysModulePojo.getPagePojo(), sysModuleLambdaQueryWrapper(sysModulePojo));
    }

    @Override
    public List<SysModulePojo> getModuleByUserId(String userId) {
        return sysModuleMapper.getModuleByUserId(userId);
    }

}