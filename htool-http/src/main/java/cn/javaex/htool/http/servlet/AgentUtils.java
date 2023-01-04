package cn.javaex.htool.http.servlet;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 获取终端信息
 *
 * @author 陈霓清
 * @Date 2022年11月26日
 */
public class AgentUtils {
	/** 未知的 */
	private static final String UNKNOWN = "unknown";
	
	/** 移动设备 */
	private static final String ANDROID = "Android";
	private static final String IPHONE = "iPhone";
	private static final String IPAD = "iPad";
	
	/** 浏览器 */
	private static final String MICROSOFT_EDGE = "Edg/";
	
    /**
     * 获取访问者的ip地址
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = null;

        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 判断访问来路是否是移动端
     * @param request
     * @return
     */
    public static boolean isMoblie(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");

        if (userAgent.indexOf(ANDROID) != -1 || userAgent.indexOf(IPHONE) != -1 || userAgent.indexOf(IPAD) != -1) {
            return true;
        }

        return false;
    }

    /**
     * 获取访问者浏览器
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");

        if (userAgent.contains(MICROSOFT_EDGE)) {
            return userAgent.substring(userAgent.indexOf(MICROSOFT_EDGE), userAgent.length()).replace("Edg/", "Edge ");
        }

        Browser browser = UserAgent.parseUserAgentString(userAgent).getBrowser();
        return browser.getName();
    }

    /**
     * 获取访问者操作系统
     * @param request
     * @return
     */
    public static String getOperatingSystem(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");

        OperatingSystem operatingSystem = UserAgent.parseUserAgentString(userAgent).getOperatingSystem();
        return operatingSystem.getName();
    }

}
