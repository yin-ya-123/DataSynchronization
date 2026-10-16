import request from "../../utils/request.js";

const baseUrl = 'dictSysDictionary'
export default {
    //新增
    addDictSysDictionary(data) {
        return request({
            url: `${baseUrl}/addDictSysDictionary`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteDictSysDictionary(data) {
        return request({
            url: `${baseUrl}/deleteDictSysDictionary`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateDictSysDictionary(data) {
        return request({
            url: `${baseUrl}/updateDictSysDictionary`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getDictSysDictionaryList(data) {
        return request({
            url: `${baseUrl}/getDictSysDictionaryList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getDictSysDictionaryListPage(data) {
        return request({
            url: `${baseUrl}/getDictSysDictionaryListPage`,
            method: 'post',
            data: data,
        })
    },
}
