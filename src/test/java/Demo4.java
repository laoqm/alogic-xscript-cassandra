import com.alogicbus.cassandra.context.CassandraSource;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.Settings;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Demo4 {

	public static void main(String[] args) {
		Settings settings = Settings.get();
		settings.SetValue("cassandra.master",
				"java:///com/alogicbus/cassandra/context/cassandra.source.xml#com.alogicbus.cassandra.context.CassandraSource");
		try {
			CassandraSource.get();// 解析配置文件
			Session s = SessionManager.getSession("conn1");
			ResultSet rs = s.execute("select *  from users");
			ColumnDefinitions metadata = rs.getColumnDefinitions();
			int columnNum = metadata.size();
			for (Row row : rs) {
				for (int i = 0; i < columnNum; i++) {
					System.out.println(metadata.getName(i) + " " + row.getObject(i));
				}
			}
		} finally {
			SessionManager.close();
		}
	}

}
