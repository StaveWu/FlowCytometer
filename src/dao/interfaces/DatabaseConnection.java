package dao.interfaces;


import java.sql.Connection;

public interface DatabaseConnection {
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public Connection getConnection();
	
	/**
	 * �ر�����
	 */
	public void close();

}
