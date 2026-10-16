<script>
export default {
  name: "TestCs",
  data() {
    return {
      dataList: [],
      data: {
        id: '',
        name: ''
      }
    }
  },
  methods: {
    addList() {
      const data = {
        id: '',
        name: ''
      }
      this.dataList.push(data)
    },
    saveList() {
      console.log(this.dataList)
    },
    deleteList(index, row) {
      this.dataList.splice(index, 1)
    }
  }
}
</script>

<template>
  <!--  <el-menu-->
  <!--    :default-active="1"-->
  <!--    class="el-menu-demo"-->
  <!--    mode="horizontal"-->
  <!--    :ellipsis="false"-->
  <!--  >-->
  <!--  <el-menu-item index="0">-->
  <!--    <img-->
  <!--        alt="Element logo"-->
  <!--        src="../../assets/vue.svg"/>-->
  <!--  </el-menu-item>-->
  <!--  </el-menu>-->
  <el-form v-model="data">
    <el-form-item>
      <el-button @click="addList">添加列</el-button>
      <el-button @click="saveList">保存</el-button>
    </el-form-item>
    <el-table :data="dataList">
      <el-table-column type="index" label="序号"/>
      <el-table-column prop="id" label="id">
        <template #default="score">
          <el-input v-model="score.row.id"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="姓名">
        <template #default="score">
          <el-input v-model="score.row.name"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="score">
          <el-button @click="deleteList(score.$index,score.row)">删除列</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>

</template>

<style>

</style>