package cn.javaex.htool.crypto.digest;

import java.util.Arrays;

public class Hex {
	
	/**
	 * byte[] 转 字符串
	 * @param bytes
	 * @return
	 */
	public static String toHexString(byte[] bytes) {
		StringBuilder buf = new StringBuilder();
		for (byte b : bytes) {
			buf.append(leftPad(Integer.toHexString(b & 0xff), '0', 2));
		}
		return buf.toString();
	}
	
	private static String leftPad(String hex, char c, int size) {
		char[] cs = new char[size];
		Arrays.fill(cs, c);
		System.arraycopy(hex.toCharArray(), 0, cs, cs.length - hex.length(), hex.length());
		return new String(cs);
	}
}
