package cn.javaex.htool.core.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字计算和转换工具类
 * 
 * @author 陈霓清
 * @Date 2022年12月4日
 */
public class NumUtils {
	
	private static final int DEF_DIV_SCALE = 10;	// 默认除法运算精度
	
	private static final String CHINESE_LOWER_NUM_UNIT = "千百十亿千百十万千百十";
	private static final String CHINESE_LOWER_NUM_CHAR = "零一二三四五六七八九";
	private static final String CHINESE_LOWER_NUM_10 = "一十";
	
	private static final String CHINESE_UPPER_NUM_UNIT = "仟佰拾亿仟佰拾万仟佰拾";
	private static final String CHINESE_UPPER_NUM_CHAR = "零壹贰叁肆伍陆柒捌玖";
	private static final String CHINESE_UPPER_NUM_10 = "壹拾";
	
	//==========以BigDecimal类为基础的精确浮点数运算==========//
	/**
	 * 提供精确的加法运算
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale<0) {
			throw new IllegalArgumentException("小数位数必须是正整数或零");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		if (b1.compareTo(BigDecimal.ZERO)==0) {
			return BigDecimal.ZERO.doubleValue();
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale<0) {
			throw new IllegalArgumentException("小数位数必须是正整数或零");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	//==========以BigDecimal类为基础的精确浮点数运算==========//
	
	//==========数字转换==========//
	/**
	 * 数字转中文小写数字
	 * @param num
	 * @return
	 */
	public static String toChineseLowerNumeral(String num) {
		return toChineseNumeral(String.valueOf(num), CHINESE_LOWER_NUM_UNIT, CHINESE_LOWER_NUM_CHAR, CHINESE_LOWER_NUM_10);
	}
	
	/**
	 * 数字转中文大写数字
	 * @param num
	 * @return
	 */
	public static String toChineseUpperNumeral(String num) {
		return toChineseNumeral(String.valueOf(num), CHINESE_UPPER_NUM_UNIT, CHINESE_UPPER_NUM_CHAR, CHINESE_UPPER_NUM_10);
	}
	
	/**
	 * 将数字转为带逗号的字符串
	 * @param num
	 * @return 例如：9,876,543,210.50
	 */
	public static String toWrittenAmount(double num) {
		DecimalFormat df = new DecimalFormat("#,###.00");
		return df.format(num);
	}

	/**
	 * 将数字转为带逗号的字符串
	 * @param num
	 * @return 例如：987,654,321
	 */
	public static String toWrittenAmount(long num) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}
	
	/**
	 * 数字转中文数字
	 * @param num
	 * @param sUnit
	 * @param sNumChar
	 * @param num10
	 * @return
	 */
	private static String toChineseNumeral(String num, String sUnit, String sNumChar, String num10) {
		int pos = num.indexOf('.');
		String sBeforeDot = "";
		String sAfterDot = "";
		
		if (pos == -1) {
			sBeforeDot = num;
		} else {
			sBeforeDot = num.substring(0, pos);
			sAfterDot = num.substring(pos + 1);
		}
		
		String sRet = "";
		char c = '\000';
		for (int i = 0; i < sBeforeDot.length(); i++) {
			c = sBeforeDot.charAt(i);
			if (c == '0') {
				if (i < sBeforeDot.length() - 1) {
					String sTmp = sUnit.substring(sUnit.length() - sBeforeDot.length() + i + 1,
							sUnit.length() - sBeforeDot.length() + i + 2);
					if ((sTmp.equals("亿")) || (sTmp.equals("万"))) {
						if (sRet.substring(sRet.length() - 1).equals("零")) {
							sRet = sRet.substring(0, sRet.length() - 1);
						}
						if (((sRet.substring(sRet.length() - 1).equals("亿"))
								|| (sRet.substring(sRet.length() - 1).equals("万"))) && (sTmp.equals("万"))) {
							continue;
						}
						sRet = sRet + sTmp;
					} else if (!sRet.substring(sRet.length() - 1).equals("零")) {
						sRet = sRet + "零";
					}
				}
			} else {
				sRet = sRet + sNumChar.substring(c - '0', c - '0' + 1);
				if (i < sBeforeDot.length() - 1) {
					sRet = sRet + sUnit.substring(sUnit.length() - sBeforeDot.length() + i + 1,
							sUnit.length() - sBeforeDot.length() + i + 2);
				}
			}
		}
		if ((sRet.length() > 2) && (sRet.substring(0, 2).equals(num10))) {
			sRet = sRet.substring(1);
		}
		if (sRet.substring(sRet.length() - 1).equals("零")) {
			sRet = sRet.substring(0, sRet.length() - 1);
		}
		if (sAfterDot.length() > 0) {
			sRet = sRet + "点";
			for (int i = 0; i < sAfterDot.length(); i++) {
				c = sAfterDot.charAt(i);
				sRet = sRet + sNumChar.substring(c - '0', c - '0' + 1);
			}
		}
		
		return sRet;
	}
	//==========数字转换==========//
	
}
