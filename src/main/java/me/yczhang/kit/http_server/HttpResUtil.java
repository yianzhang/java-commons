package me.yczhang.kit.http_server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.*;

/**
 * Created by YCZhang on 4/21/15.
 */
public class HttpResUtil {

	public static void resp(ChannelHandlerContext ctx, HttpResponseStatus status, String content) {
		try {
			FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
			res.content().writeBytes(content.getBytes("utf-8"));
			res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
			res.headers().set(HttpHeaders.Names.CONTENT_LENGTH, res.content().readableBytes());
			res.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			ctx.writeAndFlush(res);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void resp200(ChannelHandlerContext ctx, String content) {
		resp(ctx, HttpResponseStatus.OK, content);
	}

	public static void resp403(ChannelHandlerContext ctx, String content) {
		resp(ctx, HttpResponseStatus.FORBIDDEN, content);
	}

	public static void resp404(ChannelHandlerContext ctx) {
		resp(ctx, HttpResponseStatus.NOT_FOUND, "<h1>Not Found</h1>");
	}

	public static void resp500(ChannelHandlerContext ctx, String msg) {
		String page = String.format("<h1>Internal Server Error</h1><p><pre>%s</pre><b>If can't Solve Problems, Please Contact Administrators</b></p>", msg);
		resp(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, page);
	}

	public static void respFile(ChannelHandlerContext ctx, File file) throws IOException {
		FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		String fileName = file.getName();
		InputStream in = new FileInputStream(file);
		byte[] b = new byte[1024];
		while (true) {
			int l = in.read(b, 0, b.length);
			if (l<0)
				break;
			res.content().writeBytes(b, 0, l);
		}
		if (fileName.endsWith(".html"))
			res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
		else if (fileName.endsWith(".js"))
			res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/x-javascript; charset=utf-8");
		else if (fileName.endsWith(".css"))
			res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/css; charset=utf-8");
		else
			res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=utf-8");
		res.headers().set(HttpHeaders.Names.CONTENT_LENGTH, res.content().readableBytes());
		res.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);

		ctx.writeAndFlush(res);
	}

}
