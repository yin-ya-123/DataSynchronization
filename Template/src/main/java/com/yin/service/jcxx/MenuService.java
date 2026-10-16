package com.yin.service.jcxx;
/*
菜单信息表service接口
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.common.Result;
import com.yin.pojo.jcxx.MenuPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuService extends IService<MenuPojo> {
    //新增
    public Result<?> addMenuPojo(MenuPojo menuPojo);

    //删除
    public Result<?> deleteMenuPojo(MenuPojo menuPojo);

    //修改
    public Result<?> updateMenuPojo(MenuPojo menuPojo);

    //查询
    public Result<List<MenuPojo>> getMenuPojoList(MenuPojo menuPojo);

    //分页查询
    public Result<Page<MenuPojo>> getMenuPojoListPage(MenuPojo menuPojo);

    /**
     * 根据用户id，模块id 获取指定一级菜单，在登录后选择操作模块使用
     * @param userId  用户id
     * @param moduleId 模块id
     * @return 返回一级菜单，具体跳转的菜单数据
     */
    public List<MenuPojo> selectMenuByUserId(String userId,String moduleId);

    /**
     * 根据用户id，父类id 获取指定二级、三级级菜单，在登录后选择操作模块使用
     * @param userId  用户id
     * @param parentId 父类id
     * @return 返回一级菜单，具体跳转的菜单数据
     */
    public List<MenuPojo> selectMenuByParentId(String userId,String parentId);

    /**
     *  获取权限菜单结构数据
     * @return 返回数据
     */
    public Result<List<MenuPojo>> getMenuPojoDataS(MenuPojo menuPojo);
}