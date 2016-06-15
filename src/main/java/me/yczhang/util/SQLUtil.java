package me.yczhang.util;

/**
 * Created by YCZhang on 9/16/15.
 */
public class SQLUtil {

	public static void loadDriver(String dbType) throws ClassNotFoundException {
		Class.forName(getDriverName(dbType));
	}

	public static String getDriverName(String dbType) throws ClassNotFoundException {
		return getDriverNameWithUrl("jdbc:" + dbType.toLowerCase());
	}

	public static void loadDriverWithUrl(String url) throws ClassNotFoundException {
		Class.forName(getDriverNameWithUrl(url));
	}

	public static String getDriverNameWithUrl(String url) throws ClassNotFoundException {
		if (url.startsWith("jdbc:derby"))
			return "org.apache.derby.jdbc.EmbeddedDriver";
		else if (url.startsWith("jdbc:mysql"))
			return "com.mysql.jdbc.Driver";
		else if (url.startsWith("jdbc:oracle"))
			return "oracle.jdbc.driver.OracleDriver";
		else if (url.startsWith("jdbc:microsoft"))
			return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		else if (url.startsWith("jdbc:postgresql"))
			return "org.postgresql.Driver";
		else if (url.startsWith("jdbc:sqlite"))
			return "org.sqlite.JDBC";
		else if (url.startsWith("jdbc:hive2"))
			return "org.apache.hive.jdbc.HiveDriver";
		else
			throw new ClassNotFoundException("cannot identify the driver with url prefix");
	}

}
