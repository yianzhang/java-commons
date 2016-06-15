package me.yczhang.kit.http_server;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
	
	public static String getPathOfURI(String uri) {
		int i = uri.indexOf('?');
		if (-1==i) {
			try {
				return URLDecoder.decode(uri, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return uri;
			}
		}
		try {
			return URLDecoder.decode(uri.substring(0, i), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return uri.substring(0, i);
		}
	}
	
	public static Map<String, String> getParamsOfURI(String uri) {
		Map<String, String> params = new HashMap<String, String>();
		
		int i = uri.indexOf('?');
		if (-1==i)
			return params;
		
		String[] ss1 = uri.substring(i+1, uri.length()).split("&");
		for (String s : ss1) {
			String ss2[] = s.split("=");
			if (2!=ss2.length || 0==ss2[0].length() || 0==ss2[1].length())
				continue;
			try {
				params.put(URLDecoder.decode(ss2[0], "utf-8").toLowerCase(), URLDecoder.decode(ss2[1], "utf-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return params;
	}

	public static String requestToString(FullHttpRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getMethod().toString()).append(" ")
				.append(request.getUri()).append(" ")
				.append(request.getProtocolVersion()).append(String.format("%n"));
		HttpHeaders headers = request.headers();
		for (Map.Entry<String, String> header: headers.entries()) {
			sb.append(header.getKey()).append(": ").append(header.getValue()).append(String.format("%n"));
		}
		return sb.toString();
	}

}
