# 📅 DateTimeUtils 使用文档

## 概述

DateTimeUtils是基于Java 8+ LocalDateTime和时间戳的时间处理工具类，提供时间格式化、解析、计算、转换等全面的时间操作功能。

## 🚀 快速开始

### 时间戳转换

```java
// 获取当前时间戳
long currentTimestamp = DateTimeUtils.getCurrentTimestamp();

// 获取当前时间字符串
String currentTime = DateTimeUtils.getCurrentTimeString();
System.out.println("当前时间: " + currentTime); // 2023-12-01 14:30:25

// 时间戳转字符串
String timeStr = DateTimeUtils.timestampToString(1701405025000L);

// 字符串转时间戳
long timestamp = DateTimeUtils.stringToTimestamp("2023-12-01 14:30:25");
```

### 时间格式化

```java
// 格式化当前时间
String formatted = DateTimeUtils.formatCurrent("yyyy年MM月dd日 HH:mm:ss");

// 格式化指定时间戳
String formatted = DateTimeUtils.formatTimestamp(1701405025000L, "MM/dd/yyyy HH:mm");

// 格式化LocalDateTime
LocalDateTime now = LocalDateTime.now();
String formatted = DateTimeUtils.format(now, "yyyy-MM-dd");
```

## 📋 时间操作详解

### 时间解析

```java
// 解析标准格式
LocalDateTime dateTime = DateTimeUtils.parse("2023-12-01 14:30:25");

// 解析自定义格式
LocalDateTime dateTime = DateTimeUtils.parse("2023/12/01 14:30:25", "yyyy/MM/dd HH:mm:ss");

// 解析日期（时间部分为00:00:00）
LocalDateTime dateTime = DateTimeUtils.parseDate("2023-12-01");

// 解析时间（年月日为1970-01-01）
LocalDateTime dateTime = DateTimeUtils.parseTime("14:30:25");

// 安全解析（失败返回null）
LocalDateTime dateTime = DateTimeUtils.tryParse("invalid-date");
```

### 时间计算

```java
// 时间加减
LocalDateTime future = DateTimeUtils.addYears(LocalDateTime.now(), 1);    // 加1年
LocalDateTime past = DateTimeUtils.addMonths(LocalDateTime.now(), -6);    // 减6个月
LocalDateTime tomorrow = DateTimeUtils.addDays(LocalDateTime.now(), 1);   // 加1天
LocalDateTime later = DateTimeUtils.addHours(LocalDateTime.now(), 2);     // 加2小时
LocalDateTime soon = DateTimeUtils.addMinutes(LocalDateTime.now(), 30);   // 加30分钟
LocalDateTime next = DateTimeUtils.addSeconds(LocalDateTime.now(), 45);   // 加45秒

// 时间戳计算
long futureTimestamp = DateTimeUtils.addDaysToTimestamp(System.currentTimeMillis(), 7);
long pastTimestamp = DateTimeUtils.addHoursToTimestamp(System.currentTimeMillis(), -12);
```

### 时间比较

```java
LocalDateTime time1 = LocalDateTime.of(2023, 12, 1, 14, 30, 25);
LocalDateTime time2 = LocalDateTime.of(2023, 12, 1, 16, 45, 30);

// 比较大小
boolean isBefore = DateTimeUtils.isBefore(time1, time2); // true
boolean isAfter = DateTimeUtils.isAfter(time1, time2);   // false
boolean isEqual = DateTimeUtils.isEqual(time1, time2);   // false

// 判断是否是同一天
boolean sameDay = DateTimeUtils.isSameDay(time1, time2); // true

// 判断是否是今天
boolean isToday = DateTimeUtils.isToday(time1);

// 判断是否是过去
boolean isPast = DateTimeUtils.isPast(time1);

// 判断是否是未来
boolean isFuture = DateTimeUtils.isFuture(time2);
```

## ⏰ 时间范围和区间

### 时间范围计算

```java
// 获取今天的开始和结束时间戳
long[] todayRange = DateTimeUtils.getTodayRange();
long todayStart = todayRange[0];  // 今天00:00:00的时间戳
long todayEnd = todayRange[1];    // 今天23:59:59的时间戳

// 获取昨天的时间范围
long[] yesterdayRange = DateTimeUtils.getYesterdayRange();

// 获取本周的时间范围
long[] thisWeekRange = DateTimeUtils.getThisWeekRange();

// 获取本月的时间范围
long[] thisMonthRange = DateTimeUtils.getThisMonthRange();

// 获取本年的时间范围
long[] thisYearRange = DateTimeUtils.getThisYearRange();

// 获取指定日期的天范围
long[] dayRange = DateTimeUtils.getDayRange(LocalDate.of(2023, 12, 1));
```

### 自定义时间范围

```java
// 获取最近N天的时间范围
long[] lastWeekRange = DateTimeUtils.getLastDaysRange(7);

// 获取最近N小时的时间范围
long[] lastHoursRange = DateTimeUtils.getLastHoursRange(24);

// 获取指定月份的时间范围
long[] monthRange = DateTimeUtils.getMonthRange(2023, 12);

// 获取两个时间之间的天数
long daysBetween = DateTimeUtils.getDaysBetween(
    LocalDate.of(2023, 12, 1), 
    LocalDate.of(2023, 12, 15)
); // 14天
```

## 🌍 时区处理

### UTC与本地时间转换

```java
// 本地时间转UTC时间戳
long utcTimestamp = DateTimeUtils.toUtcTimestamp(LocalDateTime.now());

// UTC时间戳转本地时间
LocalDateTime localTime = DateTimeUtils.fromUtcTimestamp(utcTimestamp);

// 本地时间转UTC字符串
String utcString = DateTimeUtils.toUtcString(LocalDateTime.now());

// UTC字符串转本地时间
LocalDateTime localTime = DateTimeUtils.fromUtcString("2023-12-01T06:30:25Z");

// 获取UTC时间字符串
String utcNow = DateTimeUtils.getUtcTimeString();
```

### 时区转换

```java
// 转换到指定时区
LocalDateTime tokyoTime = DateTimeUtils.toTimeZone(
    LocalDateTime.now(), 
    ZoneId.of("Asia/Tokyo")
);

// 从指定时区转换到本地时区
LocalDateTime localTime = DateTimeUtils.fromTimeZone(
    tokyoTime, 
    ZoneId.of("Asia/Tokyo")
);

// 获取时区偏移
int offsetHours = DateTimeUtils.getTimezoneOffset();
```

## ⚡ 实际应用场景

### 日志时间处理

```java
public class LogTimeProcessor {
    
    /**
     * 生成日志时间戳
     */
    public static String generateLogTimestamp() {
        return DateTimeUtils.formatCurrent("yyyy-MM-dd HH:mm:ss.SSS");
    }
    
    /**
     * 解析日志时间
     */
    public static LocalDateTime parseLogTime(String logLine) {
        // 提取日志中的时间部分
        String timeStr = logLine.substring(0, 23); // "2023-12-01 14:30:25.123"
        return DateTimeUtils.parse(timeStr, "yyyy-MM-dd HH:mm:ss.SSS");
    }
    
    /**
     * 过滤指定时间范围的日志
     */
    public static List<String> filterLogsByTimeRange(List<String> logs, 
                                                     LocalDateTime startTime, 
                                                     LocalDateTime endTime) {
        return logs.stream()
            .filter(log -> {
                try {
                    LocalDateTime logTime = parseLogTime(log);
                    return !logTime.isBefore(startTime) && !logTime.isAfter(endTime);
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }
}
```

### 数据库时间字段处理

```java
public class DatabaseTimeHandler {
    
    /**
     * 保存数据时设置时间字段
     */
    public static JSONObject setDatabaseTimes(JSONObject data) {
        long currentTimestamp = DateTimeUtils.getCurrentTimestamp();
        
        data.put("createTime", currentTimestamp);
        data.put("updateTime", currentTimestamp);
        data.put("createTimeStr", DateTimeUtils.timestampToString(currentTimestamp));
        
        return data;
    }
    
    /**
     * 更新数据时设置更新时间
     */
    public static JSONObject updateDatabaseTime(JSONObject data) {
        long currentTimestamp = DateTimeUtils.getCurrentTimestamp();
        
        data.put("updateTime", currentTimestamp);
        data.put("updateTimeStr", DateTimeUtils.timestampToString(currentTimestamp));
        
        return data;
    }
    
    /**
     * 查询指定时间范围的数据
     */
    public static String buildTimeRangeQuery(LocalDate startDate, LocalDate endDate) {
        long[] startRange = DateTimeUtils.getDayRange(startDate);
        long[] endRange = DateTimeUtils.getDayRange(endDate);
        
        return String.format("SELECT * FROM table WHERE createTime >= %d AND createTime <= %d", 
                           startRange[0], endRange[1]);
    }
}
```

### 缓存过期时间管理

```java
public class CacheExpirationManager {
    
    /**
     * 计算缓存过期时间戳
     */
    public static long calculateExpireTime(int expireMinutes) {
        return DateTimeUtils.addMinutesToTimestamp(
            DateTimeUtils.getCurrentTimestamp(), 
            expireMinutes
        );
    }
    
    /**
     * 检查缓存是否过期
     */
    public static boolean isCacheExpired(long expireTimestamp) {
        return DateTimeUtils.getCurrentTimestamp() > expireTimestamp;
    }
    
    /**
     * 获取缓存剩余有效时间（秒）
     */
    public static long getCacheRemainingTime(long expireTimestamp) {
        long remaining = expireTimestamp - DateTimeUtils.getCurrentTimestamp();
        return Math.max(0, remaining / 1000);
    }
    
    /**
     * 设置定时过期的缓存项
     */
    public static JSONObject createCacheItem(String key, Object value, int expireMinutes) {
        JSONObject cacheItem = new JSONObject();
        cacheItem.put("key", key);
        cacheItem.put("value", value);
        cacheItem.put("expireTime", calculateExpireTime(expireMinutes));
        cacheItem.put("createTime", DateTimeUtils.getCurrentTimestamp());
        
        return cacheItem;
    }
}
```

### 定时任务调度

```java
public class TaskScheduler {
    
    /**
     * 计算下次执行时间
     */
    public static long calculateNextRunTime(String cronExpression) {
        // 简化的cron表达式解析（实际应用中可能需要更复杂的实现）
        LocalDateTime now = LocalDateTime.now();
        
        if ("0 0 * * *".equals(cronExpression)) {
            // 每天午夜执行
            return DateTimeUtils.toTimestamp(now.plusDays(1).withHour(0).withMinute(0).withSecond(0));
        } else if ("0 */1 * * *".equals(cronExpression)) {
            // 每小时执行
            return DateTimeUtils.toTimestamp(now.plusHours(1).withMinute(0).withSecond(0));
        }
        
        // 默认1小时后执行
        return DateTimeUtils.addHoursToTimestamp(DateTimeUtils.getCurrentTimestamp(), 1);
    }
    
    /**
     * 判断是否到了执行时间
     */
    public static boolean isTimeToRun(long scheduledTime) {
        return DateTimeUtils.getCurrentTimestamp() >= scheduledTime;
    }
    
    /**
     * 创建定时任务
     */
    public static JSONObject createScheduledTask(String taskName, String cronExpression) {
        JSONObject task = new JSONObject();
        task.put("taskName", taskName);
        task.put("cronExpression", cronExpression);
        task.put("nextRunTime", calculateNextRunTime(cronExpression));
        task.put("createTime", DateTimeUtils.getCurrentTimestamp());
        task.put("status", "SCHEDULED");
        
        return task;
    }
}
```

### 友好时间显示

```java
public class FriendlyTimeDisplay {
    
    /**
     * 获取友好的时间显示
     */
    public static String getFriendlyTime(long timestamp) {
        LocalDateTime time = DateTimeUtils.fromTimestamp(timestamp);
        LocalDateTime now = LocalDateTime.now();
        
        long seconds = Duration.between(time, now).toSeconds();
        
        if (seconds < 60) {
            return "刚刚";
        } else if (seconds < 3600) {
            return (seconds / 60) + "分钟前";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "小时前";
        } else if (seconds < 2592000) {
            return (seconds / 86400) + "天前";
        } else {
            return DateTimeUtils.format(time, "yyyy-MM-dd");
        }
    }
    
    /**
     * 获取相对时间描述
     */
    public static String getRelativeTime(LocalDateTime time) {
        if (DateTimeUtils.isToday(time)) {
            return "今天 " + DateTimeUtils.format(time, "HH:mm");
        } else if (DateTimeUtils.isYesterday(time)) {
            return "昨天 " + DateTimeUtils.format(time, "HH:mm");
        } else if (DateTimeUtils.isTomorrow(time)) {
            return "明天 " + DateTimeUtils.format(time, "HH:mm");
        } else {
            return DateTimeUtils.format(time, "MM-dd HH:mm");
        }
    }
    
    /**
     * 获取时间范围描述
     */
    public static String getTimeRangeDescription(long startTimestamp, long endTimestamp) {
        LocalDateTime start = DateTimeUtils.fromTimestamp(startTimestamp);
        LocalDateTime end = DateTimeUtils.fromTimestamp(endTimestamp);
        
        if (DateTimeUtils.isSameDay(start, end)) {
            return DateTimeUtils.format(start, "yyyy-MM-dd") + " " +
                   DateTimeUtils.format(start, "HH:mm") + "-" +
                   DateTimeUtils.format(end, "HH:mm");
        } else {
            return DateTimeUtils.format(start, "MM-dd HH:mm") + " - " +
                   DateTimeUtils.format(end, "MM-dd HH:mm");
        }
    }
}
```

## 🔧 高级功能

### 时间序列处理

```java
public class TimeSeriesProcessor {
    
    /**
     * 生成时间序列
     */
    public static List<LocalDateTime> generateTimeSequence(LocalDateTime start, 
                                                          LocalDateTime end, 
                                                          Duration interval) {
        List<LocalDateTime> sequence = new ArrayList<>();
        LocalDateTime current = start;
        
        while (!current.isAfter(end)) {
            sequence.add(current);
            current = current.plus(interval);
        }
        
        return sequence;
    }
    
    /**
     * 按小时分组时间戳
     */
    public static Map<String, List<Long>> groupByHour(List<Long> timestamps) {
        return timestamps.stream()
            .collect(Collectors.groupingBy(ts -> 
                DateTimeUtils.formatTimestamp(ts, "yyyy-MM-dd HH")));
    }
    
    /**
     * 按天分组时间戳
     */
    public static Map<String, List<Long>> groupByDay(List<Long> timestamps) {
        return timestamps.stream()
            .collect(Collectors.groupingBy(ts -> 
                DateTimeUtils.formatTimestamp(ts, "yyyy-MM-dd")));
    }
}
```

### 工作日处理

```java
public class WorkdayProcessor {
    
    /**
     * 判断是否是工作日
     */
    public static boolean isWorkday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    
    /**
     * 获取下一个工作日
     */
    public static LocalDate getNextWorkday(LocalDate date) {
        LocalDate nextDay = date.plusDays(1);
        while (!isWorkday(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }
    
    /**
     * 计算两个日期之间的工作日数量
     */
    public static long countWorkdays(LocalDate start, LocalDate end) {
        return start.datesUntil(end.plusDays(1))
                   .filter(WorkdayProcessor::isWorkday)
                   .count();
    }
}
```

## 📊 性能优化

### 时间格式化缓存

```java
public class TimeFormatterCache {
    
    private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 获取缓存的格式化器
     */
    public static DateTimeFormatter getFormatter(String pattern) {
        return FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }
    
    /**
     * 批量格式化时间戳
     */
    public static List<String> batchFormat(List<Long> timestamps, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return timestamps.stream()
            .map(ts -> LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault()))
            .map(formatter::format)
            .collect(Collectors.toList());
    }
}
```

## 📝 注意事项

- 所有时间戳都是毫秒级别的长整型
- 默认使用系统时区，UTC转换需要明确指定
- 时间解析失败会抛出RuntimeException，建议使用tryParse方法
- 时间范围获取返回的是[开始时间戳, 结束时间戳]数组
- 友好时间显示基于当前时间计算相对时间
- 所有时间计算都基于LocalDateTime，精确到秒 