<template>
  <div>
    <el-form :model="formObj" ref="system" label-width="100px">
      <el-form-item label="欢迎语" prop="welcome">
        <el-input type="text" v-model="formObj.system.welcome"></el-input>
      </el-form-item>

      <el-form-item label="网站标题" prop="title">
        <el-input type="text" v-model="formObj.system.title"></el-input>
      </el-form-item>

      <el-form-item label="网站描述" prop="description">
        <el-input type="text" v-model="formObj.system.description"></el-input>
      </el-form-item>

      <el-form-item label="页脚脚本" prop="content">
        <mavon-editor v-model="formObj.footer.content"></mavon-editor>
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
      formObj: {
        system: {
          welcome: '',
          title: '',
          description: '',
        },
        footer: {},
      },
    }
  },
  methods: {
    getSystemInfo() {
      this.$axios.get('system/info')
          .then(resp => {
            this.formObj.system = resp.data.data
          })
    },
    getFooterInfo() {
      this.$axios.get("footer")
          .then(resp => {
            this.formObj.footer = resp.data.data
          })
    },
    updateFooterInfo() {
      return this.$axios.post('system/update', this.formObj, {
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
    this.getSystemInfo()
    this.getFooterInfo()
  }
}
</script>

<style scoped>

</style>
