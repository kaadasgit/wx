package com.chason.wx.common.util;


import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期时间工具类 1.日常时间类java.util.Date,java.util.Calendar和String,int的转换 ;
 * 2.数据库使用类java.sql.Date,Time,Timestamp和String,int的转换
 *
 * @author Alex
 */
public class DateTimeUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String MM_DD = "MM-dd";

    public static final String HH_MM = "HH:mm";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String[] NUM_ARRAY = {"一", "二", "三", "四", "五", "六", "日"};

    /**
     * 将Date格式的值转换成：刚刚、n分钟前、n天前等
     *
     * @param date
     * @return
     */
    public static String dateTransferStr(Date date) {
        if (null == date)
            return "";

        long transferTime = date.getTime();
        long distanceTime = System.currentTimeMillis() - transferTime;
        if (0 == distanceTime)
            return "刚刚";

        long seconds = distanceTime / 1000;
        if (seconds < 60)
            return "刚刚";

        Long minute = seconds / 60;
        if (minute < 60)
            return minute.intValue() + "分钟前";

        Long hour = minute / 60;
        if (hour < 24)
            return hour.intValue() + "小时前";

        Long day = hour / 24;
        return day.intValue() + "天前";
    }

    /**
     * 返回当前时间的Date
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 字符串转为时间,字符串符合标准格式:"YYYY-MM-DD HH:MM:SS"
     *
     * @param dateTime 标准时间格式 "YYYY-MM-DD HH:MM:SS"
     * @return java.util.Date
     */
    public static Date toDate(String dateTime) {
        int index = dateTime.indexOf(" ");
        String date = dateTime.substring(0, index);
        String time = dateTime.substring(index + 1);

        return toDate(date, time);
    }

    /**
     * 字符串转为时间,字符串符合标准格式:"YYYY-MM-DD HH:MM:SS"
     *
     * @param dateTime 标准时间格式 "YYYY-MM-DD HH:MM:SS"
     * @return java.util.Date
     */
    public static Date toDate(Long dateTime) {
        return new Date(dateTime);
    }

    /**
     * 字符串转为时间,字符串符合标准日期格式:"YYYY-MM-DD",和标准时间格式:"HH:MM:SS"
     *
     * @param date 标准日期格式 "YYYY-MM-DD"
     * @param time 标准时间格式 "HH:MM:SS"
     * @return java.util.Date
     */
    public static Date toDate(String date, String time) {
        if (date == null || time == null)
            return null;

        int dateSlash1 = date.indexOf("-");
        int dateSlash2 = date.lastIndexOf("-");

        if (dateSlash1 <= 0 || dateSlash1 == dateSlash2)
            return null;

        int timeColon1 = time.indexOf(":");
        int timeColon2 = time.lastIndexOf(":");

        if (timeColon1 <= 0 || timeColon1 == timeColon2)
            return null;

        String year = date.substring(0, dateSlash1);
        String month = date.substring(dateSlash1 + 1, dateSlash2);
        String day = date.substring(dateSlash2 + 1);

        String hour = time.substring(0, timeColon1);
        String minute = time.substring(timeColon1 + 1, timeColon2);
        String second = time.substring(timeColon2 + 1);

        return toDate(year, month, day, hour, minute, second);
    }

    /**
     * 通过标准时间输入,年,月,日,时,分,秒,生成java.util.Date
     *
     * @param yearStr   年
     * @param monthStr  月
     * @param dayStr    日
     * @param hourStr   时
     * @param minuteStr 分
     * @param secondStr 秒
     * @return java.util.Date
     */
    public static Date toDate(String yearStr, String monthStr, String dayStr, String hourStr, String minuteStr, String secondStr) {
        int year, month, day, hour, minute, second;

        try {
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (Exception e) {
            return null;
        }
        return toDate(year, month, day, hour, minute, second);
    }

    /**
     * 通过标准时间输入,年,月,日,时,分,秒,生成java.util.Date
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return java.util.Date
     */
    public static Date toDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.set(year, month - 1, day, hour, minute, second);
        } catch (Exception e) {
            return null;
        }
        return calendar.getTime();
    }

    /**
     * 生成标准格式的字符串 格式为: "MM-DD-YYYY HH:MM:SS"
     *
     * @param date The Date
     * @return 生成默认格式的字符串 格式为: "MM-DD-YYYY HH:MM:SS"
     */
    public static String toDateTimeString(Date date) {
        if (date == null)
            return "";

        String dateString = toDateString(date);
        String timeString = toTimeString(date);

        if (dateString == null || timeString == null)
            return "";

        return dateString + " " + timeString;
    }

    /**
     * 生成标准日期,格式为 YYYY+spe+MM+spe+DD
     *
     * @param date The Date
     * @return 生成日期, 格式为 YYYY+spe+MM+spe+DD
     */
    public static String toDateString(Date date, String spe) {
        if (date == null)
            return "";

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String monthStr = "" + month;
        String dayStr = "" + day;
        String yearStr = "" + year;

        if (month < 10)
            monthStr = "0" + month;

        if (day < 10)
            dayStr = "0" + day;

        return yearStr + spe + monthStr + spe + dayStr;
    }


    public static String toDateMonth(Date date, String spe) {
        if (date == null)
            return "";

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String monthStr = "" + month;
        String dayStr = "" + day;
        String yearStr = "" + year;

        if (month < 10)
            monthStr = "0" + month;

        if (day < 10)
            dayStr = "0" + day;

        return yearStr + spe + monthStr;
    }

    public static String toDateMonth(Date date) {
        return toDateMonth(date, "-");
    }

    public static String toDateYear(Date date) {
        if (date == null)
            return "";

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String monthStr = "" + month;
        String dayStr = "" + day;
        String yearStr = "" + year;

        if (month < 10)
            monthStr = "0" + month;

        if (day < 10)
            dayStr = "0" + day;

        return yearStr;
    }

    /**
     * 生成标准日期,格式为 YYYY-MM-DD
     *
     * @param date The Date
     * @return 生成日期, 格式为 YYYY-MM-DD
     */
    public static String toDateString(Date date) {
        return toDateString(date, "-");
    }

    /**
     * 根据输入的时间,生成时间格式 HH:MM:SS
     *
     * @param date java.util.Date
     * @return 生成时间格式为 HH:MM:SS
     */
    public static String toTimeString(Date date) {
        if (date == null)
            return "";

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return toTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    /**
     * 根据输入的时,分,秒,生成时间格式 HH:MM:SS
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return 生成时间格式为 HH:MM:SS
     */
    public static String toTimeString(int hour, int minute, int second) {
        String hourStr = "" + hour;
        String minuteStr = "" + minute;
        String secondStr = "" + second;

        if (hour < 10)
            hourStr = "0" + hour;

        if (minute < 10)
            minuteStr = "0" + minute;

        if (second < 10)
            secondStr = "0" + second;

        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    /**
     * 取得给定日历,给定格式的日期时间字符串
     *
     * @param calendar 日历,给定一个日历
     * @return String 取得默认的日期时间字符串"yyyy-MM-dd HH:mm:ss"
     */
    public static String toDateTimeString(Calendar calendar) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 取得给定日历,给定格式的日期时间字符串
     *
     * @param calendar 日历,给定一个日历
     * @param format   格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定日历,给定格式的日期时间字符串
     */
    public static String toDateTimeString(Calendar calendar, String format) {
        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串,标准格式:"yyyy-MM-dd HH:mm:ss";
     *
     * @param datetime 日期,给定一个时间的毫秒数
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(long datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        String format = "yyyy-MM-dd HH:mm:ss";
        sdf.applyPattern(format);
        return sdf.format(new Date(datetime));
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串
     *
     * @param datetime 日期,给定一个时间的毫秒数
     * @param format   格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(long datetime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        return sdf.format(new Date(datetime));
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串
     *
     * @param date   日期,给定一个时间
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        return sdf.format(date);
    }

    /**
     * 取得当前的日期时间字符串
     *
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTimeString(String format) {
        return toDateTimeString(new Date(), format);
    }

    /**
     * 取得当前的日期时间字符串
     *
     * @param date   时间
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTimeString(Date date, String format) {
        return toDateTimeString(date, format);
    }

    /**
     * 取得当前的日期时间字符串YYYY-MM-DD HH:mm:ss
     *
     * @return String 取得当前的日期时间字符串YYYY-MM-DD HH:mm:ss
     */
    public static String getDateTimeString() {
        String format = "yyyy-MM-dd HH:mm:ss";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYY/MM/DD HH:mm:ss (移动)
     *
     * @return String 取得当前的日期时间字符串YYYY/MM/DD HH:mm:ss
     */
    public static String getDateTimeString2() {
        String format = "yyyy/MM/dd HH:mm:ss";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYY/MM/DD (移动)
     *
     * @return String 取得当前的日期时间字符串YYYY/MM/DD
     */
    public static String getDateString2() {
        String format = "yyyy/MM/dd";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYYMMDDHHMISS
     *
     * @return String 取得当前的日期时间字符串YYYYMMDDHHMISS
     */
    public static String getDateTime14String() {
        String format = "yyyyMMddHHmmss";
        return getDateTimeString(format);
    }

    public static String to14StringFromDate(Date date) {
        String format = "yyyyMMddHHmmss";
        return toDateTimeString(date, format);
    }

    /**
     * 根据时间戳转换14时间
     * @param date
     * @return
     */
    public static String to14StringFromTimestamp(int date){
        String format = "yyyyMMddHHmmss";
        return toDateTimeString(new Date(date), format);
    }

    /**
     * 取得当前的日期时间字符串YYYYMMDDHHMISS
     *
     * @return String 取得当前的日期时间字符串YYYYMMDDHHMISS
     */
    public static Date toDateTimeFrom14String(String dateTimeString) {
        try {
            String format = "yyyyMMddHHmmss";
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format);
            return sdf.parse(dateTimeString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取得当前的日期时间字符串YYMMDDHHMISS
     *
     * @return String 取得当前的日期时间字符串YYMMDDHHMISS
     */
    public static String getDateTime12String() {
        String format = "yyMMddHHmmss";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYYMMDD
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTime8String() {
        String format = "yyyyMMdd";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYYMM
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTime4String() {
        String format = "yyyyMM";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYY-MM-DD
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateString() {
        String format = "yyyy-MM-dd";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串HH:mm:ss
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getTimeString() {
        String format = "HH:mm:ss";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期整型数组共7项,分别为年,月,日,时,分,秒,毫秒
     *
     * @return int[] 共7项,分别为年,月,日,时,分,秒,毫秒
     */
    public static int[] getDateTimes() {
        int[] dates = new int[7];
        Calendar calendar = Calendar.getInstance();
        dates[0] = calendar.get(Calendar.YEAR);
        dates[1] = calendar.get(Calendar.MONTH) + 1;
        dates[2] = calendar.get(Calendar.DAY_OF_MONTH);
        dates[3] = calendar.get(Calendar.HOUR_OF_DAY);
        dates[4] = calendar.get(Calendar.MINUTE);
        dates[5] = calendar.get(Calendar.SECOND);
        dates[6] = calendar.get(Calendar.MILLISECOND);
        return dates;
    }

    /**
     * 通过标准时间输入,年,月,日,时,分,秒,生成java.util.Date
     *
     * @param yearStr   年
     * @param monthStr  月
     * @param dayStr    日
     * @param hourStr   时
     * @param minuteStr 分
     * @param secondStr 秒
     * @return Calendar
     */
    public static Calendar toCalendar(String yearStr, String monthStr, String dayStr, String hourStr, String minuteStr, String secondStr) {
        int year, month, day, hour, minute, second;

        try {
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (Exception e) {
            return null;
        }

        return toCalendar(year, month, day, hour, minute, second);
    }

    /**
     * 通过整型数组,组织一个日历
     *
     * @param dates
     * @return 通过整型数组, 返回一个日历
     */
    public static Calendar toCalendar(int[] dates) {
        if (dates == null || dates.length < 6)
            return null;

        return toCalendar(dates[0], dates[1], dates[2], dates[3], dates[4], dates[5]);
    }

    /**
     * 通过标准时间输入,年,月,日,时,分,秒,生成java.util.Date
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return Calendar
     */
    public static Calendar toCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DATE, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);

        return c;
    }

    /**
     * 通过整型数组,组织一个日期
     *
     * @param dates
     * @return 通过整型数组, 组织一个日期
     */
    public static Date toDate(int[] dates) {
        if (dates == null || dates.length < 6)
            return null;

        return toCalendar(dates).getTime();
    }

    /**
     * 取得当前的日期时间
     *
     * @param str    字符串
     * @param format 格式
     * @return 取得当前的日期时间 如果格式不对则返回null
     */
    public static Date toDateFromStr(String str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format);
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 取得当前的日期时间 按默认格式YYYY-MM-DD HH:mm:ss不对则返回null
     *
     * @param str 字符串
     * @return 取得当前的日期时间 按默认格式不对则返回null
     */
    public static Date toDateFromStr(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            String format = "yyyy-MM-dd HH:mm:ss";
            sdf.applyPattern(format);
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前年
     *
     * @return 当前年
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return 月份
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取当前时
     *
     * @return 当前时间，如:23点,0点,1点等
     */
    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return hour;
    }

    /**
     * 获取当前分
     *
     * @return 当前分
     */
    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.MINUTE);

        return hour;
    }

    /**
     * 获取当前时间的星期数:星期日=7;星期一=1;星期二=2;星期三=3;星期四=4;星期五=5;星期六=6;
     *
     * @return 周数值
     */
    public static int getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        week = week - 1;
        if (week == 0)
            week = 7;

        return week;
    }

    /**
     * 获取当前时间的星期数:星期日=7;星期一=1;星期二=2;星期三=3;星期四=4;星期五=5;星期六=6;
     *
     * @return 周数值
     */
    public static String getStringCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        week = week - 1;
        if (week == 0)
            week = 7;
        return "星期" + NUM_ARRAY[week - 1];
    }

    /**
     * 获取两个日期对象相差年数, date1 - date2
     *
     * @param date1 日期对象1
     * @param date2 日期对象2
     * @return 返回date1 - date2的整数部分
     */
    public static int compareYear(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);

        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);

        return year1 - year2;
    }

    /**
     * 获取两个日期对象相差月数
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return int 月份差值
     */
    public static int compareMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        int year = compareYear(date1, date2);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int month1 = calendar.get(Calendar.MONTH);

        calendar.setTime(date2);
        int month2 = calendar.get(Calendar.MONTH);

        /* 进行比较 */
        return 12 * year + (month1 - month2);

    }

    /**
     * 获取两个日期对象相差天数
     *
     * @param date1str String yyyy-MM-dd
     * @param date2str String yyyy-MM-dd
     * @return int 返回两个日期对象相差天数，date1str - date2str的整数部分
     */
    public static int compareDay(String date1str, String date2str) {
        if (date1str == null || date2str == null)
            return 0;

        Date date1 = toDate(date1str, "00:00:00");
        Date date2 = toDate(date2str, "00:00:00");

        return compareDay(date1, date2);
    }

    /**
     * 获取两个日期对象相差天数
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return int 日差值
     */
    public static int compareDay(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        long margin = time1 - time2;

        /* 转化成天数 */
        // int ret = (int)Math.floor((double)margin / (1000 * 60 * 60 * 24));
        int ret = (int) ((double) margin / (1000 * 60 * 60 * 24));

        return ret;
    }

    /**
     * 获取两个日期对象相差的小时数，只取整数部分
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return int 相差小时数
     */
    public static int compareHour(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        long margin = time1 - time2;

        int ret = (int) ((double) margin / (1000 * 60 * 60));
        // if (margin >= 0)
        // ret = (int)Math.floor((double)margin / (1000 * 60 * 60));
        // else
        // ret = (int)Math.ceil((double)margin / (1000 * 60 * 60));

        return ret;
    }

    /**
     * 获取两个日期对象相差的分钟数，只取整数部分
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return int 相差分钟数
     */
    public static int compareMinute(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        long margin = time1 - time2;

        int ret = (int) ((double) margin / (1000 * 60));
        // if (margin >= 0)
        // ret = (int)Math.floor((double)margin / (1000 * 60));
        // else
        // ret = (int)Math.ceil((double)margin / (1000 * 60));

        return ret;
    }

    /**
     * 获取两个日期对象相差秒数
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return int 相差秒数
     */
    public static int compareSecond(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        long margin = time1 - time2;

        Long longValue = new Long(margin / (1000));

        return longValue.intValue();
    }

    /**
     * 获取和当前时间毫秒差值
     *
     * @param dateTime YYYY-MM-DD hh:mm:ss
     * @return 毫秒差
     */
    public static long getTimeMargin(String dateTime) {
        int index = dateTime.indexOf(" ");
        String date = dateTime.substring(0, index);
        String time = dateTime.substring(index + 1);

        int dateSlash1 = date.indexOf("-");
        int dateSlash2 = date.lastIndexOf("-");

        if (dateSlash1 <= 0 || dateSlash1 == dateSlash2)
            return -1;

        int timeColon1 = time.indexOf(":");
        int timeColon2 = time.lastIndexOf(":");

        if (timeColon1 <= 0 || timeColon1 == timeColon2)
            return -1;

        Calendar calendar = Calendar.getInstance();

        try {
            int year = Integer.parseInt(date.substring(0, dateSlash1));
            int month = Integer.parseInt(date.substring(dateSlash1 + 1, dateSlash2));
            int day = Integer.parseInt(date.substring(dateSlash2 + 1));

            int hour = Integer.parseInt(time.substring(0, timeColon1));
            int minute = Integer.parseInt(time.substring(timeColon1 + 1, timeColon2));
            int second = Integer.parseInt(time.substring(timeColon2 + 1));

            calendar.set(year, month - 1, day, hour, minute, second);
        } catch (Exception e) {
            return -1;
        }

        return System.currentTimeMillis() - calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间的前一天或数天的年、月、日，并以数组形式还回。 数组0为年；1为月；2为日
     *
     * @param year  当前年
     * @param month 当前月
     * @param day   当前日期
     * @param days  相差天数
     * @return 年、月、日数组
     */
    public static int[] getPreviousDay(int year, int month, int day, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        long longDate = (calendar.getTime()).getTime() - (1000L * 60L * 60L * 24L * days);
        Date date = new Date(longDate);
        calendar.setTime(date);

        int[] rtn = new int[3];
        rtn[0] = calendar.get(Calendar.YEAR);
        rtn[1] = calendar.get(Calendar.MONTH) + 1;
        rtn[2] = calendar.get(Calendar.DATE);

        return rtn;
    }

    /**
     * 获取前一天对应的当前时间
     *
     * @param format 格式化如 yyyy-MM-dd
     * @return String
     */
    public static String getPreviousDateString(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return toDateTimeString(calendar, format);
    }

    /**
     * 获取前一天对应的当前时间,采用标准格式yyyy-MM-dd
     *
     * @return String
     */
    public static String getPreviousDateString() {

        return getPreviousDateString("yyyy-MM-dd");
    }

    /**
     * 获取前一天对应的当前时间,采用短信格式yyyy/MM/dd
     *
     * @return String
     */
    public static String getPreviousDateString2() {

        return getPreviousDateString("yyyy/MM/dd");
    }

    /**
     * 获取前一天对应的当前时间
     *
     * @param format 格式化如 yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getPreviousDateTimeString(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return toDateTimeString(calendar, format);
    }

    /**
     * 获取前三天对应的当前时间
     *
     * @param format 格式化如 yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getPrevious2DateTimeString(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);

        return toDateTimeString(calendar, format);
    }

    /**
     * 获取前三天对应的当前时间,采用标准格式yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getPrevious2DateTimeString() {

        return getPrevious2DateTimeString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取前一天对应的当前时间,采用标准格式yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getPreviousDateTimeString() {

        return getPreviousDateTimeString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取前一天对应的当前时间,采用短信格式yyyy/MM/dd HH:mm:ss
     *
     * @return String
     */
    public static String getPreviousDateTimeString2() {

        return getPreviousDateTimeString("yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 获取后一天的Date String
     *
     * @param spe 分隔符
     * @return YYYY+spe+MM+spe+DD
     */
    public static String getNextDateStr(String spe) {
        Calendar calendar = Calendar.getInstance();

        long longDate = (calendar.getTime()).getTime() + (1000 * 60 * 60 * 24 * 1);
        Date date = new Date(longDate);
        calendar.setTime(date);

        return toDateString(calendar.getTime(), spe);
    }

    /**
     * 获取后一天的Date String
     *
     * @param format 格式化
     * @return YYYY+spe+MM+spe+DD
     */
    public static String getNextDateTimeStr(String format) {
        Calendar calendar = Calendar.getInstance();

        long longDate = (calendar.getTime()).getTime() + (1000 * 60 * 60 * 24 * 1);
        Date date = new Date(longDate);
        calendar.setTime(date);

        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 获取下一天的年、月、日数组
     *
     * @return 获取下一天的年、月、日数组
     */
    public static int[] getNextDay() {
        Calendar calendar = Calendar.getInstance();

        long longDate = (calendar.getTime()).getTime() + (1000 * 60 * 60 * 24 * 1);
        Date date = new Date(longDate);
        calendar.setTime(date);

        int[] rtn = new int[3];
        rtn[0] = calendar.get(Calendar.YEAR);
        rtn[1] = calendar.get(Calendar.MONTH) + 1;
        rtn[2] = calendar.get(Calendar.DATE);

        return rtn;
    }

    /**
     * 获取当前时间的后一天或数天的年、月、日，并以数组形式还回。 数组0为年；1为月；2为日
     *
     * @param year  当前年
     * @param month 当前月
     * @param day   当前日期
     * @param days  相差天数
     * @return 年、月、日数组
     */
    public static int[] getNextDay(int year, int month, int day, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        long longDate = (calendar.getTime()).getTime() + (1000L * 60L * 60L * 24L * days);
        Date date = new Date(longDate);
        calendar.setTime(date);

        int[] rtn = new int[3];
        rtn[0] = calendar.get(Calendar.YEAR);
        rtn[1] = calendar.get(Calendar.MONTH) + 1;
        rtn[2] = calendar.get(Calendar.DATE);

        return rtn;
    }

    /**
     * 获取指定时间所在周的第一天的时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 年、月、日数组
     */
    public static int[] getDayOfWeek(int year, int month, int day) {
        int[] rtn = new int[6];
        int week = 0;
        long longDate = 0;

        Date date = null;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        week = calendar.get(Calendar.DAY_OF_WEEK);
        longDate = (calendar.getTime()).getTime() - (60L * 1000L * 60L * 24L * (week - 1));
        date = new Date(longDate);
        calendar1.setTime(date);

        rtn[0] = calendar1.get(Calendar.YEAR);
        rtn[1] = calendar1.get(Calendar.MONTH) + 1;
        rtn[2] = calendar1.get(Calendar.DATE);

        longDate = (calendar.getTime()).getTime() + (60L * 1000L * 60L * 24L * (7 - week));
        date = new Date(longDate);
        calendar2.setTime(date);
        rtn[3] = calendar2.get(Calendar.YEAR);
        rtn[4] = calendar2.get(Calendar.MONTH) + 1;
        rtn[5] = calendar2.get(Calendar.DATE);

        return rtn;
    }

    /*********************************************************/
    // 以下为数据库使用的日期方法,Timestamp ,java.sql.Date
    /*********************************************************/

    /**
     * 返回当前时间的Timestamp
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 返回从当日开始的Timestamp
     */
    public static Timestamp getDayStart(Timestamp stamp) {
        return getDayStart(stamp, 0);
    }

    /**
     * 返回多少天后开始的Timestamp
     */
    public static Timestamp getDayStart(Timestamp stamp, int daysLater) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 返回下一天开始的Timestamp
     */
    public static Timestamp getNextDayStart(Timestamp stamp) {
        return getDayStart(stamp, 1);
    }

    /**
     * 返回从当日结束的Timestamp
     */
    public static Timestamp getDayEnd(Timestamp stamp) {
        return getDayEnd(stamp, 0);
    }

    /**
     * 返回从多少日后结束的Timestamp
     */
    public static Timestamp getDayEnd(Timestamp stamp, int daysLater) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * String到java.sql.Date的转换 标准格式:YYYY-MM-DD
     *
     * @param date The date String
     * @return java.sql.Date
     */
    public static java.sql.Date toSqlDate(String date) {
        Date newDate = toDate(date, "00:00:00");

        if (newDate == null)
            return null;

        return new java.sql.Date(newDate.getTime());
    }

    /**
     * 生成java.sql.Date,通过传入year, month, day
     *
     * @param yearStr  年
     * @param monthStr 月
     * @param dayStr   日
     * @return A java.sql.Date
     */
    public static java.sql.Date toSqlDate(String yearStr, String monthStr, String dayStr) {
        Date newDate = toDate(yearStr, monthStr, dayStr, "0", "0", "0");

        if (newDate == null)
            return null;

        return new java.sql.Date(newDate.getTime());
    }

    /**
     * 生成java.sql.Date,通过传入year, month, day
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return A java.sql.Date
     */
    public static java.sql.Date toSqlDate(int year, int month, int day) {
        Date newDate = toDate(year, month, day, 0, 0, 0);

        if (newDate == null)
            return null;

        return new java.sql.Date(newDate.getTime());
    }

    /**
     * 转换String 到 java.sql.Time,格式:"HH:MM:SS"
     *
     * @param time The time String
     * @return A java.sql.Time
     */
    public static java.sql.Time toSqlTime(String time) {
        Date newDate = toDate("1970-1-1", time);

        if (newDate == null)
            return null;

        return new java.sql.Time(newDate.getTime());
    }

    /**
     * 生成 java.sql.Time 通过输入时,分,秒
     *
     * @param hourStr   时
     * @param minuteStr 分
     * @param secondStr 秒
     * @return A java.sql.Time
     */
    public static java.sql.Time toSqlTime(String hourStr, String minuteStr, String secondStr) {
        Date newDate = toDate("0", "0", "0", hourStr, minuteStr, secondStr);

        if (newDate == null)
            return null;

        return new java.sql.Time(newDate.getTime());
    }

    /**
     * 生成 java.sql.Time 通过输入时,分,秒
     *
     * @param hour   int 时
     * @param minute int 分
     * @param second 秒
     * @return A java.sql.Time
     */
    public static java.sql.Time toSqlTime(int hour, int minute, int second) {
        Date newDate = toDate(0, 0, 0, hour, minute, second);

        if (newDate == null)
            return null;

        return new java.sql.Time(newDate.getTime());
    }

    /**
     * 转换String 到 java.sql.Timestamp,格式:"YYYY-MM-DD HH:MM:SS"
     *
     * @param dateTime 格式:"YYYY-MM-DD HH:MM:SS"
     * @return Timestamp
     */
    public static Timestamp toTimestamp(String dateTime) {
        Date newDate = toDate(dateTime);

        if (newDate == null)
            return null;

        return new Timestamp(newDate.getTime());
    }

    /**
     * 转换String 到 java.sql.Timestamp,格式:"YYYY-MM-DD HH:MM:SS"
     *
     * @param date The date String: YYYY-MM-DD
     * @param time The time String: HH:MM:SS
     * @return Timestamp
     */
    public static Timestamp toTimestamp(String date, String time) {
        Date newDate = toDate(date, time);

        if (newDate == null)
            return null;

        return new Timestamp(newDate.getTime());
    }

    /**
     * 生成 Timestamp 通过输入年,月,日,时,分,秒
     *
     * @param yearStr   年
     * @param monthStr  月
     * @param dayStr    日
     * @param hourStr   时
     * @param minuteStr 分
     * @param secondStr T秒
     * @return Timestamp
     */
    public static Timestamp toTimestamp(String yearStr, String monthStr, String dayStr, String hourStr, String minuteStr, String secondStr) {
        Date newDate = toDate(yearStr, monthStr, dayStr, hourStr, minuteStr, secondStr);

        if (newDate == null)
            return null;

        return new Timestamp(newDate.getTime());
    }

    /**
     * 生成 Timestamp 通过输入年,月,日,时,分,秒
     *
     * @param year   年 int
     * @param month  月 int
     * @param day    日 int
     * @param hour   时 int
     * @param minute 分 int
     * @param second 秒 int
     * @return Timestamp
     */
    public static Timestamp toTimestamp(int year, int month, int day, int hour, int minute, int second) {
        Date newDate = toDate(year, month, day, hour, minute, second);

        if (newDate == null)
            return null;

        return new Timestamp(newDate.getTime());
    }

    /**
     * 获取当前时间差异开始和结束的时间
     *
     * @param type 1：今天 、2：昨天、3：最近7天、4：最近30天
     * @return Date[0]开始时间，Date[1]结束时间
     */
    public static Date[] getBetweenDate(int type) {
        Date[] dateArry = new Date[2];
        switch (type) {
            case 1:
                dateArry[0] = toDate(getDefinedDayStr(0, "yyyy-MM-dd 00:00:00"));
                dateArry[1] = toDate(getDefinedDayStr(0, "yyyy-MM-dd 23:59:59"));
                break;
            case 2:
                dateArry[0] = toDate(getDefinedDayStr(-1, "yyyy-MM-dd 00:00:00"));
                dateArry[1] = toDate(getDefinedDayStr(-1, "yyyy-MM-dd 23:59:59"));
                break;
            case 3:
                dateArry[0] = toDate(getDefinedDayStr(-6, "yyyy-MM-dd 00:00:00"));
                dateArry[1] = toDate(getDefinedDayStr(0, "yyyy-MM-dd 23:59:59"));
                break;
            case 4:
                dateArry[0] = toDate(getDefinedDayStr(-29, "yyyy-MM-dd 00:00:00"));
                dateArry[1] = toDate(getDefinedDayStr(0, "yyyy-MM-dd 23:59:59"));
                break;
        }
        return dateArry;
    }

    /**
     * 返回一个yyyy-MM-dd 00:00:00
     *
     * @param dateDate :yyyy-MM-dd
     * @return
     */
    public static String getDateMinTimeStr(String dateDate) {
        if (StringUtils.isEmpty(dateDate))
            return "";
        return dateDate + " 00:00:00";
    }

    /**
     * 返回一个yyyy-MM-dd 23:59:59
     *
     * @param dateDate :yyyy-MM-dd
     * @return
     */
    public static String getDateMaxTimeStr(String dateDate) {
        if (StringUtils.isEmpty(dateDate))
            return "";
        return dateDate + " 23:59:59";
    }

    /**
     * 获取当前日期日前后差异的日期
     *
     * @param day    :当前日期前后差异的天数，正负都可以
     * @param format ：时间格式
     * @return
     */
    public static String getDefinedDayStr(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return toDateTimeString(calendar, format);
    }

    /**
     * 获取当前日期日前后差异的日期
     *
     * @param day    :当前日期前后差异的天数，正负都可以
     * @param format ：时间格式
     * @return
     */
    public static Date getDefinedDay(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 获取当前日期小时前后差异的日期
     *
     * @param hour   :当前日期小时前后差异的天数，正负都可以
     * @param format ：时间格式
     * @return
     */
    public static String getDefinedHourStr(int hour, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hour);
        return toDateTimeString(calendar, format);
    }

    /**
     * 获得日期date的星期 (dayOfWeek-1)
     *
     * @param date      :日期
     * @param dayOfWeek ：Calendar中有定义
     * @return
     */
    public static Date getDayOfWeek(Date date, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return calendar.getTime();
    }

    public static Date toDate(Date date, String format) {
        return toDate(toDateTimeString(date, format));
    }

    /**
     * 获取星期*，如返回1：星期日 、2：星期一 、3：星期二 、4：星期三 、5：星期四 、6：星期五 、7：星期六
     *
     * @param date
     * @return
     */
    public static int getWeekDayValue(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getWeekDayDesc(Date date) {
        int dayNum = getWeekDayValue(date);
        String dayDesc = null;
        switch (dayNum) {
            case 1:
                dayDesc = "日";
                break;
            case 2:
                dayDesc = "一";
                break;
            case 3:
                dayDesc = "二";
                break;
            case 4:
                dayDesc = "三";
                break;
            case 5:
                dayDesc = "四";
                break;
            case 6:
                dayDesc = "五";
                break;
            case 7:
                dayDesc = "六";
                break;
        }
        return dayDesc;
    }

    /**
     * 获取指定的日期中日
     *
     * @param date
     * @return
     */
    public static int getMonthDayValue(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将秒换算成HH:mm:ss
     *
     * @param millis
     * @return
     */
    public static String toStringHhMmSs(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(millis * 1000);
    }

    /**
     * @param dateTime yyyy-mm-dd hh:mm:ss
     * @param format
     * @return
     */
    public static String formatStrDatetime(String dateTime, String format) {
        if (StringUtils.isEmpty(dateTime) || StringUtils.isEmpty(format))
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(toDate(dateTime));
    }

    /**
     * 将HH:mm:ss换算成秒
     *
     * @param hhMmSs
     * @return
     */
    public static long toSec(String hhMmSs) {
        if (StringUtils.isEmpty(hhMmSs))
            return 0;
        String[] my = hhMmSs.split(":");
        Long hour = Long.parseLong(my[0]);
        Long min = Long.parseLong(my[1]);
        Long sec = Long.parseLong(my[2]);
        return hour * 3600 + min * 60 + sec;
    }

    public static String toYyyyMMFromStr(String date) {
        if (StringUtils.isEmpty(date))
            return "";
        return date.substring(0, 7).replace("-", "");
    }

    /**
     * 字符串转为时间,字符串符合标准格式:"YYYY-MM-DD"
     *
     * @param date 标准时间格式 "YYYY-MM-DD"
     * @return java.util.Date
     */
    public static Date toDateFromDateStr(String date) {
        return toDate(date + " 00:00:00");
    }

    /**
     * 获取前一天对应的当前时间
     *
     * @param format 格式化如 yyyy-MM-dd
     * @return String
     */
    public static String getPreviousDate(Date date, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return toDateTimeString(calendar, format);
    }

    /**
     * 获取当前时间所在周的周一的日期
     *
     * @return
     */
    public static String getFirstWeekDay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        cal.add(Calendar.DAY_OF_MONTH, -1);// 解决周日会出现 并到下一周的情况
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        return df.format(cal.getTime());
    }

    /**
     * 获取当前时间所在周的周日的日期
     *
     * @return
     */
    public static String getLastWeekDay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        // 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return df.format(cal.getTime());
    }

    /**
     * 获取当前时间上周周一的日期
     *
     * @return
     */
    public static String getFirstDayLastWeek() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        cal.add(Calendar.DAY_OF_MONTH, -1);// 解决周日会出现 并到下一周的情况
        cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return df.format(cal.getTime());
    }

    /**
     * 获取当前时间上周周日的日期
     *
     * @return
     */
    public static String getLastDayLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);// 解决周日会出现 并到下一周的情况
        cal.set(Calendar.DAY_OF_WEEK, 1);
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        return df.format(cal.getTime());
    }

    /**
     * 获取指定时间多少秒后的时间
     *
     * @param dateTime
     * @param second
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeByMarginSecond(String dateTime, int second) {
        Calendar calendar = Calendar.getInstance();
        Date date = toDate(dateTime);
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getDateTimeByMarginSecond("2014-12-31 12:00:00", 24 * 60 * 60 + 10));
        System.out.println(getDateTimeByMarginSecond("2014-12-31 12:00:00", 0));
        System.out.println(getFirstDayLastWeek() + getLastDayLastWeek());
        System.out.println(getFirstWeekDay() + getLastWeekDay());

        System.out.println(getStringCurrentWeek());
    }
}
