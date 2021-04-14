package com.oceanus.bi.domain;

import com.oceanus.framework.web.domain.BaseEntity;

public class ExecSql extends BaseEntity {

    public int source_id;

    private String sql ;

    private int rows;

    private String report_id;

    private String field_cnames;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getField_cnames() {
        return field_cnames;
    }

    public void setField_cnames(String field_cnames) {
        this.field_cnames = field_cnames;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
