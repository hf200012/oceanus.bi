<template>
  <div class="word-container"> 
    <div class="chart" id="chart_right1"></div>
  </div>
</template>

<script>
import echarts from "echarts";

import "echarts-wordcloud/dist/echarts-wordcloud";
import "echarts-wordcloud/dist/echarts-wordcloud.min";

export default {
  name: "wordCloud",
  data() {
    return {
      timer: null
    }
  },
  mounted() {
    this.getEchartRight1();
    this.timer = setInterval(() => {
      this.getEchartRight1();
    }, 5000)
  },
  methods: {
    getEchartRight1() {
      let myChart = echarts.init(document.getElementById('chart_right1'));
      let option = {
        // tooltip: {
        //   show: false
        // },
        series: [{
          type: 'wordCloud',
          gridSize: 1,
          sizeRange: [12, 50],
          rotationRange: [-90, 90],
          rotationStep: 45,
          shape: 'diamond',
          width: '90%',
          textPadding: 0,
          autoSize: {
            enable: true,
            minSize: 6
          },
          textStyle: {
            normal: {
              textBorderColor: 'rgba(255,255,255,0.3)',
              textBorderWidth: 1,
              color: () => {
                return 'rgb(' + [
                  Math.round(Math.random() * 160),
                  Math.round(Math.random() * 160),
                  Math.round(Math.random() * 160)
                ].join(',') + ')';
              }
            },
            emphasis: {
              fontSize: 20,
              // shadowBlur: 10,
              // shadowColor: 'rgba(255,255,255, .1)'
            }
          },
          data: [{
            name: '新零售',
            value: 810
          }, {
            name: '云计算',
            value: 520
          },{
            name: "人工智能",
            value: 928
          },{
            name: "大数据",
            value: 906
          },{
            name: "互联网",
            value: 825
          },{
            name: "智慧零售",
            value: 514
          },{
            name: "数据可视化",
            value: 486
          },{
            name: "数据只能",
            value: 53
          },{
            name: "机器学习",
            value: 927
          },{
            name: "零售行业",
            value: 1308
          },{
            name: "供应链",
            value: 693
          },{
            name: "智慧门店",
            value: 611
          },{
            name: "数据服务",
            value: 312
          },{
            name: "关联分析",
            value: 941
          },{
            name: "应用层",
            value: 358
          },{
            name: "网络层",
            value: 246
          },{
            name: "数据层",
            value: 207
          },{
            name: "基础层",
            value: 194
          },{
            name: "可视化",
            value: 211
          },{
            name: "数据挖掘",
            value: 1309
          }]
        }]
      }

      myChart.setOption(option, true);
      window.addEventListener('resize', () => {
        myChart.resize();
      });
    },
  },
  beforeDestroy() {
    clearInterval(this.timer);
  }
};
</script>

<style lang="scss" scoped>
.word-container {
  .chart {
    height: 3rem;
  }
}
</style>
