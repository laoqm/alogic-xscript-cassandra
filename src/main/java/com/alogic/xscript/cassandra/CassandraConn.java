package com.alogic.xscript.cassandra;

import java.util.Map;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.plugins.Segment;
import com.alogicbus.cassandra.core.SessionManager;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.datastax.driver.core.Session;

public class CassandraConn extends Segment {

	protected String connId;
    protected String cid="session";
	public CassandraConn(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Properties p) {
		super.configure(p);
		connId = PropertiesConstants.getString(p, "connection", "");
	}

	@Override
	protected void onExecute(Map<String, Object> root, 
			Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		Session session = SessionManager.getSession(connId);
		if(session == null){
			logger.error("Can't get connection cassandra by connId:" + connId+", The cassandra connection is null!");
			return ;
		}
		try {
			ctx.setObject(cid, session);
			super.onExecute(root, current, ctx, watcher);
		}finally{
			//SessionManager.close();
			ctx.removeObject(cid);
		}
	}
}
