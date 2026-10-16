import request from "../../utils/request.js";

const baseUrl = 'datasource'


export default {
    //获取所有数据源配置
    list() {
        return request({
            url: `${baseUrl}/list`,
            method: 'get'
        })
    },
    //获取所有启用的数据源配置
    listEnabled() {
        return request({
            url: `${baseUrl}/listEnabled`,
            method: 'get'
        })
    },
    //刷新数据源，重新加载所有数据源
    refresh() {
        return request({
            url: `${baseUrl}/refresh`,
            method: 'get'
        })
    },
    //添加数据源
    dataSource(data) {
        return request({
            url: `${baseUrl}/dataSource`,
            method: 'post',
            data: data
        })
    },
    //测试连接
    dataSourceCon(data) {
        return request({
            url: `${baseUrl}/dataSourceCon`,
            method: 'post',
            data: data
        })
    },
    //测试sql查询
    dataSourceSql(data) {
        return request({
            url: `${baseUrl}/dataSourceSql`,
            method: 'post',
            data: data
        })
    }

}
