import {createStore} from "vuex";

const store = createStore({
    state() {
        return {
            openTab: [],
            activeIndex: ""
        }
    },
    mutations: {
        addTab(state, route) {
            let flag = true
            //判断是否重复
            state.openTab.forEach(item => {
                if (item.path === route.path) {
                    flag = false
                }
            })
            if (flag) {
                state.openTab.push(route)
                state.activeIndex = route.path
            }
        },
        removeTab(state,path){
            let url='';
            if(path!=='/'){
                state.openTab.forEach(item => {
                    if (item.path === path) {
                        //删除地址
                        state.openTab.splice(state.openTab.indexOf(item), 1)
                    }
                    url=item.path
                })
            }
        },
        getTabs(state) {
            return state.openTab;
        },
        setActiveIndex(state,name){
            state.activeIndex=name
        }
    }
})
export default store
