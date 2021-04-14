<template>
  <div class="app-container">
    <el-button
      type="primary"
      icon="el-icon-plus"
      size="small"
      class="add-source-btn"
      @click="handleAdd"
    >{{ $t('dataSource.addDataSource') }}</el-button>

    <el-table v-loading="loading" :data="datasourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="数据源名称" align="center" prop="base_alias" />
      <el-table-column label="数据库类型" align="center" prop="db_type" />
      <el-table-column label="用户名" align="center" prop="username" />
      <el-table-column label="创建时间" align="center" prop="created_at" />
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            :disabled="scope.row.is_sync == 1"
            @click="handleDelete(scope.row)"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-key"
            @click="syncMetadata(scope.row)"
          >同步元数据</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="tableManager(scope.row)"
          >{{ $t('dataSource.manageTables') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据源名称" prop="base_alias">
              <el-input v-model="form.base_alias" placeholder="请输入数据源名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="db_type">
              <el-select v-model="form.db_type" placeholder="请选择">
                <el-option
                  v-for="dict in db_type_options"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item label="数据库URL" prop="conn_url">
              <el-input v-model="form.conn_url" placeholder="数据库URL" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="驱动程序类名" prop="driver_class">
              <el-input v-model="form.driver_class" placeholder="请输入驱动程序类名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('dataSource.manageTables')" :visible.sync="showTableDialog"  width="600px">
      <el-table :data="tables">
        <el-table-column :label="$t('dataSource.tableName')" prop="table_alias">
          <template slot-scope="scope">
            <el-input v-model="scope.row.table_alias" size="mini" placeholder />
          </template>
        </el-table-column>
        <el-table-column :label="$t('dataSource.table')" prop="table_name" />
        <el-table-column :label="$t('dataSource.linked')">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-text="$t('common.open')"
              :inactive-text="$t('common.closed')"
            />
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="showTableDialog = false">{{ $t('common.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="saveTableConfig">{{ $t('common.confirm') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDatasourceList,
  addDatasource,
  updateDatasource,
  delDatasource,
  getDatasource,
  syncMetadata,
  saveTable,
  tablesByBase,
} from "@/api/source";
import { getToken } from "@/utils/auth";
import { parseTime } from "@/utils";

export default {
  name: "datasource",
  data() {
    return {
      dialogVisible: false,
      list: [],
      tables: [],
      showTableDialog: false,
      source_id: undefined,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      loading: true,
      // 总条数
      total: 0,
      // 用户表格数据
      datasourceList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      db_type_options: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单校验
      rules: {
        base_alias: [
          { required: true, message: "数据源名称不能为空", trigger: "blur" },
        ],
        conn_url: [
          { required: true, message: "连接URL不能为空", trigger: "blur" },
        ],
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
        ],
        db_type: [
          { required: true, message: "数据库类型不能为空", trigger: "blur" },
        ],
        driver_class: [
          { required: true, message: "驱动程序类名不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("db_type").then((response) => {
      this.db_type_options = response.data;
    });
  },
  methods: {
    //表管理
    tableManager(row) {
      this.tables = [];
      this.source_id = row.source_id;
      tablesByBase(row.source_id)
        .then((response) => {
          this.tables = response.rows;
        })
        .catch(() => {});
      this.showTableDialog = true;
    },
    //保存表
    saveTableConfig() {
      const tables = this.tables.map((item) => {
        item.status = item.status ? 1 : 0;
        return item;
      });
      saveTable({ source_id: this.source_id, tables })
        .then((response) => {
          this.showTableDialog = false;
          this.msgSuccess("保存成功");
        })
        .catch(() => {});
    },
    /** 查询数据源列表 */
    getList() {
      this.loading = true;
      getDatasourceList(this.addDateRange(this.queryParams, this.dateRange))
        .then((response) => {
          this.datasourceList = response.rows;
          this.total = response.total;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        source_id: undefined,
        base_alias: undefined,
        status: 1,
        is_private: 1,
        conn_url: undefined,
        username: undefined,
        password: undefined,
        db_type: undefined,
        driver_class: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /**同步数据源元数据 */
    syncMetadata(row) {
      const id = row.source_id;
      this.$confirm(
        '是否确认要重新同步数据源["' +
          row.base_alias +
          '"]的元数据?同步将删除原先的元数据',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return syncMetadata(id);
        })
        .then(() => {
          this.getList();
          this.loading = false;
          this.msgSuccess("同步元数据成功");
        })
        .catch(function () {});
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.source_id || this.ids;
      getDatasource(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据源";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.source_id != undefined) {
            updateDatasource(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDatasource(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const datasourceName = row.base_alias;
      const id = row.source_id;
      this.$confirm('是否确认删除数据源["' + datasourceName + '"]?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delDatasource(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
  },
};
</script>

<style lang="scss" scoped>
.page-container {
  padding: 24px;
}
.add-source-btn {
  margin-bottom: 24px;
}
.source-input {
  width: 250px;
}
</style>
