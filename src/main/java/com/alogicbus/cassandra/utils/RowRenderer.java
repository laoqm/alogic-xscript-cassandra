package com.alogicbus.cassandra.utils;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.ColumnDefinitions;

/**
 * 行数据的渲染器
 * @author laoqiming
 */
public interface RowRenderer<T> {
	/**
	 * 从Metadata中提取ColumnID
	 * @param metadata 本次SQL查询的元数据
	 * @param index 当前列的index
	 * @return ColumnID
	 */
	public String getColumnId(ColumnDefinitions metadata,int index);
	
	/**
	 * 渲染行的数据
	 * @param rowData 行的数据
	 * @return
	 */
	public Map<String,T> render(Map<String,T> rowData);
	
	/**
	 * 新增行的数据集
	 * @param columnCount 列的个数
	 * @return 行的数据集
	 */
	public Map<String,T> newRow(int columnCount);
	
	/**
	 * 缺省的渲染器
	 */
	public static class Default<T> implements RowRenderer<T>{
		public String getColumnId(ColumnDefinitions metadata, int index) {
			try {
				return metadata.getName(index);
			}catch (Exception ex){
				return null;
			}
		}

		public Map<String, T> render(Map<String, T> rowData) {
			return rowData;
		}
		
		public Map<String,T> newRow(int columnCount){
			return new HashMap<String,T>(columnCount);
		}
	}
}
