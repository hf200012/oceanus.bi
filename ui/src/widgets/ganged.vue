<template>
  <div class="test">
    <div id="myChart" :style="{width: '50%', height: '300px',float:'left'}"></div>
    <div id="myChart1" :style="{width: '50%', height: '300px',float:'left'}"></div>
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons')
import { labelFormatter } from './chartUtils'
export default {
  name: "FuncFormsBase",
  data() {
    return {
      star: "5星",
      data: [2, 26, 32, 87],
    };
  },
  mounted() {
    this.drawLine();
    this.drawLine1();
  },
  methods: {
    drawLine() {
      let that = this;
      let myChart = echarts.init(document.getElementById("myChart"));
      myChart.on("click", function (params) {
        that.star = params.name;
        //我们通过点击事件获取圆形图的params中的name，再根据name的值来进行判断，从而修改data的内容  
        switch (that.star) {
          case "0星":
            that.data = [];
            break;
          case "1星":
            that.data = [];
            break;
          case "2星":
            that.data = [56, 22, 1, 32];
            break;
          case "3星":
            that.data = [78, 52, 41, 21];
            break;
          case "4星":
            that.data = [9, 52, 75, 45];
            break;
          case "5星":
            that.data = [2, 26, 32, 87];
            break;
        }
        that.drawLine1();
      });
      myChart.setOption({
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        color: [
          "#E7BCF3",
          "#FF9F7F",
          "#FFE074",
          "#9FE6B8",
          "#5FA4EB",
          "#8378EA",
        ],
        legend: {
          bottom: 0,
          left: "center",
          data: ["0星", "1星", "2星", "3星", "4星", "5星"],
        },
        series: [
          {
            radius: ["20%", "70%"],
            selectedMode: "single",
            name: "客户星级",
            type: "pie",
            center: ["50%", "50%"],
            data: [
              { value: 335, name: "0星" },
              { value: 310, name: "1星" },
              { value: 234, name: "2星" },
              { value: 135, name: "3星" },
              { value: 1548, name: "4星" },
              { value: 1548, name: "5星" },
            ],
          },
        ],
      });
    },
    drawLine1() {
      let that = this;
      let myChart = echarts.init(document.getElementById("myChart1"));
      myChart.setOption({
        title: {
          text: "线索意向度" + that.star,
        },
        color: ["#5FA4EB"],
        xAxis: {
          type: "value",
        },
        yAxis: {
          type: "category",
          data: ["急需", "有兴趣", "考虑", "潜在"],
        },
        grid: {
          left: "3%",
          right: "6%",
          containLabel: true,
        },
        series: [
          {
            data: that.data,
            type: "bar",
            itemStyle: {
              normal: {
                label: {
                  show: true, //开启显示
                  position: "right", //在上方显示
                  textStyle: {
                    //数值样式
                    color: "black",
                    fontSize: 16,
                  },
                },
              },
            },
          },
        ],
      });
    },
  },
};
</script>