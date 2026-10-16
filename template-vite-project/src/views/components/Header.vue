<script>
import systemApplicationParams from "../../sys/SystemApplicationParams.js";
import {getNowTime} from "../../utils/Utils.js";
import {ElMessage} from "element-plus";

export default {
  name: 'my-header',
  data() {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      circleUrl2: '../../assets/vue.svg',
      userName: ''
    }
  },
  created() {
    this.init()
  },
  methods: {
    //获取登录信息
    init() {
      let user = JSON.parse(localStorage.getItem(systemApplicationParams.params.user))
      this.userName=user.xm
    },
    //退出登录
    loginOut(){
      //删除用户信息
      localStorage.removeItem(systemApplicationParams.params.user)
      //删除菜单信息
      localStorage.removeItem(systemApplicationParams.params.menu)
      //跳转登录界面
      ElMessage.success("退出成功"+getNowTime())
      this.$router.push({path:"/login"})
    }
  }
}
</script>

<template>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
  >
    <el-menu-item index="0">
<!--      <img src="../../assets/vue.svg"/>-->
    </el-menu-item>
    <el-sub-menu index="1">
      <template #title>
        <img src="../../assets/vue.svg"/>
        {{ userName }}
      </template>
      <el-menu-item index="1-1">个人信息</el-menu-item>
      <el-menu-item @click="loginOut" index="1-2">退出登录</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<style scoped>
.el-menu-demo {
  height: 5vh;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}
</style>