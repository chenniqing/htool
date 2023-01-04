package cn.javaex.htool.crypto.sign;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import cn.javaex.htool.core.codec.Base64Utils;
import cn.javaex.htool.core.string.StringUtils;

/**
 * RSA
 * 
 * @author 陈霓清
 * @Date 2022年12月4日
 */
public class Sign {
	/** key算法 */
	public static final String KEY_ALGORTHM = "RSA";
	/** 签名算法 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	
	/** 公钥 */
	private String publicKey;
	/** 私钥 */
	private String privateKey;
	
	/**
	 * 构造方法，生成新的私钥公钥对
	 */
	public Sign() {
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
			keyPairGenerator.initialize(1024);
			
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			//公钥
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			//私钥
			RSAPrivateKey privateKey =  (RSAPrivateKey) keyPair.getPrivate();
			
			this.publicKey = StringUtils.formatNoSTRN(Base64Utils.encodeByte(((Key) publicKey).getEncoded()));
			this.privateKey = StringUtils.formatNoSTRN(Base64Utils.encodeByte(((Key) privateKey).getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
}
