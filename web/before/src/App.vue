<template>
  <div class="warp">
    <router-view :key="$route.fullPath"/>
  </div>
</template>

<style>
.warp {
  max-width: 960px;
  margin: 0 auto;
}
</style>
