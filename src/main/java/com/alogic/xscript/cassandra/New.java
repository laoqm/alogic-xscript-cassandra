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

public class New extends CassandraOperation{

	protected String cqlInsert ="";
	protected Preprocessor processor = null;
	
	public New(String tag, Logiclet p) {
		super(tag, p);
	}
	
	@Override
	public void configure(Properties p) {
		super.configure(p);
		cqlInsert = PropertiesConstants.getString(p, "cql.Insert", cqlInsert);
	    processor = new Preprocessor(cqlInsert);
	}

	@Override
	protected void onExecute(Session session, Map<String, Object> root, Map<String, Object> current,
			LogicletContext ctx, ExecuteWatcher watcher) {
		List<Object> data =new ArrayList<Object>();
		String cql = processor.process(ctx, data);
		CassandraTools.insert(session, cql, data.toArray());
	}

}
