query
=====
query用于在cassandra中查询出单行记录，并以对象的形式输出到文档，或者扩展当前文档节点的对象属性。

本插件中所使用的CQL支持动态CQL语句，参考[cqlprocessor](Preprocessor.md)。

query必须在某个cassandra/cass语句之内，参见[cassandra/cass](Conn.md)。

### 实现模块

com.alogic.xscript.cassandra.Query

### 配置参数

支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cql.Query | 查询cQL语句 |
| 2 | tag | 输出对象的tag，支持变量计算 | 
| 3 | extend | 是否扩展当前对象，缺省为false，当前exend为true的时候，tag无意义 |

### 案例

下面案例从cassandra中查询出指定的用户。

```xml

	<script>
	<!-- 使用cassandra插件 -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
	
     <cass-conf>
		<cassandra connId="c1">	
			<query tag="data"
				cql.Query="
	 				select 
                        *
                    from 
                        users
                    where 
                        username = #{bind('name')}
	 			" />
		</cassandra>
	</cass-conf>
    </script>
```


