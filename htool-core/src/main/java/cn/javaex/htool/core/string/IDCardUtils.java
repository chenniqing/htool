package cn.javaex.htool.core.string;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.javaex.htool.core.date.DateUtils;
import cn.javaex.htool.core.date.constant.DatePattern;
import cn.javaex.htool.core.regular.CheckUtils;

/**
 * 身份证号工具类
 * 
 * @author 陈霓清
 * @Date 2023年1月2日
 */
public class IDCardUtils {
	
	/**
	 * 省市代码表
	 */
	private static final Map<String, String> PROVINCE_CODES = new HashMap<>();
	
	/**
	 * 台湾身份首字母对应数字
	 */
	private static final Map<Character, Integer> TW_FIRST_CODE = new HashMap<>();
	
	/**
	 * 每位加权因子
	 */
	private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	
	static {
		PROVINCE_CODES.put("11", "北京市");
		PROVINCE_CODES.put("12", "天津市");
		PROVINCE_CODES.put("13", "河北省");
		PROVINCE_CODES.put("14", "山西省");
		PROVINCE_CODES.put("15", "内蒙古自治区");
		PROVINCE_CODES.put("21", "辽宁省");
		PROVINCE_CODES.put("22", "吉林省");
		PROVINCE_CODES.put("23", "黑龙江省");
		PROVINCE_CODES.put("31", "上海市");
		PROVINCE_CODES.put("32", "江苏省");
		PROVINCE_CODES.put("33", "浙江省");
		PROVINCE_CODES.put("34", "安徽省");
		PROVINCE_CODES.put("35", "福建省");
		PROVINCE_CODES.put("36", "江西省");
		PROVINCE_CODES.put("37", "山东省");
		PROVINCE_CODES.put("41", "河南省");
		PROVINCE_CODES.put("42", "湖北省");
		PROVINCE_CODES.put("43", "湖南省");
		PROVINCE_CODES.put("44", "广东省");
		PROVINCE_CODES.put("45", "广西壮族自治区");
		PROVINCE_CODES.put("46", "海南省");
		PROVINCE_CODES.put("50", "重庆市");
		PROVINCE_CODES.put("51", "四川省");
		PROVINCE_CODES.put("52", "贵州省");
		PROVINCE_CODES.put("53", "云南省");
		PROVINCE_CODES.put("54", "西藏自治区");
		PROVINCE_CODES.put("61", "陕西省");
		PROVINCE_CODES.put("62", "甘肃省");
		PROVINCE_CODES.put("63", "青海省");
		PROVINCE_CODES.put("64", "宁夏回族自治区");
		PROVINCE_CODES.put("65", "新疆维吾尔自治区");
		PROVINCE_CODES.put("71", "台湾省");
		PROVINCE_CODES.put("81", "香港");
		PROVINCE_CODES.put("82", "澳门");
		PROVINCE_CODES.put("83", "台湾");
		PROVINCE_CODES.put("91", "国外");

		TW_FIRST_CODE.put('A', 10);
		TW_FIRST_CODE.put('B', 11);
		TW_FIRST_CODE.put('C', 12);
		TW_FIRST_CODE.put('D', 13);
		TW_FIRST_CODE.put('E', 14);
		TW_FIRST_CODE.put('F', 15);
		TW_FIRST_CODE.put('G', 16);
		TW_FIRST_CODE.put('H', 17);
		TW_FIRST_CODE.put('J', 18);
		TW_FIRST_CODE.put('K', 19);
		TW_FIRST_CODE.put('L', 20);
		TW_FIRST_CODE.put('M', 21);
		TW_FIRST_CODE.put('N', 22);
		TW_FIRST_CODE.put('P', 23);
		TW_FIRST_CODE.put('Q', 24);
		TW_FIRST_CODE.put('R', 25);
		TW_FIRST_CODE.put('S', 26);
		TW_FIRST_CODE.put('T', 27);
		TW_FIRST_CODE.put('U', 28);
		TW_FIRST_CODE.put('V', 29);
		TW_FIRST_CODE.put('X', 30);
		TW_FIRST_CODE.put('Y', 31);
		TW_FIRST_CODE.put('W', 32);
		TW_FIRST_CODE.put('Z', 33);
		TW_FIRST_CODE.put('I', 34);
		TW_FIRST_CODE.put('O', 35);
	}
	
	/**
	 * 验证是否为身份证号码（支持18位、15位和港澳台的10位）
	 * @param value
	 * @return
	 */
	public static boolean isIDCard(String idCard) {
		if (StringUtils.isBlank(idCard)) {
			return false;
		}

		int length = idCard.length();
		switch (length) {
			case 18:      // 18位身份证
				return isIDCard18(idCard);
			case 15:      // 15位身份证
				return isIDCard15(idCard);
			case 10: {    // 10位身份证，港澳台地区
				String[] cardVal = isIDCard10(idCard);
				return null != cardVal && "true".equals(cardVal[2]);
			}
			default:
				return false;
		}
	}
	
	/**
	 * 根据身份证号获取生日，只支持15或18位身份证号码
	 * @param idCard 身份证号
	 * @return 生日(yyyy-MM-dd)
	 */
	public static String getBirthday(String idCard) {
		if (!isIDCard(idCard)) {
			return "";
		}
		
		String birthday = "";
		int len = idCard.length();
		if (len == 18) {
			birthday = idCard.substring(6, 14);
		} else {
			birthday = "19" + idCard.substring(6, 12);
		}
		
		return birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6, 8);
	}
	
	/**
	 * 根据身份证号获取年龄
	 * 只支持15或18位身份证号码
	 * @param idCard 身份证号
	 * @return
	 */
	public static int getAge(String idCard) {
		if (!isIDCard(idCard)) {
			return 0;
		}
		
		String birthday = "";
		int len = idCard.length();
		if (len == 18) {
			birthday = idCard.substring(6, 14);
		} else {
			birthday = "19" + idCard.substring(6, 12);
		}
		
		try {
			return DateUtils.age(birthday, DatePattern.yyyyMMdd.replace("-", ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 根据身份证号获取性别
	 * 只支持15或18位身份证号码
	 * @param idCard 身份证号
	 * @return 性别(1 : 男 ， 0 : 女)
	 */
	public static int getGender(String idCard) {
		if (!isIDCard(idCard)) {
			throw new IllegalArgumentException("Invalid IDCard");
		}
		
		int len = idCard.length();
		if (len == 15) {
			idCard = convert15To18(idCard);
		}
		
		char sCardChar = Objects.requireNonNull(idCard).charAt(16);
		return (sCardChar % 2 != 0) ? 1 : 0;
	}
	
	/**
	 * 将15位身份证号码转换为18位
	 * @param idCard 15位身份证号
	 * @return 18位身份证号
	 */
	public static String convert15To18(String idCard) {
		if (!isIDCard(idCard)) {
			throw new IllegalArgumentException("Invalid IDCard");
		}
		if (idCard.length() != 15) {
			return null;
		}
		
		String birthday = "19" + idCard.substring(6, 12);
		
		StringBuffer idCard18 = new StringBuffer();
		idCard18.append(idCard, 0, 6).append(birthday.substring(0, 4)).append(idCard.substring(8));
		
		// 获取校验位
		char val = getCheckCode18(idCard18.toString());
		idCard18.append(val);
		
		return idCard18.toString();
	}

	/**
	 * 根据身份证号获取户籍省份编码
	 * 只支持15或18位身份证号码
	 * @param idCard 身份证码
	 * @return 省份编码
	 */
	public static String getProvinceCode(String idCard) {
		int len = idCard.length();
		
		if (len == 15 || len == 18) {
			return idCard.substring(0, 2);
		}
		
		return null;
	}
	
	/**
	 * 根据身份编号获取户籍省份，只支持15或18位身份证号码
	 *
	 * @param idCard 身份证号
	 * @return 省份名称
	 */
	public static String getProvince(String idCard) {
		String code = getProvinceCode(idCard);
		
		if (StringUtils.isNotBlank(code)) {
			return PROVINCE_CODES.get(code);
		}
		
		return null;
	}
	
	/**
	 * 根据身份编号获取地市级编码，只支持15或18位身份证号码
	 * 获取编码为4位
	 * @param idCard 身份证号
	 * @return 地市级编码
	 */
	public static String getCityCode(String idCard) {
		int len = idCard.length();
		
		if (len == 15 || len == 18) {
			return idCard.substring(0, 4);
		}
		
		return null;
	}

	/**
	 * 根据身份编号获取区县级编码，只支持15或18位身份证号码
	 * 获取编码为6位
	 * @param idCard 身份证号
	 * @return 地市级编码
	 */
	public static String getDistrictCode(String idCard) {
		int len = idCard.length();
		
		if (len == 15 || len == 18) {
			return idCard.substring(0, 6);
		}
		
		return null;
	}
	
	/**
	 * <p>
	 * 判断18位身份证的合法性
	 * </p>
	 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。<br>
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * <p>
	 * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
	 * </p>
	 * <ol>
	 * <li>第1、2位数字表示：所在省份的代码</li>
	 * <li>第3、4位数字表示：所在城市的代码</li>
	 * <li>第5、6位数字表示：所在区县的代码</li>
	 * <li>第7~14位数字表示：出生年、月、日</li>
	 * <li>第15、16位数字表示：所在地的派出所的代码</li>
	 * <li>第17位数字表示性别：奇数表示男性，偶数表示女性</li>
	 * <li>第18位数字是校检码，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示</li>
	 * </ol>
	 * <p>
	 * 第十八位数字(校验码)的计算方法为：
	 * <ol>
	 * <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
	 * <li>将这17位数字和系数相乘的结果相加</li>
	 * <li>用加出来和除以11，看余数是多少</li>
	 * <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2</li>
	 * <li>通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
	 * </ol>
	 *
	 * @param idCard     待验证的身份证
	 * @param ignoreCase 是否忽略大小写。{@code true}则忽略X大小写，否则严格匹配大写。
	 * @return 是否有效的18位身份证
	 */
	private static boolean isIDCard18(String idCard) {
		// 省份
		final String proCode = idCard.substring(0, 2);
		if (null == PROVINCE_CODES.get(proCode)) {
			return false;
		}

		// 校验生日
		String birthday = idCard.substring(6, 14);
		int year = Integer.parseInt(birthday.substring(0, 4));
		int month = Integer.parseInt(birthday.substring(4, 6));
		int day = Integer.parseInt(birthday.substring(6, 8));
		if (!isBirthday(year, month, day)) {
			return false;
		}

		// 前17位
		final String code17 = idCard.substring(0, 17);
		if (CheckUtils.isNumber(code17)) {
			// 获取校验位
			char val = getCheckCode18(code17);
			// 第18位
			return Character.toLowerCase(val) == Character.toLowerCase(idCard.charAt(17));
		}
		return false;
	}
	
	/**
	 * 验证15位身份证号是否合法
	 * @param idCard
	 * @return
	 */
	private static boolean isIDCard15(String idCard) {
		if (CheckUtils.isNumber(idCard)) {
			// 省份
			String proCode = idCard.substring(0, 2);
			if (null == PROVINCE_CODES.get(proCode)) {
				return false;
			}

			// 校验生日（两位年份，补充为19XX）
			String birthday = "19" + idCard.substring(6, 12);
			int year = Integer.parseInt(birthday.substring(0, 4));
			int month = Integer.parseInt(birthday.substring(4, 6));
			int day = Integer.parseInt(birthday.substring(6, 8));
			
			return isBirthday(year, month, day);
		}
		
		return false;
	}

	/**
	 * 验证10位身份证号是否合法
	 *
	 * @param idCard 身份证号
	 * @return 身份证信息数组
	 * <p>
	 * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
	 * </p>
	 */
	private static String[] isIDCard10(String idCard) {
		String[] info = new String[3];
		String card = idCard.replaceAll("[()]", "");
		if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
			return null;
		}
		
		// 台湾
		if (idCard.matches("^[a-zA-Z][0-9]{9}$")) {
			info[0] = "台湾";
			char char2 = idCard.charAt(1);
			if ('1' == char2) {
				info[1] = "M";
			} else if ('2' == char2) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = isTWIDCard(idCard) ? "true" : "false";
		}
		// 澳门
		else if (idCard.matches("^[157][0-9]{6}\\(?[0-9A-Z]\\)?$")) {
			info[0] = "澳门";
			info[1] = "N";
			info[2] = "true";
		}
		// 香港
		else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) {
			info[0] = "香港";
			info[1] = "N";
			info[2] = isHKIDCard(idCard) ? "true" : "false";
		}
		else {
			return null;
		}
		
		return info;
	}
	
	/**
	 * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
	 * <p>
	 * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
	 * </p>
	 * <p>
	 * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
	 * </p>
	 *
	 * @param idCard 身份证号码
	 * @return 验证码是否符合
	 */
	private static boolean isHKIDCard(String idCard) {
		String card = idCard.replaceAll("[()]", "");
		int sum;
		if (card.length() == 9) {
			sum = (Character.toUpperCase(card.charAt(0)) - 55) * 9 + (Character.toUpperCase(card.charAt(1)) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Character.toUpperCase(card.charAt(0)) - 55) * 8;
		}

		// 首字母A-Z，A表示1，以此类推
		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		int iflag = 7;
		for (char c : chars) {
			sum = sum + Integer.parseInt(String.valueOf(c)) * iflag;
			iflag--;
		}
		
		if ("A".equalsIgnoreCase(end)) {
			sum += 10;
		} else {
			sum += Integer.parseInt(end);
		}
		
		return sum % 11 == 0;
	}

	/**
	 * 验证台湾身份证号码
	 * @param idCard
	 * @return
	 */
	private static boolean isTWIDCard(String idCard) {
		Integer iStart = TW_FIRST_CODE.get(idCard.charAt(0));
		if (null == iStart) {
			return false;
		}
		int sum = iStart / 10 + (iStart % 10) * 9;

		String mid = idCard.substring(1, 9);
		char[] chars = mid.toCharArray();
		int iflag = 8;
		for (char c : chars) {
			sum += Integer.parseInt(String.valueOf(c)) * iflag;
			iflag--;
		}

		String end = idCard.substring(9, 10);
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.parseInt(end);
	}

	/**
	 * 获得18位身份证校验码
	 * @param code17 18位身份证号中的前17位
	 * @return 第18位
	 */
	private static char getCheckCode18(String code17) {
		int sum = getPowerSum(code17.toCharArray());
		return getCheckCode18(sum);
	}
	
	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 * @param iArr 身份证号码的数组
	 * @return 身份证编码
	 */
	private static int getPowerSum(char[] iArr) {
		int sum = 0;
		if (POWER.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				sum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
			}
		}
		return sum;
	}
	
	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 * @param sum 加权和
	 * @return 校验位
	 */
	private static char getCheckCode18(int sum) {
		switch (sum % 11) {
			case 10:
				return '2';
			case 9:
				return '3';
			case 8:
				return '4';
			case 7:
				return '5';
			case 6:
				return '6';
			case 5:
				return '7';
			case 4:
				return '8';
			case 3:
				return '9';
			case 2:
				return 'X';
			case 1:
				return '0';
			case 0:
				return '1';
			default:
				return ' ';
		}
	}
	
	/**
	 * 验证是否为生日
	 * @param year  年，从1900年开始计算
	 * @param month 月，从1开始计数
	 * @param day   日，从1开始计数
	 * @return 是否为生日
	 */
	private static boolean isBirthday(int year, int month, int day) {
		// 验证年
		int thisYear = DateUtils.getYear();
		if (year < 1900 || year > thisYear) {
			return false;
		}

		// 验证月
		if (month < 1 || month > 12) {
			return false;
		}

		// 验证日
		if (day < 1 || day > 31) {
			return false;
		}
		// 检查几个特殊月的最大天数
		if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
			return false;
		}
		// 2月，非闰年最大28，闰年最大29
		if (month == 2) {
			return day < 29 || (day == 29 && DateUtils.isLeapYear(year));
		}
		
		return true;
	}

}
