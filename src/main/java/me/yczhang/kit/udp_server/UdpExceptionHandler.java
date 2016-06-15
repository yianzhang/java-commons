package me.yczhang.kit.udp_server;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by YCZhang on 1/7/16.
 */
public interface UdpExceptionHandler {
	public void handle(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
