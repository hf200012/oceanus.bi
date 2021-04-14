<template>
  <div :style="chartStyle">
    <div style="vertical-align: middle;text-align: center;display: table-cell;">
      <span style="color: #rgb(228, 11, 11); fontSize: 35px;">{{numberValue}}</span>
    </div>
  </div>
</template>  

<script>
import echarts from "echarts";
require("echarts/theme/macarons");

export default {
  props: {
    data: {
      required: true,
      numberValue: undefined,
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
      chart: null,
      list: [],
      title: "",
      numberValue: undefined,
    };
  },
  watch: {
    data: {
      deep: true,
      numberValue: undefined,
      handler: function (data) {
        this.renderChart(data);
      },
    },
    schema: {
      deep: true,
      handler: function () {
        this.renderChart(this.data);
      },
    },
  },
  mounted() {
    this.renderChart(this.data);
    this.$on("resized", this.handleResize);
    window.addEventListener("resize", this.handleResize);
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    handleResize() {
      if (this.chart) {
        this.chart.resize();
      }
    },
    renderChart(data) {
      if (!data[0]) return;
      const result = data[0];
      const colName = this.schema[0].Column;
      this.title = this.schema[0].lable;
      const value = result[colName] + "";
      this.numberValue = value.replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g,'$&,');
    }
  },
};
</script>
<style lang="scss" scoped>
.home-card {
  width: 100%;
  overflow: hidden;
  align-self: center;
  padding: 10px 50px;
  display: flex;
  flex-wrap: wrap;
  .home-item {
    border-style: solid;
    cursor: hand;
    border-width: 1px;
    border-color: #e4e4e4;
    width: calc(25% - 30px);
    padding: 20px 0px 20px 20px;
    margin-right: 10px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    background: #fff;
    &:nth-child(4) {
      margin-right: 0;
    }
    .home-img {
      display: inline-block;
      width: 60px;
      height: 60px;
      margin: 0;
      padding: 0;
    }
    .home-right {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: flex-start;
      margin-left: 10px;
      .home-num {
        font-size: 40px;
        margin: 5px 0;
      }
    }
  }
}

div {
  display: flex;
  justify-content: center;
  align-items: Center;
}
</style>