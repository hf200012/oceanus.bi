package com.oceanus.bi.mapper;

import com.oceanus.bi.domain.Dashboard;

import java.util.List;
import java.util.Map;

public interface BIDashboradMapper {

    public int insertBiDashboard(Dashboard dashboard);

    public int updateBiDashboard(Dashboard dashboard);

    public int insertChartToDashboard(Map<String,String> params);

    public int deleteChartToDashboard(Map<String,String> params);

    public List<String> selectIdOrderByCreatorId(Map<String,Object> params);

    public Dashboard selectBiDashboardById(Map<String,String> params);

    public int deleteBiDashboardById(Map<String,Object> params);

    public int deleteBiShareDashboardById(Map<String,Object> params);

    public int deleteBiDashboardChartMapById(Map<String,Object> params);

    public List<Dashboard> selectDashboardListByCreatorId(Map<String,Object> params);

    public List<Map<String,Object>> selectMyDashboardList(Map<String,Object> params);

    public List<Map<String,Object>> selectShareToMeDashboardList(Map<String,Object> params);

    public List<Map<String,Object>> selectPublicDashboardList(Map<String,Object> params);

    public int updateBiDashboardOrder(Dashboard dashboard);

    public int deleteBiDashboardByProjectId(Map<String,Object> params);

    public int deleteBiShareDashboardByProjectId(Map<String,Object> params);

    public int deleteBiDashboardChartMapByProjectId(Map<String,Object> params);
}
