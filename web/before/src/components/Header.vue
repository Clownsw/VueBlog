<template>
  <div class="m-content">
    <h2>{{ welcome }}</h2>
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
            <blog-search ref='searchDialog'></blog-search>
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>

import BlogSearch from '@/components/BlogSearch'

export default {
  name: "Header",
  props: {
    welcome: '',
  },
  components: {
    BlogSearch
  },
  data() {
    return {
      sorts: this.$store.getters.getSortList
    }
  },
  methods: {
    searchDialog() {
      this.$refs.searchDialog.searchDialogIsShow = true
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
