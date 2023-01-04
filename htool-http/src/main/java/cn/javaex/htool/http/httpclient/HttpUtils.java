package cn.javaex.htool.http.httpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import cn.javaex.htool.core.string.StringUtils;
import cn.javaex.htool.http.httpclient.handler.HttpClientHandler;

/**
 * HttpClient工具类
 * 
 * @author 陈霓清
 * @Date 2023年1月4日
 */
public class HttpUtils {
	
	/**
	 * GET请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> querys) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpGet request = new HttpGet(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);
		
		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}

	/**
	 * POST请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @param bodys
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpPost request = new HttpPost(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);

		if (bodys != null) {
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

			for (String key : bodys.keySet()) {
				nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
			formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			request.setEntity(formEntity);
		}

		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}	
	
	/**
	 * POST请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> querys, String body) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpPost request = new HttpPost(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "UTF-8"));
		}

		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> querys, byte[] body) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpPost request = new HttpPost(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}

	/**
	 * POST请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doPostWithJson(String url, Map<String, String> headers, Map<String, String> querys, String body) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpPost request = new HttpPost(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		Map<String, String> reqHeaders = new HashMap<>();
		reqHeaders.put("Content-type", "application/json");
		if (headers != null) {
			reqHeaders.putAll(headers);
		}
		httpClientHandler.packageHeader(request, reqHeaders);

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "UTF-8"));
		}
		
		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}
	
	/**
	 * PUT请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doPut(String url, Map<String, String> headers, Map<String, String> querys, String body) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpPut request = new HttpPut(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);
		
		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "UTF-8"));
		}
		
		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}
	
	/**
	 * PUT请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doPut(String url, Map<String, String> headers, Map<String, String> querys, byte[] body) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpPut request = new HttpPut(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);
		
		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}
		
		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}
	
	/**
	 * DELETE请求
	 * @param url
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult doDelete(String url, Map<String, String> headers, Map<String, String> querys) throws Exception {
		HttpClientHandler httpClientHandler = new HttpClientHandler();
		
		HttpClient httpClient = httpClientHandler.wrapClient(url);
		HttpDelete request = new HttpDelete(httpClientHandler.buildUrl(url, querys));
		// 封装请求头
		httpClientHandler.packageHeader(request, headers);
		
		HttpResponse httpResponse = httpClient.execute(request);
		return httpClientHandler.getHttpClientResult(httpResponse);
	}
	
}
