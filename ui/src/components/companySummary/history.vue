

<template>
  <div class="history-container">
    <div class="tabs"> 
      <ul class="tab-tilte">
        <li v-for="(itemTit, index) in tabTitle" @mouseover="handleMouseOver(index)" @mouseout="handleMouseOut(index)" :class="{active: cur == index}">
          {{ itemTit }}
        </li>
      </ul>
      <div class="indicatorDots">
        <span :class="{active: cur == i}" v-for="(v, i) in tabTitle" :key="i"></span>
      </div>
      <div class="tab-content">
        <div v-for="(itemCon, index) in tabCon" v-show="cur == index">
          <div v-for="(v, i) in itemCon" :key="i" class="content">
            {{ v }}
          </div>
        </div>
      </div>
    </div>   
  </div>
</template>

<script>
import echarts from "echarts";

export default {
  name: "history",
  data() {
    return {
      timer: null,
      tabTitle: ['2015', '2016', '2017', '2018', '2019', '2020'],
      tabCon: [
        [
          '2015.10，公司成立，正式布局智慧零售、大数据方向',
          '2015.12，建设基于大数据的B2B供应链云平台'
        ],
        [
          '2016.07，基于大数据的B2B供应链平台正式上线'
        ],
        [
          '2017.03，支撑超过10万+的便利店运行'
        ],
        [
          '2018.05，开发基于深度学习的智能货架识别系统'
        ],
        [
          '2019.03，开发零售数据中台，业务中台及Devops开发技术中台'
        ],
        [
          '2020.03，开发大数据智能BI分析平台'
        ]
      ],
      cur: 0, //默认选中第一个tab
    }
  },
  mounted() {
    this.getTimer();
  },
  methods: {
    getTimer() {
      this.timer = setInterval(() => {
        this.cur++;
        if (this.cur == this.tabTitle.length) {
          this.cur = 0;
        }
      }, 2000)
    },
    handleMouseOver(index) {
      this.cur = index;
      clearInterval(this.timer);
    },
    handleMouseOut(index) {
      this.getTimer();
    }
  },
  beforeDestroy() {
    clearInterval(this.timer);
  }
};
</script>

<style lang="scss" scoped>
.history-container {
  margin: 0 0.125rem;
  .tabs {
    display: flex;
    .tab-tilte {
      li {
        color: #fff;
        font-size: 0.225rem;
        background-color: rgba(36, 196, 255, 0.5);
        text-align: center;
        cursor: pointer;
        width: 1.0rem;
        height: 0.35rem;
        line-height: 0.35rem;
        margin-bottom: 0.175rem;
        -webkit-border-radius: 5px;
        -ms-border-radius: 5px;
        -o-border-radius: 5px;
        -moz-border-radius: 5px;
        border-radius: 5px;
        &.active{
          background: linear-gradient(to right, #1b81bc, 20%, #24c4ff);
          color: #fff;
        }
      }
    }
    .indicatorDots {
      background-color: rgba(36, 196, 255, 0.5);
      width: 1px;
      height: 2.8rem;
      margin: 0.0625rem 0 0 0.375rem;
      span {
        display: block;
        height: 0.525rem;
        margin-left: -0.0875rem;
        &:after {
          content: '';
          display: block;
          width: 0.125rem;
          height: 0.125rem;
          background: #ddd;
          -webkit-border-radius: 50%;
          -ms-border-radius: 50%;
          -o-border-radius: 50%;
          -moz-border-radius: 50%;
          border-radius: 50%;
          border: 2px solid #1b81bc;
        }
        &.active {
          &:after {
            background: #24c4ff;
            -webkit-box-shadow: 0 0 0.125rem #1b81bc;
            box-shadow: 0 0 0.125rem #1b81bc;
          }
        }
      }
    }
    .tab-content {
      display: flex;
      align-items: center;
      font-size: 0.225rem;
      div {
        margin-left: 0.1875rem;
        color: #fff;
        .content {
          line-height: 0.275rem;
          padding: 0.125rem 0;
        }
      }
    }
  }
}
</style>
