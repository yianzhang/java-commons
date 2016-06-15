package me.yczhang.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetJsonUtil {
	
	public static String toJsonStringWithNextLine(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();

		JsonObject o = new JsonObject();

		if(columnCount>0 && rs.next()) {
			for(int i=0;i<columnCount;++i) {
				String key = meta.getColumnLabel(i+1);
				String value = rs.getString(i+1);

				o.addProperty(key, value);
			}
		}
		else {
			return "{}";
		}

		return o.toString();
	}

	public static JsonObject toJsonWithNextLine(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();

		JsonObject o = new JsonObject();

		if(columnCount>0 && rs.next()) {
			for(int i=0;i<columnCount;++i) {
				String key = meta.getColumnLabel(i+1);
				String value = rs.getString(i+1);

				o.addProperty(key, value);
			}
		}

		return o;
	}
	
	public static String toJsonStringWithAllLines(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();

		JsonArray a = new JsonArray();
		if(columnCount>0) {
			while (rs.next()) {
				JsonObject o = new JsonObject();
				for(int i=0;i<columnCount;++i) {
					String key = meta.getColumnLabel(i+1);
					String value = rs.getString(i+1);

					o.addProperty(key, value);
				}
				a.add(o);
			}
		}
		else {
			return "[]";
		}

		return a.toString();
	}

	public static JsonArray toJsonWithAllLines(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();

		JsonArray a = new JsonArray();
		if(columnCount>0) {
			while (rs.next()) {
				JsonObject o = new JsonObject();
				for(int i=0;i<columnCount;++i) {
					String key = meta.getColumnLabel(i+1);
					String value = rs.getString(i+1);

					o.addProperty(key, value);
				}
				a.add(o);
			}
		}

		return a;
	}

}
