package cn.javaex.htool.http;

/**
 * html文本内容工具类
 * 
 * @author 陈霓清
 * @Date 2023年1月3日
 */
public class HtmlUtils {

	/**
	 * 过滤HTML文本，防止XSS攻击
	 *
	 * @param htmlContent HTML内容
	 * @return 过滤后的内容
	 */
	public static String filter(String htmlContent) {
		if (htmlContent==null || htmlContent.length()==0) {
			return htmlContent;
		}
		
		// 删除普通标签
		htmlContent = htmlContent.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
		// 删除转义字符
		htmlContent = htmlContent.replaceAll("&.{2,6}?;", "");
		
		return htmlContent;
	}
	
}
