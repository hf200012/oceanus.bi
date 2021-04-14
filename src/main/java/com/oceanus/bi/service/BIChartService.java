package com.oceanus.bi.service;

import com.alibaba.fastjson.JSON;
import com.oceanus.bi.domain.Chart;
import com.oceanus.bi.domain.TableField;
import com.oceanus.bi.mapper.BIChartMapper;
import com.oceanus.bi.mapper.BIDataSourceTableMapper;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BIChartService {

    @Autowired
    private BIChartMapper biChartMapper;

    @Autowired
    private BIDataSourceTableMapper biDataSourceTableMapper;

    /**
     * 保存图表
     * @param chart
     * @return
     */
    public int saveChart(Chart chart){
        chart.setCreated_at(DateUtil.getCurrentDateFormat());
        chart.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        chart.setIs_private(1);
        chart.setChartContent(JSON.toJSONString(chart.getContent()));
        chart.setStatus(1);
        return this.biChartMapper.insertBiChart(chart);
    }

    /**
     * 修改图表
     * @param chart
     * @return
     */
    public int updateBiChart(Chart chart){
        chart.setUpdated_at(DateUtil.getCurrentDateFormat());
        chart.setChartContent(JSON.toJSONString(chart.getContent()));
        return this.biChartMapper.updateBiChart(chart);
    }

    /**
     * 删除图表
     * @param chart_id
     * @return
     */
    public int deleteBiChartById(String chart_id){
        Map<String,Object> params = new HashMap<>();
        params.put("chart_id",chart_id);
        return this.biChartMapper.deleteBiChartById(params);
    }

    /**
     * 查询该用户创建的所有图表
     * @return
     */
    public List<Chart> getAllChartByUser(Chart chart){
        chart.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        return this.biChartMapper.selectAllChartByCreateor(chart);
    }

    /**
     * 根据chartid查询图表
     * @param chart_id
     * @return
     */
    public Chart selectBiChartById(String chart_id){
        Map<String,Object> params = new HashMap<>();
        params.put("chart_id",chart_id);
        return this.biChartMapper.selectBiChartById(params);
    }
    /**
     * 根据dashboard id查询该下的所有chart
     * @param dashboard_id
     * @return
     */
    public List<Chart> selectChartListByDashboardId(String dashboard_id){
        Map<String,Object> params = new HashMap<>();
        params.put("dashboard_id",dashboard_id);
        List<Chart> charts = this.biChartMapper.selectChartListByDashboardId(params);
        for (Chart chart : charts){
            chart.setContent(JSON.parse(chart.getChartContent()));
            //获取图的对应数据源ID，表名
            int sourceId = chart.getSource_id();
            Map<String, Object> obj = JSON.parseObject(chart.getChartContent(),Map.class);
            String tableName = obj.get("dataSrc").toString();
            params.put("source_id",sourceId);
            params.put("table_name",tableName);
            List<TableField> fields = this.biDataSourceTableMapper.selectTabelFieldBySourceAndTableName(params);
            chart.setFields(fields);
        }
        return charts;
    }
}
