cassandra/cass
==
cassandra或cass用于获取一个Cassandra的连接对象，一个典型的应用只需要维护一个连接对象。

当一个应用需要同时连接多个Cassandra集群的时候，可以通过connection或conn设置不同的Cassandra连接对象id.

### 实现模块

com.alogic.xscript.cassandra.CassandraConn

### 配置参数

支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | connectionId/connId | 要连接的数据源id |

### 案例

通过下列方法使用cassandra/cass.


```xml

	<!-- 引入cassandra的Namespace -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
	
	<cass-conf>
		<cassandra connectionId="c1">	
			<!--
			在这里你可以使用alogic-xcript-cassandra提供的语句
		    -->
		</cassandra>
	</cass-conf>
```	

>如果想配置更简洁一点可以使用缩写，如下所示

```xml

    <!-- 引入cassandra的Namespace -->
	<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
	
    <cass-conf>
		<cass connId="c1">	
			<!--
			在这里你可以使用alogic-xcript-cassandra提供的语句
		    -->
		</cass>
	</cass-conf>
```