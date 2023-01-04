package cn.javaex.htool.crypto.sign;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import cn.javaex.htool.core.codec.Base64Utils;
import cn.javaex.htool.core.string.StringUtils;

/**
 * RSA非对称加密
 * 
 * @author 陈霓清
 * @Date 2022年12月4日
 */
public class SignUtils extends Sign {

	/**
	 * 用私钥对信息生成数字签名
	 * @param data   //加密数据
	 * @param privateKey //私钥
	 * @return
	 * @throws Exception
	 */
	public static String sign(String content, String privateKey) throws Exception {
		byte[] data =content.getBytes("UTF-8");
		// 解密私钥
		byte[] keyBytes = Base64Utils.decodeByte(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		// 取私钥匙对象
		PrivateKey key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(key);
		signature.update(data);
		
		return StringUtils.formatNoSTRN(Base64Utils.encodeByte(signature.sign()));
	}
	
	/**
	 * 校验数字签名
	 * @param data   加密数据
	 * @param publicKey  公钥
	 * @param sign   数字签名
	 * @return
	 */
	public static boolean verify(String content, String publicKey, String sign) {
		try {
			byte[] data = content.getBytes("UTF-8");
			// 解密公钥
			byte[] keyBytes = Base64Utils.decodeByte(publicKey);
			// 构造X509EncodedKeySpec对象
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
			// 指定加密算法
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
			// 取公钥匙对象
			PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
			
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(key);
			signature.update(data);
			
			// 验证签名是否正常
			return signature.verify(Base64Utils.decodeByte(sign));
		} catch (Exception e) {
			
		}
		
		return false;
	}
	
}
