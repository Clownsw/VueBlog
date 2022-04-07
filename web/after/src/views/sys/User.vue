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
        <el-button type="primary" @click="addUser">新增</el-button>
      </el-form-item>

      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗?" @confirm="deleteUsers">
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
          <el-tag size="small" v-else-if="scope.row.status === -1" type="danger">禁用</el-tag>
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
          <el-button type="info" @click="editUser(scope.row)">编辑</el-button>

          <el-popconfirm
              confirm-button-text='删除'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="您确定要删除该用户吗？"
              style="margin-left: 5px"
              @confirm="deleteUser(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>


    <!--  用户信息编辑窗口  -->
    <el-dialog
        title="编辑"
        :visible.sync="editStatus"
        width="25%"
        center
        class="edit-box">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm">

        <el-form-item label="用户ID" prop="id" v-if="!this.isAddUser">
          <el-input v-model="ruleForm.id" readonly></el-input>
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <br>
          <el-radio-group v-model="ruleForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="-1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="头像" prop="avatar">
          <el-input v-model="ruleForm.avatar"></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="ruleForm.email"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')" style="float: right">{{
              dialogBtnTitle
            }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
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
        {}
      ],
      multipleSelection: [],
      editStatus: false,
      isAddUser: false,
      dialogBtnTitle: '',
      ruleForm: {
        id: 0,
        username: '',
        password: '',
        status: 1,
        avatar: '',
        email: '',
      },
      rules: {
        id: [
          {required: true, message: '请输入用户ID', trigger: 'blur'},
        ],
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        avatar: [
          {required: true, message: '请输入头像地址', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入邮箱', trigger: 'blur'},
        ],
        status: [
          {required: true, message: '请选择状态', trigger: 'blur'},
        ]
      }
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

      this.deleteStatus = this.multipleSelection.length <= 0;
    },
    getUsers() {
      this.$axios.post("user/all", {}, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
          .then(resp => {
            this.tableData = resp.data.data

            for (let i = 0; i < this.tableData.length; i++) {
              this.tableData[i].created = new Date(this.tableData[i].created).toLocaleString()
              this.tableData[i].last_login = new Date(this.tableData[i].last_login).toLocaleString()
            }
          })
    },
    addUser() {
      this.editStatus = true
      this.isAddUser = true
      this.resetForm()
      this.dialogBtnTitle = '添加'
    },
    editUser(obj) {
      this.ruleForm = JSON.parse(JSON.stringify(obj))
      this.editStatus = true
      this.isAddUser = false
      this.dialogBtnTitle = '修改'
    },
    deleteUser(id) {
      this.$axios.post("user/delete/" + id, {}, {
        headers: {
          'authorization': this.$store.getters.getToken,
        }
      })
          .then(resp => {
            this.$message.success(resp.data.message)
            this.getUsers()
          })
    },
    deleteUsers() {
      let ids = []
      for (let i = 0; i < this.multipleSelection.length; i++) {
        ids.push(this.multipleSelection[i].id)
      }
      console.log(ids)

      this.$axios.post("user/deletes/", ids, {
        headers: {
          'authorization': this.$store.getters.getToken,
        }
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.editStatus = false
        this.getUsers()
      })

    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          let address = this.isAddUser ? 'user/insert' : 'user/update';
          let data = {
            username: this.ruleForm.username,
            password: this.ruleForm.password,
            email: this.ruleForm.email,
            avatar: this.ruleForm.avatar,
            status: this.ruleForm.status
          };

          if (!this.isAddUser) {
            data.id = this.ruleForm.id
          }

          this.$axios.post(address, data, {
            headers: {
              'authorization': this.$store.getters.getToken,
            }
          }).then(resp => {
            this.$message.success(resp.data.message)
            this.editStatus = false
            this.getUsers()
          })
        } else {
          return false;
        }
      });
    },
    resetForm() {
      this.ruleForm = {
        id: 0,
        username: '',
        password: '',
        avatar: '',
        status: 1,
        email: '',
      }
    },
  },
  created() {
    this.getUsers()
  }
}
</script>

<style scoped>
.edit-box ::v-deep(.el-dialog) {
  padding-right: 30px !important;
}
</style>
