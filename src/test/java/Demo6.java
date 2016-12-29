import com.alogicbus.cassandra.context.CassandraSource;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.Settings;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class Demo6 {

	public static void main(String[] args) {
		Settings settings = Settings.get();
		settings.SetValue("cassandra.master",
				"java:///com/alogicbus/cassandra/context/cassandra.source.xml#com.alogicbus.cassandra.context.CassandraSource");
		try {
			CassandraSource.get();// 解析配置文件
			Session s = SessionManager.getSession("c1");
			ResultSet rs = s.execute("UPDATE users SET password ='abc' WHERE username = 'laoqiming'");
			boolean flag = rs.one().getBool("[applied]");
			System.out.println(flag);
		} finally {
			SessionManager.close();
		}

	}

}
