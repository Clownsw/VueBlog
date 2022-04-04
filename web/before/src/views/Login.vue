<template>
  <div>
    <el-container>
      <el-header>
        <img class="m_logo" src="@/assets/logo.png" alt="">
      </el-header>
      <el-main>
        <el-form :model="form" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="form.username"/>
          </el-form-item>

          <el-form-item label="密码">
            <el-input type="password" v-model="form.password"/>
          </el-form-item>

          <el-form-item label="验证码">
            <el-input type="text" v-model="form.captcha_code" class="captchaCode"/>
            <el-image style="width: 100px; height: 32px; margin: auto; cursor: pointer" :src="captcha.url" @click="getCaptchaCode"/>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="login">登录</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>

import {ElMessage} from "element-plus";

export default {
  name: "Login",
  data() {
    return {
      form: {
        username: 'xiaoxiao',
        password: '123123',
        captcha_id: '',
        captcha_code: '',
      },
      captcha: {
        url: ''
      }
    }
  },
  methods: {
    login() {

      if (this.form.username === '' || this.form.password === '' || this.form.captcha_id === '' || this.form.captcha_code === '') {
        ElMessage.error('请输入必填参数')
      } else {
        this.$axios.post("login", JSON.stringify(this.form))
            .then(resp => {
              const tokenUser = JSON.parse(resp.data.data)
              let user = {
                id: tokenUser.id,
                username: tokenUser.username,
                email: tokenUser.email,
                avatar: tokenUser.avatar,
                status: tokenUser.status
              }
              this.$store.commit("SET_TOKEN", tokenUser.token)
              this.$store.commit("SET_USER_INFO", user)

              this.$router.push("/blogs")
            })
      }
    },
    getCaptchaCode() {
      this.$axios.get('api/captcha')
          .then(resp => {
            if (resp.data.code === 200) {
              this.form.captcha_id = resp.data.data.id
              this.captcha.url = resp.data.data.base64_url
            }
          })
    }
  },
  created() {
    document.title = '登录 - Smilex Blogs'
    this.getCaptchaCode()
  }
}
</script>

<style scoped>
.m_logo {
  width: 120px;
  height: 120px;
  margin-top: 10px;
}

.el-form {
  padding-right: 90px;
}

.el-header, .el-main {
  margin: 0 auto;
  height: auto;
}

.el-input {
  width: 220px;
}

.el-button {
  margin: 0 auto;
}

.captchaCode {
  width: 100px;
}
</style>