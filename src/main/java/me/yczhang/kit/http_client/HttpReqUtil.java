package me.yczhang.kit.http_client;

import me.yczhang.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by YCZhang on 4/21/15.
 */
public class HttpReqUtil {

	public static String buildUrl(String host, Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder url = new StringBuilder();
		url.append(host);
		if (!params.isEmpty())
			url.append("?");
		boolean flag = false;
		for (Map.Entry<String, String> e : params.entrySet()) {
			if (flag)
				url.append("&");
			else
				flag = true;

			url.append(URLEncoder.encode(e.getKey(), "UTF-8"))
					.append("=")
					.append(URLEncoder.encode(e.getValue(), "UTF-8"));

		}

		return url.toString();
	}

	public static String get(String url) throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet get = new HttpGet(url.toString());
			RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
			get.setConfig(config);

			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode!=200)
				throw new IOException("Response Status Code "+statusCode);

			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} finally {
			try {client.close();} catch (IOException e) {}
		}
	}

	public static String getByUrlConnection(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept-Charset", "utf-8");

		int statusCode = conn.getResponseCode();
		if (statusCode == 200)
			return StringUtil.dumpInputStreamToStringAndClose(conn.getInputStream());
		else
			throw new IOException("Response Status Code "+statusCode);
	}

	public static String post(String url, String content) throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url.toString());
			RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
			post.setConfig(config);
			StringEntity requestEntity = new StringEntity(content, "UTF-8");
			post.setEntity(requestEntity);

			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode!=200)
				throw new IOException("Response Status Code "+statusCode);

			HttpEntity responseEntity = response.getEntity();
			return EntityUtils.toString(responseEntity, "utf-8");
		} finally {
			try {client.close();} catch (IOException e) {}
		}
	}

	public static void download(String url, File saveFile) throws IOException {
		saveFile.getParentFile().mkdirs();

		CloseableHttpClient client = HttpClients.createDefault();

		InputStream in = null;
		OutputStream out = null;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);

			HttpEntity entity = response.getEntity();
			in = entity.getContent();
			out = new FileOutputStream(saveFile);
			byte[] buffer=new byte[10240];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer,0,len);
			}
		}
		finally {
			if (in != null) try { in.close(); } catch (Exception e) {}
			if (out != null) try { out.close(); } catch (Exception e) {}
			try { client.close(); } catch (Exception e) {}
		}
	}

}
