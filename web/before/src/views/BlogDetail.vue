<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <div class="m-blog">
      <h1 class="m-blog-title">{{ blog.title }}</h1>
	  
	  <div class="tags" style="text-align: center; margin-top: 5px;">
        <el-tag v-for="item in tags" style="margin: 3px 10px 3px 0">
          {{ item.name }}
        </el-tag>
      </div>

      <div style="display: flex; justify-content: center">
        <p class="blog-description" style="display: inline-block; margin-right: 10px">{{
            parseStrToDate(blog.created)
          }}</p>

        <router-link :to="{ name: 'BlogsId', params: { id: blog.sort.id } }"
                     class="blog-description">
          {{ blog.sort.name }}
        </router-link>
      </div>

      <el-divider/>

	  <mavon-editor :subfield="false" :editable="false" :defaultOpen="'preview'" :toolbarsFlag="false" class="blog-body" v-model="blog.content">
	  </mavon-editor>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "BlogDetail",
  data() {
    return {
      blog: {
        sort: {
          id: 0
        },
        name: ''
      },
      tags: [],
      onShow: false,
      systemInfo: {},
    };
  },
  methods: {
    parseStrToDate(str) {
      return new Date(str).toLocaleString()
    },
  },
  components: {
    Header,
  },
  created() {
    this.systemInfo = this.$store.getters.getSystemInfo

    let blogId = this.$route.params.blogId;
    if (blogId !== undefined) {
      this.$axios.get("blog/" + blogId).then((resp) => {
        if (resp.status === 200) {
          this.blog = resp.data.data;
          this.tags = resp.data.data.tags

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
  color: #303133;
}

.tags {
  padding: 0 20px;
}

a {
  text-decoration: none;
}

.blog-description {
  font-size: 13px;
  color: #a0a3a8;
  margin-block-start: 1em;
  margin-block-end: 0;
}

.blog-body ::v-deep blockquote {
  margin: 0 !important;
  padding: 0.3em 1em;
}
</style>
