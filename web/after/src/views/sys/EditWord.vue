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
      id: -1,
    }
  },
  created() {
    this.id = this.$route.params.id
  }
}
</script>

<style scoped>

</style>
