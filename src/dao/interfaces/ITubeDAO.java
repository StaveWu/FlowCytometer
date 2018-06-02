package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import dao.GroupCondition;

public interface ITubeDAO {
	
	public void create(String tableName, List<GroupCondition> fields) throws SQLException;
	
	public void addField(String tableName, String field) throws SQLException;

}
