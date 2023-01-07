<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item>
        <el-button type="danger" @click="flushSearchData" size="mini">刷新搜索数据</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import searchApi from '../../api/search'

export default {
  name: 'SearchIndex',
  methods: {
    flushSearchData() {
      searchApi.flushSearchData().then(() => {
        this.$message.success('刷新成功')
      }).catch(_ => {
        this.$message.error('刷新失败')
      })
    }
  }
}
</script>

<style scoped>

</style>
