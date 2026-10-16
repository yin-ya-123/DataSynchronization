package com.yin.controller.jcxx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.pojo.jcxx.SysRolePojo;
import com.yin.service.jcxx.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
角色信息Controller接口
*/
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {
    //角色信息
    @Autowired
    private SysRoleService sysRoleService;

    //新增
    @PostMapping("addSysRole")
    public Result<?> addSysRolePojo(@RequestBody SysRolePojo sysRolePojo) {
        return sysRoleService.addSysRolePojo(sysRolePojo);
    }

    //删除
    @PostMapping("deleteSysRole")
    public Result<?> deleteSysRolePojo(@RequestBody SysRolePojo sysRolePojo) {
        return sysRoleService.deleteSysRolePojo(sysRolePojo);
    }

    //修改
    @PostMapping("updateSysRole")
    public Result<?> updateSysRolePojo(@RequestBody SysRolePojo sysRolePojo) {
        return sysRoleService.updateSysRolePojo(sysRolePojo);
    }

    //查询
    @PostMapping("getSysRoleList")
    public Result<List<SysRolePojo>> getSysRolePojoList(@RequestBody SysRolePojo sysRolePojo) {
        return sysRoleService.getSysRolePojoList(sysRolePojo);
    }

    //查询2
    @PostMapping("getSysRolePojoListAsModule")
    public Result<List<SysRolePojo>> getSysRolePojoListAsModule(@RequestBody SysRolePojo sysRolePojo) {
        return sysRoleService.getSysRolePojoListAsModule(sysRolePojo);
    }

    //分页查询
    @PostMapping("getSysRoleListPage")
    public Result<Page<SysRolePojo>> getSysRolePojoListPage(@RequestBody SysRolePojo sysRolePojo) {
        return sysRoleService.getSysRolePojoListPage(sysRolePojo);
    }
}