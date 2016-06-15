package me.yczhang.kit.http_server_v2.action;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by YCZhang on 8/11/15.
 */
public interface HttpAction {

	public void act(FullHttpRequest request, FullHttpResponse response) throws Exception;

}
