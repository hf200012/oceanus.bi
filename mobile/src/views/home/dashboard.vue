<template>
  <div class="index-container">
    <div class="warpper">
      <h1 class="demo-home__title">
        <span>数据概览</span>
      </h1>
    </div>
    <el-card body-style="padding: 0px;" class="dashboard-list" shadow="never">
      <el-tabs v-model="activeName">
        <el-tab-pane label="公共概览" name="first" :key="'first'">
          <div class="head-container">
            <el-tree
              :data="publicTreeOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              ref="tree"
              default-expand-all
              @node-click="handleNodeClick"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="我的概览" name="second" :key="'second'">
          <div class="head-container">
            <el-tree
              :data="myTreeOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              ref="tree"
              default-expand-all
              @node-click="handleNodeClick"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import {
  publicDashboardTree,
  myDashboardTree,
} from '@/api/dashboard'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'dashboard',
  components: { Treeselect },
  data() {
    return {
      activeName: 'second',
      //公共概览Tree选项
      publicTreeOptions: undefined,
      //我的数据概览Tree
      myTreeOptions: undefined,
      currentDashboard: undefined,
      loading: false,
      isCollapse: false,
      project_id: undefined,
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },

  computed: {},

  mounted() {},
  created() {
    this.project_id = this.$route.query.project_id;
    this.publicDashboardList();
    this.myDashboardList();
  },
  methods: {
        //公共数据概览树
    publicDashboardList() {
      this.loading = true;
      publicDashboardTree(this.project_id).then((response) => {
        this.publicTreeOptions = response.data;
        this.loading = false;
      });
    },
    //我的数据概览树
    myDashboardList() {
      this.loading = true;
      myDashboardTree(this.project_id).then((response) => {
        this.myTreeOptions = response.data;
        this.loading = false;
      });
    },
    //概览树节点单击事件
    handleNodeClick(data) {
      const dashboardId = data.id;
      if (data.isChild == true) {
        this.$router.push(
          `/dashboard?id=${dashboardId}&project_id=${this.project_id}`
        );
      }
    },

  }
}
</script>
<style lang="scss" scoped>
.container {
  display: flex;
  min-height: calc(100vh - 62px);
  align-items: stretch;
  .dashboard-list {
    width: 250px;
    min-height: 100%;
    padding: 20px 10px;
    /deep/ .el-card__header {
      div {
        display: flex;
        justify-content: space-between;
        font-size: 14px;
        color: #606266;
        i {
          cursor: pointer;
        }
      }
      padding: 5px 0px;
    }
    .dashboard-list-item {
      display: flex;
      justify-content: space-between;
      line-height: 35px;
      font-size: 14px;
      cursor: pointer;
      color: #606266;
      i {
        margin-right: 10px;
        line-height: 35px;
      }
    }
    .high-light-dashboard {
      color: #205cd8;
    }
  }
  .dashboard-wrapper {
    width: 100%;
  }
}
</style>
