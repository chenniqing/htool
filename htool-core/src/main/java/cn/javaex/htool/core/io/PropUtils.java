package cn.javaex.htool.core.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取properties的配置信息
 * 
 * @author 陈霓清
 * @Date 2023年1月2日
 */
public class PropUtils {
	
	/**
	 * 从配置文件中获取配置参数
	 * @param path
	 * @param filename
	 * @param key
	 * @return
	 */
	public static String getValue(String path, String filename, String key) {
		String propertieValue = "";
		InputStream in = null;
		
		try {
			in = new BufferedInputStream(new FileInputStream(path + File.separator + filename));
			Properties prop = new Properties();
			prop.load(in);
			propertieValue = prop.getProperty(key,"");
			in.close();
			prop.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return propertieValue;
	}
	
	/**
	 * 从配置文件中获取配置参数
	 * @param filename
	 * @param key
	 * @return
	 */
	public static String getValue(String filename, String key) {
		String propertieValue = "";
		InputStream in = null;
		
		try {
			in = new BufferedInputStream (new FileInputStream(PropUtils.class.getClassLoader().getResource(filename).getFile()));
			
			Properties prop = new Properties();
			prop.load(in);
			propertieValue = prop.getProperty(key);
			in.close();
			prop.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return propertieValue;
	}
	
}
