package com.oceanus.bi.service;

import com.oceanus.bi.domain.DatasourceTable;
import com.oceanus.bi.domain.TableField;
import com.oceanus.bi.mapper.BIDataSourceTableMapper;
import com.oceanus.common.util.DateUtil;
import com.oceanus.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表表及字段操作接口
 */
@Service
public class BITableService {

    @Autowired
    private BIDataSourceTableMapper biDataSourceTableMapper;

    public int updateDatasourceTable(DatasourceTable table){
        return this.biDataSourceTableMapper.updateDatasourceTable(table);
    }


    public int saveTable(DatasourceTable table){
        table.setCreated_at(DateUtil.getCurrentDateMFormat());
        table.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        table.setStatus(1);
        table.setUpdated_at(DateUtil.getCurrentDateMFormat());
        return this.biDataSourceTableMapper.insertDatasourceTable(table);
    }

    public int insertTableField(List<TableField> fields){
        return this.biDataSourceTableMapper.insertDataSourceTableField(fields);
    }

    public List<DatasourceTable> getTableBySourceId(int sourceId){
        Map<String,Object> params = new HashMap<>();
        params.put("source_id",sourceId);
        return this.biDataSourceTableMapper.selectTableBySourceId(params);
    }

    public List<TableField> selectTabelFieldByTableId(Map<String,Object> params){
        return this.biDataSourceTableMapper.selectTabelFieldByTableId(params);
    }

    public List<TableField> selectTabelFieldBySourceAndTableName(int sourceId,String tableName){
        Map<String,Object> params = new HashMap<>();
        params.put("source_id",sourceId);
        params.put("table_name",tableName);
        return this.biDataSourceTableMapper.selectTabelFieldBySourceAndTableName(params);
    }

}
