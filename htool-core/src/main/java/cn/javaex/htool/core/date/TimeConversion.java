package cn.javaex.htool.core.date;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import cn.javaex.htool.core.date.handler.DateHandler;
import cn.javaex.htool.core.date.handler.LocalDateHandler;
import cn.javaex.htool.core.date.handler.LocalDateTimeHandler;
import cn.javaex.htool.core.date.handler.TimeHandler;

/**
 * 日期时间转换
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class TimeConversion {
	
	/** 最小可识别的年份 */
	protected static final int MIN_YEAR = 1990;
	/** 生肖 */
	protected static final String[] ZODIACS = new String[] {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
	/** 星座分隔时间日 */
	protected static final int[] CONSTELLATION_DAYS = new int[] {20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
	/** 星座 */
	protected static final String[] CONSTELLATIONS = new String[] {"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
	
	/**
	 * String 转 java.util.Date
	 * @param text : 时间字符串
	 * @param pattern ： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String text, String pattern) throws ParseException {
		return new DateHandler().parse(text, pattern);
	}
	
	/**
	 * String 转 java.time.LocalDateTime
	 * @param text : 时间字符串
	 * @param pattern ： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static LocalDateTime parseLocalDateTime(String text, String pattern) {
		return new LocalDateTimeHandler().parse(text, pattern);
	}
	
	/**
	 * String 转 java.time.LocalDate
	 * @param text : 时间字符串
	 * @param pattern ： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static LocalDate parseLocalDate(String text, String pattern) {
		return new LocalDateHandler().parse(text, pattern);
	}
	
	/**
	 * java.util.Date 转 String
	 * @param date
	 * @param pattern ： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 * @throws ParseException
	 */
	public static String format(Date date, String pattern) throws ParseException {
		return new TimeHandler().format(date, pattern);
	}
	
	/**
	 * java.util.Date 转 java.time.LocalDateTime
	 * @param date
	 * @return 
	 */
	public static LocalDateTime parseLocalDateTime(Date date) {
		return new DateHandler().parseLocalDateTime(date);
	}
	
	/**
	 * java.util.Date 转 java.time.LocalDate
	 * @param date
	 * @return 
	 */
	public static LocalDate parseLocalDate(Date date) {
		return new DateHandler().parseLocalDate(date);
	}
	
	/**
	 * java.time.LocalDateTime 转 String
	 * @param localDateTime
	 * @param pattern ： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 * @throws ParseException
	 */
	public static String format(LocalDateTime localDateTime, String pattern) throws ParseException {
		return new TimeHandler().format(localDateTime, pattern);
	}
	
	/**
	 * java.time.LocalDateTime 转 java.util.Date
	 * @param localDateTime
	 * @return 
	 */
	public static Date parseDate(LocalDateTime localDateTime) {
		return new LocalDateTimeHandler().parseDate(localDateTime);
	}
	
	/**
	 * java.time.LocalDateTime 转 java.time.LocalDate
	 * @param localDateTime
	 * @return 
	 */
	public static LocalDate parseLocalDate(LocalDateTime localDateTime) {
		return new LocalDateTimeHandler().parseLocalDate(localDateTime);
	}
	
	/**
	 * java.time.LocalDate 转 String
	 * @param localDate
	 * @param pattern ： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 * @throws ParseException
	 */
	public static String format(LocalDate localDate, String pattern) throws ParseException {
		return new TimeHandler().format(localDate, pattern);
	}
	
	/**
	 * java.time.LocalDate 转 java.util.Date
	 * @param localDate
	 * @return 
	 */
	public static Date parseDate(LocalDate localDate) {
		return new LocalDateHandler().parseDate(localDate);
	}
	
	/**
	 * java.time.LocalDate 转 java.time.LocalDateTime
	 * @param localDate
	 * @return 
	 */
	public static LocalDateTime parseLocalDateTime(LocalDate localDate) {
		return new LocalDateHandler().parseLocalDateTime(localDate);
	}
	
}
