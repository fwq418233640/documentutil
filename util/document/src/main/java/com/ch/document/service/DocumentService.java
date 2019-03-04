package com.ch.document.service;

import com.ch.base.Msg;
import com.ch.base.domains.ColumnStructure;
import com.ch.base.domains.ConnectionInstance;
import com.ch.base.interfaces.DataBaseHandler;
import com.ch.document.plugins.DatabseFactory;
import org.apache.ibatis.javassist.NotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
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
    public Msg<List<String>> getTables(ConnectionInstance connectionInstance, boolean fill) throws NotFoundException {
        DataBaseHandler handler = DatabseFactory.getHandler(connectionInstance.getType());
        if (fill) {
            DatabseFactory.fill(connectionInstance);
        }
        List<String> table = handler.getTable(connectionInstance);
        return Msg.sucess(table);
    }


    /**
     * 获取所有表名称及注释信息
     *
     * @param connectionInstance 链接名称
     */
    public Msg<Map<String, String>> getTableNameAndCommmit(ConnectionInstance connectionInstance, boolean fill) throws NotFoundException {
        DataBaseHandler handler = DatabseFactory.getHandler(connectionInstance.getType());
        if (fill) {
            DatabseFactory.fill(connectionInstance);
        }
        Map<String, String> table = handler.getTableNameAndCommmit(connectionInstance);
        return Msg.sucess(table);
    }

    /**
     * 获取制定表的结构信息
     *
     * @param connectionInstance 链接信息
     * @param tables             表名
     */
    public Map<String, List<ColumnStructure>> getTableDesc(List<String> tables, ConnectionInstance connectionInstance, boolean fill) throws NotFoundException, SQLException {
        DataBaseHandler handler = DatabseFactory.getHandler(connectionInstance.getType());
        if (fill) {
            DatabseFactory.fill(connectionInstance);
        }
        return handler.getTableDesc(tables, connectionInstance);
    }

    /**
     * 生成文档
     *
     * @param tables             表名 null 及代表所有
     * @param connectionInstance 数据库链接信息
     */
    public void createDocument(List<String> tables, ConnectionInstance connectionInstance) throws NotFoundException, SQLException, IOException {

        if (tables == null) {
            Msg<List<String>> data = this.getTables(connectionInstance, true);
             tables = data.getData();
        }

        String tempdir = System.getProperty("java.io.tmpdir");
        File file = new File(tempdir + File.separator + connectionInstance.getDatabseName() + "数据库设计文档.html");


        Msg<Map<String, String>> msg = getTableNameAndCommmit(connectionInstance, false);
        Map<String, List<ColumnStructure>> tableDesc = getTableDesc(tables, connectionInstance, false);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            generator(msg.getData(), tableDesc, connectionInstance.getDatabseName(), writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }


        System.out.println("文件生成路径: " + file.getAbsoluteFile());
    }

    /**
     * 生成html 文件
     *
     * @param databaseName        数据库名称
     * @param desc                数据库表详细信息
     * @param stream              输出流
     * @param tableNameAndCommmit 表名与注释
     */
    private void generator(Map<String, String> tableNameAndCommmit, Map<String, List<ColumnStructure>> desc,
                           String databaseName, BufferedWriter stream) throws IOException {

        StringBuilder body = new StringBuilder();

        //  生成固定数据
        body.append("<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <title>数据库设计文档</title>\n"
                + "\n"
                + "    <style>\n"
                + "        body {\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "\n"
                + "        .main {\n"
                + "            margin: 0 auto;\n"
                + "            width: 60%;\n"
                + "        }\n"
                + "\n"
                + "        table {\n"
                + "            font-family: verdana, arial, sans-serif;\n"
                + "            font-size: 11px;\n"
                + "            color: #333333;\n"
                + "            border-width: 1px;\n"
                + "            border-color: #666666;\n"
                + "            border-collapse: collapse;\n"
                + "            margin: 40px 0px;\n"
                + "        }\n"
                + "\n"
                + "        table th {\n"
                + "            border-width: 1px;\n"
                + "            padding: 8px;\n"
                + "            border-style: solid;\n"
                + "            border-color: #666666;\n"
                + "            background-color: #dedede;\n"
                + "        }\n"
                + "\n"
                + "        table td {\n"
                + "            border-width: 1px;\n"
                + "            padding: 8px;\n"
                + "            border-style: solid;\n"
                + "            border-color: #666666;\n"
                + "            background-color: #ffffff;\n"
                + "            width: 400px;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "<div class=\"main\">\n"
                + "    <h1>数据库设计文档</h1>\n"
                + "    <hr/>\n"
                + "    <p style=\"text-align: left\">数据库名称 : <a name=\"" + databaseName + "\">")
                .append(databaseName)
                .append("</a></p>\n")
                .append("    <table>\n")
                .append("        <tbody>\n")
                .append("        <tr>\n")
                .append("            <th style=\"width: 10%;\">序号\t</th>\n")
                .append("            <th>表名\t</th>\n")
                .append("            <th>注释</th>\n")
                .append("        </tr>\n");


        //  生成目录
        Iterator<Map.Entry<String, String>> iterator = tableNameAndCommmit.entrySet().iterator();
        int index = 1;
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();

            String key = next.getKey();
            String value = next.getValue();

            body.append("        <tr>\n")
                    .append("            <td>").append(index++).append("</td>\n")
                    .append("            <td><a href=\"#").append(key).append("\">").append(key).append("</a></td>\n")
                    .append("            <td>").append(value).append("</td>\n")
                    .append("        </tr>\n");
        }
        body.append("        </tbody>\n")
                .append("    </table>\n");


        //  生成详细表
        Iterator<Map.Entry<String, List<ColumnStructure>>> desc_iterator = desc.entrySet().iterator();
        while (desc_iterator.hasNext()) {

            Map.Entry<String, List<ColumnStructure>> next = desc_iterator.next();
            String key = next.getKey();
            List<ColumnStructure> value = next.getValue();


            int dataIndex = 1;
            body.append("<p style=\"text-align: left;display:inline;\">表名称 : <a name=\"").append(key).append("\">").append(key).append("</a></p>")
                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append("<p style=\"text-align: right;display:inline;\"><a href=\"#").append(databaseName).append("\">返回目录</a></p>\n")
                    .append("    <table>\n")
                    .append("        <tr>\n")
                    .append("            <th style=\"width: 10%;\">序号</th>\n")
                    .append("            <th>名称</th>\n")
                    .append("            <th>数据类型</th>\n")
                    .append("            <th>允许空值</th>\n")
                    .append("            <th>说明</th>\n")
                    .append("        </tr>\n");

            for (ColumnStructure c : value) {
                body.append("        <tr>\n")
                        .append("            <td>").append(dataIndex++).append("</td>\n")
                        .append("            <td>").append(c.getColumnName()).append("</td>\n")
                        .append("            <td>").append(c.getColumnType()).append("</td>\n")
                        .append("            <td>").append(c.getIsNullable()).append("</td>\n")
                        .append("            <td>").append(c.getColumnComment()).append("</td>\n")
                        .append("        </tr>\n");
            }

            body.append("    </table>\n");
        }

        //  生成详细表
        body.append("</div>\n")
                .append("</body>\n")
                .append("</html>");

        stream.write(body.toString());
    }
}
