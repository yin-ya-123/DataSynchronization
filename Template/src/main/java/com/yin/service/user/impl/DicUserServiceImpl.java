package com.yin.service.user.impl;
/*
用户信息表serviceImpl接口
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.user.DicUserMapper;
import com.yin.pojo.user.DicUserPojo;
import com.yin.service.user.DicUserService;
import com.yin.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

@Service
@Slf4j
public class DicUserServiceImpl extends ServiceImpl<DicUserMapper, DicUserPojo> implements DicUserService {

    @Autowired
    private DicUserMapper dicUserMapper;
    @Autowired
    private ServiceUtils<DicUserPojo> serviceUtils;

    /*
    用户信息表条件参数
    */
    public LambdaQueryWrapper<DicUserPojo> dicUserLambdaQueryWrapper(DicUserPojo dicUser) {
        LambdaQueryWrapper<DicUserPojo> dicUserLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (dicUser.getId() != null && !dicUser.getId().isEmpty()) {
            dicUserLambdaQueryWrapper.eq(DicUserPojo::getId, dicUser.getId());
        }

        if (dicUser.getUserkenno() != null && !dicUser.getUserkenno().isEmpty()) {
            dicUserLambdaQueryWrapper.eq(DicUserPojo::getUserkenno, dicUser.getUserkenno());
        }

        if ((dicUser.getXm() != null && !dicUser.getXm().isEmpty()) || (dicUser.getZgh() != null && !dicUser.getZgh().isEmpty())) {
            dicUserLambdaQueryWrapper.like(DicUserPojo::getXm, dicUser.getXm())
                    .or().like(DicUserPojo::getZgh, dicUser.getZgh())
            ;
        }

        if (dicUser.getPassword() != null && !dicUser.getPassword().isEmpty()) {
            dicUserLambdaQueryWrapper.eq(DicUserPojo::getPassword, dicUser.getPassword());
        }

//        if (dicUser.getCreateTime() != null) {
//            dicUserLambdaQueryWrapper.eq(DicUserPojo::getCreateTime, dicUser.getCreateTime());
//        }
//
//        if (dicUser.getUpdateTime() != null) {
//            dicUserLambdaQueryWrapper.eq(DicUserPojo::getUpdateTime, dicUser.getUpdateTime());
//        }

        if (dicUser.getRoleid() != null && !dicUser.getRoleid().isEmpty()) {
            dicUserLambdaQueryWrapper.eq(DicUserPojo::getRoleid, dicUser.getRoleid());
        }

        if (dicUser.getDelFlag() != null && !dicUser.getDelFlag().isEmpty()) {
            dicUserLambdaQueryWrapper.eq(DicUserPojo::getDelFlag, dicUser.getDelFlag());
        }

//        if (dicUser.getCreateUser() != null && !dicUser.getCreateUser().isEmpty()) {
//            dicUserLambdaQueryWrapper.eq(DicUserPojo::getCreateUser, dicUser.getCreateUser());
//        }
//
//        if (dicUser.getUpdateUser() != null && !dicUser.getUpdateUser().isEmpty()) {
//            dicUserLambdaQueryWrapper.eq(DicUserPojo::getUpdateUser, dicUser.getUpdateUser());
//        }
        return dicUserLambdaQueryWrapper;
    }

    /**
     * 用户信息表参数校验
     *
     * @param dicUser 实体
     * @param type    操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String dicUserVerifyS(DicUserPojo dicUser, SysEnum type) {

        if ((dicUser.getId() == null || dicUser.getId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "主键ID为空";
        }

        if ((dicUser.getUserkenno() == null || dicUser.getUserkenno().isEmpty())) {
            return "用户kenno为空";
        }

        if ((dicUser.getZgh() == null || dicUser.getZgh().isEmpty())) {
            return "职工号为空";
        }

        if ((dicUser.getXm() == null || dicUser.getXm().isEmpty())) {
            return "姓名为空";
        }

        if ((dicUser.getPassword() == null || dicUser.getPassword().isEmpty())) {
            return "密码为空";
        }

//        if ((dicUser.getRoleid() == null || dicUser.getRoleid().isEmpty())) {
//            return "角色id为空";
//        }

        if ((dicUser.getDelFlag() == null || dicUser.getDelFlag().isEmpty())) {
            return "删除标志为空";
        }

//        if ((dicUser.getCreateUser() == null || dicUser.getCreateUser().isEmpty())) {
//            return "创建人为空";
//        }
//
//        if ((dicUser.getUpdateUser() == null || dicUser.getUpdateUser().isEmpty())) {
//            return "更新人为空";
//        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addDicUserPojo(DicUserPojo dicUserPojo) {
        //新增用户设置默认值
        dicUserPojo.setPassword("1");
        dicUserPojo.setUserkenno(dicUserPojo.getZgh());
        return serviceUtils.insert(dicUserMapper, dicUserPojo, dicUserVerifyS(dicUserPojo, SysEnum.SAVE), dicUserLambdaQueryWrapper(dicUserPojo));
    }

    //删除
    public Result<?> deleteDicUserPojo(DicUserPojo dicUserPojo) {
        return serviceUtils.delete(dicUserMapper, dicUserPojo, dicUserVerifyS(dicUserPojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateDicUserPojo(DicUserPojo dicUserPojo) {
        return serviceUtils.update(dicUserMapper, dicUserPojo, dicUserVerifyS(dicUserPojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<DicUserPojo>> getDicUserPojoList(DicUserPojo dicUserPojo) {
        return serviceUtils.selectList(dicUserMapper, dicUserLambdaQueryWrapper(dicUserPojo));
    }

    //分页查询
    public Result<Page<DicUserPojo>> getDicUserPojoListPage(DicUserPojo dicUserPojo) {
        return serviceUtils.selectPage(dicUserMapper, DicUserPojo.class, dicUserPojo.getPagePojo(), dicUserLambdaQueryWrapper(dicUserPojo));
    }

}