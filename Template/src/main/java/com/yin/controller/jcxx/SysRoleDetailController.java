package com.yin.controller.jcxx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.pojo.jcxx.SysRoleDetailPojo;
import com.yin.service.jcxx.SysRoleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
角色详细信息Controller接口
*/
@RestController
@RequestMapping("/sysRoleDetail")
public class SysRoleDetailController {

    //角色详细信息
    @Autowired
    private SysRoleDetailService sysRoleDetailService;

    //新增
    @PostMapping("addSysRoleDetail")
    public Result<?> addSysRoleDetailPojo(@RequestBody SysRoleDetailPojo sysRoleDetailPojo) {
        return sysRoleDetailService.addSysRoleDetailPojo(sysRoleDetailPojo);
    }
    //批量新增
    @PostMapping("/addSysRoleDetailPojoList")
    public Result<?> addSysRoleDetailPojoList(@RequestBody List<SysRoleDetailPojo> sysRoleDetailPojo){
        return sysRoleDetailService.addSysRoleDetailPojoList(sysRoleDetailPojo);
    }

    //删除
    @PostMapping("deleteSysRoleDetail")
    public Result<?> deleteSysRoleDetailPojo(@RequestBody SysRoleDetailPojo sysRoleDetailPojo) {
        return sysRoleDetailService.deleteSysRoleDetailPojo(sysRoleDetailPojo);
    }

    @PostMapping("/deleteSysRoleDetailPojoByPojo")
    public Result<?> deleteSysRoleDetailPojoByPojo(@RequestBody SysRoleDetailPojo sysRoleDetailPojo) {
        return sysRoleDetailService.deleteSysRoleDetailPojoByPojo(sysRoleDetailPojo);
    }

    //修改
    @PostMapping("updateSysRoleDetail")
    public Result<?> updateSysRoleDetailPojo(@RequestBody SysRoleDetailPojo sysRoleDetailPojo) {
        return sysRoleDetailService.updateSysRoleDetailPojo(sysRoleDetailPojo);
    }

    //查询
    @PostMapping("getSysRoleDetailList")
    public Result<List<SysRoleDetailPojo>> getSysRoleDetailPojoList(@RequestBody SysRoleDetailPojo sysRoleDetailPojo) {
        return sysRoleDetailService.getSysRoleDetailPojoList(sysRoleDetailPojo);
    }
    //分页查询
    @PostMapping("getSysRoleDetailListPage")
    public Result<Page<SysRoleDetailPojo>> getSysRoleDetailPojoListPage(@RequestBody SysRoleDetailPojo sysRoleDetailPojo) {
        return sysRoleDetailService.getSysRoleDetailPojoListPage(sysRoleDetailPojo);
    }


}