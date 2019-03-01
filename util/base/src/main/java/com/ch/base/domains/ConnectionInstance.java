package com.ch.base.domains;

import com.ch.base.enums.DatabaseType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>连接信息</p>
 *
 * @author Ch
 * @since 1.0
 */
@Data
public class ConnectionInstance implements Serializable {

    private static final long serialVersionUID = 952444541190646934L;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * 数据库名称
     */
    private String databseName;

    /**
     * 驱动
     */
    private String driver;
    /**
     * 数据库类型
     */
    private DatabaseType type;

    /**
     * url
     */
    private String url;

    /**
     * 端口号
     */
    private String port;

}