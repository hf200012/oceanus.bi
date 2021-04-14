package com.oceanus.common.datasource;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zaxxer.hikari.HikariDataSource;


public class HikariPoolManager {

    private static Logger logger = LogManager.getLogger();

    //-- Hikari Datasource -->
    //driverClassName无需指定，除非系统无法自动识别
    private static String driverClassName = "";
    //database address
    private static String jdbcUrl = "";
    //useName 用户名
    private static String username = "";
    //password
    private static String password = "";
    //连接只读数据库时配置为true， 保证安全 -->
    private static boolean readOnly = false;
    //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
    private static int connectionTimeout = 30000;
    // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
    private static int idleTimeout = 600000;
    //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） -->
    private static int maxLifetime = 1800000;
    // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
    private static int maximumPoolSize = 30;
    private static int initPoolSize = 20;

    private static Map<String,HikariDataSource> datasources = new HashMap<>();

    /**
     * 读取数据库配置文件
     *
     * @param properties 配置文件名
     * @return
     * @Exception FileNotFoundException, IOException
     */
    private static void readProperties(Map<String, Object> properties) {
        try {
            driverClassName = properties.get("driver_classname").toString();
            jdbcUrl = properties.get("conn_url").toString();
            username = properties.get("user_name").toString();
            password = properties.get("pass_word").toString();
        } catch (Exception e) {
            logger.error("读取数据库参数出现问题：" + e);
            throw e;

        }
    }

    /**
     * 修改数据源的时候，删除改数据源对应的初始化连接池
     * @param source_id
     */
    public static void removeDatasource(String source_id){
        datasources.remove(source_id);
    }

    /**
     * 设置datasource各个属性值
     *
     * @param
     * @return Connection
     * @Exception Exception
     */
    public static void dataSourceConfig( Map<String, Object> properties,String datasourceName ) {
        try {
            readProperties(properties);
            HikariDataSource hikariDataSource = new HikariDataSource();
            //driverClassName无需指定，除非系统无法自动识别
            //private static String driverClassName="";
            hikariDataSource.setDriverClassName(driverClassName);
            //database address
            hikariDataSource.setJdbcUrl(jdbcUrl);
            //useName 用户名
            hikariDataSource.setUsername(username);
            //password
            hikariDataSource.setPassword(password);
            //连接只读数据库时配置为true， 保证安全 -->
            hikariDataSource.setReadOnly(readOnly);
            //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
            hikariDataSource.setConnectionTimeout(connectionTimeout);
            // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
            hikariDataSource.setIdleTimeout(idleTimeout);
            //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） -->
            hikariDataSource.setMaxLifetime(maxLifetime);
            // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
            hikariDataSource.setMaximumPoolSize(maximumPoolSize);
            hikariDataSource.setMinimumIdle(initPoolSize);
            datasources.put(datasourceName,hikariDataSource);
            logger.info("初始化Hikari连接池完成");

        } catch (Exception e) {
            logger.error("设置datasource各个属性值异常!" + e);
            throw e;
        }
    }


    /**
     * 取得数据库连接
     *
     * @return
     * @throws Exception
     */
    public static Connection getConnection(Map<String, Object> properties) {
        Connection connection = null;
        try {
            logger.info("当前线程号：" + Thread.currentThread().getId());
            String datasourceName = properties.get("source_id").toString() ;
            HikariDataSource hikariDataSource = datasources.get(datasourceName);
            if(hikariDataSource == null){

                dataSourceConfig(properties,datasourceName);
            }
            hikariDataSource = datasources.get(datasourceName);
            logger.info("获取数据库连接");
            connection = hikariDataSource.getConnection();
        } catch (Exception e) {
            logger.error("取得数据库连接时发生异常!" + e);
        }
        return connection;
    }

    /**
     * 取得数据库连接
     *
     * @return
     * @throws Exception
     */
    public static Connection getConnect(Map<String, Object> properties) {
        Connection connection = null;
        try {
            logger.info("当前线程号：" + Thread.currentThread().getId());
            readProperties(properties);
            HikariDataSource hikariDataSource = new HikariDataSource();
            //driverClassName无需指定，除非系统无法自动识别
            //private static String driverClassName="";
            hikariDataSource.setDriverClassName(driverClassName);
            //database address
            hikariDataSource.setJdbcUrl(jdbcUrl);
            //useName 用户名
            hikariDataSource.setUsername(username);
            //password
            hikariDataSource.setPassword(password);
            //连接只读数据库时配置为true， 保证安全 -->
            hikariDataSource.setReadOnly(readOnly);
            //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
            hikariDataSource.setConnectionTimeout(connectionTimeout);
            // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
            hikariDataSource.setIdleTimeout(idleTimeout);
            //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） -->
            hikariDataSource.setMaxLifetime(maxLifetime);
            // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
            hikariDataSource.setMaximumPoolSize(1);
            hikariDataSource.setMinimumIdle(1);
            connection = hikariDataSource.getConnection();
        } catch (Exception e) {
            logger.error("取得数据库连接时发生异常!" + e);
        }
        return connection;
    }

    /**
     * 释放数据库连接
     *
     * @param connection
     * @throws Exception
     */
    public static void freeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.info("释放数据库连接");
            } catch (Exception e) {
                logger.error("释放数据库连接时发生异常!" + e.getMessage());
            }
        }
    }

}
