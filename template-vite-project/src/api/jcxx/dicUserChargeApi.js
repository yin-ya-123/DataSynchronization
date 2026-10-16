import request from "../../utils/request.js";

const baseUrl = 'dicUserCharge'
export default {
    //查询
    getDicUserChargeAsRole(data) {
        return request({
            url: `${baseUrl}/getDicUserChargeAsRole`,
            method: 'post',
            data: data,
        })
    },
    addDicUserChargeS(data) {
        return request({
            url: `${baseUrl}/addDicUserChargeS`,
            method: 'post',
            data: data,
        })
    },
    deleteDicUserCharge(data) {
        return request({
            url: `${baseUrl}/deleteDicUserCharge`,
            method: 'post',
            data: data,
        })
    }
}
