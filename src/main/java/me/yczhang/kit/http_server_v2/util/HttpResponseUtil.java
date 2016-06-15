package me.yczhang.kit.http_server_v2.util;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import me.yczhang.util.GZipUtil;

import java.io.IOException;

/**
 * Created by YCZhang on 8/11/15.
 */
public class HttpResponseUtil {

	public static void writeContent(FullHttpResponse response, String content) {
		writeContent(response, content, "utf-8");
	}

	public static void writeContent(FullHttpResponse response, String content, String charset) {
		try {
			response.content().writeBytes(content.getBytes(charset));
			response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void gzipAndWriteContent(FullHttpResponse response, String content) {
		gzipAndWriteContent(response, content, "utf-8");
	}

	public static void gzipAndWriteContent(FullHttpResponse response, String content, String charset) {
		try {
			byte[] gzipBytes = GZipUtil.gzip(content.getBytes(charset));
			response.content().writeBytes(gzipBytes);
			response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, gzipBytes.length);
			response.headers().set(HttpHeaders.Names.CONTENT_ENCODING, "gzip");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
