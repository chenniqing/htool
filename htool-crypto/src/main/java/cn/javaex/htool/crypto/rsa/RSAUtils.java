package cn.javaex.htool.crypto.rsa;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import cn.javaex.htool.core.codec.Base64Utils;
import cn.javaex.htool.core.string.StringUtils;

/**
 * RSA非对称加密
 * 
 * @author 陈霓清
 * @Date 2022年12月4日
 */
public class RSAUtils extends RSA {

	/**
	 * 用私钥加密
	 * @param content 加密数据
	 * @param privateKey 私钥
	 * @return
	 * @throws Exception 
	 */
	public static String encryptByPrivateKey(String content, String privateKey) throws Exception {
		List<String> array = new ArrayList<String>();
		int length = 29;
		while (length < content.length()) { 
			array.add(content.substring(0, length)); 
			content = content.substring(length,content.length());
		}
		array.add(content);
		String returnstr = "";
		for (String sub : array) {
			byte[] data = sub.getBytes("UTF-8");
			//解密密钥
			byte[] keyBytes = Base64Utils.decodeByte(privateKey);
			//取私钥
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
			Key key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			//对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			byte[] returndate = cipher.doFinal(data);
			returnstr = returnstr + StringUtils.formatNoSTRN(Base64Utils.encodeByte(returndate)) + ",";
		}
		if (returnstr.endsWith(",")) {
			returnstr =returnstr.substring(0, returnstr.lastIndexOf(","));
		}
		
		return returnstr;
	}

	/**
	 * 用公钥解密
	 * @param content 加密数据
	 * @param publicKey 公钥
	 * @return
	 * @throws Exception 
	 */
	public static String decryptByPublicKey(String content, String publicKey) throws Exception {
		//对私钥解密
		String[] array = content.split(",");
		String returnstr = "";
		
		for (String sub : array) {
			byte[] data = Base64Utils.decodeByte(sub);
			byte[] keyBytes = Base64Utils.decodeByte(publicKey);
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
			Key key = keyFactory.generatePublic(x509EncodedKeySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] rtndate = cipher.doFinal(data);
			String temp_str = new String(rtndate, "UTF-8");
			returnstr =returnstr+temp_str;
		}
		
		return returnstr;
	}
	
}
