package dao.implementions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dao.GroupCondition;
import dao.beans.DirTreeBean;
import dao.interfaces.IDirTreeDAO;

public class DirTreeDAOImp implements IDirTreeDAO {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public DirTreeDAOImp(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		String sql = "create table if not exists "
				+ tableName + "(" + 
				"lid varchar(10) not null primary key," + 
				"name varchar(100)," + 
				"nodetype varchar(20)," + 
				"filetype varchar(20)," + 
				"level int," + 
				"father varchar(10)" + 
				");";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.execute();
		pstmt.close();
	}

	@Override
	public boolean addDirTree(String tableName, DirTreeBean bean) throws SQLException {
		boolean flag = false;
		String sql = "insert into "
				+ tableName + "(lid,name,nodetype,filetype,level,father) values(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, bean.getLid());
		pstmt.setString(2, bean.getName());
		pstmt.setString(3, bean.getNodeType().toString());
		pstmt.setString(4, bean.getFileType().toString());
		pstmt.setInt(5, bean.getLevel());
		pstmt.setString(6, bean.getFather());
		
		if(pstmt.executeUpdate() > 0){
			flag = true;
		}
		pstmt.close();
		return flag;
	}

	@Override
	public List<DirTreeBean> findAll(String tableName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DirTreeBean> find(String tableName, List<GroupCondition> gcs) throws SQLException {
		List<DirTreeBean> result = new ArrayList<>();
		
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
			DirTreeBean mb = new DirTreeBean();
			mb.setLid(rs.getString("lid"));
			mb.setName(rs.getString("name"));
			mb.setFileType(rs.getString("filetype"));
			mb.setLevel(rs.getInt("level"));
			mb.setFather(rs.getString("father"));
			mb.setNodeType(rs.getString("nodetype"));
			
			result.add(mb);
		}
		return result;
	}

	@Override
	public boolean isExist(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
		return rs.next();
	}

	@Override
	public void deleteDirTree(String tableName, String lid) throws SQLException {
		String sql = "delete from " + tableName + " where lid = " + "'" + lid + "'";
		pstmt = conn.prepareStatement(sql);
		pstmt.execute();
		pstmt.close();
	}

	@Override
	public void update(String tableName, List<GroupCondition> items, String lid) throws SQLException {
		String sql = "update " + tableName + " set ";
		Iterator<GroupCondition> iter = items.iterator();
		int i = 0;
		while (iter.hasNext()) {
			GroupCondition gc = (GroupCondition) iter.next();
			if(gc.getName().equals("level")) {
				//level为整型，不加单引号
				sql += gc.getName() + gc.getRela() + gc.getValue();
			}
			else {
				//其他字段为字符型，加单引号
				sql += gc.getName() + gc.getRela() + "'" + gc.getValue() + "'";
			}
			if(i != items.size() - 1) {
				sql += " and ";
			}
			i++;
		}
		sql += " where lid = " + "'" + lid + "'";
		pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();
	}

}
