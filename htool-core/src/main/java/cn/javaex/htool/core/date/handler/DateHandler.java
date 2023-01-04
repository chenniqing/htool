package cn.javaex.htool.core.date.handler;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import cn.javaex.htool.core.date.constant.DatePattern;

/**
 * java.util.Date处理器
 *
 * @author 陈霓清
 * @Date 2022年11月24日
 */
public class DateHandler extends TimeHandler {

    public static final Map<String, Function<String, Date>> PARSE_MAP = new ConcurrentHashMap<>();
    public static final Map<String, Function<Date, String>> FORMAT_MAP = new ConcurrentHashMap<>();

    static {
        Field[] fields = DatePattern.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                String pattern = (String) field.get(DatePattern.class);

                PARSE_MAP.put(pattern, (text) -> {
                    try {
                        return new SimpleDateFormat(pattern).parse(text);
                    } catch (ParseException e) {

                    }
                    return null;
                });

                FORMAT_MAP.put(pattern, (date) -> {
                    return new SimpleDateFormat(pattern).format(date);
                });
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * String 转 java.util.Date
     *
     * @param text
     * @param pattern
     * @return
     * @throws ParseException
     */
    public Date parse(String text, String pattern) throws ParseException {
        Function<String, Date> func = PARSE_MAP.get(pattern);
        if (func != null) {
            return func.apply(text);
        }

        return new SimpleDateFormat(pattern).parse(text);
    }

    /**
     * java.util.Date 转 java.time.LocalDateTime
     *
     * @param date
     * @return
     */
    public LocalDateTime parseLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    /**
     * java.util.Date 转 java.time.LocalDate
     *
     * @param date
     * @return
     */
    public LocalDate parseLocalDate(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

}
