<script lang="ts">
import {defineComponent} from 'vue'
import dynamicDataSourceApi from '../../api/dataflowdb/DynamicDataSourceApi'
import {ElMessage} from "element-plus";


export default defineComponent({
  name: "DynamicDataSource",
  data() {
    return {
      dataSourceList: [],
      dataSource: {
        id: null,
        dsKey: null,
        url: null,
        username: null,
        password: null,
        driverClass: null,
        isDefault: 0,
        status: 1,
        dsLx: null,
        delFlag: 0
      },
      //窗体
      dialogVisible: false,
      loading: false,
      loadingData: false
    }
  },
  created() {
    this.listEnabled()
  },
  methods: {
    listEnabled() {
      this.loadingData = true
      dynamicDataSourceApi.listEnabled().then(res => {
        this.dataSourceList = res.data
        this.loadingData = false
      })
    },
    dataSourceCon() {
      this.loading = true
      dynamicDataSourceApi.dataSourceCon(this.dataSource).then(res => {
        ElMessage.success(res.message)
        this.loading = false
      }).catch(err=>{
        ElMessage.error(err.message)
        this.loading = false
      })
    },
    //窗体打开操作 1 类型新增 2 类型修改
    dialogOpen(type, row) {
      this.dialogVisible = true
      this.dataSource = row
      if (type === 1) {
        this.dataSource.dsKey = null
      }
    },
    //窗体关闭操作
    dialogClose() {
      this.dialogVisible = false
    },
    //数据源刷新
    refresh() {
      this.loadingData = true
      dynamicDataSourceApi.refresh().then(res => {
        ElMessage.success("数据源刷新成功")
        this.listEnabled()
        this.loading = false
      }).catch(err=>{
        ElMessage.error(err.message)
        this.loading = false
      })
    },
    //保存操作
    dataSourceM() {
      if (this.dataSource.dsKey === null) {
        ElMessage.error("数据源不能为空")
        return
      }
      this.loading = true
      this.dataSource.delFlag = 0
      dynamicDataSourceApi.dataSource(this.dataSource).then(res => {
        ElMessage.success(res.message)
        this.dialogClose()
        this.refresh()
      }).catch(err=>{
        ElMessage.error(err.message)
        this.loading = false
      })
    },
    //修改操作
    updateDataSource() {

    }
  }
})
</script>

<template>
  <div class="DynamicDataSource">
    <el-card :body-style="{padding:'0px'}" class="card1">
      &nbsp;
      <el-button size="small" type="primary" @click="dialogOpen(1,{})">新增</el-button>
      <el-button size="small" type="primary" @click="refresh">数据源刷新</el-button>
      <el-button size="small" type="primary" @click="listEnabled">刷新</el-button>
    </el-card>
    <el-card class="card2" v-loading="loadingData">
      <el-table size="small" class="table" :data="dataSourceList"
                border
                :header-cell-style="{'text-align':'center'}"
                :highlight-current-row="true"
      >
        <el-table-column label="数据源标识" prop="dsKey" align="center"/>
        <el-table-column label="url" prop="url" align="center"/>
        <el-table-column label="状态" align="center">
          <template #default="scope">
            {{ scope.row.delFlag === '0' ? '启用' : '停用' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button @click="dialogOpen(2,scope.row)" size="small" type="primary">修改</el-button>
            <el-button size="small" type="primary">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
  <div class="dialog">
    <el-dialog title="数据源配置" v-model="dialogVisible" width="30%" :before-close="()=>{}">
      <el-form v-loading="loading" ref="dataSource" :model="dataSource" label-width="100px">
        <el-form-item label="数据源标识">
          <el-input v-model="dataSource.dsKey" placeholder="请输入数据源标识"/>
        </el-form-item>
        <el-form-item label="数据库类型">
          <el-select v-model="dataSource.dsLx" placeholder="请输入数据库类型">
            <el-option label="mySql" value="mySql"></el-option>
            <el-option label="oracle" value="oracle"></el-option>
            <el-option label="sqlServer" value="sqlServer"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="url">
          <el-input v-model="dataSource.url" placeholder="请输入url"/>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="dataSource.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="dataSource.password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="驱动类">
          <el-select v-model="dataSource.driverClass" placeholder="请输入驱动类">
            <el-option label="mysql" value="com.mysql.cj.jdbc.Driver"></el-option>
            <el-option label="oracle" value="oracle.jdbc.driver.OracleDriver"></el-option>
            <el-option label="sqlserver" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"></el-option>
          </el-select>
        </el-form-item>
        <el-button @click="dataSourceCon">测试连接</el-button>
        <el-button type="primary" @click="dataSourceM">保存</el-button>
        <el-button @click="dataSourceM">修改</el-button>
        <el-button @click="dialogClose">取消</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<style scoped>
.DynamicDataSource {
  height: 85vh;

  .card1 {
    height: 5vh;
    margin-bottom: 5px;
    line-height: 5vh;
  }

  .card2 {
    height: 80vh;
  }
}
</style>