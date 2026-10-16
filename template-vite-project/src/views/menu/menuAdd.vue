<script>
import menuApi from "../../api/jcxx/MenuApi.js";
import sysModuleApi from "../../api/jcxx/SysModuleApi.js";
import {ElMessage, ElMessageBox} from "element-plus";
//菜单新增
export default {
  name: "menuAdd",
  data() {
    return {
      //菜单表格属性
      menuDataS: [],
      menuData: {},
      menuParentMc: '',//上级菜单名称
      //模块信息
      moduleDataS: [],
      selValue: [],//模块下拉框
      // 强制刷新table
      isUpdate: false,
      //展开/折叠
      defaultExpandAll: false,
      //菜单窗体
      menuTypeDialog: false,
      menuDialogTitle: '',
      //模块窗体
      moduleTypeDialog: false,
      moduleDialogTitle: '',
      //操作类型属性
      moduleType: 0,
      menuType: 1,
      buttonClickType: 0, //1: 保存新增 2: 修改 3: 删除
      menuLoading: false,//加载框
    }
  },
  created() {
    this.getMenuPojoDataS()
  },
  methods: {
    //获取菜单结构信息
    getMenuPojoDataS() {
      this.menuLoading = true
      menuApi.getMenuPojoDataS({}).then(res => {
        this.menuDataS = res.data
        this.menuLoading = false
      })
    },
    //获取模块信息
    getSysModuleList() {
      sysModuleApi.getSysModuleList({}).then(res => {
        this.moduleDataS = res.data
      })
    },
    //展开/折叠
    unfoldOrFold() {
      this.isUpdate = !this.isUpdate
      this.defaultExpandAll = !this.defaultExpandAll
    },
    /*
    * 常规按钮点击事件
    * czType:0 模块 1 菜单
    * type:1: 保存新增 2: 修改 3: 删除
    * data: 传递的数据
    * */
    buttonClick(czType, type, data) {
      if (czType === 0) {

      } else if (czType === 1) {
        if (type === 1) {
          menuApi.addMenu(this.menuData).then(res => {
            ElMessage.success(res.message)
            this.dialogClose(czType)
          })
        } else if (type === 2) {
          menuApi.updateMenu(this.menuData).then(res => {
            ElMessage.success(res.message)
            this.dialogClose(czType)
          })
        } else if (type === 3) {
          ElMessageBox.confirm('确定操作吗',
              {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
              })
              .then(() => {
                menuApi.deleteMenu(data).then(res => {
                  ElMessage.success(res.message)
                  //刷新数据
                  this.getMenuPojoDataS()
                })
              })
              .catch(() => {
                // catch error
              })
        }
      }
    },
    /*
    * 窗体打开操作
    * dialogType:0 模块操作 1 菜单操作 2 一级菜单(特殊)
    * type: 1 类型新增 2 类型修改
    * data: 传递的数据
    * */
    dialogOpen(dialogType, type, data) {
      this.buttonClickType = type
      if (dialogType === 0) {
        this.moduleTypeDialog = true
      } else if (dialogType === 1) {
        this.menuTypeDialog = true
        if (this.moduleDataS.length === 0) {
          this.getSysModuleList()
        }
        if (type === 1) {
          this.menuDialogTitle = '菜单新增'
          this.menuData.moduleId = data.moduleId
          this.menuData.parentId = data.id
          this.menuParentMc = data.label
          this.menuData.menuLv = Number(data.menuLv) + 1
        } else if (type === 2) {
          this.menuDialogTitle = '菜单修改'
          this.menuData = data
        }
      } else if (dialogType === 2) {
        this.menuTypeDialog = true
        this.menuDialogTitle = '模块菜单新增'
        this.menuData.menuLv = 1
        if (this.moduleDataS.length === 0) {
          this.getSysModuleList()
        }
      }
    },
    //窗体关闭操作 dialogType:0 模块操作 1 菜单操作
    dialogClose(dialogType) {
      if (dialogType === 0) {
        this.moduleTypeDialog = false
      } else if (dialogType === 1) {
        this.menuTypeDialog = false
        this.menuData = {}
        this.menuParentMc = ''
        //刷新菜单数据
        this.getMenuPojoDataS()
      }

    },
  }
}
</script>

<template>
  <div class="menuAdd">
    <div class="menuAdd-header">
      &nbsp;&nbsp;
      <el-button @click="dialogOpen(this.moduleType,1)" type="primary" size="small">新增模块</el-button>
      <el-button @click="dialogOpen(2,1)" type="primary"
                 size="small">新增模块菜单
      </el-button>
      <el-button type="primary" size="small" @click="unfoldOrFold">展开/折叠</el-button>
      <el-button type="primary" size="small" @click="getMenuPojoDataS">刷新</el-button>
    </div>
    <div class="menuAdd-data">
      <el-table v-loading="menuLoading" size="small" class="table" :data="menuDataS"
                row-key="id"
                :key="isUpdate"
                :default-expand-all="defaultExpandAll"
      >
        <el-table-column width="200" align="center" label="菜单id" prop="id"/>
        <el-table-column width="200" align="center" label="菜单名称" prop="label"/>
        <el-table-column width="200" align="center" label="地址" prop="path"/>
        <el-table-column width="100" align="center" label="排序" prop="sort"/>
        <el-table-column width="100" align="center" label="模块id" prop="moduleId"/>
        <el-table-column width="100" align="center" label="菜单等级" prop="menuLv"/>
        <el-table-column width="200" label="操作">
          <template #default="scope">
            <el-button @click="dialogOpen(this.menuType,2,scope.row)"
                       type="primary" size="small">修改
            </el-button>
            <el-button @click="buttonClick(this.menuType,3,scope.row)" type="danger" size="small">删除</el-button>
            <el-button v-if="scope.row.menuLv!=='3'"
                       @click="dialogOpen(this.menuType,1,scope.row)" type="primary" size="small">
              新增
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  <div class="dialog">
    <!--    模块新增-->
    <el-dialog draggable width="400"
               :title="moduleDialogTitle"
               v-model="moduleTypeDialog"
               :before-close="()=>{}">
      <el-card>
        //正文
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button @click="dialogClose(0)" size="small">
              取消
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
    <!--    菜单操作-->
    <el-dialog draggable width="700"
               :title="menuDialogTitle"
               v-model="menuTypeDialog"
               :before-close="()=>{}">
      <el-card>
        <el-form
            v-model="menuData"
            size="small"
            label-width="65px"
        >
          <el-row>
            <el-col>
              <el-form-item label="模块信息"  v-if="menuData.menuLv===1">
                <el-select
                    v-model="menuData.moduleId"
                    placeholder="模块信息"
                    value-key="moduleId"
                    style="width:200px"
                >
                  <el-option v-for="item in moduleDataS"
                             :key="item.moduleId"
                             :label="item.moudleMc"
                             :value="item.moduleId"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="上级目录"  v-if="menuData.menuLv!==1">
                <el-text type="primary" size="small">{{ menuParentMc }}</el-text>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="菜单名称">
                <el-input v-model="menuData.label" class="menuInput"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="菜单地址">
                <el-input v-model="menuData.path" class="menuInput"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序">
                <el-input v-model="menuData.sort" class="menuInput"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
            <el-button @click="buttonClick(menuType,buttonClickType)" size="small">
              保存
            </el-button>
            <el-button @click="dialogClose(1)" size="small">
              取消
            </el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>

<style>
.menuAdd {
  height: 85vh;

  .menuAdd-header {
    height: 5vh;
    line-height: 5vh;
  }

  .menuAdd-data {
    height: 80vh;
  }
}

.menuInput {
  width: 200px;
}

/*设置el-form的label两端对齐，需要el-form设置属性label-width*/
.el-form-item__label {
  display: inline;
  text-align-last: justify;
}
</style>
