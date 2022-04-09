<template>
  <div>
    <el-form :model="blog" :rules="rules" ref="blog" label-width="100px" class="demo-ruleForm">
      <el-form-item label="博文名称" prop="title">
        <el-input v-model="blog.title"></el-input>
      </el-form-item>

      <el-form-item label="博文描述" prop="description">
        <el-input v-model="blog.description"></el-input>
      </el-form-item>

      <el-form-item label="博文内容" prop="content">
        <mavon-editor v-model="blog.content"></mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('blog')">{{ buttonName }}</el-button>
        <el-button @click="resetForm('blog')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "EditWord",
  data() {
    return {
      id: -1,   // -1 表示不正确, 0 表示新增博文, > 0表示修改博文
      blog: {
        title: '',
        description: '',
        content: '',
      },
      rules: {
        title: [
          {required: true, message: '请输入博文名称', trigger: 'blur'},
          {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入博文描述', trigger: 'blur'},
        ],
        content: [
          {required: true, message: '请输入博文内容', trigger: 'blur'},
        ],
      },
      buttonName: '',
    }
  },
  methods: {
    getBlogInfo(id) {
      this.$axios.get("blog/" + id)
          .then(resp => {
            this.blog = resp.data.data
          })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.id == 0) {
            this.addBlog()
          } else {
            this.updateBlog();
          }
        } else {
          return false;
        }
      });
    },
    updateBlog() {
      let obj = {
        id: this.blog.id,
        user_id: this.blog.user_id,
        title: this.blog.title,
        description: this.blog.description,
        content: this.blog.content
      }

      this.$axios.post("blog/edit", obj, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
          .then(resp => {
            this.$message.success(resp.data.message)
          })
    },
    addBlog() {
      let obj = {
        user_id: this.$store.getters.getUser.id,
        title: this.blog.title,
        description: this.blog.description,
        content: this.blog.content
      }
      this.$axios.post("blog/edit", obj, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
          .then(resp => {
            this.$message.success(resp.data.message)
          })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  },
  created() {
    this.id = this.$route.params.id
    console.log(this.id)
    this.buttonName = this.id == 0 ? '新增' : '修改'
    if (this.id > 0) {
      this.getBlogInfo(this.id)
    }
  }
}
</script>

<style scoped>

</style>
