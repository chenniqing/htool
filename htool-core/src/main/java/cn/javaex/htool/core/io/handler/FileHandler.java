package cn.javaex.htool.core.io.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 文件处理工具类
 * 
 * @author 陈霓清
 * @Date 2022年12月7日
 */
public class FileHandler {
	
    /**
     * 判断指定的文件是否是符号链接而不是实际文件
     *
     * @param file the file to test.
     * @return true if the file is a symbolic link
     * @see Files#isSymbolicLink(Path)
     */
    public static boolean isSymlink(File file) {
        return file != null && Files.isSymbolicLink(file.toPath());
    }
    
    /**
     * 要求给定的文件存在并且是一个目录
     * @param directory 待校验的目录对象
     * @return
     */
    public static File requireDirectoryExists(File directory) {
        requireExists(directory);
        requireDirectory(directory);
        return directory;
    }
    
    /**
     * 如果给定的文件存在，则必须是一个目录
     * @param directory
     * @return
     */
	public static File requireDirectoryIfExists(File directory) {
		Objects.requireNonNull(directory);
        if (directory.exists()) {
            requireDirectory(directory);
        }
        return directory;
	}
	
    /**
     * 要求文件或目录必须存在
     * @param file
     * @return
     */
    public static File requireExists(File file) {
        Objects.requireNonNull(file);
        if (!file.exists()) {
            throw new IllegalArgumentException(
                "File system element for parameter does not exist: '" + file + "'");
        }
        return file;
    }

    /**
     * 校验文件是否是一个目录，不是则报错
     * @param directory
     * @param name
     * @return
     */
    public static File requireDirectory(File directory) {
        Objects.requireNonNull(directory);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter is not a directory: '" + directory + "'");
        }
        return directory;
    }

    /**
     * 校验文件复制操作需要参数属性
     *
     * @param source 源文件
     * @param destination 目标文件
     * @throws FileNotFoundException
     */
    public static void requireFileCopy(File source, File destination) throws FileNotFoundException {
    	requireExists(source);
        Objects.requireNonNull(destination);
    }

    /**
     * 要求file必须存在，且是一个文件，而不是目录
     * @param file
     * @return
     */
    public static File requireFile(File file) {
        Objects.requireNonNull(file);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Parameter is not a file: " + file);
        }
        return file;
    }

    /**
     * 校验2个文件是否是同一个文件路径，如果是，则报错
     * @param file1 第1个比较文件
     * @param file2 第2个比较文件
     * @throws IOException
     */
    public static void requireCanonicalPathsNotEquals(File file1, File file2) throws IOException {
        String canonicalPath = file1.getCanonicalPath();
        if (canonicalPath.equals(file2.getCanonicalPath())) {
            throw new IllegalArgumentException(String
                .format("File canonical paths are equal: '%s' (file1='%s', file2='%s')", canonicalPath, file1, file2));
        }
    }
    
    /**
     * 为File对象创建父级目录
     * @param file
     * @return
     * @throws IOException
     */
    public static File createParentDirectories(File file) throws IOException {
        return mkdirs(getParentFile(file));
    }
    
    /**
     * 获取给定文件的父级文件
     * 给定的文件可能是null，文件的父级也可能是null
     * @param file
     * @return
     */
    private static File getParentFile(File file) {
        return file == null ? null : file.getParentFile();
    }
    
    /**
     * 创建文件夹
     * @param directory
     * @return
     * @throws IOException
     */
    public static File mkdirs(File directory) throws IOException {
        if ((directory != null) && (!directory.mkdirs() && !directory.isDirectory())) {
            throw new IOException("Cannot create directory '" + directory + "'.");
        }
        return directory;
    }
    
    /**
     * 要求给定的文件如果存在，就必须是一个文件，而不是目录
     * @param file
     * @return
     */
    public static File requireFileIfExists(File file) {
        Objects.requireNonNull(file);
        return file.exists() ? requireFile(file) : file;
    }
    
    /**
     * 要求文件必须是可写的
     * @param file
     */
    public static void requireCanWrite(File file) {
        Objects.requireNonNull(file);
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File parameter is not writable: '" + file + "'");
        }
    }
    
    /**
     * 要求2个文件的长度相等
     * @param srcFile    源文件
     * @param destFile   目标文件
     * @param srcLen     源文件长度
     * @param dstLen     目标文件长度
     * @throws IOException
     */
    public static void requireEqualSizes(File srcFile, File destFile, long srcLen, long dstLen)
            throws IOException {
        if (srcLen != dstLen) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile
                    + "' Expected length: " + srcLen + " Actual: " + dstLen);
        }
    }

    /**
     * 判断文件大小处于限制内
     *
     * @param file      文件
     * @param limitSize 限制大小
     * @param fileUnit  限制的单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(File file, int limitSize, String fileUnit) {
        double fileSizeCom = 0;
        switch (fileUnit.toUpperCase()) {
			case "B":
				fileSizeCom = (double) file.length();
				break;
			case "K":
				fileSizeCom = (double) file.length() / 1024;
				break;
			case "M":
				fileSizeCom = (double) file.length() / (1024*1024);
				break;
			case "G":
				fileSizeCom = (double) file.length() / (1024*1024*1024);
				break;
			default:
				break;
		}
        
        return limitSize > fileSizeCom;
    }
    
	/**
	 * 得到文件在文件夹中的相对路径，保持原有结构
	 * @param srcPath
	 * @param file
	 * @return
	 */
	public static String getRealName(String srcPath, File file) {
		return file.getAbsolutePath().replace(srcPath + File.separator, "").replace(srcPath, "");
	}
}
