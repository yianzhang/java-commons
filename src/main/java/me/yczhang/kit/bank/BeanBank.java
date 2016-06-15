package me.yczhang.kit.bank;

import me.yczhang.exception.NotFoundException;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ObjBank的实例版本
 * Created by YCZhang on 2/4/16.
 */
@ThreadSafe
public class BeanBank {

	private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public void put(String key, Object obj) {
		if (key == null || obj == null)
			return;

		map.put(key, obj);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public Object getOrThrows(String key) {
		Object value = map.get(key);
		if (null == value)
			throw new NotFoundException("Object of key " + key + " not found");
		return value;
	}

	public <T> T get(String key, Class<T> klass) {
		return klass.cast(map.get(key));
	}

	public <T> T getOr(String key, T def) {
		T value = (T) map.get(key);
		return value == null ? def : value;
	}

	public <T> T getOrThrows(String key, Class<T> klass) {
		Object value = map.get(key);
		if (null == value)
			throw new NotFoundException("Object of key " + key + " not found");
		return klass.cast(value);
	}

	public boolean contains(String key) {
		return map.containsKey(key);
	}

	public void remove(String key) {
		map.remove(key);
	}

	public void clear() {
		map.clear();
	}
}
