package com.oceanus.bi.domain;

import com.oceanus.framework.web.domain.BaseEntity;

import java.util.List;


public class Chart  extends BaseEntity {

    private String chart_name;

    private int source_id;

    private String chart_desc;

    private Object content;

    private String chartContent;

    private int is_private;

    private int status;

    private long creator_id;

    private String created_at;

    private String updated_at;

    private String chart_id;

    private Object json;

    List<TableField> fields ;

    public List<TableField> getFields() {
        return fields;
    }

    public void setFields(List<TableField> fields) {
        this.fields = fields;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public String getChartContent() {
        return chartContent;
    }

    public void setChartContent(String chartContent) {
        this.chartContent = chartContent;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public String getChart_name() {
        return chart_name;
    }

    public void setChart_name(String chart_name) {
        this.chart_name = chart_name;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getChart_desc() {
        return chart_desc;
    }

    public void setChart_desc(String chart_desc) {
        this.chart_desc = chart_desc;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getChart_id() {
        return chart_id;
    }

    public void setChart_id(String chart_id) {
        this.chart_id = chart_id;
    }
}
