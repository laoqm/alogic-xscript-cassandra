package com.alogic.xscript.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogicbus.cassandra.processor.Preprocessor;
import com.alogicbus.cassandra.utils.CassandraTools;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.datastax.driver.core.Session;

public class ListAll extends CassandraOperation{
     protected String tag ="data";
     protected String cqlQuery ="";
     protected Preprocessor processor = null;
     
     public ListAll(String tag,Logiclet p) {
		super(tag, p);
	}

    @Override
    public void configure(Properties p) {
    	super.configure(p);
    	tag = PropertiesConstants.getRaw(p, "tag", tag);
    	cqlQuery = PropertiesConstants.getString(p, "cql.Query", cqlQuery);
    	processor = new Preprocessor(cqlQuery);
    	
    }
	@Override
	protected void onExecute(Session session, Map<String, Object> root, Map<String, Object> current,
			LogicletContext ctx, ExecuteWatcher watcher) {
		String tagValue = ctx.transform(tag);
		if (StringUtils.isNotEmpty(tagValue)){
			List<Object> data = new ArrayList<Object>();
			String cql = processor.process(ctx, data);
			List<Map<String,Object>> result = CassandraTools.listAsObject(session, cql,data.toArray());
			current.put(tagValue, result);
		}
	}
	
}
