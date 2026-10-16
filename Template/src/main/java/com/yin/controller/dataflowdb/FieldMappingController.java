package com.yin.controller.dataflowdb;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.config.error.BizException;
import com.yin.pojo.dataflowdb.FieldMappingPojo;
import com.yin.pojo.dataflowdb.ScheduledDetails;
import com.yin.service.dataflowdb.FieldMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则Controller接口
*/
@RestController
@RequestMapping("/fieldMapping")
public class FieldMappingController {
    //字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则
    @Autowired
    private FieldMappingService fieldMappingService;

    //新增
    @PostMapping("addFieldMapping")
    public Result<?> addFieldMappingPojo(@RequestBody FieldMappingPojo fieldMappingPojo) {
        return fieldMappingService.addFieldMappingPojo(fieldMappingPojo);
    }

    //删除
    @PostMapping("deleteFieldMapping")
    public Result<?> deleteFieldMappingPojo(@RequestBody FieldMappingPojo fieldMappingPojo) {
        return fieldMappingService.deleteFieldMappingPojo(fieldMappingPojo);
    }

    //修改
    @PostMapping("updateFieldMapping")
    public Result<?> updateFieldMappingPojo(@RequestBody FieldMappingPojo fieldMappingPojo) {
        return fieldMappingService.updateFieldMappingPojo(fieldMappingPojo);
    }

    //查询
    @PostMapping("getFieldMappingList")
    public Result<List<FieldMappingPojo>> getFieldMappingPojoList(@RequestBody FieldMappingPojo fieldMappingPojo) {
        return fieldMappingService.getFieldMappingPojoList(fieldMappingPojo);
    }

    //分页查询
    @PostMapping("getFieldMappingListPage")
    public Result<Page<FieldMappingPojo>> getFieldMappingPojoListPage(@RequestBody FieldMappingPojo fieldMappingPojo) {
        return fieldMappingService.getFieldMappingPojoListPage(fieldMappingPojo);
    }

    //初始化字段映射
    @PostMapping("initFieldMapping")
    public Result<?> initFieldMapping(@RequestBody ScheduledDetails scheduledDetails) {
        try {
            fieldMappingService.dataMapping(scheduledDetails);
            return Result.ok("字段映射处理成功", null);
        } catch (Exception e) {
            throw new BizException("00001", e.getMessage());
        }
    }

    //测试新的映射
    @PostMapping("testFieldMapping")
    public Result<?> testFieldMapping(@RequestBody ScheduledDetails scheduledDetails) {
        try {
            fieldMappingService.dataMapping(scheduledDetails);
            return Result.ok("字段映射处理成功", null);
        } catch (Exception e) {
            throw new BizException("00001", e.getMessage());
        }
    }
}