package com.yin.controller.jcxx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.pojo.jcxx.SysModulePojo;
import com.yin.service.jcxx.SysModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
模块信息表Controller接口
*/
@RestController
@RequestMapping("/sysModule")
public class SysModuleController {
    //模块信息表
    @Autowired
    private SysModuleService sysModuleService;

    //新增
    @PostMapping("addSysModule")
    public Result<?> addSysModulePojo(@RequestBody SysModulePojo sysModulePojo) {
        return sysModuleService.addSysModulePojo(sysModulePojo);
    }

    //删除
    @PostMapping("deleteSysModule")
    public Result<?> deleteSysModulePojo(@RequestBody SysModulePojo sysModulePojo) {
        return sysModuleService.deleteSysModulePojo(sysModulePojo);
    }

    //修改
    @PostMapping("updateSysModule")
    public Result<?> updateSysModulePojo(@RequestBody SysModulePojo sysModulePojo) {
        return sysModuleService.updateSysModulePojo(sysModulePojo);
    }

    //查询
    @PostMapping("getSysModuleList")
    public Result<List<SysModulePojo>> getSysModulePojoList(@RequestBody SysModulePojo sysModulePojo) {
        return sysModuleService.getSysModulePojoList(sysModulePojo);
    }

    //分页查询
    @PostMapping("getSysModuleListPage")
    public Result<Page<SysModulePojo>> getSysModulePojoListPage(@RequestBody SysModulePojo sysModulePojo) {
        return sysModuleService.getSysModulePojoListPage(sysModulePojo);
    }
}