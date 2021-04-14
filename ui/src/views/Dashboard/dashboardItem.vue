<template>
  <div class="dashboard-wrapper">
    <div class="tool-bar">
      <div>
        <span class="db-name">{{ dashboard.dashborad_name }}</span>
        <span>{{ dashboard.dashborad_desc }}</span>
      </div>
      <div v-show="mode === 'edit'">
        <el-button type="primary" size="mini" @click="handleShare">{{ $t('common.share') }}</el-button>
        <el-button
          type="primary"
          size="mini"
          @click="handleLinkChart"
        >{{ $t('dashboard.addChart') }}</el-button>
      </div>
    </div>
    <grid-layout
      v-if="charts.length!==0"
      v-loading="loading"
      :layout="layout || []"
      :col-num="24"
      :row-height="30"
      :is-draggable="mode === 'edit'"
      :is-resizable="mode === 'edit'"
      :is-mirrored="false"
      :vertical-compact="true"
      :pane-container="false"
      :margin="[10, 10]"
      :use-css-transforms="true"
      style="min-height: 500px;"
      @layout-updated="handleLayoutChange"
    >
      <grid-item
        v-for="(item,ind) in layout || []"
        :key="item.index"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        @resized="handleResize"
      >
        <el-card
          v-loading="chartLoading[item.i]"
          class="visualize-card"
          body-style="padding: 10px;"
        >
          <div slot="header" class="operation-bar">
            <div v-show="mode === 'edit'">
              <a
                href="#"
                @click="handleEdit(getChartItem(item.i))"
              >{{ getChartItem(item.i).chart_name }}</a>
            </div>
            <div v-show="mode === 'view'">
              <a href="#">{{ getChartItem(item.i).chart_name }}</a>
            </div>
            <div>
              <i
                v-show="mode === 'edit'"
                class="el-icon-edit"
                @click="handleEdit(getChartItem(item.i))"
              />

              <el-dropdown trigger="click">
                <span class="el-dropdown-link">
                  <i class="el-icon-caret-bottom" :title="$t('common.downdrill')" />
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    v-for="it in getChartItem(item.i).fields" :key="it.index"
                    @click.native="downdrill(getChartItem(item.i),it,ind)"
                  >{{it.field_cname}}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <i
                v-show="mode === 'edit'"
                class="el-icon-delete"
                @click="handleDelete(getChartItem(item.i))"
              />
              <el-tooltip
                :content="getChartItem(item.i).chart_desc"
                class="item"
                effect="dark"
                placement="top-end"
              >
                <i class="el-icon-info" style="color:#409eff;cursor:pointer;" />
              </el-tooltip>
            </div>
          </div>
          <visualize-panel
            :key="item.index"
            :ref="`chartInstance${item.i}`"
            :data="results[item.i]"
            :schema="getChartItem(item.i).content.allSelected"
            :chart-type.sync="getChartItem(item.i).content.chartType"
            :is-edit-mode="false"
            :chart-style="{height: `${item.h*30 + 10 * (item.h-1) - 60}px`}"
          />
        </el-card>
      </grid-item>
    </grid-layout>
    <div
      v-if="charts.length === 0 && mode === 'edit'"
      v-loading="loading"
      class="welcome-container"
    >
      <el-button type="primary" size="mini" @click="handleLinkChart">{{ $t('dashboard.addChart') }}</el-button>
      <div>
        <el-link type="info" :underline="false">
          <router-link to="/chartpanel/create">{{ $t('dashboard.emptyDashboardTip') }}</router-link>
        </el-link>
      </div>
    </div>
    <el-dialog :title="$t('chart.myChart')" :visible.sync="showChartList">
      <div>
        <el-button
          type="primary"
          size="mini"
          @click="$router.push('/chartpanel/create')"
        >{{ $t('chart.createNewChart') }}</el-button>
        <el-table :data="myChartList">
          <el-table-column :label="$t('common.name')" width="200" prop="chart_name" />
          <el-table-column :label="$t('common.desc')" prop="desc" />
          <el-table-column :label="$t('common.operation')" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                :disabled="isExisted(scope.row)"
                @click="linkChart(scope.row)"
              >{{ $t('common.add') }}</el-button>
              <el-button
                size="mini"
                type="warning"
                @click="$router.push(`/chartpanel/${scope.row.chart_id}`)"
              >{{ $t('common.edit') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="handleLinkChart"
        />
        <span slot="footer" class="dialog-footer">
          <el-button
            type="primary"
            size="small"
            @click="showChartList = false"
          >{{ $t('common.close') }}</el-button>
        </span>
      </div>
    </el-dialog>
    <!--数据概览分享对话框-->
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
import { GridLayout } from "vue-grid-layout";
import { GridItem } from "vue-grid-layout";
import visualizePanel from "../ChartPanel/components/visualizePanel";
import { exeSql, exec } from "@/api/exeSql";
import {
  chartByDashboard,
  updateDashboard,
  addChartToDB,
  unMapChartDb,
  getShareUserList,
  saveShare,
} from "@/api/dashboard";
import { getTableFieldList } from "@/api/source";

import { chartList } from "@/api/chart";
import { buildSqlSentence, buildFilterSentence } from "@/utils/buildSentence";

function isLineOverLap(line1, line2) {
  const start1 = {
    x: line1[0][0],
    y: line1[0][1],
  };
  const end1 = {
    x: line1[1][0],
    y: line1[1][1],
  };
  const start2 = {
    x: line2[0][0],
    y: line2[0][1],
  };
  const end2 = {
    x: line2[1][0],
    y: line2[1][1],
  };
  if (start1.y === start2.y && end1.y === end2.y) {
    if (start1.x >= start2.x && start1.x <= end2.x) {
      return true;
    } else {
      return false;
    }
  } else {
    return false;
  }
}
export default {
  components: { GridLayout, GridItem, visualizePanel },
  props: {
    dashboard: {
      required: false,
      type: Object,
      default: () => {
        return {};
      },
    },
    mode: {
      required: false,
      type: String,
      default: "view",
    },
  },
  data() {
    return {
      charts: [],
      results: {},
      loading: false,
      layout: [],
      myChartList: [],
      showChartList: false,
      chartLoading: {},
      useroptions: [],
      shareopen: false,
      sharetitle: "",
      shareform: [],
      fieldList: [],
      loading: true,
      isshow: false,
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sourceId: undefined,
        tableName: undefined,
      },
    };
  },
  watch: {
    "dashboard.dashboard_id": {
      immediate: true,
      handler(value) {
        if (!value) return;
        this.getList(value);
      },
    },
  },
  methods: {
    //数据概览分享人员选择操作
    // 取消按钮
    sharecancel() {
      this.shareopen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.shareform = {
        userids: undefined,
        dashboard_id: undefined,
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
      this.sharetitle = "数据概览分享";
      this.shareform.dashboard_id = this.dashboard.dashboard_id;
    },
    //保存分享数据
    submitShareForm() {
      const ids = this.shareform.userids.join(",");
      this.shareform.userids = ids;
      this.shareform.dashboard_id = this.dashboard.dashboard_id;
      if (this.dashboard.dashboard_id != undefined && ids != "") {
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
    getList() {
      this.charts = [];
      this.layout = [];
      this.loading = true;
      chartByDashboard(this.dashboard.dashboard_id).then((resp) => {
        this.loading = false;
        this.charts = resp.data || [];
        let filterStrs = [];
        const layout = this.dashboard.content || [];
        this.charts.forEach((chart, index) => {
          this.$set(this.results, chart.chart_id, []);
          this.$set(this.chartLoading, chart.chart_id, false);
          chart.content = chart.content;
          chart.content.allSelected = [];
          chart.content.allSelected = chart.content.allSelected
            .concat(chart.content.selectedCalcul)
            .concat(chart.content.selectedDimension);
          if (chart.content.filters) {
            filterStrs = chart.content.filters.map(buildFilterSentence);
          }
          const sqlSentence = buildSqlSentence({
            dataSrc: chart.content.dataSrc,
            selectedCalcul: chart.content.selectedCalcul,
            selectedDimension: chart.content.selectedDimension,
            orderByStrs: chart.content.orderByStrs,
            filterStr: filterStrs.join(" and "),
            limit: chart.content.limit,
          });
          this.exeSql(sqlSentence, chart, index);
          if (!layout.find((layoutItem) => layoutItem.id === chart.chart_id)) {
            this.generatePosition(chart, layout, index);
          }
        });
        this.layout = layout.filter((item) => {
          return this.charts.find((chart) => chart.chart_id === item.id);
        });
        this.handleLayoutChange();
      });
    },
    getChartItem(id) {
      return this.charts.find((chart) => chart.chart_id === id);
    },
    handleCaculPos(layout) {
      const bottomItems = [];
      layout.forEach((i) => {
        i.yOffSet = i.y + i.h;
        i.xOffSet = i.x + i.w;
        i.bottomLine = [
          [i.x, i.yOffSet],
          [i.xOffSet, i.yOffSet],
        ];
        i.topLine = [
          [i.x, i.y],
          [i.xOffSet, i.y],
        ];
      });
      layout.forEach((i) => {
        const flag = layout.every((j) => {
          return !isLineOverLap(i.bottomLine, j.topLine);
        });
        if (flag) {
          bottomItems.push(i);
        }
      });
      return bottomItems;
    },
    generatePosition(chart, layout, index) {
      let posObj;
      if (layout.length === 0) {
        posObj = {
          id: chart.chart_id,
          x: 0,
          y: 0,
          w: 12,
          h: 9,
          i: chart.chart_id,
        };
      } else {
        const bottomItems = this.handleCaculPos(layout);
        const highestItem = bottomItems.reduce((result, item) => {
          if (result.bottomLine[0][1] > item.bottomLine[0][1]) {
            result = item;
          }
          return result;
        }, bottomItems[0]);
        posObj = {
          id: chart.chart_id,
          x: highestItem.x,
          y: highestItem.yOffSet,
          w: highestItem.w,
          h: 9,
          i: chart.chart_id,
        };
      }

      layout.push(posObj);
    },
    handleLinkChart() {
      this.loading = true;
      chartList(this.addDateRange(this.queryParams, this.dateRange)).then(
        (resp) => {
          this.myChartList = resp.rows;
          this.showChartList = true;
          this.total = resp.total;
          this.loading = false;
        }
      );
    },
    linkChart(chart) {
      const data = {
        chartId: chart.chart_id,
        dashboardId: this.dashboard.dashboard_id,
      };
      if (this.dashboard.dashboard_id != undefined) {
        addChartToDB(data).then((resp) => {
          this.showChartList = false;
          this.getList();
          this.$message({
            type: "success",
            message: this.$t("common.saveSuccess"),
          });
        });
      } else {
        this.$message({
          type: "error",
          message: "请先创建看板,然后添加图表",
        });
      }
    },
    isExisted(chart) {
      return (
        this.charts.findIndex((item) => item.chart_id === chart.chart_id) >= 0
      );
    },
    handleCommand(chart) {
      const tableName = chart.content.dataSrc;
      const sourceId = chart.source_id;
    },
    //下钻
    downdrill(chart, it, index) {
      let filterStrs = [];
      const layout = this.dashboard.content || [];

      const dimension = {
        Type: it.field_type,
        asxAxis: true,
        isDimension: true,
        Column: it.field_name,
        cname: it.field_cname,
        name: it.field_name,
        lable: it.field_cname,
        id: it.field_id,
      };
      if (chart.content.filters) {
        filterStrs = chart.content.filters.map(buildFilterSentence);
      }
      const sqlSentence = buildSqlSentence({
        dataSrc: chart.content.dataSrc,
        selectedCalcul: chart.content.selectedCalcul,
        selectedDimension: dimension,
        orderByStrs: chart.content.orderByStrs,
        filterStr: filterStrs.join(" and "),
        limit: chart.content.limit,
      });
      chart.content.selectedDimension = dimension
      chart.content.allSelected = [];
      chart.content.allSelected = chart.content.allSelected
        .concat(chart.content.selectedCalcul)
        .concat(chart.content.selectedDimension);
      chart.content.selectedDimension = dimension;
      this.exeSql(sqlSentence, chart, index);
      if (!layout.find((layoutItem) => layoutItem.id === chart.chart_id)) {
        this.generatePosition(chart, layout, index);
      }
    },
    handleEdit(chart) {
      this.$router.push(`/chartpanel/${chart.chart_id}`);
    },
    handleDelete(chart) {
      this.$confirm(
        this.$t("dashboard.removeChartConfirm"),
        this.$t("common.confirm"),
        {
          type: "warning",
        }
      ).then(() => {
        // deleteChart(index)
        const deleteChartIndex = this.layout.findIndex(
          (item) => item.id === chart.chart_id
        );
        const layout = this.layout;
        layout.splice(deleteChartIndex, 1);
        this.dashboard.content.layout = layout;
        const data = {
          chartId: chart.chart_id,
          dashboardId: this.dashboard.dashboard_id,
        };
        Promise.all([updateDashboard(this.dashboard), unMapChartDb(data)]).then(
          (resp) => {
            if (resp.code === 403) {
              this.msgError(resp.msg);
              return;
            }
            this.getList();
            this.$message({
              type: "success",
              message: this.$t("common.deleteSuccess"),
            });
          }
        );
      });
    },
    handleLayoutChange() {
      if (this.mode === "view") return;
      this.dashboard.content = this.layout || {};
      this.dashboard.content.layout = this.layout;
      updateDashboard(this.dashboard);
    },
    handleResize(i, newH, newW, newHPx, newWPx) {
      this.$refs[`chartInstance${i}`][0].$children[0].$emit("resized");
    },
    exeSql(sqlSentence, item, index) {
      this.$set(this.chartLoading, item.chart_id, true);
      if (!sqlSentence) {
        this.$message.warning(
          this.$t("dashboard.chartQueryException", item.chart_name)
        );
        this.$set(this.chartLoading, item.chart_id, false);
        return;
      }
      exec({ source_id: item.source_id, sql: sqlSentence })
        .then((resp) => {
          this.$set(this.chartLoading, item.chart_id, false);
          this.$set(this.results, item.chart_id, resp.data);
        })
        .catch(() => {
          this.$set(this.chartLoading, item.chart_id, false);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
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
.visualize-card {
  /deep/ .el-card__header {
    padding: 0;
    .operation-bar {
      font-size: 14px;
      display: flex;
      justify-content: space-between;
      height: 35px;
      padding: 0 10px;
      line-height: 35px;
      z-index: 9;
      i {
        margin-right: 10px;
        color: #409eff;
        cursor: pointer;
      }
    }
  }
}
.welcome-container {
  text-align: center;
  height: 500px;
  color: #c0c4cc;
  /deep/ .el-button {
    margin-top: 200px;
    margin-bottom: 25px;
  }
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-icon-arrow-down {
  font-size: 12px;
}
.demonstration {
  display: block;
  color: #8492a6;
  font-size: 14px;
  margin-bottom: 20px;
}
</style>
