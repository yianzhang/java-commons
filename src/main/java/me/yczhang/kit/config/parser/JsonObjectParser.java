package me.yczhang.kit.config.parser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.yczhang.kit.config.ConfigPropParser;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Created by YCZhang on 1/18/16.
 */
public class JsonObjectParser implements ConfigPropParser<JsonObject> {
	@Override
	public JsonObject parse(@Nullable String value, @Nullable Object[] refs) {
		if (StringUtils.isBlank(value))
			return null;

		return new Gson().fromJson(value, JsonObject.class);
	}
}
