package me.yczhang.kit.udp_server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by YCZhang on 1/7/16.
 */
public class UdpServer {

	private class UdpServerHandlerAdapter extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			DatagramPacket packet = (DatagramPacket) msg;
			action.action(packet);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if (exceptionHandler != null) {
				exceptionHandler.handle(ctx, cause);
			}
			else {
				super.exceptionCaught(ctx, cause);
			}
		}
	}

	private InetSocketAddress address;
	private UdpAction action;
	private UdpExceptionHandler exceptionHandler;

	private EventLoopGroup group;
	private Bootstrap bootstrap;

	private int nThreads = 0;

	public UdpServer(InetSocketAddress address, UdpAction action) {
		this.address = address;
		this.action = action;
	}

	public UdpServer setThreads(int nThreads) {
		this.nThreads = nThreads;
		return this;
	}

	public UdpServer setExceptionHandler(UdpExceptionHandler handler) {
		this.exceptionHandler = handler;
		return this;
	}

	public void start() {
		if (null != bootstrap)
			return;

		if (nThreads<=0)
			group = new NioEventLoopGroup();
		else
			group = new NioEventLoopGroup(nThreads);

		bootstrap = new Bootstrap();
		bootstrap
				.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new UdpServerHandlerAdapter());

		bootstrap.bind(address);
	}

	public void stop() {
		if (null==bootstrap)
			return;

		bootstrap = null;
		group.shutdownGracefully(1, 5, TimeUnit.SECONDS);
	}
}
