package com.alogicbus.cassandra.context;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alogicbus.cassandra.core.ConnectionHolder;
import com.anysoft.context.Context;
import com.anysoft.context.Source;
import com.anysoft.util.Factory;
import com.anysoft.util.IOTools;
import com.anysoft.util.Properties;
import com.anysoft.util.Settings;
import com.anysoft.util.XmlTools;
import com.anysoft.util.resource.ResourceFactory;

public class CassandraSource extends Source<ConnectionHolder> {

	protected String getContextName(){
		return "connections";
	}
	
	
	public static class TheFactory extends Factory<Context<ConnectionHolder>>{
	}
	
	public static final TheFactory factory = new TheFactory();
	
	public static Context<ConnectionHolder> newInstance(Element doc,Properties p){
		if (doc == null) return null;
		return factory.newInstance(doc, p);
	}
	
	@Override
	public Context<ConnectionHolder> newInstance(Element e, Properties p,String attrName) {
		return factory.newInstance(e,p,attrName,InnerContext.class.getName());
	}
	
	public static CassandraSource theInstance = null;
	
	public static CassandraSource get(){
		if (theInstance != null){
			return theInstance;
		}
		
		synchronized (factory){
			if (theInstance == null){
				theInstance = (CassandraSource)newInstance(Settings.get(), new CassandraSource());
			}
		}
		
		return theInstance;
	}
	
	protected static Context<ConnectionHolder> newInstance(Properties p,Context<ConnectionHolder> instance){
		String configFile = p.GetValue("cassandra.master", 
				"java:///com/logicbus/cassandra/context/cassandra.xml#com.logicbus.cassandra.context.CassandraSource");

		String secondaryFile = p.GetValue("cassandra.secondary", 
				"java:///com/logicbus/cassandra/context/cassandra.xml#com.logicbus.cassandra.context.CassandraSource");
		
		ResourceFactory rm = Settings.getResourceFactory();
		InputStream in = null;
		try {
			in = rm.load(configFile,secondaryFile, null);
			Document doc = XmlTools.loadFromInputStream(in);
			if (doc != null){
				if (instance == null){
					return newInstance(doc.getDocumentElement(),p);
				}else{
					instance.configure(doc.getDocumentElement(), p);
					return instance;
				}
			}
		} catch (Exception ex){
			logger.error("Error occurs when load xml file,source=" + configFile, ex);
		}finally {
			IOTools.closeStream(in);
		}
		return null;
	}
}
