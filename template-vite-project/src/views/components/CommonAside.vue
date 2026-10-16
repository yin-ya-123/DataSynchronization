<template>
  <div class="aside">
    <el-menu router class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff"
             :collapse-transition="false"
             active-text-color="#ffd04b">
      <div style="display: inline-block;margin-top: 10px;color: white">
        <!--        <span>当前时间:</span>-->
        <!--        <span> {{newTime}}</span>-->
      </div>
      <template v-for="(item,index) in MenuJson">
        <!--      有子标签的显示-->
        <el-sub-menu v-if="item.children.length>0" :index="index+''" :key="index">
          <template #title>
            <!--            <component class="icons" :is="item.icon"></component>-->
            <span>{{ item.label }}</span>
          </template>
          <!--        第二层子类的-->
          <template v-for="(itemChildren,indexChildren) in item.children">
            <el-menu-item :index="itemChildren.path+''" @click='cdClick(itemChildren.path)'>
              <!--              <component class="icons" :is="itemChildren.icon"></component>-->
              <span>{{ itemChildren.label }}</span>
            </el-menu-item>
          </template>
        </el-sub-menu>
        <!--      没有子分类的显示列表-->
        <el-menu-item v-else :index="item.path" :key="item.path">
          <!--          <component class="icons" :is="item.icon"></component>-->
          <span>{{ item.label }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script>
import systemApplicationParams from "../../sys/SystemApplicationParams.js";

export default {
  name: "CommonAside",
  data() {
    return {
      MenuJson: [],
      newTime: '',
    }
  },
  created() {
    //加载动态菜单
    this.getAll()
  },
  mounted() {
  },
  methods: {
    cdClick(name) {
      this.$store.commit('setActiveIndex', name)
    },
    //获取动态菜单
    getAll() {
      this.MenuJson = JSON.parse(localStorage.getItem(systemApplicationParams.params.menu));
      // menu.getAll().then(res=>{
      // this.MenuJson=res.data;
      //
      // }).catch(err=>{
      //   console.log(err)
      // })
    },
  }
}
</script>

<style>
.icons {
  width: 18px;
  height: 18px;
  margin-right: 4px;
}

.aside {
  overflow-x: hidden;
  overflow-y: scroll;
  height: 100%;
}

.aside::-webkit-scrollbar {
  /*隐藏滚动条*/
  display: none; /* Chrome和Safari */
}

.el-menu-vertical-demo {
  width: 100%;
  height: 100vh;
  border-right: none;
}

.el-menu-vertical-demo h3 {
  color: #fff;
  text-align: center;
  /*margin-top: 10px;*/
}
</style>
