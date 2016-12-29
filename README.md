alogic-xscript-cassandra
========================

### Overview

alogic-xscript-cassandra是基于xscript2.0的cassandra插件。

### Getting started

按照以下步骤，您可轻松在您的项目中使用alogic-xscript-cassandra

不过开始之前，我们希望您了解xscript的相关知识。

- [xscript2.0](https://github.com/yyduan/alogic/blob/master/alogic-doc/alogic-common/xscript2.md) - 您可以了解xscript的基本原理及基本编程思路
- [xscript2.0基础插件](https://github.com/yyduan/alogic/blob/master/alogic-doc/alogic-common/xscript2-plugins.md) - 如何使用xscript的基础插件
- [基于xscript的together](https://github.com/yyduan/alogic/blob/master/alogic-doc/alogic-common/xscript2-together.md) - 如何将你的script发布为alogic服务

#### 增加maven依赖

您可以在中央仓库上找到[alogic-xscript-cassandra](http://mvnrepository.com/search?q=com.github.anylogic%3Aalogic-xscript-cassandra)的发布版本。

```xml

    <dependency>
        <groupId>com.github.anylogic</groupId>
        <artifactId>alogic-xscript-cassandra</artifactId>
        <version>2.1.10-20161229</version>
    </dependency>   	

```

> alogic-xscript-cassandra版本号前面的2.1.10是其所依赖的datastax的cassandra-driver-core的版本号，后面的20161229是其发布的日期。

#### Setting文件引入Cassandra连接配置
```xml

	<parameter id="cassandra.master" value="java:///conf/cassandra/cassandra.source.xml#App" final="true"/> 
```

>首先要在[settings.xml](src/test/resources/conf/settings.xml)文件中配置cassandra的连接配置，这里[cassandra.source.xml](src/test/resources/conf/cassandra.source.xml)为cassandra连接配置文件,具体内容如下所示。

#### Cassandra连接配置

```xml

    <!-- 使用cassandra连接配置 -->
    <?xml version="1.0" encoding="UTF-8"?>
    <cassandra>
        <connections module="com.alogicbus.cassandra.context.InnerContext">
	      <connection id="c1" 
	                  name="defaultConnection" 
	                  hosts="127.0.0.1" 
	                  port="9042"
	                  username="admin"
	                  password="12345678"
	                  keyspace="playlist"
	                  compress="lz4" 
	                  asynchronous ="false"/>
	   </connections>
    </cassandra>
```




#### 引入Namespace

在您的脚本中，你需要引入CassandraNS作为Namespace，比如:

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
>其中connId="c1"为连接配置时connection的id

### Example

下面的案例是对本地Cassandra的基本操作，以[List demo](src/test/resources/xscript/List.xml)为例。

```xml

    <?xml version="1.0" encoding="utf-8"?>
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
	 		/>
		</cass>
	</cass-conf>
    </script>
```

>为正确执行上述指令，需要在本地安装好Cassandra并启动(本插件是基于apache-cassandra-2.1.0,支持2.1.0或以上版本)

Cassandra启动后，就可以运行[demo](src/test/java/Demo.java)来测试xscript脚本。

### Reference

参见[alogic-xscript-cassandra参考](src/docs/reference.md)。

### History
    
- 2.1.10 [20161229 laoqiming]
	+ 初次发布
	

	


