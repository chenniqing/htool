package cn.javaex.htool.http.httpclient;

import java.io.Serializable;

import org.apache.http.HttpEntity;

/**
 * http响应结果封装
 * 
 * @author 陈霓清
 * @Date 2023年1月3日
 */
public class HttpClientResult implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 响应状态码
	 */
	private int code;
	/**
	 * 响应数据
	 */
	private HttpEntity entity;
	/**
	 * 响应数据转byte[]
	 */
	private byte[] body;
	/**
	 * 响应数据转String
	 */
	private String content;
	
	public HttpClientResult(int scInternalServerError) {
		this.code = scInternalServerError;
	}
	
	public HttpClientResult(int code, HttpEntity entity, byte[] body, String content) {
		this.code = code;
		this.entity = entity;
		this.body = body;
		this.content = content;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public HttpEntity getEntity() {
		return entity;
	}
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
