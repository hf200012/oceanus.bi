<template>
  <div>
    <el-card body-style="padding:0;" style="margin-bottom: 20px;" class="panel-header">
      <div slot="header" style="display: flex; justify-content:space-between;">
        <span>
          <span class="back-button" @click="$router.go(-1)">
            <i class="el-icon-back" />
            <span>{{ $t('common.back') }}</span>
          </span>
          <span v-if="this.$route.params.id !== 'create'">{{ $t('sqlconsole.createReport') }}</span>
          <span v-else>{{ $t('sqlconsole.editReport') }}</span>
          <el-button
            size="mini"
            type="text"
            style="margin-left:10px;"
            @click="viewAllReport"
          >{{ $t('sqlconsole.allReports') }}</el-button>
        </span>
        <span>
          <el-button
            size="mini"
            type="primary"
            style="float: right;margin:0 10px 0 0;"
            icon="el-icon-download"
            @click="handleDownload"
          />

          <el-button
            size="mini"
            type="primary"
            style="float: right;margin:0 10px 0 0;"
            icon="el-icon-save"
            @click="handleSave"
          >{{ $t('sqlconsole.saveReport') }}</el-button>
          <el-button
            v-if="this.$route.params.id !== 'create'"
            size="mini"
            type="primary"
            style="float: right;margin:0 10px 0 0;"
            @click="$router.replace(`/sqlconsole/create`)"
          >{{ $t('sqlconsole.createReport') }}</el-button>
        </span>
      </div>
    </el-card>
    <div class="app-container" style="display: flex;">
      <el-card id="dataPanel" style="width:300px;margin-right: 10px;text-align:center;">
        <!--表数据-->
        <div class="head-container">
          <el-select v-model="selectds.dsid" placeholder="请选择" @change="selectds(selectds.dsid)">
            <el-option
              v-for="item in dsOptions"
              :key="item.source_id"
              :label="item.base_alias"
              :value="item.source_id"
            ></el-option>
          </el-select>
        </div>
        <div class="head-container">
          <el-tree
            :data="tableTreeOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
          />
        </div>
      </el-card>
      <el-card style="width: 100%;" body-style="padding: 10px 20px;">
        <div class="form-wrapper">
          <el-form id="formPanel" size="mini" class="analysis-form">
            <el-row :gutter="12" class="mb8">
              <el-button type="success" icon="el-icon-edit" size="mini" @click="run_sql">运行</el-button>
              <el-button type="success" icon="el-icon-edit" size="mini" @click="formatSqlFun">代码格式化</el-button>
            </el-row>
            <el-row :gutter="12" class="mb8">
              <div class="sql-editor-wrapper">
                <codemirror v-model="sqlCode" :options="editorOptions" ref="codeEditor"></codemirror>
              </div>
            </el-row>
          </el-form>
          <el-form
            class="chart-form"
            ref="reportform"
            :model="reportform"
            size="mini"
            label-position="top"
          >
            <el-form-item :label="$t('sqlconsole.reportName')+':'">
              <el-input
                v-model="reportform.report_name"
                size="mini"
                :placeholder="$t('sqlconsole.namePlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="$t('sqlconsole.reportDesc')+':'">
              <el-input
                v-model="reportform.report_desc"
                size="mini"
                :placeholder="$t('sqlconsole.descPlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="$t('sqlconsole.isPublic')" prop="is_public">
              <el-checkbox v-model="reportform.is_public" :label="false">{{$t('sqlconsole.public')}}</el-checkbox>
            </el-form-item>
          </el-form>
        </div>
        <el-row :gutter="10" class="mb8">
          <el-col :span="12">
            <el-form ref="form" size="mini" :model="form" :rules="rules" label-width="140px">
              <el-form-item label="返回记录行数" prop="returnrows">
                <el-input v-model="form.returnrows" :value="100" style="width:100px" />
              </el-form-item>
              <el-form-item label="返回字段中文说明" prop="field_cnames">
                <el-input v-model="form.field_cnames"  style="width:400px" placeholder="字段的顺序必须和SQL语句一致,使用英文逗号隔开"/>
              </el-form-item>              
            </el-form>
          </el-col>
        </el-row>
        <el-row :gutter="10" class="mb8">
          <el-tabs v-model="message">
            <el-tab-pane :label="`执行结果`" name="result">
              <el-table :data="tables">
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
            </el-tab-pane>
           
            <el-tab-pane :label="`错误信息`" name="errorInfo">
              <div class="error">
              <span>{{errorMsgInfo || '没有错误信息'}}</span>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </el-card>
    </div>

    <!--查看我创建的所有报表-->
    <el-dialog :title="$t('sqlconsole.report')" :visible.sync="showMyReport">
      <div>
        <el-table :data="myReportList" v-loading="loading">
          <el-table-column label="报表名称" width="200" prop="report_name" />
          <el-table-column label="报表描述" prop="report_desc" />
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                icon="el-icon-edit"
                @click="switchReport(scope.row)"
              >{{ $t('common.edit') }}</el-button>
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="deleteReport(scope.row)"
              >{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="viewAllReport"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="showMyReport = false">{{ $t('common.close') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { codemirror, CodeMirror } from "vue-codemirror";
import sqlFormatter from "sql-formatter";
import {
  getReportds,
  getReportsqlResultList,
  getReportTreeTableselect,
  updateReport,
  createReport,
  getReport,
  deleteReportByid,
  reportALlList,
} from "@/api/reportsql";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
_ = require("lodash");
import "codemirror/lib/codemirror.css";
// 语言语法
import "codemirror/mode/sql/sql";
// sql语言提示
import "codemirror/addon/hint/sql-hint.js";

// active-line.js
import "codemirror/addon/selection/active-line.js";

// styleSelectedText
import "codemirror/addon/selection/mark-selection.js";
import "codemirror/addon/search/searchcursor.js";

// 自动提示
import "codemirror/addon/hint/show-hint.js";
import "codemirror/addon/hint/show-hint.css";

// highlightSelectionMatches
import "codemirror/addon/scroll/annotatescrollbar.js";
import "codemirror/addon/search/matchesonscrollbar.js";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/match-highlighter.js";
// keyMap
import "codemirror/mode/clike/clike.js";
import "codemirror/keymap/sublime.js";

// foldGutter
import "codemirror/addon/fold/foldgutter.css";
import "codemirror/addon/fold/brace-fold.js";
import "codemirror/addon/fold/comment-fold.js";
import "codemirror/addon/fold/foldcode.js";
import "codemirror/addon/fold/foldgutter.js";
import "codemirror/addon/fold/indent-fold.js";
import "codemirror/addon/fold/markdown-fold.js";
import "codemirror/addon/fold/xml-fold.js";

// theme css
import "codemirror/theme/xq-dark.css";
import "codemirror/theme/xq-light.css";

import { KEYWORDS, FUNCTIONS } from "./editor-box";

import { parseTime } from "@/utils";

// 匹配 （from || join）关键字
const TABLE_SUGGESET_POS_REG = /(^|\s+)(from|join)\s+(\S*)$/i;

// 匹配表名 别名
const TABLES_PATTENS = [
  /\sjoin\s+([\w._`]+)\s*(?:as)?\s*([\w_`]+)?/gi,
  /\sfrom\s+([\w._`]+)\s*(?:as)?\s*([\w_`]+)?/gi,
];

export default {
  name: "sqlEditor",
  components: {
    codemirror,
    Treeselect  },
  props: {},
  data() {
    return {
      sqlCode: "",
      editorOptions: {
        // codemirror options
        tabSize: 4,
        mode: "text/x-mysql",
        theme: "xq-light",
        fullScreen: false,
        lineNumbers: true,
        line: true,
        // 代码折叠
        foldGutter: true,
        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
        // 高级配置（需要引入对应的插件包）,codemirror advanced options(You need to manually introduce the corresponding codemirror function script code)
        // sublime、emacs、vim三种键位模式，支持你的不同操作习惯
        keyMap: "sublime",
        // 按键映射，比如Ctrl键映射autocomplete，autocomplete是hint代码提示事件
        extraKeys: {
          F9: () => {
            this.formatSqlFun();
          },
          "Ctrl-space": "autocomplete",
        },
        showHitObj: null, // 延迟显示联想列表
        currentWord: "", // 关键字记录
        updateTimer: null,
        sqlSuggestList: [],
        // 联想功能
        // hint: （cm) => {
        //   return this.Hint();
        // }
        // hint.js options
        hintOptions: {
          // 当匹配只有一项的时候是否自动补全
          completeSingle: false,
        },
        // 联想表名
        sqlSuggestTableNameLists: [],
        // 联想表
        sqlSuggestTableLists: [],
        // 联想列名
        sqlSuggestColumns: [],
        runCur: null,
        // 选中文本自动高亮，及高亮方式
        // styleSelectedText: true,
        // highlightSelectionMatches: { showToken: /\w/, annotateScrollbar: true },
        // more codemirror options...
        // 如果有hint方面的配置，也应该出现在这里
      },
      tableTreeOptions: undefined,
      tableName: undefined,
      dsOptions: undefined,
      message: "result",
      chartType: "table",
      defaultProps: {
        children: "children",
        label: "label",
      },
      queryParams: {
        pageNum: 1,
        pageSize: 50,
        source_id: undefined,
        sql: "",
        rows: 100,
        field_cnames:undefined
      },
      tables: null,
      tableData: null,
      report_name: undefined,
      report_desc: undefined,
      errorMsgInfo: "",
      total: 0,
      loading: true,
      //我的报表
      showMyReport: false,
      myReportList: [],
      form: {
        returnrows: 200,
        field_cnames:''
      },
      reportform: {
        report_desc: undefined,
        report_name: undefined,
        report_sql: undefined,
        status: 1,
        source_id: undefined,
        report_id: undefined,
        field_cnames:undefined
      },
      rules: {
        returnrows: [
          { required: true, message: "返回记录行数不能为空", trigger: "blur" },
          {
            pattern: /^(0|[1-9][0-9]*)$/,
            message: "只能输入数字",
            trigger: "blur",
          },
        ],
        field_cnames: [
          { required: true, message: "返回字段中文名称不能为空", trigger: "blur" }
        ],
      },
    };
  },
  computed: {
    codemirror() {
      return this.$refs.codeEditor.codemirror;
    },
  },
  mounted() {
    if (this.$refs.codeEditor) {
      // 绑定键盘事件
      this.$refs.codeEditor.codemirror.on("keyup", this.handleEditorKeyUp);
    }
  },
  destroyed() {},
  created() {
    this.getDsList();
  },
  watch: {
    "$route.params.id": {
      immediate: true,
      handler() {
        if (this.$route.params.id !== "create") {
          //加载报表相关信息进行编辑
          const reportId = this.$route.params.id;
          if (reportId) {
            getReport(reportId).then((resp) => {
              this.reportform = resp.data;
              this.sqlCode = resp.data.report_sql;
              this.queryParams.source_id = resp.data.source_id;
              this.form.field_cnames = resp.data.field_cnames
            });
          }
        } else {
          this.reportform.report_name = undefined;
          this.reportform.report_desc = undefined;
          this.reportform.report_id = undefined;
          this.sqlCode = "";
          this.reportform.is_public = false;
        }
      },
    },
  },
  methods: {
    viewAllReport() {
      this.showMyReport = true;
      this.loading = true;
      reportALlList(this.addDateRange(this.queryParams, this.dateRange)).then(
        (resp) => {
          this.myReportList = resp.rows;
          this.total = resp.total;
          this.loading = false;
        }
      );
    },
    switchReport(report) {
      this.$confirm(
        this.$t("chart.beforeLeaveConfirm"),
        this.$t("common.confirm")
      ).then(() => {
        this.$router.replace(`/sqlconsole/${report.report_id}`);
        this.showMyReport = false;
      });
    },
    deleteReport(report) {
      this.$confirm(
        this.$t("chart.deleteConfirm", report.report_name),
        this.$t("common.confirm")
      ).then(() => {
        reportId(report.report_id).then((resp) => {
          if(resp.code === 403){
            this.msgError(resp.msg);
            return;
          }
          if (this.$route.params.id === report.report_id) {
            this.$router.push("/sqlconsole/create");
            this.showMyReport = false;
          } else {
            this.viewAllReport();
          }
          this.$message({
            type: "success",
            message: this.$t("common.deleteSuccess"),
          });
        });
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
    //保存报表
    handleSave() {
      if (this.queryParams.source_id == undefined) {
        this.$message.error("请选择左侧的数据源");
        return;
      } else if (this.queryParams.sqlCode == "") {
        this.$message.error("请输入sql语句,然后点击运行");
        return;
      } else {
        const reportId = this.reportform.report_id;
        this.reportform.report_id = reportId;
        this.reportform.source_id = this.queryParams.source_id;
        this.reportform.report_sql = this.sqlCode;
        this.reportform.field_cnames = this.form.field_cnames;
        if(this.form.field_cnames =='' || this.form.field_cnames == undefined){
            this.msgError("返回字段的中文名称不能为空");
            return ;
        }
        if (reportId) {
          updateReport(this.reportform).then((resp) => {
             if(resp.code === 403){
                this.msgError(resp.msg);
                return;
              }
            this.msgSuccess(this.$t("common.saveSuccess"));
          });
        } else {
          createReport(this.reportform).then((resp) => {
            this.msgSuccess(this.$t("common.saveSuccess"));
            this.$router.replace(`/sqlconsole/${resp.data.id}`);
          });
        }
      }
    },
    getDsList() {
      getReportds().then((response) => {
        this.dsOptions = response.rows;
      });
    },
    selectds(dsid) {
      this.queryParams.source_id = dsid;
      getReportTreeTableselect(dsid).then((response) => {
        this.tableTreeOptions = response.data;
      });
    },
    //执行sql
    run_sql() {
      this.queryParams.sql = this.sqlCode;
      this.queryParams.rows = this.form.returnrows;
      this.queryParams.field_cnames = this.form.field_cnames;
      if (this.queryParams.source_id == undefined) {
        this.$message.error("请选择左侧的数据源");
        return;
      } else if (this.queryParams.sqlCode == "") {
        this.$message.error("请输入sql语句,然后点击运行");
        return;
      } else {
        this.loading = true;
        this.$refs["form"].validate((valid) => {
          if (valid) {
            getReportsqlResultList(this.queryParams)
              .then((response) => {
                if (response.code === 200) {
                  this.tables = response.data.tables;
                  this.tableData = response.data.tableData;
                  this.total = response.count;
                  this.message = "result";
                  store.state
                  this.loading = false;

                } else if (response.code === 400) {
                  this.loading = false;
                  this.msgError("SQL执行异常");
                  this.message = "errorInfo";
                  this.errorMsgInfo = response.msg;
                  return;
                }
              })
              .catch(function (e) {});
          }
        });
      }
    },
    getTreeselect() {
      tabletreeselect().then((response) => {
        this.tableTreeOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 联想表名
    autocompleteTables(editor) {
      let cur = editor.getCursor();
      let token = this.getToken(editor, cur);
      let word = token.string.toLowerCase();
      let list = [];

      list = this.getSuggestListByRequsest(this.sqlSuggestTableNameLists, word);
      this.hintSort(list, word);
      return {
        list: list,
        from: CodeMirror.Pos(cur.line, token.start),
        to: CodeMirror.Pos(cur.line, token.end),
      };
    },
    // 联想表名|列名处理
    getSuggestListByRequsest(list, word) {
      let listArr = [];
      let map = {};
      for (let i = 0, len = list.length; i < len; i++) {
        let item = list[i].toLowerCase();
        if (item.indexOf(word.trim()) !== -1) {
          // if (word === item) {
          //     continue;
          // }
          if (!map[list[i]]) {
            listArr.push(list[i]);
            map[list[i]] = true;
          }
        }
      }
      return listArr;
    },
    autocompleteColumns(editor) {
      let list = [];
      let cur = editor.getCursor();
      let token = this.getToken(editor, cur);
      let word = token.string.toLowerCase();
      //联想列名
      let sqlSuggestColumns = this.getSuggestListByRequsest(
        this.sqlSuggestColumns,
        word
      );
      list = list.concat(sqlSuggestColumns);
      this.hintSort(list, word);
      list = [...new Set(list)];

      return {
        list: list,
        from: CodeMirror.Pos(cur.line, token.start),
        to: CodeMirror.Pos(cur.line, token.end),
      };
    },
    // 本地关键字联想
    autoComplete(editor) {
      let COMBINED_KEYWORDS = KEYWORDS.concat(FUNCTIONS);
      let cur = editor.getCursor();
      let token = this.getToken(editor, cur);
      let word = token.string.toLowerCase();
      let list = [];
      // let sql = editor.getValue();

      //关键字,函数
      if (word.indexOf(".") < 0) {
        let a = this.getSuggestListByLocal(COMBINED_KEYWORDS, word);
        list = list.concat(a);
      }
      //变量
      if (word.indexOf(".") < 0) {
        if (this.systemVars && this.systemVars.length) {
          let arr = this.getSuggestListByLocal(this.systemVars, word);
          list = list.concat(arr);
        }
      }
      this.hintSort(list, word);
      // if (list.length < 10) { // 加入已经输入的单词联想关键字
      //     let words = sql.split(/[^\w_\u4e00-\u9fa5]/);
      //     let nwords = [];
      //     for (let i = 0; i < words.length; i++) {
      //         if ((!words[i]) || (word === words[i])) {
      //             continue;
      //         }
      //         nwords.push(words[i]);
      //     }
      //     list = list.concat(this.getSuggestListByLocal(nwords, word));
      // }
      list = [...new Set(list)];

      return {
        list: list,
        from: CodeMirror.Pos(cur.line, token.start),
        to: CodeMirror.Pos(cur.line, token.end),
      };
    },
    // 通过关键字 匹配获取suggest列表
    getSuggestListByLocal(list, word) {
      if (word.trim() === "") {
        return [];
      }

      let map = {};
      let listArr = [];
      for (let i = 0, localL = list.length; i < localL; i++) {
        let a = list[i].toLowerCase();
        if (a.indexOf(word.trim()) !== -1) {
          // if (isSelf && (word === a)) {
          //     continue;
          // }
          if (!map[list[i]]) {
            listArr.push(list[i]);
            map[list[i]] = true;
          }
        }
      }

      return listArr;
    },
    hintSort(arr, key) {
      for (let i = 0; i < arr.Length; i++) {
        for (let j = i; j < arr.Length; j++) {
          let a = arr[i].toLowerCase();
          let b = arr[j].toLowerCase();
          if (a.indexOf(key) > b.indexOf(key)) {
            let temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
          }
        }
      }
    },
    formatSqlFun() {
      this.sqlCode = sqlFormatter.format(this.sqlCode, {});
    },
    // 键盘事件监听
    handleEditorKeyUp(editor, e) {
      this.timer && clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        const value = editor.getValue().trim(); // 输入的所有sql
        let cur = editor.getCursor();
        let token = this.getToken(editor, cur);
        let word = token.string.toLowerCase().trim(); // 关键字

        if (
          e.key === "Enter" ||
          e.key === "Tab" ||
          e.key === "Escape" ||
          word === ""
        ) {
          this.currentWord = "";
          return true;
        }
        if (
          this.currentWord === word &&
          (e.key === "ArrowUp" || e.key === "ArrowDown")
        ) {
          return true;
        }
        this.currentWord = word;
        // 复位
        this.sqlSuggestColumns = [];
        this.runCur = cur;
        this.getSuggestData(editor, value, word);
      }, 100);
    },
    // 获取光标的位置
    getToken(e, cur) {
      let t = e.getTokenAt(cur);

      if (
        t.string &&
        (t.string.indexOf(".") >= 0 || t.string.indexOf("$") >= 0)
      ) {
        if (cur.ch > 0) {
          let before = e.getTokenAt({
            line: cur.line,
            ch: t.start,
          });
          t.string = (before.string + t.string).trim();
          t.start = before.start;
        }
      }
      return t;
    },
    // execSql() {
    //   let sql =
    //     (this.codemirror && this.codemirror.getSelection()) || this.sqlCode;
    //   let postData = { db: this.db, name: this.sqlName, sql };
    //   console.log(execSql);
    //   execSql(postData).then(data => {
    //     console.log(data);
    //   });
    //   console.log(sql);
    // },
    getSuggestData(editor, value, word) {
      let cur = editor.getCursor();
      // 记录当前查询关键词
      if (word.trim() === "") {
        return;
      }
      let tablesAlias = this.getTablesAndAlias(value);
      let leftValue = editor.getRange({ line: 0, ch: 0 }, cur);
      // if (TABLE_SUGGESET_POS_REG.test(leftValue) && this.dsName) {
      //   // from join 后请求表名
      //   this.getHintTables({ dsName: this.dsName, q: word });
      // } else if (tablesAlias.length && this.dsName) {
      //   // 是否有表 表是否有别名
      //   this.getHintColumns(tablesAlias, word);
      // } else {
      //   this.showSuggestMenu("local"); // 本地联想
      // }
      if (TABLE_SUGGESET_POS_REG.test(leftValue)) {
        // from join 后请求表名
        this.getHintTables();
      } else if (tablesAlias.length) {
        // 是否有表 表是否有别名
        this.getHintColumns(tablesAlias, word);
      } else {
        this.showSuggestMenu("local"); // 本地联想
      }
    },
    getHintTables() {
      this.sqlSuggestTableNameLists = [
        "aaa",
        "bbb",
        "ccc",
        "def",
        "table1",
        "table2",
        "table3",
        "table4",
        "ghy",
      ];
      this.showSuggestMenu("table");
    },
    getHintColumns(tablesAlias, word) {
      let tables = [];
      let table;
      let body = {};
      let leftWord;
      let idx = word.indexOf(".");
      if (idx !== -1) {
        leftWord = word.substr(0, idx);

        tablesAlias.forEach((item, i) => {
          if (item.alias === leftWord) {
            table = item.name;
          }
        });

        if (table) {
          body.tables = [table];
          this.getColumnList();
        }
      } else {
        tablesAlias.forEach((item, i) => {
          if (item.name !== "") {
            tables.push(item.name);
          }
        });

        tables = [...new Set(tables)];
        body.tables = tables;

        if (tables.length) {
          this.getColumnList({ body });
        }
      }
    },
    getColumnList() {
      this.sqlSuggestColumns = [
        "user",
        "age",
        "name",
        "id",
        "abc",
        "efg",
        "hij",
        "klm",
        "nuv",
        "wh",
      ];
      this.showSuggestMenu("column");
    },
    // 别名是否合法
    isLegalAlias(alias) {
      let keywords = {
        where: 1,
        on: 1,
        using: 1,
        join: 1,
        group: 1,
        order: 1,
        limit: 1,
      };
      return !keywords[alias.toLowerCase()];
    },
    // 拿到sql 输入的所有表和对应的别名
    getTablesAndAlias(sql) {
      let COMBINED_KEYWORDS = KEYWORDS.concat(FUNCTIONS);
      let names = [];
      for (let i = 0; i < TABLES_PATTENS.length; i++) {
        let reg = TABLES_PATTENS[i];
        for (;;) {
          let found = reg.exec(sql);
          if (!found) {
            break;
          }

          reg.lastIndex = found.index + 1;
          let t = found[1];

          let alias = "";
          if (found[2]) {
            alias = found[2].toLowerCase();
            if (!this.isLegalAlias(alias)) {
              alias = "";
            }
          }
          if (COMBINED_KEYWORDS.indexOf(t.toUpperCase()) !== -1) {
            continue;
          }

          names.push({
            name: t,
            alias: alias,
          });
        }
      }
      return names;
    },
    showSuggestMenu(type) {
      let autoFn;
      if (this.runCur) {
        let cur = this.codemirror.getCursor();
        if (!_.isEqual(this.runCur, cur)) {
          return;
        }
      }

      if (type == "local") {
        autoFn = this.autoComplete;
      } else if (type == "table") {
        autoFn = this.autocompleteTables;
      } else if (type == "column") {
        autoFn = this.autocompleteColumns;
      }
      if (this.$refs.codeEditor && this.$refs.codeEditor.codemirror) {
        CodeMirror.showHint(this.$refs.codeEditor.codemirror, autoFn);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.sql-editor-wrapper {
  height: 300px;
}
.vue-codemirror {
  height: 100%;
  overflow: hidden;
  /deep/ .CodeMirror {
    height: 100%;
    overflow: auto;
    .CodeMirror-lines {
      padding-top: 6px;
    }
  }
}

.back-button {
  display: inline-block;
  padding-right: 10px;
  margin-right: 10px;
  border-right: 1px solid #909090;
  cursor: pointer;
  span {
    padding: 5px;
    font-size: 14px;
  }
}
.analysis-form {
  width: 100%;
  padding-right: 20px;
  /deep/ .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }
  /deep/ .el-form-item--mini .el-form-item__label,
  .limit-input {
    color: #909399;
    font-size: 14px;
  }
}
.form-wrapper {
  display: flex;
}
.chart-form {
  width: 250px;
  /deep/ .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }
}

.draggable-wrapper {
  font-size: 14px;
  min-height: 30px;
  border-bottom: 1px solid #e4e7ed;
  .draggable-item {
    margin-right: 10px;
  }
  /deep/ .el-select--mini {
    margin: 0;
  }
}
.selected-field {
  /deep/ .el-input__inner {
    height: 20px;
    line-height: 20px;
    border: none;
    // background-color: rgba($color: #fff, $alpha: 0);
    padding: 0;
  }
  /deep/ .el-input__suffix {
    right: 0px;
    .el-input__suffix-inner {
      display: inline-block;
      height: 20px;
      line-height: 20px;
      .el-input__icon {
        font-size: 12px;
        line-height: 20px;
      }
    }
  }
}
.help-center-wrapper {
  cursor: pointer;
  position: fixed;
  bottom: 25px;
  right: 25px;
  .help-center {
    width: 45px;
    height: 45px;
    background: #fff;
    border-radius: 50%;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    line-height: 45px;
    font-size: 20px;
    color: #205cd8;
    text-align: center;
    /deep/ .el-dropdown {
      font-size: 20px;
      color: #205cd8;
    }
  }
}

.error{
  font-size: 14px;
}
</style>
