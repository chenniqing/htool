package cn.javaex.htool.core.date;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import cn.javaex.htool.core.date.constant.DatePattern;
import cn.javaex.htool.core.date.enums.TimeUnit;
import cn.javaex.htool.core.date.handler.TimeHandler;

/**
 * 日期时间工具类
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class DateUtils extends TimeConversion {
	
	/**
	 * 获取当天日期
	 */
	public static String today() {
		try {
			return format(LocalDate.now(), DatePattern.yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取当天开始时间
	 */
	public static String beginOfToday() {
		try {
			return format(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), DatePattern.yyyyMMddHHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取当天结束时间
	 */
	public static String endOfToday() {
		try {
			return format(LocalDateTime.of(LocalDate.now(), LocalTime.MAX), DatePattern.yyyyMMddHHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取指定某一天的开始时间
	 * @param <T>
	 * @param time : 支持 Date、LocalDateTime、LocalDate
	 * @return
	 * @throws ParseException
	 */
	public static <T> String beginOfDay(T time) throws ParseException {
		String timestr = new TimeHandler().format(time, DatePattern.yyyyMMdd);
		LocalDate localDate = parseLocalDate(timestr, DatePattern.yyyyMMdd);
		return format(LocalDateTime.of(localDate, LocalTime.MIN), DatePattern.yyyyMMddHHmmss);
	}
	
	/**
	 * 获取指定某一天的结束时间
	 * @param <T>
	 * @param time : 支持 Date、LocalDateTime、LocalDate
	 * @return
	 * @throws ParseException
	 */
	public static <T> String endOfDay(T time) throws ParseException {
		String timestr = new TimeHandler().format(time, DatePattern.yyyyMMdd);
		LocalDate localDate = parseLocalDate(timestr, DatePattern.yyyyMMdd);
		return format(LocalDateTime.of(localDate, LocalTime.MAX), DatePattern.yyyyMMddHHmmss);
	}
	
	/**
	 * 获取本月第一天
	 */
	public static String firstDayOfMonth() {
		LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
		try {
			return format(firstDayOfMonth, DatePattern.yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 获取本月第一天的开始时间
	 */
	public static String beginOfMonth() {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
		try {
			return format(localDateTime, DatePattern.yyyyMMddHHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取本月第N天
	 * @param n
	 * @return
	 * @throws ParseException 
	 */
	public static String dayOfMonth(int n) throws ParseException {
		return format(LocalDate.now().withDayOfMonth(n), DatePattern.yyyyMMdd);
	}

	/**
	 * 获取本月最后一天
	 */
	public static String lastDayOfMonth() {
		try {
			return format(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()), DatePattern.yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 获取本月最后一天的结束时间
	 */
	public static String endOfMonth() {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
		try {
			return format(localDateTime, DatePattern.yyyyMMddHHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取当前年份
	 */
	public static int getYear() {
		return LocalDateTime.now().getYear();
	}
	
	/**
	 * 获取指定时间的年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		return parseLocalDateTime(date).getYear();
	}
	
	/**
	 * 获取当前月份，从1开始计算
	 */
	public static int getMonth() {
		return LocalDateTime.now().getMonthValue();
	}
	
	/**
	 * 获取指定时间的月份，从1开始计算
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		return parseLocalDateTime(date).getMonthValue();
	}
	
	/**
	 * 获取当前时间属于今年的哪一天，从1开始计算
	 */
	public static int getDayOfYear() {
		return LocalDateTime.now().getDayOfYear();
	}
	
	/**
	 * 获取指定时间属于该年的哪一天，从1开始计算
	 * @param date
	 * @return
	 */
	public static int getDayOfYear(Date date) {
		return parseLocalDateTime(date).getDayOfYear();
	}
	
	/**
	 * 获取当前时间属于当月的哪一天，从1开始计算
	 */
	public static int getDayOfMonth() {
		return LocalDateTime.now().getDayOfMonth();
	}
	
	/**
	 * 获取指定时间属于该月的哪一天，从1开始计算
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		return parseLocalDateTime(date).getDayOfMonth();
	}
	
	/**
	 * 获取当前时间属于本周的哪一天，从1开始计算
	 */
	public static int getDayOfWeek() {
		return LocalDateTime.now().getDayOfWeek().getValue();
	}
	
	/**
	 * 获取指定时间属于该周的哪一天，从1开始计算
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		return parseLocalDateTime(date).getDayOfWeek().getValue();
	}
	
	/**
	 * 获取当前小时
	 */
	public static int getHour() {
		return LocalDateTime.now().getHour();
	}
	
	/**
	 * 获取指定时间的小时
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		return parseLocalDateTime(date).getHour();
	}
	
	/**
	 * 获取当前分钟
	 */
	public static int getMinute() {
		return LocalDateTime.now().getMinute();
	}
	
	/**
	 * 获取指定时间的分钟
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date) {
		return parseLocalDateTime(date).getMinute();
	}
	
	/**
	 * 获取当前秒数
	 */
	public static int getSecond() {
		return LocalDateTime.now().getSecond();
	}
	
	/**
	 * 获取指定时间的秒数
	 * @param date
	 * @return
	 */
	public static int getSecond(Date date) {
		return parseLocalDateTime(date).getSecond();
	}
	
	/**
	 * 计算2个时间间隔
	 * @param <T>
	 * @param time1 : 第1个时间，支持 Date、LocalDateTime、LocalDate
	 * @param time2 : 第2个时间，支持 Date、LocalDateTime、LocalDate
	 * @param accuracy : 返回精度，支持 year、month、day、hour、minute、second
	 * @return
	 * @throws ParseException 
	 */
	public static <T> long timeDifference(T time1, T time2, String accuracy) throws ParseException {
		if (time1==null || time2==null || accuracy==null) {
			throw new RuntimeException("Parameter cannot be null");
		}
		
		TimeUnit timeUnit = TimeUnit.find(accuracy.toLowerCase());
		
		switch (timeUnit) {
			case YEAR:
				return new TimeHandler().yearDifference(time1, time2);
			case MONTH:
				return new TimeHandler().monthDifference(time1, time2);
			case DAY:
				return new TimeHandler().dayDifference(time1, time2);
			case HOUR:
				return new TimeHandler().hourDifference(time1, time2);
			case MINUTE:
				return new TimeHandler().minuteDifference(time1, time2);
			case SECOND:
				return new TimeHandler().secondDifference(time1, time2);
			default:
				throw new RuntimeException("无法处理" + accuracy + "类型的计算");
		}
	}
	
	/**
	 * 两个时间比较大小
	 * @param <T>
	 * @param time1 : 第1个时间，支持 Date、LocalDateTime、LocalDate
	 * @param time2 : 第2个时间，支持 Date、LocalDateTime、LocalDate
	 * @return 返回 0 表示[等于]，返回 1 表示[大于]，返回 -1 表示[小于]
	 * @throws ParseException 
	 */
	public static <T> int compare(T time1, T time2) throws ParseException {
		String timestr1 = new TimeHandler().format(time1, DatePattern.yyyyMMddHHmmss);
		String timestr2 = new TimeHandler().format(time2, DatePattern.yyyyMMddHHmmss);
		
		LocalDateTime localDateTime1 = parseLocalDateTime(timestr1, DatePattern.yyyyMMddHHmmss);
		LocalDateTime localDateTime2 = parseLocalDateTime(timestr2, DatePattern.yyyyMMddHHmmss);
		
		if (localDateTime1.equals(localDateTime2)) {
			return 0;
		}
		
		return localDateTime1.isBefore(localDateTime2) ? -1 : 1;
	}
	
	/**
	 * 添加指定年份数
	 * @param date
	 * @param years
	 * @return
	 */
	public static Date plusYears(Date date, long years) {
		return parseDate(parseLocalDateTime(date).plusYears(years));
	}
	
	/**
	 * 添加指定月数
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date plusMonths(Date date, long months) {
		return parseDate(parseLocalDateTime(date).plusMonths(months));
	}
	
	/**
	 * 添加指定周数
	 * @param date
	 * @param weeks
	 * @return
	 */
	public static Date plusWeeks(Date date, long weeks) {
		return parseDate(parseLocalDateTime(date).plusWeeks(weeks));
	}
	
	/**
	 * 添加指定天数
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date plusDays(Date date, long days) {
		return parseDate(parseLocalDateTime(date).plusDays(days));
	}
	
	/**
	 * 添加指定小时数
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date plusHours(Date date, long hours) {
		return parseDate(parseLocalDateTime(date).plusHours(hours));
	}
	
	/**
	 * 添加指定分钟数
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date plusMinutes(Date date, long minutes) {
		return parseDate(parseLocalDateTime(date).plusMinutes(minutes));
	}
	
	/**
	 * 添加指定秒数
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date plusSeconds(Date date, long seconds) {
		return parseDate(parseLocalDateTime(date).plusSeconds(seconds));
	}
	
	/**
	 * 减少指定年份数
	 * @param date
	 * @param years
	 * @return
	 */
	public static Date minusYears(Date date, long years) {
		return parseDate(parseLocalDateTime(date).minusYears(years));
	}
	
	/**
	 * 减少指定月数
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date minusMonths(Date date, long months) {
		return parseDate(parseLocalDateTime(date).minusMonths(months));
	}
	
	/**
	 * 减少指定周数
	 * @param date
	 * @param weeks
	 * @return
	 */
	public static Date minusWeeks(Date date, long weeks) {
		return parseDate(parseLocalDateTime(date).minusWeeks(weeks));
	}
	
	/**
	 * 减少指定天数
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date minusDays(Date date, long days) {
		return parseDate(parseLocalDateTime(date).minusDays(days));
	}
	
	/**
	 * 减少指定小时数
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date minusHours(Date date, long hours) {
		return parseDate(parseLocalDateTime(date).minusHours(hours));
	}
	
	/**
	 * 减少指定分钟数
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date minusMinutes(Date date, long minutes) {
		return parseDate(parseLocalDateTime(date).minusMinutes(minutes));
	}
	
	/**
	 * 减少指定秒数
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date minusSeconds(Date date, long seconds) {
		return parseDate(parseLocalDateTime(date).minusSeconds(seconds));
	}
	
	/**
	 * 判断指定年份是否是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return Year.isLeap(year);
	}
	
	/**
	 * 计算年龄
	 * @param time
	 * @param pattern： 时间格式字符串，例如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 * @throws ParseException 
	 */
	public static int age(String time, String pattern) throws ParseException {
		LocalDate time1 = DateUtils.parseLocalDate(time, pattern);
		LocalDate time2 = LocalDate.now();
		
		return (int) DateUtils.timeDifference(time1, time2, TimeUnit.YEAR.getValue());
	}
	
	/**
	 * 计算年龄
	 * @param <T>
	 * @param time : 支持 Date、LocalDateTime、LocalDate
	 * @return
	 * @throws ParseException
	 */
	public static <T> int age(T time) throws ParseException {
		String timestr1 = new TimeHandler().format(time, DatePattern.yyyyMMdd);
		
		LocalDate time1 = parseLocalDate(timestr1, DatePattern.yyyyMMdd);
		LocalDate time2 = LocalDate.now();
		
		return (int) DateUtils.timeDifference(time1, time2, TimeUnit.YEAR.getValue());
	}
	
	/**
	 * 计算生肖，只计算1900年后出生的人
	 * @param year
	 * @return
	 */
	public static String getZodiac(int year) {
		if (year < MIN_YEAR) {
			return "未知";
		}
		
		return ZODIACS[(year - 1900) % ZODIACS.length];
	}
	
	/**
	 * 计算星座
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		return day < CONSTELLATION_DAYS[month - 1] ? CONSTELLATIONS[month - 1] : CONSTELLATIONS[month];
	}
}
