<template>
  <div class="app-container">
    <div class="tags clearfix"
      style="margin-top: 20px; display: flex; justify-content: center; align-items: flex-start;">

      <el-card class="box-card" style="margin-right: 10px; max-width: 600px; float: left">
        <div slot="header" class="clearfix">
          <span>所有标签</span>
        </div>

        <div class="tags">
          <el-tag v-for="item in tags" :key="item.id" @click.native="editTag(item.id)">
            {{ item.name }}
          </el-tag>
        </div>
      </el-card>

      <el-card class="box-card" style="max-width: 600px; min-width: 450px;">
        <div slot="header" class="clearfix">
          <span>添加标签</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="returnEdit">返回新增</el-button>
        </div>

        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="标签名称" prop="name">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>

          <el-form-item class="tag-dlalog" style="margin-left: 0; text-align: center;">
            <el-button type="primary" @click="submitForm('ruleForm')" size="mini">
              {{ formBtnName }}
            </el-button>
            <el-button type="danger" v-show="delBtnStatus" @click="deleteForm" size="mini">删除</el-button>
          </el-form-item>
        </el-form>

      </el-card>
    </div>
  </div>
</template>

<script>
import tagApi from '../../api/tag'

export default {
  name: 'TagIndex',
  data() {
    return {
      tags: [],
      ruleForm: {
        name: '',
      },
      rules: {
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' },
          { min: 1, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
      },
      formBtnName: '保存',
      delBtnStatus: false,
      status: 0, // 0 = 新增, 1 = 编辑
      tmp: 0,
    }
  },
  methods: {
    fetchGetTags() {
      tagApi.getTagList().then(resp => {
        this.tags = resp.data
      })
    },
    fetchSaveTag() {
      return tagApi.saveTag({
        name: this.ruleForm.name
      })
    },
    fetchUpdateTagByID() {
      return tagApi.updateTagById({
        id: this.tmp,
        name: this.ruleForm.name
      })
    },
    fetchDeleteTagById() {
      return tagApi.deleteTagById(this.tmp)
    },
    editTag(id) {
      this.delBtnStatus = true
      this.status = 1
      this.formBtnName = '更新'
      this.tmp = id

      for (let i = 0; i < this.tags.length; i++) {
        if (this.tags[i].id === id) {
          this.ruleForm.name = this.tags[i].name
        }
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.status === 0) {
            this.fetchSaveTag().then(resp => {
              this.$message.success(resp.message)

              // 刷新
              this.fetchGetTags()
              this.returnEdit()
            })
          } else {
            this.fetchUpdateTagByID().then(resp => {
              if (resp.code === 200) {
                this.$message.success(resp.message)
              } else {
                this.$message.error(resp.message)
              }

              // 刷新
              this.fetchGetTags()
            })
          }
        } else {
          return false;
        }
      });
    },
    deleteForm() {
      this.fetchDeleteTagById().then(resp => {
        this.$message.success(resp.message)

        // 刷新
        this.fetchGetTags()
        this.returnEdit()
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    returnEdit() {
      this.delBtnStatus = false
      this.status = 0
      this.resetForm('ruleForm')
      this.formBtnName = '保存'
    }
  },
  created() {
    this.fetchGetTags()
  }
}
</script>

<style scoped>
.tags {
  margin: 0 auto;
}

.tags span {
  margin-bottom: 10px;
  margin-right: 10px;
  cursor: pointer;
}

.tag-dlalog ::v-deep .el-form-item__content {
  margin-left: 0 !important;
}
</style>
