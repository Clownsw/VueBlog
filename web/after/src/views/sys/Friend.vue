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
        <el-button type="primary" @click="addFriendDialog">新增</el-button>
      </el-form-item>

      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗?" @confirm="deleteFriends">
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
          prop="name"
          label="友链名称"
          width="200"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="description"
          label="友链描述"
          width="350"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="href"
          label="友链地址"
          show-overflow-tooltip
          align="center">
      </el-table-column>

      <el-table-column
          prop="avatar"
          label="友链头像"
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
          <el-button type="info" @click="editFriend(scope.row.id)">编辑</el-button>

          <el-popconfirm
              confirm-button-text='删除'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="您确定要删除该用户吗？"
              style="margin-left: 5px"
              @confirm="deleteFriends(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        title="提示"
        :visible.sync="dialogStatus"
        top="8vh"
        width="30%"
        :before-close="handleClose">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="友链名称" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>

        <el-form-item label="友链描述" prop="description">
          <el-input v-model="ruleForm.description"></el-input>
        </el-form-item>

        <el-form-item label="友链链接" prop="href">
          <el-input v-model="ruleForm.href"></el-input>
        </el-form-item>

        <el-form-item label="友链头像" prop="avatar">
          <el-input v-model="ruleForm.avatar"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">{{ dialogTitle }}</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Friend",
  data() {
    return {
      searchName: '',
      deleteStatus: true,
      dialogStatus: false,
      dialogTitle: '',
      isAdd: true,
      tableData: [],
      multipleSelection: [],
      ruleForm: {
        name: '',
        description: '',
        href: '',
        avatar: '',
      },
      rules: {
        name: [
          {required: true, message: '请输入友链名称', trigger: 'blur'},
        ],
        description: [
          {required: true, message: '请输入友链描述', trigger: 'blur'},
        ],
        href: [
          {required: true, message: '请输入友链地址', trigger: 'blur'},
        ],
        avatar: [
          {required: true, message: '请输入友链头像', trigger: 'blur'},
        ],
      }
    }
  }
  ,
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.deleteStatus = this.multipleSelection.length <= 0;
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            this.dialogStatus = false
            done();
          })
          .catch(_ => {
          });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let url = this.isAdd ? "friend/add" : "friend/update";

          this.$axios.post(url, this.ruleForm, {
            headers: {
              'authorization': this.$store.getters.getToken
            }
          }).then(resp => {
            this.$message.success(resp.data.message)
            this.dialogStatus = false
            this.getFriends()
          })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    addFriendDialog() {
      this.dialogStatus = true
      this.isAdd = true
      this.dialogTitle = this.isAdd ? '添加' : '修改'
    },
    editFriend(id) {
      this.dialogStatus = true
      this.isAdd = false
      this.dialogTitle = this.isAdd ? '添加' : '修改'

      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].id === id) {
          this.ruleForm = JSON.parse(JSON.stringify(this.tableData[i]))
          break
        }
      }
    },
    deleteFriends(id) {
      let ids = []
      if (id === undefined) {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id)
        }
      } else {
        ids.push(id)
      }

      this.$axios.post("friend/deletes", ids, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.getFriends()
      })
    },
    getFriends() {
      this.$axios.get("friends").then(resp => {
        this.tableData = resp.data.data
      })
    }
  },
  created() {
    this.getFriends()
  }
}
</script>

<style scoped>

</style>
