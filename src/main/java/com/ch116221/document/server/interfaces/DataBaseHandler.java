package com.ch116221.document.server.interfaces;

import com.ch116221.document.server.domains.ColumnStructure;
import com.ch116221.document.server.domains.ConnectionInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据库处理接口
 */
public interface DataBaseHandler {

    /**
     * 获取数据源下所有表名称
     */
    List<String> getTable(Connection connection,String databaseName);

    /**
     * 获取数据源下所有表名称以及注释信息
     * */
    Map<String, String> getTableNameAndCommit(Connection connection,String databaseName);

    /**
     * 获取数据源下所有表结构
     */
    Map<String, List<ColumnStructure>> getTableStructure(Connection connection,String databaseName);

    /**
     * 获取表结构
     */
    Map<String, List<ColumnStructure>> getTableDesc(List<String> list, Connection connection,String databaseName) throws SQLException;

    /**
     * 测试连接
     */
    boolean testConnection(ConnectionInstance connectionInstance);

    Connection getConnection(ConnectionInstance connectionInstance);
}
