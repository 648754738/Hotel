<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getRoomType } from '@/api/dashboard'

const animationDuration = 6000

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
      // 从后端获取客房类型分布数据
      try {
        const response = await getRoomType()
        if (response.code === 200) {
          const roomTypeData = response.data
          const roomTypeNames = Object.keys(roomTypeData)
          const roomTypeCounts = Object.values(roomTypeData)
          
          this.chart = echarts.init(this.$el, 'macarons')
          this.chart.setOption({
            title: {
              text: '客房类型分布',
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
            grid: {
              top: 50,
              left: '2%',
              right: '2%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: [{
              type: 'category',
              data: roomTypeNames,
              axisTick: {
                alignWithLabel: true
              },
              axisLabel: {
                color: '#999'
              }
            }],
            yAxis: [{
              type: 'value',
              axisTick: {
                show: false
              },
              axisLabel: {
                color: '#999'
              }
            }],
            series: [{
              name: '房间数量',
              type: 'bar',
              barWidth: '60%',
              data: roomTypeCounts,
              animationDuration,
              itemStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(
                    0, 0, 0, 1,
                    [
                      { offset: 0, color: '#83bff6' },
                      { offset: 0.5, color: '#188df0' },
                      { offset: 1, color: '#188df0' }
                    ]
                  ),
                  shadowBlur: 10,
                  shadowColor: 'rgba(0, 0, 0, 0.3)'
                }
              }
            }]
          })
        }
      } catch (error) {
        console.error('获取客房类型分布数据失败:', error)
        // 如果获取数据失败，使用默认数据
        this.renderDefaultChart()
      }
    },
    
    renderDefaultChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        title: {
          text: '客房类型分布',
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
        grid: {
          top: 50,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: ['标准间', '大床房', '豪华套房', '商务房', '家庭房'],
          axisTick: {
            alignWithLabel: true
          },
          axisLabel: {
            color: '#999'
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#999'
          }
        }],
        series: [{
          name: '房间数量',
          type: 'bar',
          barWidth: '60%',
          data: [79, 52, 200, 334, 390],
          animationDuration,
          itemStyle: {
            normal: {
              color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  { offset: 0, color: '#83bff6' },
                  { offset: 0.5, color: '#188df0' },
                  { offset: 1, color: '#188df0' }
                ]
              ),
              shadowBlur: 10,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
          }
        }]
      })
    }
  }
}
</script>