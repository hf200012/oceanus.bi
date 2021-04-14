package com.oceanus.bi.service;

import com.alibaba.fastjson.JSON;
import com.oceanus.bi.domain.Datasource;
import com.oceanus.bi.domain.DatasourceTable;
import com.oceanus.bi.domain.TableField;
import com.oceanus.bi.mapper.BIDataSourceMapper;
import com.oceanus.bi.mapper.BIDataSourceTableMapper;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.utils.StringUtils;
import com.oceanus.common.datasource.HikariPoolManager;
import com.oceanus.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BIDatasourceService {

    @Autowired
    private BIDataSourceMapper biDataSourceMapper;

    @Autowired
    private BIDataSourceTableMapper biDataSourceTableMapper;

    /**
     * 插入数据源
     * @param datasource
     * @return
     */
    public int insertDatasource(Datasource datasource){
        datasource.setCreated_at(DateUtil.getCurrentDateFormat());
        datasource.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        datasource.setIs_private(1);
        datasource.setStatus(1);
        datasource.setUpdated_at(DateUtil.getCurrentDateFormat());
        return this.biDataSourceMapper.insertDatasource(datasource);
    }

    public boolean verifyDBConnect(Datasource datasource){
        Connection connect = null;
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> dbConfig = new HashMap<>();
        dbConfig.put("driver_classname",datasource.getDriver_class());
        dbConfig.put("conn_url",datasource.getConn_url());
        dbConfig.put("user_name",datasource.getUsername());
        dbConfig.put("pass_word",datasource.getPassword());
        try {
            connect = HikariPoolManager.getConnection(dbConfig);
            if(connect != null) {
                String schema = connect.getSchema();
                datasource.setDatabasename(schema);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HikariPoolManager.freeConnection(connect);
        }
        return false;
    }

    /**
     * 保存表中文名称
     * @param json
     * @return
     */
    public int saveTableConfig(String json){
        try {
            Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
            List<Map<String, Object>> tables = (List<Map<String, Object>>) map.get("tables");
            for (Map<String, Object> params : tables) {
                this.biDataSourceTableMapper.updateDatasourceTableAlise(params);
            }
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 保存表字段中文名称
     * @param json
     * @return
     */
    public int saveTableFieldsConfig(String json){
        try {
            List<Map<String, Object>> tables = (List<Map<String, Object>>) JSON.parse(json);
            for (Map<String, Object> params : tables) {
                this.biDataSourceTableMapper.updateDatasourceTableField(params);
            }
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新数据源
     * @param datasource
     * @return
     */
    public int updateDatasource(Datasource datasource){
        //从数据库连接池中删除改数据源对用的连接池
        HikariPoolManager.removeDatasource(datasource.getSource_id() +"");
        return this.biDataSourceMapper.updateDatasource(datasource);
    }

    /**
     * 根据数据源编号删除数据源
     * @param params
     * @return
     */
    public int deleteDatasource(Map<String,Object> params){
        int source_id = (Integer) params.get("source_id");
        HikariPoolManager.removeDatasource(source_id +"");
        return this.biDataSourceMapper.deleteDatasource(params);
    }


    public Datasource getDatasourceById(int sourceId){
        Map<String,Object> params = new HashMap<>();
        params.put("source_id",sourceId);
        return this.biDataSourceMapper.getDatasourceById(params);
    }
    /**
     * 获取该用户的所有数据源
     * 目前实现是根据用户来实现的，后期要改成和企业，用户角色绑定，根据用户获取企业，及对应的角色
     * @return
     */
    public List<Datasource> getAllDatasourceByUser(){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id",SecurityUtils.getLoginUser().getUser().getUserId());
        return this.biDataSourceMapper.selectDatasourceByUser(params);
    }

    /**
     * 根据数据源获取该数据源的所有表
     * @param sourceId
     * @return
     */
    public List<DatasourceTable> getTableBySourceId(int sourceId){
        Map<String,Object> params = new HashMap<>();
        params.put("source_id",sourceId);
        return this.biDataSourceTableMapper.selectTableBySourceId(params);
    }

    /**
     * 查询表的所有字段属性信息
     * @param sourceId
     * @param tableName
     * @return
     */
    public List<TableField> getTableFieldsByTableName(int sourceId,String tableName){
        Map<String,Object> params = new HashMap<>();
        params.put("table_name",tableName);
        params.put("source_id",sourceId);
        return this.biDataSourceTableMapper.selectTabelFieldByTableNameAndSource(params);
    }

    /**
     * 查询表的所有字段属性信息
     * @param table_id
     * @return
     */
    public List<TableField> getTableFieldsByTableId(int table_id){
        Map<String,Object> params = new HashMap<>();
        params.put("table_id",table_id);
        return this.biDataSourceTableMapper.selectTabelFieldByTableId(params);
    }


    /**
     * 根据chartId查询该图表对应的数据源信息
     * @param chartId
     * @return
     */
    public Datasource getDatasourceByChartId(String chartId){
        Map<String,Object> params = new HashMap<>();
        params.put("chart_id",chartId);
        return this.biDataSourceMapper.getDatasourceByChartId(params);
    }

    /**
     * 根据数据源ID查询数据信息
     * @param source_id
     * @return
     */
    public Map<String, Object> getDbConfigById(int source_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("source_id", source_id);
        Map<String, Object> db = this.biDataSourceMapper.selectDatasourceById(params);
        return db;
    }

    /**
     * 从目标数据源中同步元数据
     * @param source_id
     * @return
     */
    public int syncMeta(int source_id){
        Map<String, Object> dbconfig = this.getDbConfigById(source_id);
        Map<String, Object> params = new HashMap<>();
        params.put("source_id",source_id);
        //重新同步元数据首先删除该数据源对应的表及字段规则
        this.biDataSourceTableMapper.deleteDatasourceTableFieldByDatasourceId(params);
        this.biDataSourceTableMapper.deleteDatasourceTableBySource(params);
        //获取数据库连接
        Connection connect = null;
        List<String> tableNames = new ArrayList<>();
        try {
            connect = HikariPoolManager.getConnection(dbconfig);
            //获取数据库中所有的表及视图
            DatabaseMetaData metaData = connect.getMetaData();
            String schema = connect.getCatalog();
            ResultSet res = metaData.getTables(schema, null, "%", new String[]{"TABLE", "VIEW"});
            while (res.next()) {
                String tableName = res.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            for (String tableName : tableNames) {
                List<TableField> fields = new ArrayList<>();
                ResultSet rsColimns = metaData.getColumns(null, "%", tableName, "%");
                while (rsColimns.next()) {
                    TableField field = new TableField();
                    field.setCreatedate(DateUtil.getCurrentDateFormat());
                    field.setIs_enable(1);
                    String typeName = rsColimns.getString("TYPE_NAME");
                    field.setField_type(typeName.toLowerCase());
                    field.setField_name(rsColimns.getString("COLUMN_NAME"));
                    field.setField_cname(rsColimns.getString("COLUMN_NAME"));
                    field.setCreate_user_id(SecurityUtils.getLoginUser().getUser().getUserId());
                    field.setCreate_user_name(SecurityUtils.getUsername());
                    if(StringUtils.isNotEmpty(rsColimns.getString("COLUMN_SIZE"))) {
                        field.setField_lenght(Integer.parseInt(rsColimns.getString("COLUMN_SIZE")));
                    } else {
                        field.setField_lenght(0);
                    }
                    String isEmpty = rsColimns.getString("IS_NULLABLE");
                    if ("YES".equalsIgnoreCase(isEmpty)) {
                        field.setIs_empty(1);
                    } else {
                        field.setIs_empty(0);
                    }
                    fields.add(field);
                }
                boolean isSuccess = saveTable(tableName, fields, source_id);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            HikariPoolManager.freeConnection(connect);
        }
    }

    private boolean saveTable(String tableName, List<TableField> fields, int datasource_id)  {
        try {
            DatasourceTable table = new DatasourceTable();
            table.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
            table.setSource_id(datasource_id);
            table.setCreated_at(DateUtil.getCurrentDateFormat());
            table.setStatus(1);
            table.setTable_name(tableName);
            table.setTable_alias(tableName);
            table.setUpdated_at(DateUtil.getCurrentDateFormat());
            this.biDataSourceTableMapper.insertDatasourceTable(table);
            for (TableField field : fields) {
                field.setTable_id(table.getTable_id());
            }
            this.biDataSourceTableMapper.insertDataSourceTableField(fields);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
