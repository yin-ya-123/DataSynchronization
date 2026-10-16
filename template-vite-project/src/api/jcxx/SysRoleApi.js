import request from "../../utils/request.js";

const baseUrl = 'sysRole'
export default {
    //新增
    addSysRole(data) {
        return request({
            url: `${baseUrl}/addSysRole`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteSysRole(data) {
        return request({
            url: `${baseUrl}/deleteSysRole`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateSysRole(data) {
        return request({
            url: `${baseUrl}/updateSysRole`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getSysRoleList(data) {
        return request({
            url: `${baseUrl}/getSysRoleList`,
            method: 'post',
            data: data,
        })
    },
    getSysRolePojoListAsModule(data) {
        return request({
            url: `${baseUrl}/getSysRolePojoListAsModule`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getSysRoleListPage(data) {
        return request({
            url: `${baseUrl}/getSysRoleListPage`,
            method: 'post',
            data: data,
        })
    },
}
