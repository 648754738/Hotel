<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>数据统计</span>
          </div>
          <panel-group @handleSetLineChartData="handleSetLineChartData" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>近7天预约数据</span>
          </div>
          <line-chart :chart-data="lineChartData" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>热门房间类型预订统计</span>
          </div>
          <word-cloud :chart-data="wordCloudData" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>客房状态分布</span>
          </div>
          <raddar-chart />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>预订状态统计</span>
          </div>
          <pie-chart />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>客房类型分布</span>
          </div>
          <bar-chart />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>近期预约记录</span>
          </div>
          <reservation-table />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import WordCloud from './dashboard/WordCloud'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import ReservationTable from './dashboard/ReservationTable'
import { getRecentWeekReservations, getRoomTypeReservations } from '@/api/dashboard'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    WordCloud,
    RaddarChart,
    PieChart,
    BarChart,
    ReservationTable
  },
  data() {
    return {
      lineChartData: lineChartData.newVisitis,
      wordCloudData: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    fetchData() {
      // 获取近7天预约数据
      this.getRecentWeekReservations()
      
      // 获取房间类型预订统计（词云数据）
      this.getRoomTypeReservations()
    },
    async getRecentWeekReservations() {
      try {
        const response = await getRecentWeekReservations()
        if (response.code === 200) {
          // 处理数据用于折线图显示
          this.lineChartData = {
            expectedData: response.data.reservationCounts,
            actualData: response.data.revenueAmounts,
            dates: response.data.dates
          }
        }
      } catch (error) {
        console.error('获取近7天预约数据失败:', error)
      }
    },
    async getRoomTypeReservations() {
      try {
        const response = await getRoomTypeReservations()
        if (response.code === 200) {
          this.wordCloudData = response.data
        }
      } catch (error) {
        console.error('获取房间类型预订统计失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 20px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .box-card {
    margin-bottom: 20px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>