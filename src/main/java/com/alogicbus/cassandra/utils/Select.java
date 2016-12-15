package com.alogicbus.cassandra.utils;



import java.util.List;
import java.util.Map;

import com.anysoft.util.BaseException;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * 查询语句操作类
 * 
 * @author laoqiming
 */
public class Select extends CassandraOperation {

	public Select(Session session) {
		super(session);
	}

	protected PreparedStatement stmt = null;
	protected ResultSet resultSet = null;
	
	/**
	 * 执行CQL语句
	 * @param cql CQL语句
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	public Select execute(String cql,Object... params) throws BaseException{
		try {
			stmt = session.prepare(cql);
		    BoundStatement bs = stmt.bind(params);
		    resultSet = session.execute(bs);
			return this;
		}
		catch (Exception ex){
			throw new BaseException("core.cql_error","Error occurs when executing cql:" + ex.getMessage());
		}
	}
	
	/**
	 * 获取查询结果（单返回值）
	 * 
	 * @return 结果值
	 * @throws Exception
	 *//*
	public Object single()throws BaseException{
		try {
			if (rs != null){
			   return rs.one();
			}
			return null;
		}
		catch (Exception ex){
			throw new BaseException("core.cql_error","Error occurs when executing cql:" + ex.getMessage());
		}		
	}

	*//**
	 * 以Long形式获取查询结果（单返回值）
	 * @param dftValue 缺省值
	 * @return 结果值
	 * @throws BaseException
	 * 
	 * @since 1.6.2.3
	 *//*
	public long singleAsLong(long dftValue)throws BaseException{
		Object result = single();
		
		if (result == null){
			return dftValue;
		}
		
		if (result instanceof Number){
			Number value = (Number) result;
			return value.longValue();
		}
		
		String value = result.toString();
		try{
			return Long.parseLong(value);
		}catch (Exception ex){
			return dftValue;
		}
	}
	
	*//**
	 * 以String形式获取查询结果（单返回值）
	 * @param dftValue 缺省值
	 * @return 结果值
	 * @throws BaseException
	 * 
	 * @since 1.6.2.3
	 *//*
	public String singleAsString(String dftValue)throws BaseException{
		Object result = single();
		
		if (result == null){
			return dftValue;
		}
		
		return result.toString();
	}*/
	
	/**
	 * 获取查询结果(单行返回值)
	 * 
	 * @return 查询结果
	 */
	public Map<String,Object> singleRow()throws BaseException{
		return singleRow(null);
	}
	
	/**
	 * 获取查询结果(单行返回值)
	 * 
	 * @return 查询结果
	 * s@since 1.6.2.3
	 */
	public Map<String,String> singleRowAsString()throws BaseException{
		return singleRowAsString(null);
	}	

	/**
	 * 获取查询结果(单行返回值)
	 * 
	 * @param result
	 * @return
	 * @since 1.2.0
	 */
	public Map<String,Object> singleRow(Map<String,Object> result)throws BaseException{
		return singleRow(null,result);
	}		
	
	/**
	 * 获取查询结果(单行返回值)
	 * @param result 预创建的结果集
	 * @param renderer 渲染器
	 * @return 查询结果
	 * @throws BaseException
	 * @since 1.6.2.4
	 */
	public Map<String, Object> singleRow(RowRenderer<Object> renderer, Map<String, Object> result)
			throws BaseException {
		try {
			if (resultSet != null) {
				if (renderer == null) {
					renderer = new RowRenderer.Default<Object>();
				}
				
				Row row = resultSet.one();
				ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
				
				if(result ==null){
					result = renderer.newRow(columnDefinitions.size());
				}
				for (int i = 0; i < columnDefinitions.size(); i++) {
					Object value = row.getObject(i);
					if (value == null)
						continue;
					String name = renderer.getColumnId(columnDefinitions, i);
					if (name == null)
						continue;
					result.put(name, value);
				}
				return renderer.render(result);
			}
			return null;
		} catch (Exception ex) {
			throw new BaseException("core.cql_error", "Error occurs when executing cql:" + ex.getMessage());
		}
	}
	
	/**
	 * 获取单行结果
	 * @param result
	 * @return 单行结果
	 * @throws BaseException
	 * @since 1.6.2.3
	 */
	public Map<String,String> singleRowAsString(Map<String,String> result)throws BaseException{
		return singleRowAsString(null,result);
	}	
	
	/**
	 * 获取单行结果
	 * @param result 预创建的结果集
	 * @param renderer 渲染器
	 * @return 单行结果
	 * @throws BaseException
	 */
	public Map<String,String> singleRowAsString(RowRenderer<String> renderer,Map<String,String> result)throws BaseException{
		try {
			if (resultSet != null){
				if (renderer == null){
					renderer = new RowRenderer.Default<String>();
				}

				Row row = resultSet.one();
				ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
				if(result ==null){
					result = renderer.newRow(columnDefinitions.size());
				}
				for (int i = 0; i < columnDefinitions.size(); i++) {
					Object value = row.getObject(i);
					if (value == null)
						continue;
					String name = renderer.getColumnId(columnDefinitions, i);
					if (name == null)
						continue;
					result.put(name, value.toString());
				}
				return renderer.render(result);
			}
			return null;
		}
		catch (Exception ex){
			throw new BaseException("core.cql_error","Error occurs when executing cql:" + ex.getMessage());
		}
	}	
	
	public void result(RowListener<Object> rowListener)throws BaseException{
		if (resultSet == null || rowListener == null){
			return ;
		}
		try{
			ColumnDefinitions metadata=resultSet.getColumnDefinitions();
			int columnCount = metadata.size();
			for(Row row:resultSet){
				Object cookies = rowListener.rowStart(columnCount);
				for (int i = 0; i < columnCount; i++) {
                    Object value=row.getObject(i);
                    if(value !=null){
                    	rowListener.columnFound(cookies, i, metadata, value);
                    }
				}
				rowListener.rowEnd(cookies);
			}
		}
		catch (Exception ex){
			throw new BaseException("core.cql_error","Error occurs when executing cql:" + ex.getMessage());
		}
	}

	/**
	 * 获取查询结果
	 * 
	 * <p>查询结果通过监听器获取
	 * 
	 * @param rowListener 行监听器
	 */
	public void resultAsString(RowListener<String> rowListener)throws BaseException{
		if (resultSet == null || rowListener == null){
			return ;
		}
		try{
			ColumnDefinitions metadata = resultSet.getColumnDefinitions();
			int columnCount = metadata.size();
			for(Row row:resultSet){
				Object cookies = rowListener.rowStart(columnCount);
				for (int i = 0 ; i < columnCount ; i++){			
					Object value = row.getObject(i);
					if (value != null){
						rowListener.columnFound(cookies,i,metadata,value.toString());
					}
				}
				rowListener.rowEnd(cookies);
			}
		}
		catch (Exception ex){
			throw new BaseException("core.cql_error","Error occurs when executing cql:" + ex.getMessage());
		}
	}
	
	/**
	 * 获取查询结果
	 * 
	 * <p>查询结果通过列表返回，可直接作为JSON数据
	 * @return 查询结果
	 */
	public List<Map<String,Object>> result()throws BaseException{
		RowListener.Default<Object> data = new RowListener.Default<Object>();
		result(data);
		return data.getResult();
	}
	
	public List<Map<String,Object>> result(RowRenderer<Object> renderer)throws BaseException{
		RowListener.Default<Object> data = new RowListener.Default<Object>(renderer);
		result(data);
		return data.getResult();
	}
	
	public List<Map<String,String>> resultAsString()throws BaseException{
		RowListener.Default<String> data = new RowListener.Default<String>();
		resultAsString(data);
		return data.getResult();
	}
	
	public List<Map<String,String>> resultAsString(RowRenderer<String> renderer)throws BaseException{
		RowListener.Default<String> data = new RowListener.Default<String>(renderer);
		resultAsString(data);
		return data.getResult();
	}
}
