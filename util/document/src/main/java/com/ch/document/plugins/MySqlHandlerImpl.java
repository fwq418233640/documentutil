package com.ch.document.plugins;


import com.ch.base.Msg;
import com.ch.base.domains.ColumnStructure;
import com.ch.base.domains.ConnectionInstance;
import com.ch.base.enums.ColumnType;
import com.ch.base.interfaces.DataBaseHandler;
import com.ch.utils.JDBCUtil;
import com.ch.utils.MessyUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Mysql处理器</p>
 *
 * @author Ch
 * @since 1.0
 */
@Data
@Component
public class MySqlHandlerImpl implements DataBaseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MySqlHandlerImpl.class);

    /**
     * 获取表结构信息
     *
     * @param connectionInstance 链接
     */
    @Override
    public Map<String, List<ColumnStructure>> getTableStructure(ConnectionInstance connectionInstance) {
        if (connectionInstance == null) return null;
        Connection connection = getConnection(connectionInstance);
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";

        List<String> source = JDBCUtil.getList(sql, new Object[]{connectionInstance.getDatabseName()}, connection);
        try {
            return getTableDesc(source, connection);
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            JDBCUtil.close(connection);
        }
        return null;
    }

    /**
     * 获取所有表
     * */
    @Override
    public List<String> getTable(ConnectionInstance connectionInstance) {
        Connection connection = getConnection(connectionInstance);
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";
        try {
            return JDBCUtil.getList(sql, new Object[]{connectionInstance.getDatabseName()}, connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(connection);
        }
        return null;
    }


    /**
     * 获取表信息
     * */
    public Msg getTableInfo(ConnectionInstance connectionInstance,String tableName){
        Connection connection = getConnection(connectionInstance);
        String sql = "select COLUMN_NAME,COLUMN_TYPE,COLUMN_KEY,COLUMN_COMMENT from information_schema.columns where table_schema =?  and table_name =?";
        try {
            ResultSet query = JDBCUtil.query(sql, new Object[]{connectionInstance.getDatabseName(), tableName}, connection);

            while (query.next()){
                String column_name = query.getString("COLUMN_NAME");
                String column_type = query.getString("COLUMN_TYPE");
                String column_key = query.getString("COLUMN_KEY");
                String column_comment = query.getString("COLUMN_COMMENT");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(connection);
        }

        return null;
    }

    /**
     * 获取表名称和注释
     *
     * @param connectionInstance 链接信息
     */
    @Override
    public Map<String, String> getTableNameAndCommmit(ConnectionInstance connectionInstance) {
        Connection connection = getConnection(connectionInstance);
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";
        try {
            return JDBCUtil.getTableNameAndComment(sql, new Object[]{connectionInstance.getDatabseName()}, connection, "TABLE_NAME", "TABLE_COMMENT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(connection);
        }
        return null;
    }

    /**
     * 获取数据库链接
     */
    private static Connection getConnection(ConnectionInstance connectionInstance) {
        try {
            JDBCUtil jdbcUtil = new JDBCUtil(connectionInstance);
            return jdbcUtil.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>获取表结构信息</p>
     *
     * @param list       表名集合
     * @param connection 数据库连接
     */
    @Override
    public Map<String, List<ColumnStructure>> getTableDesc(List<String> list, Connection connection) throws SQLException {
        Map<String, List<ColumnStructure>> map = new LinkedHashMap<>();
        getColumns(list, connection, map);
        return map;
    }

    /**
     * <p>获取列信息</p>
     *
     * @param list       表名称集合
     * @param connection 链接
     * @param map        返回值
     */
    private static void getColumns(List<String> list, Connection connection, Map<String, List<ColumnStructure>> map) throws SQLException {
        for (String str : list) {
            List<ColumnStructure> lis = new ArrayList<>();

            String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name ='" + str + "'";
            ResultSet query = JDBCUtil.query(sql, null, connection);

            if (!query.next()) {
                LOG.error(str + " 表不存在！MysqlHandlerImpl.getColumns() 138 line.");
                return;
            }

            ResultSet que = JDBCUtil.query("desc " + str, null, connection);
            ResultSetMetaData metaData = que.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (que.next()) {
                ColumnStructure columnStructure = new ColumnStructure();
                for (int i = 1; i < columnCount; i++) {
                    String key = metaData.getColumnName(i);
                    Object val = que.getObject(i);
                    selectColumnType(columnStructure, key, val);
                    columnStructure.setTableName(str);
                }
                columnStructure.setUserColumnName(columnStructure.getColumnName());
                columnStructure.setColumnName(columnStructure.getColumnName());
                lis.add(columnStructure);
            }
            map.put(str, lis);
        }
    }

    private static void selectColumnType(ColumnStructure columnStructure, String key, Object val) {
        switchColumnType(columnStructure, key, val);
    }

    /**
     * <p>设置列属性</p>
     */
    private static void switchColumnType(ColumnStructure columnStructure, String key, Object val) {
        switch (key) {
            case ColumnType.COLUMN_NAME:
                columnStructure.setColumnName(MessyUtil.getStringVal(val));
                break;
            case ColumnType.COLUMN_TYPE:
                columnStructure.setColumnType(MessyUtil.getStringVal(val));
                break;
            case ColumnType.COLUMN_KEY:
                columnStructure.setColumnKey(MessyUtil.getStringVal(val));
                break;
            case ColumnType.IS_NULLABLE:
                columnStructure.setIsNullable(MessyUtil.getStringVal(val));
                break;
            default:
                break;
        }
    }

    /**
     * <p>测试链接</p>
     *
     * @param connectionInstance 链接信息
     */
    @Override
    public boolean testConnection(ConnectionInstance connectionInstance) {

        Connection connection = null;
        try {
            connectionInstance.setUrl(getUrl(connectionInstance));
            connectionInstance.setDriver("com.mysql.jdbc.Driver");
            JDBCUtil jdbcUtil = new JDBCUtil(connectionInstance);
            connection = jdbcUtil.getConnection();
        } catch (Exception e) {
            LOG.error(e.toString());
            return false;
        } finally {
            JDBCUtil.close(connection);
        }

        return true;
    }

    private static String getUrl(ConnectionInstance connectionInstance) {
        String url = connectionInstance.getUrl();
        String prefix = "jdbc:mysql://";
        String suffix = "?characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
        return prefix + url + ":" + connectionInstance.getPort() + "/" + connectionInstance.getDatabseName() + suffix;
    }
}

