import com.alogicbus.cassandra.context.CassandraSource;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.CommandLine;
import com.anysoft.util.Settings;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Demo3 {

	public static void main(String[] args) {
		Settings settings = Settings.get();
		settings.addSettings(new CommandLine(args));
		settings.addSettings("java:///conf/settings.xml#App", null, Settings.getResourceFactory());
		try {
			CassandraSource.get();// 解析配置文件
			Session s = SessionManager.getSession("c1");
			ResultSet rs = s.execute("select release_version from system.local");
			Row row = rs.one();
			System.out.println(row.getString("release_version"));
		} finally {
			SessionManager.close();
		}
	}

}
