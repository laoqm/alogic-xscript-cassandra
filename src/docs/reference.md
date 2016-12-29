alogic-xcript-cassandra参考
==========================

alogic-xscript-cassandra插件包括：

- [创建cassandra连接](plugins/Conn.md)
- [查看列表list](plugins/List.md)
- [查询单个对象query](plugins/Query.md)
- [新增语句new](plugins/New.md)
- [删除语句delete](plugins/Delete.md)
- [更新语句update](plugins/Update.md)


### 实现模块

com.alogic.xscript.cassandra.CassandraNS

### 配置参数

无

### 案例

通过下列方法使用cassandra插件.

```xml

	<script>
		<using xmlTag="cass-conf" module="com.alogic.xscript.cassandra.CassandraNS" />
		<cass-conf>
			<!--下面使用CassandraNS注册插件进行操作-->
		</cass-conf>
	</script>

```
