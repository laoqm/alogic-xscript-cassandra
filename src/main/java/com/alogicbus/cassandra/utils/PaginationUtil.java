package com.alogicbus.cassandra.utils;

import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

/**
 * 分页的工具类
 * @author laoqiming
 */
public class PaginationUtil {

	/*public static List<Row> fetchRowsWithPage(Session session, Statement statement, int offset, int limit,int fetchSize) {
		List<Row> rows = new ArrayList<>(limit);
		ResultSet result = null;
		int currentPagesCnt=0;
		int skippingPagesCnt=0;
		String savingPageState = null;
		statement.setFetchSize(fetchSize);
		boolean isEnd = false;
		while(currentPagesCnt< (offset+1)){
			if (null != savingPageState) {
				statement = statement.setPagingState(PagingState.fromString(savingPageState));
			}
			result = session.execute(statement);
			PagingState pagingState = result.getExecutionInfo().getPagingState();
			if (null != pagingState) {
				savingPageState = result.getExecutionInfo().getPagingState().toString();
			}
			currentPagesCnt+=result.getAvailableWithoutFetching();
			if(currentPagesCnt<(offset+1)){
				skippingPagesCnt=currentPagesCnt;
			}
			if (result.isFullyFetched() && null == pagingState) {
				if (true == isEnd) { // offset > 记录总数
					return rows;  
				} else {
					isEnd = true;
				}
			}
		}
		if (result != null) {
			int remainSkippingRows = offset-skippingPagesCnt;
			int index = 0;
			for (Iterator<Row> iter = result.iterator(); iter.hasNext() && rows.size() < limit;) {
				Row row = iter.next();
				if (index >= remainSkippingRows) {
					rows.add(row);
				}
				index++;
			}
		}
		return rows;
	}*/
	
	public static ResultSet fetchRowsWithPage(Session session, Statement statement, int offset, int limit,int fetchSize) {
		ResultSet result = null;
		int currentPagesCnt=0;
		int skippingPagesCnt=0;
		String savingPageState = null;
		statement.setFetchSize(fetchSize);
		boolean isEnd = false;
		while(currentPagesCnt< (offset+1)){
			if (null != savingPageState) {
				statement = statement.setPagingState(PagingState.fromString(savingPageState));
			}
			result = session.execute(statement);
			PagingState pagingState = result.getExecutionInfo().getPagingState();
			if (null != pagingState) {
				savingPageState = result.getExecutionInfo().getPagingState().toString();
			}
			currentPagesCnt+=result.getAvailableWithoutFetching();
			if(currentPagesCnt<(offset+1)){
				skippingPagesCnt=currentPagesCnt;
			}
			if (result.isFullyFetched() && null == pagingState) {
				if (true == isEnd) { // offset > 记录总数
					return null;  
				} else {
					isEnd = true;
				}
			}
		}
		if (result != null) {
		   int remainSkippingRows = offset-skippingPagesCnt;
		   if(remainSkippingRows > 0){
			  for (Row r : result) {
			    if (--remainSkippingRows == 0) {
			        break;
			    }
			  }
		   }
		}
		return result;
	}

}
