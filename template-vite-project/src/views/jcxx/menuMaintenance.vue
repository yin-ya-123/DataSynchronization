<script>
import sysModuleApi from "../../api/jcxx/SysModuleApi.js";
import sysRoleApi from "../../api/jcxx/SysRoleApi.js";
import sysRoleDetailApi from "../../api/jcxx/SysRoleDetailApi.js";
import menuApi from "../../api/jcxx/MenuApi.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Close} from "@element-plus/icons-vue";
import StringUtils from "../../utils/common/StringUtils.js";
//权限分配
export default {
  name: "menuMaintenance",
  components: {Close},
  data() {
    return {
      //模块数据
      moduleDataS: [],
      selValue: {},
      modulePojo: {moduleId: ''},
      //角色数据信息
      roleTableDataS: [],
      roleCheckData: {},
      isUpdate: false, //强制刷新table数据
      rolePojo: {
        id: '',
        jssm: '',
        moduleId: '',
        delFlag: '0'
      },//角色操作信息
      //菜单列表信息
      defaultCheckedKeys: [],//默认勾选的
      menuDataS: [], //菜单数据
      treeCheckedKeys: [],//树节点上自己勾选的节点
      //加载框
      loading: false,
      //窗体
      typeDialog: false,
      dialogTitle: ''
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.getSysModuleList()
    },
    //获取模块数据
    getSysModuleList() {
      sysModuleApi.getSysModuleList(this.modulePojo).then(res => {
        // console.log(res)
        this.moduleDataS = res.data
        //默认第一个模块
        this.selValue = res.data[0]
        //联动查询角色信息
        this.getSysRoleList(res.data[0].moduleId)
        //联动查询菜单信息
        this.getMenuPojoDataS(res.data[0].moduleId)
      })
    },
    //获取角色信息
    getSysRoleList(moduleId) {
      //构建参数
      let data = {
        "moduleId": moduleId
      }
      sysRoleApi.getSysRoleList(data).then(res => {
        this.roleTableDataS = res.data
        this.isUpdate = !this.isUpdate
      })
    },
    //获取对应模块菜单信息
    getMenuPojoDataS(moduleId) {
      //构建参数
      let data = {
        "moduleId": moduleId
      }
      menuApi.getMenuPojoDataS(data).then(res => {
        this.menuDataS = res.data
      })
    },
    //获取角色对应的权限菜单列表
    getSysRoleDetailList(roleId) {
      //构建参数
      let data = {
        "roleId": roleId
      }
      this.defaultCheckedKeys = []
      sysRoleDetailApi.getSysRoleDetailList(data).then(res => {
        let data = res.data
        for (let i = 0; i < data.length; i++) {
          this.defaultCheckedKeys.push(data[i].menuId)
        }
        // console.log(data)
        // console.log(this.defaultCheckedKeys)
        //设置树界面数据勾选
        // console.log(this.defaultCheckedKeys)
        this.$refs.tree.setCheckedKeys(this.defaultCheckedKeys)
      })
    },
    //权限分配
    permissionAllocation() {
      if (this.permissionPd()) {
        this.loading = true
        //先删除角色对应的权限
        //构建参数
        let data = {
          "roleId": this.roleCheckData.id
        }
        let roleDataS = []
        sysRoleDetailApi.deleteSysRoleDetailPojoByPojo(data).then(async res => {
          //在进行重新插入权限
          for (let i = 0; i < this.treeCheckedKeys.length; i++) {
            //构建参数
            const menuRoleDetailLocal = {
              roleId: this.roleCheckData.id,
              delFlag: 0,
              jssm: this.roleCheckData.jssm,
              menuId: this.treeCheckedKeys[i]
            }
            roleDataS.push(menuRoleDetailLocal)
            // console.log(JSON.stringify(menuRoleDetailLocal))
          }
          sysRoleDetailApi.addSysRoleDetailPojoList(roleDataS).then(res => {
            //分配成功之后，重新查询角色下的权限
            this.getSysRoleDetailList(this.roleCheckData.id)
            ElMessage.success("权限分配成功!")
            this.loading = false
          })

        })
      }
    },
    //判断角色权限是否有变动
    permissionPd() {
      if (this.treeCheckedKeys.length > 0) {
        //判断选择的权限数量是否跟查询用户的一致，一致的话，判断是否完全相同
        let czFlag = 0;
        if (this.treeCheckedKeys.length === this.defaultCheckedKeys.length) {
          ElMessage.error("权限无变动!")
          return false
        }
        //否则返回true
        return true
      } else {
        ElMessage.error("请勾选用户权限!")
        return false
      }
    },
    //表格点击事件
    roleCheck(row, event, column) {
      //点击角色的时候，清空默认跟菜单数组,清空菜单树选中
      //设置树界面数据勾选
      // console.log(this.defaultCheckedKeys)
      this.$refs.tree.setCheckedKeys([])
      this.defaultCheckedKeys = []
      this.treeCheckedKeys = []
      this.roleCheckData = row
      this.getSysRoleDetailList(row.id)
    },
    //下拉框改变值事件
    selectChange() {
      //点击角色的时候，清空默认跟菜单数组,清空菜单树选中
      //设置树界面数据勾选
      // console.log(this.defaultCheckedKeys)
      this.$refs.tree.setCheckedKeys([])
      this.roleCheckData = {}
      this.getSysRoleList(this.selValue.moduleId)
      //联动查询菜单信息
      this.getMenuPojoDataS(this.selValue.moduleId)
    },
    //树节点勾选
    setCheckedNodes(data, checked) {
      if (checked) {
        this.treeCheckedKeys.push(data.id)
        // console.log("新增了:" + this.treeCheckedKeys)
      } else {
        for (let i = 0; i < this.treeCheckedKeys.length; i++) {
          if (data.id === this.treeCheckedKeys[i]) {
            this.treeCheckedKeys.splice(i, 1)
          }
        }
        // console.log("移除了:" + this.treeCheckedKeys)
      }
    },
    //窗体打开操作 1 类型新增 2 类型修改
    dialogOpen(type) {
      if (type === 1) {
        this.dialogTitle = '新增'
      } else if (type === 2) {
        if (StringUtils.strIsEmpty(this.roleCheckData.id)) {
          ElMessage.error("请选中角色")
          return
        }
        this.dialogTitle = '修改'
        //获取角色信息，赋值
        this.rolePojo.id = this.roleCheckData.id
        this.rolePojo.moduleId = this.roleCheckData.moduleId
        this.rolePojo.delFlag = this.roleCheckData.delFlag
        this.rolePojo.jssm = this.roleCheckData.jssm
      }
      this.typeDialog = true
    },
    //窗体关闭操作
    dialogClose() {
      this.typeDialog = false
      this.rolePojo = {}
      //重新查询
      //联动查询角色信息
      this.getSysRoleList(this.selValue.moduleId)
    },
    //窗体新增、修改按钮
    buttonClick() {
      //组装数据
      this.rolePojo.moduleId = this.selValue.moduleId
      this.rolePojo.delFlag = 0
      // console.log(this.rolePojo)
      ElMessageBox.confirm('确定操作吗',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          })
          .then(() => {
            if (this.dialogTitle === '新增') {
              this.addSysRole(this.rolePojo)
            } else if (this.dialogTitle === '修改') {
              this.updateSysRole(this.rolePojo)
            }
          })
          .catch(() => {
            // catch error
          })
    },
    //角色新增
    addSysRole(data) {
      sysRoleApi.addSysRole(data).then(res => {
        ElMessage.success("添加成功")
      })
    },
    //角色删除
    deleteSysRole() {
      sysRoleApi.deleteSysRole(this.roleCheckData).then(res => {
        ElMessage.success("删除成功")
        //重新查询
        //联动查询角色信息
        this.getSysRoleList(this.selValue.moduleId)
      })
    },
    //角色修改
    updateSysRole(data) {
      sysRoleApi.updateSysRole(data).then(res => {
        ElMessage.success("修改成功")
      })
    },
  }
}
</script>

<template>
  <div class="menuMaintenance">
    <el-card :body-style="{padding:'0px'}" class="elCard1">
      <div class="header">
        <div class="selHeader">
          &nbsp;&nbsp;&nbsp;<span style="font-size: 15px">模块</span>:
          <el-select
              class="elSelect"
              v-model="selValue"
              placeholder="Select"
              value-key="moduleId"
              @change="selectChange"
          >
            <el-option v-for="item in moduleDataS"
                       :key="item.moduleId"
                       :label="item.moudleMc"
                       :value="item"
            >
            </el-option>
          </el-select>
        </div>
        <div class="butHeader">
          <el-text type="primary">当前选中:{{ roleCheckData.jssm }}</el-text>&nbsp;&nbsp;&nbsp;
          <el-button size="small" @click="deleteSysRole" type="danger">删除</el-button>
          <el-button size="small" @click="dialogOpen(2)" type="primary">修改</el-button>
          <el-button size="small" @click="dialogOpen(1)" type="primary">新增</el-button>
          <el-button size="small" @click="permissionAllocation" type="success">权限分配</el-button>
        </div>
      </div>
    </el-card>
    <el-card :body-style="{padding:'0px'}" class="elCard2">
      <el-row>
        <el-col :span="18">
          <el-table v-loading="loading"
                    class="table" :data="roleTableDataS"
                    border
                    :key="isUpdate"
                    @row-click="roleCheck"
                    :highlight-current-row="true"
          >
            <el-table-column label="角色ID" prop="id" align="center"/>
            <el-table-column label="角色名称" prop="jssm" align="center"/>
            <el-table-column label="状态" align="center">
              <template #default="scope">
                {{ scope.row.delFlag === '0' ? '启用' : '停用' }}
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="0.1">
          <el-divider style="height: 100%" direction="vertical"/>
        </el-col>
        <el-col :span="5">
          <el-scrollbar height="80vh">
            <el-tree
                ref="tree"
                :data="menuDataS"
                show-checkbox
                default-expand-all
                node-key="id"
                check-strictly
                :default-checked-keys="defaultCheckedKeys"
                @check-change="setCheckedNodes"
            />
          </el-scrollbar>
        </el-col>
      </el-row>
    </el-card>
  </div>
  <div class="dialog">
    <el-dialog draggable width="400" :title="dialogTitle"
               v-model="typeDialog"

               :before-close="()=>{}">
      <el-card>
        <el-form v-model="rolePojo" inline>
          <el-form-item label="角色名称">
            <el-input v-model="rolePojo.jssm"></el-input>
          </el-form-item>
        </el-form>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button @click="buttonClick" size="small" type="primary">
              操作
            </el-button>
            <el-button @click="dialogClose" size="small">
              <el-icon>
                <Close/>
              </el-icon>
              取消
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>

<style scoped>
.menuMaintenance {
  height: 85vh;

  .elSelect {
    width: 200px;
  }

  .elCard1 {
    height: 5vh;
    line-height: 5vh;
    margin-bottom: 5px;

    .header {
      display: flex;

      .butHeader {
        margin-left: 10px;
      }
    }
  }

  .elCard2 {
    height: 80vh;

    .table {
      margin-left: 5px;
      height: 80vh;
    }
  }
}

</style>