package me.yczhang.kit.config.parser;

import me.yczhang.kit.config.ConfigPropParser;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Created by YCZhang on 1/18/16.
 */
public class MailAddressParser implements ConfigPropParser<String[]> {
	@Override
	public String[] parse(@Nullable String value, @Nullable Object[] refs) {
		return StringUtils.isBlank(value) ? null : StringUtils.split(value, ';');
	}
}
