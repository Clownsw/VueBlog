<template>
  <el-row class="tac">
    <el-menu
        default-active="2"
        class="el-menu-vertical-admin"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
      <router-link :to="{ name: 'Index' }">
        <el-menu-item index="1">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
      </router-link>

      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-s-operation"></i>
          <span>系统管理</span>
        </template>

        <router-link :to="{ name: 'SysUser' }">
          <el-menu-item index="2-1">
            <i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
        </router-link>

        <router-link :to="{ name: 'SysWord' }">
          <el-menu-item index="2-2">
            <i class="el-icon-s-order"></i>
            <span slot="title">文章管理</span>
          </el-menu-item>
        </router-link>

        <router-link :to="{ name: 'SysTag' }">
          <el-menu-item index="2-3">
            <i class="el-icon-coin"></i>
            <span slot="title">标签管理</span>
          </el-menu-item>
        </router-link>

        <router-link :to="{ name: 'SysFriend' }">
          <el-menu-item index="2-4">
            <i class="el-icon-paperclip"></i>
            <span slot="title">友链管理</span>
          </el-menu-item>
        </router-link>

        <router-link :to="{ name: 'SysMe' }">
          <el-menu-item index="2-5">
            <i class="el-icon-thumb"></i>
            <span slot="title">关于我</span>
          </el-menu-item>
        </router-link>
      </el-submenu>
    </el-menu>
  </el-row>
</template>

<script>
export default {
  name: "SideMenu"
}
</script>

<style scoped>
.el-menu-vertical-admin {
  height: 100vh;
  text-align: left;
}

a {
  text-decoration: none;
}
</style>
