package com.yin.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.config.error.BizException;
import com.yin.config.error.ExceptionEnum;
import com.yin.pojo.jcxx.MenuPojo;
import com.yin.pojo.jcxx.SysModulePojo;
import com.yin.pojo.user.DicUserPojo;
import com.yin.service.jcxx.MenuService;
import com.yin.service.jcxx.SysModuleService;
import com.yin.service.user.DicUserService;
import com.yin.service.user.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yin
 * @date 2024-12-16
 * @Description
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private DicUserService dicUserService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private SysModuleService sysModuleService;

    @Override
    public DicUserPojo login(String username, String password) {
        log.info("用户名:{},密码:{}",username,password);
        if((null==username||null==password) ||(username.isEmpty() && password.isEmpty())){
            throw new BizException(ExceptionEnum.CODE_VERIFY.getResultMsg());
        }
        LambdaQueryWrapper<DicUserPojo> dicUserWrapper = new LambdaQueryWrapper<>();
        dicUserWrapper.eq(DicUserPojo::getZgh,username);
        dicUserWrapper.eq(DicUserPojo::getPassword,password);
        List<DicUserPojo> dicUserlist = dicUserService.list(dicUserWrapper);
        if(dicUserlist.size()>1){
            throw new BizException("登录失败,登录用户查询数量大于1");
        }
        if(dicUserlist.isEmpty()){
            throw new BizException("未找到该用户");
        }
        return dicUserlist.get(0);
    }
    @Override
    public List<SysModulePojo> getLoginModuleMenu(String userId) {
        log.info("用户名:{}",userId);
        if((null==userId) ||(userId.isEmpty())){
            throw new BizException(ExceptionEnum.CODE_VERIFY.getResultMsg());
        }
        List<SysModulePojo> moduleByUserId = sysModuleService.getModuleByUserId(userId);
        for(SysModulePojo sysModulePojo : moduleByUserId){
            //查询用户一级模块菜单信息
            List<MenuPojo> menuPojos = menuService.selectMenuByUserId(userId, sysModulePojo.getModuleId());
            sysModulePojo.setChildren(menuPojos);
        }
        return moduleByUserId;
    }

    @Override
    public List<MenuPojo> getLoginMenus(String userId,String patentId) {
        //查询用户选择的模块菜单id，获取对于界面的菜单信息
        if((null==userId||null==patentId) ||(userId.isEmpty() && patentId.isEmpty())){
            throw new BizException(ExceptionEnum.CODE_VERIFY.getResultMsg());
        }
        //先根据选择的模块菜单id，查询对于的界面一级菜单，在根据一级菜单，查询下列的功能菜单
        List<MenuPojo> menuPojos = menuService.selectMenuByParentId(userId, patentId);
        for (MenuPojo menuPojo : menuPojos) {
            List<MenuPojo> menuPojoChildren = menuService.selectMenuByParentId(userId, menuPojo.getId());
            menuPojo.setChildren(menuPojoChildren);
        }
        return menuPojos;
    }
}
