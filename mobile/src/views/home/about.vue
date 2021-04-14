<!-- home -->
<template>
  <div class="index-container">
    <div class="warpper">
      <h1 class="demo-home__title">
        <span>个人中心</span>
      </h1>
    </div>
  </div>
</template>

<script>
// 请求接口
import { getToken } from '@/utils/auth'
export default {
  data() {
    return {
      // wechat: `${this.$cdn}/wx/640.gif`
    }
  },
  mounted() {
    // this.initData()
  },
  methods: {}
}
</script>
<style lang="scss">
.about-container {
  /* 你的命名空间 */
  background: #fff;
  height: 100vh;
  box-sizing: border-box;
  .warpper {
    padding: 50px 12px 12px 12px;
    .list {
      display: flex;
      flex-direction: column;
      align-items: center;
      color: #666;
      font-size: 14px;
      .demo-home__title {
        margin: 0 0 6px;
        align-items: center;
        font-size: 16px;
        .demo-home__title img,
        .demo-home__title span {
          display: inline-block;
          vertical-align: middle;
        }
      }
      .item {
        font-size: 14px;
        line-height: 34px;
        a {
          text-decoration: underline;
        }
        .van-button {
          /* vant-ui 元素*/
          background: #ff5500;
        }
      }

      .logo {
        width: 120px;
        height: 120px;
        // background: url($cdn+'/weapp/logo.png') center / contain no-repeat;
      }
      .wechat {
        width: 200px;
        height: 200px;
        img {
          width: 100%;
          height: auto;
        }
      }
    }
  }
}
</style>
