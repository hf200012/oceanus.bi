package com.oceanus.bi.mapper;

import com.oceanus.bi.domain.Datasource;

import java.util.List;
import java.util.Map;

/**
 * BI数据源操作接口
 */
public interface BIDataSourceMapper {

    public int insertDatasource(Datasource datasource);

    public int updateDatasource(Datasource datasource);

    public int deleteDatasource(Map<String,Object> params);

    public List<Datasource> selectDatasourceByUser(Map<String,Object> params);

    public Map<String, Object> selectDatasourceById(Map<String,Object> params);

    public Datasource getDatasourceById(Map<String,Object> params);

    public Datasource getDatasourceByChartId(Map<String,Object> params);



}
