package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;
import dao.beans.ParamSettingsBean;

public interface IParamSettingsDAO {
	
	/**
	 * 建表
	 * @param tableName
	 * @throws Exception
	 */
	public void createTable(String tableName) throws SQLException;
	
	/**
	 * 查找所有
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<ParamSettingsBean> findAll(String tableName) throws SQLException;
	
	/**
	 * 添加参数设置
	 * @param tableName
	 * @param psbean
	 * @return
	 * @throws Exception
	 */
	public boolean addParamSetting(String tableName, ParamSettingsBean psbean) throws SQLException;

	/**
	 * 删除指定的参数行
	 * @param tableName
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean deleteParamSetting(String tableName, int id) throws SQLException;
	
	public void updateAll(String tableName, List<ParamSettingsBean> beans) throws SQLException;
	
	/**
	 * 判断表是否存在
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean isExist(String tableName) throws SQLException;
	
	/**
	 * 更新表
	 * @param tableName
	 * @param items
	 * @param id
	 * @throws SQLException
	 */
	public void update(String tableName, List<GroupCondition> items, int id) throws SQLException;
	
	/**
	 * 查找所有id
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> findId(String tableName) throws SQLException;

}
