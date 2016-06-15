package me.yczhang.kit.http_server_v2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import me.yczhang.kit.http_server_v2.action.HttpAction;
import me.yczhang.kit.http_server_v2.route.HttpRoute;
import me.yczhang.kit.http_server_v2.util.HttpResponseUtil;
import me.yczhang.util.StringUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class HttpServer {
	
	private class HttpServerHandlerAdapter extends ChannelInboundHandlerAdapter {
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			FullHttpRequest request = (FullHttpRequest) msg;

			HttpAction action = route.route(request);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
			if (action == null) {
				response.setStatus(HttpResponseStatus.NOT_FOUND);
			}
			else {
//				try {
					action.act(request, response);
//				} catch (Throwable e) {
//					response.content().clear();
//					HttpResponseUtil.writeContent(response, StringUtil.dumpStackTraceToString(e));
//				}
			}

			response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());

			ctx.writeAndFlush(response);
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)	throws Exception {
			if (exceptionHandler != null) {
				exceptionHandler.handle(ctx, cause);
			}
			else {
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
				response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
				response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
				HttpResponseUtil.writeContent(response, "<pre>" + StringUtil.dumpStackTraceToString(cause) + "</pre>");
				response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
				ctx.writeAndFlush(response);
			}
		}
	}
	
	private InetSocketAddress address;
	private HttpRoute route;
	private HttpExceptionHandler exceptionHandler;
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workGroup;
	private ServerBootstrap bootstrap;

	private int nBossThreads = 0;
	private int nWorkThreads = 0;
	
	public HttpServer(InetSocketAddress address, HttpRoute route) {
		this.address = address;
		this.route = route;
	}

	public HttpServer setBossThreads(int nBossThreads) {
		this.nBossThreads = nBossThreads;
		return this;
	}

	public HttpServer setWorkThreads(int nWorkThreads) {
		this.nWorkThreads = nWorkThreads;
		return this;
	}

	public HttpServer setExceptionHandler(HttpExceptionHandler handler) {
		this.exceptionHandler = handler;
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
