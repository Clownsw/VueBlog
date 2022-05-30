<template>
  <div>
    <el-form :model="blog" :rules="rules" ref="blog" label-width="100px" class="demo-ruleForm">
      <el-form-item label="博文名称" prop="title">
        <el-input v-model="blog.title"></el-input>
      </el-form-item>

      <el-form-item label="博文描述" prop="description">
        <el-input v-model="blog.description"></el-input>
      </el-form-item>

      <el-form-item label="博文分类" prop="sort">
        <el-select v-model="blog.sort.id" placeholder="请选择">
          <el-option v-for="s in sorts" :label="s.name" :value="s.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="博客标签" prop="content">
        <el-tag
            :key="tag.name"
            v-for="tag in tags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
            class="tag"
        >
          {{ tag.name }}
        </el-tag>
        <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">添加新标签</el-button>
      </el-form-item>

      <el-form-item label="博文内容" prop="content">
        <mavon-editor v-model="blog.content"></mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('blog')">{{ buttonName }}</el-button>
        <el-button @click="resetForm('blog')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "EditWord",
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
          {required: true, message: '请输入博文名称', trigger: 'blur'},
          {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入博文描述', trigger: 'blur'},
        ],
        content: [
          {required: true, message: '请输入博文内容', trigger: 'blur'},
        ],
        sort: [
          {required: true, message: '请选择分类', trigger: 'blur'},
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
    getBlogInfo(id) {
      this.$axios.get("blog/" + id)
          .then(resp => {
            this.blog = resp.data.data
            this.tags = resp.data.data.tags
          })
    },
    getAllSort() {
      this.$axios.get("/sort/list").then(resp => {
        this.sorts = resp.data.data
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.id == 0) {
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

      this.$axios.post("blog/edit", obj, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
          .then(resp => {
            this.$message.success(resp.data.message)
          })
    },
    addBlog() {
      let obj = {
        user_id: this.$store.getters.getUser.id,
        title: this.blog.title,
        sort_id: this.blog.sort.id,
        description: this.blog.description,
        content: this.blog.content,
        tag: this.tags,
      }
      this.$axios.post("blog/edit", obj, {
        headers: {
          'authorization': this.$store.getters.getToken
        }
      })
          .then(resp => {
            this.$message.success(resp.data.message)

            setTimeout(() => {
              this.$router.push('/sys/words')
            }, 1000)
          })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleClose(tag) {
      this.tags.splice(this.tags.indexOf(tag), 1);
    },
    tagExist(name) {
      return this.$axios.get("tag/exist/" + name)
    },
    getTagIdByName(name) {
      return this.$axios.get("tag/id/" + name)
    },
    addTag(name) {
      return this.$axios.post("tag/add", {
            name: name,
          },
          {
            headers: {
              'authorization': this.$store.getters.getToken
            }
          })
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
        let r = this.tagExist(inputValue)
        r.then(resp => {
          // 存在
          if (resp.data.data) {
            this.localAddTag(inputValue)
          }
          // 不存在
          else {
            // 先添加
            this.addTag(inputValue).then(_ => {
              this.localAddTag(inputValue)
            })
          }
        })
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
    localAddTag(inputValue) {
      this.getTagIdByName(inputValue).then(resp => {
        let tag = resp.data.data
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
      })
    }
  },
  created() {
    this.id = this.$route.params.id
    this.buttonName = this.id == 0 ? '新增' : '修改'
    if (this.id > 0) {
      this.getBlogInfo(this.id)
    }
    this.getAllSort()
  }
}
</script>

<style scoped>
.tag {
  margin-right: 10px;
}
</style>
