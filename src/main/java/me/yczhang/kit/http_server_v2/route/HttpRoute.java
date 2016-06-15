package me.yczhang.kit.http_server_v2.route;

import io.netty.handler.codec.http.FullHttpRequest;
import me.yczhang.kit.http_server_v2.action.HttpAction;

/**
 * Created by YCZhang on 8/11/15.
 */
public interface HttpRoute {

	public HttpAction route(FullHttpRequest request) throws Exception;
}
