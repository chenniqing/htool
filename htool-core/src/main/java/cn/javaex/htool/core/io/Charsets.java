package cn.javaex.htool.core.io;

import java.nio.charset.Charset;

import cn.javaex.htool.core.string.StringUtils;

/**
 * 字符集工具类
 * 
 * @author 陈霓清
 * @Date 2022年12月7日
 */
public class Charsets {

	/**
	 * 字符集转换
	 * 
	 * @param charsetName
	 * @return
	 */
	public static Charset toCharset(String charsetName) {
		if (StringUtils.isNotEmpty(charsetName)) {
			Charset.forName(charsetName);
		}

		return Charset.defaultCharset();
	}

	/**
	 * 返回给定的Charset或默认的Charset
	 * 
	 * @param charset
	 * @return
	 */
	public static Charset toCharset(final Charset charset) {
		return charset == null ? Charset.defaultCharset() : charset;
	}

}
