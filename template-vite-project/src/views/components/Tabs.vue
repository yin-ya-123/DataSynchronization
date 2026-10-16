<template>
  <div class="myTable">
    <!--    @edit="closeClick" -->
    <el-tabs v-model="activeName" editable type="card" class="demo-tabs"
             @tab-click="handleClick"
             @tab-remove="removeTab"
    >
      <el-tab-pane
          v-for="(item,index) in tabsData"
          :key="index"
          :label="item.name"
          :name="item.path"
      ></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: "tabs",
  computed: {
  },
  watch: {
    $route(to, form) {
      // console.log(form) //旧的地址
      // console.log(to) //去新的地址
      const data = {
        path: to.path,
        name: to.meta.name,
        index: to.meta.index
      }
      this.setRoute(data)
      this.tabsData = this.$store.state.openTab
      this.activeName = this.$store.state.activeIndex
      // console.log(this.tabsData)
    }
  },
  data() {
    return {
      tabsData: [],
      activeName: '',
      dialogVisible: false,
      key: '',
      value: '',
      apiResponse: '',
    }
  },
  created() {
    this.getTabs()
  },
  methods: {
    handleClick(tab, event) {
      // console.log(tab, event)
      this.toPage(tab.props.name)
      this.$store.commit('setActiveIndex', tab.props.name)
    },
    // closeClick(targetName, action) {
    //点击任意差删除表签页
    removeTab(targetName) {
      this.$store.commit('removeTab', targetName)
      //自动跳转标签页的倒数第一页
      const toUrl = this.tabsData[this.tabsData.length - 1];
      //   让删除后的标签页显示蓝色
      this.$store.commit('setActiveIndex', toUrl.path)
      this.toPage(toUrl.path)
    },
    toPage(url) {
      this.$router.push({path: url})
    },
    setRoute(route) {
      this.$store.commit('addTab', route)
    },
    getTabs() {
      this.tabsData = this.$store.state.openTab
    }
  }
}
</script>

<style>
.el-tabs__header.is-top{
  margin-bottom: 5px;
}
</style>
