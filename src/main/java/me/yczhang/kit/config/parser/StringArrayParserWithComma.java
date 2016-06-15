package me.yczhang.kit.config.parser;

import me.yczhang.kit.config.ConfigPropParser;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Created by YCZhang on 1/18/16.
 */
public class StringArrayParserWithComma implements ConfigPropParser<String[]> {

	@Override
	public String[] parse(@Nullable String value, @Nullable Object[] refs) {
		if (value == null)
			return null;

		return StringUtils.split(value, ',');
	}
}
