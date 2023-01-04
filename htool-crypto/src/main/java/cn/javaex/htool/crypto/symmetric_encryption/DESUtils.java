package cn.javaex.htool.crypto.symmetric_encryption;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import cn.javaex.htool.core.codec.Base64Utils;

/**
 * DES对称加密
 * 
 * @author 陈霓清
 * @Date 2022年12月5日
 */
public class DESUtils {

	/** 偏移变量，固定占8位字节 */
	private final static String IV_PARAMETER = "12345678";
	/** 密钥算法 */
	private static final String ALGORITHM = "DES";
	/** 加密/解密算法-工作模式-填充模式 */
	private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
	/** 默认编码 */
	private static final String CHARSET = "utf-8";
	
	/**
	 * 加密
	 *
	 * @param data     待加密字符串
	 * @param password 加密密码，长度不能够小于8位
	 * @return 加密后内容
	 */
	public static String encrypt(String data, String password) {
		if (password == null || password.length() < 8) {
			throw new RuntimeException("加密失败，key不能小于8位");
		}
		if (data == null)
			return null;
		try {
			Key secretKey = generateKey(password);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

			return Base64Utils.encodeByte(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * 解密
	 *
	 * @param data     待解密字符串
	 * @param password 解密密码，长度不能够小于8位
	 * @return 解密后内容
	 */
	public static String decrypt(String data, String password) {
		if (password == null || password.length() < 8) {
			throw new RuntimeException("加密失败，key不能小于8位");
		}
		if (data == null)
			return null;
		try {
			Key secretKey = generateKey(password);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			return new String(cipher.doFinal(Base64Utils.decodeByte(data)), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * 生成key
	 *
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static Key generateKey(String password) throws Exception {
		DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		return keyFactory.generateSecret(dks);
	}
	
}
