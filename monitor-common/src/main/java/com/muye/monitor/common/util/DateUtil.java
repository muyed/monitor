package com.muye.monitor.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {
    private DateUtil() {
    }

    /**
     * 日期格式
     **/
    public interface DATE_PATTERN {
        String HHMMSS = "HHmmss";
        String HH_MM_SS = "HH:mm:ss";
        String YYYYMMDD = "yyyyMMdd";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static final String format(Object date) {
        return format(date, DATE_PATTERN.YYYY_MM_DD);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static final String getDate() {
        return format(new Date());
    }

    /**
     * 获取日期时间
     *
     * @return
     */
    public static final String getDateTime() {
        return format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * 日期计算
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static final Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 根据计算今天与传过来的String类型的字符串相隔天数
     *
     * @param dateStr the date str
     * @return int int
     * @see [相关类/方法]（可选）
     * @since [产品 /模块版本] （可选）
     */
    public static int calculateNumberOfDays(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN.YYYY_MM_DD);
        try {
            Date date = sdf.parse(dateStr);
            Date now = new Date();
            long n1 = now.getTime();
            long n2 = date.getTime();
            if (n1 > n2) {
                long diffTime = n1 - n2;
                int numberOfDays = (int) (diffTime / (3600 * 1000 * 24));
                return numberOfDays;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 计算每个月的天数
     *
     * @param year  年份
     * @param month 月份
     * @return days 每个月的天数
     */
    public static int getDaysOfMonth(int year, int month) {
        int days = 0;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 9 || month == 10 || month == 12) {
            days = 31;
        } else if (month == 4 || month == 6 || month == 8 || month == 11) {
            days = 30;
        } else { // 2月份，闰年29天、平年28天
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }

        return days;
    }

    /**
     * 计算一年中的天数
     *
     * @return int int
     * @see [相关类/方法]（可选）
     * @since [产品 /模块版本] （可选）
     */
    public static int calculateDays() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    /**
     * 计算两个时间相差的天数 输入时间格式: yyyy-MM-dd
     *
     * @param start the start
     * @param end   the end
     * @return day diff
     * @throws Exception the exception
     */
    public static int getDayDiff(String start, String end) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startTime = df.parse(start);
            Date endTime = df.parse(end);
            long interval = endTime.getTime() - startTime.getTime();
            if (interval < 0) {
                throw new Exception("开始时间 start > 终止时间 end");
            }
            int day = (int) (interval / (24 * 60 * 60 * 1000));
            return day;
        } catch (ParseException e) {
            throw new Exception("时间格式应为 yyyy-MM-dd");
        }
    }

    /**
     * 计算两个时间相差的秒数 输入时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param start the start
     * @param end   the end
     * @return seconds diff
     * @throws Exception the exception
     */
    public static int getSecondsDiff(String start, String end) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = df.parse(start);
            Date endTime = df.parse(end);
            long interval = endTime.getTime() - startTime.getTime();
            if (interval < 0) {
                throw new Exception("开始时间 start > 终止时间 end");
            }
            int minute = (int) (interval / (1000));
            return minute;
        } catch (ParseException e) {
            throw new Exception("时间格式应为 yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 计算两个时间相差的分钟数 输入时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param start the start
     * @param end   the end
     * @return minute diff
     * @throws Exception the exception
     */
    public static int getMinuteDiff(String start, String end) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = df.parse(start);
            Date endTime = df.parse(end);
            long interval = endTime.getTime() - startTime.getTime();
            if (interval < 0) {
                throw new Exception("开始时间 start > 终止时间 end");
            }
            int minute = (int) (interval / (60 * 1000));
            return minute;
        } catch (ParseException e) {
            throw new Exception("时间格式应为 yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 根据给定的格式取当前时间。
     * <p>
     * 如果给定的格式为空，则使用默认格式：yyyy-MM-dd HH:mm:ss。
     *
     * @param pattern 指定格式
     * @return 字符串表示的当前时间 date time
     */
    public static String getDateTime(String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dt = sdf.format(new Date());
        return dt;
    }

    /**
     * 字符串转换为日期:不支持yyM[M]d[d]格式
     *
     * @param date
     * @return
     */
    public static final Date stringToDate(String date) {
        if (date == null) {
            return null;
        }
        String separator = String.valueOf(date.charAt(4));
        String pattern = "yyyyMMdd";
        if (!separator.matches("\\d*")) {
            pattern = "yyyy" + separator + "MM" + separator + "dd";
            if (date.length() < 10) {
                pattern = "yyyy" + separator + "M" + separator + "d";
            }
            pattern += " HH:mm:ss.SSS";
        } else if (date.length() < 8) {
            pattern = "yyyyMd";
        } else {
            pattern += "HHmmss.SSS";
        }
        pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转date类型
     *
     * @param date    the date
     * @param pattern the pattern
     * @return date date
     * @throws ParseException the parse exception
     * @see [相关类/方法]（可选）
     * @since [产品 /模块版本] （可选）
     */
    public static Date toDate(String date, String pattern) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(date);
    }

    /**
     * 间隔秒
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getBetween(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        long n = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (n / 1000L);
    }

    /**
     * 间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getDayBetween(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        long n = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (n / (60 * 60 * 24 * 1000l));
    }

    /**
     * 间隔月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n = (year2 - year1) * 12;
        n = n + month2 - month1;
        return n;
    }

    /**
     * 间隔月，多一天就多算一个月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n = (year2 - year1) * 12;
        n = n + month2 - month1;
        int day1 = start.get(Calendar.DAY_OF_MONTH);
        int day2 = end.get(Calendar.DAY_OF_MONTH);
        if (day1 <= day2) {
            n++;
        }
        return n;
    }

    /**
     * 时间int值
     *
     * @param date
     * @return
     */
    public static Long dateTransformInteger(Date date) {
        Long longDate = date.getTime();
        return longDate;

    }

    /**
     * int 时间转 Date
     *
     * @param integerDate
     * @return
     */
    public static Date integerTransformDate(Long integerDate) {
        try {
            long nowTimeLong = new Long(integerDate).longValue();
            DateFormat ymdhmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTimeStr = ymdhmsFormat.format(nowTimeLong);
            return ymdhmsFormat.parse(nowTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * int 时间转 Date
     *
     * @param integerDate
     * @return
     */
    public static String stringTransformInterDate(Long integerDate) {
        try {
            long nowTimeLong = new Long(integerDate).longValue();
            DateFormat ymdhmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTimeStr = ymdhmsFormat.format(nowTimeLong);
            return nowTimeStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测日期是否是预期格式
     *
     * @param strTime the str time
     * @param pattern the pattern
     * @return boolean boolean
     * @see [相关类/方法]（可选）
     * @since [产品 /模块版本] （可选）
     */
    public static boolean checkDateFormat(String strTime, String pattern) {
        if (strTime.length() != pattern.trim().length()) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern.trim());
            sdf.parse(strTime);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前时间戳
     *
     * @return timestamp timestamp
     */
    public static long getTimestamp() {
        Date dateNow = new Date();
        return dateNow.getTime();
    }

    /**
     * * 功能描述:UTC 时间格式转换 <br>
     * 〈功能详细描述〉
     * <p>
     * Utc 2 local string.
     *
     * @param utcTime         the utc time
     * @param utcTimePatten   the utc time patten
     * @param localTimePatten the local time patten
     * @return the string
     * @see [相关类/方法]（可选）
     * @since [产品 /模块版本] （可选）
     */
    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    public static String ymdToDash(String ymd){
        return ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-" + ymd.substring(6);
    }

    /**
     * 获取指定周第一天的日期
     * @param year
     * @param week
     * @return
     */
    public static Date getWeekFirstDay(Integer year, Integer week){
        Calendar cal = Calendar.getInstance();

        if (week < 1) {
            week = 1;
        }

        if (week > 52) {
            week = 52;
        }

        //周一为本周第一天
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        //如果在前一年则加一天 直到到本年
        for (;;) {
            if (cal.get(Calendar.YEAR) < year) {
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }

            return cal.getTime();
        }
    }

    /**
     * 获取指定周最后一天的日期
     * @param year
     * @param week
     * @return
     */
    public static Date getWeekLastDay(Integer year, Integer week){
        Calendar cal = Calendar.getInstance();

        if (week < 1) {
            week = 1;
        }

        if (week > 52) {
            week = 52;
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 7);

        for (;;) {
            if (cal.get(Calendar.YEAR) > year) {
                cal.add(Calendar.DAY_OF_WEEK, -1);
            }
            return cal.getTime();
        }
    }

    public static Date getMonthFirstDay(Integer year, Integer month){
        Calendar cal = Calendar.getInstance();

        if (month < 1) {
            month = 1;
        }

        if (month > 12) {
            month = 12;
        }

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);

        return cal.getTime();
    }

    public static Date getMonthLastDay(Integer year, Integer month){
        Calendar cal = Calendar.getInstance();

        if (month < 1) {
            month = 1;
        }

        if (month > 12) {
            month = 12;
        }

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }
}
