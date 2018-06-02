package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import plot.Plot;

public interface IPlotsDAO {
	
	public void createTable(String caret) throws SQLException;
	
	public List<Plot> findAll(String caret) throws SQLException;
	
	public void addAll(String caret, List<Plot> plots) throws SQLException;
	
	public boolean isExist(String caret) throws SQLException;

}
