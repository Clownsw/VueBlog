<template>
  <el-container>
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>VueAdmin后台管理系统</strong>
        <div class="header-avatar">
          <el-avatar :src="user.avatar"></el-avatar>

          <el-dropdown :hide-on-click="false">
            <span class="el-dropdown-link">
              {{ user.username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>
                <el-link @click="logout">退出</el-link>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

          <el-link href="https://blog.areyou.ml/" target="_blank">网站</el-link>
          <el-link href="https://blog.areyou.ml/" target="_blank">B站</el-link>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideMenu from "@/components/SideMenu";

export default {
  name: "Home",
  data() {
    return {
      user: {}
    }
  },
  components: {
    SideMenu
  },
  methods: {
    logout() {
      this.$store.commit("SET_TOKEN", "")
      this.$router.push("/login")
    }
  },
  created() {
    this.user = this.$store.getters.getUser
  }
}
</script>

<style scoped>
.el-container {
  margin: 0;
  padding: 0;
  height: 100vh;
}

.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

.el-dropdown-link {
  cursor: pointer;
}

.header-avatar {
  display: flex;
  float: right;
  width: 210px;
  justify-content: space-around;
  align-items: center;
}
</style>

