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
import com.alogicbus.cassandra.utils.RowRenderer;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.datastax.driver.core.Session;

public class Query extends CassandraOperation{
     protected String tag ="data";
     protected String cqlQuery ="";
     protected boolean extend =false;
     protected Preprocessor processor = null;
     
     public Query(String tag,Logiclet p) {
		super(tag, p);
	}

    @Override
    public void configure(Properties p) {
    	super.configure(p);
    	tag = PropertiesConstants.getRaw(p, "tag", tag);
    	cqlQuery = PropertiesConstants.getString(p, "cql.Query", cqlQuery);
    	processor = new Preprocessor(cqlQuery);
    	extend = PropertiesConstants.getBoolean(p, "extend", extend);
    	
    }
	@Override
	protected void onExecute(Session session, Map<String, Object> root, final Map<String, Object> current,
			LogicletContext ctx, ExecuteWatcher watcher) {
		if (extend){
			List<Object> data = new ArrayList<Object>();
			String cql = processor.process(ctx, data);
			CassandraTools.selectAsObjects(session, cql, new RowRenderer.Default<Object>(){
				@Override
				public Map<String, Object> newRow(int columnCount) {
					return current;
				}
			}, data.toArray());
		}else{
			String tagValue = ctx.transform(tag);
			if (StringUtils.isNotEmpty(tagValue)){
				List<Object> data = new ArrayList<Object>();
				String cql = processor.process(ctx, data);
				Map<String,Object> result = CassandraTools.selectAsObjects(session, cql,data.toArray());
				current.put(tagValue, result);
			}
		}
	}
	
}
