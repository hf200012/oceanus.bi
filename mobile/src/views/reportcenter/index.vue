<template>
  <div class="index-container">
    <div class="warpper">
      <h1 class="demo-home__title">
        <span>报表中心 > {{report_name}}</span>
      </h1>
    </div>
      <el-table :data="tables" v-loading="reportloading">
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
</template>
<script>
import { getReport, reportList } from '@/api/reportsql'

import { parseTime } from '@/utils'

export default {
  name: 'report',
  data() {
    return {
      currentReport: undefined,
      loading: true,
      tableloading: true,
      report_name: '',
      tables: null,
      tableData: null,
      reportloading: true,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        source_id: undefined,
        report_id: undefined
      }
    }
  },
  created() {
    this.queryParams.report_id = this.$route.query.id
    this.queryParams.source_id = this.$route.query.sourceid
    this.getCurrentReport()
    this.getList()
  },
  methods: {
    getCurrentReport() {
      getReport(this.queryParams.report_id).then(resp => {
        this.currentReport = resp.data
        this.report_name = resp.data.report_name
      })
    },
    //获取默认报表的table数据
    getList() {
      this.reportloading = true
      reportList(this.queryParams)
        .then(resp => {
          this.tables = resp.rows
          this.tableData = resp.headers
          this.total = resp.total
          this.reportloading = false
        })
        .catch(function (e) {
          console.log(e)
          this.reportloading = false
        })
    }
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
