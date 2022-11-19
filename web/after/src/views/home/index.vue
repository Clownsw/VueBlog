<template>
  <div class="app-container">
    <el-row type="flex" style="flex-wrap: wrap; flex-direction: row;" justify="space-between">
      <el-col :xl="{ span: 6 }" :lg="{ span: 6 }" :md="{ span: 7 }" :sm="{ span: 12 }">
        <el-card>
          <div class="left" style="background-color: #4C4A59;">
            <svg t="1655339752161" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="7230" width="200" height="200">
              <path
                d="M678.3 642.4c24.2-13 51.9-20.4 81.4-20.4h0.1c3 0 4.4-3.6 2.2-5.6-30.8-27.6-65.6-49.7-103.7-65.8-0.4-0.2-0.8-0.3-1.2-0.5C719.2 505 759.6 431.7 759.6 349c0-137-110.8-248-247.5-248S264.7 212 264.7 349c0 82.7 40.4 156 102.6 201.1-0.4 0.2-0.8 0.3-1.2 0.5-44.7 18.9-84.8 46-119.3 80.6-34.5 34.5-61.5 74.7-80.4 119.5C147.9 794.5 138 841 137 888.8c-0.1 4.5 3.5 8.2 8 8.2h59.9c4.3 0 7.9-3.5 8-7.8 2-77.2 32.9-149.5 87.6-204.3C357 628.2 432.2 597 512.2 597c56.7 0 111.1 15.7 158 45.1 2.5 1.5 5.5 1.7 8.1 0.3zM512.2 521c-45.8 0-88.9-17.9-121.4-50.4-32.4-32.5-50.3-75.7-50.3-121.6 0-45.9 17.9-89.1 50.3-121.6S466.3 177 512.2 177s88.9 17.9 121.4 50.4c32.4 32.5 50.3 75.7 50.3 121.6 0 45.9-17.9 89.1-50.3 121.6C601.1 503.1 558 521 512.2 521zM880 759h-84v-84c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v84h-84c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h84v84c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8v-84h84c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8z"
                p-id="7231" fill="#ffffff"></path>
            </svg>
          </div>

          <div class="right">
            <h1>{{ statisticsInfo.blog_total_num }}</h1>
            <p>博文条数</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xl="{ span: 6 }" :lg="{ span: 6 }" :md="{ span: 7 }" :sm="{ span: 12 }">

        <el-card>
          <div class="left" style="background-color: #1B7F7A;">
            <svg t="1655339841044" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="7543" width="200" height="200">
              <path
                d="M740 161c-61.8 0-112 50.2-112 112 0 50.1 33.1 92.6 78.5 106.9v95.9L320 602.4V318.1c44.2-15 76-56.9 76-106.1 0-61.8-50.2-112-112-112s-112 50.2-112 112c0 49.2 31.8 91 76 106.1V706c-44.2 15-76 56.9-76 106.1 0 61.8 50.2 112 112 112s112-50.2 112-112c0-49.2-31.8-91-76-106.1v-27.8l423.5-138.7c20.9-6.8 34.9-26.2 34.9-48.2V378.2c42.9-15.8 73.6-57 73.6-105.2 0-61.8-50.2-112-112-112z m-504 51c0-26.5 21.5-48 48-48s48 21.5 48 48-21.5 48-48 48-48-21.5-48-48z m96 600c0 26.5-21.5 48-48 48s-48-21.5-48-48 21.5-48 48-48 48 21.5 48 48z m408-491c-26.5 0-48-21.5-48-48s21.5-48 48-48 48 21.5 48 48-21.5 48-48 48z"
                p-id="7544" fill="#ffffff"></path>
            </svg>
          </div>

          <div class="right">
            <h1>{{ statisticsInfo.sort_total_num }}</h1>
            <p>分类个数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xl="{ span: 6 }" :lg="{ span: 6 }" :md="{ span: 7 }" :sm="{ span: 12 }">

        <el-card>
          <div class="left" style="background-color: #0897B4;">
            <svg t="1655339872444" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="7717" width="200" height="200">
              <path
                d="M483.2 790.3L861.4 412c1.7-1.7 2.5-4 2.3-6.3l-25.5-301.4c-0.7-7.8-6.8-13.9-14.6-14.6L522.2 64.3c-2.3-0.2-4.7 0.6-6.3 2.3L137.7 444.8c-3.1 3.1-3.1 8.2 0 11.3l334.2 334.2c3.1 3.2 8.2 3.2 11.3 0z m62.6-651.7l224.6 19 19 224.6L477.5 694 233.9 450.5l311.9-311.9z"
                p-id="7718" fill="#ffffff"></path>
              <path d="M605.958852 324.826232a48 48 0 1 0 67.881066-67.883435 48 48 0 1 0-67.881066 67.883435Z"
                    p-id="7719" fill="#ffffff"></path>
              <path
                d="M889.7 539.8l-39.6-39.5c-3.1-3.1-8.2-3.1-11.3 0l-362 361.3-237.6-237c-3.1-3.1-8.2-3.1-11.3 0l-39.6 39.5c-3.1 3.1-3.1 8.2 0 11.3l243.2 242.8 39.6 39.5c3.1 3.1 8.2 3.1 11.3 0l407.3-406.6c3.1-3.1 3.1-8.2 0-11.3z"
                p-id="7720" fill="#ffffff"></path>
            </svg>
          </div>

          <div class="right">
            <h1>{{ statisticsInfo.tag_total_num }}</h1>
            <p>标签个数</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xl="{ span: 6 }" :lg="{ span: 6 }" :md="{ span: 7 }" :sm="{ span: 12 }">
        <el-card>
          <div class="left" style="background-color: #4CABA6;">
            <svg t="1655339894158" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="7893" width="200" height="200">
              <path
                d="M574 665.4c-3.1-3.1-8.2-3.1-11.3 0L446.5 781.6c-53.8 53.8-144.6 59.5-204 0-59.5-59.5-53.8-150.2 0-204l116.2-116.2c3.1-3.1 3.1-8.2 0-11.3l-39.8-39.8c-3.1-3.1-8.2-3.1-11.3 0L191.4 526.5c-84.6 84.6-84.6 221.5 0 306s221.5 84.6 306 0l116.2-116.2c3.1-3.1 3.1-8.2 0-11.3L574 665.4zM832.6 191.4c-84.6-84.6-221.5-84.6-306 0L410.3 307.6c-3.1 3.1-3.1 8.2 0 11.3l39.7 39.7c3.1 3.1 8.2 3.1 11.3 0l116.2-116.2c53.8-53.8 144.6-59.5 204 0 59.5 59.5 53.8 150.2 0 204L665.3 562.6c-3.1 3.1-3.1 8.2 0 11.3l39.8 39.8c3.1 3.1 8.2 3.1 11.3 0l116.2-116.2c84.5-84.6 84.5-221.5 0-306.1z"
                p-id="7894" fill="#ffffff"></path>
              <path
                d="M610.1 372.3c-3.1-3.1-8.2-3.1-11.3 0L372.3 598.7c-3.1 3.1-3.1 8.2 0 11.3l39.6 39.6c3.1 3.1 8.2 3.1 11.3 0l226.4-226.4c3.1-3.1 3.1-8.2 0-11.3l-39.5-39.6z"
                p-id="7895" fill="#ffffff"></path>
            </svg>
          </div>

          <div class="right">
            <h1>{{ statisticsInfo.friend_total_num }}</h1>
            <p>友链个数</p>
          </div>
        </el-card>

      </el-col>
    </el-row>

    <el-row style="margin-top: 4rem">
      <el-col :span="24">
        <div id="view_count" style="height: 440px;"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echarts from 'echarts';
import statisticsApi from '../../api/statistics';

export default {
  name: 'HomeIndex',
  data() {
    return {
      statisticsInfo: {},
      myChart: null,
    }
  },
  created() {
    statisticsApi.statistics().then(resp => {
      this.statisticsInfo = resp.data
    })
  },
  mounted() {
    this.showEcharts()
    window.addEventListener('resize', () => {
      this.myChart.resize()
    })
  },
  methods: {
    showEcharts() {
      let r = async () => {
        let statisticsBlogResp = await statisticsApi.statisticsBlog()
        this.myChart = echarts.init(document.getElementById('view_count'))
        let option = {
          title: {
            text: "最近15访问量"
          },
          tooltip: {
            trigger: 'axis',
            formatter: function (params) {
              params = params[0];
              let date = new Date(params.name);
              return (
                date.getFullYear() + '/' +
                (date.getMonth() + 1) + '/' +
                date.getDate() + ' 访问量: ' +
                params.value
              );
            },
            axisPointer: {
              animation: false
            }
          },
          xAxis: {
            type: 'category',
            data: statisticsBlogResp.data.left
          },
          yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
              show: false
            }
          },
          series: [
            {
              type: 'line',
              showSymbol: false,
              data: statisticsBlogResp.data.right
            }
          ],
          grid: {
            top: '10%',
            left: '2%',
            right: '2%',
            bottom: '4%',
          }
        };
        this.myChart.setOption(option)
      }
      r().then(() => {
      })
    }
  },
}
</script>

<style scoped>
.el-card {
  height: 150px;
  margin-right: 10px;
}

.el-card ::v-deep .el-card__body {
  display: flex;
  padding: 0;
}

.el-card .left {
  position: relative;
  width: 140px;
  height: 150px;
  background: red;
}

.el-card .left svg {
  position: absolute;
  top: 11%;
  left: 11%;
  width: 80%;
  height: 80%;
}

.el-card .right {
  flex: 1;
  text-align: center;
}

.el-card .right h1 {
  color: #5e5e5e;
  font-size: 50px;
  margin: 26px 0 0 0;
}

.el-card .right p {
  color: #5e5e5e;
}
</style>
