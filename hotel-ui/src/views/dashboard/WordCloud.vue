<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
import 'echarts-wordcloud'
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
      default: '300px'
    },
    // 数据
    chartData: {
      type: Array,
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
      this.chart = echarts.init(this.$el)
      this.setOptions(this.chartData)
    },
    setOptions(data) {
      if (!this.chart) return
      
      // 生成随机颜色数组
      const colors = []
      for (let i = 0; i < data.length; i++) {
        const r = Math.floor(Math.random() * 255)
        const g = Math.floor(Math.random() * 255)
        const b = Math.floor(Math.random() * 255)
        colors.push(`rgb(${r},${g},${b})`)
      }
      
      this.chart.setOption({
        title: {
          text: '热门房间类型预订统计',
          left: 'center',
          textStyle: {
            fontSize: 16,
            fontWeight: 'normal'
          }
        },
        tooltip: {
          show: true,
          backgroundColor: 'rgba(0,0,0,0.7)',
          textStyle: {
            color: '#fff'
          }
        },
        series: [{
          type: 'wordCloud',
          sizeRange: [12, 60],
          rotationRange: [-90, 90],
          rotationStep: 45,
          gridSize: 8,
          shape: 'circle',
          width: '100%',
          height: '100%',
          textStyle: {
            fontFamily: 'sans-serif',
            fontWeight: 'bold',
            color: function() {
              return 'rgb(' + [
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160)
              ].join(',') + ')'
            }
          },
          emphasis: {
            textStyle: {
              color: '#52c41a',
              shadowBlur: 10,
              shadowColor: 'rgba(0, 0, 0, 0.3)',
              fontSize: '120%'
            }
          },
          data: data.map((item, index) => {
            return {
              name: item.name,
              value: item.value,
              textStyle: {
                color: colors[index % colors.length]
              }
            }
          })
        }]
      }, true)
    }
  }
}
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>