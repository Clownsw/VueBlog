<template>
  <div>
    <el-form :model="footer" :rules="rules" ref="system" label-width="100px" class="demo-ruleForm">
      <el-form-item label="页脚脚本" prop="content">
        <mavon-editor v-model="footer.content"></mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('system')">修改</el-button>
        <el-button @click="resetForm('system')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "EditSystem",
  data() {
    return {
      footer: {},
      rules: {
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'},
        ],
      },
    }
  },
  methods: {
    getFooterInfo() {
      this.$axios.get("footer")
          .then(resp => {
            this.footer = resp.data.data
          })
    },
    updateFooterInfo() {
      return this.$axios.post('footer/update', this.footer, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.updateFooterInfo().then(resp => {
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
    this.getFooterInfo()
  }
}
</script>

<style scoped>

</style>
