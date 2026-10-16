<script>
import {CirclePlus, Delete, Edit} from "@element-plus/icons-vue";
import dicUserApi from "../../api/user/DicUserApi.js";
import {ElMessage} from "element-plus";
import StringUtils from "../../utils/common/StringUtils.js";
import dicUserChargeApi from "../../api/jcxx/dicUserChargeApi.js";
import sysRoleApi from "../../api/jcxx/SysRoleApi.js";
//用户信息
export default {
  name: "DictUser",
  components: {CirclePlus, Delete, Edit},
  data() {
    return {
      //用户基本信息
      dicUserDataS: [], //用户信息查询数据
      //分页数据信息
      pagePojo: {
        total: 0
      },
      //用户信息参数
      dicUserPar: {
        zgh: '',
        xm: '',
        pagePojo: {
          pageNumber: 1,
          pageSize: 10
        },
      },
      //用户实体类数据信息
      dicUserPojo: {},
      //职工号/姓名的参数数据
      zgOrXm: '',
      //加载框
      loading: false,
      //弹窗加载
      //按钮操作
      buttonType: 1, //1 修改 0 新增
      saveButtonFlag: true,
      //用户角色信息
      dicUserChargeDataS: [],
      //窗体
      typeDialog: false,
      dialogTitle: '',
      //下拉框
      selValue: [],
      roleDataS: [],
    }
  },
  created() {
    this.getDicUserListPage()
  },
  methods: {
    //分页查询用户信息
    getDicUserListPage() {
      this.loading = true
      this.dicUserPar.zgh = this.zgOrXm
      this.dicUserPar.xm = this.zgOrXm
      dicUserApi.getDicUserListPage(this.dicUserPar).then(res => {
        this.dicUserDataS = res.data.records
        this.pagePojo.total = res.data.total
        this.loading = false
      })
    },
    //操作类型按钮
    buttonTypeClick(type) {
      //0 新增  1 修改 2 删除
      this.buttonType = type
      if (type === 0) {
        this.dicUserPojo = {}
        this.saveButtonFlag = false
      } else if (type === 1) {
        this.saveButtonFlag = true
      } else if (type === 2) {
        this.buttonClick()
      }
    },
    //保存按钮
    buttonClick() {
      if (StringUtils.strIsEmpty(this.dicUserPojo.zgh)) {
        const czType = this.buttonType === 0 ? '新增' : '修改';
        ElMessage.error("数据为空，无法" + czType + "操作")
        return;
      }
      if (this.buttonType === 1 || this.buttonType === 2) {
        if (StringUtils.strIsEmpty(this.dicUserPojo.id)) {
          let czType = '';
          if (this.buttonType === 1) {
            czType = '修改';
          }
          if (this.buttonType === 2) {
            czType = '删除';
          }
          ElMessage.error("数据异常无法进行" + czType + "!")
          return;
        }
      }
      this.dicUserPojo.delFlag = 0
      //0 新增  1 修改 2 删除
      if (this.buttonType === 0) {
        this.addDicUser()
      } else if (this.buttonType === 1) {
        this.updateDicUser()
      } else if (this.buttonType === 2) {
        this.deleteDicUser()
        this.buttonType = 1
      }
      this.dicUserPojo = {}
    },
    //用户新增
    addDicUser() {
      dicUserApi.addDicUser(this.dicUserPojo).then(res => {
        ElMessage.success(res.message)
        this.getDicUserListPage()
      })
    },
    deleteDicUser() {
      dicUserApi.deleteDicUser(this.dicUserPojo).then(res => {
        ElMessage.success(res.message)
        this.getDicUserListPage()
      })
    },
    updateDicUser() {
      dicUserApi.updateDicUser(this.dicUserPojo).then(res => {
        ElMessage.success(res.message)
        this.getDicUserListPage()
      })
    },
    //表格点击事件
    roleCheck(row, event, column) {
      this.dicUserPojo = row
      this.buttonTypeClick(1)
      this.getDicUserChargeAsRole()
    },
    //点击第几页
    changeSize(data) {
      this.pagePojo.pageNumber = data
      this.getDicUserListPage()
    },
    //切换显示数量大小
    changeCurrent(data) {
      this.pagePojo.pageSize = data
      this.getDicUserListPage()
    },
    //用户角色信息查询
    getDicUserChargeAsRole() {
      //构建参数
      const data = {
        userId: this.dicUserPojo.id
      }
      dicUserChargeApi.getDicUserChargeAsRole(data).then(res => {
        this.dicUserChargeDataS = res.data
      })
    },
    //角色字典信息查询
    getSysRolePojoListAsModule() {
      sysRoleApi.getSysRolePojoListAsModule({}).then(res => {
        this.roleDataS = res.data
      })
    },
    //用户角色信息新增
    addDicUserChargeS() {
      if (this.selValue.length === 0) {
        ElMessage.error("请勾选角色信息，再保存!")
        return;
      }
      const list = []
      for (let i = 0; i < this.selValue.length; i++) {
        const data = this.selValue[i]
        const dicUserChargeData = {
          userId: this.dicUserPojo.id,
          roleId: data.id,
          delFlag: 0
        }
        list.push(dicUserChargeData)
      }
      dicUserChargeApi.addDicUserChargeS(list).then(res => {
        ElMessage.success(res.message)
        this.getDicUserChargeAsRole()
        this.dialogClose()
      })
    },
    //用户角色信息删除
    deleteDicUserCharge(data) {
      dicUserChargeApi.deleteDicUserCharge(data).then(res => {
        ElMessage.success(res.message)
        this.getDicUserChargeAsRole()
      })
    },
    //角色下拉框
    selectChange() {
      // console.log(this.selValue)
    },
    //窗体打开操作 1 类型新增 2 类型修改
    dialogOpen(type) {
      if (StringUtils.strIsEmpty(this.dicUserPojo.zgh)) {
        ElMessage.error("数据为空，无法操作")
        return;
      }
      this.typeDialog = true
      if (type === 1 && this.roleDataS.length === 0) {
        this.getSysRolePojoListAsModule()
      }
    },
    //窗体关闭操作
    dialogClose() {
      this.typeDialog = false
      this.selValue = []
    },
  }
}
</script>

<template>
  <div class="dicUser">
    <el-card :body-style="{padding:'0px'}" class="card1">
      <div class="header">
        <div class="formHeader">
          &nbsp;&nbsp;
          <span style="font-size: 15px">职工号/姓名</span>&nbsp;&nbsp;
          <el-input size="small" v-model="zgOrXm" style="width: 200px"></el-input>
        </div>
        <div class="buttonHeader">
          &nbsp;&nbsp;
          <el-button @click="getDicUserListPage" type="primary" size="small">
            查询
          </el-button>
          <el-button @click="buttonClick" type="primary" size="small">
            保存
          </el-button>
          <el-button v-if="saveButtonFlag" @click="buttonTypeClick(0)" type="primary" size="small">
            添加新数据
          </el-button>
        </div>
      </div>
    </el-card>
    <el-card :body-style="{padding:'0px'}" class="card2">
      <el-row>
        <el-col :span="9">
          <div style="padding:5px">
            <el-table v-loading="loading" size="small"
                      class="table"
                      :data="dicUserDataS"
                      border
                      @row-click="roleCheck"
                      :highlight-current-row="true"
            >
              <el-table-column label="职工号" prop="zgh"/>
              <el-table-column label="姓名" prop="xm"/>
              <el-table-column label="状态">
                <template #default="scope">
                  {{ scope.row.delFlag === '0' ? '启用' : '停用' }}
                </template>
              </el-table-column>
            </el-table>
            <div class="date-pag">
              <el-pagination :size="'small'"
                             @size-change="changeSize"
                             @current-change="changeCurrent"
                             :page-sizes="[10,50,100,200,300,400]"
                             layout="sizes,prev, pager, next,jumper,total"
                             :total="pagePojo.total"/>
            </div>
          </div>
        </el-col>
        <el-col :span="15">
          <div style="padding:5px">
            <div style="margin-bottom: 5px">
              <el-text type="primary">当前状态:{{ buttonType === 0 ? '新增' : '修改' }}</el-text>&nbsp;&nbsp;
              <el-text type="success">当前选中:{{ dicUserPojo.xm }}</el-text>&nbsp;&nbsp;
              <el-button @click="buttonTypeClick(2)" type="danger" size="small">
                删除
              </el-button>
            </div>
            <el-form
                inline
                size="small"
                v-model="dicUserPojo"
            >
              <el-form-item label="职工号">
                <el-input v-model="dicUserPojo.zgh"></el-input>
              </el-form-item>
              <el-form-item label="姓名">
                <el-input v-model="dicUserPojo.xm"></el-input>
              </el-form-item>
            </el-form>
            <el-divider/>
            <el-text type="primary">当前角色</el-text>&nbsp;&nbsp;
            <el-button @click="dialogOpen(1)" type="primary" size="small">新增角色</el-button>
            <el-table size="small" class="table" :data="dicUserChargeDataS"
                      border
                      :highlight-current-row="true"
            >
              <el-table-column label="角色ID" prop="roleId"/>
              <el-table-column label="角色名称" prop="jssm"/>
              <el-table-column label="模块名称" prop="moduleMc"/>
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button @click="deleteDicUserCharge(scope.row)" type="danger" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
  <div class="dialog">
    <el-dialog draggable width="300" :title="dialogTitle" v-model="typeDialog"
               :before-close="()=>{}">
      <el-card>
        <el-select
            v-model="selValue"
            multiple
            placeholder="角色信息"
            @change="selectChange"
            value-key="id"
        >
          <el-option v-for="item in roleDataS"
                     :key="item.id"
                     :label="item.sysModulePojo.moudleMc+'-'+item.jssm"
                     :value="item"
          >
          </el-option>
        </el-select>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button @click="addDicUserChargeS" size="small" type="primary">
              保存
            </el-button>
            <el-button @click="dialogClose" size="small">
              取消
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>

<style scoped>
.dicUser {
  height: 85vh;

  .card1 {
    height: 5vh;
    line-height: 5vh;
    margin-bottom: 5px;

    .header {
      display: flex;
    }
  }

  .card2 {
    height: 80vh;

    .table {
      height: 75vh;
    }

    .date-pag {
      margin-left: 10%;
      line-height: 4vh;
      height: 4vh;
    }
  }
}
</style>
