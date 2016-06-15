package me.yczhang.kit.jdbc_pool;

import me.yczhang.exception.PropertyNotFoundException;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class JDBCPoolConfiguration {

	private String filename;
	
	private String url;
	private String driverClassName;
	private String username;
	private String password;
	private boolean jmxEnabled;
	private boolean testWhileIdle;
	private boolean testOnBorrow;
	private String validationQuery;
	private boolean testOnReturn;
	private long validationInterval;
	private int timeBetweenEvictionRunsMillis;
	private int maxActive;
	private int initialSize;
	private int maxWait;
	private int removeAbandonedTimeout;
	private int minEvictableIdleTimeMillis;
	private int minIdle;
	private boolean logAbandoned;
	private boolean removeAbandoned;
	
	private PoolProperties poolProperties;
	
	public JDBCPoolConfiguration(String name) throws IOException, PropertyNotFoundException {
		filename = "config/jdbcpool-"+name+".properties";
		load();
	}
	
	public void load() throws IOException, PropertyNotFoundException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filename));

		//Url
		url = properties.getProperty("Url");
		if (url==null)
			throw new PropertyNotFoundException("Property \"Url\" NOT FOUND in FIle \""+filename+"\"");
		url = url.trim();
		
		//DriverClassName
		driverClassName = properties.getProperty("DriverClassName");
		if (driverClassName==null) {
			if (url.startsWith("jdbc:mysql"))
				driverClassName = "com.mysql.jdbc.Driver";
			else if (url.startsWith("jdbc:sqlserver"))
				driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			else if (url.startsWith("jdbc:hive2"))
				driverClassName = "org.apache.hive.jdbc.HiveDriver";
			else if (url.startsWith("jdbc:sqlite"))
				driverClassName = "org.sqlite.JDBC";
			else
				throw new PropertyNotFoundException("Property \"DriverClassName\" NOT FOUND in FIle \""+filename+"\"");
		}
		driverClassName = driverClassName.trim();
		
		//Username
		username = properties.getProperty("Username");
		if (username!=null)
			username.trim();
		
		//Password
		password = properties.getProperty("Password");
		if (password!=null)
			password.trim();
		
		//JmxEnabled
		jmxEnabled = Boolean.parseBoolean(properties.getProperty("JmxEnabled", "true").trim());
		
		//TestWhileIdle
		testWhileIdle = Boolean.parseBoolean(properties.getProperty("TestWhileIdle", "false").trim());
		
		//TestOnBorrow
		testOnBorrow = Boolean.parseBoolean(properties.getProperty("TestOnBorrow", "true").trim());
		
		//ValidationQuery
		validationQuery = properties.getProperty("ValidationQuery", "SELECT 1").trim();
		
		//TestOnReturn
		testOnReturn = Boolean.parseBoolean(properties.getProperty("TestOnReturn", "false").trim());
		
		//ValidationInterval
		validationInterval = Long.parseLong(properties.getProperty("ValidationInterval", "30000").trim());
		
		//TimeBetweenEvictionRunsMillis
		timeBetweenEvictionRunsMillis = Integer.parseInt(properties.getProperty("TimeBetweenEvictionRunsMillis", "30000").trim());
		
		//MaxActive
		maxActive = Integer.parseInt(properties.getProperty("MaxActive", "100").trim());
		
		//InitialSize
		initialSize = Integer.parseInt(properties.getProperty("InitialSize", "3").trim());
		
		//MaxWait
		maxWait = Integer.parseInt(properties.getProperty("MaxWait", "100").trim());
		
		//RemoveAbandonedTimeout
		removeAbandonedTimeout = Integer.parseInt(properties.getProperty("RemoveAbandonedTimeout", "60").trim());
		
		//MinEvictableIdleTimeMillis
		minEvictableIdleTimeMillis = Integer.parseInt(properties.getProperty("MinEvictableIdleTimeMillis", "30000").trim());
		
		//MinIdle
		minIdle = Integer.parseInt(properties.getProperty("MinIdle", "3").trim());
		
		//LogAbandoned
		logAbandoned = Boolean.parseBoolean(properties.getProperty("LogAbandoned", "true").trim());
		
		//RemoveAbandoned
		removeAbandoned = Boolean.parseBoolean(properties.getProperty("RemoveAbandoned", "true").trim());		
		
		poolProperties = new PoolProperties();
		
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
	}
	
	public PoolProperties getPoolProperties() {
		return poolProperties;
	}
}
