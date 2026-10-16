package com.yin.controller.jcxx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.pojo.jcxx.MenuPojo;
import com.yin.service.jcxx.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
菜单信息表Controller接口
*/
@RestController
@RequestMapping("/menu")
public class MenuController {
    //菜单信息表
    @Autowired
    private MenuService menuService;

    //新增
    @PostMapping("addMenu")
    public Result<?> addMenuPojo(@RequestBody MenuPojo menuPojo) {
        return menuService.addMenuPojo(menuPojo);
    }

    //删除
    @PostMapping("deleteMenu")
    public Result<?> deleteMenuPojo(@RequestBody MenuPojo menuPojo) {
        return menuService.deleteMenuPojo(menuPojo);
    }
    //修改
    @PostMapping("updateMenu")
    public Result<?> updateMenuPojo(@RequestBody MenuPojo menuPojo) {
        return menuService.updateMenuPojo(menuPojo);
    }

    //查询
    @PostMapping("getMenuList")
    public Result<List<MenuPojo>> getMenuPojoList(@RequestBody MenuPojo menuPojo) {
        return menuService.getMenuPojoList(menuPojo);
    }

    //分页查询
    @PostMapping("getMenuListPage")
    public Result<Page<MenuPojo>> getMenuPojoListPage(@RequestBody MenuPojo menuPojo) {
        return menuService.getMenuPojoListPage(menuPojo);
    }

    //查询菜单数据,结构数据
    @PostMapping("/getMenuPojoDataS")
    public Result<List<MenuPojo>> getMenuPojoDataS(@RequestBody MenuPojo menuPojo) {
        return menuService.getMenuPojoDataS(menuPojo);
    }

}