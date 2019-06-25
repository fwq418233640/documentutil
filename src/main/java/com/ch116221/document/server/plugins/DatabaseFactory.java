package com.ch116221.document.server.plugins;


import com.ch116221.document.server.domains.ConnectionInstance;
import com.ch116221.document.server.enums.DatabaseType;
import com.ch116221.document.server.interfaces.DataBaseHandler;

/**
 * 数据库链接实现
 *
 * @author ch
 */
public class DatabaseFactory {

    private final static MySqlHandlerImpl MY_SQL_HANDLER = new MySqlHandlerImpl();


    /**
     * <p>获取对应数据处理Handler</p>
     *
     * @param type 处理器类型
     */
    public static DataBaseHandler getHandler(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return MY_SQL_HANDLER;
            case ORACLE:
            case SQLSERVER:
            default:
                throw new RuntimeException("未找到类型为 " + type + " 的 DataBaseHandler");
        }
    }

    /**
     * 设置URL
     *
     * @param connectionInstance 需要设置的链接
     */
    public static void setURL(ConnectionInstance connectionInstance) throws RuntimeException {
        DatabaseType type = connectionInstance.getType();
        switch (type) {
            case ORACLE:
            case SQLSERVER:
                throw new RuntimeException("未找到类型为 " + type + " 的 DataBaseHandler");
            case MYSQL:
                String url = connectionInstance.getUrl();
                String prefix = "jdbc:mysql://";
                String suffix = "?characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
                connectionInstance.setUrl(prefix + url + ":" + connectionInstance.getPort() + "/" + connectionInstance.getDatabaseName() + suffix);
                break;
        }
    }

    /**
     * 填充
     *
     * @param connectionInstance 需要填充的链接
     */
    public static void fill(ConnectionInstance connectionInstance) throws RuntimeException {
        DatabaseType type = connectionInstance.getType();
        switch (type) {
            case ORACLE:
                connectionInstance.setType(DatabaseType.ORACLE);
                throw new RuntimeException("未找到类型为 " + type + " 的 DataBaseHandler");
            case MYSQL:
                String url = connectionInstance.getUrl();
                String prefix = "jdbc:mysql://";
                String suffix = "?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
                connectionInstance.setUrl(prefix + url + ":" + connectionInstance.getPort() + "/" + connectionInstance.getDatabaseName() + suffix);
                connectionInstance.setDriver("com.mysql.cj.jdbc.Driver");
                connectionInstance.setType(DatabaseType.MYSQL);
                break;
            case SQLSERVER:
                connectionInstance.setType(DatabaseType.SQLSERVER);
                throw new RuntimeException("未找到类型为 " + type + " 的 DataBaseHandler");
        }
    }
}
