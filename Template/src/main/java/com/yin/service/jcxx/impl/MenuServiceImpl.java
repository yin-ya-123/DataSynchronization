package com.yin.service.jcxx.impl;
/*
菜单信息表serviceImpl接口
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.common.sys.SysEnum;
import com.yin.config.error.BizException;
import com.yin.config.error.ExceptionEnum;
import com.yin.mapper.jcxx.MenuMapper;
import com.yin.pojo.jcxx.MenuPojo;
import com.yin.service.jcxx.MenuService;
import com.yin.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuPojo> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ServiceUtils<MenuPojo> serviceUtils;

    /*
    菜单信息表条件参数
    */
    public LambdaQueryWrapper<MenuPojo> menuLambdaQueryWrapper(MenuPojo menu) {
        LambdaQueryWrapper<MenuPojo> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (menu.getId() != null && !menu.getId().isEmpty()) {
            menuLambdaQueryWrapper.eq(MenuPojo::getId, menu.getId());
        }

        if (menu.getLabel() != null && !menu.getLabel().isEmpty()) {
            menuLambdaQueryWrapper.eq(MenuPojo::getLabel, menu.getLabel());
        }

        if (menu.getPath() != null && !menu.getPath().isEmpty()) {
            menuLambdaQueryWrapper.eq(MenuPojo::getPath, menu.getPath());
        }

        if (menu.getModuleId() != null && !menu.getModuleId().isEmpty()) {
            menuLambdaQueryWrapper.eq(MenuPojo::getModuleId, menu.getModuleId());
        }

        if (menu.getMenuLv() != null && !menu.getMenuLv().isEmpty()) {
            menuLambdaQueryWrapper.eq(MenuPojo::getMenuLv, menu.getMenuLv());
        }
        return menuLambdaQueryWrapper;
    }

    /**
     * 菜单信息表参数校验
     *
     * @param menu 实体
     * @param type 操作类型 0 新增 1 删除 2修改
     * @return 标志
     */
    public String menuVerifyS(MenuPojo menu, SysEnum type) {
        if ((menu.getId() == null || menu.getId().isEmpty()) && (type.getSysCodeInt().equals(SysEnum.DELETE.getSysCodeInt()) || type.getSysCodeInt().equals(SysEnum.UPDATE.getSysCodeInt()))) {
            return "ID为空";
        }
        if ((menu.getLabel() == null || menu.getLabel().isEmpty())) {
            return "菜单名称为空";
        }

        if ((menu.getPath() == null || menu.getPath().isEmpty())) {
            return "路由地址为空";
        }
        if ((menu.getModuleId() == null || menu.getModuleId().isEmpty())) {
            return "模块id为空";
        }
        if ((menu.getMenuLv() == null || menu.getMenuLv().isEmpty())) {
            return "菜单等级为空";
        }
//        if ((menu.getSort() == null || menu.getSort().isEmpty())) {
//            return "排序号为空";
//        }
        return ExceptionEnum.CODE_CZ.getResultCode();
    }

    //新增
    public Result<?> addMenuPojo(MenuPojo menuPojo) {
        return serviceUtils.insert(menuMapper, menuPojo, menuVerifyS(menuPojo, SysEnum.SAVE), menuLambdaQueryWrapper(menuPojo));
    }

    //删除
    public Result<?> deleteMenuPojo(MenuPojo menuPojo) {
        //判断是否有子类
        LambdaQueryWrapper<MenuPojo> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(MenuPojo::getParentId,menuPojo.getId());
        List<MenuPojo> menuPojos = menuMapper.selectList(menuWrapper);
        if(!menuPojos.isEmpty()){
            return Result.error(ExceptionEnum.CODE_DELETE,"");
        }
        return serviceUtils.delete(menuMapper, menuPojo, menuVerifyS(menuPojo, SysEnum.DELETE));
    }

    //修改
    public Result<?> updateMenuPojo(MenuPojo menuPojo) {
        return serviceUtils.update(menuMapper, menuPojo, menuVerifyS(menuPojo, SysEnum.UPDATE));
    }

    //查询
    public Result<List<MenuPojo>> getMenuPojoList(MenuPojo menuPojo) {
        return serviceUtils.selectList(menuMapper, menuLambdaQueryWrapper(menuPojo));
    }

    //分页查询
    public Result<Page<MenuPojo>> getMenuPojoListPage(MenuPojo menuPojo) {
        return serviceUtils.selectPage(menuMapper, MenuPojo.class, menuPojo.getPagePojo(), menuLambdaQueryWrapper(menuPojo));
    }

    @Override
    public List<MenuPojo> selectMenuByUserId(String userId, String moduleId) {
        if (null != userId && null != moduleId) {
            return menuMapper.selectMenuByUserId(userId, moduleId);
        }
        throw new BizException(ExceptionEnum.CODE_VERIFY.getResultMsg());
    }

    @Override
    public List<MenuPojo> selectMenuByParentId(String userId, String parentId) {
        if (null != userId && null != parentId) {
            return menuMapper.selectMenuByParentId(userId, parentId);
        }
        throw new BizException(ExceptionEnum.CODE_VERIFY.getResultMsg());
    }

    @Override
    public Result<List<MenuPojo>> getMenuPojoDataS(MenuPojo menuPojo) {
//        //先获取二级菜单
//        LambdaQueryWrapper<MenuPojo> menuPojoLambdaQueryWrapper = menuLambdaQueryWrapper(menuPojo);
//        menuPojoLambdaQueryWrapper.eq(MenuPojo::getMenuLv, "2");
//        //获取三级菜单
//        List<MenuPojo> menuPojos = menuMapper.selectList(menuPojoLambdaQueryWrapper);
//        for (MenuPojo pojo : menuPojos) {
//            menuPojoLambdaQueryWrapper.clear();
//            menuPojoLambdaQueryWrapper.eq(MenuPojo::getParentId, pojo.getId());
//            List<MenuPojo> menuPojoMenuLv3 = menuMapper.selectList(menuPojoLambdaQueryWrapper);
//            pojo.setChildren(menuPojoMenuLv3);
//        }
        //先获取一级菜单
        LambdaQueryWrapper<MenuPojo> menuPojoLambdaQueryWrapper = menuLambdaQueryWrapper(menuPojo);
        menuPojoLambdaQueryWrapper.eq(MenuPojo::getMenuLv, "1");
        menuPojoLambdaQueryWrapper.orderByAsc(MenuPojo::getModuleId).orderByAsc(MenuPojo::getSort);
        List<MenuPojo> menuPojoMenuLv1 = menuMapper.selectList(menuPojoLambdaQueryWrapper);
        for (MenuPojo pojo : menuPojoMenuLv1) {
            //获取二级菜单
            menuPojoLambdaQueryWrapper.clear();
            menuPojoLambdaQueryWrapper.eq(MenuPojo::getParentId, pojo.getId());
            menuPojoLambdaQueryWrapper.orderByAsc(MenuPojo::getModuleId).orderByAsc(MenuPojo::getSort);
            List<MenuPojo> menuPojoMenuLv2 = menuMapper.selectList(menuPojoLambdaQueryWrapper);
            pojo.setChildren(menuPojoMenuLv2);
            for (MenuPojo menuPojo1 : menuPojoMenuLv2) {
                //再获取三级
                menuPojoLambdaQueryWrapper.clear();
                menuPojoLambdaQueryWrapper.eq(MenuPojo::getParentId, menuPojo1.getId());
                menuPojoLambdaQueryWrapper.orderByAsc(MenuPojo::getModuleId).orderByAsc(MenuPojo::getSort);
                List<MenuPojo> menuPojoMenuLv3 = menuMapper.selectList(menuPojoLambdaQueryWrapper);
                menuPojo1.setChildren(menuPojoMenuLv3);
            }
        }
        return Result.okSel(menuPojoMenuLv1);
    }

}