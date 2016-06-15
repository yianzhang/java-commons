package me.yczhang.kit.config.parser;

import me.yczhang.kit.config.ConfigException;
import me.yczhang.kit.config.ConfigPropParser;

import javax.annotation.Nullable;

/**
 * Created by YCZhang on 1/25/16.
 */
public class ClassParser implements ConfigPropParser<Class> {
	@Override
	public Class parse(@Nullable String value, @Nullable Object[] refs) {
		if (value == null)
			return null;
		try {
			return Class.forName(value);
		} catch (ClassNotFoundException e) {
			throw new ConfigException(e);
		}
	}
}
