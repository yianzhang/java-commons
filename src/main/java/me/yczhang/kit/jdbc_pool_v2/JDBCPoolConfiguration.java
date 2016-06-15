package me.yczhang.kit.jdbc_pool_v2;

import me.yczhang.exception.AppRunException;
import me.yczhang.util.SQLUtil;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class JDBCPoolConfiguration {


	protected String url;
	protected String driverClassName;
	protected String username;
	protected String password;
	protected boolean jmxEnabled = true;
	protected boolean testWhileIdle = false;
	protected boolean testOnBorrow = true;
	protected String validationQuery = "SELECT 1";
	protected boolean testOnReturn = false;
	protected long validationInterval = 30000;
	protected int timeBetweenEvictionRunsMillis = 30000;
	protected int maxActive = 100;
	protected int initialSize = 3;
	protected int maxWait = 100;
	protected int removeAbandonedTimeout = 60;
	protected int minEvictableIdleTimeMillis = 30000;
	protected int minIdle = 3;
	protected boolean logAbandoned = true;
	protected boolean removeAbandoned = true;

	private PoolProperties poolProperties;

	public JDBCPoolConfiguration() {

	}

	public JDBCPoolConfiguration(String url, String username, String password) {
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}

	public JDBCPoolConfiguration setUrl(String url) {
		this.url = url;
		setDriverClassName(null);
		return this;
	}

	public JDBCPoolConfiguration setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
		if (this.driverClassName==null) {
			try {
				this.driverClassName = SQLUtil.getDriverNameWithUrl(url);
			} catch (ClassNotFoundException e) {
				throw new AppRunException("DriverClassName not identify");
			}
		}

		try {
			Class.forName(this.driverClassName);
		} catch (ClassNotFoundException e) {
			throw new AppRunException(e);
		}

		return this;
	}

	public JDBCPoolConfiguration setUsername(String username) {
		this.username = username;
		return this;
	}

	public JDBCPoolConfiguration setPassword(String password) {
		this.password = password;
		return this;
	}

	public JDBCPoolConfiguration setJmxEnabled(boolean jmxEnabled) {
		this.jmxEnabled = jmxEnabled;
		return this;
	}

	public JDBCPoolConfiguration setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
		return this;
	}

	public JDBCPoolConfiguration setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
		return this;
	}

	public JDBCPoolConfiguration setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
		return this;
	}

	public JDBCPoolConfiguration setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
		return this;
	}

	public JDBCPoolConfiguration setValidationInterval(long validationInterval) {
		this.validationInterval = validationInterval;
		return this;
	}

	public JDBCPoolConfiguration setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		return this;
	}

	public JDBCPoolConfiguration setMaxActive(int maxActive) {
		this.maxActive = maxActive;
		return this;
	}

	public JDBCPoolConfiguration setInitialSize(int initialSize) {
		this.initialSize = initialSize;
		return this;
	}

	public JDBCPoolConfiguration setMaxWait(int maxWait) {
		this.maxWait = maxWait;
		return this;
	}

	public JDBCPoolConfiguration setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
		return this;
	}

	public JDBCPoolConfiguration setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		return this;
	}

	public JDBCPoolConfiguration setMinIdle(int minIdle) {
		this.minIdle = minIdle;
		return this;
	}

	public JDBCPoolConfiguration setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
		return this;
	}

	public JDBCPoolConfiguration setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
		return this;
	}

	public PoolProperties getPoolProperties() {
		PoolProperties poolProperties = new PoolProperties();

		poolProperties.setUrl(url);
		poolProperties.setDriverClassName(driverClassName);
		poolProperties.setUsername(username);
		poolProperties.setPassword(password);
		poolProperties.setJmxEnabled(jmxEnabled);
		poolProperties.setTestWhileIdle(testWhileIdle);
		poolProperties.setTestOnBorrow(testOnBorrow);
		poolProperties.setValidationQuery(validationQuery);
		poolProperties.setTestOnReturn(testOnReturn);
		poolProperties.setValidationInterval(validationInterval);
		poolProperties.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		poolProperties.setMaxActive(maxActive);
		poolProperties.setInitialSize(initialSize);
		poolProperties.setMaxWait(maxWait);
		poolProperties.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		poolProperties.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		poolProperties.setMinIdle(minIdle);
		poolProperties.setLogAbandoned(logAbandoned);
		poolProperties.setRemoveAbandoned(removeAbandoned);
		poolProperties.setJdbcInterceptors(
				"org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
						"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
		);

		return poolProperties;
	}
}
