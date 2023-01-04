package cn.javaex.htool.core.string;

import cn.javaex.htool.core.string.zh.PinyinConverter;
import cn.javaex.htool.core.string.zh.ZHConverter;

/**
 * 中文转换工具类
 * 
 * @author 陈霓清
 * @Date 2022年11月30日
 */
public class ZHUtils {
	
	/**
	 * 简体转繁体
	 * @param text
	 * @return
	 */
	public static String toTC(String text) {
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.TC);
		return converter.convert(text);
	}
	
	/**
	 * 繁体转简体
	 * @param text
	 * @return
	 */
	public static String toSC(String text) {
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SC);
		return converter.convert(text);
	}
	

	/**
	 * 简体汉字转拼音首字母
	 * @param text
	 * @return
	 */
	public static String toPinyin(String text) {
		PinyinConverter converter = new PinyinConverter();
		return converter.toPinyin(text);
	}
	
}
