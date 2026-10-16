<script lang="ts">
import {defineComponent} from 'vue'
import errorLogApi from '../../api/dataflowdb/ErrorLogApi'

export default defineComponent({
  name: "ErrorLog",
  data() {
    return {
      //日志数据
      errorLogS: [],
      errorLog: {
        errorId: "",
        scheduledId: "",
        errorType: "",
        errorData: null,
        errorDescription: "",
        delFlag: "0",
        createTime: "",
        createUser: "",
        updateTime: null,
        updateUser: null,
        pagePojo: null
      },
      //分页数据
      pagePojo: {
        total: 0
      },
      //参数信息
      errorLogPar: {
        errorType:'',
        scheduledId: "",
        pagePojo: {
          pageNumber: 1,
          pageSize: 50
        }
      },
      //加载状态
      errLoading: false

    }
  },
  created() {
    this.getErrorLogListPage()
  },
  methods: {
    //日志查询
    getErrorLogListPage() {
      this.errLoading = true
      errorLogApi.getErrorLogListPage(this.errorLogPar).then(res => {
        this.errorLogS = res.data.records
        this.pagePojo.total = res.data.total
        this.errLoading = false
      }).catch(err => {
        ElMessage.error(err.message)
        this.errLoading = false
      })
    },
    //分页操作
    //点击显示大小
    changeSize(data) {
      this.errorLogPar.pagePojo.pageSize = data
      this.getErrorLogListPage()
    },
    //切换第几页
    changeCurrent(data) {
      this.errorLogPar.pagePojo.pageNumber = data
      this.getErrorLogListPage()
    }

  }
})
</script>

<template>

  <div class="ErrorLog">
    <el-card :body-style="{padding:'6px'}" class="card1">
      <el-form size="small" v-model="errorLogPar" inline>
        <el-form-item label="任务ID">
          <el-input placeholder="请输入任务ID" v-model="errorLogPar.scheduledId"></el-input>
        </el-form-item>
        <el-form-item label="任务类型">
          <el-input placeholder="数据任务类型,全匹配" v-model="errorLogPar.errorType"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" @click="getErrorLogListPage">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="card2">
      <el-table v-loading="errLoading" size="small" class="table" :data="errorLogS"
                border
                height="73vh"
                :header-cell-style="{'text-align':'center'}"
                :highlight-current-row="true"
      >
        <el-table-column label="日志ID" prop="errorId" width="150" align="center"/>
        <el-table-column label="任务ID" prop="scheduledId" width="150" align="center"/>
        <el-table-column label="日志状态" prop="errorType" width="120" align="center"/>
        <el-table-column label="创建时间" prop="createTime" width="150" align="center"/>
        <el-table-column label="数据信息" prop="errorData" :show-overflow-tooltip="true" align="center"/>
        <el-table-column label="报错信息" prop="errorDescription" :show-overflow-tooltip="true" width="300" align="center"/>
        <!--        <el-table-column label="errorType">-->
        <!--          <template #default="scope">-->
        <!--            {{ scope.row.delFlag === '0' ? '启用' : '停用' }}-->
        <!--          </template>-->
        <!--        </el-table-column>-->
      </el-table>
      <div style="display: flex;justify-content: center;margin-top: 10px">
        <el-pagination :size="'small'"
                       @size-change="changeSize"
                       @current-change="changeCurrent"
                       :page-size="errorLogPar.pagePojo.pageSize"
                       :page-sizes="[10,50,100,200,300,400]"
                       layout="sizes,prev, pager, next,jumper,total"
                       :total="Number(pagePojo.total)"/>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.ErrorLog {
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