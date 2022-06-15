<template>
  <div class="warp">
    <router-view :key="$route.fullPath"/>
	
    <div class="page-footer" v-html="this.$store.getters.getPageFooter">
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
    this.getPageFooter()
  },
  methods: {
    getPageFooter() {
      this.$axios.get('footer').then(resp => {
        this.footer = resp.data.data.content
      })
    }
  }
}
</script>

<style>
.warp {
  max-width: 960px;
  margin: 0 auto;
}
</style>
