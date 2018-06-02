package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.beans.TableBean;

public interface ITableDAO {
	
	/**
	 * ����
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws SQLException;
	
	/**
	 * �������
	 * @param tb
	 * @throws Exception
	 */
	public boolean addTableBean(String tableName, TableBean tb) throws SQLException;
	
	/**
	 * ����id
	 * @param tableName
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public TableBean findById(String tableName, String columnId) throws SQLException;
	
	/**
	 * ��ȡ��������
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<TableBean> findAll(String tableName) throws SQLException;

}
