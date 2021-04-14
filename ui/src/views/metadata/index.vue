<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--表字段数据-->
      <el-col :span="5" :xs="24">
        <div class="head-container">
          <el-input
            v-model="tableName"
            placeholder="请输入Table Name"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="tableTreeOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <el-col :span="19" :xs="24">
        <el-button
          type="primary"
          icon="el-icon-check"
          size="small"
          @click="saveFieldAlase"
        >{{ $t('common.saveData') }}</el-button>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="addtable"
        >{{ $t('common.addtable') }}</el-button>
        <el-button
         type="info"
         icon="el-icon-upload2"
         size="small"
         @click="handleImport"
        >{{ $t('common.importtable') }}</el-button>
        <el-table
          v-loading="loading"
          :data="fieldList"
          class="tb-edit"
          style="width: 100%"
          highlight-current-row
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="字段中文名称" sortable align="center" prop="field_cname" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.field_cname" size="mini" placeholder />
            </template>
          </el-table-column>
          <el-table-column label="字段名称" sortable align="center" prop="field_name" />
          <el-table-column label="字段类型" sortable align="center" prop="field_type"></el-table-column>
          <el-table-column
            label="是否为空"
            sortable
            align="center"
            prop="is_empty"
            :formatter="statusFormat"
          ></el-table-column>
          <el-table-column label="字段长度" sortable align="center" prop="field_lenght" width="120" />
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加表对话框 -->
    <el-dialog :title="title" :visible.sync="tableopen" width="450px" append-to-body>
      <el-form ref="tableform" :model="tableform" :rules="tablerules" label-width="120px">
        <el-row>
          <el-col>
            <el-form-item label="表中文名称" prop="table_alias">
              <el-input v-model="tableform.table_alias" placeholder="表中文名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="表名" prop="table_name">
              <el-input v-model="tableform.table_name" placeholder="表名" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="tablesubmitForm">确 定</el-button>
        <el-button @click="tablecancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 表字段导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?table_id=' + this.upload.table_id"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getToken } from "@/utils/auth";
import {
  tabletreeselect,
  getTableFieldListById,
  saveTableFields,
  addTable,
  importTemplate
} from "@/api/source";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "metadata",
  components: { Treeselect },
  data() {
    return {
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      // 总条数
      total: 0,
      // table tree选项
      tableTreeOptions: undefined,
      table_id: undefined,
      // 是否显示弹出层
      open: false,
      is_empty_options: [],
      field_type_options: [],
      // 部门名称
      tableName: undefined,
      // 日期范围
      dateRange: [],
      fieldList: [],
      dsid:undefined,
      // 表单参数
      form: {},
      importform: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 50,
        table_id: undefined,
        table_name: undefined,
        dsid: undefined,
        table_alias: undefined,
      },
      // 表字段导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        table_id: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/rest/bi/meta/importField",
      },
      tableform: {},
      tableopen: false,
      tablerules: {
        table_name: [
          { required: true, message: "表名不能为空", trigger: "blur" }
        ],
        table_alias: [
          { required: true, message: "表中文不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getDicts("field_is_empty").then((response) => {
      this.is_empty_options = response.data;
    });
    this.getDicts("rule_field_type").then((response) => {
      this.field_type_options = response.data;
    });
    this.getTreeselect();
  },
  methods: {
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "表字段导入";
      if(this.upload.table_id == undefined){
        this.msgError("请选择左侧数据源下的表,然后倒入字段");
        return;
      }
      // this.upload.table_id = this.queryParams.table_id;
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then((response) => {
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    //打开添加表对话框
    addtable() {
      if (this.dsid == undefined) {
        this.msgError("请选择左侧数据源,然后添加表");
        return;
      }
      this.tableform = {
        table_name: undefined,
        table_alias: undefined,
        source_id: this.dsid,
      };
      this.resetForm("tableform");
      this.tableopen = true;
      this.title = "添加表";
    },
    tablecancel() {
      this.tableopen = false;
      this.tableform.table_name = undefined;
      this.tableform.table_alias = undefined;
      this.tableform.source_id = undefined;
      this.resetForm("tableform");
    },
    /** 重命名表 */
    tablesubmitForm: function () {
      const datasource_id = this.dsid;
      this.$refs["tableform"].validate((valid) => {
        if (valid) {
          if ( datasource_id != undefined) {
            this.$confirm("你确认要添加表吗？", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
            }).then(({ value }) => {
              addTable(this.addDateRange(this.tableform))
                .then((response) => {
                  if (response.code === 200) {
                    this.msgSuccess("表添加成功");
                    this.tableopen = false;
                    this.getTreeselect();
                  }
                });
            });
          } else {
            this.msgError("请选择左侧选择数据源,然后添加表");
          }
        }
      });
    },
    //保存字段中文名称
    saveFieldAlase() {
      const tables = this.fieldList.map((item) => {
        return item;
      });
      saveTableFields(tables)
        .then((response) => {
          this.showTableDialog = false;
          this.msgSuccess("保存成功");
        })
        .catch(() => {});
    },
    getList() {
      this.loading = true;
      getTableFieldListById(this.queryParams).then((response) => {
        this.fieldList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.is_empty_options, row.is_empty);
    },
    fieldTypeFormat(row, column) {
      return this.selectDictLabel(this.field_type_options, row.field_type);
    },
    /** 查询Table下拉树结构 */
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
    // 节点单击事件
    handleNodeClick(data) {
      const tableName = data.tableName;
      if (data.dbtype != undefined) {
        this.dsid = data.id;
        this.queryParams.dsid = data.id;
      }
      if (tableName != undefined) {
        this.table_id = data.is;
        this.queryParams.table_id = data.id;
        this.upload.table_id = data.id;
        this.getList();
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
</style>
