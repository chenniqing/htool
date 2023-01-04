package cn.javaex.htool.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.javaex.htool.core.io.handler.FileHandler;
import cn.javaex.htool.core.io.handler.FileHelper;
import cn.javaex.htool.core.io.handler.FilenameHandler;
import cn.javaex.htool.core.io.model.TreeFile;
import cn.javaex.htool.core.string.StringUtils;

/**
 * 文件操作工具类
 * 
 * @author 陈霓清
 * @Date 2022年12月1日
 */
public class FileUtils extends FileHelper {
	
	/**
	 * 递归删除文件或目录（目录本身也删除）
	 * @param path	目录或文件的绝对路径
	 * @return boolean
	 * @throws IOException 
	 */
	public static void deleteFileOrDirectory(String path) throws IOException {
		File file = new File(path);
		deleteFileOrDirectory(file);
	}
	
	/**
	 * 递归删除文件或目录（目录本身也删除）
	 * @param file	File文件对象
	 * @return boolean
	 * @throws IOException 
	 */
	public static void deleteFileOrDirectory(File file) throws IOException {
		// 1. 校验参数
		FileHandler.requireExists(file);
		if (!file.exists()) {
			return;
		}
		
		// 2. 清理目录而不删除它
		if (!FileHandler.isSymlink(file)) {
			cleanDirectory(file);
		}
		
		// 3. 删除目录或文件本身
		file.delete();
	}

	/**
	 * 删除目录下的内容而不删除目录本身
	 * @param path	目录的绝对路径
	 * @throws IOException 
	 */
	public static void cleanDirectory(String path) throws IOException {
		File file = new File(path);
		cleanDirectory(file);
	}
	
	/**
	 * 删除目录下的内容而不删除目录本身
	 * @param directory	File文件对象
	 * @throws IOException 
	 */
	public static void cleanDirectory(File directory) throws IOException {
		FileHandler.requireExists(directory);
		if (!directory.exists()) {
			return;
		}
		
		if (directory.isDirectory()) {
			File[] files = listFiles(directory);
			for (File file : files) {
				deleteFileOrDirectory(file);
			}
		}
	}

	/**
	 * 列出指定目录下的文件
	 * @param path  目录的绝对路径
	 * @return
	 * @throws IOException
	 */
	public static File[] listFiles(String path) throws IOException {
		File directory = new File(path);
		return listFiles(directory);
	}
	
	/**
	 * 列出指定目录下的文件
	 * @param directory	File文件对象
	 * @return
	 * @throws IOException
	 */
	public static File[] listFiles(File directory) throws IOException {
		FileHandler.requireDirectoryExists(directory);
		File[] files = directory.listFiles();
		if (files == null) {
			throw new IOException("Unknown I/O error listing contents of directory: " + directory);
		}
		return files;
	}
	
	/**
	 * 递归列出指定目录下的所有文件（不列出文件夹）
	 * @param path  目录的绝对路径
	 * @return
	 * @throws IOException 
	 */
	public static List<File> listDeepFiles(String path) throws IOException {
		File directory = new File(path);
		return listDeepFiles(directory);
	}
	
	/**
	 * 递归列出指定目录下的所有文件（不列出文件夹）
	 * @param directory
	 * @return
	 * @throws IOException 
	 */
	public static List<File> listDeepFiles(File directory) throws IOException {
		List<File> fileList = new ArrayList<File>();
		
		File[] files = listFiles(directory);
		for (File file : files) {
			if (file.isFile()) {
				fileList.add(file);
			} else {
				// 递归，获取路径中子路径中的所有文件
				fileList.addAll(listDeepFiles(file));
			}
		}
		
		return fileList;
	}
	
	/**
	 * 递归列出指定目录下的所有文件（列出文件夹）
	 * @param path  目录的绝对路径
	 * @return
	 * @throws IOException 
	 */
	public static List<TreeFile> listDeepFilesAndDirectorys(String path) throws IOException {
		File directory = new File(path);
		return listDeepFilesAndDirectorys(directory);
	}
	
	/**
	 * 递归列出指定目录下的所有文件（列出文件夹）
	 * @param directory
	 * @return
	 * @throws IOException 
	 */
	public static List<TreeFile> listDeepFilesAndDirectorys(File directory) throws IOException {
		List<TreeFile> fileList = new ArrayList<>();
		
		File[] files = listFiles(directory);
		for (File file : files) {
			TreeFile treeFile = new TreeFile();
			treeFile.setFile(file);
			
			if (file.isDirectory()) {
				// 递归，获取路径中子路径中的所有文件
				List<TreeFile> listDeepFilesAndDirectorys = listDeepFilesAndDirectorys(file);
				treeFile.setChildren(listDeepFilesAndDirectorys);
			}
			
			fileList.add(treeFile);
		}
		
		return fileList;
	}
	
	/**
	 * 复制一个文件到一个新位置
	 * 如果目标文件不存在，则创建保存目标文件。如果目标文件存在，则覆盖
	 *
	 * @param srcPath 要复制的源文件的绝对路径
	 * @param destPath 要复制的目标文件的绝对路径
	 * @throws IOException 
	 */
	public static void copyFile(String srcPath, String destPath) throws IOException {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		
		copyFile(srcFile, destFile);
	}
	
	/**
	 * 复制一个文件到一个新位置
	 * 如果目标文件不存在，则创建保存目标文件。如果目标文件存在，则覆盖
	 *
	 * @param srcFile 要复制的源文件，不能为null
	 * @param destFile 要复制的目标文件，不能为null
	 * @throws IOException 
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		// 1. 校验文件合法性
		// 1.1 校验文件复制操作需要的参数属性
		FileHandler.requireFileCopy(srcFile, destFile);
		// 1.2 校验复制源是不是一个文件
		FileHandler.requireFile(srcFile);
		// 1.3 校验2个文件是否是同一个文件，如果是，则报错
		FileHandler.requireCanonicalPathsNotEquals(srcFile, destFile);
		// 1.4 为目标文件创建父级目录，如果父级目录不存在的话
		FileHandler.createParentDirectories(destFile);
		// 1.5 目标文件对应的File对象不能为null，且该File对象代表的必须是一个文件
		FileHandler.requireFileIfExists(destFile);
		// 1.6 目标文件如果本身存在，代表覆盖操作，则目标文件必须是可写的
		if (destFile.exists()) {
			FileHandler.requireCanWrite(destFile);
		}

		// 2. 复制文件（目标文件存在，则覆盖）
		Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

		// 3. 校验复制的完整性
		FileHandler.requireEqualSizes(srcFile, destFile, srcFile.length(), destFile.length());
	}
	
	/**
	 * 复制目录（不复制源目录本身）
	 * 将指定的目录及其所有子目录和文件复制到指定的目标
	 * 目标是目录的新位置和名称
	 * 如果目标目录不存在，将创建该目录
	 * 如果目标目录确实存在，则此方法将源与目标合并，源文件会覆盖目标文件
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public static void copyDirectory(String srcPath, String destPath) throws IOException {
		File srcDir = new File(srcPath);
		File destDir = new File(destPath);
		
		copyDirectory(srcDir, destDir);
	}
	
	/**
	 * 复制目录（不复制源目录本身）
	 * 将指定的目录及其所有子目录和文件复制到指定的目标
	 * 目标是目录的新位置和名称
	 * 如果目标目录不存在，将创建该目录
	 * 如果目标目录确实存在，则此方法将源与目标合并，源文件会覆盖目标文件
	 * @param srcDir
	 * @param destDir
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir) throws IOException {
		// 1. 校验文件合法性
		// 1.1 校验文件复制操作需要的参数属性
		FileHandler.requireFileCopy(srcDir, destDir);
		// 1.2 校验复制源是否是一个目录
		FileHandler.requireDirectory(srcDir);
		// 1.3 校验2个File对象是否是同一个文件路径，如果是，则报错
		FileHandler.requireCanonicalPathsNotEquals(srcDir, destDir);

		// 2. 源目录中的目录分类
		List<String> exclusionList = null;
		String srcDirCanonicalPath = srcDir.getCanonicalPath();
		String destDirCanonicalPath = destDir.getCanonicalPath();
		if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
			File[] srcFiles = listFiles(srcDir);
			if (srcFiles.length > 0) {
				exclusionList = new ArrayList<>(srcFiles.length);
				for (File srcFile : srcFiles) {
					File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		
		// 3. 复制目录
		doCopyDirectory(srcDir, destDir, exclusionList);
	}
	
	/**
	 * 复制目录的具体实现
	 * @param srcDir
	 * @param destDir
	 * @param exclusionList
	 * @throws IOException
	 */
	private static void doCopyDirectory(File srcDir, File destDir, List<String> exclusionList) throws IOException {
		// 1. 创建目标目录
		FileHandler.mkdirs(destDir);
		FileHandler.requireCanWrite(destDir);
		
		// 2. 递归遍历目录，复制文件
		File[] srcFiles = listFiles(srcDir);
		for (File srcFile : srcFiles) {
			File dstFile = new File(destDir, srcFile.getName());
			if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
				if (srcFile.isDirectory()) {
					doCopyDirectory(srcFile, dstFile, exclusionList);
				} else {
					copyFile(srcFile, dstFile);
				}
			}
		}
	}
	
	/**
	 * 复制目录（复制源目录本身）
	 * 将指定的目录及其所有子目录和文件复制到指定的目标
	 * 目标是目录的新位置和名称
	 * 如果目标目录不存在，将创建该目录
	 * 如果目标目录确实存在，则此方法将源与目标合并，源文件会覆盖目标文件
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public static void copyDirectoryToDirectory(String srcPath, String destPath) throws IOException {
		File srcDir = new File(srcPath);
		File destDir = new File(destPath);
		
		copyDirectoryToDirectory(srcDir, destDir);
	}
	
	/**
	 * 复制目录（复制源目录本身）
	 * 将指定的目录及其所有子目录和文件复制到指定的目标
	 * 目标是目录的新位置和名称
	 * 如果目标目录不存在，将创建该目录
	 * 如果目标目录确实存在，则此方法将源与目标合并，源文件会覆盖目标文件
	 * @param sourceDir
	 * @param destinationDir
	 * @throws IOException
	 */
	public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
		// 1. File校验
		FileHandler.requireDirectory(srcDir);
		FileHandler.requireDirectoryIfExists(destDir);
		
		// 2. 复制目录
		copyDirectory(srcDir, new File(destDir, srcDir.getName()));
	}
	
	/**
	 * 移动文件
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public static void moveFile(String srcPath, String destPath) throws IOException {
		copyFile(srcPath, destPath);
		deleteFileOrDirectory(srcPath);
	}
	
	/**
	 * 移动目录（不移动源目录本身）
	 * 将指定的目录及其所有子目录和文件复制到指定的目标
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public static void moveDirectory(String srcPath, String destPath) throws IOException {
		copyDirectory(srcPath, destPath);
		deleteFileOrDirectory(srcPath);
	}
	
	/**
	 * 移动目录（移动源目录本身）
	 * 将指定的目录及其所有子目录和文件复制到指定的目标
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public static void moveDirectoryToDirectory(String srcPath, String destPath) throws IOException {
		copyDirectoryToDirectory(srcPath, destPath);
		deleteFileOrDirectory(srcPath);
	}
	
	/**
	 * 强制创建文件夹，如果该文件夹父级目录不存在，会自动创建父级目录
	 * @param directoryPath
	 * @throws IOException
	 */
	public static void forceMkdir(String directoryPath) throws IOException {
		forceMkdir(new File(directoryPath));
	}
	
	/**
	 * 强制创建文件夹，如果该文件夹父级目录不存在，会自动创建父级目录
	 * @param directory
	 * @throws IOException
	 */
	public static void forceMkdir(File directory) throws IOException {
		FileHandler.mkdirs(directory);
	}
	
	/**
	 * 向文件中写入内容
	 * 如果该文件不存在，则自动创建
	 * @param filePath	 文件的绝对路径
	 * @param data		 写入的数据
	 * @param charsetName  字符集名称（例如：UTF-8），如果使用系统默认字符集的话，就填写null
	 * @param isAppend	 是否追加写入(true)，或者覆盖写入(false)
	 * @throws IOException
	 */
	public static void write(String filePath, String data, String charsetName, boolean isAppend)
			throws IOException {
		write(new File(filePath), data, charsetName, isAppend);
	}
	
	/**
	 * 向文件中写入内容
	 * 如果该文件不存在，则自动创建
	 * @param file		 File对象
	 * @param data		 写入的数据
	 * @param charsetName  字符集名称（例如：UTF-8），如果使用系统默认字符集的话，就填写null
	 * @param isAppend	 是否追加写入(true)，或者覆盖写入(false)
	 * @throws IOException
	 */
	public static void write(File file, String data, String charsetName, boolean isAppend)
			throws IOException {
		try (OutputStream output = IOUtils.openOutputStream(file, isAppend)) {
			if (data != null) {
				output.write(data.getBytes(Charsets.toCharset(charsetName)));
			}
		}
	}

	/**
	 * 将文件内容读取为字符串
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		return readFile(new File(filePath), null);
	}
	
	/**
	 * 将文件内容读取为字符串
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		return readFile(file, null);
	}
	
	/**
	 * 将文件内容读取为字符串
	 * @param filePath
	 * @param charsetName 字符集名称（例如：UTF-8），如果使用系统默认字符集的话，就填写null
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath, String charsetName) throws IOException {
		return readFile(new File(filePath), charsetName);
	}
	
	/**
	 * 将文件内容读取为字符串
	 * @param file
	 * @param charsetName 字符集名称（例如：UTF-8），如果使用系统默认字符集的话，就填写null
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file, String charsetName) throws IOException {
		// 校验文件大小是否在2G内
		boolean checkFileSize = FileHandler.checkFileSize(file, 2, "G");
		if (checkFileSize) {
			byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
			return new String(bytes, Charsets.toCharset(charsetName));
		} else {
			try (InputStream inputStream = IOUtils.openInputStream(file)) {
				ByteArrayOutputStream result = new ByteArrayOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int length;
				
				while ((length = inputStream.read(buffer)) != -1) {
					result.write(buffer, 0, length);
				}
				
				charsetName = charsetName==null ? Charset.defaultCharset().name() : charsetName;
				
				return result.toString(charsetName);
			}
		}
	}

	/**
	 * 下载resources文件夹下的文件（不重命名）
	 * @param filePath  resources文件夹下的路径，例如：template/excel/模板.xlsx
	 */
	public static void downloadFileFromResource(HttpServletResponse response, String filePath) {
		downloadFileFromResource(response, filePath, null);
	}
	
	/**
	 * 下载resources文件夹下的文件
	 * @param filePath  resources文件夹下的路径，例如：template/excel/模板.xlsx
	 * @param newFilename   重命名文件名称（带后缀）
	 */
	public static void downloadFileFromResource(HttpServletResponse response, String filePath, String newFilename) {
		if (filePath.startsWith("/")) {
			filePath = filePath.substring(1);
		}
		
		InputStream inputStream =  Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		downloadFile(response, inputStream, newFilename);
	}
	
	/**
	 * 下载文件（InputStream流）
	 * @param inputStream
	 * @param newFilename  重命名文件名称（带后缀）
	 */
	public static void downloadFile(HttpServletResponse response, InputStream inputStream, String newFilename) {
		OutputStream out = null;
		
		try {
			newFilename = java.net.URLEncoder.encode(newFilename, "UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=" + newFilename);
			out = response.getOutputStream();
			
			int b = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((b = inputStream.read(buffer)) != -1) {
				out.write(buffer, 0, b);
			}
			
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(inputStream);
		}
	}

	/**
	 * 下载文件（byte[]字节数组）
	 * @param bytes
	 * @param newFilename   重命名文件名称（带后缀）
	 */
	public static void downloadFile(HttpServletResponse response, byte[] bytes, String newFilename) {
		try (OutputStream out = response.getOutputStream()) {
			if (bytes != null) {
				newFilename = java.net.URLEncoder.encode(newFilename, "UTF-8");
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition", "attachment; filename=" + newFilename);
				out.write(bytes);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载文件（不重命名）
	 * @param filePath  文件的绝对路径（带具体的文件名）
	 */
	public static void downloadFile(HttpServletResponse response, String filePath) {
		downloadFile(response, filePath, null);
	}
	
	/**
	 * 下载文件（重命名）
	 * @param filePath  文件的绝对路径（带具体的文件名）
	 * @param newFilename  重命名文件名称（带后缀）
	 */
	public static void downloadFile(HttpServletResponse response, String filePath, String newFilename) {
		File file = new File(filePath);
		if (StringUtils.isEmpty(newFilename)) {
			newFilename = file.getName();
		}
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(newFilename, "UTF-8"));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[BUFFER_SIZE];
			while (true) {
				int bytesRead;
				if (-1 == (bytesRead=bis.read(buff, 0, buff.length))) {
					break;
				}
				bos.write(buff, 0, bytesRead);
			}
			
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(bis);
		}
	}
	
	/**
	 * 获取文件的后缀
	 * <pre>
	 * foo.txt	  --&gt; "txt"
	 * a/b/c.jpg	--&gt; "jpg"
	 * a/b.txt/c	--&gt; ""
	 * a/b/c		--&gt; ""
	 * </pre>
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		if (StringUtils.isEmpty(filename)) {
			return filename;
		}
		
		// 如果是Linux下的特殊文件后缀，则直接返回
		for (String specialExtension : SPECIAL_EXTENSION) {
			if (filename.endsWith(EXTENSION_SEPARATOR_STR + specialExtension)) {
				return specialExtension;
			}
		}
		
		int index = FilenameHandler.indexOfExtension(filename);
		if (index == NOT_FOUND) {
			return EMPTY_STRING;
		}
		return filename.substring(index + 1);
	}
	
	/**
	 * 获取文件的名称，不包括文件后缀和文件路径
	 * <pre>
	 * a/b/c.txt --&gt; c
	 * a.txt     --&gt; a
	 * a/b/c     --&gt; c
	 * a/b/c/    --&gt; ""
	 * </pre>
	 * @param filename
	 * @return
	 */
	public static String getBaseName(String filename) {
		filename =  FilenameHandler.getName(filename);
		String extension = getExtension(filename);
		
		return filename.replace(EXTENSION_SEPARATOR_STR + extension, "");
	}

	/**
	 * 返回文件全名（返回最后一个正斜杠或反斜杠之后的文本）
	 * <pre>
	 * a/b/c.txt --&gt; c.txt
	 * a.txt	 --&gt; a.txt
	 * a/b/c	 --&gt; c
	 * a/b/c/	--&gt; ""
	 * </pre>
	 * @param filename
	 * @return
	 */
	public static String getName(String filename) {
		if (StringUtils.isEmpty(filename)) {
			return filename;
		}
		
		int index = FilenameHandler.indexOfLastSeparator(filename);
		return filename.substring(index + 1);
	}

	/**
	 * 获取文件路径，不包括文件名
	 * <p>
	 * <pre>
	 * C:\a\b\c.txt --&gt; C:\a\b\
	 * a.txt		--&gt; ""
	 * a/b/c		--&gt; a/b/
	 * </pre>
	 * <p>
	 * @param filename
	 * @return
	 */
	public static String getFullPath(String filename) {
		String name = getName(filename);
		int indexOf = filename.indexOf(name);
		return filename.substring(0, indexOf);
	}
	
}
