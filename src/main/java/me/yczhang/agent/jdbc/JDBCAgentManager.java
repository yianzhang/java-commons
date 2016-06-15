package me.yczhang.agent.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YCZhang on 9/29/15.
 */
public class JDBCAgentManager implements Closeable {

	protected Map<String, JDBCAgent> agents = new ConcurrentHashMap<>();

	public JDBCAgentManager(Map<String, JDBCAgentConfig> configs) {
		for (Map.Entry<String, JDBCAgentConfig> entry : configs.entrySet()) {
			agents.put(entry.getKey(), new JDBCAgent(entry.getValue()));
		}
	}

	public JDBCAgent getAgent(String name) {
		return agents.get(name);
	}

	public Connection getConnection(String name) throws SQLException {
		return agents.containsKey(name) ? agents.get(name).getConnection() : null;
	}

	@Override
	public void close() throws IOException {
		for (JDBCAgent agent : agents.values()) {
			agent.close();
		}
	}
}
