import com.alogicbus.cassandra.context.CassandraSource;
import com.anysoft.util.Settings;

public class Demo7 {

	public static void main(String[] args) {
		Settings settings = Settings.get();
		settings.SetValue("cassandra.master",
				"java:///com/alogicbus/cassandra/context/cassandra.source.xml#com.alogicbus.cassandra.context.CassandraSource");
		
		CassandraSource _source=CassandraSource.get();// 解析配置文件
		_source.get("cluster1");
		System.out.println(_source);

	}

}
