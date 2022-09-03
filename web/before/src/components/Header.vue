<template>
  <div class="m-content">
    <h2>{{ welcome }}</h2>
    <div class="m-action" style="display: flex; justify-content: center;">

      <ul style="list-style: none; display: flex; margin: 0 auto; width: auto; padding-inline-start: 0;">
        <li>
          <span>
            <el-link href="/blogs" :underline="false">所有</el-link>
          </span>
          <el-divider direction="vertical"/>
        </li>

        <li v-for="sort in sorts">
          <span>
            <el-link :href="'/blogs/sort/' + sort.id" :underline="false">{{ sort.name }}</el-link>
          </span>
          <el-divider direction="vertical"/>
        </li>

        <li>
          <span>
            <el-link href="/friend" :underline="false">友链</el-link>
          </span>
        </li>

        <li>
          <el-divider direction="vertical" border-style="dashed"/>
          <span>
            <el-link href="/me" :underline="false">我</el-link>
          </span>
        </li>

        <li>
          <el-divider direction="vertical" border-style="dashed"/>
          <span>
            <el-link :underline="false" @click="searchDialog">搜索</el-link>
            <blog-search ref='searchDialog'></blog-search>
          </span>
        </li>
      </ul>
    </div>

    <aplayer :audio="audio" fixed :lrcType="3"/>
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
      sorts: this.$store.getters.getSortList,
      audio: {
        name: '回到夏天',
        artist: '傲七爷 / 小田音乐社',
        url: 'http://music.163.com/song/media/outer/url?id=1449678888.mp3',
        cover: 'https://p1.music.126.net/6DtmsO-P4jyJPLjnGa8Ytg==/109951165004114065.jpg?param=300y300',
        lrc: 'https://music.api.smilex.cn/lyric?songId=1449678888',
      },
    }
  },
  methods: {
    searchDialog() {
      this.$refs.searchDialog.searchDialogIsShow = true
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

.m-action {
  margin: 10px 0;
}

.aplayer {
  z-index: 9999 !important;
}
</style>
