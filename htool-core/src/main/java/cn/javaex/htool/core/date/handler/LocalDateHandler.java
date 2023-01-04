package cn.javaex.htool.core.date.handler;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import cn.javaex.htool.core.date.constant.DatePattern;

/**
 * java.time.LocalDate处理器
 * 
 * @author 陈霓清
 * @Date 2022年11月24日
 */
public class LocalDateHandler extends TimeHandler {

	public static final Map<String, Function<String, LocalDate>> PARSE_MAP = new ConcurrentHashMap<>();
	public static final Map<String, Function<LocalDate, String>> FORMAT_MAP = new ConcurrentHashMap<>();
	
	static {
		Field[] fields = DatePattern.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				String pattern = (String) field.get(DatePattern.class);
				
				PARSE_MAP.put(pattern, (text) -> {
					return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
				});
				
				FORMAT_MAP.put(pattern, (localDate) -> {
					return localDate.format(DateTimeFormatter.ofPattern(pattern));
				});
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * String 转 java.time.LocalDate
	 * @param text
	 * @param pattern
	 * @return
	 */
	public LocalDate parse(String text, String pattern) {
		Function<String, LocalDate> func = PARSE_MAP.get(pattern);
		if (func != null) {
			return func.apply(text);
		}
		
		return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * java.time.LocalDate 转 java.util.Date
	 * @param localDate
	 * @return 
	 */
	public Date parseDate(LocalDate localDate) {
		Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
		return Date.from(instant);
	}
	
	/**
	 * java.time.LocalDate 转 java.time.LocalDateTime
	 * @param localDate
	 * @return 
	 */
	public LocalDateTime parseLocalDateTime(LocalDate localDate) {
		return localDate.atTime(LocalTime.MIN);
	}
	
}
