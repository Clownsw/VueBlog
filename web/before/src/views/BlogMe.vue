<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "BlogMe",
  data() {
    return {
      systemInfo: {},
    }
  },
  components: {
    Header
  },
  created() {
    this.systemInfo = this.$store.getters.getSystemInfo
    document.title = '我 - ' + this.systemInfo.title
  }
}
</script>

<style scoped>

</style>
