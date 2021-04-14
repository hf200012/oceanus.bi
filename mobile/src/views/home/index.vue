<!-- home -->
<template>
  <div class="index-container">
    <div class="warpper">
      <h1 class="demo-home__title">
        <span>项目</span>
      </h1>
    </div>

    <div class="home-card">
      <div class="home-item" v-for="item  in list" :key="item.project_id">
        <a @click="viewDashboard(item.project_id)">
          <img class="home-img" src="../../assets/image/project.png" />
        </a>
        <div class="home-right">
          <span style="color: #999; fontSize: 16px">
            <a href="#" @click="viewDashboard(item.project_id)">{{item.project_name}}</a>
          </span>

          <span style="color: #999; fontSize: 14px">{{item.creator_username}}</span>
          <span style="color: #999; fontSize: 14px">{{item.private_status==1?'非公开':'公开'}}</span>
          <span style="color: #999; fontSize: 14px">{{item.created_at}}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import { getProjectList } from '@/api/project'
export default {
  data() {
    return {
      list: [],
      loading: true
    }
  },

  computed: {},
  created() {
    this.getProjects()
  },
  mounted() {},

  methods: {
    viewDashboard(id) {
      this.$router.push(`/dashboard_list?project_id=${id}`);
    },
    getProjects() {
      this.loading = true
      getProjectList()
        .then(response => {
          this.list = response.rows
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
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
    width: calc(100% - 10px);
    padding: 10px 0px 20px 20px;
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
      width: 30px;
      height: 30px;
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
</style>
