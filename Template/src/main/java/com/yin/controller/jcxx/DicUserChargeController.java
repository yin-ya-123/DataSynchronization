package com.yin.controller.jcxx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.config.error.BizException;
import com.yin.config.error.ExceptionEnum;
import com.yin.pojo.jcxx.DicUserChargePojo;
import com.yin.service.jcxx.DicUserChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/*
用户对应角色信息中间表Controller接口
*/
@RestController
@RequestMapping("/dicUserCharge")
public class DicUserChargeController {
    //用户对应角色信息中间表
    @Autowired
    private DicUserChargeService dicUserChargeService;

    //新增
    @PostMapping("addDicUserCharge")
    public Result<?> addDicUserChargePojo(@RequestBody DicUserChargePojo dicUserChargePojo) {
        return dicUserChargeService.addDicUserChargePojo(dicUserChargePojo);
    }

    //新增
    @PostMapping("addDicUserChargeS")
    @Transactional
    public Result<?> addDicUserChargePojoS(@RequestBody List<DicUserChargePojo> dicUserChargePojos) {
        Result<?> result = null;
        for (DicUserChargePojo dicUserChargePojo : dicUserChargePojos) {
            result = dicUserChargeService.addDicUserChargePojo(dicUserChargePojo);
        }
        return result;
    }

    //删除
    @PostMapping("deleteDicUserCharge")
    public Result<?> deleteDicUserChargePojo(@RequestBody DicUserChargePojo dicUserChargePojo) {
        return dicUserChargeService.deleteDicUserChargePojo(dicUserChargePojo);
    }

    //修改
    @PostMapping("updateDicUserCharge")
    public Result<?> updateDicUserChargePojo(@RequestBody DicUserChargePojo dicUserChargePojo) {
        return dicUserChargeService.updateDicUserChargePojo(dicUserChargePojo);
    }

    //查询
    @PostMapping("getDicUserChargeList")
    public Result<List<DicUserChargePojo>> getDicUserChargePojoList(@RequestBody DicUserChargePojo dicUserChargePojo) {
        return dicUserChargeService.getDicUserChargePojoList(dicUserChargePojo);
    }

    @PostMapping("/getDicUserChargeAsRole")
    public Result<List<DicUserChargePojo>> getDicUserChargeAsRole(@RequestBody DicUserChargePojo dicUserChargePojo) {
        return dicUserChargeService.getDicUserChargeAsRole(dicUserChargePojo);
    }

    //分页查询
    @PostMapping("getDicUserChargeListPage")
    public Result<Page<DicUserChargePojo>> getDicUserChargePojoListPage(@RequestBody DicUserChargePojo dicUserChargePojo) {
        return dicUserChargeService.getDicUserChargePojoListPage(dicUserChargePojo);
    }
}