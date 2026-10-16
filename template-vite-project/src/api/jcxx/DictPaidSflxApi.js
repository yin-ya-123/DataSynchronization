import request from "../../utils/request.js";

const baseUrl = 'dictPaidSflx'
export default {
    //新增
    addDictPaidSflx(data) {
        return request({
            url: `${baseUrl}/addDictPaidSflx`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteDictPaidSflx(data) {
        return request({
            url: `${baseUrl}/deleteDictPaidSflx`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateDictPaidSflx(data) {
        return request({
            url: `${baseUrl}/updateDictPaidSflx`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getDictPaidSflxList(data) {
        return request({
            url: `${baseUrl}/getDictPaidSflxList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getDictPaidSflxListPage(data) {
        return request({
            url: `${baseUrl}/getDictPaidSflxListPage`,
            method: 'post',
            data: data,
        })
    },
}
