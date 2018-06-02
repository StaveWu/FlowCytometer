package dao.implementions;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;
import dao.beans.DirTreeBean;
import dao.interfaces.DatabaseConnection;
import dao.interfaces.IDirTreeDAO;

public class DirTreeDAOProxy implements IDirTreeDAO {
	
	private DatabaseConnection dbc;
	private IDirTreeDAO dao;
	
	public DirTreeDAOProxy() {
		dbc = new DirTreeConnection();
		dao = new DirTreeDAOImp(dbc.getConnection());
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		dao.createTable(tableName);
		dbc.close();
	}

	@Override
	public boolean addDirTree(String tableName, DirTreeBean bean) throws SQLException {
		boolean flag = false;
		flag = dao.addDirTree(tableName, bean);
		dbc.close();
		return flag;
	}

	@Override
	public List<DirTreeBean> findAll(String tableName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DirTreeBean> find(String tableName, List<GroupCondition> gcs) throws SQLException {
		List<DirTreeBean> res = dao.find(tableName, gcs);
		dbc.close();
		return res;
	}

	@Override
	public boolean isExist(String tableName) throws SQLException {
		boolean flag = false;
		flag = dao.isExist(tableName);
		dbc.close();
		return flag;
	}

	@Override
	public void deleteDirTree(String tableName, String lid) throws SQLException {
		dao.deleteDirTree(tableName, lid);
		dbc.close();
	}

	@Override
	public void update(String tableName, List<GroupCondition> items, String lid) throws SQLException {
		dao.update(tableName, items, lid);
		dbc.close();
	}

}
