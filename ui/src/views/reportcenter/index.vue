<template>
  <div v-loading="loading" class="container">
    <el-card body-style="padding: 0px;" class="dashboard-list" shadow="never">
      <div slot="header">
        <span>{{ $t('reportcenter.title') }}</span>

        <i class="el-icon-edit" size="small" @click="editReport" v-if="isShare==false" />
        <i class="el-icon-delete" size="small" @click="deleteReport" v-if="isShare==false" />
      </div>
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
    <div class="dashboard-wrapper">
    <div class="tool-bar">
        <div>
          <span class="db-name">{{ report_name }}</span>
          <span>{{ report_desc }}</span>
        </div>

        <div v-show="isShare == false">

        <el-button type="primary" size="mini" @click="handleShare">{{ $t('common.share') }}</el-button>
        <el-button type="primary" size="mini" @click="handleDownload">{{ $t('common.export') }}</el-button>
         </div>
    </div>
    <el-table :data="tables" v-loading="reportloading">
      <el-table-column type="selection" />
      <el-table-column label="序号" type="index" />
      <template v-for="(col) in tableData">
        <el-table-column
          sortable
          :show-overflow-tooltip="true"
          :prop="col.dataItem"
          :label="col.dataName"
          :key="col.dataItem"
        ></el-table-column>
      </template>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    </div>

     <!--数据报告分享对话框-->
    <el-dialog :title="sharetitle" :visible.sync="shareopen" append-to-body>
      <el-form ref="shareform" :model="shareform" label-width="120px">
        <el-form-item label="选择人员" prop="userids">
          <el-select multiple collapse-tags v-model="shareform.userids" placeholder="请选择要分享的人员">
            <el-option
              v-for="item in useroptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShareForm">确 定</el-button>
        <el-button @click="sharecancel">取 消</el-button>
      </div>
    </el-dialog>

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
  watch: {
    "currentReport.report_id": {
      immediate: true,
      handler(value) {
        if (!value) return;
        this.report_name = this.currentReport.report_name;
        this.report_desc = this.currentReport.report_desc;
        this.queryParams.source_id = this.currentReport.source_id;
        this.queryParams.report_id = this.currentReport.report_id;
        this.getList();
      },
    },
  },
  methods: {
    // 取消按钮
    sharecancel() {
      this.shareopen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.shareform = {
        userids: undefined,
        report_id: undefined,
      };
      this.resetForm("shareform");
    },
    handleShare() {
      //获取要分享的用户列表
      getShareUserList(this.queryParams).then((response) => {
        this.useroptions = response.rows;
      });
      this.reset();
      this.shareopen = true;
      this.sharetitle = "数据报告分享";
      this.shareform.dashboard_id = this.dashboard.dashboard_id;
    },
    //保存分享数据
    submitShareForm() {
      const ids = this.shareform.userids.join(",");
      this.shareform.userids = ids;
      this.shareform.report_id = this.currentReport.report_id;
      if (this.currentReport.report_id != undefined && ids != "") {
        saveShare(this.shareform).then((response) => {
          if (response.code === 200) {
            this.msgSuccess("分享成功");
            this.shareopen = false;
          }
        });
      } else {
        this.msgError("分享失败");
        this.shareopen = false;
        return;
      }
    },
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
        this.reportList = response.data[0].children;
        const report = this.reportList[0];
        if (report) {
          this.currentReport = report;
        } else {
          this.currentReport = this.reportList[0];
        }
        //这里是加载的tree，在数据概览对象中content布局内容是空，所以要根据id重新加载布局
        getReport(this.currentReport.id).then((resp) => {
          this.currentReport = resp.data;
        });
        if (this.currentReport) {
          this.report_name = this.currentReport.report_name;
          this.report_desc = this.currentReport.report_desc;
          //这地方因为是树，dashboard_id对应的是id，所以这里是去id
          this.$router.push(`/reportcenter?id=${this.currentReport.id}`);
        }
      });
    },
    //概览树节点单击事件
    handleNodeClick(data) {
      const reportId = data.id;
      if (data.isChild == true) {
        if (data.isShare) {
          this.isShare = true;
        } else {
          this.isShare = false;
        }
        getReport(data.id).then((resp) => {
          this.currentReport = resp.data;
        });
        this.report_name = this.currentReport.report_name;
        this.report_desc = this.currentReport.report_desc;
        this.$router.push(`/reportcenter?id=${this.currentReport.report_id}`);
      }
    },
    //获取默认报表的table数据
    getList() {
      this.reportloading = true;
      reportList(this.queryParams)
        .then((resp) => {
            this.tables = resp.rows;
            this.tableData = resp.headers;
            this.total = resp.total;
            this.reportloading = false;
        })
        .catch(function (e) {
          console.log(e)
          this.reportloading = false;
        });
    },
    handleDownload() {
      import("@/vendor/Export2Excel").then((excel) => {
        const tHeader = this.tableData.map((item) => item.dataItem);
        const header = this.tableData.map((item) => item.dataName);
        const filterVal = tHeader;
        const data = this.formatJson(filterVal, this.tables);
        excel.export_json_to_excel({
          header: header,
          data,
          filename: "DataExport" + parseTime(Date.now(), "{m}{d}{h}{i}{s}"),
          autoWidth: true,
        });
      });
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map((v) =>
        filterVal.map((j) => {
          const tempArr = j.split(".");
          if (tempArr.length <= 1) {
            return v[j];
          } else {
            return tempArr.reduce(
              (pre, cur) => (pre[cur] ? pre[cur] : "--"),
              v
            );
          }
        })
      );
    },
    //修改数据报表
    editReport(db) {
      const reportId = this.currentReport.id || this.currentReport.report_id
      this.$router.push(`/sqlconsole/${reportId}`);
    },
    handleCommand(cmd) {
      if (cmd.type === "edit") {
        this.editReport(cmd.target);
      } else {
        this.deleteReport(cmd.target);
      }
    },
    deleteReport() {
      this.$confirm(
        "确定要删除[" +
          this.currentReport.report_name +
          "]报表吗？将同时删除该该报表的分享",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        const reportId = this.currentReport.id || this.currentReport.report_id
        deleteReportByid(reportId).then(() => {   
          this.$message({
            type: "success",
            message: this.$t("common.deleteSuccess"),
          });
          this.myReportList();
          this.publicReportList();
        });
      });
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
