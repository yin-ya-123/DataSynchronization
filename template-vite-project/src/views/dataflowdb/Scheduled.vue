<script lang="ts">
import {defineComponent} from 'vue'
import scheduledApi from '../../api/dataflowdb/ScheduledApi'
import scheduledDetailsApi from '../../api/dataflowdb/scheduledDetailsApi'
import sqlParamsApi from '../../api/dataflowdb/SqlParamsApi'
import {ElMessage, ElMessageBox} from "element-plus";
import dynamicDataSourceApi from '../../api/dataflowdb/DynamicDataSourceApi'
import fieldMappingApi from '../../api/dataflowdb/FieldMappingApi'
import listUtils from '../../utils/common/ListUtils'
import stringUtils from '../../utils/common/StringUtils'

export default defineComponent({
  name: "Scheduled",
  data() {
    return {
      //定时任务数据信息
      scheduledS: [],
      scheduled: {
        cronId: "1",
        cronName: "定时器任务一",
        cronType: '1',
        cron: "0/6 * * * * ?",
        status: "1",
        delFlag: "0",
        sort: null,
        pagePojo: null
      },
      //分页数据信息
      pagePojo: {
        total: 0
      },
      //任务参数信息
      scheduledPar: {
        cronId: null,
        cronName: null,
        cron: null,
        status: null,
        delFlag: null,
        pagePojo: {
          pageNumber: 1,
          pageSize: 50
        },
      },
      //窗体信息
      typeDialog: false,
      dialogTitle: "任务信息",
      //操作类型
      cz: null,
      // 添加表单验证规则
      rules: {
        cronName: [
          {required: true, message: '请输入任务名称', trigger: 'blur'}
        ],
        cron: [
          {required: true, message: '请输入任务表达式', trigger: 'blur'}
        ],
        cronType: [
          {required: true, message: '请选择任务类型', trigger: 'blur'}
        ]
      },
      ///////////////////////////////////
      //定时任务详情数据信息
      scheduledDetatilS: [],
      scheduledDetatil: {
        id: "",
        zsjMc: "",
        zsjSql: "",
        zsjKs: "",
        toMc: "",
        toSql: "",
        toKs: "",
        czLx: "",
        scheduledId: "1",
        delFlag: "0",
        pagePojo: null
      },
      //明细校验
      rulesD: {
        zsjMc: [
          {required: true, message: '请输入源表名称', trigger: 'blur'}
        ],
        zsjSql: [
          {required: true, message: '请输入源表查询语句', trigger: 'blur'}
        ],
        zsjKs: [
          {required: true, message: '请输入源表标记', trigger: 'blur'}
        ],
        toMc: [
          {required: true, message: '请输入目标表名称', trigger: 'blur'}
        ],
        toSql: [
          {required: true, message: '请输入目标表查询语句', trigger: 'blur'}
        ],
        toKs: [
          {required: true, message: '请输入对方数据库标记', trigger: 'blur'}
        ]
      },
      //数据源信息
      dataSourceList: [],
      //字段映射相关数据
      fieldMappingList: [],
      //老的字段映射信息
      oldFieldMappingList: [],
      //字段映射信息
      fieldMapping: {
        mappingId: "",
        scheduledId: "",
        sourceSystem: "",
        sourceTable: "",
        sourceField: "",
        targetSystem: "",
        targetTable: "",
        targetField: "",
        dataTypeMapping: "",
        mappingRule: "",
        delFlag: "0"
      },
      //字段映射窗体处理
      fieldMappingDialog: false,
      //窗体加载
      loadingFlag: false,
      //参数相关信息
      paramDialog: false,
      isMrValue: "",
      //参数的任务保存数据
      scheduledParData: {
        cronId: "1",
        cronName: "定时器任务一",
        cronType: '1',
        cron: "0/6 * * * * ?",
        status: "1",
        delFlag: "0",
        sort: null,
        pagePojo: null,
        sqlParams: []
      },
      //参数信息
      sqlParamPar: {
        id: '',
        scheduledId: '',
        paramName: '',
        paramValue: '',
        paramType: '',
        description: '',
        createTime: null,
        isMr: '0', //0 非默认 1默认参数
        delFlag: '0'  // 默认值：0-未删除，1-已删除
      },
      sqlParams: [
        {
          id: '',
          scheduledId: '',
          paramName: '',
          paramValue: '',
          paramType: '',
          description: '',
          createTime: null,
          isMr: '0', //0 非默认 1默认参数
          delFlag: '0'  // 默认值：0-未删除，1-已删除
        }
      ]
    }
  },
  created() {
    this.pageTask();
    this.listEnabled()
  },
  methods: {
    listEnabled() {
      dynamicDataSourceApi.listEnabled().then(res => {
        this.dataSourceList = res.data
      })
    },
    //分页查询定时任务信息
    pageTask() {
      scheduledApi.pageTask(this.scheduledPar).then(res => {
        this.scheduledS = res.data.records
        this.pagePojo.total = res.data.total
      })
    },
    //点击显示大小
    changeSize(data) {
      this.scheduledPar.pagePojo.pageSize = data
      this.pageTask()
    },
    //切换第几页
    changeCurrent(data) {
      this.scheduledPar.pagePojo.pageNumber = data
      this.pageTask()
    },
    //任务的启用停用
    changeStatus(cz, data) {
      this.scheduled = data
      if (cz === 0) {
        this.scheduled.status = "0"
        this.updateTask2()
      } else if (cz === 1) {
        this.scheduled.status = "1"
        this.updateTask2()
      }
    },
    //表单验证 1 任务添加 2 任务修改
    validate(type) {
      if (this.$refs.scheduledForm && this.$refs.scheduledForm2) {
        this.$refs.scheduledForm.validate((valid) => {
          if (valid) {
            this.$refs.scheduledForm2.validate((valid2) => {
              if (valid2) {
                this.loadingFlag = true
                if (type === 1) {
                  this.addTask()
                } else if (type === 2) {
                  this.updateTask()
                }
              }
            })
          }
        })
      }
    },
    //单纯的任务修改
    updateTask2() {
      scheduledApi.updateTask(this.scheduled).then(res => {
        ElMessage.success(res.message)
        this.pageTask()
        this.loadingFlag = false
      }).catch(err => {
        ElMessage.error(err.message)
        this.loadingFlag = false
      })
    },
    //任务修改
    updateTask() {
      scheduledApi.updateTask(this.scheduled).then(res => {
        //明细修改
        scheduledDetailsApi.updateScheduledDetails(this.scheduledDetatil).then(res => {
          ElMessage.success(res.message)
          this.pageTask()
          this.loadingFlag = false
        }).catch(err => {
          ElMessage.error("任务明细:" + err.message)
          this.loadingFlag = false
        })
      }).catch(err => {
        ElMessage.error("任务修改:" + err.message)
        this.loadingFlag = false
      })
    },
    //任务新增
    addTask() {
      this.scheduled.status = "1"
      this.scheduled.delFlag = "0"
      scheduledApi.addTask(this.scheduled).then(res => {
        //明细添加
        this.scheduledDetatil.delFlag = "0"
        this.scheduledDetatil.scheduledId = res.data.cronId
        scheduledDetailsApi.addScheduledDetails(this.scheduledDetatil).then(res => {
          ElMessage.success(res.message)
          this.pageTask()
          this.loadingFlag = false
          this.clearPojo()
        }).catch(err => {
          ElMessage.error("任务明细添加:" + err.message)
          this.loadingFlag = false
        })
      }).catch(err => {
        ElMessage.error("任务添加:" + err.message)
        this.loadingFlag = false
      })

    },
    //任务删除
    deleteTask(data) {
      ElMessageBox.confirm('确定操作吗',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          })
          .then(() => {
            this.scheduled = data
            scheduledApi.deleteTask(this.scheduled).then(res => {
              ElMessage.success(res.message)
              this.pageTask()
            })
          })
          .catch(() => {
            // catch error
          })
    },
    //任务类型点击事件
    changeTaskType(data) {
      if (data === '2') {
        this.scheduledDetatil.toMc = "-"
        this.scheduledDetatil.toSql = "-"
        this.scheduledDetatil.toKs = "-"
      }
    },
    //初始化表对应
    initFieldMapping() {
      this.loadingFlag = true
      fieldMappingApi.initFieldMapping(this.scheduledDetatil).then(res => {
        ElMessage.success(res.message)
        this.loadingFlag = false
      }).catch(err => {
        ElMessage.error(err.message)
        this.loadingFlag = false
      })
    },
    //字段对于明细查询
    getFieldMappingList() {
      this.oldFieldMappingList = []
      const par = {
        scheduledId: this.scheduledDetatil.scheduledId
      }
      fieldMappingApi.getFieldMappingList(par).then(res => {
        this.fieldMappingList = res.data
        this.oldFieldMappingList = JSON.parse(JSON.stringify(res.data))
      })
    },
    //字段保存
    saveFieldMapping() {
      this.fieldMappingList.forEach(item => {
        if (
            listUtils.containDataField(item, "targetField", this.oldFieldMappingList) === false
            ||
            listUtils.containDataField(item, "dataTypeMapping", this.oldFieldMappingList) === false
        ) {
          fieldMappingApi.updateFieldMapping(item).then(res => {
            ElMessage.success(res.message)
            this.getFieldMappingList()
          })
        }
      })
    },
    //窗体操作
    //窗体打开操作 1 类型新增 2 类型修改
    dialogOpen(type, data) {
      this.typeDialog = true
      this.scheduled = data
      this.cz = type
      if (type === 1) {
        this.dialogTitle = "新增任务"
        this.clearPojo()
      } else if (type === 2) {
        this.dialogTitle = "修改任务"
        this.scheduledDetatil.scheduledId = data.cronId
        scheduledDetailsApi.getScheduledDetails(this.scheduledDetatil).then(res => {
          this.scheduledDetatil = res.data
        })
      }
    },
    //窗体关闭操作,
    dialogClose() {
      this.typeDialog = false
      this.$refs.scheduledForm2.clearValidate()
      this.$refs.scheduledForm.clearValidate()
      this.clearPojo()
      this.pageTask()
    },
    //字段映射窗体打开操作
    dialogOpenField() {
      this.fieldMappingDialog = true
      this.getFieldMappingList()
    },
    //字段映射窗体关闭操作,
    dialogCloseField() {
      this.fieldMappingDialog = false
      this.clearFieldMappingPojo()
    },
    clearPojo() {
      this.scheduledDetatil = {
        id: null,
        zsjMc: "",
        zsjSql: "",
        zsjKs: null,
        toMc: "",
        toSql: "",
        toKs: null,
        czLx: "",
        scheduledId: "1",
        delFlag: "0",
        pagePojo: null
      }
      this.scheduled = {
        cronId: "",
        cronName: "",
        cron: "",
        status: "",
        delFlag: "",
        sort: "",
        pagePojo: null
      }
    },
    //字段映射数据清理
    clearFieldMappingPojo() {
      this.fieldMapping = {
        mappingId: "",
        scheduledId: "",
        sourceSystem: "",
        sourceTable: "",
        sourceField: "",
        targetSystem: "",
        targetTable: "",
        targetField: "",
        dataTypeMapping: "",
        mappingRule: "",
        delFlag: "0"
      }
    },
    //任务立即执行一次
    startScheduled(data) {
      this.sqlParams.length = 0
      //获取任务对应的明细数据
      const par = {
        scheduledId: data.cronId
      }
      this.scheduledParData = data
      scheduledDetailsApi.getScheduledDetails(par).then(res => {
        const data1 = res.data
        const parList = stringUtils.getUniquePlaceholders(data1.zsjSql)
        if (parList.length > 0) {
          this.paramDialog = true
          this.isMrValue = 0
          parList.forEach(item => {
            //先获取是否存在参数
            sqlParamsApi.get({paramName: item, scheduledId: data1.scheduledId, isMr: 0}).then(res => {
              if (res.data) {
                res.data.id = null
                this.sqlParams.push(res.data)
              } else {
                this.sqlParams.push(
                    {
                      paramName: item,
                      paramValue: "",
                      paramType: "字符",
                      isMr: 0, //0 非默认 1默认参数
                    }
                )
              }
            })
            // console.log(item)
          })
        } else {
          scheduledApi.startScheduled(data).then(res => {
            ElMessage.success(res.message)
          })
        }
      })
    },
    //参数框关闭方法
    paramDialogClose() {
      this.paramDialog = false
    },
    //参数框的立即执行
    startScheduledNowPar() {
      this.scheduledParData.sqlParams = this.sqlParams
      scheduledApi.startScheduled(this.scheduledParData).then(res => {
        ElMessage.success(res.message)
      })
    },
    //参数框的下拉选项
    checkedParLx(val, item) {
      // console.log(item)
      item.paramType = val
      item.paramValue = ""
    },
    //测试SQL参数
    testSql() {
      this.sqlParams.forEach(item => {
        item.isMr = this.isMrValue
      })
      this.scheduledParData.sqlParams = this.sqlParams
      sqlParamsApi.testSql(this.scheduledParData).then(res => {
        ElMessage.success(res.message)
      }).catch(err => {
        ElMessage.error(err.message)
      })
    },
    //参数单选按钮的事件
    radioChange(val) {
      this.isMrValue = val
      //切换之后，重新查询参数切换参数
      this.sqlParams.forEach(item => {
        sqlParamsApi.get({paramName: item.paramName, scheduledId: item.scheduledId, isMr: this.isMrValue}).then(res => {
          if (res.data) {
            //修改this.sqlParams的value
            item.paramValue = res.data.paramValue
            item.id = res.data.id
            item.paramType = res.data.paramType
          } else {
            item.paramValue = ""
            item.id = ""
            item.paramType = "默认"
          }
        })
      })
    }
  }
})
</script>

<template>
  <div class="Scheduled">
    <el-card :body-style="{ padding: '6px' }" class="card1">
      <el-form size="small" inline v-model="scheduledPar">
        <el-form-item label="任务ID">
          <el-input placeholder="输入任务ID" v-model="scheduledPar.cronId"></el-input>
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input placeholder="任务名称" v-model="scheduledPar.cronName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" @click="dialogOpen(1, null)">新增</el-button>
          <el-button size="small" type="primary" @click="pageTask">刷新</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="card2">
      <el-table size="small" class="table" :data="scheduledS" border :header-cell-style="{ 'text-align': 'center' }"
                :highlight-current-row="true" height="73vh">
        <el-table-column label="ID" prop="cronId" align="center" width="200"/>
        <el-table-column label="任务名称" prop="cronName" align="center" width="200"/>
        <el-table-column label="任务表达式" prop="cron" align="center" width="200"/>
        <el-table-column label="任务类型" prop="cronType" align="center" width="70"/>
        <el-table-column label="序号" prop="sort" align="center" width="80"/>
        <el-table-column label="启用状态" align="center" width="100">
          <template #default="scope">
            {{ scope.row.status === '0' ? '启用' : '停用' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button size="small" type="primary" @click="startScheduled(scope.row)">立即执行</el-button>
            <el-button size="small" type="primary" @click="changeStatus(0, scope.row)">启用</el-button>
            <el-button size="small" type="primary" @click="changeStatus(1, scope.row)">停止</el-button>
            <el-button size="small" type="primary" @click="deleteTask(scope.row)">删除</el-button>
            <el-button size="small" type="primary" @click="dialogOpen(2, scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: center">
        <el-pagination :size="'small'" @size-change="changeSize" @current-change="changeCurrent"
                       :page-sizes="[10, 50, 100, 200, 300, 400]" :pageSize="100"
                       layout="sizes,prev, pager, next,jumper,total"
                       :total="Number(pagePojo.total)"/>
      </div>
    </el-card>
  </div>
  <div class="dialog">
    <el-dialog draggable width="800px" :title="dialogTitle" v-model="typeDialog" :before-close="() => { }">
      <el-card v-loading="loadingFlag">
        <el-form ref="scheduledForm" :rules="rules" size="small" :model="scheduled" :inline="true">
          <el-form-item label="任务名称" prop="cronName">
            <el-input v-model="scheduled.cronName" placeholder="请输入任务名称"/>
          </el-form-item>
          <el-form-item label="任务表达式" prop="cron">
            <el-input v-model="scheduled.cron" placeholder="请输入任务表达式"/>
          </el-form-item>
          <el-form-item label="序号" prop="sort">
            <el-input style="width: 100px" v-model="scheduled.sort" placeholder="请输入序号"/>
          </el-form-item>
          <el-form-item label="任务类型" prop="cronType">
            <el-select style="width: 100px" v-model="scheduled.cronType" placeholder="选择任务类型" value-key="id"
                       @change="changeTaskType">
              <el-option id="1" value="数据同步" label="数据同步">数据同步</el-option>
              <el-option id="2" value="定时任务" label="定时任务">定时任务</el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-form ref="scheduledForm2" :rules="rulesD" size="small" :model="scheduledDetatil" label-width="100">
          <el-form-item label="主数据名称" prop="zsjMc">
            <el-input v-model="scheduledDetatil.zsjMc" placeholder="请输入主数据名称"/>
          </el-form-item>
          <el-form-item label="主数据sql" prop="zsjSql">
            <el-input type="textarea" resize="none" :autosize="{ minRows: 4, maxRows: 4 }"
                      v-model="scheduledDetatil.zsjSql" placeholder="请输入主数据sql"/>
          </el-form-item>
          <el-form-item label="主数据源标记" prop="zsjKs">
            <el-select style="width: 200px" v-model="scheduledDetatil.zsjKs" placeholder="Select" value-key="id">
              <el-option v-for="item in dataSourceList" :key="item.id" :label="item.dsKey" :value="item.dsKey">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="对方名称" prop="toMc">
            <el-input v-model="scheduledDetatil.toMc" placeholder="请输入对方名称"/>
          </el-form-item>
          <el-form-item label="对方表" prop="toSql">
            <el-input type="textarea" resize="none" :autosize="{ minRows: 4, maxRows: 4 }"
                      v-model="scheduledDetatil.toSql" placeholder="请输入对方表"/>
          </el-form-item>
          <el-form-item label="对方数据库标记" prop="toKs">
            <el-select style="width: 200px" v-model="scheduledDetatil.toKs" placeholder="Select" value-key="id">
              <el-option v-for="item in dataSourceList" :key="item.id" :label="item.dsKey" :value="item.dsKey">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button v-if="cz === 1" type="primary" @click="validate(1)" size="small">
              保存
            </el-button>
            <el-button v-if="cz === 2" type="primary" @click="validate(2)" size="small">
              修改
            </el-button>
            <el-button @click="dialogClose" size="small">
              取消
            </el-button>
            <el-button v-if="dialogTitle !== '新增任务'" type="primary" @click="initFieldMapping" size="small">
              映射初始化
            </el-button>
            <el-button v-if="dialogTitle !== '新增任务'" type="primary" size="small" @click="dialogOpenField">
              打开映射
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
    <!-- 字段对应关系 -->
    <el-dialog draggable width="800px" title="字段映射处理" v-model="fieldMappingDialog" :before-close="() => { }">
      <el-card>
        <el-table :data="fieldMappingList" style="width: 100%"
                  :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
                  :cell-style="{ background: '#fafafa' }"
                  border height="500">
          <el-table-column label="主数据字段" align="center">
            <template #default="scope">
              <el-input v-model="scope.row.sourceField" placeholder="请输入主数据字段"/>
            </template>
          </el-table-column>
          <el-table-column label="对方字段" align="center">
            <template #default="scope">
              <el-input v-model="scope.row.targetField" placeholder="请输入对方字段"/>
            </template>
          </el-table-column>
          <el-table-column label="目标日期转换类型">
            <template #default="scope">
              <el-select style="width: 200px" v-model="scope.row.dataTypeMapping" placeholder="请选择" value-key="id">
                <el-option id="1" value="无" label="无">无</el-option>
                <el-option id="2" value="年月日时分秒" label="年月日时分秒">年月日时分秒</el-option>
                <el-option id="3" value="年月日" label="年月日">年月日</el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button type="primary" @click="saveFieldMapping" size="small">
              保存
            </el-button>
            <el-button @click="dialogCloseField" size="small">
              取消
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
    <!--立即执行参数框-->
    <el-dialog draggable width="700px" title="参数处理" v-model="paramDialog" :before-close="() => { }">
      <el-card v-loading="loadingFlag">
        <el-col>
          <el-row style="margin-bottom: 10px">
            <el-radio-group @change="radioChange" size="small" v-model="isMrValue">
              <el-radio :value="1">默认参数</el-radio>
              <el-radio :value="0">执行参数</el-radio>
            </el-radio-group>
          </el-row>
          <el-row>
            <template v-for="(item, index) in sqlParams">
              <el-form ref="scheduledForm" size="small" :model="item" :inline="true">
                <el-form-item label="占位符" prop="paramName">
                  <el-input style="width: 60px" v-model="item.paramName" placeholder=""/>
                </el-form-item>
                <el-form-item label="参数类型" prop="paramType">
                  <el-select style="width: 100px" v-model="item.paramType" placeholder="选择任务类型" value-key="id"
                             @change="checkedParLx(item.paramType, item)">
                    <el-option id="1" value="日期(天)" label="日期(天)">日期(天)</el-option>
                    <el-option id="2" value="日期(时分秒)" label="日期(时分秒)">日期(时分秒)</el-option>
                    <el-option id="3" value="字符" label="字符">字符</el-option>
                    <el-option id="4" value="默认" label="默认">默认</el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="值" prop="paramValue">

                  <el-tooltip content="字符根据输入的文字直接替换占位符增加''包裹着" placement="top" effect="dark">
                    <el-input v-if="item.paramType === '字符'" style="width:210px" v-model="item.paramValue"
                              placeholder="字符根据输入的文字直接替换占位符增加''包裹着"/>
                  </el-tooltip>
                  <el-tooltip content="默认则直接替换写入的信息" placement="top" effect="dark">
                    <el-input v-if="item.paramType === '默认'" style="width:210px" v-model="item.paramValue"
                              placeholder="默认则直接替换写入的信息"/>
                  </el-tooltip>

                  <el-tooltip content="根据输入的文字直接替换占位符增加''包裹着" placement="top" effect="dark">
                    <span>
                      <el-date-picker v-if="item.paramType === '日期(天)'" v-model="item.paramValue" type="date"
                                      placeholder="根据输入的文字直接替换占位符增加''包裹着"/>
                    </span>
                  </el-tooltip>

                  <el-tooltip content="根据输入的文字直接替换占位符增加''包裹着" placement="top" effect="dark">
                    <span>
                      <el-date-picker v-if="item.paramType === '日期(时分秒)'" v-model="item.paramValue" type="datetime"
                                      placeholder="根据输入的文字直接替换占位符增加''包裹着"
                                      format="YYYY-MM-DD HH:mm:ss"
                                      value-format="YYYY-MM-DD HH:mm:ss" title=""/>
                    </span>
                  </el-tooltip>
                </el-form-item>
              </el-form>

            </template>
          </el-row>
        </el-col>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button type="primary" @click="testSql()" size="small">
              参数验证
            </el-button>
            <el-button type="primary" @click="startScheduledNowPar()" size="small">
              立即执行
            </el-button>
            <el-button @click="paramDialogClose" size="small">
              取消
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>

<style scoped>
.Scheduled {
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