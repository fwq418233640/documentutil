package com.ch116221.document.server.services;

import com.ch116221.document.server.domains.ColumnStructure;
import com.ch116221.document.server.domains.ConnectionInstance;
import com.ch116221.document.server.enums.DatabaseType;
import com.ch116221.document.server.interfaces.DataBaseHandler;
import com.ch116221.document.server.plugins.DatabaseFactory;
import com.ch116221.document.server.utils.FileUtil;
import com.ch116221.document.server.utils.JDBCUtil;
import com.ch116221.document.server.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 文档生成器主业务类
 *
 * @author ch
 */
@Slf4j
@Service
public class DocumentService {

    /**
     * 获取所有表名称
     *
     * @param instance 链接名称
     */
    public Result<List<String>> getTables(Connection instance, DatabaseType type, String databaseName) throws RuntimeException {
        DataBaseHandler handler = DatabaseFactory.getHandler(type);
        List<String> table = handler.getTable(instance, databaseName);
        return Result.success(table);
    }


    /**
     * 获取所有表名称及注释信息
     *
     * @param instance 链接名称
     */
    public Result<Map<String, String>> getTableNameAndCommit(ConnectionInstance instance, boolean fill) throws RuntimeException {
        DataBaseHandler handler = DatabaseFactory.getHandler(instance.getType());
        if (fill) {
            DatabaseFactory.fill(instance);
        }
        Connection connection = null;
        Map<String, String> table;
        try {
            connection = handler.getConnection(instance);
            table = handler.getTableNameAndCommit(connection, instance.getDatabaseName());
        } finally {
            JDBCUtil.close(connection);
        }
        return Result.success(table);
    }

    /**
     * 获取所有表名称及注释信息
     *
     * @param instance 链接名称
     */
    public Result<Map<String, String>> getTableNameAndCommit(Connection instance, DatabaseType type, String databaseName) throws RuntimeException {
        DataBaseHandler handler = DatabaseFactory.getHandler(type);
        Map<String, String> table = handler.getTableNameAndCommit(instance, databaseName);
        return Result.success(table);
    }

    /**
     * 获取制定表的结构信息
     *
     * @param instance 链接信息
     * @param tables   表名
     */
    public Map<String, List<ColumnStructure>> getTableDesc(List<String> tables, Connection instance, DatabaseType type, String databaseName)
            throws RuntimeException, SQLException {
        DataBaseHandler handler = DatabaseFactory.getHandler(type);
        return handler.getTableDesc(tables, instance, databaseName);
    }

    /**
     * 生成文档
     *
     * @param tables   表名 null 及代表所有
     * @param instance 数据库链接信息
     */
    public Result<String> createDocument(List<String> tables, ConnectionInstance instance) {
        String databaseName = instance.getDatabaseName();
        Connection connection = this.getConnection(instance, true);

        if (tables == null || tables.isEmpty()) {
            Result<List<String>> data = this.getTables(connection, instance.getType(), databaseName);
            tables = data.getData();
        }

        File file = new File(FileUtil.TEMP_DIR + File.separator + instance.getDatabaseName() + "-数据库设计文档.html");

        Result<Map<String, String>> result = this.getTableNameAndCommit(connection, instance.getType(), databaseName);
        Map<String, List<ColumnStructure>> tableDesc;
        try {
            tableDesc = this.getTableDesc(tables, connection, instance.getType(), databaseName);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            generator(result.getData(), tableDesc, instance.getDatabaseName(), writer);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.fail(e.getMessage());
        }

        JDBCUtil.close(connection);
        log.info("文件生成路径: {}", file.getAbsoluteFile());
        return Result.success(file.getName());
    }


    private Connection getConnection(ConnectionInstance instance, boolean fill) {
        DataBaseHandler handler = DatabaseFactory.getHandler(instance.getType());
        if (fill) {
            DatabaseFactory.fill(instance);
        }
        return handler.getConnection(instance);

    }

    /**
     * 生成html 文件
     *
     * @param databaseName       数据库名称
     * @param desc               数据库表详细信息
     * @param stream             输出流
     * @param tableNameAndCommit 表名与注释
     */
    private void generator(Map<String, String> tableNameAndCommit, Map<String, List<ColumnStructure>> desc,
                           String databaseName, BufferedWriter stream) throws IOException {

        StringBuilder body = new StringBuilder();

        //  生成固定数据
        body.append("<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <title>数据库设计文档</title>\n"
                        + "\n" + "    <style>\n"
                        + "        body {\n" + "            text-align: center;\n"
                        + "        }\n" + "\n" + "        .main {\n"
                        + "            margin: 0 auto;\n" + "            width: 60%;\n"
                        + "        }\n" + "\n" + "        table {\n"
                        + "            font-family: verdana, arial, sans-serif;\n"
                        + "            font-size: 11px;\n" + "            color: #333333;\n"
                        + "            border-width: 1px;\n"
                        + "            border-color: #666666;\n"
                        + "            border-collapse: collapse;\n" + "            margin: 40px 0px;\n"
                        + "        }\n" + "\n" + "        table th {\n" + "            border-width: 1px;\n"
                        + "            padding: 8px;\n" + "            border-style: solid;\n"
                        + "            border-color: #666666;\n" + "            background-color: #dedede;\n"
                        + "        }\n" + "\n" + "        table td {\n" + "            border-width: 1px;\n"
                        + "            padding: 8px;\n" + "            border-style: solid;\n"
                        + "            border-color: #666666;\n" + "            background-color: #ffffff;\n"
                        + "            width: 400px;\n" + "        }\n" + "    </style>\n" + "</head>\n"
                        + "<body>\n" + "<div class=\"main\">\n" + "    <h1>数据库设计文档</h1>\n"
                        + "    <hr/>\n" + "    <p style=\"text-align: left\">数据库名称 : <a name=\"")
                .append(databaseName)
                .append("\">")
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
        Iterator<Map.Entry<String, String>> iterator = tableNameAndCommit.entrySet().iterator();
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
        for (Map.Entry<String, List<ColumnStructure>> next : desc.entrySet()) {
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

    public Result<?> test(ConnectionInstance instance) {
        DataBaseHandler handler = DatabaseFactory.getHandler(instance.getType());
        boolean b = handler.testConnection(instance);
        return Result.success(b);
    }
}
