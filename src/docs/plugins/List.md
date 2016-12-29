list
====
list用于在Cassandra数据库中查询出多行记录，并以数组的形式输出到文档。

本插件中所使用的CQL支持动态CQL语句，参考[cqlprocessor](cqlprocessor.md)。

list必须在某个cassandra/cass语句之内，参见[cassandra/cass](Conn.md)。

### 实现模块

com.alogic.xscript.cassandra.ListAll

### 配置参数

支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cql.Query | 查询CQL语句 |
| 2 | tag | 输出对象的tag，支持变量计算 | 
| 3 | limit | 输出的最大记录数 | 
| 4 | offset | 记录的偏移量 | 
| 5 | fetchSize | 每次从cassandra返回的记录数 |

> 说明：其中limit和offset的概念与其他的关系型数据库一样，而fetchSize表示cassandra每次要从返回多少记录。由于CQL本身是不支持分页操作的，因为这种操作通常效率比较慢，但是有时候我们确实需要这种功能。官方推荐的做法是：比方你有20页结果，每页10条，当用户查询指定页比如第12页时，你的fetch size就不要每次只读10条了，可以考虑每次读50条：
第一次读取1-50
丢弃，继续读51-100
丢弃，继续读101-150，取其中的121-130，丢弃其他的
这个50的fetch size你得考虑好，不能太大也不能太小，多试几种找到最佳实践。

### 案例

下面案例从cassandra中查询出用户列表。

```xml

	<script>
	
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
		
	<cass-conf>
		<cass connId="c1">	
			<list tag="data"
				cql.Query="
	 				SELECT
						username as name,
						password as pw,
						playlist_names as list
	 				FROM
	 					users	
	 				#{not_nvl(keyword,'WHERE (
	 					      username=' +bind('keyword') +')'
	 				    )}"
	 			 limit="10" offset="0" fetchSize="10"/>
		</cass>
	</cass-conf>
    </script>
```


