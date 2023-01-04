package cn.javaex.htool.crypto.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法加密工具类
 * 
 * @author 陈霓清
 * @Date 2022年12月4日
 */
public class DigestUtils extends Hex {
	
	private static final String MD5 = "md5";
	private static final String SHA1 = "SHA-1";
	private static final String SHA256 = "SHA-256";
	private static final String SHA384 = "SHA-384";
	private static final String SHA512 = "SHA-512";
	
	/**
	 * 将传入的字符串进行MD5加密，并返回加密后的字符串
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		try {
			byte[] md5 = digest(str.getBytes("utf-8"), MD5);
			return toHexString(md5);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 将传入的字符串进行sha1加密，并返回加密后的字符串
	 * @param str
	 * @return
	 */
	public static String sha1(String str) {
		try {
			byte[] sha = digest(str.getBytes("utf-8"), SHA1);
			return toHexString(sha);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 将传入的字符串进行sha256加密，并返回加密后的字符串
	 * @param str
	 * @return
	 */
	public static String sha256(String str) {
		try {
			byte[] sha = digest(str.getBytes("utf-8"), SHA256);
			return toHexString(sha);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 将传入的字符串进行sha384加密，并返回加密后的字符串
	 * @param str
	 * @return
	 */
	public static String sha384(String str) {
		try {
			byte[] sha = digest(str.getBytes("utf-8"), SHA384);
			return toHexString(sha);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 将传入的字符串进行sha512加密，并返回加密后的字符串
	 * @param str
	 * @return
	 */
	public static String sha512(String str) {
		try {
			byte[] sha = digest(str.getBytes("utf-8"), SHA512);
			return toHexString(sha);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private static byte[] digest(byte[] data, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(data);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new byte[] {};
	}
	
}
