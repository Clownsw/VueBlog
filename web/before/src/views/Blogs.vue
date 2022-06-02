<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <el-card v-for="item in blogs.data" style="margin-bottom: 10px">
      <h3 class="blog-title">
        <router-link :to="{ name: 'BlogDetail', params: { blogId: item.id } }">
          {{ item.title }}
        </router-link>
      </h3>
      <p class="blog-description">{{ item.description }}</p>

      <el-tag v-for="tag in item.tags" style="margin: 3px 10px 3px 0">
        {{ tag.name }}
      </el-tag>

      <div>
        <p class="blog-footer" style="display: inline-block">{{ parseStrToDate(item.created) }}</p>

        <router-link :to="{ name: 'BlogsId', params: { id: item.sort.id } }"
                     class="blog-footer" style="float: right">
          {{ item.sort.name }}
        </router-link>
      </div>
    </el-card>

    <div class="limit">
      <el-pagination background layout="prev, pager, next"
                     :total="blogs.total"
                     :current-page="blogs.currentPage"
                     :page-size="blogs.size"
                     @current-change=page
                     class="m-page"/>
    </div>
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
      sortId: null,
    }
  },
  methods: {
    page(currentPage) {
      let url = this.sortId !== null
          ? "/blogs/sort/list?currentPage=" + currentPage + '&sortId=' + this.sortId
          : "/blogs?currentPage=" + currentPage
      this.$axios.get(url)
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
    this.systemInfo = this.$store.getters.getSystemInfo
    document.title = this.systemInfo.title

    this.sortId = this.$route.params.id === undefined ? null : this.$route.params.id

    this.page(1)
  }
}
</script>

<style scoped>
.m-page {
  margin: 0 auto;
  text-align: center;
  max-width: 40%;
}

.limit {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

a {
  text-decoration: none;
}

.blog-title ::v-deep a {
  color: #303133;
}

.blog-description {
  color: #a0a3a8;
}

.blog-footer {
  font-size: 13px;
  color: #a0a3a8;
  margin-block-start: 1em;
  margin-block-end: 0;
}
</style>
