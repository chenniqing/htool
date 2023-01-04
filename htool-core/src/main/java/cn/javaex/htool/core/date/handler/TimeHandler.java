package cn.javaex.htool.core.date.handler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

import cn.javaex.htool.core.date.DateUtils;
import cn.javaex.htool.core.date.constant.DatePattern;

/**
 * 时间处理器
 *
 * @param
 * @author 陈霓清
 * @Date 2022年11月24日
 */
public class TimeHandler {

    protected static final ZoneId ZONE = ZoneId.systemDefault();

    /**
     * 时间类型 转 String
     *
     * @param <T>
     * @param <T>
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public <T> String format(T date, String pattern) throws ParseException {
        if (date instanceof Date) {
            Function<Date, String> func = DateHandler.FORMAT_MAP.get(pattern);
            if (func != null) {
                return func.apply((Date) date);
            }

            return new SimpleDateFormat(pattern).format(date);
        } else if (date instanceof LocalDateTime) {
            Function<LocalDateTime, String> func = LocalDateTimeHandler.FORMAT_MAP.get(pattern);
            if (func != null) {
                return func.apply((LocalDateTime) date);
            }

            return ((DateFormat) date).format(DateTimeFormatter.ofPattern(pattern));
        } else if (date instanceof LocalDate) {
            Function<LocalDate, String> func = LocalDateHandler.FORMAT_MAP.get(pattern);
            if (func != null) {
                return func.apply((LocalDate) date);
            }

            return ((DateFormat) date).format(DateTimeFormatter.ofPattern(pattern));
        }

        return null;
    }

    /**
     * 计算2个时间间隔多少年
     *
     * @param <T>
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public <T> long yearDifference(T time1, T time2) throws ParseException {
        String timestr1 = this.format(time1, DatePattern.yyyyMMdd);
        String timestr2 = this.format(time2, DatePattern.yyyyMMdd);

        LocalDate localDate1 = DateUtils.parseLocalDate(timestr1, DatePattern.yyyyMMdd);
        LocalDate localDate2 = DateUtils.parseLocalDate(timestr2, DatePattern.yyyyMMdd);

        return Period.between(localDate1, localDate2).getYears();
    }

    /**
     * 计算2个时间间隔多少月
     *
     * @param <T>
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public <T> long monthDifference(T time1, T time2) throws ParseException {
        String timestr1 = this.format(time1, DatePattern.yyyyMMdd);
        String timestr2 = this.format(time2, DatePattern.yyyyMMdd);

        LocalDate localDate1 = DateUtils.parseLocalDate(timestr1, DatePattern.yyyyMMdd);
        LocalDate localDate2 = DateUtils.parseLocalDate(timestr2, DatePattern.yyyyMMdd);

        return Period.between(localDate1, localDate2).getYears() * 12 + Period.between(localDate1, localDate2).getMonths();
    }

    /**
     * 计算2个时间间隔多少天
     *
     * @param <T>
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public <T> long dayDifference(T time1, T time2) throws ParseException {
        String timestr1 = this.format(time1, DatePattern.yyyyMMddHHmmss);
        String timestr2 = this.format(time2, DatePattern.yyyyMMddHHmmss);

        LocalDateTime localDateTime1 = DateUtils.parseLocalDateTime(timestr1, DatePattern.yyyyMMddHHmmss);
        LocalDateTime localDateTime2 = DateUtils.parseLocalDateTime(timestr2, DatePattern.yyyyMMddHHmmss);

        return Duration.between(localDateTime1, localDateTime2).toDays();
    }

    /**
     * 计算2个时间间隔多少小时
     *
     * @param <T>
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public <T> long hourDifference(T time1, T time2) throws ParseException {
        String timestr1 = this.format(time1, DatePattern.yyyyMMddHHmmss);
        String timestr2 = this.format(time2, DatePattern.yyyyMMddHHmmss);

        LocalDateTime localDateTime1 = DateUtils.parseLocalDateTime(timestr1, DatePattern.yyyyMMddHHmmss);
        LocalDateTime localDateTime2 = DateUtils.parseLocalDateTime(timestr2, DatePattern.yyyyMMddHHmmss);

        return Duration.between(localDateTime1, localDateTime2).toHours();
    }

    /**
     * 计算2个时间间隔多少分
     *
     * @param <T>
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public <T> long minuteDifference(T time1, T time2) throws ParseException {
        String timestr1 = this.format(time1, DatePattern.yyyyMMddHHmmss);
        String timestr2 = this.format(time2, DatePattern.yyyyMMddHHmmss);

        LocalDateTime localDateTime1 = DateUtils.parseLocalDateTime(timestr1, DatePattern.yyyyMMddHHmmss);
        LocalDateTime localDateTime2 = DateUtils.parseLocalDateTime(timestr2, DatePattern.yyyyMMddHHmmss);

        return Duration.between(localDateTime1, localDateTime2).toMinutes();
    }

    /**
     * 计算2个时间间隔多少秒
     *
     * @param <T>
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public <T> long secondDifference(T time1, T time2) throws ParseException {
        String timestr1 = this.format(time1, DatePattern.yyyyMMddHHmmss);
        String timestr2 = this.format(time2, DatePattern.yyyyMMddHHmmss);

        LocalDateTime localDateTime1 = DateUtils.parseLocalDateTime(timestr1, DatePattern.yyyyMMddHHmmss);
        LocalDateTime localDateTime2 = DateUtils.parseLocalDateTime(timestr2, DatePattern.yyyyMMddHHmmss);

        return Duration.between(localDateTime1, localDateTime2).getSeconds();
    }

}
