<template>
  <div class="app-container">
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
        <el-button type="primary" @click="submitForm('system')" size="mini">修改</el-button>
        <el-button @click="resetForm('system')" size="mini">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import systemApi from '../../api/system'

export default {
  name: 'SystemIndex',
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
    fetchGetSystem() {
      systemApi.getSystem().then(resp => {
        this.formObj.system = resp.data
      })
    },
    fetchGetFooter() {
      systemApi.getFooter().then(resp => {
        this.formObj.footer = resp.data
      })
    },
    fetchUpdateSystem() {
      return systemApi.updateSystem(this.formObj)
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.fetchUpdateSystem().then(resp => {
            this.$message.success(resp.message)
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
    this.fetchGetSystem()
    this.fetchGetFooter()
  }
}
</script>

<style scoped>
</style>
