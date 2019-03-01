package com.ch.document.test;

import com.ch.base.Msg;
import com.ch.base.domains.ConnectionInstance;
import com.ch.base.enums.DatabaseType;
import com.ch.document.service.DocumentService;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.Test;

import java.util.List;

public class Test1 {


    @Test
    public void test1() throws NotFoundException {

        ConnectionInstance connectionInstance = new ConnectionInstance();
        connectionInstance.setUrl("127.0.0.1");
        connectionInstance.setDatabseName("visod");
        connectionInstance.setType(DatabaseType.MYSQL);
        connectionInstance.setPassword("root");
        connectionInstance.setPort("3306");
        connectionInstance.setUsername("root");

        DocumentService documentService = new DocumentService();
//        Msg<Map<String, String>> msg = documentService.getTableNameAndCommmit(connectionInstance);
//        Map<String, String> data = msg.getData();
//        data.forEach((k, v) -> System.out.println(k + " : " + v));


        Msg<List<String>> tables = documentService.getTables(connectionInstance);
        List<String> data1 = tables.getData();
        data1.forEach(System.out::println);
    }


    public static void main(String[] args) throws NotFoundException {
        new Test1().test1();
    }

}
