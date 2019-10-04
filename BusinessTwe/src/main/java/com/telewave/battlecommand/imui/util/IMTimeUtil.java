package com.telewave.battlecommand.imui.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化处理
 *
 * @author PF-NAN
 * @date 2018/7/22
 */
public class IMTimeUtil {

    /**
     * 格式化时间
     * <p>
     * 一分钟内：刚刚
     * 一小时内：xx分钟前
     * 一天内：HH:mm
     * 一年内：MM-dd HH:mm
     * 一年外：yyyy-MM-dd HH:mm
     *
     * @param formatTimeMillis
     */
    public static String formatTimeA(long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        String formatTimeStr;
        long currentTimeMillis = System.currentTimeMillis();
        if (isSameMinute(currentTimeMillis, formatTimeMillis)) {//一分钟内
            formatTimeStr = "刚刚";
        } else if (isSameHour(currentTimeMillis, formatTimeMillis)) {//一小时内处理
            long millisDifference = Math.abs(currentTimeMillis - formatTimeMillis) / 60000;
            if (millisDifference > 1) {
                formatTimeStr = String.format(Locale.CHINESE, "%d分钟前", millisDifference);
            } else {
                formatTimeStr = "刚刚";
            }
        } else if (isSameDay(currentTimeMillis, formatTimeMillis)) {//同一天处理
            formatTimeStr = formatTime("HH:mm", formatTimeMillis);
        } else if (isSameYear(currentTimeMillis, formatTimeMillis)) {//同一年处理
            formatTimeStr = formatTime("MM-dd HH:mm", formatTimeMillis);
        } else {
            formatTimeStr = formatTime("yyyy-MM-dd HH:mm", formatTimeMillis);
        }
        if (formatTimeStr.startsWith("1970")) {
            formatTimeStr = "";
        }
        return formatTimeStr;
    }

    /**
     * 今天:今天
     * 今天以前:yyyy-MM-dd
     *
     * @param formatTimeMillis
     * @return
     */
    public static String formatTimeB(long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        String formatTimeStr;
        long currentTimeMillis = System.currentTimeMillis();
        if (isSameDay(currentTimeMillis, formatTimeMillis)) {
            formatTimeStr = "今天";
        } else {
            formatTimeStr = formatTime("yyyy.MM.dd", formatTimeMillis);
        }
        if (formatTimeStr.startsWith("1970")) {
            formatTimeStr = "";
        }
        return formatTimeStr;
    }

    /**
     * 今天:今天
     * 今年以前:yyyy年MM月dd日
     * 今年:MM月dd日
     *
     * @param formatTimeMillis
     * @return
     */
    public static String formatTimeC(long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        String formatTimeStr;
        long currentTimeMillis = System.currentTimeMillis();
        if (isSameDay(currentTimeMillis, formatTimeMillis)) {
            formatTimeStr = "今天";
        } else if (isSameYear(currentTimeMillis, currentTimeMillis)) {
            formatTimeStr = formatTime("MM月dd日", formatTimeMillis);
        } else {
            formatTimeStr = formatTime("yyyy年MM月dd日", formatTimeMillis);
        }
        if (formatTimeStr.startsWith("1970")) {
            formatTimeStr = "";
        }
        return formatTimeStr;
    }


    /**
     * yyyy/MM/dd HH:mm
     *
     * @param formatTimeMillis
     * @return
     */
    public static String formatTimeD(long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        String formatTimeStr;
        formatTimeStr = formatTime("yyyy/MM/dd HH:mm", formatTimeMillis);
        if (formatTimeStr.startsWith("1970")) {
            formatTimeStr = "";
        }
        return formatTimeStr;
    }

    /**
     * 格式化时间
     * <p>
     * 一天内：HH:mm
     * 一年内：MM月dd日
     * 一年外：yyyy年
     *
     * @param formatTimeMillis
     */
    public static String formatTimeE(long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        String formatTimeStr;
        long currentTimeMillis = System.currentTimeMillis();
        if (isSameDay(currentTimeMillis, formatTimeMillis)) {//同一天处理
            formatTimeStr = formatTime("HH:mm", formatTimeMillis);
        } else if (isSameYear(currentTimeMillis, formatTimeMillis)) {//同一年处理
            formatTimeStr = formatTime("MM月dd日", formatTimeMillis);
        } else {
            formatTimeStr = formatTime("yyyy年", formatTimeMillis);
        }
        if (formatTimeStr.startsWith("1970")) {
            formatTimeStr = "";
        }
        return formatTimeStr;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param formatTimeMillis
     * @return
     */
    public static String formatTimeF(long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        String formatTimeStr;
        formatTimeStr = formatTime("yyyy-MM-dd HH:mm:ss", formatTimeMillis);
        if (formatTimeStr.startsWith("1970")) {
            formatTimeStr = "";
        }
        return formatTimeStr;
    }

    /**
     * 判断两个时间是否为同一分钟
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMinute(long date1, long date2) {
        date1 = autoComple(date1);
        date2 = autoComple(date2);
        return Math.abs(date1 - date2) / 60000 < 2;
    }

    /**
     * 判断两个时间是否为同一小时
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameHour(long date1, long date2) {
        date1 = autoComple(date1);
        date2 = autoComple(date2);
        return Math.abs(date1 - date2) / 60000 < 60;
    }

    /**
     * 判断两个时间是否为同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(long date1, long date2) {
        date1 = autoComple(date1);
        date2 = autoComple(date2);
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTimeInMillis(date1);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTimeInMillis(date2);
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断两个时间是否为同一年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameYear(long date1, long date2) {
        date1 = autoComple(date1);
        date2 = autoComple(date2);
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTimeInMillis(date1);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTimeInMillis(date2);
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR);
    }

    /**
     * 判断两个时间是否为同一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(long date1, long date2) {
        date1 = autoComple(date1);
        date2 = autoComple(date2);
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTimeInMillis(date1);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTimeInMillis(date2);
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 格式化时间
     *
     * @param model
     * @param formatTimeMillis
     * @return
     */
    public static String formatTime(String model, long formatTimeMillis) {
        formatTimeMillis = autoComple(formatTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(model, Locale.CHINESE);
        return dateFormat.format(new Date(formatTimeMillis));
    }


    /**
     * 获取两个时间之间的间隔,分钟
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getTimeInterval(long time1, long time2) {
        long longIntervalTime = Math.abs(time1 - time2);
        return (int) (longIntervalTime / 60000);//间隔的分钟
    }

    /**
     * 时间自动补全操作
     *
     * @param timeMillis
     * @return
     */
    private static long autoComple(long timeMillis) {
        int difference = 13 - String.valueOf(timeMillis).length();
        if (difference > 0) {
            timeMillis = (long) (timeMillis * Math.pow(10, difference));
        }
        return timeMillis;
    }

    /**
     * 时间补零  如 1 = 01
     *
     * @param formatTimeMillis
     * @return
     */
    public static String formatTime(int formatTimeMillis) {
        String str = formatTimeMillis + "";
        if (formatTimeMillis < 10) {
            str = "0" + formatTimeMillis;
        }
        return str;
    }

    /**
     * 获取当前时间 年月日 时分秒
     *
     * @return
     */
    public static String getCurrentTime() {
        return formatTimeF(System.currentTimeMillis());
    }


    /**
     * 将时间戳转为字符串
     *
     * @param time  long类型时间
     * @param model 时间显示模式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeStr(long time, String model) {
        // "yyyy年MM月dd日HH时mm分ss秒"
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(model);
        // 例如：cc_time=1291778220
        // long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(time));
        return re_StrTime;
    }

    /**
     * 字符串时间转换为时间戳（毫秒）
     *
     * @param timeFormat
     * @param model
     * @return
     */
    public static long formatModel2Time(String timeFormat, String model) {
        long millionSeconds = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(model, Locale.CHINA);
        try {
            millionSeconds = simpleDateFormat.parse(timeFormat).getTime();//毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }
}