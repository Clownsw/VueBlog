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

      <el-form-item label="博文内容" prop="content">
        <mavon-editor :boxShadow="true" :code-style="'github-dark'" v-model="blog.content" class="blog-body">
        </mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click.native="submitForm('blog')">{{ buttonName }}</el-button>
        <el-button @click="resetForm('blog')">重置</el-button>
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
        }
      },
      rules: {
        title: [
          { required: true, message: '请输入博文名称', trigger: 'blur' },
          { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入博文描述', trigger: 'blur' },
        ],
        content: [
          { required: true, message: '请输入博文内容', trigger: 'blur' },
        ],
        sort: [
          { required: true, message: '请选择分类', trigger: 'blur' },
        ]
      },
      buttonName: '',
      tags: [],
      sorts: [],
      inputVisible: false,
      inputValue: ''
    }
  },
  methods: {
    fetchGetBlogById(id) {
      blogApi.getBlogById(id).then(resp => {
        this.blog = resp.data
        this.tags = resp.data.tags
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
          if (this.id === -1) {
            this.addBlog()
          } else {
            this.updateBlog();
          }
        } else {
          return false;
        }
      });
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
      }
      console.log(obj);
      this.fetchBlogAdd(obj).then(resp => {
        this.$message.success(resp.message)

        setTimeout(() => {
          this.$router.push('/blog/list')
        }, 1000)
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleClose(tag) {
      this.tags.splice(this.tags.indexOf(tag), 1);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        let r = async () => {
          let resp = await this.fetchExistsTagByName(inputValue)
          // 存在
          if (resp.data) {
            await this.localAddTag(inputValue)
          }
          // 不存在
          else {
            await tagApi.saveTag({
              name: inputValue
            })
            await this.localAddTag(inputValue)
          }
        }

        r().then(() => { })
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
    getBlogTags(id) {
      this.$axios.get("tag/" + id)
        .then(resp => {
          this.tags = resp.data.data
        })
    },
    async localAddTag(inputValue) {
      let resp = await this.fetchGetTagIdByName(inputValue)

      let tag = resp.data
      if (tag.id === -1) {
        this.tags.push({
          id: -1,
          name: inputValue
        })
      } else {
        this.tags.push({
          id: tag.id,
          name: inputValue
        })
      }
    }
  },
  created() {
    const isAdd = this.$route.name === 'BlogAdd'
    this.buttonName = isAdd ? '新增' : '修改'
    if (!isAdd) {
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
