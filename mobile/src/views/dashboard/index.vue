<template>
  <div class="dashboard-container">
    <div class="warpper">
      <h1 class="demo-home__title">
        <span>数据概览 > {{dashboard.dashborad_name}}</span>
      </h1>
    </div>

    <el-card
      v-for="item in layout || []"
      :key="item.index"
      class="visualize-card"
      body-style="padding: 10px;"
    >
      <div slot="header" class="operation-bar">
        {{ getChartItem(item.i).chart_name }}
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
  </div>
</template>
<script>
import { GridLayout } from 'vue-grid-layout'
import { GridItem } from 'vue-grid-layout'
import visualizePanel from '../ChartPanel/components/visualizePanel'
import { exeSql, exec } from '@/api/exeSql'
import {
  chartByDashboard,
  updateDashboard,
  addChartToDB,
  unMapChartDb,
  getShareUserList,
  saveShare,
  getdDashboardById
} from '@/api/dashboard'
import { getTableFieldList } from '@/api/source'

import { chartList } from '@/api/chart'
import { buildSqlSentence, buildFilterSentence } from '@/utils/buildSentence'

function isLineOverLap(line1, line2) {
  const start1 = {
    x: line1[0][0],
    y: line1[0][1]
  }
  const end1 = {
    x: line1[1][0],
    y: line1[1][1]
  }
  const start2 = {
    x: line2[0][0],
    y: line2[0][1]
  }
  const end2 = {
    x: line2[1][0],
    y: line2[1][1]
  }
  if (start1.y === start2.y && end1.y === end2.y) {
    if (start1.x >= start2.x && start1.x <= end2.x) {
      return true
    } else {
      return false
    }
  } else {
    return false
  }
}
export default {
  components: { GridLayout, GridItem, visualizePanel },

  data() {
    return {
      dashboard: {
        required: false,
        type: Object,
        default: () => {
          return {}
        }
      },
      mode: {
        required: false,
        type: String,
        default: 'view'
      },
      charts: [],
      results: {},
      loading: false,
      layout: [],
      myChartList: [],
      showChartList: false,
      chartLoading: {},
      useroptions: [],
      shareopen: false,
      sharetitle: '',
      shareform: [],
      fieldList: [],
      loading: true,
      isshow: false,
      dashboardId: '',
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sourceId: undefined,
        tableName: undefined
      }
    }
  },
  created() {
    this.dashboardId = this.$route.query.id
    this.getDashboard()
  },
  methods: {
    getDashboard() {
      getdDashboardById(this.dashboardId).then(resp => {
        this.dashboard = resp.data
      })
      this.getList(this.dashboardId)
    },
    //数据概览分享人员选择操作
    // 取消按钮
    sharecancel() {
      this.shareopen = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.shareform = {
        userids: undefined,
        dashboard_id: undefined
      }
      this.resetForm('shareform')
    },
    handleShare() {
      //获取要分享的用户列表
      getShareUserList(this.queryParams).then(response => {
        this.useroptions = response.rows
      })
      this.reset()
      this.shareopen = true
      this.sharetitle = '数据概览分享'
      this.shareform.dashboard_id = this.dashboard.dashboard_id
    },
    //保存分享数据
    submitShareForm() {
      const ids = this.shareform.userids.join(',')
      this.shareform.userids = ids
      this.shareform.dashboard_id = this.dashboard.dashboard_id
      if (this.dashboard.dashboard_id != undefined && ids != '') {
        saveShare(this.shareform).then(response => {
          if (response.code === 200) {
            this.msgSuccess('分享成功')
            this.shareopen = false
          }
        })
      } else {
        this.msgError('分享失败')
        this.shareopen = false
        return
      }
    },
    getList(dashboardId) {
      this.charts = []
      this.layout = []
      this.loading = true
      chartByDashboard(dashboardId).then(resp => {
        this.loading = false
        this.charts = resp.data || []
        let filterStrs = []
        const layout = this.dashboard.content || []
        this.charts.forEach((chart, index) => {
          this.$set(this.results, chart.chart_id, [])
          this.$set(this.chartLoading, chart.chart_id, false)
          chart.content = chart.content
          chart.content.allSelected = []
          chart.content.allSelected = chart.content.allSelected
            .concat(chart.content.selectedCalcul)
            .concat(chart.content.selectedDimension)
          if (chart.content.filters) {
            filterStrs = chart.content.filters.map(buildFilterSentence)
          }
          const sqlSentence = buildSqlSentence({
            dataSrc: chart.content.dataSrc,
            selectedCalcul: chart.content.selectedCalcul,
            selectedDimension: chart.content.selectedDimension,
            orderByStrs: chart.content.orderByStrs,
            filterStr: filterStrs.join(' and '),
            limit: chart.content.limit
          })
          this.exeSql(sqlSentence, chart, index)
          if (!layout.find(layoutItem => layoutItem.id === chart.chart_id)) {
            this.generatePosition(chart, layout, index)
          }
        })
        this.layout = layout.filter(item => {
          return this.charts.find(chart => chart.chart_id === item.id)
        })
      })
    },
    getChartItem(id) {
      const chart = this.charts.find(chart => chart.chart_id === id)
      return chart
    },
    handleCaculPos(layout) {
      const bottomItems = []
      layout.forEach(i => {
        i.yOffSet = i.y + i.h
        i.xOffSet = i.x + i.w
        i.bottomLine = [
          [i.x, i.yOffSet],
          [i.xOffSet, i.yOffSet]
        ]
        i.topLine = [
          [i.x, i.y],
          [i.xOffSet, i.y]
        ]
      })
      layout.forEach(i => {
        const flag = layout.every(j => {
          return !isLineOverLap(i.bottomLine, j.topLine)
        })
        if (flag) {
          bottomItems.push(i)
        }
      })
      return bottomItems
    },
    generatePosition(chart, layout, index) {
      let posObj
      if (layout.length === 0) {
        posObj = {
          id: chart.chart_id,
          x: 0,
          y: 0,
          w: 12,
          h: 9,
          i: chart.chart_id
        }
      } else {
        const bottomItems = this.handleCaculPos(layout)
        const highestItem = bottomItems.reduce((result, item) => {
          if (result.bottomLine[0][1] > item.bottomLine[0][1]) {
            result = item
          }
          return result
        }, bottomItems[0])
        posObj = {
          id: chart.chart_id,
          x: highestItem.x,
          y: highestItem.yOffSet,
          w: highestItem.w,
          h: 9,
          i: chart.chart_id
        }
      }

      layout.push(posObj)
    },
    handleLinkChart() {
      this.loading = true
      chartList(this.addDateRange(this.queryParams, this.dateRange)).then(resp => {
        this.myChartList = resp.rows
        this.showChartList = true
        this.total = resp.total
        this.loading = false
      })
    },
    linkChart(chart) {
      const data = {
        chartId: chart.chart_id,
        dashboardId: this.dashboard.dashboard_id
      }
      if (this.dashboard.dashboard_id != undefined) {
        addChartToDB(data).then(resp => {
          this.showChartList = false
          this.getList()
          this.$message({
            type: 'success',
            message: this.$t('common.saveSuccess')
          })
        })
      } else {
        this.$message({
          type: 'error',
          message: '请先创建看板,然后添加图表'
        })
      }
    },
    isExisted(chart) {
      return this.charts.findIndex(item => item.chart_id === chart.chart_id) >= 0
    },
    handleCommand(chart) {
      const tableName = chart.content.dataSrc
      const sourceId = chart.source_id
    },
    //下钻
    downdrill(chart, it, index) {
      let filterStrs = []
      const layout = this.dashboard.content || []

      const dimension = {
        Type: it.field_type,
        asxAxis: true,
        isDimension: true,
        Column: it.field_name,
        cname: it.field_cname,
        name: it.field_name,
        lable: it.field_cname,
        id: it.field_id
      }
      if (chart.content.filters) {
        filterStrs = chart.content.filters.map(buildFilterSentence)
      }
      const sqlSentence = buildSqlSentence({
        dataSrc: chart.content.dataSrc,
        selectedCalcul: chart.content.selectedCalcul,
        selectedDimension: dimension,
        orderByStrs: chart.content.orderByStrs,
        filterStr: filterStrs.join(' and '),
        limit: chart.content.limit
      })
      chart.content.selectedDimension = dimension
      chart.content.allSelected = []
      chart.content.allSelected = chart.content.allSelected
        .concat(chart.content.selectedCalcul)
        .concat(chart.content.selectedDimension)
      chart.content.selectedDimension = dimension
      this.exeSql(sqlSentence, chart, index)
      if (!layout.find(layoutItem => layoutItem.id === chart.chart_id)) {
        this.generatePosition(chart, layout, index)
      }
    },
    handleEdit(chart) {
      this.$router.push(`/chartpanel/${chart.chart_id}`)
    },
    handleDelete(chart) {
      this.$confirm(this.$t('dashboard.removeChartConfirm'), this.$t('common.confirm'), {
        type: 'warning'
      }).then(() => {
        // deleteChart(index)
        const deleteChartIndex = this.layout.findIndex(item => item.id === chart.chart_id)
        const layout = this.layout
        layout.splice(deleteChartIndex, 1)
        this.dashboard.content.layout = layout
        const data = {
          chartId: chart.chart_id,
          dashboardId: this.dashboard.dashboard_id
        }
        Promise.all([updateDashboard(this.dashboard), unMapChartDb(data)]).then(resp => {
          if (resp.code === 403) {
            this.msgError(resp.msg)
            return
          }
          this.getList()
          this.$message({
            type: 'success',
            message: this.$t('common.deleteSuccess')
          })
        })
      })
    },
    handleLayoutChange() {
      if (this.mode === 'view') return
      this.dashboard.content = this.layout || {}
      this.dashboard.content.layout = this.layout
      updateDashboard(this.dashboard)
    },
    handleResize(i, newH, newW, newHPx, newWPx) {
      this.$refs[`chartInstance${i}`][0].$children[0].$emit('resized')
    },
    exeSql(sqlSentence, item, index) {
      this.$set(this.chartLoading, item.chart_id, true)
      if (!sqlSentence) {
        this.$message.warning(this.$t('dashboard.chartQueryException', item.chart_name))
        this.$set(this.chartLoading, item.chart_id, false)
        return
      }
      exec({ source_id: item.source_id, sql: sqlSentence })
        .then(resp => {
          this.$set(this.chartLoading, item.chart_id, false)
          this.$set(this.results, item.chart_id, resp.data)
        })
        .catch(() => {
          this.$set(this.chartLoading, item.chart_id, false)
        })
    }
  }
}
</script>
<style lang="scss" scoped>
.dashboard-container {
  .warpper {
    position: relative;
    padding: 3px 0;
    box-sizing: border-box;
    background-color: #35495e;
    .demo-home__title {
      line-height: 4px;
      text-align: center;
      font-size: 14px;
      font-weight: 40;
      color: #fff;
    }
  }
  .visualize-card {
    /deep/ .el-card__header {
      padding: 0;
      .operation-bar {
        font-size: 12px;
        align-items: center;
        display: flex;
        justify-content: space-between;
        height: 20px;
        padding: 0 10px;
        line-height: 20px;
        z-index: 9;
        i {
          margin-right: 10px;
          vertical-align: middle;
          color: #409eff;
          cursor: pointer;
        }
      }
    }
  }
}
</style>
