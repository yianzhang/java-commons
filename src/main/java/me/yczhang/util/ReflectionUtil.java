package me.yczhang.util;

import javax.annotation.Nonnull;
import javax.management.ReflectionException;
import java.lang.reflect.Field;

/**
 * Created by YCZhang on 2/4/16.
 */
public class ReflectionUtil {

	public static Class parseClass(@Nonnull String name) throws ClassNotFoundException {
		switch (name) {
			case "string":
			case "String":
				return String.class;
			case "boolean":
				return boolean.class;
			case "Boolean":
				return Boolean.class;
			case "byte":
				return byte.class;
			case "Byte":
				return Byte.class;
			case "char":
				return char.class;
			case "Character":
				return Character.class;
			case "double":
				return double.class;
			case "Double":
				return Double.class;
			case "float":
				return float.class;
			case "Float":
				return Float.class;
			case "int":
				return int.class;
			case "Integer":
				return Integer.class;
			case "long":
				return long.class;
			case "Long":
				return Long.class;
			case "short":
				return short.class;
			case "Short":
				return Short.class;
			default:
				return Class.forName(name);
		}
	}

	public static Object cast(@Nonnull Class klass, @Nonnull Object object) {
		if (object instanceof Number) {
			Number number = (Number) object;
			if (byte.class.equals(klass) || Byte.class.equals(klass))
				return number.byteValue();
			else if (char.class.equals(klass) || Character.class.equals(klass))
				return (char) number.intValue();
			else if (double.class.equals(klass) || Double.class.equals(klass))
				return number.doubleValue();
			else if (float.class.equals(klass) || Float.class.equals(klass))
				return number.floatValue();
			else if (int.class.equals(klass) || Integer.class.equals(klass))
				return number.intValue();
			else if (long.class.equals(klass) || Long.class.equals(klass))
				return number.longValue();
			else if (short.class.equals(klass) || Short.class.equals(klass))
				return number.shortValue();
		}

		return klass.cast(object);
	}

	public static Class getWrapperClass(Class basicType) {
		if (boolean.class.equals(basicType))
			return Boolean.class;
		else if (byte.class.equals(basicType))
			return Byte.class;
		else if (char.class.equals(basicType))
			return Character.class;
		else if (double.class.equals(basicType))
			return Double.class;
		else if (float.class.equals(basicType))
			return Float.class;
		else if (int.class.equals(basicType))
			return Integer.class;
		else if (long.class.equals(basicType))
			return Long.class;
		else if (short.class.equals(basicType))
			return Short.class;
		else
			return null;
	}

	/**
	 * 获取值
	 * @param object 实例对象，静态为null
	 * @param field 字段
	 * @return 字段值
	 */
	public static Object getFieldValue(Object object, Field field) throws ReflectionException {
		Class klass = field.getType();

		try {
			if (klass.equals(boolean.class)) {
				return field.getBoolean(object);
			} else if (klass.equals(byte.class)) {
				return field.getByte(object);
			} else if (klass.equals(char.class)) {
				return field.getChar(object);
			} else if (klass.equals(double.class)) {
				return field.getDouble(object);
			} else if (klass.equals(float.class)) {
				return field.getFloat(object);
			} else if (klass.equals(int.class)) {
				return field.getInt(object);
			} else if (klass.equals(long.class)) {
				return field.getLong(object);
			} else if (klass.equals(short.class)) {
				return field.getShort(object);
			} else {
				return field.get(object);
			}
		}
		catch (Exception e) {
			throw new ReflectionException(e, "get value of field " + field + " error");
		}
	}

	/**
	 * 设置字段值
	 * @param object 实例对象，静态为null
	 * @param field 字段
	 * @param value 字段值
	 */
	public static void setFieldValue(Object object, Field field, Object value) throws ReflectionException {
		Class klass = field.getType();

		try {
			if (klass.equals(boolean.class)) {
				field.setBoolean(object, (Boolean) value);
			} else if (klass.equals(byte.class)) {
				field.setByte(object, (Byte) value);
			} else if (klass.equals(char.class)) {
				field.setChar(object, (Character) value);
			} else if (klass.equals(double.class)) {
				field.setDouble(object, (Double) value);
			} else if (klass.equals(float.class)) {
				field.setFloat(object, (Float) value);
			} else if (klass.equals(int.class)) {
				field.setInt(object, (Integer) value);
			} else if (klass.equals(long.class)) {
				field.setLong(object, (Long) value);
			} else if (klass.equals(short.class)) {
				field.setShort(object, (Short) value);
			} else {
				field.set(object, value);
			}
		}
		catch (Exception e) {
			throw new ReflectionException(e, "set value of field " + field + " error");
		}
	}
}
