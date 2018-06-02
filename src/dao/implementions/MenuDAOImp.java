package dao.implementions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dao.GroupCondition;
import dao.beans.MenuBean;
import dao.interfaces.IMenuDAO;

public class MenuDAOImp implements IMenuDAO {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public MenuDAOImp(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable(String tableName) throws Exception {
		
		String sql = "create table if not exists "
				+ tableName + "(" + 
				"lid varchar(10) not null primary key," + 
				"name varchar(100)," + 
				"type varchar(20)," + 
				"level int," + 
				"father varchar(10)," + 
				"command varchar(100)" + 
				");";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.execute();
		pstmt.close();
	}

	@Override
	public MenuBean findById(String tableName, String lid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuBean> findAll(String tableName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addMenu(String tableName, MenuBean menu) throws Exception {
		
		boolean flag = false;
		String sql = "insert into "
				+ tableName + "(lid,name,type,level,father,command) values(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, menu.getLid());
		pstmt.setString(2, menu.getName());
		pstmt.setString(3, menu.getType());
		pstmt.setInt(4, menu.getLevel());
		pstmt.setString(5, menu.getFather());
		pstmt.setString(6, menu.getCommand());
		
		if(pstmt.executeUpdate() > 0)
		{  //如果更新的行数大于0
			flag = true;
		}
		pstmt.close();
		return flag;
	}

	@Override
	public List<MenuBean> find(String tableName, List<GroupCondition> gcs) throws Exception {
		
		List<MenuBean> result = new ArrayList<>();
		
		String sql = "select * from "
				+ tableName + " where ";
		Iterator<GroupCondition> iter = gcs.iterator();
		int i = 0;
		while(iter.hasNext()) {
			GroupCondition gc = iter.next();
			if(gc.getName().equals("level")) {
				//level为整型，不加单引号
				sql += gc.getName() + gc.getRela() + gc.getValue();
			}
			else {
				//其他字段为字符型，加单引号
				sql += gc.getName() + gc.getRela() + "'" + gc.getValue() + "'";
			}
			if(i != gcs.size() - 1) {
				sql += " and ";
			}
			i++;
		}
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			MenuBean mb = new MenuBean();
			mb.setLid(rs.getString("lid"));
			mb.setName(rs.getString("name"));
			mb.setType(rs.getString("type"));
			mb.setLevel(rs.getInt("level"));
			mb.setFather(rs.getString("father"));
			mb.setCommand(rs.getString("command"));
			result.add(mb);
		}
		return result;
	}


}
