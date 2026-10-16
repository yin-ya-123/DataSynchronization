import request from "../../utils/request.js";
const baseUrl = 'login'
export default {
    //登录
    login(data) {
        return request({
            url: `${baseUrl}/login`,
            method: 'post',
            data: data
        })
    },
    //取消登录
    loginOut(){

    },
    //用户登录后查询模块
    getLoginModuleMenu(data){
        return request({
            url: `${baseUrl}/getLoginModuleMenu`,
            method: 'post',
            data: data
        })
    },
    //用户登录后选择对应模块下的菜单获取具体菜单列表
    getLoginMenus(data){
        return request({
            url: `${baseUrl}/getLoginMenus`,
            method: 'post',
            data: data
        })
    }
}