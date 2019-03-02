package com.ch.document.test;

import com.ch.base.Msg;
import com.ch.base.domains.ColumnStructure;
import com.ch.base.domains.ConnectionInstance;
import com.ch.base.enums.DatabaseType;
import com.ch.document.service.DocumentService;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DocumentServicesTest {

    private static ConnectionInstance connectionInstance;

    private static DocumentService documentService;

    static {
        connectionInstance = new ConnectionInstance();
        connectionInstance.setUrl("127.0.0.1");
        connectionInstance.setDatabseName("visod");
        connectionInstance.setType(DatabaseType.MYSQL);
        connectionInstance.setPassword("root");
        connectionInstance.setPort("3306");
        connectionInstance.setUsername("root");

        documentService = new DocumentService();
    }

    /**
     * 测试 获取所有表名称
     */
    @Test
    public void getTablesTest() throws NotFoundException {
        Msg<List<String>> tables = documentService.getTables(connectionInstance,true);
        List<String> data1 = tables.getData();
        data1.forEach(System.out::println);
    }

    /**
     * 测试获取所有表明以及 表注释
     */
    @Test
    public void getTableNameAndCommmitTest() throws NotFoundException {
        Msg<Map<String, String>> msg = documentService.getTableNameAndCommmit(connectionInstance,true);
        Map<String, String> data = msg.getData();
        data.forEach((k, v) -> System.out.println(k + " : " + v));
    }


    /**
     * 获取制定表的结构信息
     */
    @Test
    public void getTablesDescTest() throws NotFoundException, SQLException {
        Map<String, List<ColumnStructure>> tableDesc = documentService.getTableDesc(Arrays.asList("t_role","project_process"), connectionInstance,true);
        tableDesc.forEach((k, v) -> {
            for (ColumnStructure columnStructure : v) {
                System.out.println(k + " : " + columnStructure);
            }
        });

    }


    /**
     * 生成数据库文档
     * */
    public void createDocument() throws IOException, SQLException, NotFoundException {
        documentService.createDocument(null,connectionInstance);
    }


    public static void main(String[] args) throws NotFoundException, SQLException, IOException {
        DocumentServicesTest documentServicesTest = new DocumentServicesTest();
        //documentServicesTest.getTablesTest(connectionInstance);
//        documentServicesTest.getTableNameAndCommmitTest();
//        documentServicesTest.getTablesDescTest();
        documentServicesTest.createDocument();
    }

}
