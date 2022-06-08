<template>
  <div class="warp">
    <router-view :key="$route.fullPath"/>

    <div class="page-footer" v-html="footer">
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      footer: ''
    }
  },
  created() {
    this.footer = this.$store.getters.getPageFooter
  },
}
</script>

<style>
.warp {
  max-width: 960px;
  margin: 0 auto;
}
</style>
