package com.oceanus.bi.domain;

import com.oceanus.framework.web.domain.BaseEntity;

/**
 * 项目
 */
public class Project extends BaseEntity {

    private String project_id;

    private String project_name;

    private String project_desc;

    private String created_at;

    private long creator_id;

    private int private_status;

    private boolean is_public;

    private int status;

    private String creator_username;

    private int dashboard_count;

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public int getDashboard_count() {
        return dashboard_count;
    }

    public void setDashboard_count(int dashboard_count) {
        this.dashboard_count = dashboard_count;
    }

    public String getCreator_username() {
        return creator_username;
    }

    public void setCreator_username(String creator_username) {
        this.creator_username = creator_username;
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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_desc() {
        return project_desc;
    }

    public void setProject_desc(String project_desc) {
        this.project_desc = project_desc;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }
}
