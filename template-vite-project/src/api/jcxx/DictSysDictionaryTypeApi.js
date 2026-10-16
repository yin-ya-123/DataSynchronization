import request from "../../utils/request.js";

const baseUrl = 'dictSysDictionaryType'
export default {
    //新增
    addDictSysDictionaryType(data) {
        return request({
            url: `${baseUrl}/addDictSysDictionaryType`,
            method: 'post',
            data: data,
        })
    },
    //删除
    deleteDictSysDictionaryType(data) {
        return request({
            url: `${baseUrl}/deleteDictSysDictionaryType`,
            method: 'post',
            data: data,
        })
    },
    //修改
    updateDictSysDictionaryType(data) {
        return request({
            url: `${baseUrl}/updateDictSysDictionaryType`,
            method: 'post',
            data: data,
        })
    },
    //查询
    getDictSysDictionaryTypeList(data) {
        return request({
            url: `${baseUrl}/getDictSysDictionaryTypeList`,
            method: 'post',
            data: data,
        })
    },
    //分页查询
    getDictSysDictionaryTypeListPage(data) {
        return request({
            url: `${baseUrl}/getDictSysDictionaryTypeListPage`,
            method: 'post',
            data: data,
        })
    },
}
