package dao.implementions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.interfaces.DatabaseConnection;
import dao.interfaces.IPlotsDAO;
import plot.Plot;

public class PlotsDAOProxy implements IPlotsDAO {
	
	private DatabaseConnection dbc;
	private IPlotsDAO dao;
	
	public PlotsDAOProxy(String path) {
		dbc = new PlotsConnection(path);
		dao = new PlotsDAOImp(dbc.getConnection());
	}

	@Override
	public void createTable(String caret) throws SQLException {
		dao.createTable(caret);
		dbc.close();
	}

	@Override
	public List<Plot> findAll(String caret) throws SQLException {
		List<Plot> plots = new ArrayList<Plot>();
		plots = dao.findAll(caret);
		dbc.close();
		return plots;
	}

	@Override
	public void addAll(String caret, List<Plot> plots) throws SQLException {
		dao.addAll(caret, plots);
		dbc.close();
	}

	@Override
	public boolean isExist(String caret) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
