<template>
  <div class="m-content">
    <h3>{{ welcome }}</h3>
    <div class="m-action"
         style="display: flex; justify-content: center;">

      <ul style="list-style: none; display: flex; margin: 0 auto; width: auto; padding-inline-start: 0;">
        <li>
          <span>
            <el-link href="/blogs" :underline="false">所有</el-link>
          </span>
          <el-divider direction="vertical"/>
        </li>

        <li v-for="sort in sorts">
          <span>
            <el-link :href="'/blogs/sort/' + sort.id" :underline="false">{{ sort.name }}</el-link>
          </span>
          <el-divider direction="vertical"/>
        </li>

        <li>
          <span>
            <el-link href="/friend" :underline="false">友联</el-link>
          </span>
        </li>

        <li>
          <el-divider direction="vertical" border-style="dashed"/>
          <span>
            <el-link href="/me" :underline="false">我</el-link>
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
      sorts: []
    }
  },
  methods: {
    getAllSort() {
      this.$axios.get('/sort/list').then(resp => {
        this.sorts = resp.data.data
      })
    }
  },
  created() {
    this.getAllSort()
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
