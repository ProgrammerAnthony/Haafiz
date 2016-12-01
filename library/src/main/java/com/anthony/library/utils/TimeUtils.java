package com.anthony.library.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    // 日期格式为 2016-02-03 17:04:58
    public static final String PATTERN_DATE = "yyyy年MM月dd日";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_SPLIT = " ";
    public static final String PATTERN = PATTERN_DATE + PATTERN_SPLIT + PATTERN_TIME;
    public static final long ONE_SECOND_MILLIONS = 1000;
    public static final long ONE_MINUTE_MILLIONS = 60 * ONE_SECOND_MILLIONS;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;
    public static final int DAY_OF_YEAR = 365;

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static long getNowTime() {
        return System.currentTimeMillis();
    }

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    /**
     * 转化为几天前这种形式。
     *
     * @param millis
     * @return
     */
    public static String format(long millis) {
        long delta = System.currentTimeMillis() - millis;
        if (delta < 1L * ONE_MINUTE) {
//            long seconds = toSeconds(delta);
//            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
            return 1 + ONE_MINUTE_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    //一天的秒数
    public static final long ONE_DAY_TIME = 24 * 60 * 60;

    /**
     * 获取年月日时间
     */
    public static String getNYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String yymmddString = sdf.format(new Date());
        return yymmddString;
    }


    /**
     * 获取年月日时间
     */
    public static String getNYYMMDD(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        long now = date.getTime();
        long next = now + day * ONE_DAY_TIME * 1000;
        String yymmddString = sdf.format(next);
        return yymmddString;
    }

    /**
     * convert time str
     *
     * @param time
     * @return
     */
    public static String convertTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * get format date
     *
     * @param timemillis
     * @return
     */
    public static String getFormatDate(long timemillis) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(timemillis));
    }


    /**
     * 获取日期 PATTERN_DATE 部分
     */
    public static String getDate(String date) {
        if (TextUtils.isEmpty(date) || !date.contains(PATTERN_SPLIT)) {
            return "";
        }
        return date.split(PATTERN_SPLIT)[0];
    }
    /**
     * 原有日期上累加月
     *
     * @return 累加后的日期 PATTERN_DATE 部分
     */
    public static String addMonth(String date, int moonCount) {
        //如果date为空 就用当前时间
        if (TextUtils.isEmpty(date)) {
            SimpleDateFormat df = new SimpleDateFormat(PATTERN_DATE + PATTERN_SPLIT + PATTERN_TIME);
            date = df.format(new Date());
        }
        Calendar calendar = str2calendar(date);
        calendar.add(Calendar.MONTH, moonCount);
        return getDate(calendar2str(calendar));
    }

    /**
     * 计算天数差
     */
    public static int calculateDayDiff(Date targetTime, Date compareTime) {
        boolean sameYear = isSameYear(targetTime, compareTime);
        if (sameYear) {
            return calculateDayDiffOfSameYear(targetTime, compareTime);
        } else {
            int dayDiff = 0;

            // 累计年数差的整年天数
            int yearDiff = calculateYearDiff(targetTime, compareTime);
            dayDiff += yearDiff * DAY_OF_YEAR;

            // 累计同一年内的天数
            dayDiff += calculateDayDiffOfSameYear(targetTime, compareTime);

            return dayDiff;
        }
    }

    /**
     * 计算同一年内的天数差
     */
    public static int calculateDayDiffOfSameYear(Date targetTime, Date compareTime) {
        if (targetTime == null || compareTime == null) {
            return 0;
        }

        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }

    /**
     * 计算年数差
     */
    public static int calculateYearDiff(Date targetTime, Date compareTime) {
        if (targetTime == null || compareTime == null) {
            return 0;
        }

        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear - comYear;
    }

    /**
     * 计算月数差
     *
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static int calculateMonthDiff(String targetTime, String compareTime) {
        return calculateMonthDiff(str2date(targetTime, PATTERN_DATE),
                str2date(compareTime, PATTERN_DATE));
    }

    /**
     * 计算月数差
     *
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static int calculateMonthDiff(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);
        int tarMonth = tarCalendar.get(Calendar.MONTH);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);
        int comMonth = compareCalendar.get(Calendar.MONTH);
        return ((tarYear - comYear) * 12 + tarMonth - comMonth);

    }

    /**
     * 是否为同一年
     */
    public static boolean isSameYear(Date targetTime, Date compareTime) {
        if (targetTime == null || compareTime == null) {
            return false;
        }

        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }

    public static Date str2date(String str, String format) {
        Date date = null;
        try {
            if (str != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                date = sdf.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date str2date(String str) {
        return str2date(str, PATTERN);
    }

    public static String date2str(Date date) {
        return date2str(date, PATTERN);
    }

    public static String date2str(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    public static Calendar str2calendar(String str) {
        Calendar calendar = null;
        Date date = str2date(str);
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        return calendar;
    }


    public static Calendar str2calendar(String str, String format) {
        Calendar calendar = null;
        Date date = str2date(str, format);
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        return calendar;
    }

    public static String calendar2str(Calendar calendar) {
        return date2str(calendar.getTime());
    }

    public static String calendar2str(Calendar calendar, String format) {
        return date2str(calendar.getTime(), format);
    }

}
