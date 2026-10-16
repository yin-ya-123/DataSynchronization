package com.yin.mapper.jcxx;
/*
菜单信息表mapper接口
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.pojo.jcxx.MenuPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<MenuPojo> {

    /**
     * 根据用户id，模块id 获取指定一级菜单，在登录后选择操作模块使用
     * @param userId  用户id
     * @param moduleId 模块id
     * @return 返回一级菜单，具体跳转的菜单数据
     */
    public List<MenuPojo> selectMenuByUserId(@Param("userId")String userId,@Param("moduleId")String moduleId);

    /**
     * 根据用户id，父类id 获取指定二级、三级级菜单，在登录后选择操作模块使用
     * @param userId  用户id
     * @param parentId 父类id
     * @return 返回一级菜单，具体跳转的菜单数据
     */
    public List<MenuPojo> selectMenuByParentId(@Param("userId")String userId, @Param("parentId")String parentId);
}