import request from "../../utils/request.js";

const baseUrl = 'scheduledDetails'
export default {
// ... existing code ...
    //新增定时任务详情
    addScheduledDetails(data) {
        return request({
            url: `${baseUrl}/add`,
            method: 'post',
            data: data
        })
    },

    //删除定时任务详情
    deleteScheduledDetails(data) {
        return request({
            url: `${baseUrl}/delete`,
            method: 'post',
            data: data
        })
    },

    //修改定时任务详情
    updateScheduledDetails(data) {
        return request({
            url: `${baseUrl}/update`,
            method: 'post',
            data: data
        })
    },

    //查询定时任务详情
    getScheduledDetails(data) {
        return request({
            url: `${baseUrl}/get`,
            method: 'post',
            data: data
        })
    },

    //查询定时任务详情列表
    listScheduledDetails(data) {
        return request({
            url: `${baseUrl}/list`,
            method: 'post',
            data: data
        })
    },

    //分页查询定时任务详情
    pageScheduledDetails(data) {
        return request({
            url: `${baseUrl}/page`,
            method: 'post',
            data: data
        })
    }
// ... existing code ...

}
