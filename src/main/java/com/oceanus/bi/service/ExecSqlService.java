package com.oceanus.bi.service;

import com.oceanus.bi.domain.ExecSql;
import com.oceanus.bi.domain.Report;
import com.oceanus.bi.mapper.BIDataSourceMapper;
import com.oceanus.bi.mapper.ReportMapper;
import com.oceanus.common.datasource.HikariPoolManager;
import com.oceanus.common.utils.StringUtils;
import com.oceanus.framework.web.page.PageDomain;
import com.oceanus.framework.web.page.TableSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExecSqlService {

    @Autowired
    private BIDataSourceMapper biDataSourceMapper;

    @Autowired
    private ReportMapper reportMapper;

    public List<Map<String,Object>> execSql(int sourceId,String sql){
        Map<String, Object> params = new HashMap<>();
        params.put("source_id",sourceId);
        Map<String, Object> dbConfig = this.biDataSourceMapper.selectDatasourceById(params);
        Connection connect = null;
        List<Map<String, Object>> records = new ArrayList<>();
        try {
            connect = HikariPoolManager.getConnection(dbConfig);
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //获取列名
            ResultSetMetaData rsm = rs.getMetaData();
            int col = rsm.getColumnCount();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                for (int j = 0; j < col; j++) {
                    record.put(rsm.getColumnName(j + 1), rs.getString(j + 1));
                }
                records.add(record);
            }
            st.close();
            return records;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HikariPoolManager.freeConnection(connect);
        }
        return null;
    }


    /**
     * 在数据仓库中执行sql，获取结果及返回结果的字段名称
     * @param sourceId
     * @param sql
     * @param limitRows
     * @return
     */
    public Map<String,Object> exec(int sourceId,String sql,int limitRows){
        Map<String, Object> params = new HashMap<>();
        params.put("source_id",sourceId);
        Map<String, Object> db = this.biDataSourceMapper.selectDatasourceById(params);
        Connection connect = null;
        Map<String,Object> result = new HashMap<>();
        List<Map<String,String>> cols = new ArrayList<>();
        List<Map<String,String>> records = new ArrayList<>();
        try {
            connect = HikariPoolManager.getConnection(db);
            sql = sql + " limit " + limitRows;
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //获取列名
            ResultSetMetaData rsm =rs.getMetaData();
            int col = rsm.getColumnCount();
            for(int i = 0;i < col; i ++){
                Map<String,String> colNames = new HashMap<>();
                colNames.put("dataItem", rsm.getColumnName( i + 1 ));
                colNames.put("dataName", rsm.getColumnName( i + 1 ));
                cols.add(colNames);
            }
            while (rs.next()){
                Map<String,String> record = new HashMap<>();
                for(int j = 0;j < col; j ++){
                    record.put(rsm.getColumnName( j + 1 ),rs.getString(j+1));
                }
                records.add(record);
            }
            result.put("tables",records);
            result.put("tableData",cols);
            st.close();

        } catch (SQLException e){
            result.put("error",e.getMessage());
        }finally {
            HikariPoolManager.freeConnection(connect);
        }
        return result;
    }


    /**
     * 在数据仓库中执行sql，获取结果及返回结果的字段名称
     * @param sqlObj
     * @return
     */
    public Map<String,Object> execSQL(ExecSql sqlObj){
        Map<String, Object> params = new HashMap<>();
        params.put("source_id",sqlObj.source_id);
        Map<String, Object> db = this.biDataSourceMapper.selectDatasourceById(params);
        Connection connect = null;
        Map<String,Object> result = new HashMap<>();
        List<Map<String,String>> cols = new ArrayList<>();
        List<Map<String,String>> records = new ArrayList<>();
        try {
            connect = HikariPoolManager.getConnection(db);
            String sql = sqlObj.getSql();
            sql = sql + " limit " + sqlObj.getRows();
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //获取列名
            ResultSetMetaData rsm =rs.getMetaData();
            int col = rsm.getColumnCount();
            //获取字段的中文名称如果没有，取sql返回的元数据中的字段名称
            String field_cname = sqlObj.getField_cnames();
            if(StringUtils.isNotEmpty(field_cname)){
                String[] cnames = field_cname.split(",");
                for(int i = 0;i < col; i ++){
                    Map<String,String> colNames = new HashMap<>();
                    colNames.put("dataItem", rsm.getColumnName(i + 1));
                    colNames.put("dataName", cnames[i]);
                    cols.add(colNames);
                }
            } else {
                for (int i = 0; i < col; i++) {
                    Map<String, String> colNames = new HashMap<>();
                    colNames.put("dataItem", rsm.getColumnName(i + 1));
                    colNames.put("dataName", rsm.getColumnName(i + 1));
                    cols.add(colNames);
                }
            }
            while (rs.next()){
                Map<String,String> record = new HashMap<>();
                for(int j = 0;j < col; j ++){
                    record.put(rsm.getColumnName( j + 1 ),rs.getString(j+1));
                }
                records.add(record);
            }
            result.put("tables",records);
            result.put("tableData",cols);
            st.close();

        } catch (SQLException e){
            result.put("error",e.getMessage());
        }finally {
            HikariPoolManager.freeConnection(connect);
        }
        return result;
    }




    /**
     * 在数据仓库中执行sql，获取结果及返回结果的字段名称
     * @param report
     * @return
     */
    public Map<String,Object> getReportResult(Report report){
        Map<String, Object> params = new HashMap<>();
        params.put("source_id",report.getSource_id());
        Map<String, Object> db = this.biDataSourceMapper.selectDatasourceById(params);
        Connection connect = null;
        Map<String,Object> result = new HashMap<>();
        List<Map<String,String>> cols = new ArrayList<>();
        List<Map<String,String>> records = new ArrayList<>();
        try {
//            Object dataScope = report.getParams().get("dataScope");
            params.put("report_id",report.getReport_id());
            report = this.reportMapper.selectBiReportById(params);
            connect = HikariPoolManager.getConnection(db);
            String sql = report.getReport_sql();
            PageDomain pageDomain = TableSupport.buildPageRequest();
            System.out.println("size：" + pageDomain.getPageSize());
            System.out.println("page num：" + pageDomain.getPageNum());
            /**
             * 这里需要根据不同的数据库类型添加不同的翻页支持，目前支持mysql、doris
             */
            if (pageDomain != null){
                sql +=  " limit " + pageDomain.getPageSize() * (pageDomain.getPageNum()-1) + "," + pageDomain.getPageSize();
            }
            System.out.println("执行SQL： " + sql);

            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //获取列名
            ResultSetMetaData rsm =rs.getMetaData();
            int col = rsm.getColumnCount();
            //获取字段的中文名称如果没有，取sql返回的元数据中的字段名称
            String field_cname = report.getField_cnames();
            if(StringUtils.isNotEmpty(field_cname)){
                String[] cnames = field_cname.split(",");
                for(int i = 0;i < col; i ++){
                    Map<String,String> colNames = new HashMap<>();
                    colNames.put("dataItem", rsm.getColumnName(i + 1));
                    colNames.put("dataName", cnames[i]);
                    cols.add(colNames);
                }
            } else {
                for (int i = 0; i < col; i++) {
                    Map<String, String> colNames = new HashMap<>();
                    colNames.put("dataItem", rsm.getColumnName(i + 1));
                    colNames.put("dataName", rsm.getColumnName(i + 1));
                    cols.add(colNames);
                }
            }
            while (rs.next()){
                Map<String,String> record = new HashMap<>();
                for(int j = 0;j < col; j ++){
                    record.put(rsm.getColumnName( j + 1 ),rs.getString(j+1));
                }
                records.add(record);
            }
            //计算总记录数
            String sqlcount = "select count(1) from (" + report.getReport_sql() + ") a";
            st = connect.createStatement();
            ResultSet rs1 = st.executeQuery(sqlcount);
            long total = 0;
            while (rs1.next()){
                total = rs1.getLong(1);
            }
            result.put("total",total);
            result.put("tables",records);
            result.put("tableData",cols);
            st.close();

        } catch (SQLException e){
            e.printStackTrace();
            result.put("error",e.getMessage());
        }finally {
            HikariPoolManager.freeConnection(connect);
        }
        return result;
    }

}
