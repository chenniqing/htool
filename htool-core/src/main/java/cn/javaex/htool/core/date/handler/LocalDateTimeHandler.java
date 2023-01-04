package cn.javaex.htool.core.date.handler;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import cn.javaex.htool.core.date.constant.DatePattern;

/**
 * java.time.LocalDateTime处理器
 * 
 * @author 陈霓清
 * @Date 2022年11月24日
 */
public class LocalDateTimeHandler extends TimeHandler {

	public static final Map<String, Function<String, LocalDateTime>> PARSE_MAP = new ConcurrentHashMap<>();
	public static final Map<String, Function<LocalDateTime, String>> FORMAT_MAP = new ConcurrentHashMap<>();
	
	static {
		Field[] fields = DatePattern.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				String pattern = (String) field.get(DatePattern.class);
				
				PARSE_MAP.put(pattern, (text) -> {
					return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
				});
				
				FORMAT_MAP.put(pattern, (localDateTime) -> {
					return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
				});
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * String 转 java.time.LocalDateTime
	 * @param text
	 * @param pattern
	 * @return
	 */
	public LocalDateTime parse(String text, String pattern) {
		Function<String, LocalDateTime> func = PARSE_MAP.get(pattern);
		if (func != null) {
			return func.apply(text);
		}
		
		return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * java.time.LocalDateTime 转 java.util.Date
	 * @param localDateTime
	 * @return 
	 */
	public Date parseDate(LocalDateTime localDateTime) {
		Instant instant = localDateTime.atZone(ZONE).toInstant();
		return Date.from(instant);
	}
	
	/**
	 * java.time.LocalDateTime 转 java.time.LocalDate
	 * @param localDateTime
	 * @return 
	 */
	public LocalDate parseLocalDate(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate();
	}
	
}
