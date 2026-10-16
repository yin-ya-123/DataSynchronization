package com.yin.service.jcxx;
/*
角色信息service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.jcxx.SysRolePojo;

import java.util.List;

public interface SysRoleService extends IService<SysRolePojo> {
    //新增
    public Result<?> addSysRolePojo(SysRolePojo sysRolePojo);

    //删除
    public Result<?> deleteSysRolePojo(SysRolePojo sysRolePojo);

    //修改
    public Result<?> updateSysRolePojo(SysRolePojo sysRolePojo);

    //查询
    public Result<List<SysRolePojo>> getSysRolePojoList(SysRolePojo sysRolePojo);
    //查询角色信息，顺带吧模块信息带上
    public Result<List<SysRolePojo>> getSysRolePojoListAsModule(SysRolePojo sysRolePojo);
    //分页查询
    public Result<Page<SysRolePojo>> getSysRolePojoListPage(SysRolePojo sysRolePojo);
}