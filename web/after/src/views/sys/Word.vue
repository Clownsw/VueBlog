<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
            v-model="searchName"
            placeholder="名称"
            clearable
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="">新增</el-button>
      </el-form-item>

      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗?" @confirm="">
          <el-button type="danger" slot="reference" :disabled="deleteStatus">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Word",
  data() {
    return {
      searchName: '',
      deleteStatus: true
    }
  }
}
</script>

<style scoped>

</style>
