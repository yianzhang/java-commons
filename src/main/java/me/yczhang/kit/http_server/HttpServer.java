package me.yczhang.kit.http_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class HttpServer {
	
	public static interface HttpServerHandler {
		public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception;
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;
	}
	
	private class HttpServerHandlerAdapter extends ChannelInboundHandlerAdapter {
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelRead");
			handler.messageReceived(ctx, msg);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelRead over");
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)	throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": exceptionCaught");
			handler.exceptionCaught(ctx, cause);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": exceptionCaught over");
		}
		
		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelRegistered");
			super.channelRegistered(ctx);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelRegistered over");
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelUnregistered");
			super.channelUnregistered(ctx);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelUnregistered over");
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelActive");
			super.channelActive(ctx);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelActive over");
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelInactive");
			super.channelInactive(ctx);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelInactive over");
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelReadComplete");
			super.channelReadComplete(ctx);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelReadComplete over");
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": userEventTriggered");
			super.userEventTriggered(ctx, evt);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": userEventTriggered over");
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())+": channelWritabilityChanged over");
		}
	}
	
	private InetSocketAddress address;
	private HttpServerHandler handler;
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workGroup;
	private ServerBootstrap bootstrap;

	private int nBossThreads = 0;
	private int nWorkThreads = 0;
	
	public HttpServer(InetSocketAddress address, HttpServerHandler handler) {
		this.address = address;
		this.handler = handler;
	}

	public HttpServer setBossThreads(int nBossThreads) {
		this.nBossThreads = nBossThreads;
		return this;
	}

	public HttpServer setWorkThreads(int nWorkThreads) {
		this.nWorkThreads = nWorkThreads;
		return this;
	}
	
	public void start() {
		if (null!=bootstrap) {
			return;
		}

		if (nBossThreads<=0)
			bossGroup = new NioEventLoopGroup();
		else
			bossGroup = new NioEventLoopGroup(nBossThreads);
		if (nWorkThreads<=0)
			workGroup = new NioEventLoopGroup();
		else
			workGroup = new NioEventLoopGroup(nWorkThreads);
		
		bootstrap = new ServerBootstrap();
		bootstrap
			.group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast(new HttpResponseEncoder());
//					ch.pipeline().addLast(new HttpRequestDecoder());
					ch.pipeline().addLast(new HttpServerCodec());
					ch.pipeline().addLast(new HttpObjectAggregator(64*1024*1024));
					ch.pipeline().addLast(new ChunkedWriteHandler());
					ch.pipeline().addLast(new HttpServerHandlerAdapter());
				}
			})
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
		
		bootstrap.bind(address);
	}
	
	public void stop() {
		if (null==bootstrap) {
			return;
		}
		bootstrap = null;
		workGroup.shutdownGracefully(1, 5, TimeUnit.SECONDS);
		bossGroup.shutdownGracefully(1, 5, TimeUnit.SECONDS);
	}
	
}
