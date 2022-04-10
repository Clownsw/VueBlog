<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <el-timeline>
      <el-timeline-item v-for="item in blogs.data" :timestamp="parseStrToDate(item.created)" placement="top">
        <el-card>
          <h4>
            <router-link :to="{ name: 'BlogDetail', params: { blogId: item.id } }">
              {{ item.title }}
            </router-link>
          </h4>
          <p>{{ item.description }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <el-pagination background layout="prev, pager, next"
                   :total="blogs.total"
                   :current-page="blogs.currentPage"
                   :page-size="blogs.size"
                   @current-change=page
                   class="m-page"/>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "Blogs",
  components: {
    Header
  },
  data() {
    return {
      blogs: {
        currentPage: 1,
        total: 0,
        pages: 1,
        size: 0,
        data: []
      },
      systemInfo: {},
    }
  },
  methods: {
    page(currentPage) {
      this.$axios.get("blogs?currentPage=" + currentPage)
          .then(resp => {
            this.blogs.data = resp.data.data.datas
            this.blogs.currentPage = resp.data.data.currentPage
            this.blogs.pages = resp.data.data.pages
            this.blogs.total = resp.data.data.total
            this.blogs.size = resp.data.data.size
          })
    },
    parseStrToDate(str) {
      return new Date(str).toLocaleString()
    },
  },
  created() {
    document.title = 'Smilex Blog'
    this.page(1)
    this.systemInfo = this.$store.getters.getSystemInfo
    document.title = this.systemInfo.title
  }
}
</script>

<style scoped>
.m-page {
  margin: 0 auto;
  text-align: center;
  max-width: 40%;
}
</style>
