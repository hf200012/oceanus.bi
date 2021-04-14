package com.oceanus.bi.service;

import com.alibaba.fastjson.JSON;
import com.oceanus.bi.domain.Dashboard;
import com.oceanus.bi.mapper.BIDashboradMapper;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BIDashboardService {

    @Autowired
    private BIDashboradMapper biDashboradMapper;

    public int saveBiDashboard(Dashboard dashboard){
        dashboard.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        dashboard.setCreated_at(DateUtil.getCurrentDateFormat());
        if(!dashboard.isIs_public()) {
            dashboard.setPrivate_status(1);
        } else {
            dashboard.setPrivate_status(0);
        }
        dashboard.setDashborad_status(1);
        dashboard.setDashbordContent(JSON.toJSONString(dashboard.getContent()));
        return this.biDashboradMapper.insertBiDashboard(dashboard);
    }

    public int updateBiDashboard(Dashboard dashboard){
        if(!dashboard.isIs_public()) {
            dashboard.setPrivate_status(1);
        } else {
            dashboard.setPrivate_status(0);
        }
        dashboard.setUpdated_at(DateUtil.getCurrentDateFormat());
        dashboard.setDashbordContent(JSON.toJSONString(dashboard.getContent()));
        return this.biDashboradMapper.updateBiDashboard(dashboard);
    }

    /**
     * 添加图表到看板中
     * @param chart_id
     * @param dashboard_id
     * @return
     */
    public int addChartToDashboard(String chart_id,String dashboard_id){
        Map<String,String> params = new HashMap<>();
        params.put("chart_id",chart_id);
        params.put("dashboard_id",dashboard_id);
        params.put("updated_at",DateUtil.getCurrentDateFormat());
        params.put("created_at",DateUtil.getCurrentDateFormat());
        return this.biDashboradMapper.insertChartToDashboard(params);
    }

    /**
     * 从看板中移除图表
     * @param chart_id
     * @param dashboard_id
     * @return
     */
    public int removeChartToDashboard(String chart_id,String dashboard_id){
        Map<String,String> params = new HashMap<>();
        params.put("chart_id",chart_id);
        params.put("dashboard_id",dashboard_id);
        return this.biDashboradMapper.deleteChartToDashboard(params);
    }

    /**
     * 根据ID查询看板
     * @param dashboard_id
     * @return
     */
    public Dashboard selectBiDashboardById(String dashboard_id){
        Map<String,String> params = new HashMap<>();
        params.put("dashboard_id",dashboard_id);
        return this.biDashboradMapper.selectBiDashboardById(params);
    }

    /**
     * 删除看板
     * @param dashboard_id
     * @return
     */
    public int deleteBiDashboardById(String dashboard_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("dashboard_id", dashboard_id);
        try {
            //首先删除改概览对应的分享
            this.biDashboradMapper.deleteBiShareDashboardById(params);
            //在删除改概览对应的图表关系
            this.biDashboradMapper.deleteBiDashboardChartMapById(params);
            //最后删除概览
            this.biDashboradMapper.deleteBiDashboardById(params);
            return 1;
        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前用户创建的所有看板
     * @return
     */
    public List<Dashboard> getDashboardListByCreatorId(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id", SecurityUtils.getLoginUser().getUser().getUserId());
        params.put("project_id", project_id);
        List<Dashboard> dashboards = this.biDashboradMapper.selectDashboardListByCreatorId(params);
        for (Dashboard dashboard :dashboards){
            dashboard.setContent(JSON.parse(dashboard.getDashbordContent()));
        }
        return dashboards;
    }

    /**
     * 获取当前企业所有公共的数据概览
     * @return
     */
    public List<Map<String,Object>> getMyDashboardList(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id", SecurityUtils.getLoginUser().getUser().getUserId());
        params.put("project_id", project_id);
        List<Map<String,Object>> dashboards = this.biDashboradMapper.selectMyDashboardList(params);
        for (Map<String,Object> map :dashboards){
            map.put("content",JSON.parse(map.get("dashbordContent").toString()));
            map.put("isShare",false);
        }
        return dashboards;
    }

    /**
     * 获取当前企业所有公共的数据概览
     * @return
     */
    public List<Map<String,Object>> getPublicDashboardList(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id", SecurityUtils.getLoginUser().getUser().getUserId());
        params.put("project_id", project_id);
        List<Map<String,Object>> dashboards = this.biDashboradMapper.selectPublicDashboardList(params);
        for (Map<String,Object> map :dashboards){
            map.put("content",JSON.parse(map.get("dashbordContent").toString()));
            map.put("isShare",true);
        }
        return dashboards;
    }


    /**
     * 获取分享给我的数据概览
     * @param project_id
     * @return
     */
    public List<Map<String,Object>> getShareDashboardList(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id", SecurityUtils.getLoginUser().getUser().getUserId());
        params.put("project_id", project_id);
        List<Map<String,Object>> dashboards = this.biDashboradMapper.selectShareToMeDashboardList(params);
        for (Map<String,Object> map :dashboards){
            map.put("content",JSON.parse(map.get("dashbordContent").toString()));
            map.put("isShare",true);
        }
        return dashboards;
    }


    /**
     * 获取看板排序
     * @return
     */
    public List<String> getOrderByCreatorId(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id", SecurityUtils.getLoginUser().getUser().getUserId());
        params.put("project_id", project_id);
        return this.biDashboradMapper.selectIdOrderByCreatorId(params);
    }

    /**
     * 看板排序
     * @param dashboards
     * @return
     */
    public int updateBiDashboardOrder(List<Dashboard> dashboards){
        try {
            for (Dashboard dashboard : dashboards) {
                dashboard.setUpdated_at(DateUtil.getCurrentDateMFormat());
                this.biDashboradMapper.updateBiDashboardOrder(dashboard);
            }
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}
