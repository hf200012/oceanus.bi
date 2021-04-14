<template>
  <!-- <div class="g-home relative"> -->
  <div id="map" ref="myEchart" :style="chartStyle"></div>
  <!-- </div> -->
</template>

<script>
import echarts from "echarts";
import "echarts/map/js/china"; // 引入中国地图数据
import axios from "axios";
require("echarts/theme/macarons");
import { labelFormatter } from "./chartUtils";

export default {
  props: {
    widgetMeta: {
      required: false,
      type: Object,
      default: () => {},
    },
    data: {
      required: true,
      default: () => {},
    },
    schema: {
      type: Array,
      required: true,
    },
    chartOpt: {
      type: Object,
      required: false,
    },
    chartStyle: {
      require: false,
      type: Object,
      default: () => {
        return {
          height: "500px",
        };
      },
    },
  },
  data() {
    return {
      provinces: [
        "shanghai",
        "hebei",
        "shanxi",
        "neimenggu",
        "liaoning",
        "jilin",
        "heilongjiang",
        "jiangsu",
        "zhejiang",
        "anhui",
        "fujian",
        "jiangxi",
        "shandong",
        "henan",
        "hubei",
        "hunan",
        "guangdong",
        "guangxi",
        "hainan",
        "sichuan",
        "guizhou",
        "yunnan",
        "xizang",
        "shanxi1",
        "gansu",
        "qinghai",
        "ningxia",
        "xinjiang",
        "beijing",
        "tianjin",
        "chongqing",
        "xianggang",
        "aomen",
        "taiwan",
      ],
      provincesText: [
        "上海",
        "河北",
        "山西",
        "内蒙古",
        "辽宁",
        "吉林",
        "黑龙江",
        "江苏",
        "浙江",
        "安徽",
        "福建",
        "江西",
        "山东",
        "河南",
        "湖北",
        "湖南",
        "广东",
        "广西",
        "海南",
        "四川",
        "贵州",
        "云南",
        "西藏",
        "陕西",
        "甘肃",
        "青海",
        "宁夏",
        "新疆",
        "北京",
        "天津",
        "重庆",
        "香港",
        "澳门",
        "台湾",
      ],
      map: {},
      province: "",
    };
  },
  watch: {
    data: {
      deep: true,
      handler: function (data) {
        this.data = data;
        this.$nextTick(() => {
          this.renderChart("china");
        });
        if (this.map) {
          this.map.resize();
        }
      },
    },
    schema: {
      deep: true,
      handler: function () {
        this.renderChart("china");
      },
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.renderChart("china");
    });
    this.$on("resized", this.handleResize);
    window.addEventListener("resize", this.handleResize);
  },
  beforeDestroy() {
    if (this.map) {
      this.map.dispose();
    }
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    handleResize() {
      if (this.map) {
        this.map.resize();
      }
    },
    //地图参数配置
    getMapOpt(place) {
      let option = {
        tooltip: {
          trigger: "item",
          formatter: function (a) {
            if(a["data"]){
              return `${a["name"]}</br>${a["data"].label}: (${a["value"]})`;
            }
          },
          position: "inside", //设置定位很重要不然提示框会偏移
        },
        dataRange: {
          show: false,
          min: 0,
          max: 1000,
          text: ["High", "Low"],
          realtime: true,
          calculable: true,
          color: ["orangered", "yellow", "lightskyblue"],
        },
        geo: {
          // 这个是重点配置区
          map: place ? place : "china", // 表示中国地图
          roam: true,
          label: {
            normal: {
              show: true, // 是否显示对应地名
              textStyle: {
                color: "rgba(0,0,0)",
              },
            },
          },
          itemStyle: {
            normal: {
              borderColor: "rgba(0, 0, 0, 0.2)",
            },
            emphasis: {
              areaColor: null,
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowBlur: 20,
              borderWidth: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
        series: [
          {
            type: "scatter",
            coordinateSystem: "geo", // 对应上方配置
          },
          {
            // name: "客户数量", // 浮动框的标题
            type: "map",
            geoIndex: 0,
            data: this.data,
          },
        ],
      };
      return option;
    },

    //用于在省级地图,点击空白处返回全国地图,这里要根据自己的业务数据来判断是否返回
    showChinaMap() {
      this.map.dispose();
      this.renderChart();
    },
    renderChart(place) {
      if (this.schema.filter((schema) => schema.asxAxis).length === 1) {
        let xAxisData = [];
        const seriesObj = {};
        let legend = [];
        // //维度
        const dimensions = this.schema.filter((schema) => schema.asxAxis);
        const xAxisName = dimensions[0].name;
        const label = this.schema.find((schema) => !schema.asxAxis).lable;
        const valueName = this.schema.find((schema) => !schema.asxAxis).name;
        this.data.map((item) => {
          item.name = item[xAxisName];
          item.value = item[valueName];
          item.label = label;
        });

        this.map = echarts.init(document.getElementById("map"), "macarons");
        window.onresize = this.map.resize;
        this.map.showLoading(); //加动画效果
        let option = this.getMapOpt(place);
        if (option && typeof option === "object") {
          this.map.setOption(option, true);
          setTimeout(() => {
            this.map.hideLoading();
          }, 500);
        }
      } else {
        this.map = echarts.init(document.getElementById("map"), "macarons");
        window.onresize = this.map.resize;
        this.map.showLoading(); //加动画效果
        let option = this.getMapOpt(place);
        if (option && typeof option === "object") {
          this.map.setOption(option, true);
          setTimeout(() => {
            this.map.hideLoading();
          }, 500);
        }
      }
    },
  },
};
</script>

<style lang="less">
.g-home {
  height: 100%;
  overflow: hidden;
  #map {
    width: 100%;
    height: 400px;
    background: #fff;
    border-radius: 4px;
    border: 1px solid #ebeef5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
}
</style>