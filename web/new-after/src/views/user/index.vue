<template>
  <div class="app-container">
    <el-form :model="user" ref="user" label-width="100px">
      <el-form-item label="用户名" prop="username">
        <el-input type="text" v-model="user.username"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input type="text" v-model="user.password"></el-input>
      </el-form-item>

      <el-form-item label="头像" prop="avatar">
        <el-input type="text" v-model="user.avatar"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input type="text" v-model="user.email"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('user')">修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { default as userApi } from '../../api/user';

export default {
  name: 'UserIndex',
  data() {
    return {
      user: {}
    }
  },
  created() {
    this.fetchGetUserInfo()
  },
  methods: {
    fetchGetUserInfo() {
      userApi.getUserInfo().then(resp => {
        this.user = resp.data
      })
    },
    fetchUpdateUserById() {
      let obj = {
        id: this.user.id,
        username: this.user.username,
        password: this.user.password,
        avatar: this.user.avatar,
        email: this.user.email,
        status: this.user.status
      }
      return userApi.updateUserById(obj)
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.fetchUpdateUserById().then(resp => {
            this.$message.success(resp.message)
            setTimeout(() => {
              this.logout()
            }, 1500)
          })
            .catch(_ => {
              this.$message.error("更新用户失败!")
            })
        } else {
          return false;
        }
      });
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  },
}
</script>

<style scoped>
</style>
