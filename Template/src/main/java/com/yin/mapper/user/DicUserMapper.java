package com.yin.mapper.user;
/*
用户信息表mapper接口
*/
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.user.DicUserPojo;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface DicUserMapper extends BaseMapper<DicUserPojo>{
}