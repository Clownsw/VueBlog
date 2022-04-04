<template>
  <div>
    <Header></Header>
    <div class="m-content">
      <el-form :model="form" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="form.title"/>
        </el-form-item>

        <el-form-item label="摘要">
          <el-input v-model="form.description" type="textarea"/>
        </el-form-item>

        <el-form-item label="内容">
          <v-md-editor v-model="form.content" height="400px"></v-md-editor>
        </el-form-item>

        <el-form-item class="push-button">
          <el-button type="primary" @click="add">添加</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";
import {ElMessage} from "element-plus";

export default {
  name: "BlogEdit",
  data() {
    return {
      form: {
        id: undefined,
        title: '',
        description: '',
        content: '',
      }
    }
  },
  methods: {
    add() {
      if (this.form.title !== '' && this.form.description !== '' && this.content !== '') {
        let blog;

        console.log(this.$store.getters.getUserInfo)

        if (this.form.id !== undefined) {
          blog = {
            id: this.form.id,
            user_id: this.$store.getters.getUserInfo.id,
            title: this.form.title,
            description: this.form.description,
            content: this.form.content
          }
        } else {
          blog = {
            user_id: this.$store.getters.getUserInfo.id,
            title: this.form.title,
            description: this.form.description,
            content: this.form.content
          }
        }

        this.$axios.post("blog/edit", JSON.stringify(blog), {
          headers: {
            'authorization': this.$store.getters.getToken
          }
        }).then(resp => {
          if (resp.data.code === 200) {
            ElMessage({
              message: resp.data.message,
              type: 'success',
            })
          } else {
            ElMessage.error(resp.data.message)
          }

          if (this.form.id === undefined) {
            this.reset()
          }
        })
      }
    },
    reset() {
      this.form = {
        title: '',
        description: '',
        content: '',
      }
    }
  },
  components: {
    Header
  },
  created() {
    const blogId = this.$route.params.blogId;

    console.log(blogId)

    // 编辑页面
    if (blogId !== undefined) {
      this.$axios("blog/" + blogId)
          .then(resp => {
            if (resp.status === 200) {
              // content: "# Hello\n## 你好\n### Hi\n#### 嗨"
              // created: "2022-04-01T12:26:18"
              // description: "测试md"
              // id: 14
              // status: 0
              // title: "测试md"
              // user_id: 1
              this.form.id = resp.data.id
              this.form.title = resp.data.title;
              this.form.content = resp.data.content;
              this.form.description = resp.data.description
            }
          })
    } else {
      this.form.id = undefined
    }
  }
}
</script>

<style scoped>
.push-button ::v-deep(.el-form-item__content) {
  justify-content: center;
}
</style>