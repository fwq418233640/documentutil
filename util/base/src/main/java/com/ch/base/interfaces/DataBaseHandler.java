package com.ch.base.interfaces;

import com.ch.base.domains.ColumnStructure;
import com.ch.base.domains.ConnectionInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据库处理接口
 */
public interface DataBaseHandler {

    /**
     * <p>获取数据源下所有表名称</p>
     */
    List<String> getTable(ConnectionInstance connectionInstance);

    Map<String, String> getTableNameAndCommmit(ConnectionInstance connectionInstance);

    /**
     * <p>获取数据源下所有表结构</p>
     */
    Map<String, List<ColumnStructure>> getTableStructure(ConnectionInstance connectionInstance);

    /**
     * <p>获取表结构</p>
     */
    Map<String, List<ColumnStructure>> getTableDesc(List<String> list, Connection connection) throws SQLException;

    /**
     * <p>测试连接</p>
     */
    boolean testConnection(ConnectionInstance connectionInstance);
}
