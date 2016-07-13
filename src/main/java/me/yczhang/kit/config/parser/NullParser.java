package me.yczhang.kit.config.parser;

import me.yczhang.kit.config.ConfigException;
import me.yczhang.kit.config.ConfigPropParser;

import javax.annotation.Nullable;

/**
 * Created by YCZhang on 6/17/16.
 */
public class NullParser implements ConfigPropParser<Object> {

	public static final NullParser PARSER = new NullParser();

	@Override
	public Object parse(@Nullable String value, @Nullable Object[] refs) throws ConfigException {
		return null;
	}
}
