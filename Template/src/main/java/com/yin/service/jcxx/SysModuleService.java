package com.yin.service.jcxx;
/*
模块信息表service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.jcxx.SysModulePojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysModuleService extends IService<SysModulePojo> {
    //新增
    public Result<?> addSysModulePojo(SysModulePojo sysModulePojo);

    //删除
    public Result<?> deleteSysModulePojo(SysModulePojo sysModulePojo);

    //修改
    public Result<?> updateSysModulePojo(SysModulePojo sysModulePojo);

    //查询
    public Result<List<SysModulePojo>> getSysModulePojoList(SysModulePojo sysModulePojo);

    //分页查询
    public Result<Page<SysModulePojo>> getSysModulePojoListPage(SysModulePojo sysModulePojo);
    /**
     * 根据用户id，查询对于的模块数据
     * @param userId 用户id
     * @return 模块信息
     */
    public List<SysModulePojo> getModuleByUserId(String userId);
}