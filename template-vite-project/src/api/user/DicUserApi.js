import request from "../../utils/request.js";

const baseUrl = 'dicUser'
export default {
    //新增
    addDicUser(data) {
        return request({
            url: `${baseUrl}/addDicUser`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteDicUser(data) {
        return request({
            url: `${baseUrl}/deleteDicUser`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateDicUser(data) {
        return request({
            url: `${baseUrl}/updateDicUser`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getDicUserList(data) {
        return request({
            url: `${baseUrl}/getDicUserList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getDicUserListPage(data) {
        return request({
            url: `${baseUrl}/getDicUserListPage`,
            method: 'post',
            data: data,
        })
    },

}
