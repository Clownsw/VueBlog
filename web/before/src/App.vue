<template>
  <div class="warp">
    <router-view :key="$route.fullPath"/>

    <div class="page-footer" v-html="this.$store.getters.getPageFooter">
    </div>
  </div>
</template>

<style>
.warp {
  max-width: 960px;
  margin: 0 auto;
}
</style>
