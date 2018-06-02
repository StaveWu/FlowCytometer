package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;
import dao.beans.DirTreeBean;

public interface IDirTreeDAO {
	
	/**
	 * ����
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws SQLException;
	
	/**
	 * ������ڵ�
	 * @param tableName
	 * @param bean
	 * @throws Exception
	 */
	public boolean addDirTree(String tableName, DirTreeBean bean) throws SQLException;
	
	/**
	 * ��ѯ���нڵ�
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<DirTreeBean> findAll(String tableName) throws SQLException;
	
	/**
	 * ����ָ����Ϣ��ѯ
	 * @param tableName
	 * @param gcs
	 * @return
	 * @throws Exception
	 */
	public List<DirTreeBean> find(String tableName, List<GroupCondition> gcs) throws SQLException;
	
	/**
	 * �жϱ���Ƿ����
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(String tableName) throws SQLException;
	
	/**
	 * ɾ��ָ��lid����
	 * @param tableName
	 * @param lid
	 * @throws SQLException
	 */
	public void deleteDirTree(String tableName, String lid) throws SQLException;
	
	public void update(String tableName, List<GroupCondition> items, String lid) throws SQLException;


}
