package com.ch.document.plugins;

import com.ch.base.domains.ConnectionInstance;
import com.ch.base.enums.DatabaseType;
import com.ch.base.interfaces.DataBaseHandler;
import com.ch.utils.SpringUtil;
import org.apache.ibatis.javassist.NotFoundException;

/**
 * 数据库链接实现
 *
 * @author ch
 */
public class DatabseFactory {
    /**
     * <p>获取对应数据处理Handler</p>
     *
     * @param type 处理器类型
     */
    public static DataBaseHandler getHandler(DatabaseType type) throws NotFoundException {
        switch (type) {
            case MYSQL:
//                return SpringUtil.getBean(MySqlHandlerImpl.class);
                return new MySqlHandlerImpl();
            case ORACLE:
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
            case SQLSERVER:
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
            default:
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
        }
    }

    /**
     * 设置URL
     *
     * @param connectionInstance 需要设置的链接
     */
    public static void setURL(ConnectionInstance connectionInstance) throws NotFoundException {
        DatabaseType type = connectionInstance.getType();
        switch (type) {
            case ORACLE:
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
            case MYSQL:
                String url = connectionInstance.getUrl();
                String prefix = "jdbc:mysql://";
                String suffix = "?characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
                connectionInstance.setUrl(prefix + url + ":" + connectionInstance.getPort() + "/" + connectionInstance.getDatabseName() + suffix);
                break;
            case SQLSERVER:
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
        }
    }

    /**
     * 填充
     *
     * @param connectionInstance 需要填充的链接
     */
    public static void fill(ConnectionInstance connectionInstance) throws NotFoundException {
        DatabaseType type = connectionInstance.getType();
        switch (type) {
            case ORACLE:
                connectionInstance.setType(DatabaseType.ORACLE);
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
            case MYSQL:
                String url = connectionInstance.getUrl();
                String prefix = "jdbc:mysql://";
                String suffix = "?characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
                connectionInstance.setUrl(prefix + url + ":" + connectionInstance.getPort() + "/" + connectionInstance.getDatabseName() + suffix);
                connectionInstance.setDriver("com.mysql.jdbc.Driver");
                connectionInstance.setType(DatabaseType.MYSQL);
                break;
            case SQLSERVER:
                connectionInstance.setType(DatabaseType.SQLSERVER);
                throw new NotFoundException("未找到类型为 " + type + " 的 DataBaseHandler");
        }
    }
}
