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
        <el-button type="primary">新增</el-button>
      </el-form-item>

      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗?" @confirm="">
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
          prop="username"
          label="用户名"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="password"
          label="密码"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="avatar"
          label="头像"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="email"
          label="邮箱"
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
          <el-tag size="small" v-else-if="scope.row.status === 0" type="danger">禁用</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          prop="created"
          label="创建时间"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="last_login"
          label="最后登录时间"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="action"
          label="操作"
          show-overflow-tooltip
          align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="show(scope)">编辑</el-button>
          <el-button type="danger" @click="show">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      searchName: '',
      deleteStatus: true,
      tableData: [
        {
          id: 1,
          username: "xiaoxiao",
          avatar: "https://avatars.githubusercontent.com/u/28394742",
          email: "msmliexx1@gmail.com",
          password: "123123",
          status: 1,
          create_time: "2022-03-29 21:23:55",
          last_login_time: "2022-03-29 21:24:00",
        }
      ],
      multipleSelection: []
    }
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getUsers() {
      this.$axios.post("user/all", {}, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
          .then(resp => {
            this.tableData = resp.data.data
          })
    },
    show(d) {
      console.log(d)
    }
  },
  created() {
    this.getUsers()
  }
}
</script>

<style scoped>
</style>
