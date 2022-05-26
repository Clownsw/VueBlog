<template>
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :md="6" :lg="7">
      <div class="grid-content bg-purple">
        <h2>欢迎来到VueBlog管理系统</h2>
        <el-image src="https://avatars.githubusercontent.com/u/28394742?v=4" style="width: 200px;"></el-image>
        <p>博客地址: blog.areyou.ml</p>
      </div>
    </el-col>

    <el-col :span="1">
      <div class="grid-content bg-purple-light">
        <el-divider direction="vertical" class="f"></el-divider>
      </div>
    </el-col>

    <el-col :md="6" :lg="7">
      <div class="grid-content bg-purple" style="padding-right: 130px">
        <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username"></el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="loginForm.password"></el-input>
          </el-form-item>

          <el-form-item label="验证码" prop="captcha_code">
            <el-input v-model="loginForm.captcha_code" style="width: 172px; float: left"></el-input>
            <el-image :src="base64_url" class="captchaImg" @click="getCaptcha"></el-image>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
            <el-button @click="resetForm('loginForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<script>

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
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'change'}
        ],
        captcha_code: [
          {required: true, message: '请输入验证码', trigger: 'change'}
        ],
      },
      base64_url: ''
    }
  },
  methods: {
    getCaptcha() {
      this.$axios.get("api/captcha")
          .then(resp => {
            this.loginForm.captcha_id = resp.data.data.id
            this.base64_url = resp.data.data.base64_url
          })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("admin/login", this.loginForm)
              .then(resp => {
                if (resp.status === 200) {
                  const token = resp.data.data.token

                  this.$store.commit('SET_TOKEN', token)

                  this.$message({
                    message: '登录成功',
                    type: 'success'
                  });

                  setTimeout(() => {
                    this.$router.replace('/sys/index')
                  }, 1500)
                }

                this.getCaptcha()
              })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
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

.f {
  height: 200px;
}

.captchaImg {
  cursor: pointer;
  height: 40px;
  border-radius: 8px;
}
</style>
