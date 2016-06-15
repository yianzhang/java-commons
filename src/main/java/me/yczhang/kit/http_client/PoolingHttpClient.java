package me.yczhang.kit.http_client;

import me.yczhang.exception.ProcessException;
import me.yczhang.exception.SingletonException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by YCZhang on 4/21/15.
 */

@Deprecated
public class PoolingHttpClient {

	private static final Logger LOG = LogManager.getLogger();

	private static PoolingHttpClient instance = null;

	public static PoolingHttpClient createInstance() throws SingletonException {
		if (instance!=null)
			throw new SingletonException("Instance has created");

		return instance = new PoolingHttpClient();
	}

	public static PoolingHttpClient getInstance() throws SingletonException {
		if (instance==null)
			throw new SingletonException("Instance not created");

		return instance;
	}

	public static void destroyInstance() {
		if (instance==null)
			return;

		instance.destroy();
		instance = null;
	}

	private CloseableHttpClient client;

	private PoolingHttpClient() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		cm.setDefaultMaxPerRoute(100);
		this.client = HttpClients.custom().setConnectionManager(cm).build();
	}

	private void destroy() {
		try {
			this.client.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

//	public HttpEntity get(String url) throws IOException, ProcessException {
//		HttpGet get = new HttpGet(url);
//		CloseableHttpResponse response = null;
//		try {
//			HttpClientContext context = HttpClientContext.create();
//			response = client.execute(get, context);
//
//			int statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode!=200)
//				throw new ProcessException("Http Response Status Code: "+statusCode);
//
//			return response.getEntity();
//		} finally {
//			if (response!=null) try { response.close(); } catch (Exception e) {}
////			if (get!=null) try { get.releaseConnection(); } catch (Exception e) {}
//		}
//	}
//
//	public HttpEntity get(String host, Map<String, String> params) throws IOException, ProcessException {
//		return get(jointUrl(host, params));
//	}

	public String getString(String url) throws IOException, ProcessException {
		CloseableHttpResponse response = null;
		try {
			HttpGet get = new HttpGet(url);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(get, context);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode!=200)
				throw new ProcessException("Http Response Status Code: "+statusCode);

			return EntityUtils.toString(response.getEntity());
		} finally {
			if (response!=null) try { response.close(); } catch (Exception e) {}
		}
	}

//	public HttpEntity post(String url, HttpEntity content) throws IOException, ProcessException {
//		CloseableHttpResponse response = null;
//		try {
//			HttpPost post = new HttpPost(url);
//			post.setEntity(content);
//			HttpClientContext context = HttpClientContext.create();
//			response = client.execute(post, context);
//
//			int statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode!=200)
//				throw new ProcessException("Http Response Status Code: "+statusCode);
//
//			return response.getEntity();
//		} finally {
//			if (response!=null) try { response.close(); } catch (Exception e) {}
//		}
//	}
//
//	public HttpEntity post(String url, String content) throws IOException, ProcessException {
//		return post(url, new StringEntity(content, "utf-8"));
//	}

	public String postString(String url, HttpEntity content) throws IOException, ProcessException {
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			post.setEntity(content);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(post, context);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode!=200)
				throw new ProcessException("Http Response Status Code: "+statusCode);

			return EntityUtils.toString(response.getEntity());
		} finally {
			if (response!=null) try { response.close(); } catch (Exception e) {}
		}
	}

	public String postString(String url, String content) throws IOException, ProcessException {
		return postString(url, new StringEntity(content, "utf-8"));
	}

//	public static void main(String[] args) throws SingletonException, IOException, ProcessException {
//
//
//
//		LOG.info(System.currentTimeMillis());
//		PoolingHttpClient client = PoolingHttpClient.createInstance();
//		System.out.println(client.getString("http://op.yg84.com:7000/manager/sosmt!query.action?cityId=004&magic=zhangxu0328&lineNo=0186&direction=0"));
////
//////		client.getString("https://www.baidu.com/");
////
////		int totCount = 1000;
////
////		long tot = 0;
////		for (int i=0;i<totCount;++i) {
////			long btime = System.currentTimeMillis();
////			client.get("http://www.baidu.com");
////			tot += System.currentTimeMillis()-btime;
////		}
////
////		double avg1 = (tot+0.0)/totCount;
////		LOG.info("2 avg cost "+avg1);
////
//		PoolingHttpClient.destroyInstance();
////
////		tot = 0;
////		for (int i=0;i<totCount;++i) {
////			long btime = System.currentTimeMillis();
////			HttpReqUtil.get("http://www.baidu.com");
////			tot += System.currentTimeMillis()-btime;
////		}
////
////		double avg2 = (tot+0.0)/totCount;
////
////		LOG.info("2 avg cost "+avg2);
//	}

}
