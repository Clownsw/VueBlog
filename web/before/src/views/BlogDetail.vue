<template>
  <div>
    <Header></Header>
    <div class="m-blog">
      <h1 class="m-blog-title">{{ blog.title }}</h1>
      <el-divider/>
      <v-md-preview :text="blog.content"></v-md-preview>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "BlogDetail",
  data() {
    return {
      blog: {},
      onShow: false,
    };
  },
  methods: {
  },
  components: {
    Header,
  },
  created() {
    let blogId = this.$route.params.blogId;
    if (blogId !== undefined) {
      this.$axios.get("blog/" + blogId).then((resp) => {
        if (resp.status === 200) {
          this.blog = resp.data.data;
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
