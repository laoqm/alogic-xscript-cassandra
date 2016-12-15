package com.alogic.xscript.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogicbus.cassandra.processor.Preprocessor;
import com.alogicbus.cassandra.utils.CassandraTools;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.datastax.driver.core.Session;

public class Delete extends CassandraOperation{

	protected String cqlDelete ="";
	protected Preprocessor processor = null;
	
	public Delete(String tag, Logiclet p) {
		super(tag, p);
	}
	
	@Override
	public void configure(Properties p) {
		super.configure(p);
		cqlDelete = PropertiesConstants.getString(p, "cql.Delete", cqlDelete);
	    processor = new Preprocessor(cqlDelete);
	}

	@Override
	protected void onExecute(Session session, Map<String, Object> root, Map<String, Object> current,
			LogicletContext ctx, ExecuteWatcher watcher) {
		List<Object> data =new ArrayList<Object>();
		String cql = processor.process(ctx, data);
		CassandraTools.delete(session, cql, data.toArray());
	}

}
