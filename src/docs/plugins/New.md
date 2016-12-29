new
===
new用于执行insert语句。

本插件中所使用的CQL支持动态CQL语句，参考[cqlprocessor](Preprocessor.md)。

new必须在某个cassandra/cass语句之内，参见[cassandra/cass](Conn.md)。

### 实现模块

com.alogic.xscript.cassandra.New

### 配置参数

支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cql.Insert | 插入SQL语句 |

### 案例

下面案例从cassandra中新增用户.

```xml

	<script>	
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />		
	<cass-conf>
		<cass connId="c1">	
			<new 
				cql.Insert="
	 				INSERT INTO users
                    	(
                    		#{not_nvl(name,'username')} 
							#{not_nvl(password,',password')} 
                    	) 
                    VALUES 
                    	(
                    		#{not_nvl(name, bind('name'))} 
                    		#{not_nvl(password,',' + bind('password'))} 
                    	)				    
	 			" />
		</cass>
	</cass-conf>
    </script>
```


