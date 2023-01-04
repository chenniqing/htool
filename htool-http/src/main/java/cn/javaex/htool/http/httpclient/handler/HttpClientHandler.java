package cn.javaex.htool.http.httpclient.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import cn.javaex.htool.core.string.StringUtils;
import cn.javaex.htool.http.httpclient.HttpClientResult;

/**
 * HttpClient公共方法
 * 
 * @author 陈霓清
 * @Date 2023年1月4日
 */
public class HttpClientHandler {
	
	/**
	 * 构造HttpClient
	 * @param host
	 * @return
	 * @throws Exception 
	 */
	public HttpClient wrapClient(String host) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		if (host.startsWith("https://")) {
			this.sslClient(httpClient);
		}
		
		return httpClient;
	}
	
	/**
	 * SSL
	 * @param httpClient
	 */
	@SuppressWarnings("deprecation")
	private void sslClient(HttpClient httpClient) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] xcs, String str) {
					
				}
				public void checkServerTrusted(X509Certificate[] xcs, String str) {
					
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry registry = ccm.getSchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
		} catch (KeyManagementException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 拼接URL
	 * @param url
	 * @param querys 查询参数
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String buildUrl(String url, Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuffer sbUrl = new StringBuffer();
		sbUrl.append(url);
		
		if (querys != null) {
			StringBuffer sbQuery = new StringBuffer();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(), "UTF-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}
		
		return sbUrl.toString();
	}
	
	/**
	 * 封装请求头
	 * @param request
	 * @param headers
	 */
	public void packageHeader(HttpGet request, Map<String, String> headers) {
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
	}
	
	/**
	 * 封装请求头
	 * @param request
	 * @param headers
	 */
	public void packageHeader(HttpPost request, Map<String, String> headers) {
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
	}
	
	/**
	 * 封装请求头
	 * @param request
	 * @param headers
	 */
	public void packageHeader(HttpPut request, Map<String, String> headers) {
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
	}
	
	/**
	 * 封装请求头
	 * @param request
	 * @param headers
	 */
	public void packageHeader(HttpDelete request, Map<String, String> headers) {
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
	}
	
	/**
	 * 获取返回结果
	 * @param httpResponse
	 * @return
	 * @throws IOException
	 */
	public HttpClientResult getHttpClientResult(HttpResponse httpResponse) throws IOException {
		if (httpResponse != null && httpResponse.getStatusLine() != null) {
			byte[] body = null;
			String content = "";
			
			if (httpResponse.getEntity() != null) {
				body = EntityUtils.toByteArray(httpResponse.getEntity());
				
				content = new String(body, "UTF-8");
				content = StringUtils.unicodeToString(content);
			}
			
			return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), httpResponse.getEntity(), body, content);
		}
		
		return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR); 
	}

}
