package dao.implementions;

import java.sql.SQLException;
import java.util.List;

import dao.beans.TableBean;
import dao.interfaces.DatabaseConnection;
import dao.interfaces.ITableDAO;

public class TableDAOProxy implements ITableDAO {
	
	private ITableDAO dao;
	private DatabaseConnection dbc;
	
	public TableDAOProxy() {
		dbc = new TableConnection();
		dao = new TableDAOImp(dbc.getConnection());
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		
		dao.createTable(tableName);
		dbc.close();
	}

	@Override
	public boolean addTableBean(String tableName, TableBean tb) throws SQLException {
		
		boolean flag = false;
		flag = dao.addTableBean(tableName, tb);
		dbc.close();
		return flag;
	}

	@Override
	public TableBean findById(String tableName, String columnId) throws SQLException {
		
		TableBean tb = dao.findById(tableName, columnId);
		dbc.close();
		return tb;
	}

	@Override
	public List<TableBean> findAll(String tableName) throws SQLException {
		
		List<TableBean> tbs = dao.findAll(tableName);
		dbc.close();
		return tbs;
	}

}
