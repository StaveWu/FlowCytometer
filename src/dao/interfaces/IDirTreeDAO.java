package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;
import dao.beans.DirTreeBean;

public interface IDirTreeDAO {
	
	/**
	 * 建表
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws SQLException;
	
	/**
	 * 添加树节点
	 * @param tableName
	 * @param bean
	 * @throws Exception
	 */
	public boolean addDirTree(String tableName, DirTreeBean bean) throws SQLException;
	
	/**
	 * 查询所有节点
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<DirTreeBean> findAll(String tableName) throws SQLException;
	
	/**
	 * 根据指定信息查询
	 * @param tableName
	 * @param gcs
	 * @return
	 * @throws Exception
	 */
	public List<DirTreeBean> find(String tableName, List<GroupCondition> gcs) throws SQLException;
	
	/**
	 * 判断表格是否存在
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(String tableName) throws SQLException;
	
	/**
	 * 删除指定lid的行
	 * @param tableName
	 * @param lid
	 * @throws SQLException
	 */
	public void deleteDirTree(String tableName, String lid) throws SQLException;
	
	public void update(String tableName, List<GroupCondition> items, String lid) throws SQLException;


}
