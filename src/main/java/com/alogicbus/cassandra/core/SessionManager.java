package com.alogicbus.cassandra.core;

import org.apache.commons.lang3.StringUtils;

import com.alogicbus.cassandra.context.CassandraSource;
import com.datastax.driver.core.Session;

public class SessionManager {

	public static ConnectionHolder getConnection(String id) {
		if (StringUtils.isEmpty(id))
			return null;
		return CassandraSource.get().get(id);
	}

	public static Session getSession(String id) {
		ConnectionHolder holder = getConnection(id);
		if (holder != null)
			return holder.getSession();
		else
			return null;
	}

	public static void close() {
		CassandraSource.get().close();
	}
}
