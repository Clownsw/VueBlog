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
        <el-button type="primary" @click="addBlog">新增</el-button>
      </el-form-item>

      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗?" @confirm="deleteBlogs(null)">
          <el-button type="danger" slot="reference" :disabled="deleteStatus">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe
        @selection-change="handleSelectionChange"
        class="data-table">
      <el-table-column
          type="selection"
          align="center">
      </el-table-column>

      <el-table-column
          prop="id"
          label="ID"
          width="100"
          align="center">
      </el-table-column>

      <el-table-column
          prop="user_id"
          label="发布者"
          width="100"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="title"
          label="标题"
          width="350"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="description"
          label="描述"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="created"
          label="创建时间"
          width="230"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="action"
          label="操作"
          width="200"
          show-overflow-tooltip
          align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="editWord(scope.row.id)">编辑</el-button>

          <el-popconfirm
              confirm-button-text='删除'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="您确定要删除该文章吗？"
              style="margin-left: 5px"
              @confirm="deleteBlog(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="limit">
      <el-pagination
          background
          layout="prev, pager, next"
          :page-size="size"
          :currnet-page="current"
          :total="total"
          @current-change=getBlogs
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Word",
  data() {
    return {
      searchName: '',
      deleteStatus: true,
      total: 0,
      size: 0,
      current: 1,
      pages: 0,
      tableData: [],
      multipleSelection: [],
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.deleteStatus = this.multipleSelection.length <= 0;
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
    },
    addBlog() {
      this.editWord(0)
    },
    getBlogs(currentPage) {
      this.$axios.get("blogs?currentPage=" + currentPage)
          .then(resp => {
            this.total = resp.data.data.total
            this.size = resp.data.data.size
            this.pages = resp.data.data.pages
            this.current = resp.data.data.current
            this.tableData = resp.data.data.datas

            for (let i = 0; i < this.tableData.length; i++) {
              this.tableData[i].created = new Date(this.tableData[i].created).toLocaleString()
            }

            this.updateAllBlogTag()
          })
    },
    editWord(id) {
      this.$router.push("/sys/word/edit/" + id)
    },
    deleteBlog(id) {
      let ids = []
      ids.push(id)
      this.deleteBlogs(ids)
    },
    deleteBlogs(_ids) {
      let ids = []
      if (_ids === null) {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id)
        }
      } else {
        ids = _ids
      }

      if (ids.length > 0) {
        this.$axios.post("blog/deletes/", ids, {
          headers: {
            'authorization': this.$store.getters.getToken
          }
        })
            .then(resp => {
              this.$message.success(resp.data.message)
              this.getBlogs(this.current)
            })
      } else {
        this.$message.error('请先选择要删除的数据!')
      }
    },
    updateAllBlogTag() {
      for (let i = 0; i < this.tableData.length; i++) {
        this.getBlogTags(i, this.tableData[i].id)
      }
    },
    getBlogTags(i, id) {
      this.$axios.get("tag/" + id).then(resp => {
        this.tableData[i].tag = resp.data.data
      })
    },
  },
  created() {
    this.getBlogs(1)
  }
}
</script>

<style scoped>
.limit {
  display: flex;
  justify-content: center;
  margin-top: 8px;
}

span {
  margin-right: 5px;
}
</style>
