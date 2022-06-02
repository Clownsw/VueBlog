<template>
  <div>
    <el-form :model="me" :rules="rules" ref="blog" label-width="100px" class="demo-ruleForm">
      <el-form-item label="标题" prop="title">
        <el-input v-model="me.title"></el-input>
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <mavon-editor v-model="me.content"></mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('blog')">修改</el-button>
        <el-button @click="resetForm('blog')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "EditMe",
  data() {
    return {
      me: {
        title: '',
        content: '',
      },
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
          {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
        ],
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'},
        ],
      },
    }
  },
  methods: {
    getMeInfo() {
      this.$axios.get("me")
          .then(resp => {
            this.me = resp.data.data
          })
    },
    updateMeInfo() {
      return this.$axios.post('me/update', this.me, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.updateMeInfo().then(resp => {
            this.$message.success(resp.data.message)
          })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  },
  created() {
    this.getMeInfo()
  }
}
</script>

<style scoped>

</style>
