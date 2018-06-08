package dao.implementions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dao.GroupCondition;
import dao.beans.ParamSettingsBean;
import dao.interfaces.IParamSettingsDAO;

public class ParamSettingsImp implements IParamSettingsDAO {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public ParamSettingsImp(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		String sql = "create table if not exists "
				+ tableName + "(" + 
				"id int auto_increment primary key," +
				"参数名 varchar(100)," + 
				"电压值 int," + 
				"阈值 int," + 
				"A boolean," + 
				"H boolean," + 
				"W boolean" + 
				");";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.execute();
		pstmt.close();
	}

	@Override
	public List<ParamSettingsBean> findAll(String tableName) throws SQLException {
		List<ParamSettingsBean> psBeans = new ArrayList<>();
		
		String sql = "select 参数名,电压值,阈值,A,H,W from " + tableName;
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			ParamSettingsBean psb = new ParamSettingsBean();
			psb.setParamName(rs.getString(1));
			psb.setVoltage(rs.getInt(2));
			psb.setThreshold(rs.getInt(3));
			psb.setA(rs.getBoolean(4));
			psb.setH(rs.getBoolean(5));
			psb.setW(rs.getBoolean(6));
			psBeans.add(psb);
		}
		
		return psBeans;
	}

	@Override
	public boolean addParamSetting(String tableName, ParamSettingsBean psbean) throws SQLException {
		boolean flag = false;
		String sql = "insert into "
				+ tableName + "(参数名,电压值,阈值,A,H,W) values(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, psbean.getParamName());
		pstmt.setInt(2, psbean.getVoltage());
		pstmt.setInt(3, psbean.getThreshold());
		pstmt.setBoolean(4, psbean.isA());
		pstmt.setBoolean(5, psbean.isH());
		pstmt.setBoolean(6, psbean.isW());
		
		if(pstmt.executeUpdate() > 0)
		{//如果更新的行数大于0
			flag = true;
		}
		pstmt.close();
		return flag;
	}

	@Override
	public boolean deleteParamSetting(String tableName, int id) throws SQLException {
		boolean flag = false;
		String sql = "delete from " + tableName + " where id = '" + id + "'";
		pstmt = conn.prepareStatement(sql);
		if(pstmt.executeUpdate() > 0) {
			flag = true;
		}
		pstmt.close();
		return flag;
	}

	@Override
	public boolean isExist(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
		return rs.next();
	}

	@Override
	public void update(String tableName, List<GroupCondition> items, int id) throws SQLException {
		String sql = "update " + tableName + " set ";
		Iterator<GroupCondition> iter = items.iterator();
		int i = 0;
		while (iter.hasNext()) {
			GroupCondition gc = (GroupCondition) iter.next();
			sql += gc.getName() + gc.getRela() + "'" + gc.getValue() + "'";
			if(i != items.size() - 1) {
				sql += " and ";
			}
			i++;
		}
		sql += " where id = " + id;
		pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();
	}

	@Override
	public List<Integer> findId(String tableName) throws SQLException {
		List<Integer> res = new ArrayList<>();
		String sql = "select id from " + tableName;
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			res.add(rs.getInt(1));
		}
		return res;
	}

	@Override
	public void updateAll(String tableName, List<ParamSettingsBean> beans) throws SQLException {
		// 先清空数据库
		String deleteSql = "delete from " + tableName;
		pstmt = conn.prepareStatement(deleteSql);
		pstmt.execute();
		// 再添加数据
		String sql = "insert into " + tableName
				+ "(参数名,电压值,阈值,A,H,W) values(?,?,?,?,?,?)";
		for (ParamSettingsBean b : beans) {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b.getParamName());
			pstmt.setInt(2, b.getVoltage());
			pstmt.setInt(3, b.getThreshold());
			pstmt.setBoolean(4, b.isA());
			pstmt.setBoolean(5, b.isH());
			pstmt.setBoolean(6, b.isW());
			pstmt.executeUpdate();
		}
		
		pstmt.close();
	}

}
