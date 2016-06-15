package me.yczhang.kit.http_server_v2.util;

import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YCZhang on 8/11/15.
 */
public class HttpRequestUtil {

	public static String getPath(String uriString) throws URISyntaxException {
		return new URI(uriString).getPath();
	}

	public static Map<String, List<String>> getParams(String uriString) {
		return new QueryStringDecoder(uriString).parameters();
	}

	public static Map<String, String> getSingleParams(String uriString) {
		return getSingleParams(uriString, false);
	}

	public static Map<String, String> getSingleParams(String uriString, boolean keyIgnoreCase) {
		Map<String, List<String>> tmp = getParams(uriString);
		if (tmp == null)
			return null;

		Map<String, String> ret = new HashMap<>();
		for (Map.Entry<String, List<String>> e : tmp.entrySet()) {
			String key = keyIgnoreCase ? e.getKey().toLowerCase() : e.getKey();
			List<String> value = e.getValue();
			if (value.size() > 0) {
				ret.put(key, value.get(0));
			}
		}

		return ret;
	}

}
