package com.alogicbus.cassandra.utils;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class StatementCache {

	public static Map<Session, Map<String, PreparedStatement>> cache = new HashMap<Session, Map<String, PreparedStatement>>();

	public static PreparedStatement get(Session session, String cql) {
		Map<String, PreparedStatement> map = null;
		PreparedStatement stmt =null;
		String key = Md5Util.toMD5_16(cql);//对cql进行md5编码生成id
		if (cache.containsKey(session)) {
			map = cache.get(session);
			if (map.containsKey(key)) {
				return map.get(key);
			} else {
				synchronized (cache) {
					if (!map.containsKey(key)) {
						stmt = session.prepare(cql);
						map.put(key, stmt);
					}
				}
				return map.get(key);
			}
		} else {
			synchronized (cache) {
				if (!cache.containsKey(session)) {
					map = new HashMap<String, PreparedStatement>();
					stmt = session.prepare(cql);
					map.put(key, stmt);
					cache.put(session, map);
				}else if(!cache.get(session).containsKey(key)){
					stmt=session.prepare(cql);
					cache.get(session).put(key, stmt);
				}
			}
			return cache.get(session).get(key);
		}
	}
	
}
