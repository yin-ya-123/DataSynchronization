package com.yin.service.jcxx.impl;
/*
角色信息serviceImpl接口
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.jcxx.SysModuleMapper;
import com.yin.mapper.jcxx.SysRoleMapper;
import com.yin.pojo.jcxx.SysModulePojo;
import com.yin.pojo.jcxx.SysRolePojo;
import com.yin.service.jcxx.SysRoleService;
import com.yin.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRolePojo> implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ServiceUtils<SysRolePojo> serviceUtils;
    @Autowired
    private SysModuleMapper sysModuleMapper;

    /*
    角色信息条件参数
    */
    public LambdaQueryWrapper<SysRolePojo> sysRoleLambdaQueryWrapper(SysRolePojo sysRole) {
        LambdaQueryWrapper<SysRolePojo> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (sysRole.getId() != null && !sysRole.getId().isEmpty()) {
            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getId, sysRole.getId());
        }

        if (sysRole.getMenuId() != null && !sysRole.getMenuId().isEmpty()) {
            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getMenuId, sysRole.getMenuId());
        }

        if (sysRole.getDelFlag() != null && !sysRole.getDelFlag().isEmpty()) {
            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getDelFlag, sysRole.getDelFlag());
        }

//        if (sysRole.getCreateTime() != null) {
//            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getCreateTime, sysRole.getCreateTime());
//        }
//
//        if (sysRole.getUpdateTime() != null) {
//            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getUpdateTime, sysRole.getUpdateTime());
//        }

        if (sysRole.getJssm() != null && !sysRole.getJssm().isEmpty()) {
            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getJssm, sysRole.getJssm());
        }

//        if (sysRole.getCreateUser() != null && !sysRole.getCreateUser().isEmpty()) {
//            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getCreateUser, sysRole.getCreateUser());
//        }
//
//        if (sysRole.getUpdateUser() != null && !sysRole.getUpdateUser().isEmpty()) {
//            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getUpdateUser, sysRole.getUpdateUser());
//        }

        if (sysRole.getModuleId() != null && !sysRole.getModuleId().isEmpty()) {
            sysRoleLambdaQueryWrapper.eq(SysRolePojo::getModuleId, sysRole.getModuleId());
        }
        return sysRoleLambdaQueryWrapper;
    }

    /**
     * 角色信息参数校验
     *
     * @param sysRole 实体
     * @param type    操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String sysRoleVerifyS(SysRolePojo sysRole, SysEnum type) {

        if ((sysRole.getId() == null || sysRole.getId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "角色ID为空";
        }

//        if ((sysRole.getMenuId() == null || sysRole.getMenuId().isEmpty())) {
//            return "菜单id为空";
//        }

        if ((sysRole.getDelFlag() == null || sysRole.getDelFlag().isEmpty())) {
            return "删除标志为空";
        }

        if ((sysRole.getJssm() == null || sysRole.getJssm().isEmpty())) {
            return "角色说明为空";
        }

//        if ((sysRole.getCreateUser() == null || sysRole.getCreateUser().isEmpty())) {
//            return "null为空";
//        }
//
//        if ((sysRole.getUpdateUser() == null || sysRole.getUpdateUser().isEmpty())) {
//            return "null为空";
//        }

        if ((sysRole.getModuleId() == null || sysRole.getModuleId().isEmpty())) {
            return "模块ID为空";
        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addSysRolePojo(SysRolePojo sysRolePojo) {
        return serviceUtils.insert(sysRoleMapper, sysRolePojo, sysRoleVerifyS(sysRolePojo, SysEnum.SAVE), sysRoleLambdaQueryWrapper(sysRolePojo));
    }

    //删除
    public Result<?> deleteSysRolePojo(SysRolePojo sysRolePojo) {
        return serviceUtils.delete(sysRoleMapper, sysRolePojo, sysRoleVerifyS(sysRolePojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateSysRolePojo(SysRolePojo sysRolePojo) {
        return serviceUtils.update(sysRoleMapper, sysRolePojo, sysRoleVerifyS(sysRolePojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<SysRolePojo>> getSysRolePojoList(SysRolePojo sysRolePojo) {
        return serviceUtils.selectList(sysRoleMapper, sysRoleLambdaQueryWrapper(sysRolePojo));
    }

    @Override
    public Result<List<SysRolePojo>> getSysRolePojoListAsModule(SysRolePojo sysRolePojo) {
        LambdaQueryWrapper<SysRolePojo> sysRoleWrapper = sysRoleLambdaQueryWrapper(sysRolePojo);
        sysRoleWrapper.orderByAsc(SysRolePojo::getModuleId);
        List<SysRolePojo> sysRolePojos = sysRoleMapper.selectList(sysRoleWrapper);
        for (SysRolePojo rolePojo : sysRolePojos) {
            SysModulePojo sysModulePojo = sysModuleMapper.selectById(rolePojo.getModuleId());
            rolePojo.setSysModulePojo(sysModulePojo);
        }
        return Result.okSel(sysRolePojos);
    }

    //分页查询
    public Result<Page<SysRolePojo>> getSysRolePojoListPage(SysRolePojo sysRolePojo) {
        return serviceUtils.selectPage(sysRoleMapper, SysRolePojo.class, sysRolePojo.getPagePojo(), sysRoleLambdaQueryWrapper(sysRolePojo));
    }

}