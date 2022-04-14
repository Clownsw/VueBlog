<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <div class="m-blog">
      <h1 class="m-blog-title">{{ blog.title }}</h1>
      <el-divider/>
      <v-md-preview :text="blog.content"></v-md-preview>
      <div class="tags">
        <el-tag v-for="item in tags" style="margin: 3px 10px 3px 0">
          {{ item.name }}
        </el-tag>
      </div>
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
      tags: [],
      onShow: false,
      systemInfo: {},
    };
  },
  methods: {
    getBlogTags(id) {
      this.$axios.get("tag/" + id)
          .then(resp => {
            this.tags = resp.data.data
          })
    },
  },
  components: {
    Header,
  },
  created() {
    this.systemInfo = this.$store.getters.getSystemInfo

    let blogId = this.$route.params.blogId;
    if (blogId !== undefined) {
      this.getBlogTags(blogId)
      this.$axios.get("blog/" + blogId).then((resp) => {
        if (resp.status === 200) {
          this.blog = resp.data.data;
          document.title = this.blog.title + ' - ' + this.systemInfo.title;

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
  padding: 10px 0;
}

.m-blog-title {
  margin: 0 auto;
  display: table;
}

.tags {
  padding: 0 20px;
}
</style>
