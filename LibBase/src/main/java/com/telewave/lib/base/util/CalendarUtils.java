package com.telewave.lib.base.util;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarUtils {
    /**
     * 将时间戳转为字符串
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    //获取12：12
    public static String getHourAndMinutes(Date date) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(date);
    }


    public static String getBirth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public static String getQianDaoDay(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(timeStr, pos);
        return CalendarUtils.getDay(date);
    }

    public static String getQianDaoMonth(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(timeStr, pos);
        return CalendarUtils.getMonth(date);
    }

    public static String getQianDaoDate(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(timeStr, pos);
        return CalendarUtils.getBirth(date);
    }

    public static String getQianDaoTime(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(timeStr, pos);
        return CalendarUtils.getHourAndMinutes(date);
    }

    public static Date getDate(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(timeStr, pos);
        return date;
    }

    public static String getDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String getMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM月");
        return format.format(date);
    }

    /**
     * 取得某个月有多少天
     */
    public static int getDaysOfMonth(Calendar cal, int year, int month) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int days_of_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days_of_month;
    }

    /**
     * 获取星期几返回的数字
     *
     * @param weekStr
     * @return
     */
    public static int getWeekRowCount(String weekStr) {
        int weekCount = -1;
        switch (weekStr) {
            case "星期日":
            case "周日":
                weekCount = 0;
                break;
            case "星期一":
            case "周一":
                weekCount = 1;
                break;
            case "星期二":
            case "周二":
                weekCount = 2;
                break;
            case "星期三":
            case "周三":
                weekCount = 3;
                break;
            case "星期四":
            case "周四":
                weekCount = 4;
                break;
            case "星期五":
            case "周五":
                weekCount = 5;
                break;
            case "星期六":
            case "周六":
                weekCount = 6;
                break;
            default:
                break;
        }
        return weekCount;
    }

    /**
     * 一个月有多少天
     *
     * @param calendar
     * @return
     */
    public static int getDaysCount(@NonNull Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DATE);
    }


    public static int getDayOfWeek(@NonNull Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfYear(@NonNull Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }


    public static int getMonth(@NonNull Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    public static int getRealMonth(@NonNull Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYear(@NonNull Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int getDay(@NonNull Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 年月份相同
     *
     * @param firstCalendar
     * @param secondCalendar
     * @return
     */
    public static boolean isTheSameMouthAndYear(Calendar firstCalendar, Calendar secondCalendar) {
        if (CalendarUtils.getMonth(firstCalendar) == CalendarUtils.getMonth(secondCalendar)
                && CalendarUtils.getYear(firstCalendar) == CalendarUtils.getYear(firstCalendar)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 每月的同一天
     *
     * @param firstCalendar
     * @param secondCalendar
     * @return
     */
    public static boolean isTheDayOfMouth(Calendar firstCalendar, Calendar secondCalendar) {
        if (null == firstCalendar || null == secondCalendar) {
            return false;
        }
        if (CalendarUtils.getDay(firstCalendar) == CalendarUtils.getDay(secondCalendar)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 每周的同一天
     *
     * @param firstCalendar
     * @param secondCalendar
     * @return
     */
    public static boolean isTheDayOfWeek(Calendar firstCalendar, Calendar secondCalendar) {
        if (CalendarUtils.getDayOfWeek(firstCalendar) == CalendarUtils.getDayOfWeek(secondCalendar)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 复制相同的日历
     *
     * @param firstCalendar
     * @return
     */
    public static Calendar copyCalendar(Calendar firstCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(CalendarUtils.getYear(firstCalendar), CalendarUtils.getMonth(firstCalendar), CalendarUtils.getDay(firstCalendar));
        return calendar;
    }


    /**
     * 时间间隔了多少个月
     */
    public static int getMonthCount(Calendar firstCalendar, Calendar secondCalendar) {
        int addYear = CalendarUtils.getYear(secondCalendar) - CalendarUtils.getYear(firstCalendar);
        int addMonth = CalendarUtils.getMonth(secondCalendar) - CalendarUtils.getMonth(firstCalendar);
        if (addYear < 0) {
            return 0;
        }
        return addYear * 12 + addMonth + 1;
    }

    /**
     * 时间间隔了多少周
     */
    public static int getWeekCount(Calendar firstCalendar, Calendar secondCalendar) {
        int firstDayOfWeek = getDayOfWeek(firstCalendar);
        int secondDayOfWeek = getDayOfWeek(secondCalendar);
        int weekCount = (getDayCount(firstCalendar, secondCalendar) + firstDayOfWeek - 1 + 7 - secondDayOfWeek) / 7 + 1;
        return weekCount;
    }

    /**
     * 时间间隔了多少天
     */
    public static int getDayCount(Calendar firstCalendar, Calendar secondCalendar) {
        //设置时间为0时
        firstCalendar.set(Calendar.HOUR_OF_DAY, 0);
        firstCalendar.set(Calendar.MINUTE, 0);
        firstCalendar.set(Calendar.SECOND, 0);
        secondCalendar.set(Calendar.HOUR_OF_DAY, 0);
        secondCalendar.set(Calendar.MINUTE, 0);
        secondCalendar.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int fistDay = (int) (firstCalendar.getTime().getTime() / (1000 * 60 * 60 * 24));
        int secondeDay = (int) (secondCalendar.getTime().getTime() / (1000 * 60 * 60 * 24));

        int addDay = secondeDay - fistDay;
        return addDay;
    }

    public static String toString(Calendar cal) {
        if (null == cal) {
            return "";
        }
        return getYear(cal) + "年" + getRealMonth(cal) + "月" + getDay(cal) + "日";
    }

    public static String getDate(Calendar calendar) {
        String year = CalendarUtils.getYear(calendar) + "";
        String mouth = CalendarUtils.getRealMonth(calendar) + "";
        if (mouth.length() == 1) {
            mouth = "0" + mouth;
        }
        String day = CalendarUtils.getDay(calendar) + "";
        if (day.length() == 1) {
            day = "0" + day;
        }
        return year + mouth + day;
    }


    public static Boolean isAfterToday(Calendar calendar) {

        Calendar instance = Calendar.getInstance();

        if (getDayCount(instance, calendar) >= 1 || calendar.after(instance)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }
}