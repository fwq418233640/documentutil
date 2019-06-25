package com.ch116221.document.server.utils;

import com.ch116221.document.server.domains.ConnectionInstance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>JDBC 工具类</p>
 *
 * @author Ch
 * @since 1.0
 */
@Slf4j
@Data
public class JDBCUtil {

    private String url;

    private String driver;

    private String username;

    private String password;

    public JDBCUtil(ConnectionInstance connectionInstance) throws ClassNotFoundException {
        this.url = connectionInstance.getUrl();
        this.driver = connectionInstance.getDriver();
        this.username = connectionInstance.getUsername();
        this.password = connectionInstance.getPassword();
        Class.forName(this.driver);
        log.info("Get JDBCUtil instance {}", this);
    }


    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
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
        log.debug("execute sql = {}", sql + sbf);
        return preparedStatement.executeQuery();
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
            log.error(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }


    /**
     * 关闭连接
     *
     * @param connection 数据库连接
     */
    public static void close(Connection connection) {
        close(null, null, connection);
    }

    /**
     * 获取表名称以及注释
     *
     * @param sql        sql
     * @param params     参数
     * @param connection 连接
     */
    public static Map<String, String> getTableNameAndComment(String sql, Object[] params, Connection connection, String key, String value) {
        Map<String, String> map = new HashMap<>();
        try {
            ResultSet query = JDBCUtil.query(sql, params, connection);
            while (query.next()) {
                map.put(query.getString(key), query.getString(value));
            }
            return map;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
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
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "JDBCUtil{" +
                "url='" + url + '\'' +
                ", driver='" + driver + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
