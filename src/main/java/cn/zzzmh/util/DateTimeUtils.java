package cn.zzzmh.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期时间工具类
 * 基于Java 8+ Time API，提供时间戳转换、格式化、计算等功能
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class DateTimeUtils {

    // ==================== 常用日期格式 ====================
    
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DATE_COMPACT = "yyyyMMdd";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    
    // ==================== 常用DateTimeFormatter ====================
    
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
    public static final DateTimeFormatter FORMATTER_DATETIME_MS = DateTimeFormatter.ofPattern(PATTERN_DATETIME_MS);
    public static final DateTimeFormatter FORMATTER_DATE_COMPACT = DateTimeFormatter.ofPattern(PATTERN_DATE_COMPACT);
    public static final DateTimeFormatter FORMATTER_DATETIME_COMPACT = DateTimeFormatter.ofPattern(PATTERN_DATETIME_COMPACT);
    public static final DateTimeFormatter FORMATTER_ISO8601 = DateTimeFormatter.ofPattern(PATTERN_ISO8601);

    /**
     * 私有构造函数，防止实例化
     */
    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== 当前时间获取 ====================

    /**
     * 获取当前时间戳（毫秒）
     * 
     * @return 当前时间戳
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前LocalDateTime
     * 
     * @return 当前LocalDateTime
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前LocalDate
     * 
     * @return 当前LocalDate
     */
    public static LocalDate nowDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间的格式化字符串
     * 
     * @param pattern 格式模式
     * @return 格式化后的时间字符串
     */
    public static String nowFormat(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        return nowDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间的默认格式字符串 (yyyy-MM-dd HH:mm:ss)
     * 
     * @return 格式化后的时间字符串
     */
    public static String nowFormat() {
        return nowFormat(PATTERN_DATETIME);
    }

    // ==================== 时间戳转换 ====================

    /**
     * 时间戳转LocalDateTime
     * 
     * @param timestamp 时间戳（毫秒）
     * @return LocalDateTime
     */
    public static LocalDateTime timestampToDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 时间戳转LocalDate
     * 
     * @param timestamp 时间戳（毫秒）
     * @return LocalDate
     */
    public static LocalDate timestampToDate(long timestamp) {
        return timestampToDateTime(timestamp).toLocalDate();
    }

    /**
     * LocalDateTime转时间戳
     * 
     * @param dateTime LocalDateTime
     * @return 时间戳（毫秒）
     */
    public static long dateTimeToTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null");
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * LocalDate转时间戳（当天0点）
     * 
     * @param date LocalDate
     * @return 时间戳（毫秒）
     */
    public static long dateToTimestamp(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return dateTimeToTimestamp(date.atStartOfDay());
    }

    /**
     * Date转时间戳
     * 
     * @param date Date对象
     * @return 时间戳（毫秒）
     */
    public static long dateToTimestamp(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.getTime();
    }

    // ==================== 格式化和解析 ====================

    /**
     * 时间戳格式化为字符串
     * 
     * @param timestamp 时间戳（毫秒）
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String timestampFormat(long timestamp, String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        return timestampToDateTime(timestamp).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间戳格式化为默认字符串 (yyyy-MM-dd HH:mm:ss)
     * 
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的字符串
     */
    public static String timestampFormat(long timestamp) {
        return timestampFormat(timestamp, PATTERN_DATETIME);
    }

    /**
     * LocalDateTime格式化为字符串
     * 
     * @param dateTime LocalDateTime
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDate格式化为字符串
     * 
     * @param date LocalDate
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期时间字符串为LocalDateTime
     * 
     * @param dateTimeStr 日期时间字符串
     * @param pattern 格式模式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null) {
            throw new IllegalArgumentException("DateTime string cannot be null");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Failed to parse datetime: " + dateTimeStr + " with pattern: " + pattern, e);
        }
    }

    /**
     * 解析日期字符串为LocalDate
     * 
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null) {
            throw new IllegalArgumentException("Date string cannot be null");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Failed to parse date: " + dateStr + " with pattern: " + pattern, e);
        }
    }

    /**
     * 解析日期时间字符串为时间戳
     * 
     * @param dateTimeStr 日期时间字符串
     * @param pattern 格式模式
     * @return 时间戳（毫秒）
     */
    public static long parseToTimestamp(String dateTimeStr, String pattern) {
        return dateTimeToTimestamp(parseDateTime(dateTimeStr, pattern));
    }

    // ==================== 时间计算 ====================

    /**
     * 时间戳加上指定天数
     * 
     * @param timestamp 原时间戳
     * @param days 天数（可为负数）
     * @return 新时间戳
     */
    public static long addDays(long timestamp, long days) {
        return dateTimeToTimestamp(timestampToDateTime(timestamp).plusDays(days));
    }

    /**
     * 时间戳加上指定小时数
     * 
     * @param timestamp 原时间戳
     * @param hours 小时数（可为负数）
     * @return 新时间戳
     */
    public static long addHours(long timestamp, long hours) {
        return dateTimeToTimestamp(timestampToDateTime(timestamp).plusHours(hours));
    }

    /**
     * 时间戳加上指定分钟数
     * 
     * @param timestamp 原时间戳
     * @param minutes 分钟数（可为负数）
     * @return 新时间戳
     */
    public static long addMinutes(long timestamp, long minutes) {
        return dateTimeToTimestamp(timestampToDateTime(timestamp).plusMinutes(minutes));
    }

    /**
     * 时间戳加上指定秒数
     * 
     * @param timestamp 原时间戳
     * @param seconds 秒数（可为负数）
     * @return 新时间戳
     */
    public static long addSeconds(long timestamp, long seconds) {
        return dateTimeToTimestamp(timestampToDateTime(timestamp).plusSeconds(seconds));
    }

    /**
     * 计算两个时间戳之间的天数差
     * 
     * @param timestamp1 时间戳1
     * @param timestamp2 时间戳2
     * @return 天数差（timestamp2 - timestamp1）
     */
    public static long daysBetween(long timestamp1, long timestamp2) {
        LocalDate date1 = timestampToDate(timestamp1);
        LocalDate date2 = timestampToDate(timestamp2);
        return ChronoUnit.DAYS.between(date1, date2);
    }

    /**
     * 计算两个时间戳之间的小时差
     * 
     * @param timestamp1 时间戳1
     * @param timestamp2 时间戳2
     * @return 小时差（timestamp2 - timestamp1）
     */
    public static long hoursBetween(long timestamp1, long timestamp2) {
        LocalDateTime dateTime1 = timestampToDateTime(timestamp1);
        LocalDateTime dateTime2 = timestampToDateTime(timestamp2);
        return ChronoUnit.HOURS.between(dateTime1, dateTime2);
    }

    /**
     * 计算两个时间戳之间的分钟差
     * 
     * @param timestamp1 时间戳1
     * @param timestamp2 时间戳2
     * @return 分钟差（timestamp2 - timestamp1）
     */
    public static long minutesBetween(long timestamp1, long timestamp2) {
        LocalDateTime dateTime1 = timestampToDateTime(timestamp1);
        LocalDateTime dateTime2 = timestampToDateTime(timestamp2);
        return ChronoUnit.MINUTES.between(dateTime1, dateTime2);
    }

    // ==================== 时间范围 ====================

    /**
     * 获取指定日期的开始时间戳（当天00:00:00）
     * 
     * @param timestamp 时间戳
     * @return 当天开始时间戳
     */
    public static long startOfDay(long timestamp) {
        return dateTimeToTimestamp(timestampToDateTime(timestamp).with(LocalTime.MIN));
    }

    /**
     * 获取指定日期的结束时间戳（当天23:59:59.999）
     * 
     * @param timestamp 时间戳
     * @return 当天结束时间戳
     */
    public static long endOfDay(long timestamp) {
        return dateTimeToTimestamp(timestampToDateTime(timestamp).with(LocalTime.MAX));
    }

    /**
     * 获取指定日期所在月的第一天时间戳
     * 
     * @param timestamp 时间戳
     * @return 月初时间戳
     */
    public static long startOfMonth(long timestamp) {
        LocalDateTime dateTime = timestampToDateTime(timestamp);
        return dateTimeToTimestamp(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN));
    }

    /**
     * 获取指定日期所在月的最后一天时间戳
     * 
     * @param timestamp 时间戳
     * @return 月末时间戳
     */
    public static long endOfMonth(long timestamp) {
        LocalDateTime dateTime = timestampToDateTime(timestamp);
        return dateTimeToTimestamp(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX));
    }

    /**
     * 获取指定日期所在年的第一天时间戳
     * 
     * @param timestamp 时间戳
     * @return 年初时间戳
     */
    public static long startOfYear(long timestamp) {
        LocalDateTime dateTime = timestampToDateTime(timestamp);
        return dateTimeToTimestamp(dateTime.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN));
    }

    /**
     * 获取指定日期所在年的最后一天时间戳
     * 
     * @param timestamp 时间戳
     * @return 年末时间戳
     */
    public static long endOfYear(long timestamp) {
        LocalDateTime dateTime = timestampToDateTime(timestamp);
        return dateTimeToTimestamp(dateTime.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX));
    }

    // ==================== 时区转换 ====================

    /**
     * 将时间戳从一个时区转换到另一个时区
     * 
     * @param timestamp 原时间戳
     * @param fromZone 源时区
     * @param toZone 目标时区
     * @return 转换后的时间戳
     */
    public static long convertTimezone(long timestamp, ZoneId fromZone, ZoneId toZone) {
        if (fromZone == null) {
            throw new IllegalArgumentException("From zone cannot be null");
        }
        if (toZone == null) {
            throw new IllegalArgumentException("To zone cannot be null");
        }
        
        ZonedDateTime fromZonedDateTime = Instant.ofEpochMilli(timestamp).atZone(fromZone);
        ZonedDateTime toZonedDateTime = fromZonedDateTime.withZoneSameInstant(toZone);
        return toZonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 本地时间转UTC时间戳
     * 
     * @param timestamp 本地时间戳
     * @return UTC时间戳
     */
    public static long toUtc(long timestamp) {
        return convertTimezone(timestamp, ZoneId.systemDefault(), ZoneOffset.UTC);
    }

    /**
     * UTC时间戳转本地时间
     * 
     * @param utcTimestamp UTC时间戳
     * @return 本地时间戳
     */
    public static long fromUtc(long utcTimestamp) {
        return convertTimezone(utcTimestamp, ZoneOffset.UTC, ZoneId.systemDefault());
    }

    // ==================== 工具方法 ====================

    /**
     * 判断是否为闰年
     * 
     * @param year 年份
     * @return 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return Year.of(year).isLeap();
    }

    /**
     * 判断日期是否为今天
     * 
     * @param timestamp 时间戳
     * @return 是否为今天
     */
    public static boolean isToday(long timestamp) {
        return timestampToDate(timestamp).equals(nowDate());
    }

    /**
     * 判断日期是否为昨天
     * 
     * @param timestamp 时间戳
     * @return 是否为昨天
     */
    public static boolean isYesterday(long timestamp) {
        return timestampToDate(timestamp).equals(nowDate().minusDays(1));
    }

    /**
     * 判断日期是否为明天
     * 
     * @param timestamp 时间戳
     * @return 是否为明天
     */
    public static boolean isTomorrow(long timestamp) {
        return timestampToDate(timestamp).equals(nowDate().plusDays(1));
    }

    /**
     * 获取星期几（1=周一，7=周日）
     * 
     * @param timestamp 时间戳
     * @return 星期几
     */
    public static int getDayOfWeek(long timestamp) {
        return timestampToDate(timestamp).getDayOfWeek().getValue();
    }

    /**
     * 获取月份中的第几天
     * 
     * @param timestamp 时间戳
     * @return 日
     */
    public static int getDayOfMonth(long timestamp) {
        return timestampToDate(timestamp).getDayOfMonth();
    }

    /**
     * 获取一年中的第几天
     * 
     * @param timestamp 时间戳
     * @return 一年中的第几天
     */
    public static int getDayOfYear(long timestamp) {
        return timestampToDate(timestamp).getDayOfYear();
    }

    /**
     * 判断时间是否在指定范围内
     * 
     * @param timestamp 待判断的时间戳
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 是否在范围内
     */
    public static boolean isBetween(long timestamp, long startTimestamp, long endTimestamp) {
        return timestamp >= startTimestamp && timestamp <= endTimestamp;
    }

    /**
     * 获取友好的时间描述（如：刚刚、1分钟前、1小时前等）
     * 
     * @param timestamp 时间戳
     * @return 友好的时间描述
     */
    public static String getFriendlyTime(long timestamp) {
        long now = now();
        long diff = now - timestamp;
        
        if (diff < 0) {
            return "未来时间";
        }
        
        long seconds = diff / 1000;
        
        if (seconds < 60) {
            return "刚刚";
        }
        
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "分钟前";
        }
        
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "小时前";
        }
        
        long days = hours / 24;
        if (days < 7) {
            return days + "天前";
        }
        
        if (days < 30) {
            long weeks = days / 7;
            return weeks + "周前";
        }
        
        if (days < 365) {
            long months = days / 30;
            return months + "个月前";
        }
        
        long years = days / 365;
        return years + "年前";
    }
} 