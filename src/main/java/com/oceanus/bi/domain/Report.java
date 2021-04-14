package com.oceanus.bi.domain;

import com.oceanus.framework.web.domain.BaseEntity;

public class Report extends BaseEntity {

    private String report_id;

    private String report_name;

    private String report_desc;

    private String report_sql;

    private boolean is_public;

    private int private_status;

    private int status;

    private String create_date;

    private long creator_id;

    private int source_id;

    private String update_date;

    private String field_cnames;

    public String getField_cnames() {
        return field_cnames;
    }

    public void setField_cnames(String field_cnames) {
        this.field_cnames = field_cnames;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public String getReport_desc() {
        return report_desc;
    }

    public void setReport_desc(String report_desc) {
        this.report_desc = report_desc;
    }

    public String getReport_sql() {
        return report_sql;
    }

    public void setReport_sql(String report_sql) {
        this.report_sql = report_sql;
    }

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public int getPrivate_status() {
        return private_status;
    }

    public void setPrivate_status(int private_status) {
        this.private_status = private_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }
}
