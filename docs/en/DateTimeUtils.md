# 📅 DateTimeUtils Documentation

## Overview

DateTimeUtils is a time processing utility class based on Java 8+ LocalDateTime and timestamps, providing comprehensive time operations including formatting, parsing, calculation, and conversion.

## 🚀 Quick Start

### Timestamp Conversion

```java
// Get current timestamp
long currentTimestamp = DateTimeUtils.getCurrentTimestamp();

// Get current time string
String currentTime = DateTimeUtils.getCurrentTimeString();
System.out.println("Current time: " + currentTime); // 2023-12-01 14:30:25

// Timestamp to string
String timeStr = DateTimeUtils.timestampToString(1701405025000L);

// String to timestamp
long timestamp = DateTimeUtils.stringToTimestamp("2023-12-01 14:30:25");
```

### Time Formatting

```java
// Format current time
String formatted = DateTimeUtils.formatCurrent("yyyy-MM-dd HH:mm:ss");

// Format specified timestamp
String formatted = DateTimeUtils.formatTimestamp(1701405025000L, "MM/dd/yyyy HH:mm");

// Format LocalDateTime
LocalDateTime now = LocalDateTime.now();
String formatted = DateTimeUtils.format(now, "yyyy-MM-dd");
```

## 📋 Time Operations

### Time Parsing

```java
// Parse standard format
LocalDateTime dateTime = DateTimeUtils.parse("2023-12-01 14:30:25");

// Parse custom format
LocalDateTime dateTime = DateTimeUtils.parse("2023/12/01 14:30:25", "yyyy/MM/dd HH:mm:ss");

// Parse date (time part as 00:00:00)
LocalDateTime dateTime = DateTimeUtils.parseDate("2023-12-01");

// Parse time (date part as 1970-01-01)
LocalDateTime dateTime = DateTimeUtils.parseTime("14:30:25");

// Safe parsing (returns null on failure)
LocalDateTime dateTime = DateTimeUtils.tryParse("invalid-date");
```

### Time Calculation

```java
// Time addition/subtraction
LocalDateTime future = DateTimeUtils.addYears(LocalDateTime.now(), 1);    // Add 1 year
LocalDateTime past = DateTimeUtils.addMonths(LocalDateTime.now(), -6);    // Subtract 6 months
LocalDateTime tomorrow = DateTimeUtils.addDays(LocalDateTime.now(), 1);   // Add 1 day
LocalDateTime later = DateTimeUtils.addHours(LocalDateTime.now(), 2);     // Add 2 hours
LocalDateTime soon = DateTimeUtils.addMinutes(LocalDateTime.now(), 30);   // Add 30 minutes
LocalDateTime next = DateTimeUtils.addSeconds(LocalDateTime.now(), 45);   // Add 45 seconds

// Timestamp calculation
long futureTimestamp = DateTimeUtils.addDaysToTimestamp(System.currentTimeMillis(), 7);
long pastTimestamp = DateTimeUtils.addHoursToTimestamp(System.currentTimeMillis(), -12);
```

### Time Comparison

```java
LocalDateTime time1 = LocalDateTime.of(2023, 12, 1, 14, 30, 25);
LocalDateTime time2 = LocalDateTime.of(2023, 12, 1, 16, 45, 30);

// Compare time
boolean isBefore = DateTimeUtils.isBefore(time1, time2); // true
boolean isAfter = DateTimeUtils.isAfter(time1, time2);   // false
boolean isEqual = DateTimeUtils.isEqual(time1, time2);   // false

// Check if same day
boolean sameDay = DateTimeUtils.isSameDay(time1, time2); // true

// Check if today
boolean isToday = DateTimeUtils.isToday(time1);

// Check if past
boolean isPast = DateTimeUtils.isPast(time1);

// Check if future
boolean isFuture = DateTimeUtils.isFuture(time2);
```

## ⏰ Time Ranges and Intervals

### Time Range Calculation

```java
// Get today's start and end timestamps
long[] todayRange = DateTimeUtils.getTodayRange();
long todayStart = todayRange[0];  // Today 00:00:00 timestamp
long todayEnd = todayRange[1];    // Today 23:59:59 timestamp

// Get yesterday's time range
long[] yesterdayRange = DateTimeUtils.getYesterdayRange();

// Get this week's time range
long[] thisWeekRange = DateTimeUtils.getThisWeekRange();

// Get this month's time range
long[] thisMonthRange = DateTimeUtils.getThisMonthRange();

// Get this year's time range
long[] thisYearRange = DateTimeUtils.getThisYearRange();

// Get specified date's day range
long[] dayRange = DateTimeUtils.getDayRange(LocalDate.of(2023, 12, 1));
```

### Custom Time Ranges

```java
// Get last N days time range
long[] lastWeekRange = DateTimeUtils.getLastDaysRange(7);

// Get last N hours time range
long[] lastHoursRange = DateTimeUtils.getLastHoursRange(24);

// Get specified month's time range
long[] monthRange = DateTimeUtils.getMonthRange(2023, 12);

// Get days between two dates
long daysBetween = DateTimeUtils.getDaysBetween(
    LocalDate.of(2023, 12, 1), 
    LocalDate.of(2023, 12, 15)
); // 14 days
```

## 🌍 Timezone Handling

### UTC and Local Time Conversion

```java
// Local time to UTC timestamp
long utcTimestamp = DateTimeUtils.toUtcTimestamp(LocalDateTime.now());

// UTC timestamp to local time
LocalDateTime localTime = DateTimeUtils.fromUtcTimestamp(utcTimestamp);

// Local time to UTC string
String utcString = DateTimeUtils.toUtcString(LocalDateTime.now());

// UTC string to local time
LocalDateTime localTime = DateTimeUtils.fromUtcString("2023-12-01T06:30:25Z");

// Get UTC time string
String utcNow = DateTimeUtils.getUtcTimeString();
```

### Timezone Conversion

```java
// Convert to specified timezone
LocalDateTime tokyoTime = DateTimeUtils.toTimeZone(
    LocalDateTime.now(), 
    ZoneId.of("Asia/Tokyo")
);

// Convert from specified timezone to local timezone
LocalDateTime localTime = DateTimeUtils.fromTimeZone(
    tokyoTime, 
    ZoneId.of("Asia/Tokyo")
);

// Get timezone offset
int offsetHours = DateTimeUtils.getTimezoneOffset();
```

## 📝 Notes

- All timestamps are millisecond-level long integers
- Default uses system timezone, UTC conversion needs explicit specification
- Time parsing failure throws RuntimeException, recommend using tryParse method
- Time range retrieval returns [start timestamp, end timestamp] array
- Friendly time display calculates relative time based on current time
- All time calculations are based on LocalDateTime, accurate to seconds 