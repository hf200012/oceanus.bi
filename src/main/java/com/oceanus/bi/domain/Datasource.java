package com.oceanus.bi.domain;

public class Datasource {

    private String username;
    private String password;
    private int source_id;
    private String created_at;
    private long creator_id;
    private String updated_at;
    private int is_private;
    private int status;
    private String conn_url        ;
    private String db_type;
    private String base_alias;
    private String driver_class;
    private String databasename;

    public int getIs_private() {
        return is_private;
    }

    public String getDatabasename() {
        return databasename;
    }

    public void setDatabasename(String databasename) {
        this.databasename = databasename;
    }

    public String getConn_url() {
        return conn_url;
    }

    public void setConn_url(String conn_url) {
        this.conn_url = conn_url;
    }

    public String getDb_type() {
        return db_type;
    }

    public void setDb_type(String db_type) {
        this.db_type = db_type;
    }

    public String getDriver_class() {
        return driver_class;
    }

    public void setDriver_class(String driver_class) {
        this.driver_class = driver_class;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public String getBase_alias() {
        return base_alias;
    }

    public void setBase_alias(String base_alias) {
        this.base_alias = base_alias;
    }
}
