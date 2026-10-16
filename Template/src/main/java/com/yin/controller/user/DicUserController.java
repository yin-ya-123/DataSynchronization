package com.yin.controller.user;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.pojo.user.DicUserPojo;
import com.yin.service.user.DicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/*
用户信息表Controller接口
*/
@RestController
@RequestMapping("/dicUser")
public class DicUserController {
    //用户信息表
    @Autowired
    private DicUserService dicUserService;

    //新增
    @PostMapping("addDicUser")
    public Result<?> addDicUser(@RequestBody DicUserPojo dicUserPojo) {
        return dicUserService.addDicUserPojo(dicUserPojo);
    }

    //删除
    @PostMapping("deleteDicUser")
    public Result<?> deleteDicUser(@RequestBody DicUserPojo dicUserPojo) {
        return dicUserService.deleteDicUserPojo(dicUserPojo);
    }

    //修改
    @PostMapping("updateDicUser")
    public Result<?> updateDicUser(@RequestBody DicUserPojo dicUserPojo) {
        return dicUserService.updateDicUserPojo(dicUserPojo);
    }

    //查询
    @PostMapping("getDicUserList")
    public Result<List<DicUserPojo>> getDicUserPojoList(@RequestBody DicUserPojo dicUserPojo) {
        return dicUserService.getDicUserPojoList(dicUserPojo);
    }

    //分页查询
    @PostMapping("getDicUserListPage")
    public Result<Page<DicUserPojo>> getDicUserPojoListPage(@RequestBody DicUserPojo dicUserPojo) {
        return dicUserService.getDicUserPojoListPage(dicUserPojo);
    }
}