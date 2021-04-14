<template>
  <div v-loading="loading" class="container">
    <el-card body-style="padding: 0px;" class="dashboard-list" shadow="never">
      <div slot="header">
        <span>{{ $t('common.dashboard') }}</span>

        <i class="el-icon-plus" size="small" @click="addDashboard" />
        <i class="el-icon-edit" size="small" @click="editDashboard"  v-if="isShare==false"/>
        <i class="el-icon-delete" size="small" @click="deleteDashboard"   v-if="isShare==false"/>
      </div>
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
    <div v-if="isShare==false" class="dashboard-wrapper">
      <dashboardItem :dashboard="currentDashboard" mode="edit" />
    </div>
    <div v-else class="dashboard-wrapper">
      <dashboardItem :dashboard="currentDashboard" mode="view" />
    </div>
    <!--添加或编辑数据概览对话框-->
    <el-dialog
      :title="$t('dashboard.addOrEditDashboard')"
      width="750px"
      :visible.sync="editDialogVisible"
    >
      <el-form ref="dbObj" :model="dbObj" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dashboard.dashboardName')" prop="dashborad_name">
              <el-input
                v-model="dbObj.dashborad_name"
                size="small"
                style="width: 450px;"
                :placeholder="$t('dashboard.dashboardNamePlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item :label="$t('dashboard.dashboardDesc')" prop="dashborad_desc">
              <el-input
                v-model="dbObj.dashborad_desc"
                type="textarea"
                :rows="5"
                size="small"
                style="width: 450px;"
                :placeholder="$t('dashboard.dashboardDescPlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dashboard.isPublic')" prop="is_public">
              <el-checkbox v-model="dbObj.is_public" :label="false">{{$t('dashboard.public')}}</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="small" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>

        <el-button
          type="primary"
          size="small"
          @click="editDialogVisible = false"
        >{{ $t('common.cancel') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import draggable from "vuedraggable";
import dashboardItem from "./dashboardItem";
import {
  addDashboard,
  updateDashboard,
  dashboardList,
  deleteDashboard,
  dbOrder,
  publicDashboardTree,
  myDashboardTree,
  getdDashboardById,
} from "@/api/dashboard";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "dashboard",
  components: { dashboardItem, draggable, Treeselect },
  data() {
    return {
      activeName: "second",
      dashboardList: [],
      //公共概览Tree选项
      publicTreeOptions: undefined,
      //我的数据概览Tree
      myTreeOptions: undefined,
      currentDashboard: undefined,
      editDialogVisible: false,
      dbObj: {},
      loading: false,
      isCollapse: false,
      project_id: undefined,
      isShare:false,
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 表单校验
      rules: {
        dashborad_name: [
          { required: true, message: "概览名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.project_id = this.$route.query.project_id;
    this.publicDashboardList();
    this.myDashboardList();
    this.getList();
  },
  methods: {
    //公共数据概览树
    publicDashboardList() {
      this.loading = true;
      // if(this.project_id == undefined){
      //   this.loading = false;
      //   return ;
      // }
      publicDashboardTree(this.project_id).then((response) => {
        this.publicTreeOptions = response.data;
        this.loading = false;
      });
    },
    //我的数据概览树
    myDashboardList() {
      this.loading = true;
      // if(this.project_id == undefined){
      //   this.loading = false;
      //   return ;
      // }
      myDashboardTree(this.project_id).then((response) => {
        this.myTreeOptions = response.data;
        this.loading = false;
        this.dashboardList = response.data[0].children;
        const dashboard = this.dashboardList[0]
        if (dashboard) {
          this.currentDashboard = dashboard;
        } else {
          this.currentDashboard = this.dashboardList[0];
        }
        //这里是加载的tree，在数据概览对象中content布局内容是空，所以要根据id重新加载布局
        getdDashboardById(this.currentDashboard.id).then((resp) => {
          this.currentDashboard = resp.data;
        });
        if (this.currentDashboard) {
          //这地方因为是树，dashboard_id对应的是id，所以这里是去id
          this.$router.push(
            `/dashboard?id=${this.currentDashboard.id}&project_id=${this.project_id}`
          );
        }
      });
    },
    //概览树节点单击事件
    handleNodeClick(data) {
      const dashboardId = data.id;
      if (data.isChild == true) {
        if(data.isShare ){
          this.isShare = true;
        } else {
          this.isShare = false;
        }
        getdDashboardById(data.id).then((resp) => {
          this.currentDashboard = resp.data;
        });
        this.$router.push(
          `/dashboard?id=${this.currentDashboard.id}&project_id=${this.project_id}`
        );
      }
    },
    getList() {
      this.loading = true;
      // if(this.project_id == undefined){
      //   this.loading = false;
      //   return ;
      // }
      dashboardList(this.project_id).then((resp) => {
        this.loading = false;
        this.dashboardList = [];
        resp.data.order.forEach((id, index) => {
          const itemIndex = resp.data.dashboards.findIndex(
            (item) => item.dashboard_id === id
          );
          if (itemIndex >= 0) {
            this.dashboardList.push(resp.data.dashboards[itemIndex]);
            resp.data.dashboards.splice(itemIndex, 1);
          } else {
            console.log(id, index);
          }
        });
        this.dashboardList = this.dashboardList.concat(resp.data.dashboards);
        const dashboard = this.dashboardList.find(
          (item) => item.dashboard_id === this.$route.query.id
        );
        if (dashboard) {
          this.currentDashboard = dashboard;
        } else {
          this.currentDashboard = this.dashboardList[0];
        }
        if (this.currentDashboard) {
          this.$router.push(
            `/dashboard?id=${this.currentDashboard.dashboard_id}&project_id=${this.project_id}`
          );
        }
      }).catch(function (e) {
          console.log(e)
           this.loading = false;
        });;
    },
    //添加数据概览
    addDashboard() {
      this.dbObj = {};
      this.dbObj.private_status = 1;
      this.dbObj.is_public = false;
      this.editDialogVisible = true;
    },
    //修改数据概览
    editDashboard(db) {
      this.dbObj = this.currentDashboard;
      this.editDialogVisible = true;
    },
    handleCommand(cmd) {
      if (cmd.type === "edit") {
        this.editDashboard(cmd.target);
      } else {
        this.deleteDashboard(cmd.target);
      }
    },
    handleSubmit() {
      this.dbObj.project_id = this.project_id;
      if (this.project_id != undefined) {
        if (this.dbObj.dashboard_id) {
          updateDashboard(this.dbObj).then((resp) => {
             if(resp.code === 403){
                this.msgError(resp.msg);
                return;
              }
            this.myDashboardList();
            this.editDialogVisible = false;
          });
        } else {
          this.$refs["dbObj"].validate((valid) => {
            if (valid) {
              addDashboard(this.dbObj).then((resp) => {
                this.myDashboardList();
                this.editDialogVisible = false;
              });
            }
          });
        }
      } else {
        this.$message({
          type: "error",
          message: this.$t("common.firstAddProject"),
        });
      }
    },
    handleOrderChange(evt) {
      const data = this.dashboardList.map((item, index) => {
        return {
          dashboard_id: item.dashboard_id,
        };
      });
      dbOrder(data);
    },
    deleteDashboard() {
      this.$confirm(
        '确定要删除['+this.currentDashboard.dashborad_name+']概览吗？将同时删除该概览的分享',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        deleteDashboard({ dashboardId: this.currentDashboard.dashboard_id }).then((resp) => {
          if(resp.code === 403){
            this.msgError(resp.msg);
            return;
          }
          this.myDashboardList();
          this.$message({
            type: "success",
            message: this.$t("common.deleteSuccess"),
          });
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
</style>
