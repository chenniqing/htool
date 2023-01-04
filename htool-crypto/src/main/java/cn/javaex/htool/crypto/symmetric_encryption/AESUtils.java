package cn.javaex.htool.crypto.symmetric_encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.javaex.htool.core.codec.Base64Utils;

/**
 * AES对称加密
 * 
 * @author 陈霓清
 * @Date 2022年12月5日
 */
public class AESUtils {

	/** 密钥key */
	public static final String KEY = "oVPSRgw8o1IWmkZn";
	/** 向量iv */
	public static final String IV = "KLyWDN7IivzcIgRW";

	/**
	 * 加密（使用系统默认key和iv）
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content) throws Exception {
		byte[] raw = KEY.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		// "算法/模式/补码方式"
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
		byte[] encrypted = cipher.doFinal(content.getBytes());
		return Base64Utils.encodeByte(encrypted);
	}

	/**
	 * 加密
	 * @param content
	 * @param key 密钥key
	 * @param iv  向量iv
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content, String key, String iv) throws Exception {
		if (content == null) {
			return null;
		}
		byte[] raw = key.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		// "算法/模式/补码方式"
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
		byte[] encrypted = cipher.doFinal(content.getBytes());
		return Base64Utils.encodeByte(encrypted);
	}

	/**
	 * 解密（使用系统默认key和iv）
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content) throws Exception {
		try {
			byte[] raw = KEY.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
			byte[] encrypted1 = Base64Utils.decodeByte(content);
			byte[] original = cipher.doFinal(encrypted1);
			return new String(original);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 解密
	 * @param content
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content, String key, String iv) throws Exception {
		try {
			byte[] raw = key.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
			byte[] encrypted = Base64Utils.decodeByte(content);
			byte[] original = cipher.doFinal(encrypted);
			return new String(original);
		} catch (Exception ex) {
			return null;
		}
	}
}
