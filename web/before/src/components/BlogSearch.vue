<template>
    <el-dialog
      class="searchDialog"
      :visible.sync="searchDialogIsShow"
      :width="blogSearhWidth"
      @close="searchDialogCloseHandler"
    >
      <div class="searchDialogTitle" slot="title">
        搜索
        <el-divider></el-divider>
      </div>
      <el-input class="searchDialogInput" v-model="searchStr" @change="searchInputChangeHandler"></el-input>
      <el-card
        v-for="item in searchBlog"
        :key="item.id"
        style="margin-bottom: 10px"
      >
        <h3 class="blog-title">
          <router-link
            :to="{ name: 'BlogDetail', params: { blogId: item.id } }"
          >
            {{ item.title }}
          </router-link>
        </h3>

        <p class="blog-description">{{ item.description }}</p>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import searchApi from "@/api/search";

export default {
  name: "BlogSearch",
  data() {
    return {
      searchDialogIsShow: false,
      searchStr: "",
      searchBlog: [],
      blogSearhWidth: "45%",
    };
  },
  methods: {
    searchDialogCloseHandler() {
      this.searchStr = ''
      this.searchBlog = []
    },
    searchInputChangeHandler(newValue) {
      this.searchStr = newValue;
      if (newValue === "") {
        this.searchBlog = []
      } else {
        searchApi.searchBlog(newValue).then((resp) => {
          this.searchBlog = resp.data.data
        })
      }
    },
    setBlogSearchDialogWidth() {
      let windowSize = document.body.clientWidth;
      if (windowSize <= 700) {
        this.blogSearhWidth = "95%";
      } else if (windowSize <= 900) {
        this.blogSearhWidth = "85%";
      } else if (windowSize <= 1300) {
        this.blogSearhWidth = "75%";
      } else {
        this.blogSearhWidth = "45%";
      }
    },
  },
  mounted() {
    window.onresize = () => {
      return (() => {
        this.setBlogSearchDialogWidth();
      })();
    };
  },
};
</script>

<style scoped>
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

.searchDialog .searchDialogTitle {
  text-align: left;
}

.searchDialog .searchDialogInput {
  margin-bottom: 10px;
}

.searchDialog ::v-deep .el-dialog__header {
  padding: 20px 20px 0 !important;
}

.searchDialog ::v-deep .el-divider--horizontal {
  margin: 18px 0 !important;
}

.searchDialog ::v-deep .el-dialog__body {
  padding-top: 0 !important;
  padding-bottom: 20px !important;
  text-align: left !important;
}
</style>