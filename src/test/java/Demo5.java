import com.alogicbus.cassandra.context.CassandraSource;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.Settings;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Demo5 {

	public static void main(String[] args) {
		Settings settings = Settings.get();
		settings.SetValue("cassandra.master",
				"java:///com/alogicbus/cassandra/context/cassandra.source.xml#com.alogicbus.cassandra.context.CassandraSource");
		try {
			boolean flag=false;
			CassandraSource.get();// 解析配置文件
			Session s = SessionManager.getSession("c1");
			ResultSet rs = s.execute("INSERT INTO users (username, password) values ('a', 'b') IF NOT EXISTS");
			Row row =rs.one();
		    if(row !=null){
		    	flag = row.getBool("[applied]");
		    }else{
		    	System.out.println("can not check");
		    	flag=true;
		    }
			System.out.println(flag);
		} finally {
			SessionManager.close();
		}

	}

}
