import axios from 'axios'
import { ElMessage } from 'element-plus'
//创建axios实例
const service = axios.create({
    baseURL: 'http://127.0.0.1:8088',
    // baseURL: 'http://192.168.0.250:8088',
    timeout: 150000 //请求超时时间
})
//http request拦截器
service.interceptors.request.use(
    config => {
        //token先不处理，后续使用时在完善
        // if(cookie.get('token')){
        //     config.headers['token']=cookie.get('token')
        // }
        config.headers['Content-Type'] ='application/json;charset=UTF-8'
        config.headers['Access-Control-Allow-Origin'] ='*' //设置响应头，*表示任何地址都亦可以访问
        config.headers['token']=localStorage.getItem("token") //设置请求头携带token
        return config
    },
    err => {
        return Promise.reject(err)
    })
//http response拦截器
service.interceptors.response.use(
    response => {
        // console.log("response")
        if (response.data.code !== "200") {
            // ElMessage({
            //     message: response.data.message,
            //     type: 'error',
            //     duration: 5 * 1000
            // })
            return Promise.reject(response.data)
        } else {
            // console.log(response)
            return response.data
        }
    },
    error => {
        return Promise.reject(error.response)
    })
export default service
