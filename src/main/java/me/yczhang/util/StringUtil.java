package me.yczhang.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * Created by YCZhang on 5/14/15.
 */
public class StringUtil {

	public static final String PATTERN_COMMA = Pattern.quote(",");
	public static final String PATTERN_SEMICOLON = Pattern.quote(";");
	public static final String PATTERN_OR = Pattern.quote("|");
	public static final String PATTERN_AND = Pattern.quote("&");

	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static String isBlank(String str, String def) {
		return isBlank(str) ? def : str;
	}

	public static String dumpInputStreamToStringAndClose(InputStream is) throws IOException {
		return dumpInputStreamToStringAndClose(is, "utf-8");
	}

	public static String dumpInputStreamToStringAndClose(InputStream is, String charset) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, charset));
			char[] chars = new char[1024];
			while (true) {
				int len = reader.read(chars, 0, 1024);
				if (len==-1)
					break;

				stringBuilder.append(chars, 0, len);
			}
		} finally {
			if (reader!=null) try { reader.close(); } catch (Exception e) {}
		}

		return stringBuilder.toString();
	}

	public static String dumpStackTraceToString(Throwable t) {
		StringWriter result = new StringWriter();
		PrintWriter writer = new PrintWriter(result);
		t.printStackTrace(writer);
		return result.toString();
	}

	/**
	 * 解析
	 * @param klass 值类型
	 * @param value 属性值
	 * @return
	 */
	public static Object parse(@Nonnull Class klass, @Nullable String value) throws ParseException {
		if (value == null)
			return null;

		if (String.class.equals(klass)) {
			return value;
		}
		else if (boolean.class.equals(klass) || Boolean.class.equals(klass)) {
			return Boolean.parseBoolean(value);
		}
		else if (byte.class.equals(klass) || Byte.class.equals(klass)) {
			return Byte.parseByte(value);
		}
		else if (char.class.equals(klass) || Character.class.equals(klass)) {
			if (value.length() == 1)
				return value.charAt(0);
			else
				throw new ParseException("cannot parse " + value + " to char", 0);
		}
		else if (double.class.equals(klass) || Double.class.equals(klass)) {
			return Double.parseDouble(value);
		}
		else if (float.class.equals(klass) || Float.class.equals(klass)) {
			return Float.parseFloat(value);
		}
		else if (int.class.equals(klass) || Integer.class.equals(klass)) {
			return Integer.parseInt(value);
		}
		else if (long.class.equals(klass) || Long.class.equals(klass)) {
			return Long.parseLong(value);
		}
		else if (short.class.equals(klass) || Short.class.equals(klass)) {
			return Short.parseShort(value);
		}
		else {
			throw new ParseException("cannot parse " + value + " to class " + klass, 0);
		}
	}

	public static Object parse(@Nonnull String type, String value) throws ParseException, ClassNotFoundException {
		return parse(ReflectionUtil.parseClass(type), value);
	}

}
