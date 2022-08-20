<template>
  <div>
    <el-dialog
      title="提示"
      :visible.sync="searchDialogIsShow"
      width="30%"
    >
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "BlogSearch",
  data() {
    return {
      searchDialogIsShow: false,
    };
  },
  methods: {
  },
};
</script>

<style scoped>
</style>