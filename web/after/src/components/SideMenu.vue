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

        <router-link :to="{ name: 'SysRole' }">
          <el-menu-item index="3-2">
            <i class="el-icon-rank"></i>
            <span slot="title">角色管理</span>
          </el-menu-item>
        </router-link>

        <router-link :to="{ name: 'SysMenu' }">
          <el-menu-item index="3-3">
            <i class="el-icon-setting"></i>
            <span slot="title">菜单管理</span>
          </el-menu-item>
        </router-link>
      </el-submenu>

      <el-submenu index="4">
        <template slot="title">
          <i class="el-icon-setting"></i>
          <span>系统工具</span>
        </template>

        <el-menu-item index="4-1">
          <i class="el-icon-s-cooperation"></i>
          <span slot="title">数字字典</span>
        </el-menu-item>
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
