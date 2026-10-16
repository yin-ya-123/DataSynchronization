<script>
import {Close} from "@element-plus/icons-vue";
import loginApi from "../../api/user/LoginApi.js";
import systemApplicationParams from "../../sys/SystemApplicationParams.js";
import {ElMessage} from "element-plus";
import {getNowTime} from '../../utils/Utils.js'

export default {
  name: "Login",
  components: {Close},
  data() {
    return {
      loginForm: {
        username: '',
        userId: '',
        password: '',
        parentId: ''
      },
      rules: [],
      //窗体属性
      dialogTitle: '',
      itemDialog: false,
      //模块菜单属性
      moduleDatas: []
    }
  },
  methods: {
    login() {
      loginApi.login(this.loginForm).then(res => {
        //登录成功，获取模块菜单
        let data = res.data
        this.loginForm.userId = data.id
        //保存用户信息数据
        localStorage.setItem(systemApplicationParams.params.user, JSON.stringify(data))
        loginApi.getLoginModuleMenu(this.loginForm).then(res => {
          //赋值给表格
          this.moduleDatas = res.data
          this.dialogOpen()
        })
      })
    },
    //窗体打开操作 1 类型新增 2 类型修改
    dialogOpen(type) {
      this.itemDialog = true
    },
    dialogClose() {
      this.itemDialog = false;
      this.moduleDatas = []
    },
    //跳转具体模块菜单下
    loginIn(data) {
      console.log(data)
      this.loginForm.parentId = data.id
      loginApi.getLoginMenus(this.loginForm).then(res => {
        //保存菜单数据信息
        localStorage.setItem(systemApplicationParams.params.menu, JSON.stringify(res.data))
        // console.log(localStorage.getItem(systemApplicationParams.params.menu))
        //跳转首页
        ElMessage.success("本次登录成功"+getNowTime())
        this.$router.push({path:"/"})
      })

    }
  }
}
</script>

<template>
  <div class="loginModuleDialog">
    <el-dialog draggable width="700" :title="dialogTitle" v-model="itemDialog"
               :before-close="()=>{}">
      <el-card>
        <el-table :data="moduleDatas" height="500" border
                  :highlight-current-row="true"
                  default-expand-all
                  row-key="moduleId">
          <el-table-column prop="moudleMc" label="模块名称" align="center"/>
          <el-table-column prop="label" label="功能菜单" align="center"/>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button v-if="scope.row.label!=null"
                         type="primary"
                         @click="loginIn(scope.row)">进入
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="display: flex;justify-content: center">
          <div style="display: flex;justify-content: center;margin-top: 20px">
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
  <div class="parentLogin">
    <svg t="1728898002608" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
         p-id="4462">
      <path d="M0 0h1024v512H0z" fill="#6699FF" p-id="4463"></path>
      <path d="M0 512h1024v512H0z" fill="#6699ff" p-id="4464"></path>
    </svg>
    <div class="login">
      <div class="title_l">
        <img class="hexagon" src="../../assets/vue.svg">
        <h1>登录界面</h1>
      </div>
      <el-form ref="loginRulesForm" :model="loginForm" label-width="auto" style="max-width: 600px" :rules="rules">
        <el-form-item class="input_l" label="" prop="username">
          <el-input placeholder="请输入账户" v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item class="input_l" label="" prop="password">
          <el-input placeholder="请输入密码" type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="login-l">
            <el-button class="login-l_b" @click="login" type="primary">登录</el-button>
            <!--            <el-button style="width: 200px" @click="formClear" type="primary">重置</el-button>-->
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.parentLogin {
  position: relative; /* 使 video 成为相对定位的子元素 */
  width: 100%;
  height: 100%;
  overflow: hidden; /* 隐藏超出部分 */
}

.login {
  width: 450px;
  height: 450px;
  text-align: center; /*div内容居中*/
  /*margin:300px auto;*/

  /*动态div居中*/
  position: absolute;
  left: 50%;
  top: 40%;
  margin-top: -100px;
  margin-left: -200px;
  background-color: white;
  border-radius: 25px;
}

.login-l {
  /*display: flex;*/
  /*justify-content: center;*/
  width: 100%;
}

.login-l_b {
  width: 350px;
  height: 50px;
  border-radius: 20px;
}

.input_l {
  margin-left: 20px;
  width: 400px;

}

.el-form-item__label {
  font-size: 17px;
  font-weight: bold;
}

.el-input {
  height: 40px;
}

.title_l {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 150px;
}

.hexagon {
  height: 60px;
  width: auto;
}
</style>