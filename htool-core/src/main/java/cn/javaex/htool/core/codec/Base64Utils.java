package cn.javaex.htool.core.codec;

import java.util.Base64;

/**
 * Base64工具
 * 
 * @author 陈霓清
 * @Date 2022年11月26日
 */
public class Base64Utils {
	
	/** 字母表 */
	private static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
	
	private static byte[] CODES = new byte[256];

	static {
		for (int i = 0; i < 256; i++) {
			CODES[i] = -1;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			CODES[i] = (byte) (i - 'A');
		}

		for (int i = 'a'; i <= 'z'; i++) {
			CODES[i] = (byte) (26 + i - 'a');
		}
		for (int i = '0'; i <= '9'; i++) {
			CODES[i] = (byte) (52 + i - '0');
		}
		CODES['+'] = 62;
		CODES['/'] = 63;
	}
	
	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}

	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		return new String(Base64.getDecoder().decode(str));
	}
	
	/**
	 * byte[] 转 String
	 * @param  bytes
	 * @return String
	 */
	public static String encodeByte(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	/**
	 * String 转 byte[]
	 * @param str
	 * @return
	 */
	public static byte[] decodeByte(String str) {
		return Base64.getDecoder().decode(str);
	}
	
	/**
	 * byte[] 转 char[]
	 * @param data
	 * @return
	 */
	public static char[] encodeChar(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;

			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
			val |= (0xFF & (int) data[i + 1]);
			trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
			val |= (0xFF & (int) data[i + 2]);
			quad = true;
			}
			out[index + 3] = ALPHABET[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = ALPHABET[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = ALPHABET[val & 0x3F];
			val >>= 6;
			out[index + 0] = ALPHABET[val & 0x3F];
		}
		return out;
	}
	
	/**
	 * char[] 转 byte[]
	 * @param data
	 * @return
	 */
	public static byte[] decodeChar(char[] data) {
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || CODES[data[ix]] < 0) {
				--tempLen; // ignore non-valid chars and padding
			}
		}

		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3) {
			len += 2;
		}
		if ((tempLen % 4) == 2) {
			len += 1;

		}
		byte[] out = new byte[len];

		int shift = 0; // # of excess bits stored in accum
		int accum = 0; // excess bits
		int index = 0;

		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : CODES[data[ix]];

			if (value >= 0) { // skip over non-code
				accum <<= 6; // bits shift up by 6 each time thru
				shift += 6; // loop, with new bits being put in
				accum |= value; // at the bottom.
				if (shift >= 8) { // whenever there are 8 or more shifted in,
					shift -= 8; // write them out (from the top, leaving any
					out[index++] = // excess at the bottom for next iteration.
					(byte) ((accum >> shift) & 0xff);
				}
			}
		}

		if (index != out.length) {
			throw new Error("Miscalculated data length (wrote " + index
					+ " instead of " + out.length + ")");
		}

		return out;
	}
	
}
