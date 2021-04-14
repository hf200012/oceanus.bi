package com.oceanus.bi.domain;

import com.oceanus.framework.aspectj.lang.annotation.Excel;
import com.oceanus.framework.web.domain.BaseEntity;

/**
 * 表对应的字段名称
 */
public class TableField extends BaseEntity {

    private int field_id           ;
    private int table_id           ;
    @Excel(name = "字段名称")
    private String field_name      ;
    @Excel(name = "字段中文名称")
    private String field_cname     ;
    @Excel(name = "字段类型")
    private String field_type      ;
    @Excel(name = "是否为空")
    private int is_empty        ;
    private String createdate      ;
    private long create_user_id  ;
    private String create_user_name;
    private int is_enable       ;
    @Excel(name = "字段长度")
    private int field_lenght    ;

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getField_cname() {
        return field_cname;
    }

    public void setField_cname(String field_cname) {
        this.field_cname = field_cname;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public int getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(int is_empty) {
        this.is_empty = is_empty;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public long getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(long create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public int getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(int is_enable) {
        this.is_enable = is_enable;
    }

    public int getField_lenght() {
        return field_lenght;
    }

    public void setField_lenght(int field_lenght) {
        this.field_lenght = field_lenght;
    }
}
