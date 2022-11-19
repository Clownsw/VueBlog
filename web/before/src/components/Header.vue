<template>
  <div class="m-content">
    <h2 class="title">{{ welcome }}</h2>
    <div class="m-action" style="display: flex; justify-content: center;">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal"
               @select="handleSelect">
        <el-menu-item v-for="sort in sorts" :key="sort.name" :index="sort.name">
          <router-link :to="'/blogs/sort/' + sort.id">{{ sort.name }}</router-link>
        </el-menu-item>

        <el-menu-item index="友链">
          <router-link to="/friend">友链</router-link>
        </el-menu-item>

        <el-menu-item index="我">
          <router-link to="/me">我</router-link>
        </el-menu-item>
      </el-menu>
    </div>

    <blog-search ref='searchDialog'></blog-search>
    <el-backtop :bottom="100" :visibility-height="50"></el-backtop>
  </div>
</template>

<script>

import BlogSearch from '@/components/BlogSearch'

export default {
  name: "Header",
  props: {
    welcome: '',
  },
  components: {
    BlogSearch
  },
  data() {
    return {
      activeIndex: '',
      sorts: this.$store.getters.getSortList,
    }
  },
  methods: {
    searchDialog() {
      this.$refs.searchDialog.searchDialogIsShow = true
    },
    handleSelect(key) {
      this.activeIndex = key
      if (key === '友链') {
        this.$router.push('/friend')
      } else if (key === '我') {
        this.$router.push('/me')
      } else {
        this.$router.push(`/blogs/sort/${this.getSortIdBySortName(key)}`)
      }
    },
    getSortIdBySortName(sortName) {
      for (let i = 0; i < this.sorts.length; i++) {
        if (this.sorts[i].name === sortName) {
          return this.sorts[i].id
        }
      }
    },
    getSortNameBySortId(sortId) {
      for (let i = 0; i < this.sorts.length; i++) {
        if (this.sorts[i].id === sortId) {
          return this.sorts[i].name
        }
      }
      return '置顶'
    }
  },
  mounted() {
    window.addEventListener('keydown', e => {
      if (e.ctrlKey && e.shiftKey && e.key === 'F') {
        this.searchDialog()
      }
    });
  },
  created() {
    if (this.$route.path === '/friend') {
      this.activeIndex = '友链'
    } else if (this.$route.path === '/me') {
      this.activeIndex = '我'
    } else if (this.$route.path.indexOf('blogs\/sort') !== -1) {
      const sortId = parseInt(this.$route.path.substring(this.$route.path.lastIndexOf('/') + 1, this.$route.path.length))
      this.activeIndex = this.getSortNameBySortId(sortId)
    } else {
      this.activeIndex = '置顶'
    }
  }
}
</script>

<style scoped>
.m-content {
  margin: 0 auto;
  max-width: 960px;
  text-align: center;
}

.m-content .title {
  margin: 1rem 0;
}

.m-action {
  margin: 10px 0;
}

a {
  text-decoration: none;
}

.aplayer {
  z-index: 9999 !important;
}
</style>
