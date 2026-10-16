import request from "../../utils/request.js";

const baseUrl = 'dictPaiditemtype'
export default {
    //新增
    addDictPaiditemtype(data) {
        return request({
            url: `${baseUrl}/addDictPaiditemtype`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteDictPaiditemtype(data) {
        return request({
            url: `${baseUrl}/deleteDictPaiditemtype`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateDictPaiditemtype(data) {
        return request({
            url: `${baseUrl}/updateDictPaiditemtype`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getDictPaiditemtypeList(data) {
        return request({
            url: `${baseUrl}/getDictPaiditemtypeList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getDictPaiditemtypeListPage(data) {
        return request({
            url: `${baseUrl}/getDictPaiditemtypeListPage`,
            method: 'post',
            data: data,
        })
    },
}
