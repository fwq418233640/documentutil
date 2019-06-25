package com.ch116221.document.server.plugins;


import com.ch116221.document.server.domains.ColumnStructure;
import com.ch116221.document.server.domains.ConnectionInstance;
import com.ch116221.document.server.interfaces.DataBaseHandler;
import com.ch116221.document.server.utils.JDBCUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>Mysql处理器</p>
 *
 * @author Ch
 * @since 1.0
 */
@Data
@Slf4j
public class MySqlHandlerImpl implements DataBaseHandler {


    /**
     * 获取表结构信息
     *
     * @param connection 链接
     */
    @Override
    public Map<String, List<ColumnStructure>> getTableStructure(Connection connection, String databaseName) {
        if (connection == null) {
            return new HashMap<>();
        }

        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";

        List<String> source = JDBCUtil.getList(sql, new Object[]{databaseName}, connection);
        return getTableDesc(source, connection, databaseName);
    }

    /**
     * 获取所有表
     */
    @Override
    public List<String> getTable(Connection connection, String databaseName) {
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";
        try {
            return JDBCUtil.getList(sql, new Object[]{databaseName}, connection);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 获取表名称和注释
     *
     * @param connection 链接信息
     */
    @Override
    public Map<String, String> getTableNameAndCommit(Connection connection, String databaseName) {
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";
        try {
            return JDBCUtil.getTableNameAndComment(sql, new Object[]{databaseName},
                    connection, "TABLE_NAME", "TABLE_COMMENT");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取数据库链接
     */
    public Connection getConnection(ConnectionInstance instance) {
        try {
            JDBCUtil jdbcUtil = new JDBCUtil(instance);
            return jdbcUtil.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * <p>获取表结构信息</p>
     *
     * @param list       表名集合
     * @param connection 数据库连接信息
     */
    @Override
    public Map<String, List<ColumnStructure>> getTableDesc(List<String> list, Connection connection, String databaseName) {
        Map<String, List<ColumnStructure>> map = new LinkedHashMap<>();
        try {
            getColumns(list, connection, map, databaseName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * <p>获取列信息</p>
     *
     * @param list       表名称集合
     * @param connection 链接
     * @param map        返回值
     */
    private static void getColumns(List<String> list, Connection connection,
                                   Map<String, List<ColumnStructure>> map,
                                   String databaseName) throws SQLException {
        for (String str : list) {
            List<ColumnStructure> lis = new ArrayList<>();
            String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name ='" + str + "'" + " and TABLE_SCHEMA = '"
                    + databaseName + "'";
            ResultSet query = JDBCUtil.query(sql, null, connection);

            if (!query.next()) {
                log.error(str, " 表不存在!");
                return;
            }

            sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT from information_schema.columns where TABLE_NAME = ?"
                    + " and TABLE_SCHEMA = ?";

            ResultSet que = JDBCUtil.query(sql, new String[]{str, databaseName}, connection);
            while (que.next()) {
                String columnName = que.getString("COLUMN_NAME");
                String columnType = que.getString("COLUMN_TYPE");
                String nullable = que.getString("IS_NULLABLE");
                String columnComment = que.getString("COLUMN_COMMENT");

                ColumnStructure columnStructure = new ColumnStructure();
                columnStructure.setColumnName(columnName);
                columnStructure.setColumnType(columnType);
                columnStructure.setIsNullable(nullable);
                columnStructure.setColumnComment(columnComment);
                columnStructure.setTableName(str);
                lis.add(columnStructure);
            }
            map.put(str, lis);
        }
    }


    /**
     * <p>测试链接</p>
     *
     * @param instance 链接信息
     */
    @Override
    public boolean testConnection(ConnectionInstance instance) {
        Connection connection = null;
        try {
            instance.setUrl(getUrl(instance));
            instance.setDriver("com.mysql.cj.jdbc.Driver");
            JDBCUtil jdbcUtil = new JDBCUtil(instance);
            connection = jdbcUtil.getConnection();
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
        return true;
    }

    private static String getUrl(ConnectionInstance instance) {
        String url = instance.getUrl();
        String prefix = "jdbc:mysql://";
        String suffix = "?characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
        return prefix + url + ":" + instance.getPort() + "/" + instance.getDatabaseName() + suffix;
    }
}

