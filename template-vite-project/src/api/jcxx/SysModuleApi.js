import request from "../../utils/request.js";
const baseUrl = 'sysModule'

export default {
    //新增
    addSysModule(data) {
        return request({
            url: `${baseUrl}/addSysModule`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteSysModule(data) {
        return request({
            url: `${baseUrl}/deleteSysModule`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateSysModule(data) {
        return request({
            url: `${baseUrl}/updateSysModule`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getSysModuleList(data) {
        return request({
            url: `${baseUrl}/getSysModuleList`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getSysModuleListPage(data) {
        return request({
            url: `${baseUrl}/getSysModuleListPage`,
            method: 'post',
            data: data,
        })
    },
}
