package me.yczhang.kit.config;

import javax.annotation.Nullable;

/**
 * Created by YCZhang on 9/14/15.
 */
@FunctionalInterface
public interface ConfigPropParser<T> {
	/**
	 * 解析
	 * @param value 属性值，可能为null
	 * @param refs 依赖的字段的值，顺序是ConfigField注解中refFields的顺序，可能为null或者空
	 * @return
	 */
	public T parse(@Nullable String value, @Nullable Object[] refs) throws ConfigException;
}
