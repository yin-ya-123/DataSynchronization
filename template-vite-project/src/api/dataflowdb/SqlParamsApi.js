import request from "../../utils/request.js";

const baseUrl = 'sqlParams'
export default {
    //新增SQL参数
    add(data) {
        return request({
            url: `${baseUrl}/add`,
            method: 'post',
            data: data,
        })
    },

    //删除SQL参数
    delete(data) {
        return request({
            url: `${baseUrl}/delete`,
            method: 'post',
            data: data,
        })
    },

    //修改SQL参数
    update(data) {
        return request({
            url: `${baseUrl}/update`,
            method: 'post',
            data: data,
        })
    },

    //查询单个SQL参数
    get(data) {
        return request({
            url: `${baseUrl}/get`,
            method: 'post',
            data: data,
        })
    },

    //查询SQL参数列表
    list(data) {
        return request({
            url: `${baseUrl}/list`,
            method: 'post',
            data: data,
        })
    },

    //分页查询SQL参数
    page(data) {
        return request({
            url: `${baseUrl}/page`,
            method: 'post',
            data: data,
        })
    },
    //测试SQL参数
    testSql(data) {
        return request({
            url: `${baseUrl}/testSql`,
            method: 'post',
            data: data,
        })
    }
}
