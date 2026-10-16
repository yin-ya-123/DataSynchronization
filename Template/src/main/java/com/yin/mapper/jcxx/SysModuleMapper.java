package com.yin.mapper.jcxx;
/*
模块信息表mapper接口
*/
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.jcxx.SysModulePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysModuleMapper extends BaseMapper<SysModulePojo>{
    /**
     * 根据用户id，查询对于的模块数据
     * @param userId 用户id
     * @return 模块信息
     */
    public List<SysModulePojo> getModuleByUserId(@Param("userId") String userId);
}