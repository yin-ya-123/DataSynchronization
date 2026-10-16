import request from "../../utils/request.js";

const baseUrl = 'errorLog'
export default {
    //新增
    addErrorLog(data) {
        return request({
            url: `${baseUrl}/addErrorLog`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteErrorLog(data) {
        return request({
            url: `${baseUrl}/deleteErrorLog`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateErrorLog(data) {
        return request({
            url: `${baseUrl}/updateErrorLog`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getErrorLogList(data) {
        return request({
            url: `${baseUrl}/getErrorLogList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getErrorLogListPage(data) {
        return request({
            url: `${baseUrl}/getErrorLogListPage`,
            method: 'post',
            data: data,
        })
    },
}
