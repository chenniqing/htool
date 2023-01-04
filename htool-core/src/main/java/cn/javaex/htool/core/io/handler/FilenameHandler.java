package cn.javaex.htool.core.io.handler;

import cn.javaex.htool.core.string.StringUtils;

/**
 * 文件名处理
 * 
 * @author 陈霓清
 * @Date 2022年12月8日
 */
public class FilenameHandler extends FileHelper {

	/**
	 * 返回文件后缀的索引
	 * 
	 * @param fileName
	 * @return
	 */
	public static int indexOfExtension(String fileName) {
		if (fileName == null) {
			return -1;
		}

		// 正常的文件后缀索引
		final int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
		// 非正常的文件后缀索引
		// 例如 https://webimg.javaex.cn/FiPZlaXnGeqvJYV1CtH3yGbBVH_i
		// 例如 D:\\test\\1
		final int lastSeparator = indexOfLastSeparator(fileName);

		return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
	}

	/**
	 * 获取系统分隔符的索引
	 * 
	 * @param fileName
	 * @return
	 */
	public static int indexOfLastSeparator(String fileName) {
		if (fileName == null) {
			return NOT_FOUND;
		}
		final int lastUnixPos = fileName.lastIndexOf(UNIX_SEPARATOR);
		final int lastWindowsPos = fileName.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	/**
	 * 获取完整的文件名称，去除文件路径 a/b/c.txt -> c.txt
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getName(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return fileName;
		}

		int index = FilenameHandler.indexOfLastSeparator(fileName);
		return fileName.substring(index + 1);
	}

}
