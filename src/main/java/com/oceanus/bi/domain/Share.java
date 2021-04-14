package com.oceanus.bi.domain;

import java.io.Serializable;

/**
 * 数据概览共享对象
 */
public class Share implements Serializable {

    public int share_id;

    public long to_user_id;

    public long share_user_id;

    public String dashboard_id;

    public String share_date;

    public void setShare_id(int share_id) {
        this.share_id = share_id;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public long getShare_user_id() {
        return share_user_id;
    }

    public void setShare_user_id(long share_user_id) {
        this.share_user_id = share_user_id;
    }

    public int getShare_id() {
        return share_id;
    }

    public String getDashboard_id() {
        return dashboard_id;
    }

    public void setDashboard_id(String dashboard_id) {
        this.dashboard_id = dashboard_id;
    }

    public String getShare_date() {
        return share_date;
    }

    public void setShare_date(String share_date) {
        this.share_date = share_date;
    }
}
