package com.ch.document.service;

import com.ch.base.Msg;
import com.ch.base.domains.ConnectionInstance;
import com.ch.base.interfaces.DataBaseHandler;
import com.ch.document.plugins.DatabseFactory;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * 文档生成器主业务类
 *
 * @author ch
 */
public class DocumentService {

    /**
     * 获取所有表名称
     *
     * @param connectionInstance 链接名称
     */
    public Msg<List<String>> getTables(ConnectionInstance connectionInstance) throws NotFoundException {
        DataBaseHandler handler = DatabseFactory.getHandler(connectionInstance.getType());


        DatabseFactory.fill(connectionInstance);


        List<String> table = handler.getTable(connectionInstance);
        return Msg.sucess(table);
    }


    /**
     * 获取所有表名称
     *
     * @param connectionInstance 链接名称
     */
    public Msg<Map<String, String>> getTableNameAndCommmit(ConnectionInstance connectionInstance) throws NotFoundException {
        DataBaseHandler handler = DatabseFactory.getHandler(connectionInstance.getType());


        DatabseFactory.fill(connectionInstance);


        Map<String, String> table = handler.getTableNameAndCommmit(connectionInstance);
        return Msg.sucess(table);
    }

}
