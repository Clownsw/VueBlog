<template>
  <div class="m-content">
    <h3>欢迎来到Smilex博客</h3>
    <div class="block">
      <el-avatar :size="50" :src="user.avatar"/>
      <div>{{ user.username }}</div>
    </div>

    <div class="m-action">
      <span>
        <el-link href="/blogs">文章</el-link>
      </span>
      <el-divider direction="vertical"/>
      <span>
        <el-link type="success" href="/blog/add">发表</el-link>
      </span>
      <el-divider direction="vertical" border-style="dashed"/>
      <span v-if="user.hasLogin">
        <el-link type="danger" @click="logout">退出</el-link>
      </span>
      <span v-else>
        <el-link type="primary" href="/login">登录</el-link>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      user: {
        hasLogin: false,
        username: '请先登录',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
      }
    }
  },
  methods: {
    logout() {
      this.$store.commit('REMOVE_INFO')
      this.user.hasLogin = false
      this.user.username = '请先登录!'
      this.user.avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
      this.$router.push('/')
    }
  },
  created() {
    if (this.$store.getters.getUserInfo !== null && this.$store.getters.getUserInfo.username !== undefined) {
      this.user.hasLogin = true
      this.user.username = this.$store.getters.getUserInfo.username
      this.user.avatar = this.$store.getters.getUserInfo.avatar
    }
  }
}
</script>

<style scoped>
.m-content {
  margin: 0 auto;
  max-width: 960px;
  text-align: center;
}

.m-action {
  margin: 10px 0;
}
</style>