package cn.javaex.htool.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import cn.javaex.htool.core.io.handler.FileHandler;
import cn.javaex.htool.core.io.handler.FileHelper;

/**
 * 文件压缩工具类
 * 
 * @author 陈霓清
 * @Date 2023年1月3日
 */
public class ZipUtils extends FileHelper {

	/**
	 * 创建一个zip压缩文件，并存放到新的路径中
	 * @param srcPath	 源目录或文件的绝对路径，例如：D:\\Temp  或  D:\\Temp\\1.docx
	 * @param zipPath	 压缩后的文件绝对路径，例如：D:\\Temp\\xx.zip
	 * @throws IOException 
	 */
	public static void zip(String srcPath, String zipPath) throws IOException {
		zip(srcPath, zipPath, true);
	}
	
	/**
	 * 创建一个zip压缩文件，并存放到新的路径中
	 * @param srcPath	  源目录或文件的绝对路径，例如：D:\\Temp  或  D:\\Temp\\1.docx
	 * @param zipPath	  压缩后的文件绝对路径，例如：D:\\Temp\\xx.zip
	 * @param isKeepFolder 是否将目录名称也一起压缩
	 * @throws IOException 
	 */
	public static void zip(String srcPath, String zipPath, boolean isKeepFolder) throws IOException {
		File srcFile = new File(srcPath);
		FileHandler.requireExists(srcFile);
		
		File zipFile = new File(zipPath);
		// 如果压缩包已存在，则先删除
		if (zipFile.exists()) {
			FileUtils.deleteFileOrDirectory(zipFile);
		}
		
		byte[] buffer = new byte[BUFFER_SIZE * 10];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		try {
			// 判断是否是一个具体的文件
			if (srcFile.isFile()) {
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				
				// 创建zip实体，并添加进压缩包
				ZipEntry zipEntry = new ZipEntry(srcFile.getName());
				zos.putNextEntry(zipEntry);
				
				fis = new FileInputStream(srcFile);
				bis = new BufferedInputStream(fis, BUFFER_SIZE * 10);
				int read = 0;
				while ((read=bis.read(buffer, 0, BUFFER_SIZE * 10)) != -1) {
					zos.write(buffer, 0, read);
				}
				zos.flush();
			}
			// 目录
			else if (srcFile.isDirectory()) {
				// 获取文件源中的所有文件
				List<File> fileList = FileUtils.listDeepFiles(srcFile);
				
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				
				// 将每个文件放入zip流中
				for (File file : fileList) {
					String name = FileHandler.getRealName(srcPath, file);	// 获取文件相对路径，保持文件原有结构
					ZipEntry zipEntry = null;
					if (isKeepFolder) {
						zipEntry = new ZipEntry(new File(srcPath).getName() + File.separator + name); 
					} else {
						zipEntry = new ZipEntry(name); 
					}
					
					zipEntry.setSize(file.length());
					zos.putNextEntry(zipEntry);
					
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis, BUFFER_SIZE * 10);
					int read = 0;
					while ((read=bis.read(buffer, 0, BUFFER_SIZE * 10)) != -1) {
						zos.write(buffer, 0, read);
					}
					
					zos.flush();
					bis.close();
					fis.close();
				}
				zos.close();
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
	}
	
	/**
	 * zip解压
	 * @param zipPath	  zip文件的绝对路径，例如：D:\\Temp\\xx.zip
	 * @param destDirPath  解压后的目标文件夹路径，例如：D:\\Temp
	 * @throws IOException 
	 */
	public static void unZip(String zipPath, String destDirPath) throws IOException {
		File file = new File(zipPath);
		FileHandler.requireFile(file);
		
		byte[] buffer = new byte[BUFFER_SIZE * 10];
		ZipFile zipFile = null;
		
		try {
			zipFile = new ZipFile(zipPath);
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = destDirPath + File.separator + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				} else {
					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
					File targetFile = new File(destDirPath + File.separator + entry.getName());
					// 保证这个文件的父文件夹必须要存在
					if (!targetFile.getParentFile().exists()) {
						targetFile.getParentFile().mkdirs();
					}
					targetFile.createNewFile();
					// 将压缩文件内容写入到这个文件中
					InputStream in = zipFile.getInputStream(entry);
					BufferedInputStream bis = new BufferedInputStream(in, BUFFER_SIZE * 10);
					FileOutputStream fos = new FileOutputStream(targetFile);
					
					int read;
					while ((read=bis.read(buffer, 0, BUFFER_SIZE * 10)) != -1) {
						fos.write(buffer, 0, read);
					}
					
					fos.close();
					in.close();
				}
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(zipFile);
		}
	}
	
}
