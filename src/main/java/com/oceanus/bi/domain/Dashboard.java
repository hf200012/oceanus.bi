package com.oceanus.bi.domain;

/**
 * dashboard
 */
public class Dashboard {

    private Object content;

    private String dashbordContent;

    private Object json;

    private String created_at;

    private String dashboard_id;

    private String updated_at;

    private long creator_id;

    private int dashborad_status;

    private int private_status;

    private String dashborad_desc;

    private String dashborad_name;

    private String project_id;

    private boolean is_public;

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getDashbordContent() {
        return dashbordContent;
    }

    public void setDashbordContent(String dashbordContent) {
        this.dashbordContent = dashbordContent;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDashboard_id() {
        return dashboard_id;
    }

    public void setDashboard_id(String dashboard_id) {
        this.dashboard_id = dashboard_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public int getDashborad_status() {
        return dashborad_status;
    }

    public void setDashborad_status(int dashborad_status) {
        this.dashborad_status = dashborad_status;
    }

    public int getPrivate_status() {
        return private_status;
    }

    public void setPrivate_status(int private_status) {
        this.private_status = private_status;
    }

    public String getDashborad_desc() {
        return dashborad_desc;
    }

    public void setDashborad_desc(String dashborad_desc) {
        this.dashborad_desc = dashborad_desc;
    }

    public String getDashborad_name() {
        return dashborad_name;
    }

    public void setDashborad_name(String dashborad_name) {
        this.dashborad_name = dashborad_name;
    }
}
