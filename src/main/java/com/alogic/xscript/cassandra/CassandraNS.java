package com.alogic.xscript.cassandra;

import com.alogic.xscript.Logiclet;
import com.alogic.xscript.plugins.Segment;

public class CassandraNS extends Segment{

	public CassandraNS(String tag, Logiclet p) {
		super(tag, p);
		registerModule("cassandra",CassandraConn.class);
		registerModule("cass",CassandraConn.class);
		registerModule("list",ListAll.class);
		registerModule("new",New.class);
		registerModule("query",Query.class);
		registerModule("update",Update.class);
		registerModule("delete", Delete.class);
	}

}
