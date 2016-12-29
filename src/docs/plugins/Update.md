update
======
update用于执行update语句。

本插件中所使用的CQL支持动态CQL语句，参考[Preprocessor](Preprocessor.md)。

update必须在某个cassandra/cass语句之内，参见[cassandra/cass](Conn.md)。

### 实现模块

com.alogic.xscript.cassandra.Update

### 配置参数

支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cql.Update | 更新CQL语句 |

### 案例

下面案例从cassandra中更新用户信息。

####更新记录

```xml
	
    <script>
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
	<cass-conf>
		<cass connId="c1">	
			<update 
				cql.Update="
	 				 UPDATE 
                    	users
                     set 
                        #{not_nvl(password,'password=' + bind('password'))}
                    WHERE 
                    	username = #{bind('name')}
	 			" />
		</cass>
	</cass-conf>
    </script>
```

####新建表

```xml

	<script>
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
	<cass-conf>
		<cassandra connId="c1">	
			<update 
				cql.Update="
	 				 CREATE TABLE IF NOT EXISTS 
	 				 simplex.songs (
	 				    id uuid PRIMARY KEY,
	 				    title text,
	 				    album text,
	 				    tags set &lt;text&gt;,
	 				    data blob
	 				 )" />
		</cassandra>
	</cass-conf>
</script>
```

####新建keyspace

```xml

	<script>
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />	
	<cass-conf>
		<cass connId="c1">	
			<update 
				cql.Update="
	 				 CREATE KEYSPACE IF NOT EXISTS
	 				      simplex
	 				 WITH 
	 				      replication = {
	 				         'class' : 'SimpleStrategy',
	 				         'replication_factor' : 1
	 				         }     
	 			" />
		</cass>
	</cass-conf>
    </script>
```
