package com.ch.mysql.service;

import base.domains.base.domains.base.ConnectionInstance;
import base.domains.base.domains.base.Msg;
import com.ch.mysql.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlServer {




    /**
     * 获取当前数据库状态
     * */
    public Msg getStatus(ConnectionInstance connectionInstance) throws ClassNotFoundException, SQLException {

        JDBCUtil jdbcUtil  = new JDBCUtil(connectionInstance);
        Connection connection = jdbcUtil.getConnection();








        return Msg.fail();
    }













}
