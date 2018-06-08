package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;
import dao.beans.ParamSettingsBean;

public interface IParamSettingsDAO {
	
	/**
	 * ����
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws SQLException;
	
	/**
	 * ��������
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<ParamSettingsBean> findAll(String tableName) throws SQLException;
	
	/**
	 * ��Ӳ�������
	 * @param tableName
	 * @param psbean
	 * @return
	 * @throws Exception
	 */
	public boolean addParamSetting(String tableName, ParamSettingsBean psbean) throws SQLException;

	/**
	 * ɾ��ָ���Ĳ�����
	 * @param tableName
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean deleteParamSetting(String tableName, int id) throws SQLException;
	
	public void updateAll(String tableName, List<ParamSettingsBean> beans) throws SQLException;
	
	/**
	 * �жϱ��Ƿ����
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean isExist(String tableName) throws SQLException;
	
	/**
	 * ���±�
	 * @param tableName
	 * @param items
	 * @param id
	 * @throws SQLException
	 */
	public void update(String tableName, List<GroupCondition> items, int id) throws SQLException;
	
	/**
	 * ��������id
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> findId(String tableName) throws SQLException;

}
