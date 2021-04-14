package com.oceanus.bi.mapper;


import com.oceanus.bi.domain.DatasourceTable;
import com.oceanus.bi.domain.TableField;

import java.util.List;
import java.util.Map;

public interface BIDataSourceTableMapper {

    public int insertDatasourceTable(DatasourceTable table);

    public int updateDatasourceTable(DatasourceTable table);

    public int updateDatasourceTableAlise(Map<String,Object> params);

    public int deleteDatasourceTable(Map<String,Object> params);

    public int deleteDatasourceTableBySource(Map<String,Object> params);

    public List<DatasourceTable> selectTableBySourceId(Map<String,Object> params);

    public int insertDataSourceTableField(List<TableField> field);

    public int updateDatasourceTableField(Map<String,Object> params);

    public int insertTableField(TableField field);

    public int deleteDatasourceTableFieldByTableId(Map<String,Object> params);

    public int deleteDatasourceTableFieldByDatasourceId(Map<String,Object> params);

    public List<TableField> selectTabelFieldByTableId(Map<String,Object> params);

    public List<TableField> selectTabelFieldByTableNameAndSource(Map<String,Object> params);

    public List<TableField> selectTabelFieldBySourceAndTableName(Map<String,Object> params);



}
