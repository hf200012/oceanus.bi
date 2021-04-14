package com.oceanus.bi.mapper;

import com.oceanus.bi.domain.Chart;

import java.util.List;
import java.util.Map;

public interface BIChartMapper {

    public int insertBiChart(Chart chart);

    public int updateBiChart(Chart chart);

    public int deleteBiChartById(Map<String,Object> params);

    public Chart selectBiChartById(Map<String,Object> params);

    public List<Chart> selectAllChartByCreateor(Chart chart);
    /**
     * 根据dashboard id查询该下的所有chart
     * @param params
     * @return
     */
    public List<Chart> selectChartListByDashboardId(Map<String,Object> params);

}
