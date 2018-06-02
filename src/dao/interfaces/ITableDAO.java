package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.beans.TableBean;

public interface ITableDAO {
	
	/**
	 * 建表
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws SQLException;
	
	/**
	 * 添加数据
	 * @param tb
	 * @throws Exception
	 */
	public boolean addTableBean(String tableName, TableBean tb) throws SQLException;
	
	/**
	 * 查找id
	 * @param tableName
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public TableBean findById(String tableName, String columnId) throws SQLException;
	
	/**
	 * 获取所有数据
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<TableBean> findAll(String tableName) throws SQLException;

}
