<template>
  <div>
    <Header ref="header" :welcome="systemInfo.welcome"></Header>
    <div class="m-blog">
      <h1 class="m-blog-title">{{ blog.title }}</h1>

      <div class="tags" style="text-align: center; margin-top: 5px;">
        <router-link :to="{ name: 'BlogsTag', params: { tagId: tag.id } }" v-for="tag in blog.tags" :key="tag.id">
          <el-tag style="margin: 3px 10px 3px 0">
            {{ tag.name }}
          </el-tag>
        </router-link>
      </div>

      <div style="display: flex; justify-content: center">
        <p class="blog-description" style="display: inline-block; margin-right: 10px">{{
            parseStrToDate(blog.created)
        }}</p>

        <router-link :to="{ name: 'BlogsId', params: { id: blog.sort.id } }" class="blog-description">
          {{ blog.sort.name }}
        </router-link>
      </div>

      <el-divider />
      <el-empty v-if="blog.status === 1" description="该文章已加密!">
        <el-input v-model="key" placeholder="输入秘钥" @keyup.enter.native="handlerInputKey"></el-input>
      </el-empty>
      <mavon-editor v-else v-viewer="viewerOptions" ref="markdown"
                    :fontSize="'20px'"
                    :subfield="false"
                    :editable="false"
                    :defaultOpen="'preview'"
                    :toolbarsFlag="false"
                    :boxShadow="true"
                    :code-style="'monokai-sublime'"
                    :imageClick="handleImageClick"
                    :externalLink="externalLink"
                    class="blog-body"
                    v-model="blog.content"/>
    </div>

    <Valine />
  </div>
</template>

<script>
import Valine from "@/components/Valine";
import Header from "@/components/Header";

export default {
  name: "BlogDetail",
  data() {
    return {
      viewerOptions: {
        filter (image) {
          return !!image.closest(".v-note-show")
        },
        movable: false,
      },
      blog: {
        sort: {
          id: 0
        },
        name: ''
      },
      tags: [],
      onShow: false,
      systemInfo: this.$store.getters.getSystemInfo,
      loading: null,
      key: '',
      externalLink: {
            markdown_css: function() {
                // 这是你的markdown css文件路径
                return 'https://cdn.smilex.cn/cdn/mavon-editor/markdown/github-markdown.min.css';
            },
            hljs_js: function() {
                // 这是你的hljs文件路径
                return 'https://cdn.smilex.cn/cdn/mavon-editor/highlightjs/highlight.min.js';
            },
            hljs_css: function(css) {
                // 这是你的代码高亮配色文件路径
                return 'https://cdn.smilex.cn/cdn/mavon-editor/highlightjs/styles/' + css + '.min.css';
            },
            hljs_lang: function(lang) {
                // 这是你的代码高亮语言解析路径
                return 'https://cdn.smilex.cn/cdn/mavon-editor/highlightjs/languages/' + lang + '.css';
            },
            katex_css: function() {
                // 这是你的katex配色方案路径路径
                return 'https://cdn.smilex.cn/cdn/mavon-editor/katex/katex.min.css';
            },
            katex_js: function() {
                // 这是你的katex.js路径
                return 'https://cdn.smilex.cn/cdn/mavon-editor/katex/katex.min.js';
            },
      }
    };
  },
  methods: {
    handleImageClick() {

    },
    parseStrToDate(str) {
      return new Date(str).toLocaleString()
    },
    handlerInputKey() {
      if (this.key !== '' && this.key.length <= 100) {
        this.$axios.get(`blog/key/${this.blog.id}/${this.key}`)
        .then(resp => {
          if (resp.data.code !== 200) {
            this.$message.error('秘钥错误!')
            return
          }

          this.blog.title = resp.data.data.title
          this.blog.content = resp.data.data.content
          this.blog.status = 0

          document.title = this.blog.title + ' - ' + this.systemInfo.title;
        })
        .catch(_ => {
          this.$message.error('秘钥错误!')
        })
      }
    }
  },
  components: {
    Header,
    Valine
  },
  created() {
    this.loading = this.$loading({
      lock: true,
      text: 'loading',
      spinner: 'el-icon-loading',
      background: 'rgb(0 0 0 / 80%)'
    })

    let blogId = this.$route.params.blogId;
    if (blogId !== undefined) {
      this.$axios.get("blog/" + blogId).then((resp) => {
        if (resp.status === 200) {
          this.blog = resp.data.data;
          this.tags = resp.data.data.tags

          document.title = this.blog.title + ' - ' + this.systemInfo.title;
          this.$refs.header.activeIndex = this.blog.sort.name

          if (this.$store.getters.getUserInfo != null) {
            if (this.blog.user_id === this.$store.getters.getUserInfo.id) {
              this.onShow = true;
            }
          }
        }

        if (this.blog.code === 400) {
          this.$router.push('/blogs')
        }

        if (this.blog.status === 1) {
          this.$message.error('该文章已加密!')
        }
      }).catch(() => {
        this.$router.push("/blogs");
      })
    } else {
      this.$router.push("/blogs");
    }
  },
  mounted() {
    this.loading.close()
  }
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

.blog-body ::v-deep pre {
  padding: 0;
}

.blog-body ::v-deep .hljs {
  padding: 12px;
}

.markdown-body {
  font-size: 17px !important;
}
</style>
