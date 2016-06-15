package me.yczhang.kit.http_server_v2;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by YCZhang on 8/11/15.
 */
public interface HttpExceptionHandler {

	public void handle(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
