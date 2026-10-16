import request from "../../utils/request.js";

const baseUrl = 'dictPaiditemDictionary'
export default {
    //新增
    addDictPaiditemDictionary(data) {
        return request({
            url: `${baseUrl}/addDictPaiditemDictionary`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteDictPaiditemDictionary(data) {
        return request({
            url: `${baseUrl}/deleteDictPaiditemDictionary`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateDictPaiditemDictionary(data) {
        return request({
            url: `${baseUrl}/updateDictPaiditemDictionary`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getDictPaiditemDictionaryList(data) {
        return request({
            url: `${baseUrl}/getDictPaiditemDictionaryList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getDictPaiditemDictionaryListPage(data) {
        return request({
            url: `${baseUrl}/getDictPaiditemDictionaryListPage`,
            method: 'post',
            data: data,
        })
    },
}
