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
          width="50"
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
          prop="status"
          label="状态"
          width="100"
          show-overflow-tooltip
          align="center">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 1" type="success">正常</el-tag>
          <el-tag size="small" v-else-if="scope.row.status === -1" type="danger">禁用</el-tag>
        </template>
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
              title="您确定要删除该用户吗？"
              style="margin-left: 5px"
              @confirm="deleteBlog(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "Word",
  data() {
    return {
      searchName: '',
      deleteStatus: true,
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
    getBlogs() {
      this.$axios.get("blogs")
          .then(resp => {
            this.tableData = resp.data.data.datas

            for (let i = 0; i < this.tableData.length; i++) {
              this.tableData[i].created = new Date(this.tableData[i].created).toLocaleString()
            }
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
              this.getBlogs()
            })
      } else {
        this.$message.error('请先选择要删除的数据!')
      }
    }
  },
  created() {
    this.getBlogs()
  }
}
</script>

<style scoped>

</style>
