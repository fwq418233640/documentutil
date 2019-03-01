package com.ch.mysql.utils;

import base.domains.base.domains.base.ConnectionInstance;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>JDBC 工具类</p>
 *
 * @author Ch
 * @since 1.0
 */
@Data
public class JDBCUtil {

    private String url;

    private String driver;

    private String username;

    private String password;

    private static final Logger LOG = LoggerFactory.getLogger(JDBCUtil.class);

    public JDBCUtil(ConnectionInstance connectionInstance) throws ClassNotFoundException {
        this.url = connectionInstance.getUrl();
        this.driver = connectionInstance.getDriver();
        this.username = connectionInstance.getUsername();
        this.password = connectionInstance.getPassword();
        Class.forName(this.driver);
//        LOG.info("Get JDBCUtil instance");
//        LOG.info(this.toString());
    }

    public JDBCUtil(String url, String driver, String username, String password) throws ClassNotFoundException {
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
        Class.forName(this.driver);
//        LOG.info("Get JDBCUtil instance");
//        LOG.info(this.toString());
    }

    public static boolean tableExist(String tableName, Connection connection) {
        String sql = "select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='visod_t' and TABLE_NAME='"+tableName+"' ;";
        ResultSet query;
        try {
            query = query(sql, connection);
            return query.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        //LOG.info("Get Connection " + this.url);
        return connection;
    }

    /**
     * 执行sql
     *
     * @param sql        sql语句
     * @param params     参数
     * @param connection 数据库连接
     */
    public static void execSql(String sql, Object[] params, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //StringBuilder sbf = new StringBuilder(" params=[");
        for (int i = 0; params != null && i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
            //sbf.append(params[i]).append(",");
        }
        //sbf.append("]");
        //LOG.info("execute SQL = " + sql + sbf);
        preparedStatement.execute();
    }

    /**
     * 批处理
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     */
    public static void insertBatch(String[] sql,Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        for (String s : sql) {
            statement.addBatch(s);
        }
        statement.executeBatch();
    }

    /**
     * 批处理
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     */
    public static void insertBatch(List<String> sql,Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        for (String s : sql) {
            statement.addBatch(s);
        }
        statement.executeBatch();
    }

    /**
     * 执行 sql
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     */
    public static void execDDL(String sql, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        int i = statement.executeUpdate(sql);
        //LOG.info("execute = " + i + " SQL = " + sql);
    }

    /**
     * 执行查询语句
     *
     * @param sql        sql语句
     * @param params     参数
     * @param connection 数据库连接
     * @return ResultSet
     */
    public static ResultSet query(String sql, Object[] params, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        StringBuilder sbf = new StringBuilder(" params=[");
        for (int i = 0; params != null && i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
            sbf.append(params[i]);
        }
        sbf.append("]");
        //LOG.info("execute sql = " + sql + sbf);
        return preparedStatement.executeQuery();
    }


    /**
     * 执行查询语句
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @return ResultSet
     */
    public static ResultSet query(String sql,Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    /**
     * 关闭连接
     *
     * @param statement  statement
     * @param resultSet  resultSet
     * @param connection 数据库连接
     */
    public static void close(Statement statement, ResultSet resultSet, Connection connection) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //LOG.info("Connection close");
    }

    /**
     * 关闭连接
     *
     * @param statement  statement
     */
    public static void close(Statement statement) {
        close(statement,null,null);
    }

    /**
     * 关闭连接
     *
     * @param resultSet  resultSet
     */
    public static void close(ResultSet resultSet) {
        close(null,resultSet,null);
    }

    /**
     * 关闭连接
     *
     * @param connection 数据库连接
     */
    public static void close(Connection connection) {
         close(null,null,connection);
    }

    /**
     * <p>返回list结果集 但只能返回下表为1的列</p>
     *
     * @param sql        sql
     * @param params     参数
     * @param connection 连接
     */
    public static List<String> getList(String sql, Object[] params, Connection connection) {
        List<String> list = new ArrayList<>();

        try {
            ResultSet query = JDBCUtil.query(sql, params, connection);
            while (query.next()) {
                String string = query.getString(1);
                list.add(string);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "JDBCUtil{" +
                "url='" + url + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }
}
