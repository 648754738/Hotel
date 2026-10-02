<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getRoomStatus } from '@/api/dashboard'

const animationDuration = 3000

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    async initChart() {
      // 从后端获取客房状态分布数据
      try {
        const response = await getRoomStatus()
        if (response.code === 200) {
          const roomStatusData = response.data
          const statusNames = Object.keys(roomStatusData)
          const statusValues = Object.values(roomStatusData)
          
          // 计算最大值用于雷达图的指示器
          const maxValue = Math.max(...statusValues, 100) * 1.2 // 增加20%的余量
          
          this.chart = echarts.init(this.$el, 'macarons')
          this.chart.setOption({
            title: {
              text: '客房状态分布',
              left: 'center',
              textStyle: {
                fontSize: 16,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
              }
            },
            radar: {
              radius: '66%',
              center: ['50%', '50%'],
              splitNumber: 8,
              splitArea: {
                areaStyle: {
                  color: 'rgba(127,95,132,.3)',
                  opacity: 1,
                  shadowBlur: 45,
                  shadowColor: 'rgba(0,0,0,.5)',
                  shadowOffsetX: 0,
                  shadowOffsetY: 15
                }
              },
              indicator: statusNames.map((name, index) => ({
                name: name,
                max: maxValue
              }))
            },
            legend: {
              left: 'center',
              bottom: '5',
              data: ['客房状态']
            },
            series: [{
              type: 'radar',
              symbolSize: 0,
              areaStyle: {
                normal: {
                  shadowBlur: 13,
                  shadowColor: 'rgba(0,0,0,.2)',
                  shadowOffsetX: 0,
                  shadowOffsetY: 10,
                  opacity: 1
                }
              },
              data: [
                {
                  value: statusValues,
                  name: '客房状态',
                  itemStyle: {
                    normal: {
                      color: '#3888fa'
                    }
                  }
                }
              ],
              animationDuration: animationDuration
            }]
          })
        } else {
          console.error('获取客房状态分布数据失败:', response.msg)
          this.renderDefaultChart()
        }
      } catch (error) {
        console.error('获取客房状态分布数据失败:', error)
        this.renderDefaultChart()
      }
    },
    
    renderDefaultChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        title: {
          text: '客房状态分布',
          left: 'center',
          textStyle: {
            fontSize: 16,
            fontWeight: 'normal'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        radar: {
          radius: '66%',
          center: ['50%', '50%'],
          splitNumber: 8,
          splitArea: {
            areaStyle: {
              color: 'rgba(127,95,132,.3)',
              opacity: 1,
              shadowBlur: 45,
              shadowColor: 'rgba(0,0,0,.5)',
              shadowOffsetX: 0,
              shadowOffsetY: 15
            }
          },
          indicator: [
            { name: '空闲', max: 1000 },
            { name: '已预定', max: 1000 },
            { name: '已入住', max: 1000 },
            { name: '维修中', max: 1000 }
          ]
        },
        legend: {
          left: 'center',
          bottom: '5',
          data: ['客房状态']
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
          areaStyle: {
            normal: {
              shadowBlur: 13,
              shadowColor: 'rgba(0,0,0,.2)',
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1
            }
          },
          data: [
            {
              value: [500, 700, 1200, 150],
              name: '客房状态',
              itemStyle: {
                normal: {
                  color: '#3888fa'
                }
              }
            }
          ],
          animationDuration: animationDuration
        }]
      })
    }
  }
}
</script>