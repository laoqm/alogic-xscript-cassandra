package com.alogic.xscript.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogicbus.cassandra.processor.Preprocessor;
import com.alogicbus.cassandra.utils.CassandraTools;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.datastax.driver.core.Session;

public class Update extends CassandraOperation{

	protected String cqlUpdate ="";
	protected Preprocessor processor = null;
	
	public Update(String tag, Logiclet p) {
		super(tag, p);
	}
	
	@Override
	public void configure(Properties p) {
		super.configure(p);
		cqlUpdate = PropertiesConstants.getString(p, "cql.Update", cqlUpdate);
	    processor = new Preprocessor(cqlUpdate);
	}

	@Override
	protected void onExecute(Session session, Map<String, Object> root, Map<String, Object> current,
			LogicletContext ctx, ExecuteWatcher watcher) {
		List<Object> data =new ArrayList<Object>();
		String cql = processor.process(ctx, data);
		CassandraTools.update(session, cql, data.toArray());
	}

}
