import {createRouter, createWebHistory} from 'vue-router'
import MenuIndex from '../views/menu/index.vue'

const router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    history: createWebHistory("/vue/"),
    routes: [
        {
            path: '/cs',
            name: 'cs',
            meta: {name: '测试', index: 0},
            component: () => import('../views/test/TestCs.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import("../views/user/Login.vue"),
            meta: {name: '登录界面', index: 0},
        },
        {
            path: '/',
            name: 'MenuIndex',
            component: MenuIndex,
            meta: {name: '首页', index: 0},
            children: [
                // routerData.userRouter,
                {
                    path: '/menuMaintenance',
                    name: 'menuMaintenance',
                    meta: {name: '权限分配', index: 0},
                    component: () => import('../views/jcxx/menuMaintenance.vue')
                },
                {
                    path: '/DictUser',
                    name: 'DictUser',
                    meta: {name: '员工信息', index: 0},
                    component: () => import('../views/user/DictUser.vue')
                },
                {
                    path: '/menuAdd',
                    name: 'menuAdd',
                    meta: {name: '菜单维护', index: 0},
                    component: () => import('../views/menu/menuAdd.vue')
                },
                {
                    path: '/DynamicDataSource',
                    name: 'DynamicDataSource',
                    meta: {name: '数据源维护', index: 0},
                    component: () => import('../views/dataflowdb/DynamicDataSource.vue')
                },
                {
                    path: '/Scheduled',
                    name: 'Scheduled',
                    meta: {name: '任务数据维护', index: 0},
                    component: () => import('../views/dataflowdb/Scheduled.vue')
                },
                {
                    path: '/ErrorLog',
                    name: 'ErrorLog',
                    meta: {name: '任务日志查询', index: 0},
                    component: () => import('../views/dataflowdb/ErrorLog.vue')
                },
                {
                    path: '/:catchAll(.*)',
                    name: 'not-found',
                    meta: {name: '404', index: 0},
                    component: () => import('../views/error/NotFound.vue'),
                },
            ]
        },
    ]
})
//路由守卫，拦截
// router.beforeEach((to, from, next) => {
//     //获取token
//     const token=localStorage.getItem("token")
//     if(token || to.path==='/login'){
//         //如果有token或者是跳转到登录界面，放行
//         next()
//     }else {
//         ElMessage.error("请先登录")
//         next('/login')
//     }
// })
export default router
