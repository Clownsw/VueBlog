<template>
  <div class="app-container">
    <div class="musicManageContainer">
      <el-card class="currentMusicManagerBox">
        <el-table
          :data="currentMusicTableData"
          stripe
          style="width: 100%">

          <el-table-column
            prop="id"
            label="id"
            width="100"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="name"
            label="名称"
            width="180"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="artist"
            label="作者"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="cover"
            label="封面"
            align="center"
          >
            <template slot-scope="slot">
              <img width="200px" height="200px" :src="slot.row.cover"/>
            </template>
          </el-table-column>

          <el-table-column
            prop="action"
            label="操作"
            width="100"
            align="center"
          >
            <template slot-scope="slot">
              <el-button type="danger" size="mini" @click="handlerDeleteMusic(slot.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          class="currentMusicTablePagination"
          :current-page="currentMusicPagination.currentPage"
          :page-size="currentMusicPagination.pageSize"
          :total="currentMusicPagination.total"
          layout="prev, pager, next"
          @current-change="handlerCurrentMusicPaginationChange"
        >
        </el-pagination>
      </el-card>

      <el-card class="searchMusicManagerBox">
        <el-input
          v-model="searchMusicKeyWord"
          size="mini"
          placeholder="输入关键字搜索"
          @keyup.enter.native="handlerSearchMusicKeyWordChange"
        />

        <el-table
          :data="searchMusicTableData"
          stripe
          style="width: 100%">

          <el-table-column
            prop="id"
            label="id"
            width="100"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="name"
            label="名称"
            width="180"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="author"
            label="作者"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="cover"
            label="封面"
            align="center"
          >
            <template slot-scope="slot">
              <img width="200px" height="200px" :src="slot.row.cover"/>
            </template>
          </el-table-column>

          <el-table-column
            prop="action"
            label="操作"
            width="100"
            align="center"
          >
            <template slot-scope="slot">
              <el-button type="primary" size="mini" @click="handlerAddMusic(slot.row)">添加</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import musicApi from '../../api/music'

export default {
  name: 'index',
  data() {
    return {
      currentMusicTableData: [],
      searchMusicKeyWord: null,
      searchMusicTableData: [],
      loading: null,
      currentMusicPagination: {
        currentPage: 1,
        pageSize: 30,
        total: 0
      }
    }
  },
  methods: {
    handlerSearchMusic() {
      this.loading = this.$loading({
        lock: true,
        text: '正在搜索中...',
        spinner: 'el-icon-loading',
        background: 'rgb(0 0 0 / 80%)'
      })

      musicApi.search(this.searchMusicKeyWord).then(resp => {
        if (resp.code === 200) {
          this.searchMusicTableData = resp.data
        } else {
          this.$message.error('搜索失败')
        }
        this.loading.close()
      }).catch(_ => {
        this.loading.close()
      })
    },
    handlerSelectMusicPage() {
      musicApi.list(this.currentMusicPagination.currentPage, this.currentMusicPagination.pageSize).then(resp => {
        if (resp.code === 200) {
          this.currentMusicPagination.total = resp.data.total
          this.currentMusicTableData = resp.data.records
        } else {
          this.$message.error('获取音乐列表失败')
        }
      })
    },
    handlerAddMusic(music) {
      this.loading = this.$loading({
        lock: true,
        text: '正在添加中...',
        spinner: 'el-icon-loading',
        background: 'rgb(0 0 0 / 80%)'
      })

      musicApi.add(music).then(resp => {
        if (resp.code === 200) {
          this.$message.success('添加成功')
        } else {
          this.$message.error('添加失败')
        }

        this.currentMusicPagination.currentPage = 1
        this.handlerSelectMusicPage()

        this.loading.close()
      }).catch(_ => {
        this.$message.error('添加失败')
        this.loading.close()
      })
    },
    handlerDeleteMusic(id) {
      this.loading = this.$loading({
        lock: true,
        text: '正在删除中...',
        spinner: 'el-icon-loading',
        background: 'rgb(0 0 0 / 80%)'
      })

      musicApi.delete(id).then(resp => {
        if (resp.code === 200) {
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }

        this.currentMusicPagination.currentPage = 1
        this.handlerSelectMusicPage()
        this.loading.close()
      }).catch(_ => {
        this.$message.error('删除失败')
        this.loading.close()
      })
    },
    handlerCurrentMusicPaginationChange(currentPage) {
      this.currentMusicPagination.currentPage = currentPage
      this.handlerSelectMusicPage()
    },
    handlerSearchMusicKeyWordChange() {
      this.handlerSearchMusic()
    },
  },
  created() {
    this.handlerSelectMusicPage()
  }
}
</script>

<style scoped>
.musicManageContainer {
  display: flex;
  justify-content: space-between;
}

.musicManageContainer .currentMusicManagerBox .currentMusicTablePagination {
  text-align: center;
  margin-top: 10px;
}

.musicManageContainer .currentMusicManagerBox, .searchMusicManagerBox {
  width: 49%;
  min-height: 90vh;
}
</style>
