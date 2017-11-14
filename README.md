# 将表数据导出xml
公司临时有个需求要求有个工具能够导出表的数据，输出格式为xml。</br>
于是用了一小时写了个工具类。

## 一、创建 元数据表
存储 表和字段之间的关系，和字段描述
```sql
CREATE TABLE [dbo].[TABLE_INFO](
	[tableName] [nvarchar](50) NULL,
	[columnName] [nvarchar](50) NULL,
	[columnDesc] [nvarchar](50) NULL
) 

```
### 元数据表中的测试数据
```sql
tableName	columnName	columnDesc
A01	A0101	姓名
A01	A0000	ID
A02	A0200	职务组件
A02	A0000	人员主键
```

## 二、模版文件
template.txt 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<table>

  <#list rowsList as rowInfoList>
  <row type="add">
	<#list rowInfoList as columnInfo>    
	  <${columnInfo.columnName} name="${columnInfo.columnDesc}" isattachment="false">${columnInfo.columnValue}</${columnInfo.columnName}>
	</#list> 
  </row>
  </#list> 
</table>
```

## 三、Constant.java 常量类
>* 数据库配置
>* 模版位置和模版名称
>* 文件输出位置

```java
package com.rongji.dbdata;

public class Constant {
	public static final String TEMPLATEPATH = "F:\\zwhjTemplate";
	public static final String TEMPNAME = "template.txt";
	public static final String OUTPUT_FILE_PATH = "F:\\zwhjTemplate\\";
	
	public static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// 加载驱动程序
	public static final String url = "jdbc:sqlserver://localhost:1433; DatabaseName=CMIS_TEST";// 设置数据库连接串
	public static final String user = "sa";// 数据库登录用户名
	public static final String password = "123456";// 数据库登录密码
}

```

## 四、如何使用
CreateXML.java

String tableName = "TableName" 指定要导出的表数据。
右键-run

```java
public static void main(String[] args) {

		// TODO 指定表面
		String tableName = "TableName";
		
		try {
			// 配置模版
			Configuration configuration = buildConfiguration();
			Template template = configuration.getTemplate(Constant.TEMPNAME);

			RowInfoUtils rowInfoUtils = new RowInfoUtils();
			// 构造数据 根据表名
			List<List<ColumnInfo>> rowsList = rowInfoUtils.getRowInfo(tableName);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("rowsList", rowsList);
			// 生成文件
			outputFile(template, paramMap, tableName + ".xml");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (TemplateException e) {
			LOG.error(e.getMessage(), e);
		}
	}
```

## 五、生成的结果

```xml
<?xml version="1.0" encoding="UTF-8"?>
<table>

    <row type="add">
	  <A0101 name="姓名" isattachment="false">"企业五"</A0101>
	  <A0000 name="ID" isattachment="false">"004b8bc07cf34ba3ae752f34192f4ced"</A0000>
  </row>
  <row type="add">
	  <A0101 name="姓名" isattachment="false">"企业六"</A0101>
	  <A0000 name="ID" isattachment="false">"00e500a1f62c4cc499d7251523d67e75"</A0000>
  </row>
  
</table>
```

### 六、开发工具
>* eclipse
>* jdk1.7
>* freeMarker
>* 数据库 sqlserver

note: 如果要使用其他数据库需要导入对应的Jar


