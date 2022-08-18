<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>

    <p v-if="blogs.data.length === 0" style="text-align: center; font-style: italic">暂无文章</p>
    <el-card v-else v-for="item in blogs.data" :key="item.id" style="margin-bottom: 10px">
      <h3 class="blog-title">
        <router-link :to="{ name: 'BlogDetail', params: { blogId: item.id } }">
          {{ item.title }}
        </router-link>
      </h3>
      
      <p class="blog-description">{{ item.description }}</p>

      <router-link :to="{ name: 'BlogsTag', params: { tagId: tag.id } }" v-for="tag in item.tags" :key="tag.id">
        <el-tag style="margin: 3px 10px 3px 0">
          {{ tag.name }}
        </el-tag>
      </router-link>

      <div>
        <p class="blog-footer" style="display: inline-block">{{ parseStrToDate(item.created) }}</p>

        <router-link :to="{ name: 'BlogsId', params: { id: item.sort.id } }"
                     class="blog-footer" style="float: right">
          {{ item.sort.name }}
        </router-link>
      </div>
    </el-card>


    <el-dialog
      class="searchDialogClass"
      title="文章搜索"
      :visible.sync="searchDialogVisible"
      width="30%"
      :before-close="handleSearchDialogClose">
      <el-input v-model="searchQueryStr" @keyup.native.enter="handleSearchInputEnter"></el-input>
    </el-dialog>

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
      tagId: null,
      searchQueryStr: '',
      searchDialogVisible: false,
    }
  },
  methods: {
    page(currentPage, queryStr) {
      let url = this.sortId !== null
          ? "/blogs/sort/list?currentPage=" + currentPage + '&sortId=' + this.sortId
          : this.tagId !== null
              ? "/blogs/tag/list?currentPage=" + currentPage + '&tagId=' + this.tagId
              : queryStr != null
              ? "/blogs?currentPage=" + currentPage + '&queryStr=' + queryStr
              : "/blogs?currentPage=" + currentPage
      this.$axios.get(url)
          .then(resp => {
            if (resp.data.data !== null) {
              this.blogs.data = resp.data.data.datas
              this.blogs.currentPage = resp.data.data.current
              this.blogs.pages = resp.data.data.pages
              this.blogs.total = resp.data.data.total
              this.blogs.size = resp.data.data.size
            }

            console.log(this.blogs.data.length);
          })
    },
    parseStrToDate(str) {
      return new Date(str).toLocaleString()
    },
    handleSearchDialogClose() {
      this.searchQueryStr = ''
      this.searchDialogVisible = false
    },
    handleSearchInputEnter() {
      if (this.searchQueryStr != '') {
        this.page(1, this.searchQueryStr)
        this.searchQueryStr = ''
      } else {
        this.page(1, null)
      }
      this.searchDialogVisible = false
    }
  },
  created() {
    this.systemInfo = this.$store.getters.getSystemInfo
    document.title = this.systemInfo.title

    this.sortId = this.$route.params.id === undefined ? null : this.$route.params.id
    this.tagId = this.$route.params.tagId === undefined ? null : this.$route.params.tagId

    this.page(1, null)
  }
}
</script>

<style scoped>
.m-page {
  margin: 0 auto;
  text-align: center;
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

.searchDialogClass ::v-deep .el-dialog__body {
  padding: 10px 10px !important;
}
</style>
