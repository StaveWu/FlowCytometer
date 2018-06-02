package dao.interfaces;

import java.util.List;

import dao.GroupCondition;
import dao.beans.MenuBean;

public interface IMenuDAO {
	
	/**
	 * 建表
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws Exception;
	
	/**
	 * 查找id
	 * @param tableName
	 * @param lid
	 * @return
	 * @throws Exception
	 */
	public MenuBean findById(String tableName, String lid) throws Exception;
	
	/**
	 * 查找所有
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<MenuBean> findAll(String tableName) throws Exception;
	
	/**
	 * 添加菜单
	 * @param tableName
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public boolean addMenu(String tableName, MenuBean menu) throws Exception;
	
	/**
	 * 通过条件信息查询
	 * @param tableName
	 * @param gcs
	 * @return
	 * @throws Exception
	 */
	public List<MenuBean> find(String tableName, List<GroupCondition> gcs) throws Exception;

}
