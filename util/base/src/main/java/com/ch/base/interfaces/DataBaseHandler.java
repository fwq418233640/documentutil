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
     * 获取数据源下所有表名称
     */
    List<String> getTable(ConnectionInstance connectionInstance);
    
    /**
     * 获取数据源下所有表名称以及注释信息
     * */
    Map<String, String> getTableNameAndCommmit(ConnectionInstance connectionInstance);

    /**
     * 获取数据源下所有表结构
     */
    Map<String, List<ColumnStructure>> getTableStructure(ConnectionInstance connectionInstance);

    /**
     * 获取表结构
     */
    Map<String, List<ColumnStructure>> getTableDesc(List<String> list, ConnectionInstance connectionInstance) throws SQLException;

    /**
     * 测试连接
     */
    boolean testConnection(ConnectionInstance connectionInstance);
}
