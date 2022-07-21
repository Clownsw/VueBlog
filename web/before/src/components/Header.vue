<template>
  <div class="m-content">
    <h2>{{ welcome }}</h2>
    <el-button type="primary" size="mini" @click="testBtnClick">测试</el-button>
    <div class="m-action" style="display: flex; justify-content: center;">

      <ul style="list-style: none; display: flex; margin: 0 auto; width: auto; padding-inline-start: 0;">
        <li>
          <span>
            <el-link href="/blogs" :underline="false">所有</el-link>
          </span>
          <el-divider direction="vertical" />
        </li>

        <li v-for="sort in sorts">
          <span>
            <el-link :href="'/blogs/sort/' + sort.id" :underline="false">{{ sort.name }}</el-link>
          </span>
          <el-divider direction="vertical" />
        </li>

        <li>
          <span>
            <el-link href="/friend" :underline="false">友链</el-link>
          </span>
        </li>

        <li>
          <el-divider direction="vertical" border-style="dashed" />
          <span>
            <el-link href="/me" :underline="false">我</el-link>
          </span>
        </li>

        <li>
          <el-divider direction="vertical" border-style="dashed" />
          <span>
            <el-link :underline="false" @click="searchDialog">搜索</el-link>
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    welcome: '',
  },
  data() {
    return {
      sorts: this.$store.getters.getSortList
    }
  },
  methods: {
    searchDialog() {
      this.$parent.searchDialogVisible = true
    },
    testBtnClick() {
      this.$axios.get('/blogs?currentPage=1&queryStr=Element').then(resp => {
        this.$parent.blogs.data = resp.data.data.datas
        this.$parent.blogs.currentPage = resp.data.data.current
        this.$parent.blogs.pages = resp.data.data.pages
        this.$parent.blogs.total = resp.data.data.total
        this.$parent.blogs.size = resp.data.data.size
      })
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
