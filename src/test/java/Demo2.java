import com.alogicbus.cassandra.core.ConnectionHolder;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.CommandLine;
import com.anysoft.util.Settings;
import com.datastax.driver.core.Session;

public class Demo2 {

	public static void main(String[] args) {
		Settings settings = Settings.get();
		settings.addSettings(new CommandLine(args));
		settings.addSettings("java:///conf/settings.xml#App", null, Settings.getResourceFactory());
		try{
		ConnectionHolder c = SessionManager.getConnection("c1");
	    Session s = c.getSession();
	    System.out.println(s);
		}finally{
			SessionManager.close();
		}
	}

}
