package dao.implementions;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;
import dao.beans.ParamSettingsBean;
import dao.interfaces.DatabaseConnection;
import dao.interfaces.IParamSettingsDAO;

public class ParamSettingsDAOProxy implements IParamSettingsDAO {
	
	private DatabaseConnection dbc;
	private IParamSettingsDAO dao;
	
	public ParamSettingsDAOProxy(String path) {
		this.dbc = new ParamSettingsConnection(path);
		this.dao = new ParamSettingsImp(dbc.getConnection());
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		dao.createTable(tableName);
		dbc.close();
	}

	@Override
	public List<ParamSettingsBean> findAll(String tableName) throws SQLException {
		List<ParamSettingsBean> res = dao.findAll(tableName);
		dbc.close();
		return res;
	}

	@Override
	public boolean addParamSetting(String tableName, ParamSettingsBean psbean) throws SQLException {
		boolean flag = false;
		flag = dao.addParamSetting(tableName, psbean);
		dbc.close();
		return flag;
	}

	@Override
	public boolean deleteParamSetting(String tableName, int id) throws SQLException {
		boolean flag = false;
		flag = dao.deleteParamSetting(tableName, id);
		dbc.close();
		return flag;
	}

	@Override
	public boolean isExist(String tableName) throws SQLException {
		boolean flag = false;
		flag = dao.isExist(tableName);
		dbc.close();
		return flag;
	}

	@Override
	public void update(String tableName, List<GroupCondition> items, int id) throws SQLException {
		dao.update(tableName, items, id);
		dbc.close();
	}

	@Override
	public List<Integer> findId(String tableName) throws SQLException {
		List<Integer> res = dao.findId(tableName);
		dbc.close();
		return res;
	}

	@Override
	public void updateAll(String tableName, List<ParamSettingsBean> beans) throws SQLException {
		dao.updateAll(tableName, beans);
		dbc.close();
	}

}
