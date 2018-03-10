package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenTime(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }


    public static boolean isBetweenDate(LocalDate time, LocalDate startTime, LocalDate endTime) {
      return   time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0;
    }

    public static boolean isBetweenDateTime(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        return   dateTime.compareTo(start) >= 0 && dateTime.compareTo(end) <= 0;
    }
}
