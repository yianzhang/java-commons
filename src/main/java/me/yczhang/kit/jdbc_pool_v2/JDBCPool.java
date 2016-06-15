package me.yczhang.kit.jdbc_pool_v2;

import me.yczhang.exception.PropertyNotFoundException;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Deprecated use agent.jdbc.JDBCAgent instead
 */
@Deprecated
public class JDBCPool {
	
	private DataSource dataSource;
	
	private JDBCPool(JDBCPoolConfiguration configuration) throws IOException, PropertyNotFoundException {
		dataSource = new DataSource(configuration.getPoolProperties());
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void close() {
		dataSource.close();
	}
	
}
