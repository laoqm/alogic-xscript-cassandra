import java.util.HashMap;
import java.util.Map;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.Script;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.CommandLine;
import com.anysoft.util.Properties;
import com.anysoft.util.Settings;
import com.jayway.jsonpath.spi.JsonProvider;
import com.jayway.jsonpath.spi.JsonProviderFactory;

public class Demo {

	public static void run(String src,Properties p){
		Script script = Script.create(src, p);
		if (script == null){
			System.out.println("Fail to compile the script");
			return;
		}
		long start = System.currentTimeMillis();
		Map<String,Object> root = new HashMap<String,Object>();
		LogicletContext ctx = new LogicletContext(p);
		script.execute(root, root, ctx, new ExecuteWatcher.Quiet());
		
		System.out.println("Script:" + src);
		System.out.println("Duration:" + (System.currentTimeMillis() - start) + "ms");
		
		JsonProvider provider = JsonProviderFactory.createProvider();
		System.out.println("#########################################################");
		System.out.println(provider.toJson(root));				
		System.out.println("#########################################################");
	}
	
	public static void main(String[] args) {
		Settings settings = Settings.get();		
		settings.addSettings(new CommandLine(args));
		settings.SetValue("servant.config.master","java:///com/alogicbus/cassandra/context/servant.catalog.xml#com.alogicbus.cassandra.context.CassandraSource");
		settings.SetValue("cassandra.master","java:///com/alogicbus/cassandra/context/cassandra.source.xml#com.alogicbus.cassandra.context.CassandraSource");
		run("java:///xscript/list.xml#Demo",settings);
		
		SessionManager.close();//手动关闭客户端连接
		
	}

}
