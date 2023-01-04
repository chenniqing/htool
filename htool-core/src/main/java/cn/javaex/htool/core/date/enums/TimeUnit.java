package cn.javaex.htool.core.date.enums;

import java.util.Arrays;

/**
 * 时间单位
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public enum TimeUnit {
	/** 年 */
	YEAR("year"),
	/** 月 */
	MONTH("month"),
	/** 日 */
	DAY("day"),
	/** 时 */
	HOUR("hour"),
	/** 分 */
	MINUTE("minute"),
	/** 秒 */
	SECOND("second");

	TimeUnit(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	
	public static TimeUnit find(String value) {
		return Arrays.stream(TimeUnit.values()).filter(timeUnit -> timeUnit.value == value).findAny()
				.orElseThrow(() -> new RuntimeException("TimeUnit must be one of year, month, day, hour, minute and second"));
	}

	public static boolean has(String value) {
		return Arrays.stream(TimeUnit.values()).anyMatch(timeUnit -> timeUnit.value.equals(value));
	}
	
}