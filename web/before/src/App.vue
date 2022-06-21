<template>
  <div>
    <el-row>
      <el-col :xl="{ span: 13, offset: 6 }" :lg="{ span: 15, offset: 5 }">
        <router-view :key="$route.fullPath" />
        <div class="page-footer" v-html="this.$store.getters.getPageFooter">
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
}
</script>

<style>
</style>
