package com.ch.mysql.controller;


import base.domains.base.domains.base.Msg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MySql api
 */
@RestController
@RequestMapping("/mysql")
public class MysqlController {


    /**
     * 获取当前数据库已经建立的链接信息
     */
    @GetMapping
    public Msg getConnectionInfo() {


        return null;
    }

}
