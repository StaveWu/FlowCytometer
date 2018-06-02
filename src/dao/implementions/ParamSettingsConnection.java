package dao.implementions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dao.interfaces.DatabaseConnection;

public class ParamSettingsConnection implements DatabaseConnection {

	private static final String DBDRIVER= "org.h2.Driver";
	private static final String DBURL= "jdbc:h2:file:";
	private static final String DBUSER= "sa";
	private static final String DBPWD= "";
	private Connection conn = null;
	
	public ParamSettingsConnection(String path) {
		try {
			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(DBURL + path, DBUSER, DBPWD);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new RuntimeException("ParamSettings���ݿ����Ӵ���û���ҵ������ļ�");
		} catch (SQLException e2) {
			e2.printStackTrace();
			throw new RuntimeException("ParamSettings���ݿ����Ӵ��󣺼�����ݿ�·�����û����������Ƿ���ȷ");
		}
	}
	
	public Connection getConnection()
	{
		return this.conn;
	}
	
	public void close()
	{
		if(this.conn != null)
		{
			try {
				this.conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
				throw new RuntimeException("���ݿ�û�������رգ�");
			}
		}
	}

}
