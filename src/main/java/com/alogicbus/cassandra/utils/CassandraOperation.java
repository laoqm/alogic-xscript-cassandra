package com.alogicbus.cassandra.utils;

import com.datastax.driver.core.Session;


abstract public class CassandraOperation {
	
	protected Session session = null;
	protected CassandraOperation(Session _session){
		session = _session;
	}


}
