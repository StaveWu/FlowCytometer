package dao.interfaces;

import java.util.List;

import dao.GroupCondition;
import dao.beans.MenuBean;

public interface IMenuDAO {
	
	/**
	 * ����
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws Exception;
	
	/**
	 * ����id
	 * @param tableName
	 * @param lid
	 * @return
	 * @throws Exception
	 */
	public MenuBean findById(String tableName, String lid) throws Exception;
	
	/**
	 * ��������
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<MenuBean> findAll(String tableName) throws Exception;
	
	/**
	 * ��Ӳ˵�
	 * @param tableName
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public boolean addMenu(String tableName, MenuBean menu) throws Exception;
	
	/**
	 * ͨ��������Ϣ��ѯ
	 * @param tableName
	 * @param gcs
	 * @return
	 * @throws Exception
	 */
	public List<MenuBean> find(String tableName, List<GroupCondition> gcs) throws Exception;

}
