package com.yin.mapper.jcxx;
/*
用户对应角色信息中间表mapper接口
*/
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.jcxx.DicUserChargePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DicUserChargeMapper extends BaseMapper<DicUserChargePojo>{
    /**
     * 获取用户对应的角色信息
     * @param userId 角色id
     * @return 实体信息
     */
    public List<DicUserChargePojo> getDicUserChargeAsRole(@Param("userId") String userId);
}