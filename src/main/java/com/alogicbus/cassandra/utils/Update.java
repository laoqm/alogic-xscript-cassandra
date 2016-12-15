package com.alogicbus.cassandra.utils;



import com.anysoft.util.BaseException;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


/**
 * Update操作
 * 
 * @author laoqiming
 */
public class Update extends CassandraOperation {

	public Update(Session _session) {
		super(_session);
	}

	protected PreparedStatement stmt = null;
	protected ResultSet resultSet = null;
	
	/**
	 * 执行单个CQL语句
	 * @param cql 
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String cql,Object...params) throws BaseException{
	
		try {
			boolean flag=false;
			stmt = session.prepare(cql);
		    BoundStatement bs = stmt.bind(params);
		    resultSet = session.execute(bs);
		    Row row = resultSet.one();
		    if(row !=null){
		    	flag =row.getBool("[applied]");
		    }else{
		    	flag=true;
		    }
		    return flag;
		}
		catch (Exception ex){
			throw new BaseException("core.cql_error","Error occurs when executing cql:" + ex.getMessage());
		}
	}

}
