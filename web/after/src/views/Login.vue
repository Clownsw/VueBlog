<template>
  <el-row class="row-bg" justify="center">
    <el-col :xl="6" :lg="7">
      <div class="grid-content bg-purple">
        <h2>欢迎来到VueAdmin管理系统</h2>
        <el-image src="https://avatars.githubusercontent.com/u/28394742?v=4" style="width: 200px"></el-image>
        <p>博客 blog.areyou.ml</p>
      </div>
    </el-col>

    <el-col :span="1">
      <el-divider direction="vertical" style="height: 200px"/>
    </el-col>

    <el-col :xl="6" :lg="7">
      <el-form
          ref="ruleFormRef"
          :model="loginForm"
          label-width="120px"
          class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="name" style="line-height: 40px">
          <el-input v-model="loginForm.username"/>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"/>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <el-input v-model="loginForm.captcha_code" style="width: 190px;"/>
          <el-image :src="base64_url" class="captcha-code" @click="getCaptcha"></el-image>
        </el-form-item>

        <el-form-item class="login-button">
          <el-button type="primary" @click="submitForm"
          >登录
          </el-button
          >

          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
import {ElMessage} from 'element-plus'

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        captcha_id: '',
        username: 'xiaoxiao',
        password: '123123',
        captcha_code: '',
      },
      base64_url: ''
    }
  },
  methods: {
    getCaptcha() {
      this.$axios.get("http://localhost:9999/api/captcha")
          .then(resp => {
            this.loginForm.captcha_id = resp.data.data.id
            this.base64_url = resp.data.data.base64_url
          })
    },
    submitForm() {
      if (this.loginForm.username !== '' && this.loginForm.password !== '' && this.loginForm.captcha_code !== '') {
        this.$axios.post("http://localhost:9999/admin/login", this.loginForm)
            .then(resp => {
              console.log(resp.data)
              this.getCaptcha()
            })
      } else {
        ElMessage.error('请输入必填参数!')
      }
    },
    resetForm() {
      this.loginForm.captcha_id = ''
      this.loginForm.username = ''
      this.loginForm.password = ''
      this.loginForm.captcha_code = ''
    }
  },
  created() {
    this.getCaptcha()
  }
}
</script>

<style scoped>
body {
  overflow: hidden;
}

.el-row {
  background-color: #fafafa;
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-col {
  text-align: center;
}

.el-input {
  width: 300px;
  height: 40px;
}

.el-input ::v-deep(.el-input__inner) {
  height: 40px;
}

.login-button ::v-deep(.el-form-item__content) {
  justify-content: center;
}

.el-form-item ::v-deep(.el-form-item__label) {
  line-height: 40px;
}

.captcha-code {
  flex: 1;
  margin-left: 6px;
  height: 40px;
  cursor: pointer;
}

.captcha-code ::v-deep(img) {
  width: 98px;
  margin-right: 60px;
}
</style>
