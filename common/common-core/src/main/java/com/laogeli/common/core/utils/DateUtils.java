package com.laogeli.common.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * 日期工具类
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS");

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String dateToString(Date date) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return date != null ? dFormat.format(date) : "";
    }

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String dateToStringSingle(Date date) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        return date != null ? dFormat.format(date) : "";
    }

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String localDateMillisToString(LocalDateTime date) {
        return date != null ? date.format(FORMATTER_MILLIS) : "";
    }

    /**
     * LocalDate转Date
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime localDateTime
     * @return Date
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date转LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 返回距离凌晨 00：00：01 时间
     *
     * @return Long
     */
    public static Long getExpireTime() {
        Long todayTime = LocalDate.now().plusDays(1).atTime(0, 0, 0, 1).atOffset(ZoneOffset.ofHours(8)).toEpochSecond();
        Long nowTime = LocalDateTime.now().atOffset(ZoneOffset.ofHours(8)).toEpochSecond();
        Long expireTime = todayTime - nowTime;
        return expireTime;
    }

    /**
     * 返回当前日期时间字符串 yyyyMMdd
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date().getTime());
    }

    /**
     * 返回当前日期字符串  yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateAndMS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date().getTime());
    }

    public static String getDateAndMS1() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        return df.format(new Date().getTime());
    }

    /**
     * 返回当前日期时间字符串 yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentDateTimeStr() {
        return LocalDateTime.now().format(FORMATTER);
    }

    /**
     * 返回当前的日期
     *
     * @return
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 获取本月的第一天
     *
     * @return
     */
    public static String getFirstDayOfThisMonth() {
        return getCurrentLocalDate().with(TemporalAdjusters.firstDayOfMonth()).format(DATE_FORMATTER);
    }

    /**
     * 获取本月的最后一天
     *
     * @return
     */
    public static String getLastDayOfThisMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = strToDateNotDD(getCurrentDateTimeStr());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        // return getCurrentLocalDate().with(TemporalAdjusters.lastDayOfMonth()).format(DATE_FORMATTER);
        return sdf.format(dates);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM
     *
     * @param strDate
     * @return
     */
    public static Date strToDateNotDD(String strDate) {
        if (StringUtils.isBlank(strDate)) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 日期相隔小时
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    /**
     * 日期相隔分钟
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMinutes();
    }

    /**
     * 日期相隔秒数
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationSeconds(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).getSeconds();
    }

    /**
     * 日期相隔毫秒数
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 获取距离月底 最后一天23:59:59 的秒数
     *
     * @return Long
     */
    public static Long getSecondsApart() {
        String dt = getCurrentDateTimeStr();
        String dt2 = getLastDayOfThisMonth();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Instant start = sd.parse(dt).toInstant();
            Instant end = sd.parse(dt2).toInstant();
            return durationSeconds(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        if (StringUtils.isBlank(strDate)) return null;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间戳转换为时间
     *
     * @param s
     * @return
     */
    public static Date stampToDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        String strDate = simpleDateFormat.format(date);
        if (StringUtils.isBlank(strDate)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得当前年xxxx-01-01
     *
     * @return
     */
    public static String getCurrentYear() {
        Calendar currCal = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currCal.get(Calendar.YEAR));
        Date time = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }



    /**
     * 判断两个日期是否为同一年
     */
    public static Boolean isOneYear(String beginTime, String endTime) {
        String year1 = beginTime.substring(0, 4);
        String year2 = endTime.substring(0, 4);
        if (year1.equals(year2)) {
            return true;
        }
        return false;
    }


    /**
     * 两个日期相差天数
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws Exception
     */
    public static int daysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据日期判断是当年第几天
     *
     * @param str
     * @return
     */
    public static int dayNumber(String str) {
        int dateSum = 0;
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(5, 7));
        int day = Integer.valueOf(str.substring(8, 10));
        for (int i = 1; i < month; i++) {
            switch (i) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    dateSum += 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    dateSum += 30;
                    break;
                case 2:
                    if (((year % 4 == 0) & (year % 100 != 0)) | (year % 400 == 0))
                        dateSum += 29;
                    else dateSum += 28;
            }
        }
        return dateSum = dateSum + day;
    }

    /**
     * 获取时间中的 年份
     *
     * @param dateStr
     * @return
     */
    public static int getYear(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calender = Calendar.getInstance();
        try {
            Date date = simpleDateFormat.parse(dateStr);
            calender.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calender.get(Calendar.YEAR);
    }


    /**
     * 获取时间中的 月份
     *
     * @param dateStr
     * @return
     */
    public static int getMonth(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calender = Calendar.getInstance();
        try {
            Date date = simpleDateFormat.parse(dateStr);
            calender.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calender.get(Calendar.MONTH) + 1;
    }


    //获取日期
    public static Date getDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =null;
        try{
             date = simpleDateFormat.parse(dateStr);

        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

}
