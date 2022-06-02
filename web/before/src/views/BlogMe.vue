<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <div class="m-me">
      <v-md-preview :text="me.content" />
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "BlogMe",
  data() {
    return {
      systemInfo: {},
      me: {},
    }
  },
  components: {
    Header
  },
  methods: {
    getMe() {
      this.$axios.get("me")
          .then(resp => {
            this.me = resp.data.data
          })
    },
  },
  created() {
    this.systemInfo = this.$store.getters.getSystemInfo
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
