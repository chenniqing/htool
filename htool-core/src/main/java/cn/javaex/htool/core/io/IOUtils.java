package cn.javaex.htool.core.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import cn.javaex.htool.core.io.handler.FileHandler;

/**
 * IO流工具类
 * 
 * @author 陈霓清
 * @Date 2022年12月6日
 */
public class IOUtils {

	/**
	 * 关闭一个IO流、socket、或者selector且不抛出异常
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * 打开指定文件的FileOutputStream，并检查如果父级目录不存在，则创建父级目录
	 * @param file
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
		Objects.requireNonNull(file);
		if (file.exists()) {
			FileHandler.requireFile(file);
			FileHandler.requireCanWrite(file);
		} else {
			FileHandler.createParentDirectories(file);
		}
		return new FileOutputStream(file, append);
	}

	/**
	 * 打开指定文件的FileInputStream
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static FileInputStream openInputStream(File file) throws IOException {
		Objects.requireNonNull(file);
		return new FileInputStream(file);
	}

}
