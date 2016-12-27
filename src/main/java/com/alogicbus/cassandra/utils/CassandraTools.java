package com.alogicbus.cassandra.utils;

import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Session;

/**
 * Cassandra相关工具
 * @author laoqiming
 *
 */
public class CassandraTools {

	/**
	 * 通过CQL语句查询单个值
	 * @param session
	 * @param cql
	 * @param args
	 * @return 查询出对象
	 */
	public static Object selectAsObject(Session session, String cql, Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		return args;
	}

	/**
	 * 通过CQL语句查询单个值
	 * @param session
	 * @param cql
	 * @param args
	 * @return 查询出对象
	 */
	public static String selectAsString(Session session, String cql, Object... args) {
		Object obj = selectAsObject(session, cql, args);
		if(obj!=null){
			return obj.toString();
		}
		return null;
	}

	/**
	 * 通过CQL语句查询单个值(long)
	 * @param session
	 * @param cql
	 * @param dftValue
	 * @param args
	 * @return 结果值(Long)
	 */
	public static Long selectAsLong(Session session, String cql, long dftValue, Object... args) {
		String s = selectAsString(session, cql, args);
		long value = dftValue;
		if(isNotNull(s)){
			try{
				value = Long.valueOf(s);
			}catch(Exception ex){
			}
		}
		return value;
	}

	/**
	 * 通过CQL语句查询单个值（Integer）
	 * @param session
	 * @param cql
	 * @param defValue
	 * @param args
	 * @return
	 */
	public static Integer selectAsInt(Session session, String cql, int dftValue, Object... args) {
		String s = selectAsString(session, cql, args);
		int value = dftValue;
		if (isNotNull(s)) {
			try {
				value = Integer.valueOf(s);
			}catch (Exception ex){
			}
		}
		return value;
	}
    /**
     * 执行Update语句
     * @param session
     * @param cql
     * @param args
     * @return Update是否成功
     */
	public static boolean update(Session session, String cql, Object... args) {
		Update update = new Update(session);
	    return update.execute(cql, args);	
	}

	/**
	 * 执行Insert操作
	 * @param session
	 * @param cql
	 * @param args
	 * @return Insert操作是否成功
	 */
	public static boolean insert(Session session, String cql, Object... args) {
		return update(session, cql, args);
	}

	/**
	 * 执行Delete语句
	 * @param session
	 * @param cql
	 * @param args
	 * @return
	 */
	public static boolean delete(Session session, String cql, Object... args) {
		return update(session, cql, args);
	}

	/**
	 * Select 单行数据
	 * @param session
	 * @param cql
	 * @param args
	 * @return
	 */
	public static Map<String, String> select(Session session, String cql, Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		return selector.singleRowAsString();
	}

	/**
	 * Select 单行数据
	 * @param session
	 * @param cql
	 * @param renderer
	 * @param args
	 * @return
	 */
	public static Map<String, String> select(Session session, String cql, RowRenderer<String> renderer,
			Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		if (renderer != null) {
			return selector.singleRowAsString(renderer, null);
		}
		return selector.singleRowAsString();
		
	}
    
	/**
	 * Select 单行数据
	 * @param session
	 * @param cql
	 * @param args
	 * @return
	 */
	public static Map<String, Object> selectAsObjects(Session session, String cql, Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		return selector.singleRow();
	}

	/**
	 * Select 单行数据
	 * @param session
	 * @param cql
	 * @param renderer
	 * @param args
	 * @return
	 */
	public static Map<String, Object> selectAsObjects(Session session, String cql, RowRenderer<Object> renderer,
			Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		if(renderer !=null){
			return  selector.singleRow();
		}
		return selector.singleRow();
	}

	/**
	 * Select 多行数据
	 * @param session
	 * @param cql
	 * @param args
	 * @return
	 */
	public static List<Map<String, String>> list(Session session, String cql, Object... args) {
		return list(session, cql, null,args);
	}

	/**
	 * Select 多行数据
	 * @param session
	 * @param cql
	 * @param renderer
	 * @param args
	 * @return
	 */
	public static List<Map<String, String>> list(Session session, String cql, RowRenderer<String> renderer,
			Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		if (renderer != null) {
			return selector.resultAsString(renderer);
		}
			return selector.resultAsString();
		
	}
	
	/**
	 * Select 多行数据
	 * @param session
	 * @param cql
	 * @param args
	 * @return
	 */
	public static List<Map<String, Object>> listAsObject(Session session,String cql, Object... args) {
		return listAsObject(session, cql, null, args);
	}
	
	/**
	 * Select 多行数据,分页查询
	 * @param session
	 * @param cql
	 * @param limit
	 * @param offset
	 * @param args
	 * @return
	 */
	public static List<Map<String, Object>> listAsObjectByPage(Session session,String cql,int limit,int offset,int fetchSize, Object... args) {
		return listAsObjectByPage(session, cql,limit,offset,fetchSize,null, args);
	}
	
	/**
	 * Select 多行数据
	 * @param session
	 * @param cql
	 * @param renderer
	 * @param args
	 * @return
	 */
	public static List<Map<String, Object>> listAsObject(Session session, String cql, 
			RowRenderer<Object> renderer,Object... args) {
		Select selector = new Select(session);
		selector.execute(cql, args);
		if (renderer != null) {
			return selector.result(renderer);
		}
		return selector.result();
	}
	
    /**
     * Select 多行数据，分页查询
     * @param session
     * @param cql
     * @param limit
     * @param offset
     * @param renderer
     * @param args
     * @return
     */
	public static List<Map<String, Object>> listAsObjectByPage(Session session, String cql, int limit,int offset,int fetchSize,RowRenderer<Object> renderer,Object... args) {
		Select selector = new Select(session);
		selector.execute(cql,limit,offset,fetchSize,args);
		if (renderer != null) {
			return selector.result(renderer,limit);
		}
		return selector.result(limit);
	}

	private static boolean isNotNull(String value) {
		return value != null && value.length() > 0;
	}
}
