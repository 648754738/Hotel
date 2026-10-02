<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
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
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({ expectedData, actualData, dates } = {}) {
      // 如果有日期数据，使用日期作为x轴
      const xAxisData = dates && dates.length > 0 ? dates : ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      
      this.chart.setOption({
        xAxis: {
          data: xAxisData,
          boundaryGap: false,
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#999',
            fontSize: 12
          }
        },
        grid: {
          left: 20,
          right: 20,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10],
          formatter: function (params) {
            let result = params[0].name + '<br/>'
            params.forEach(function (item) {
              if (item.seriesName === '预订数') {
                result += item.marker + item.seriesName + ': ' + item.value + '个<br/>'
              } else {
                result += item.marker + item.seriesName + ': ¥' + item.value + '<br/>'
              }
            })
            return result
          }
        },
        yAxis: {
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#999'
          }
        },
        legend: {
          data: ['预订数', '收入(元)'],
          textStyle: {
            color: '#999'
          }
        },
        series: [{
          name: '预订数', 
          itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: expectedData || [],
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '收入(元)',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: actualData || [],
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>