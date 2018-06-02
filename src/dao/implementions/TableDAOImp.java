package dao.implementions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.beans.TableBean;
import dao.interfaces.ITableDAO;

public class TableDAOImp implements ITableDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	
	public TableDAOImp(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		
		String sql = "create table if not exists "
				+ tableName + "(" + 
				"columnId int(10) not null primary key," + 
				"columnName varchar(100)," + 
				"command varchar(100)" + 
				");";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.execute();
		pstmt.close();
	}

	@Override
	public TableBean findById(String tableName, String columnId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TableBean> findAll(String tableName) throws SQLException {
		
		List<TableBean> tbs = new ArrayList<>();
		
		String sql = "select * from " + tableName;
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			TableBean tb = new TableBean();
			tb.setColumnId(rs.getInt(1));
			tb.setColumnName(rs.getString(2));
			tb.setCommand(rs.getString(3));
			tbs.add(tb);
		}
		
		return tbs;
	}

	@Override
	public boolean addTableBean(String tableName, TableBean tb) throws SQLException {
		
		boolean flag = false;
		String sql = "insert into "
				+ tableName + "(columnid,columnname,command) values(?,?,?)";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, tb.getColumnId());
		pstmt.setString(2, tb.getColumnName());
		pstmt.setString(3, tb.getCommand());
		
		if(pstmt.executeUpdate() > 0)
		{  //如果更新的行数大于0
			flag = true;
		}
		pstmt.close();
		return flag;
	}
	
	

}
