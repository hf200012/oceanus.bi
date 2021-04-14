<template>
  <div class="page-container">
    <el-card body-style="padding:0;" style="margin-bottom: 20px;" class="panel-header">
      <div slot="header" style="display: flex; justify-content:space-between;">
        <span>
          <el-button
            size="mini"
            type="primary"
            style="float: right;margin:0 10px 0 0;"
            @click="handleAdd"
          >{{ $t('project.createproject') }}</el-button>
        </span>
      </div>
    </el-card>

    <!-- 网格 -->
    <div class="home-card">
      <div class="home-item" v-for="item  in projectlist" :key="item.project_id">
        <a @click="viewDashboard(item.project_id)">
          <img class="home-img" src="../../assets/image/project.png" />
        </a>
        <div class="home-right">
          <span style="color: #999; fontSize: 16px">
             <a  href="#" @click="viewDashboard(item.project_id)">{{item.project_name}}</a>
            <el-dropdown szie="mini" trigger="click" @command="handleCommand">
              <span class="el-dropdown-link">
                <i class="el-icon-more" />
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :command="{
                    type: 'edit',
                    target: item
                  }"
                >{{ $t('common.edit') }}</el-dropdown-item>
                <el-dropdown-item
                  :command="{
                    type: 'delete',
                    target: item
                  }"
                >{{ $t('common.delete') }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </span>

          <span style="color: #999; fontSize: 14px">{{item.creator_username}}</span>
          <span style="color: #999; fontSize: 14px">{{item.private_status==1?'非公开':'公开'}}</span>
          <span style="color: #999; fontSize: 14px">{{item.created_at}}</span>
        </div>
      </div>
    </div>
    <!-- 添加项目对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="project_name">
              <el-input v-model="form.project_name" placeholder="请输入项目" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item label="项目描述" prop="project_desc">
              <el-input v-model="form.project_desc" type="textarea" :rows="3" placeholder="项目介绍" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否公开" prop="is_public">
              <el-checkbox v-model="form.is_public" key="0">公开</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getProjectList,
  addProject,
  editProject,
  deleteProject,
  getProject,
} from "@/api/project";
import { getToken } from "@/utils/auth";
import { parseTime } from "@/utils";

export default {
  name: "project",
  data() {
    return {
      projectlist: [],
      form: [],
      loading: true,
      // 弹出层标题
      title: "",
      count: 0,
      // 是否显示弹出层
      open: false,
      // 表单校验
      rules: {
        project_name: [
          { required: true, message: "项目名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getProjects();
  },
  methods: {
    handleCommand(cmd) {
      if (cmd.type === "edit") {
        this.handleEdit(cmd.target);
      } else {
        this.deleteProject(cmd.target);
      }
    },
    deleteProject(item) {
      this.$confirm(
        "确定要删除[" + item.project_name+ "]概览吗？将同时删除该项目下的所有概览及概览相关数据",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        deleteProject(item.project_id).then((resp) => {
          if(resp.code === 403){
            this.msgError(resp.msg);
            return;
          }
          this.getProjects();
          this.$message({
            type: "success",
            message: this.$t("common.deleteSuccess"),
          });
        });
      });
    },
    viewDashboard(id) {
      this.$router.push(`/dashboard?project_id=${id}`);
    },
    //添加项目提交按钮
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.project_id != undefined) {
            editProject(this.form).then((resp) => {
              if (resp.code === 200) {
                this.msgSuccess("项目修改成功");
                this.open = false;
                this.getProjects();
              } else if(resp.code === 403){
                this.msgError(resp.msg);
                return;
              }
            });
          } else {
            addProject(this.form).then((resp) => {
              if (resp.code === 200) {
                this.msgSuccess("项目添加成功");
                this.open = false;
                this.getProjects();
              }
            });
          }
        }
      });
    },
    handleEdit(item) {
      getProject(item.project_id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑项目";
      });
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加项目";
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        project_name: undefined,
        project_id: undefined,
        private_status: 1,
        is_public: false,
        project_desc: undefined,
      };
      this.resetForm("form");
    },
    /** 查询数据源列表 */
    getProjects() {
      this.loading = true;
      getProjectList()
        .then((response) => {
          this.projectlist = response.rows;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
  },
};
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
    width: calc(25% - 30px);
    padding: 20px 0px 20px 20px;
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
      width: 60px;
      height: 60px;
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
