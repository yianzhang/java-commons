package me.yczhang.agent.jdbc;

import me.yczhang.util.SQLUtil;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by YCZhang on 9/28/15.
 */
public class JDBCAgentConfig {

	protected PoolProperties poolProperties = new PoolProperties();

	public JDBCAgentConfig() {
		validationQuery("SELECT 1");
	}

	public JDBCAgentConfig url(String url) throws ClassNotFoundException {
		SQLUtil.loadDriverWithUrl(url);
		this.poolProperties.setUrl(url);
		this.poolProperties.setDriverClassName(SQLUtil.getDriverNameWithUrl(url));
		return this;
	}

	public JDBCAgentConfig username(String username) {
		this.poolProperties.setUsername(username);
		return this;
	}

	public JDBCAgentConfig password(String password) {
		this.poolProperties.setPassword(password);
		return this;
	}

	public JDBCAgentConfig testWhileIdle(boolean testWhileIdle) {
		this.poolProperties.setTestWhileIdle(testWhileIdle);
		return this;
	}

	public JDBCAgentConfig testOnBorrow(boolean testOnBorrow) {
		this.poolProperties.setTestOnBorrow(testOnBorrow);
		return this;
	}

	public JDBCAgentConfig testOnReturn(boolean testOnReturn) {
		this.poolProperties.setTestOnReturn(testOnReturn);
		return this;
	}

	public JDBCAgentConfig validationQuery(String validationQuery) {
		this.poolProperties.setValidationQuery(validationQuery);
		return this;
	}

	public JDBCAgentConfig validationInterval_ms(long validationInterval_ms) {
		this.poolProperties.setValidationInterval(validationInterval_ms);
		return this;
	}

	public JDBCAgentConfig maxActive(int maxActive) {
		this.poolProperties.setMaxActive(maxActive);
		return this;
	}

	public JDBCAgentConfig initialSize(int initialSize) {
		this.poolProperties.setInitialSize(initialSize);
		return this;
	}

	public JDBCAgentConfig maxWait(int maxWait) {
		this.poolProperties.setMaxWait(maxWait);
		return this;
	}

	public JDBCAgentConfig minIdle(int minIdle) {
		this.poolProperties.setMinIdle(minIdle);
		return this;
	}

	public JDBCAgentConfig logAbandoned(boolean logAbandoned) {
		this.poolProperties.setLogAbandoned(logAbandoned);
		return this;
	}

	public JDBCAgentConfig removeAbandoned(boolean removeAbandoned) {
		this.poolProperties.setRemoveAbandoned(removeAbandoned);
		return this;
	}

	public static JDBCAgentConfig parseMap(Map<String, String> props) throws ClassNotFoundException {
		return parseMap(props, "");
	}

	public static JDBCAgentConfig parseMap(Map<String, String> props, String prefix) throws ClassNotFoundException {
		if (prefix == null)
			prefix = "";
		else if (prefix.length() > 0)
			prefix = prefix + ".";

		JDBCAgentConfig config = new JDBCAgentConfig();

		String tmp = null;

		tmp = props.get(prefix + "URL");
		if (tmp != null) config.url(tmp);

		tmp = props.get(prefix + "Username");
		if (tmp != null) config.username(tmp);

		tmp = props.get(prefix + "Password");
		if (tmp != null) config.password(tmp);

		tmp = props.get(prefix + "TestWhileIdle");
		if (tmp != null) config.testWhileIdle(Boolean.parseBoolean(tmp));

		tmp = props.get(prefix + "TestOnBorrow");
		if (tmp != null) config.testOnBorrow(Boolean.parseBoolean(tmp));

		tmp = props.get(prefix + "TestOnReturn");
		if (tmp != null) config.testOnReturn(Boolean.parseBoolean(tmp));

		tmp = props.get(prefix + "ValidationQuery");
		if (tmp != null) config.validationQuery(tmp);

		tmp = props.get(prefix + "ValidationInterval.ms");
		if (tmp != null) config.validationInterval_ms(Long.parseLong(tmp));

		tmp = props.get(prefix + "MaxActive");
		if (tmp != null) config.maxActive(Integer.parseInt(tmp));

		tmp = props.get(prefix + "InitialSize");
		if (tmp != null) config.initialSize(Integer.parseInt(tmp));

		tmp = props.get(prefix + "MaxWait");
		if (tmp != null) config.maxWait(Integer.parseInt(tmp));

		tmp = props.get(prefix + "MinIdle");
		if (tmp != null) config.minIdle(Integer.parseInt(tmp));

		tmp = props.get(prefix + "LogAbandoned");
		if (tmp != null) config.logAbandoned(Boolean.parseBoolean(tmp));

		tmp = props.get(prefix + "RemoveAbandoned");
		if (tmp != null) config.removeAbandoned(Boolean.parseBoolean(tmp));

		return config;
	}

	public static Map<String, JDBCAgentConfig> parseMaps(Map<String, String> props) throws ClassNotFoundException {
		return parseMaps(props, "");
	}

	public static Map<String, JDBCAgentConfig> parseMaps(Map<String, String> props, String prefix) throws ClassNotFoundException {
		if (prefix == null)
			prefix = "";
		else if (!prefix.isEmpty())
			prefix += ".";

		final String PREFIX_JDBCS = prefix + "JDBCs";

		Set<String> keys = new HashSet<>();
		for (String key : props.keySet()) {
			if (key.startsWith(PREFIX_JDBCS)) {
				String[] fields = key.substring(PREFIX_JDBCS.length()).split("\\.");
				if (fields.length < 2)
					continue;
				keys.add(fields[0]);
			}
		}

		Map<String, JDBCAgentConfig> configs = new HashMap<>();
		for (String key : keys) {
			JDBCAgentConfig config = parseMap(props, PREFIX_JDBCS + key);
			if (config != null)
				configs.put(key, config);
		}

		return configs;
	}
}
