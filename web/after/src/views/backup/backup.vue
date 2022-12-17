<template>
  <div class="app-container">
    <el-button type="danger" @click="buyBackUp" size="mini">立即备份</el-button>
  </div>
</template>

<script>
import backupApi from '../../api/backup'

export default {
  name: 'BackUpReal',
  methods: {
    buyBackUp() {
      backupApi.buyBackUp().then(resp => {
        this.$message.success(resp.message)
      })
    }
  }
}
</script>
