delete
======
delete用于执行delete语句。

本插件中所使用的CQL支持动态CQL语句，参考[cqlprocessor](cqlprocessor.md)。

delete必须在某个cassandra/cass语句之内，参见[cassandra/cass](Conn.md)。

### 实现模块

com.alogic.xscript.cassandra.Delete

### 配置参数

支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cql.Delete | 删除CQL语句 |

### 案例

下面案例从cassandra中删除指定用户.

```xml
 
    <script>
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
		
	<cass-conf>
		<cass connId="c1">	
			<delete 
				cql.Delete="
	 				DELETE FROM
                    	users
                    WHERE 
                    	username = #{bind('name')}
	 				    
	 			" />
		</cass>
	</cass-conf>
    </script>
```


