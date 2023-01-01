<template>
  <div>
    <el-row>
      <el-col :xl="{ span: 13, offset: 6 }" :lg="{ span: 15, offset: 5 }">
        <aplayer :audio="musicList" :lrcType="3" fixed autoplay />
        <router-view :key="$route.fullPath"/>
        <div class="page-footer" v-html="this.$store.getters.getPageFooter">
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      musicList: [],
    }
  },
  created() {
    const musicListId = 3018135711
    this.$axios.get(`http://localhost:2222/api/vueblog/playlist/detail?id=${musicListId}`).then(resp => {
      this.musicList = resp.data
    })
  }
}
</script>

<style>
</style>
