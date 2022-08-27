<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://www.google.com/">Google</a>
      <a href="https://upyun.com/">又拍云</a>
      <a href="https://smilex.cn/" target="_blank">Smilex' Blog</a>
    </template>
    <template v-slot:copyright>
      <a href="https://github.com/Clownsw/VueBlog" target="_blank">VueBlog</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
