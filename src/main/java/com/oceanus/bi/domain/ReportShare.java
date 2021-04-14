package com.oceanus.bi.domain;

public class ReportShare {

    private String report_id;

    private long to_user_id;

    private long share_user_id;

    private String share_date;

    private int share_id;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
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

    public String getShare_date() {
        return share_date;
    }

    public void setShare_date(String share_date) {
        this.share_date = share_date;
    }

    public int getShare_id() {
        return share_id;
    }

    public void setShare_id(int share_id) {
        this.share_id = share_id;
    }
}
