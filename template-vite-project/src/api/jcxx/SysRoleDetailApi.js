import request from "../../utils/request.js";

const baseUrl = 'sysRoleDetail'
export default {
//新增
    addSysRoleDetail(data) {
        return request({
            url: `${baseUrl}/addSysRoleDetail`,
            method: 'post',
            data: data,
        })
    },
    //批量新增
    addSysRoleDetailPojoList(data) {
        return request({
            url: `${baseUrl}/addSysRoleDetailPojoList`,
            method: 'post',
            data: data,
        })
    },
//删除
    deleteSysRoleDetail(data) {
        return request({
            url: `${baseUrl}/deleteSysRoleDetail`,
            method: 'post',
            data: data,
        })
    },
    //根据对应参数删除
    deleteSysRoleDetailPojoByPojo(data) {
        return request({
            url: `${baseUrl}/deleteSysRoleDetailPojoByPojo`,
            method: 'post',
            data: data,
        })
    },
//修改
    updateSysRoleDetail(data) {
        return request({
            url: `${baseUrl}/updateSysRoleDetail`,
            method: 'post',
            data: data,
        })
    },
//查询
    getSysRoleDetailList(data) {
        return request({
            url: `${baseUrl}/getSysRoleDetailList`,
            method: 'post',
            data: data,
        })
    },
//查询
    getSysRoleDetailListPage(data) {
        return request({
            url: `${baseUrl}/getSysRoleDetailListPage`,
            method: 'post',
            data: data,
        })
    },
}
