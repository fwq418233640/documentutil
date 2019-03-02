# 数据库文档生成器

<a name="a4d3b02a"></a>
# 概述
这几天在写一些项目交付文档，其中就有数据库的相关文档，同事推荐了一款不错的工具 DBImport[链接](http://www.cyqdata.com/download/article-detail-42517) 但貌似只能在Windows 环境下使用,Linux 环境下出现了一下问题 (我用的是 Deppin发行版),然后变仿照这 DBImport 的思路实现了一个Java版本的。<br /><br /><br /><br />
<a name="a653042e"></a>
# 使用方式

```java

//	设置数据库
ConnectionInstance connectionInstance = new ConnectionInstance();
connectionInstance.setUrl("127.0.0.1");
connectionInstance.setDatabseName("visod");
connectionInstance.setType(DatabaseType.MYSQL);
connectionInstance.setPassword("root");
connectionInstance.setPort("3306");
connectionInstance.setUsername("root");


//	运行
public static void main(String[] args) throws NotFoundException, SQLException, IOException {
		DocumentService documentService = new DocumentService();
  	//	第一个参数参入 null 及代表导出所有表
		documentService.createDocument(null,connectionInstance);
}
```


<a name="b6e8224b"></a>
## 运行输出
![document.png](https://cdn.nlark.com/yuque/0/2019/png/265309/1551561110837-eb848164-f9ad-4bc9-855e-beecebcef06d.png#align=left&display=inline&height=196&name=document.png&originHeight=167&originWidth=636&size=6284&status=done&width=746)


相关测试代码在 com.ch.document.test.DocumentServicesTest 类中有详细例子


<a name="97c957fc"></a>
# 运行结果



![document2.png](https://cdn.nlark.com/yuque/0/2019/png/265309/1551561258053-50fa1f48-328d-4b25-bb94-ece1183edc4c.png#align=left&display=inline&height=500&name=document2.png&originHeight=785&originWidth=1172&size=32533&status=done&width=746)



![document3.png](https://cdn.nlark.com/yuque/0/2019/png/265309/1551561262679-213bf29f-69fb-4c8b-89e4-ab058cb3be3e.png#align=left&display=inline&height=299&name=document3.png&originHeight=464&originWidth=1158&size=24938&status=done&width=746)


<a name="138a6766"></a>
# 注意
目前仅进入测试阶段,另外还有一些样式上的问题


<a name="3528697a"></a>
# github地址
[https://github.com/fwq418233640/documentutil](https://github.com/fwq418233640/documentutil)

