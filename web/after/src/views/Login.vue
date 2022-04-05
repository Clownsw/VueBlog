<template>
  <el-row class="row-bg" justify="center">
    <el-col :span="7">
      <div class="grid-content bg-purple">
        <h2>欢迎来到VueAdmin管理系统</h2>
        <el-image src="https://avatars.githubusercontent.com/u/28394742?v=4" style="width: 200px"></el-image>
        <p>博客 blog.areyou.ml</p>
      </div>
    </el-col>

    <el-col :span="1">
      <el-divider direction="vertical" style="height: 200px"/>
    </el-col>

    <el-col :span="6">
      <el-form
          ref="ruleFormRef"
          :model="loginForm"
          label-width="120px"
          class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="name" style="line-height: 40px">
          <el-input v-model="loginForm.name"/>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"/>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <el-input v-model="loginForm.code"/>
          <el-image src=""></el-image>
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
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        name: '',
        password: '',
        code: '',
      },
    }
  },
  methods: {
    submitForm(formName) {
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
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
  min-width: 1230px;
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
</style>
