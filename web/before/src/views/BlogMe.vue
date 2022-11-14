<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <div class="m-me">
	  <mavon-editor :subfield="false" :editable="false" :defaultOpen="'preview'" :toolbarsFlag="false" class="blog-body" v-model="me.content">
	  </mavon-editor>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "BlogMe",
  data() {
    return {
      systemInfo: this.$store.getters.getSystemInfo,
      me: {},
    }
  },
  components: {
    Header
  },
  methods: {
    getMe() {
      this.$axios.get("/other/me")
          .then(resp => {
            this.me = resp.data.data
          })
    },
  },
  created() {
    document.title = '我 - ' + this.systemInfo.title
    this.getMe()
  }
}
</script>

<style scoped>
.m-me {
  width: 100%;
  box-shadow: var(--el-box-shadow);
}
</style>
