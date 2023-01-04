package cn.javaex.htool.core.io.handler;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件帮助类
 * 
 * @author 陈霓清
 * @Date 2022年12月8日
 */
public class FileHelper {
	/**
	 * 默认缓冲区大小
	 */
	public static final int BUFFER_SIZE = 1024;
	
	/**
	 * 文件扩展分隔符（字符）
	 */
	public static final char EXTENSION_SEPARATOR = '.';

	/**
	 * 文件扩展分隔符（字符串）
	 */
	public static final String EXTENSION_SEPARATOR_STR = ".";

	/**
	 * Unix分隔符
	 */
	protected static final char UNIX_SEPARATOR = '/';

	/**
	 * Windows分隔符
	 */
	protected static final char WINDOWS_SEPARATOR = '\\';

	/**
	 * 系统分隔符
	 */
	protected static final char SYSTEM_SEPARATOR = File.separatorChar;

	/**
	 * 与系统分隔符相反的分隔符
	 */
	protected static final char OTHER_SEPARATOR;

	/**
	 * 空数组
	 */
	protected static final String[] EMPTY_STRING_ARRAY = {};

	/**
	 * 空字符串
	 */
	protected static final String EMPTY_STRING = "";

	/**
	 * 未找到
	 */
	protected static final int NOT_FOUND = -1;
	
	/**
	 * 特殊的文件扩展名
	 */
	protected static final String[] SPECIAL_EXTENSION = {"tar.gz", "tar.bz2", "tar.Z", "tar.xz"};
	
	/**
	 * Windows下文件名中被禁止的字符
	 */
	protected static final Pattern INVALID_FILENAME_WINDOWS = Pattern.compile("[\\\\/:*?\"<>|\r\n]");
	
	static {
		if (isWindowsSystem()) {
			OTHER_SEPARATOR = UNIX_SEPARATOR;
		} else {
			OTHER_SEPARATOR = WINDOWS_SEPARATOR;
		}
	}

	/**
	 * 是否是windows系统
	 * 
	 * @return
	 */
	protected static boolean isWindowsSystem() {
		return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
	}

}
