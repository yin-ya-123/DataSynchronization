package com.yin.service.jcxx;
/*
角色详细信息service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.jcxx.SysRoleDetailPojo;

import java.util.List;

public interface SysRoleDetailService extends IService<SysRoleDetailPojo> {
    //新增
    public Result<?> addSysRoleDetailPojo(SysRoleDetailPojo sysRoleDetailPojo);

    //新增
    public Result<?> addSysRoleDetailPojoList(List<SysRoleDetailPojo> sysRoleDetailPojo);

    //删除
    public Result<?> deleteSysRoleDetailPojo(SysRoleDetailPojo sysRoleDetailPojo);

    public Result<?> deleteSysRoleDetailPojoByPojo(SysRoleDetailPojo sysRoleDetailPojo);

    //修改
    public Result<?> updateSysRoleDetailPojo(SysRoleDetailPojo sysRoleDetailPojo);

    //查询
    public Result<List<SysRoleDetailPojo>> getSysRoleDetailPojoList(SysRoleDetailPojo sysRoleDetailPojo);

    //分页查询
    public Result<Page<SysRoleDetailPojo>> getSysRoleDetailPojoListPage(SysRoleDetailPojo sysRoleDetailPojo);

}