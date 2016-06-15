package me.yczhang.kit.http_server_v2.action;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by YCZhang on 8/11/15.
 */
public class FileAction implements HttpAction {

	private File file;

	public FileAction(File file) {
		this.file = file;
	}

	@Override
	public void act(FullHttpRequest request, FullHttpResponse response) throws Exception {
		if (!file.exists() || !file.isFile()) {
			response.setStatus(HttpResponseStatus.NOT_FOUND);
			return;
		}

		String fileName = file.getName();
		InputStream in = new FileInputStream(file);
		byte[] b = new byte[1024];
		while (true) {
			int l = in.read(b, 0, b.length);
			if (l<0)
				break;
			response.content().writeBytes(b, 0, l);
		}
		if (fileName.endsWith(".html"))
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
		else if (fileName.endsWith(".js"))
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/x-javascript; charset=utf-8");
		else if (fileName.endsWith(".css"))
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/css; charset=utf-8");
		else
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=utf-8");
	}

}
