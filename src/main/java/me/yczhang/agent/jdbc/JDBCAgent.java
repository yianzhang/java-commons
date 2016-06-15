package me.yczhang.agent.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by YCZhang on 9/28/15.
 */
public class JDBCAgent implements Closeable {

	protected DataSource dataSource;

	public JDBCAgent(JDBCAgentConfig config) {
		this.dataSource = new DataSource(config.poolProperties);
	}

	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}

	@Override
	public void close() throws IOException {
		this.dataSource.close(true);
	}
}
