import com.alogicbus.cassandra.core.ConnectionHolder;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.Settings;
import com.datastax.driver.core.Session;

public class Demo2 {

	public static void main(String[] args) {
		Settings settings = Settings.get();		
		settings.SetValue("cassandra.master","java:///com/alogicbus/cassandra/context/cassandra.source.xml#com.alogicbus.cassandra.context.CassandraSource");
		
		try{
		ConnectionHolder c = SessionManager.getConnection("conn1");
		System.out.println(c.getId()+","+c.getName()+","+c.getHosts()+","+c.getPort());
	    Session s = c.getSession();
	    System.out.println(s);
		}finally{
			SessionManager.close();
		}
	}

}
