package cn.javaex.htool.http.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session工具类
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class SessionUtils {

	/**
	 * 设置session
	 * @param request
	 * @param name
	 * @param value
	 */
	public static void setSession(HttpServletRequest request, String name, Object value) {
		request.getSession().setAttribute(name, value);
	}
	
	/**
	 * 删除session
	 * @param request
	 * @param name
	 */
	public static void deleteSession(HttpServletRequest request, String name) {
		request.getSession().removeAttribute(name);
	}
	
	/**
	 * 获取sessionId
	 * @param request
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		return session.getId();
	}
	
	/**
	 * 获取session
	 * @param <T>
	 * @param request
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSession(HttpServletRequest request, String name) {
		HttpSession session = request.getSession();
		
		return (T) session.getAttribute(name);
	}
}
