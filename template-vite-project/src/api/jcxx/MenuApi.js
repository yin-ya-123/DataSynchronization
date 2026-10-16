import request from "../../utils/request.js";

const baseUrl = 'menu'
export default {
    //新增
    addMenu(data) {
        return request({
            url: `${baseUrl}/addMenu`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteMenu(data) {
        return request({
            url: `${baseUrl}/deleteMenu`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateMenu(data) {
        return request({
            url: `${baseUrl}/updateMenu`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getMenuList(data) {
        return request({
            url: `${baseUrl}/getMenuList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getMenuListPage(data) {
        return request({
            url: `${baseUrl}/getMenuListPage`,
            method: 'post',
            data: data,
        })
    },
    //菜单结构信息查询
    getMenuPojoDataS(data) {
        return request({
            url: `${baseUrl}/getMenuPojoDataS`,
            method: 'post',
            data: data,
        })
    },

}
