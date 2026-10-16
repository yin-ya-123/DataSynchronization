package com.yin.controller.user;
import com.yin.common.Result;
import com.yin.config.error.ExceptionEnum;
import com.yin.pojo.jcxx.MenuPojo;
import com.yin.pojo.jcxx.SysModulePojo;
import com.yin.pojo.user.DicUserPojo;
import com.yin.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @author yin
 * @date 2024-12-16
 * @Description 登录操作
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> map) {
        String user = map.get("username");
        String pwd = map.get("password");
        DicUserPojo login = loginService.login(user, pwd);
        //密码置空
        login.setPassword(null);
        return Result.ok(ExceptionEnum.CODE_CZ,login);
    }

    @PostMapping("/getLoginModuleMenu")
    public Result<?> getLoginModuleMenu(@RequestBody Map<String, String> map) {
        String userId = map.get("userId");
        List<SysModulePojo> loginModuleMenu = loginService.getLoginModuleMenu(userId);
        return Result.ok(ExceptionEnum.CODE_CZ,loginModuleMenu);
    }

    @PostMapping("/getLoginMenus")
    public Result<?> getLoginMenus(@RequestBody Map<String, String> map) {
        String userId = map.get("userId");
        String parentId = map.get("parentId");
        List<MenuPojo> loginMenus = loginService.getLoginMenus(userId, parentId);
        return Result.ok(ExceptionEnum.CODE_CZ,loginMenus);
    }
}
