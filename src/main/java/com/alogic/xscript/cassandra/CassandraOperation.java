package com.alogic.xscript.cassandra;

import java.util.Map;

import com.alogic.xscript.AbstractLogiclet;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.datastax.driver.core.Session;
import com.logicbus.backend.ServantException;

public abstract class CassandraOperation extends AbstractLogiclet {
	protected String cid = "session";

	public CassandraOperation(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
	     Session session=ctx.getObject(cid);	
	     if(session == null){
	    	 throw new ServantException("core.no_cassandra_session","It must be in a cassandra context,check your together script.");
	     }
	     onExecute(session,root,current,ctx,watcher);
	}

	protected abstract void onExecute(Session session, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher);
}
