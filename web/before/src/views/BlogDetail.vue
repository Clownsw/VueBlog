<template>
  <div>
    <Header></Header>
    <div class="m-blog">
      <h1 class="m-blog-title">{{ blog.title }}</h1>
      <el-link v-show="onShow">
        <router-link
            :to="{
            name: 'BlogEdit',
            params: {
              blogId: this.$route.params.blogId,
            },
          }"
        >
          编辑
        </router-link>
      </el-link>

      <el-link v-show="onShow" style="margin-left: 10px;" @click="del">
        删除
      </el-link>
      <el-divider/>
      <v-md-preview :text="blog.content"></v-md-preview>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";
import {ElMessage, ElMessageBox} from 'element-plus'

export default {
  name: "BlogDetail",
  data() {
    return {
      blog: {},
      onShow: false,
    };
  },
  methods: {
    del() {
      ElMessageBox.confirm(
          '是否要删除该文章?',
          '警告',
          {
            confirmButtonText: '删除',
            cancelButtonText: '取消',
            type: 'warning',
          }
      )
          .then(() => {
            this.$axios.post('blog/remove/' + this.blog.id, {}, {
              headers: {
                'authorization': this.$store.getters.getToken
              }
            }).then(resp => {
              console.log(resp)
              if (resp.data.code === 200) {
                this.$router.push('/blogs')
              } else {
                ElMessage.error('删除失败!')
              }
            })
          })
          .catch(() => {
          })
    }
  },
  components: {
    Header,
  },
  created() {
    let blogId = this.$route.params.blogId;
    if (blogId !== undefined) {
      this.$axios.get("blog/" + blogId).then((resp) => {
        // content: "# Hello\n## 你好\n### Hi\n#### 嗨"
        // created: "2022-04-01T12:26:18"
        // description: "测试md"
        // id: 14
        // status: 0
        // title: "测试md"
        // user_id: 1
        if (resp.status === 200) {
          this.blog = resp.data;
          document.title = this.blog.title + "  -  Smilex' Blog";

          if (this.$store.getters.getUserInfo != null) {
            if (this.blog.user_id === this.$store.getters.getUserInfo.id) {
              this.onShow = true;
            }
          }
        }

        if (this.blog.code === 400) {
          this.$router.push('/blogs')
        }
      });
    } else {
      this.$router.push("/blogs");
    }
  },
};
</script>

<style scoped>
.m-blog {
  width: 100%;
  box-shadow: var(--el-box-shadow);
  padding: 20px 15px;
}

.m-blog-title {
  margin: 0 auto;
  display: table;
}
</style>