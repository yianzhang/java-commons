package me.yczhang.kit.jdbc_pool;

import me.yczhang.exception.PropertyNotFoundException;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;


public class JDBCPool {

	private static HashMap<String, JDBCPool> map = new HashMap<String, JDBCPool>();
		
	public static JDBCPool getInstance(String name) throws Exception {
		synchronized (map) {
			if (!map.containsKey(name)) {
				map.put(name, new JDBCPool(name));
			}
			
			return map.get(name);
		}
	}

	public static void closeInstance(String name) {
		synchronized (map) {
			if (map.containsKey(name)) {
				map.get(name).dataSource.close();
				map.remove(name);
			}
		}
	}
	
	private JDBCPoolConfiguration configuration;
	private DataSource dataSource;
	
	private JDBCPool(String name) throws IOException, PropertyNotFoundException {
		configuration = new JDBCPoolConfiguration(name);
		dataSource = new DataSource();
		dataSource.setPoolProperties(configuration.getPoolProperties());
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
}
