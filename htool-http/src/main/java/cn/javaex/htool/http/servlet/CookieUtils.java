package cn.javaex.htool.http.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class CookieUtils {
	
	/**
	 * 设置Cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry : 过期失效时间，单位：秒
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
		Cookie addCookie = new Cookie(name, value);
		addCookie.setMaxAge(expiry);
		addCookie.setPath("/");
		response.addCookie(addCookie);
	}
	
	/**
	 * 设置Cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry : 过期失效时间，单位：秒
	 * @param domain : 域名，例如 javaex.cn
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int expiry, String domain) {
		Cookie addCookie = new Cookie(name, value);
		addCookie.setMaxAge(expiry);
		addCookie.setPath("/");
		addCookie.setDomain(domain);
		response.addCookie(addCookie);
	}
	
	/**
	 * 获取指定名称的cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookieArr = request.getCookies();
		if (cookieArr!=null && cookieArr.length>0) {
			for (int i=0; i<cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				if (cookie.getName().equals(name)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						break;
					}
				}
			}
		}
		
		return "";
	}
	
	/**
	 * 获取Cookie列表
	 * @param request
	 * @return
	 */
	public static List<String> getCookieList(HttpServletRequest request) {
		List<String> cookieList = new ArrayList<>();
		Cookie[] cookies = request.getCookies();
		if (cookies==null || cookies.length==0) {
			return cookieList;
		}
		
		for (Cookie cookie : cookies) {
			cookieList.add(cookie.getName() + "=" + cookie.getValue());
		}
		
		return cookieList;
	}
	
	/**
	 * 删除Cookie
	 * @param response
	 * @param name
	 */
	public static void deleteCookie(HttpServletResponse response, String name) {
		Cookie deleteCookie = new Cookie(name, "");
		deleteCookie.setPath("/");
		deleteCookie.setMaxAge(0);
		response.addCookie(deleteCookie);
	}
	
	/**
	 * 删除Cookie
	 * @param response
	 * @param name
	 * @param domain : 域名，例如 javaex.cn
	 */
	public static void deleteCookie(HttpServletResponse response, String name, String domain) {
		Cookie deleteCookie = new Cookie(name, "");
		deleteCookie.setPath("/");
		deleteCookie.setMaxAge(0);
		deleteCookie.setDomain(domain);
		response.addCookie(deleteCookie);
	}
}
