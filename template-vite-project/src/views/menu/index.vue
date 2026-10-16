<script>
import CommonAside from "../components/CommonAside.vue";
import Tabs from "../components/Tabs.vue";
import Header from '../components/Header.vue'

export default {
  name: "home.vue",
  components: {
    'my-CommonAside': CommonAside,
    'my-Tabs': Tabs,
    'my-Header': Header,
  },
  created() {
    this.get()
  },
  methods: {
    get() {
      const data = {
        path: "/",
        name: "首页",
        index: '0'
      }
      this.$store.commit('addTab', data)
    }
  }
}
</script>
<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="elAside" width="230px">
        <my-CommonAside/>
      </el-aside>
      <el-container>
        <el-header class="elHeader">
          <my-Header/>
        </el-header>
        <el-main class="elMain">
          <my-Tabs/>
          <router-view v-slot="{ Component }">
            <transition>
              <keep-alive>
                <component :is="Component" />
              </keep-alive>
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>

</template>
<style scoped>
.common-layout {
  height: 100vh;
}
.elMain {
  height: 95vh; /* 设置元素高度为视口高度 */
  overflow: auto; /* 当内容超出时显示滚动条 */
  /* 隐藏滚动条 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}
.elMain::-webkit-scrollbar {
  display: none; /* Chrome和Safari */
}
.elHeader{
  height: 5vh;
  padding: 0;
  margin: 0;
}
/*设置header下边距不那么大*/
main.el-main.elMain {
  margin-top: -15px;
}
/*设置el-form的label两端对齐，需要el-form设置属性label-width*/
.el-form-item__label {
  display: inline;
  text-align-last: justify;
}
</style>
