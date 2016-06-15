package me.yczhang.kit.bank;

import me.yczhang.exception.NotFoundException;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YCZhang on 8/3/15.
 */
@ThreadSafe
public class ObjBank {

	private static Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public static void put(String key, Object obj) {
		if (key == null || obj == null)
			return;

		map.put(key, obj);
	}

	public static Object get(String key) {
		return map.get(key);
	}

	public static Object getOrThrows(String key) {
		Object value = map.get(key);
		if (null == value)
			throw new NotFoundException("Object of key " + key + " not found");
		return value;
	}

	public static <T> T get(String key, Class<T> klass) {
		return klass.cast(map.get(key));
	}

	public static <T> T getOr(String key, T def) {
		T value = (T) map.get(key);
		return value == null ? def : value;
	}

	public static <T> T getOrThrows(String key, Class<T> klass) {
		Object value = map.get(key);
		if (null == value)
			throw new NotFoundException("Object of key " + key + " not found");
		return klass.cast(value);
	}

	public static boolean contains(String key) {
		return map.containsKey(key);
	}

	public static void remove(String key) {
		map.remove(key);
	}

	public static void clear() {
		map.clear();
	}

}
