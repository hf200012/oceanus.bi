<!-- home -->
<template>
  <div class="index-container">
    <div class="warpper">
      <h1 class="demo-home__title">
        <span>报表中心</span>
      </h1>
    </div>
    <el-card body-style="padding: 0px;" class="dashboard-list" shadow="never">
      <el-tabs v-model="activeName">
        <el-tab-pane label="公共报表" name="first" :key="'first'">
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

        <el-tab-pane label="我的报表" name="second" :key="'second'">
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
import {
  myReportTree,
  getReport,
  reportList,
  publicReportTree,
  saveShare,
  deleteReportByid
} from "@/api/reportsql";
import {
  getShareUserList
} from "@/api/dashboard";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { parseTime } from "@/utils";


export default {
  name: "reportcenter",
  components: { Treeselect },
  data() {
    return {
      activeName: "second",
      reportList: [],
      //公共概览Tree选项
      publicTreeOptions: undefined,
      //我的数据概览Tree
      myTreeOptions: undefined,
      currentReport: undefined,
      report_name:undefined,
      report_desc:undefined,
      loading: true,
      isCollapse: false,
      tableloading:true,
      isShare: false,
      defaultProps: {
        children: "children",
        label: "label",
      },
      tables: null,
      tableData: null,
      shareopen: false,
      useroptions:[],
      sharetitle: "",
      shareform: [],
      reportloading:true,
      total:0,
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        source_id: undefined,
        report_id: undefined,
      },
    };
  },
  created() {
    this.publicReportList();
    this.myReportList();
  },

  methods: {
    //公共数据概览树
    publicReportList() {
      this.loading = true;
      publicReportTree().then((response) => {
        this.publicTreeOptions = response.data;
        this.loading = false;
      });
    },
    //我的数据概览树
    myReportList() {
      this.loading = true;
      myReportTree().then((response) => {
        this.myTreeOptions = response.data;
        this.loading = false;
      });
    },
    //概览树节点单击事件
    handleNodeClick(data) {
      const reportId = data.id;
      if (data.isChild == true) {
        this.$router.push(`/reportcenter?id=${reportId}&sourceid=${data.source_id}`);
      }
    },
  },
};
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
.tool-bar {
  display: flex;
  justify-content: space-between;
  border-top: none;
  height: 45px;
  line-height: 45px;
  color: #303133;
  padding: 0 10px;
  position: relative;
  .db-name {
    font-size: 1.2em;
    font-weight: 600;
    color: #909399;
    margin-left: 0;
  }
  span {
    color: #c0c4cc;
    font-size: 0.8em;
    margin-left: 10px;
  }
}
</style>
