package com.ywl.framework.common.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * 新老日期转换
 *
 * @author luozhan
 */
@UtilityClass
public class DateUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_HOURS_PATTERN = "yyyy-MM-dd HH:mm";

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 时间字符串格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, ISO_DATE_TIME);
    }

    /**
     * 时间戳转localDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime toLocalDateTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    public static Boolean isBetweenDateTime(LocalDateTime localDateTime, LocalDateTime startTime, LocalDateTime endTime) {
        if (localDateTime != null && startTime != null && endTime != null) {
            return !(localDateTime.isBefore(startTime) || localDateTime.isAfter(endTime));
        }
        return false;
    }

    public static Boolean isBetweenTime(LocalTime localTime, LocalTime startTime, LocalTime endTime) {
        if (localTime != null && startTime != null && endTime != null) {
            return !(localTime.isBefore(startTime) || localTime.isAfter(endTime));
        }
        return false;
    }

    public static Long betweenHours(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toHours();
    }

    public static Long betweenMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
    }

    public static Long toMill(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public LocalDateTime todayStart() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    public LocalDateTime todayEnd() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    public LocalDate getWeekOfMonday() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    public LocalDate getLastDay() {
        return LocalDate.now().minusDays(1);
    }

    public LocalDate getLastWeekMonday() {
        TemporalAdjuster LastMonday = TemporalAdjusters.ofDateAdjuster(
                temporal -> {
                    DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
                    int value = -(dow.getValue() + 6);
                    return temporal.plus(value, ChronoUnit.DAYS);
                });
        return LocalDate.now().with(LastMonday);
    }

    public LocalDate getLastWeekSunday() {
        return LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    }

    public LocalDate getLastMonthFirstDay() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    }

    public LocalDate getLastMonthLastDay() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

    public LocalDate getMonthFirstDay() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    public LocalDate getMonthLastDay() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static String format(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间戳为小时 类似 2020-02-02 02:00
     *
     * @param timestamp 时间戳
     * @return 格式化后时间
     */
    public static String format2Hours(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MINUTE, 0);
        return DateUtil.format(calendar.getTime(), DATE_HOURS_PATTERN);
    }

    /**
     * 解析起止时间戳中的连续小时
     *
     * @param start 开始
     * @param end 截止
     * @return 连续小时
     */
    public static List<String> parseHoursRange(long start, long end) {
        if (end < start) {
            throw new IllegalArgumentException("end小于start");
        }
        List<String> hoursRange = new LinkedList<>();
        Calendar cStart = Calendar.getInstance();
        cStart.setTimeInMillis(start);
        Calendar cEnd = Calendar.getInstance();
        cEnd.setTimeInMillis(end);
        while (true){
            if(cEnd.before(cStart)){
                break;
            }
            hoursRange.add(DateUtil.format(cStart.getTime(), DATE_HOURS_PATTERN));
            cStart.add(Calendar.HOUR_OF_DAY,1);
        }
        return hoursRange;
    }

    /**
     * 格式化时间戳为日期 类似 2020-02-02
     *
     * @param timestamp 时间戳
     * @return 格式化后时间
     */
    public static String format2Days(Long timestamp) {
        Date date = new Date(timestamp);
        return DateUtil.format(date, DATE_PATTERN);
    }

    /**
     * 解析起止时间戳中的连续天数
     *
     * @param start 开始
     * @param end 截止
     * @return 连续天数
     */
    public static List<String> parseDaysRange(Long start, Long end) {
        if (end < start) {
            throw new IllegalArgumentException("end小于start");
        }
        List<String> hoursRange = new LinkedList<>();
        Calendar cStart = Calendar.getInstance();
        cStart.setTimeInMillis(start);
        Calendar cEnd = Calendar.getInstance();
        cEnd.setTimeInMillis(end);
        while (true){
            if(cEnd.before(cStart)){
                break;
            }
            hoursRange.add(DateUtil.format(cStart.getTime(), DATE_PATTERN));
            cStart.add(Calendar.DAY_OF_MONTH,1);
        }
        return hoursRange;
    }
}
