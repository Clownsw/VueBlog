<template>
  <div>
    {{ id }}
  </div>
</template>

<script>
export default {
  name: "EditWord",
  data() {
    return {
      id: -1,   // -1 表示不正确, 0 表示新增博文, > 0表示修改博文
    }
  },
  created() {
    this.id = this.$route.params.id
  }
}
</script>

<style scoped>

</style>
