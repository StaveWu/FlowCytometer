package dao.interfaces;


import java.sql.Connection;

public interface DatabaseConnection {
	
	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConnection();
	
	/**
	 * 关闭连接
	 */
	public void close();

}
