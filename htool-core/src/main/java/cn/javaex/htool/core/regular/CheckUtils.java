package cn.javaex.htool.core.regular;

import java.util.regex.Pattern;

import cn.javaex.htool.core.string.IDCardUtils;

/**
 * 字符串校验工具类
 * 
 * @author 陈霓清
 * @Date 2023年1月2日
 */
public class CheckUtils {
	
	/**
	 * 给定内容是否匹配正则
	 * @param pattern 模式
	 * @param content 内容
	 * @return 正则为null或者""时不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(Pattern pattern, CharSequence content) {
		if (content==null || pattern==null) {
			return false;
		}
		return pattern.matcher(content).matches();
	}
	
	/**
	 * 验证是否为（中国大陆）手机号码
	 * 中国大陆： +86  133 9511 1345，2位区域码标示+11位数字
	 * @param value
	 * @return
	 */
	public static boolean isMobile(String value) {
		String regex = "(?:0|86|\\+86)?1[3-9]\\d{9}";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为（中国香港）手机号码
	 * 中国香港： +852 5100 4870， 三位区域码+10位数字, 中国香港手机号码8位数
	 * @param value
	 * @return
	 */
	public static boolean isMobileHK(String value) {
		String regex = "(?:0|852|\\+852)?\\d{8}";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为（中国台湾）手机号码
	 * 中国台湾： +886 09 60 000000， 三位区域码+号码以数字09开头 + 8位数字, 中国台湾手机号码10位数
	 * @param value
	 * @return
	 */
	public static boolean isMobileTW(String value) {
		String regex = "(?:0|886|\\+886)?(?:|-)09\\d{8}";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为（中国澳门）手机号码
	 * 中国澳门： +853 68 00000， 三位区域码 +号码以数字6开头 + 7位数字, 中国澳门手机号码8位数
	 * @param value
	 * @return
	 */
	public static boolean isMobileMO(String value) {
		String regex = "(?:0|853|\\+853)?(?:|-)6\\d{7}";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为座机号码（中国）
	 * @param value
	 * @return
	 */
	public static boolean isTel(String value) {
		String regex = "(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为座机服务号码（中国+400 +800）
	 * @param value
	 * @return
	 */
	public static boolean isTel400800(String value) {
		String regex = "0\\d{2,3}[\\- ]?[1-9]\\d{6,7}|[48]00[\\- ]?[1-9]\\d{6}";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为邮箱
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String value) {
		String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为邮政编码
	 * @param value
	 * @return
	 */
	public static boolean isPostCode(String value) {
		String regex = "^(0[1-7]|1[0-356]|2[0-7]|3[0-6]|4[0-7]|5[0-7]|6[0-7]|7[0-5]|8[0-9]|9[0-8])\\d{4}|99907[78]$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为IPV4地址
	 * @param value
	 * @return
	 */
	public static boolean isIpv4(String value) {
		String regex = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为IPV6地址
	 * @param value
	 * @return
	 */
	public static boolean isIpv6(String value) {
		String regex = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为MAC地址
	 * @param value
	 * @return
	 */
	public static boolean isMac(String value) {
		String regex = "((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为中国车牌号（兼容新能源车牌）
	 * @param value
	 * @return
	 */
	public static boolean isCarNumber(String value) {
		String regex = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为车架号
	 * 行驶证编号 车辆识别代号 车辆识别码
	 * @param value
	 * @return
	 */
	public static boolean isCarVin(String value) {
		String regex = "^[A-HJ-NPR-Z0-9]{8}[0-9X][A-HJ-NPR-Z0-9]{2}\\d{6}$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为驾驶证  别名：驾驶证档案编号、行驶证编号
	 * @param value
	 * @return
	 */
	public static boolean isCarDrivingLicence(String value) {
		String regex = "^[0-9]{12}$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为身份证号码（支持18位、15位和港澳台的10位）
	 * @param value
	 * @return
	 */
	public static boolean isIDCard(String value) {
		return IDCardUtils.isIDCard(value);
	}
	
	/**
	 * 验证是否为统一社会信用代码
	 * @param value
	 * @return
	 */
	public static boolean isCreditCode(String value) {
		String regex = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为纯汉字
	 * @param value
	 * @return
	 */
	public static boolean isChinese(String value) {
		/**
		 * 单个中文汉字<br>
		 * 参照维基百科汉字Unicode范围(https://zh.wikipedia.org/wiki/%E6%B1%89%E5%AD%97 页面右侧)
		 */
		String chinese = "[\u2E80-\u2EFF\u2F00-\u2FDF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF\uD840\uDC00-\uD869\uDEDF\uD869\uDF00-\uD86D\uDF3F\uD86D\uDF40-\uD86E\uDC1F\uD86E\uDC20-\uD873\uDEAF\uD87E\uDC00-\uD87E\uDE1F]";
		/**
		 * 中文汉字
		 */
		String chineses = chinese + "+";
		
		Pattern pattern = Pattern.compile(chineses);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为纯字母
	 * @param value
	 * @return
	 */
	public static boolean isWord(String value) {
		String regex = "[a-zA-Z]+";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为Hex（16进制）字符串
	 * @param value
	 * @return
	 */
	public static boolean isHex(String value) {
		String regex = "^[a-fA-F0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为纯数字
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value) {
		if (value != null && value.trim().length() > 0) {
			value = value.trim();
			value = (value.charAt(0) == '-' ? value.substring(1) : value);
			String regex = "^\\d+$";
			Pattern pattern = Pattern.compile(regex);
			return isMatch(pattern, value);
		}
		
		return false;
	}
	
	/**
	 * 验证是否为价格
	 * @param value
	 * @return
	 */
	public static boolean isMoney(String value) {
		String regex = "^(\\d+(?:\\.\\d+)?)$";
		Pattern pattern = Pattern.compile(regex);
		if (!isMatch(pattern, value)) {
			return false;
		}
		
		// 判断2位小数位
		if (value.indexOf(".") > 0) {
			String decimal = value.split("\\.")[1];
			if (decimal.length() > 2) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 验证是否为整数
	 * @param value
	 * @return
	 */
	public static boolean isInt(String value) {
		String regex = "^-?\\d+$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为正整数
	 * @param value
	 * @return
	 */
	public static boolean isPositiveInt(String value) {
		String regex = "^[0-9]*[1-9][0-9]*$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为负整数
	 * @param value
	 * @return
	 */
	public static boolean isNegativeInt(String value) {
		String regex = "^-[0-9]*[1-9][0-9]*$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为非负整数：正整数 + 0
	 * @param value
	 * @return
	 */
	public static boolean isNonnegativeInt(String value) {
		String regex = "^\\d+$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为非正整数：负整数 + 0
	 * @param value
	 * @return
	 */
	public static boolean isNonpositiveInt(String value) {
		String regex = "^((-\\d+)|(0+))$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为正数
	 * @param value
	 * @return
	 */
	public static boolean isPositiveDecimal(String value) {
		String regex = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为负数
	 * @param value
	 * @return
	 */
	public static boolean isNegativeDecimal(String value) {
		String regex = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为非负数：0 + 正整数 + 正小数
	 * @param value
	 * @return
	 */
	public static boolean isNonnegativeDecimal(String value) {
		String regex = "^\\d+(\\.\\d+)?$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为非正数：0 + 负整数 + 负小数
	 * @param value
	 * @return
	 */
	public static boolean isNonpositiveDecimal(String value) {
		String regex = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为账号：只能输入5-10个以字母开头，可带数字、下划线的字符串
	 * @param value
	 * @return
	 */
	public static boolean isAccount(String value) {
		String regex = "^[a-zA-Z]{1}([a-zA-Z0-9_]){5,10}$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为昵称：长度限制为2-10个字，只允许中文、字母、数字和下划线
	 * @param value
	 * @return
	 */
	public static boolean isNickname(String value) {
		String regex = "^[a-zA-Z0-9_\u4e00-\u9fa5]{2,10}$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
	/**
	 * 验证是否为密码：6到16位，允许有字母、数字和一些特殊字符
	 * @param value
	 * @return
	 */
	public static boolean isPassword(String value) {
		String regex = "^[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{6,16}$";
		Pattern pattern = Pattern.compile(regex);
		return isMatch(pattern, value);
	}
	
}
