<template>
  <div>
    <el-row>
      <el-col :xl="{ span: 13, offset: 6 }" :lg="{ span: 15, offset: 5 }">
        <aplayer :audio="musicList" :lrcType="3" fixed autoplay/>
        <router-view :key="$route.fullPath"/>
        <div class="page-footer" v-html="this.$store.getters.getPageFooter">
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import musicApi from "@/api/music";

export default {
  data() {
    return {
      musicList: [],
    }
  },
  methods: {
    handlerSelectMusicList() {
      musicApi.list().then(resp => {
        resp = resp.data
        if (resp.code === 200) {
          this.musicList = resp.data
        } else {
          this.$message.error('获取歌单失败')
        }
      }).catch(_ => {
        this.$message.error('获取歌单失败')
      })
    }
  },
  created() {
    this.handlerSelectMusicList()
  }
}
</script>

<style>
</style>
