import request from "../../utils/request.js";

const baseUrl = 'tasks'
export default {
//新增
    addFieldMapping(data) {
        return request({
            url: `${baseUrl}/addFieldMapping`,
            method: 'post',
            data: data,
        })
    },

    //重新加载所有任务
    reloadTasks() {
        return request({
            url: `${baseUrl}/reload`,
            method: 'get'
        })
    },

    //新增任务
    addTask(data) {
        return request({
            url: `${baseUrl}/addScheduled`,
            method: 'post',
            data: data
        })
    },

    //查询任务
    getTask() {
        return request({
            url: `${baseUrl}/getScheduled`,
            method: 'get'
        })
    },

    //分页查询任务
    pageTask(data) {
        return request({
            url: `${baseUrl}/page`,
            method: 'post',
            data: data
        })
    },

    //删除任务
    deleteTask(data) {
        return request({
            url: `${baseUrl}/deleteScheduled`,
            method: 'delete',
            data: data
        })
    }
    ,
    //更新任务
    updateTask(data) {
        return request({
            url: `${baseUrl}/updateScheduled`,
            method: 'post',
            data: data
        })
    },
    //立即执行任务一次
    startScheduled(data) {
        return request({
            url: `${baseUrl}/startScheduled`,
            method: 'post',
            data: data
        })
    }

}
