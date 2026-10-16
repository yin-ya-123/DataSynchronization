package com.yin.mapper.jcxx;
/*
角色信息mapper接口
*/
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.jcxx.SysRolePojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRolePojo>{
}