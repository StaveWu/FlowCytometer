package dao.implementions;

import java.util.List;

import dao.GroupCondition;
import dao.beans.MenuBean;
import dao.interfaces.DatabaseConnection;
import dao.interfaces.IMenuDAO;

public class MenuDAOProxy implements IMenuDAO {
	
	private DatabaseConnection dbc;
	private IMenuDAO dao;
	
	public MenuDAOProxy() {
		dbc = new MenuConnection();
		dao = new MenuDAOImp(dbc.getConnection());
	}

	@Override
	public void createTable(String tableName) throws Exception {
		
		dao.createTable(tableName);
		dbc.close();
	}

	@Override
	public MenuBean findById(String tableName, String lid) throws Exception {
		
		MenuBean mb = dao.findById(tableName, lid);
		dbc.close();
		return mb;
	}

	@Override
	public List<MenuBean> findAll(String tableName) throws Exception {
		
		List<MenuBean> res = dao.findAll(tableName);
		dbc.close();
		return res;
	}

	@Override
	public boolean addMenu(String tableName, MenuBean menu) throws Exception {
		
		boolean flag = false;
		flag = dao.addMenu(tableName, menu);
		dbc.close();
		return flag;
	}

	@Override
	public List<MenuBean> find(String tableName, List<GroupCondition> gcs) throws Exception {
		
		List<MenuBean> res = dao.find(tableName, gcs);
		dbc.close();
		return res;
	}

}
