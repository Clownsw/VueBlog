<template>
  <div class="app-container">
    <el-form :model="blog" :rules="rules" ref="blog" label-width="100px" class="demo-ruleForm">
      <el-form-item label="博文名称" prop="title">
        <el-input v-model="blog.title"></el-input>
      </el-form-item>

      <el-form-item label="博文描述" prop="description">
        <el-input v-model="blog.description"></el-input>
      </el-form-item>

      <el-form-item label="博文分类" prop="sort">
        <el-select v-model="blog.sort.id" placeholder="请选择">
          <el-option v-for="s in sorts" :key="s.id" :label="s.name" :value="s.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="博客标签" prop="content">
        <draggable v-model="tags">
          <el-tag :key="tag.id" v-for="tag in tags" closable :disable-transitions="false" @close="handleClose(tag)"
                  class="tag">
            {{ tag.name }}
          </el-tag>
        </draggable>

        <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
                  @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">添加新标签</el-button>
      </el-form-item>

      <el-form-item label="博文状态" prop="status">
        <el-radio v-model="blog.status" :label="0">未加密</el-radio>
        <el-radio v-model="blog.status" :label="1">加密</el-radio>
      </el-form-item>

      <el-form-item label="加密秘钥" prop="key" v-if="blog.status === 1">
        <el-input v-model="key"></el-input>
      </el-form-item>

      <el-form-item label="加密标题" prop="key" v-if="blog.status === 1">
        <el-input v-model="key_title"></el-input>
      </el-form-item>

      <el-form-item label="博文内容" prop="content">
        <mavon-editor :boxShadow="true" :code-style="'github-dark'" :externalLink="externalLink" v-model="blog.content"
                      class="blog-body">
        </mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click.native="submitForm('blog')" size="mini">{{ buttonName }}</el-button>
        <el-button @click="resetForm('blog')" size="mini">重置</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
import draggable from 'vuedraggable'
import blogApi from '../../api/blog'
import sortApi from '../../api/sort'
import tagApi from '../../api/tag'

export default {
  name: 'BlogForm',
  components: {
    draggable
  },
  data() {
    return {
      id: -1,   // -1 表示不正确, 0 表示新增博文, > 0表示修改博文
      blog: {
        title: '',
        description: '',
        content: '',
        sort: {
          id: null
        },
        status: 0
      },
      rules: {
        title: [
          { required: true, message: '请输入博文名称', trigger: 'blur' },
          { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入博文描述', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入博文内容', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请选择分类', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请输入博文内容', trigger: 'blur' }
        ]
      },
      buttonName: '',
      tags: [],
      sorts: [],
      key: '',
      key_title: '',
      inputVisible: false,
      inputValue: '',
      externalLink: {
        markdown_css: function() {
          // 这是你的markdown css文件路径
          return 'https://cdn.smilex.cn/cdn/mavon-editor/markdown/github-markdown.min.css'
        },
        hljs_js: function() {
          // 这是你的hljs文件路径
          return 'https://cdn.smilex.cn/cdn/mavon-editor/highlightjs/highlight.min.js'
        },
        hljs_css: function(css) {
          // 这是你的代码高亮配色文件路径
          return 'https://cdn.smilex.cn/cdn/mavon-editor/highlightjs/styles/' + css + '.min.css'
        },
        hljs_lang: function(lang) {
          // 这是你的代码高亮语言解析路径
          return 'https://cdn.smilex.cn/cdn/mavon-editor/highlightjs/languages/' + lang + '.css'
        },
        katex_css: function() {
          // 这是你的katex配色方案路径路径
          return 'https://cdn.smilex.cn/cdn/mavon-editor/katex/katex.min.css'
        },
        katex_js: function() {
          // 这是你的katex.js路径
          return 'https://cdn.smilex.cn/cdn/mavon-editor/katex/katex.min.js'
        }
      }
    }
  },
  methods: {
    fetchGetBlogById(id) {
      blogApi.getBlogById(id).then(resp => {
        this.blog = resp.data
        this.tags = resp.data.tags

        if (this.blog.status === 1) {
          blogApi.getBlogKeyById(this.blog.id).then(resp => {
            this.key = resp.data.key
            this.key_title = resp.data.title
          })
        }
      })
    },
    fetchBlogAdd(blog) {
      return blogApi.saveBlog(blog)
    },
    fetchBlogUpdateById(blog) {
      return blogApi.updateBlogById(blog)
    },
    fetchGetSortList() {
      sortApi.getSortList().then(resp => {
        this.sorts = resp.data
      })
    },
    fetchExistsTagByName(name) {
      return tagApi.existsTagByName(name)
    },
    fetchGetTagIdByName(name) {
      return tagApi.getTagIdByName(name)
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          for (let i = 0; i < this.tags.length; i++) {
            this.tags[i].sort = i
          }

          if (this.id === -1) {
            this.addBlog()
          } else {
            this.updateBlog()
          }

        } else {
          return false
        }
      })
    },
    updateBlog() {
      let obj = {
        id: this.blog.id,
        user_id: this.blog.user_id,
        sort_id: this.blog.sort.id,
        title: this.blog.title,
        description: this.blog.description,
        content: this.blog.content,
        tag: this.tags,
        status: this.blog.status,
        key: this.key,
        key_title: this.key_title
      }

      this.fetchBlogUpdateById(obj).then(resp => {
        this.$message.success(resp.message)
      })
    },
    addBlog() {
      let obj = {
        user_id: this.$store.getters.userInfo.id,
        title: this.blog.title,
        sort_id: this.blog.sort.id,
        description: this.blog.description,
        content: this.blog.content,
        tag: this.tags,
        status: this.blog.status,
        key: this.key,
        key_title: this.key_title
      }

      this.fetchBlogAdd(obj).then(resp => {
        this.$message.success(resp.message)

        setTimeout(() => {
          this.$router.push('/blog/list')
        }, 1000)
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    handleClose(tag) {
      this.tags.splice(this.tags.indexOf(tag), 1)
    },
    showInput() {
      this.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleInputConfirm() {
      let inputValue = this.inputValue
      if (inputValue) {
        let r = async() => {
          let resp = await this.fetchExistsTagByName(inputValue)
          // 存在
          if (resp.data) {
            await this.localAddTag(inputValue)
          }
          // 不存在
          else {
            await tagApi.saveTag(inputValue)
            await this.localAddTag(inputValue)
          }
        }

        r().then(() => {
        })
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    getBlogTags(id) {
      this.$axios.get('tag/' + id)
        .then(resp => {
          this.tags = resp.data.data
        })
    },
    async localAddTag(inputValue) {
      let resp = await this.fetchGetTagIdByName(inputValue)

      if (resp.code !== 200) {
        this.$message.error(resp.message)
        return
      }

      this.tags.push(resp.data)
    }
  },
  watch: {
    blog: {
      handler(newVal, oldVal) {
        if (this.blog.status === 0) {
          this.key = ''
          this.key_title = ''
        }
      },
      deep: true
    }
  },
  created() {
    const isAdd = this.$route.name === 'BlogAdd'
    this.buttonName = isAdd ? '新增' : '修改'
    if (!isAdd) {
      this.id = this.$route.params.id
      this.fetchGetBlogById(this.$route.params.id)
    }
    this.fetchGetSortList()
  }
}
</script>

<style scoped>
.tag {
  margin-right: 10px;
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
</style>
