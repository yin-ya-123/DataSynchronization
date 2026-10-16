package com.yin.service.jcxx.impl;
/*
角色详细信息serviceImpl接口
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.jcxx.SysRoleDetailMapper;
import com.yin.pojo.jcxx.SysRoleDetailPojo;
import com.yin.service.jcxx.SysRoleDetailService;
import com.yin.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysRoleDetailServiceImpl extends ServiceImpl<SysRoleDetailMapper, SysRoleDetailPojo> implements SysRoleDetailService {
    @Autowired
    private SysRoleDetailMapper sysRoleDetailMapper;
    @Autowired
    private ServiceUtils<SysRoleDetailPojo> serviceUtils;

    /*
    角色详细信息条件参数
    */
    public LambdaQueryWrapper<SysRoleDetailPojo> sysRoleDetailLambdaQueryWrapper(SysRoleDetailPojo sysRoleDetail) {
        LambdaQueryWrapper<SysRoleDetailPojo> sysRoleDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (sysRoleDetail.getId() != null && !sysRoleDetail.getId().isEmpty()) {
            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getId, sysRoleDetail.getId());
        }

        if (sysRoleDetail.getRoleId() != null && !sysRoleDetail.getRoleId().isEmpty()) {
            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getRoleId, sysRoleDetail.getRoleId());
        }

        if (sysRoleDetail.getDelFlag() != null && !sysRoleDetail.getDelFlag().isEmpty()) {
            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getDelFlag, sysRoleDetail.getDelFlag());
        }

//        if (sysRoleDetail.getCreateTime() != null) {
//            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getCreateTime, sysRoleDetail.getCreateTime());
//        }
//
//        if (sysRoleDetail.getUpdateTime() != null) {
//            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getUpdateTime, sysRoleDetail.getUpdateTime());
//        }

        if (sysRoleDetail.getJssm() != null && !sysRoleDetail.getJssm().isEmpty()) {
            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getJssm, sysRoleDetail.getJssm());
        }

        if (sysRoleDetail.getMenuId() != null && !sysRoleDetail.getMenuId().isEmpty()) {
            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getMenuId, sysRoleDetail.getMenuId());
        }

//        if (sysRoleDetail.getCreateUser() != null && !sysRoleDetail.getCreateUser().isEmpty()) {
//            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getCreateUser, sysRoleDetail.getCreateUser());
//        }
//
//        if (sysRoleDetail.getUpdateUser() != null && !sysRoleDetail.getUpdateUser().isEmpty()) {
//            sysRoleDetailLambdaQueryWrapper.eq(SysRoleDetailPojo::getUpdateUser, sysRoleDetail.getUpdateUser());
//        }
        return sysRoleDetailLambdaQueryWrapper;
    }

    /**
     * 角色详细信息参数校验
     *
     * @param sysRoleDetail 实体
     * @param type          操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String sysRoleDetailVerifyS(SysRoleDetailPojo sysRoleDetail, SysEnum type) {

        if ((sysRoleDetail.getId() == null || sysRoleDetail.getId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "id为空";
        }

        if ((sysRoleDetail.getRoleId() == null || sysRoleDetail.getRoleId().isEmpty())) {
            return "角色为空";
        }

        if ((sysRoleDetail.getDelFlag() == null || sysRoleDetail.getDelFlag().isEmpty())) {
            return "删除标志为空";
        }

        if ((sysRoleDetail.getJssm() == null || sysRoleDetail.getJssm().isEmpty())) {
            return "角色说明为空";
        }

        if ((sysRoleDetail.getMenuId() == null || sysRoleDetail.getMenuId().isEmpty())) {
            return "菜单id为空";
        }

//        if ((sysRoleDetail.getCreateUser() == null || sysRoleDetail.getCreateUser().isEmpty())) {
//            return "null为空";
//        }
//
//        if ((sysRoleDetail.getUpdateUser() == null || sysRoleDetail.getUpdateUser().isEmpty())) {
//            return "null为空";
//        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addSysRoleDetailPojo(SysRoleDetailPojo sysRoleDetailPojo) {
        return serviceUtils.insert(sysRoleDetailMapper, sysRoleDetailPojo, sysRoleDetailVerifyS(sysRoleDetailPojo, SysEnum.SAVE), sysRoleDetailLambdaQueryWrapper(sysRoleDetailPojo));
    }

    @Override
    public Result<?> addSysRoleDetailPojoList(List<SysRoleDetailPojo> sysRoleDetailPojo) {
        for (SysRoleDetailPojo roleDetailPojo : sysRoleDetailPojo) {
            sysRoleDetailMapper.insert(roleDetailPojo);
        }
        return Result.okInsert("");
    }

    //删除
    public Result<?> deleteSysRoleDetailPojo(SysRoleDetailPojo sysRoleDetailPojo) {
        return serviceUtils.delete(sysRoleDetailMapper, sysRoleDetailPojo, sysRoleDetailVerifyS(sysRoleDetailPojo, SysEnum.DELETE));
    }

    @Override
    public Result<?> deleteSysRoleDetailPojoByPojo(SysRoleDetailPojo sysRoleDetailPojo) {
        LambdaQueryWrapper<SysRoleDetailPojo> sysRoleDetailWrapper = sysRoleDetailLambdaQueryWrapper(sysRoleDetailPojo);
        int delete = sysRoleDetailMapper.delete(sysRoleDetailWrapper);
        if(delete>0){
            return Result.okDelete("");
        }else {
            return Result.errDelete("");
        }
    }

    //修改
    public Result<?> updateSysRoleDetailPojo(SysRoleDetailPojo sysRoleDetailPojo) {
        return serviceUtils.update(sysRoleDetailMapper, sysRoleDetailPojo, sysRoleDetailVerifyS(sysRoleDetailPojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<SysRoleDetailPojo>> getSysRoleDetailPojoList(SysRoleDetailPojo sysRoleDetailPojo) {
        return serviceUtils.selectList(sysRoleDetailMapper, sysRoleDetailLambdaQueryWrapper(sysRoleDetailPojo));
    }

    //分页查询
    public Result<Page<SysRoleDetailPojo>> getSysRoleDetailPojoListPage(SysRoleDetailPojo sysRoleDetailPojo) {
        return serviceUtils.selectPage(sysRoleDetailMapper, SysRoleDetailPojo.class, sysRoleDetailPojo.getPagePojo(), sysRoleDetailLambdaQueryWrapper(sysRoleDetailPojo));
    }

}