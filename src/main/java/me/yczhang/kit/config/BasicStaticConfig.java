package me.yczhang.kit.config;

import jodd.props.Props;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YCZhang on 1/25/16.
 */
public class BasicStaticConfig {

	protected static Props props = new Props();

	protected static void loadResource(@Nullable String... propStrs) {
		if (propStrs != null && propStrs.length > 0) {
			props.load(StringUtils.join(propStrs, '\n'));
		}
	}

	protected static void loadResource(@Nullable File file, @Nullable String encoding) throws IOException {
		if (file != null && file.exists()) {
			props.load(file, encoding == null ? "utf8" : encoding);
		}
	}

	protected static void loadResource(@Nullable File file) throws IOException {
		loadResource(file, null);
	}

	protected static void loadResource(@Nullable InputStream inputStream, @Nullable String encoding) throws IOException {
		if (inputStream != null) {
			props.load(inputStream, encoding == null ? "utf8" : encoding);
		}
	}

	protected static void loadResource(@Nullable InputStream inputStream) throws IOException {
		loadResource(inputStream, null);
	}

	protected static void loadResource(@Nullable Map<String, String> map, @Nullable String prefix) {
		if (map != null) {
			if (prefix == null)
				props.load(map);
			else
				props.load(map, prefix);
		}
	}

	protected static void loadResource(@Nullable Map<String, String> map) {
		if (map != null) {
			props.load(map);
		}
	}

	protected static void loadFile(@Nonnull String filePath, @Nullable String encoding) throws IOException {
		Objects.requireNonNull(filePath);

		File file = new File(filePath);
		loadResource(file, encoding);
	}

	protected static void loadFile(@Nonnull String filePath) throws IOException {
		loadFile(filePath, null);
	}

	protected static void loadClassPathFile(@Nonnull String filePath, @Nullable String encoding) throws IOException {
		Objects.requireNonNull(filePath);

		InputStream inputStream = BasicStaticConfig.class.getResourceAsStream(filePath);
		loadResource(inputStream, encoding);
	}

	protected static void loadClassPathFile(@Nonnull String filePath) throws IOException {
		loadClassPathFile(filePath, null);
	}

	protected static void inject(@Nonnull Class klass) {
		ConfigFactory.create(props).inject(klass, null);
	}

	public static Map<String, String> innerMap(String prefix) {
		if (props == null)
			return null;

		Map<String, String> ret = new HashMap<>();
		props.innerMap(prefix).forEach((x, y) -> ret.put(x, (String) y));

		return ret;
	}
}
