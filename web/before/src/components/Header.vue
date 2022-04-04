<template>
  <div class="m-content">
    <h3>欢迎来到Smilex博客</h3>
    <div class="m-action">
      <span>
        <el-link href="/blogs">文章</el-link>
      </span>
      <el-divider direction="vertical"/>
      <span>
        <el-link href="/friend">友联</el-link>
      </span>
      <el-divider direction="vertical" border-style="dashed"/>
      <span>
        <el-link href="/me">我</el-link>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
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