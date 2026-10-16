package com.yin.service.user;


import com.yin.pojo.jcxx.MenuPojo;
import com.yin.pojo.jcxx.SysModulePojo;
import com.yin.pojo.user.DicUserPojo;

import java.util.List;

/**
 * @author yin
 * @date 2024-12-16
 * @Description 登录操作接口
 */
public interface LoginService {
    //登录接口
    public DicUserPojo login(String username, String password);
    //登录成功返回具体模块菜单接口
    public List<SysModulePojo> getLoginModuleMenu(String userId);
    //选择菜单后，重新调整数据接口
    public List<MenuPojo> getLoginMenus(String userId,String patentId);
}
