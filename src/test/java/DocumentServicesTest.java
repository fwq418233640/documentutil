
import com.ch116221.document.server.services.DocumentService;
import com.ch116221.document.server.utils.Result;
import com.ch116221.document.server.domains.ColumnStructure;
import com.ch116221.document.server.domains.ConnectionInstance;
import com.ch116221.document.server.enums.DatabaseType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DocumentServicesTest {

    private static ConnectionInstance connectionInstance;

    private static DocumentService documentService;

    static {
        connectionInstance = new ConnectionInstance();
        connectionInstance.setUrl("192.168.8.26");
        connectionInstance.setDatabaseName("data_cloud");
        connectionInstance.setType(DatabaseType.MYSQL);
        connectionInstance.setPassword("root");
        connectionInstance.setPort("3306");
        connectionInstance.setUsername("root");
        documentService = new DocumentService();
    }

//    /**
//     * 测试 获取所有表名称
//     */
//    public void getTablesTest() throws RuntimeException {
//        Result<List<String>> tables = documentService.getTables(connectionInstance,true);
//        List<String> data1 = tables.getData();
//        data1.forEach(System.out::println);
//    }
//
//    /**
//     * 测试获取所有表明以及 表注释
//     */
//    public void getTableNameAndCommitTest() throws RuntimeException {
//        Result<Map<String, String>> result = documentService.getTableNameAndCommit(connectionInstance,true);
//        Map<String, String> data = result.getData();
//        data.forEach((k, v) -> System.out.println(k + " : " + v));
//    }
//
//
//    /**
//     * 获取制定表的结构信息
//     */
//    public void getTablesDescTest() throws RuntimeException, SQLException {
//        Map<String, List<ColumnStructure>> tableDesc = documentService.getTableDesc(Arrays.asList("t_role","project_process"), connectionInstance,true);
//        tableDesc.forEach((k, v) -> {
//            for (ColumnStructure columnStructure : v) {
//                System.out.println(k + " : " + columnStructure);
//            }
//        });
//
//    }
//
//
//    /**
//     * 生成数据库文档
//     * */
//    public void createDocument() throws IOException, SQLException, RuntimeException {
//        documentService.createDocument(null,connectionInstance);
//    }


    public static void main(String[] args) throws RuntimeException, SQLException, IOException {
        DocumentServicesTest documentServicesTest = new DocumentServicesTest();
//        documentServicesTest.getTablesTest();
//        documentServicesTest.getTableNameAndCommmitTest();
//        documentServicesTest.getTablesDescTest();
//        documentServicesTest.createDocument();
    }

}
