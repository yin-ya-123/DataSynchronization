import request from "../../utils/request.js";

const baseUrl = 'fieldMapping'
export default {
    //新增
    addFieldMapping(data) {
        return request({
            url: `${baseUrl}/addFieldMapping`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteFieldMapping(data) {
        return request({
            url: `${baseUrl}/deleteFieldMapping`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateFieldMapping(data) {
        return request({
            url: `${baseUrl}/updateFieldMapping`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getFieldMappingList(data) {
        return request({
            url: `${baseUrl}/getFieldMappingList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getFieldMappingListPage(data) {
        return request({
            url: `${baseUrl}/getFieldMappingListPage`,
            method: 'post',
            data: data,
        })
    },
    //初始化字段映射
    initFieldMapping(data) {
        return request({
            url: `${baseUrl}/initFieldMapping`,
            method: 'post',
            data: data,
        })
    }
}
