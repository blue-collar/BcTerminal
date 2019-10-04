package com.telewave.lib.base.util;


import android.content.Context;
import android.util.Log;

import com.telewave.lib.base.ConstData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 日期时间工具类
 *
 * @author Administrator
 */
public class DateUtils {

    public static final String FORMAT_STYLE_A = "yyyy-MM";
    public static final String FORMAT_STYLE_B = "M月d日";
    public static final String FORMAT_STYLE_C = "HH:mm";
    public static final String FORMAT_STYLE_D = "MM-dd HH:mm:ss";
    public static final String FORMAT_STYLE_E = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_STYLE_F = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_STYLE_G = "yyyy-MM-dd HH:mm:ss E";
    public static final String FORMAT_STYLE_H = "yyyyMMddHHmmss";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATETIME2 = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_DATE_ZH = "yyyy年MM月dd日";
    public final static String FORMAT_DATETIME_ZH = "yyyy年MM月dd日 HH时mm分ss秒";

    public final static String TYPE_DATE = "date";
    public final static String TYPE_DATETIME = "datetime";

    public final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = "DateUtils";

    /**
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空 默认当前时间 ,表示和当前时间相比)
     * @return
     * @throws ParseException 转换异常
     */
    public static boolean oneWeek(Date oldTime, Date newTime) throws ParseException {
        if (newTime == null) {
            newTime = new Date();
        }
        // 将下面的 理解成 yyyy-MM-dd 00：00：00
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);

        Log.d(TAG, "todaytime= " + today.getTime() + "  oldtime= " + oldTime.getTime());
        Log.d(TAG, "相减的值 " + (today.getTime() - oldTime.getTime()) + "  标准值： " + 86400000L * 7);
        Log.d(TAG, "todaytime= " + todayStr + "  oldtime= " + oldTime.getDate());
        //  86400000=24*60*60*1000 一天
        if ((today.getTime() - oldTime.getTime()) > 0
                && (today.getTime() - oldTime.getTime()) >= 86400000L * 7) {
            Log.d(TAG, "todaytime= " + todayStr + "  oldtime= " + oldTime.getDate());
            return true;
        }
        return false;
    }

    /**
     * 获取当天时间
     *
     * @param dateformat
     * @return
     */
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格式
        String nowtime = dateFormat.format(now);
        return nowtime;
    }

    /**
     * 获得本周一的日期
     *
     * @return
     */
    public String getMondayOFWeek() {

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.e("time:", mCalendar.getTime() + "");
        return mDateFormat.format(mCalendar.getTime());
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空 默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一周. 0：前一周 . 1 ：至少是前一周.
     * @throws ParseException 转换异常
     */
    public static boolean oneMonth(Date oldTime, Date newTime) throws ParseException {
        if (newTime == null) {
            newTime = new Date();
        }
        // 将下面的 理解成 yyyy-MM-dd 00：00：00
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);

        Log.d(TAG, "todaytime= " + today.getTime() + "  oldtime= " + oldTime.getTime());
        Log.d(TAG, "相减的值 " + (today.getTime() - oldTime.getTime()) + "  标准值： " + 86400000L * 30);
        Log.d(TAG, "todaytime= " + todayStr + "  oldtime= " + oldTime.getDate());
        // 86400000=24*60*60*1000 一天
        if ((today.getTime() - oldTime.getTime()) > 0
                && (today.getTime() - oldTime.getTime()) >= 86400000L * 30) {
            Log.d(TAG, "todaytime= " + todayStr + "  oldtime= " + oldTime.getDate());
            return true;
        }
        return false;
    }


    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 日期排序类型-升序
     */
    public final static int DATE_ORDER_ASC = 0;

    /**
     * 日期排序类型-降序
     */
    public final static int DATE_ORDER_DESC = 1;


    /**
     * 年-月
     */
    public static String formatA(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_A);
        return sdf.format(date);
    }

    /**
     * 月-日
     */
    public static String formatB(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_B);
        return sdf.format(date);
    }

    /**
     * 时-分
     */
    public static String formatC(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_C);
        return sdf.format(date);
    }

    /**
     * 月-日 时:分
     */
    public static String formatD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_D);
        return sdf.format(date);
    }

    /**
     * 年-月-日 时:分
     */
    public static String formatE(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_E);
        return sdf.format(date);
    }

    /**
     * 年-月-日 时:分:秒
     */
    public static String formatF(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_F);
        return sdf.format(date);
    }

    /**
     * 年-月-日 时:分:秒 ：周
     */
    public static String formatG(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_G);
        return sdf.format(date);
    }

    /**
     * 年月日时分秒
     */
    public static String formatH(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STYLE_H);
        return sdf.format(date);
    }

    /**
     * 年-月-日
     */
    public static String baseDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE, Locale.CHINA);
        date.setMonth(date.getMonth());
        return sdf.format(date);
    }
    /*
	public static String charToDateTochar(String dd) throws ParseException{
		return baseDate(getDate(dd,TYPE_DATE)) ;
	}
	*/

    /**
     * 获得上、下午
     *
     * @return
     */
    public static String getAP_PM() {
        int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
        switch (am_pm) {
            case 0:
                return "上午";
            case 1:
                return "下午";
        }
        return "获得AM_PM失败";
    }

    /**
     * 获得当前日期的周几
     *
     * @return
     */
    public static String getWeek() {
        return convertWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * 获得指定日期的周几
     *
     * @param date java.util.Date日期对象
     * @return
     */
    public static String getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return convertWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * 获得指定日期的周几
     *
     * @param date 字符串
     * @return
     */
    public static String getWeek(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(FORMAT_DATE).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * 获得周几
     *
     * @return
     */
    public static String convertWeek(int week) {

        switch (week) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
        }
        return "日前操作失败";
    }


    /**
     * 用字符串获得日期
     *
     * @throws ParseException
     * @dateValue 日期字符串
     * @dateType 格式化的类型, date和datetime
     */
    public static Date getDate(String dateValue, String dateType)
            throws ParseException {
        if (dateValue == null)
            return null;
        if (dateType.equals(TYPE_DATE)) {
            SimpleDateFormat sfdate = new SimpleDateFormat(FORMAT_DATE);
            return sfdate.parse(dateValue);
        } else if (dateType.equals(TYPE_DATETIME)) {
            SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATETIME);
            return sftime.parse(dateValue);
        }
        return null;
    }

    /**
     * 用字符串获得java.sql.Date日期
     *
     * @throws ParseException
     * @dateValue 日期字符串
     * @dateType 格式化的类型, date和datetime
     */
    public static java.sql.Date getSqlDate(String dateValue, String dateType)
            throws ParseException {
        Date date = getDate(dateValue, dateType);
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }

    /**
     * 将日期加上某些天或减去天数)返回字符串
     *
     * @param date 待处理日期
     * @param to   加减的天数
     * @return 日期
     */
    public static Date dateAdd(String date, int to) {
        Date d = null;
        try {
            d = java.sql.Date.valueOf(date);
        } catch (Exception e) {
            e.printStackTrace();
            d = new Date();
        }
        Calendar strDate = Calendar.getInstance();
        strDate.setTime(d);
        strDate.add(Calendar.DATE, to); // 日期减 如果不够减会将月变动
        return strDate.getTime();
    }

    /**
     * 将日期加上某些天或减去天数)返回字符串
     *
     * @param date 待处理日期
     * @param to   加减的天数
     * @return 日期
     */
    public static java.sql.Date dateAdd(java.sql.Date date, int to) {
        Calendar strDate = Calendar.getInstance();
        strDate.setTime(date);
        strDate.add(Calendar.DATE, to); // 日期减 如果不够减会将月变动
        return new java.sql.Date(strDate.getTime().getTime());
    }

    /**
     * 格式化日期
     *
     * @param date      日期对象
     * @param splitChar 分隔字符
     * @return
     */
    public static String formatDate(Date date, String splitChar) {
        SimpleDateFormat sfdate = new SimpleDateFormat(
                "yyyy" + splitChar + "MM" + splitChar + "dd");
        return sfdate.format(date);
    }

    /**
     * @dateValue 日期对象，可以是java.util.Date和java.sql.Date
     * @dateType 格式化的类型, date和datetime
     */
    public static String format(Object dateValue, String dateType) {
        if (dateValue == null)
            return "";
        if (dateValue instanceof java.sql.Date) {
            return dateValue.toString();
        } else if (dateValue instanceof Date) {
            if (dateType.equals(TYPE_DATE)) {
                SimpleDateFormat sfdate = new SimpleDateFormat(
                        FORMAT_DATE);
                return sfdate.format(dateValue);
            } else if (dateType.equals(TYPE_DATETIME)) {
                SimpleDateFormat sftime = new SimpleDateFormat(
                        FORMAT_DATETIME);
                return sftime.format(dateValue);
            } else {
                return "非法日期格式[" + dateType + "]";
            }
        } else {
            return "非日期类型";
        }
    }

    /**
     * 转换日期对象为中文化日期
     *
     * @dateValue 日期对象，可以是java.util.Date和java.sql.Date
     * @dateType 格式化的类型, date和datetime
     */
    public static String formatZh(Date dateValue, String dateType) {
        if (dateValue == null)
            return "";
        if (dateValue instanceof java.sql.Date) {
            return dateValue.toString();
        } else if (dateValue instanceof Date) {
            if (dateType.equals(TYPE_DATE)) {
                SimpleDateFormat sfdate = new SimpleDateFormat(
                        FORMAT_DATE_ZH);
                return sfdate.format(dateValue);
            } else if (dateType.equals(TYPE_DATETIME)) {
                SimpleDateFormat sftime = new SimpleDateFormat(
                        FORMAT_DATETIME_ZH);
                return sftime.format(dateValue);
            } else {
                return "非法日期格式[" + dateType + "]";
            }
        } else {
            return "非日期类型";
        }
    }

    /**
     * 转化成年月日期
     *
     * @param sDate         字符型日期：2009-02-02
     * @param DelimeterChar 分割符号比如 / -
     * @return 年月日期 :2009年02月02日
     */
    public static String chDateChange(String sDate, String DelimeterChar) {
        String tmpArr[] = sDate.split(DelimeterChar);
        tmpArr[0] = tmpArr[0] + "年";
        tmpArr[1] = tmpArr[1] + "月";
        tmpArr[2] = tmpArr[2] + "日";
        return tmpArr[0] + tmpArr[1] + tmpArr[2];
    }

    /**
     * 得到系统日期
     *
     * @return YYYY-MM-DD
     */
    public static String getSysdate() {
        java.sql.Timestamp timeNow = new java.sql.Timestamp(
                System.currentTimeMillis());
        return timeNow.toString().substring(0, 10);
    }

    public static String getDate() {
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date(currentTime);

        String sim = formatter.format(date);
        return sim;
    }

    public static String getTime() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date(currentTime);

        String sim = formatter.format(date);
        return sim;
    }

    /**
     * 得到某天是周几
     *
     * @param strDay
     * @return 周几
     */
    public static int getWeekDay(String strDay) {
        Date day = DateUtils.dateAdd(strDay, -1);
        Calendar strDate = Calendar.getInstance();
        strDate.setTime(day);
        int meStrDate = strDate.get(Calendar.DAY_OF_WEEK);
        return meStrDate;
    }

    /**
     * 得到某天是周几
     *
     * @param
     * @return 周几
     */
    public static int getWeekDay(Date date) {
        Date day = DateUtils.dateAdd(format(date, "date"), -1);
        Calendar strDate = Calendar.getInstance();
        strDate.setTime(day);
        int meStrDate = strDate.get(Calendar.DAY_OF_WEEK);
        return meStrDate;
    }

    /**
     * 取得两个日期段的日期间隔
     *
     * @param t1 时间1
     * @param t2 时间2
     * @return t2 与t1的间隔天数
     * @throws ParseException 如果输入的日期格式不是0000-00-00 格式抛出异常
     * @author color
     */
    public static int getBetweenDays(String t1, String t2)
            throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int betweenDays = 0;
        Date d1 = format.parse(t1);
        Date d2 = format.parse(t2);
        betweenDays = getBetweenDays(d1, d2);
        return betweenDays;
    }

    /**
     * 取得两个日期段的日期间隔
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return t2 与t1的间隔天数
     */
    private static int getBetweenDays(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return -1;
        }
        int betweenDays;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间
        if (c1.after(c2)) {
            c2.setTime(d1);
            c1.setTime(d2);
        }
        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        betweenDays = c2.get(Calendar.DAY_OF_YEAR)
                - c1.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
            betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }

    /**
     * 判断指定日期是否在一个日期范围内
     *
     * @param fromDate 范围开始日期
     * @param toDate   范围结束日期
     * @param testDate 测试日期
     * @return 在范围内true, 否则false
     */
    public static boolean betweenDays(java.sql.Date fromDate,
                                      java.sql.Date toDate, java.sql.Date testDate) {
        if (fromDate == null || toDate == null || testDate == null) {
            return false;
        }

        // 1、 交换开始和结束日期
        if (fromDate.getTime() > toDate.getTime()) {
            java.sql.Date tempDate = fromDate;
            fromDate = toDate;
            toDate = tempDate;
        }

        // 2、缩小范围
        long testDateTime = testDate.getTime();
        if ((testDateTime > fromDate.getTime() && testDateTime > toDate
                .getTime())
                || testDateTime < fromDate.getTime()
                && testDateTime < toDate.getTime()) {
            return false;
        }

        return true;
    }

    /**
     * 判断两个日期是否为同一天
     *
     * @param d1 日期一
     * @param d2 日期二
     * @return 同一天true，不是同一天false
     */
    public static boolean isSameDate(Date d1, Date d2) {
        boolean result = false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2
                .get(Calendar.DAY_OF_MONTH)) {
            result = true;
        }
        return result;
    }

    /**
     * 是否为周末
     *
     * @param strDate
     * @return true|false
     */
    public static boolean isWeekend(String strDate) {
        int weekDay = getWeekDay(strDate);
        if (weekDay == 6 || weekDay == 7) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否为周末
     *
     * @param
     * @return true|false
     */
    public static boolean isWeekend(Date date) {
        int weekDay = getWeekDay(format(date, "date"));
        if (weekDay == 6 || weekDay == 7) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日期排序
     *
     * @param dates     日期列表
     * @param orderType 排序类型 <br/>
     * @return 排序结果
     */
    public static List<Date> orderDate(List<Date> dates, int orderType) {
        DateComparator comp = new DateComparator(orderType);
        Collections.sort(dates, comp);
        return dates;
    }

    static class DateComparator implements Comparator<Date> {

        int orderType;

        public DateComparator(int orderType) {
            this.orderType = orderType;
        }

        public int compare(Date d1, Date d2) {
            if (d1.getTime() > d2.getTime()) {
                if (orderType == DateUtils.DATE_ORDER_ASC) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (d1.getTime() == d2.getTime()) {
                    return 0;
                } else {
                    if (orderType == DateUtils.DATE_ORDER_DESC) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }

    }

    /**
     * 日期分组<br/>
     * 能够对指定日期列表按照连续性分组<br/>
     * 例如：[2010-01-15, 2010-01-16, 2010-01-17, 2010-01-20, 2010-01-21,
     * 2010-01-25]<br/>
     * 分组结果为：<br/>
     * <ul>
     * <li>[2010-01-15, 2010-01-16, 2010-01-17]</li>
     * <li>[2010-01-20, 2010-01-21]</li>
     * <li>[2010-01-25]</li>
     * </ul>
     *
     * @param dates 日期对象
     * @return 连续性分组结果
     */
    public static List<List<Date>> groupDates(List<Date> dates) {
        List<List<Date>> result = new ArrayList<List<Date>>();

        // 按照升序排序
        orderDate(dates, DateUtils.DATE_ORDER_ASC);

        // 临时结果
        List<Date> tempDates = null;

        // 上一组最后一个日期
        Date lastDate = null;

        // 当前读取日期
        Date cdate = null;
        for (int i = 0; i < dates.size(); i++) {
            cdate = dates.get(i);

            // 第一次增加
            if (tempDates == null) {
                tempDates = new ArrayList<Date>();
                tempDates.add(cdate);
                result.add(tempDates);
            } else {
                /**
                 * 差距为1是继续在原有的列表中添加，大于1就是用新的列表
                 */
                lastDate = tempDates.get(tempDates.size() - 1);
                int days = getBetweenDays(lastDate, cdate);
                if (days == 1) {
                    tempDates.add(cdate);
                } else {
                    tempDates = new ArrayList<Date>();
                    tempDates.add(cdate);
                    result.add(tempDates);
                }
            }
        }

        return result;
    }

    public static final int YEAR_RETURN = 0;

    public static final int MONTH_RETURN = 1;

    public static final int DAY_RETURN = 2;

    public static final int HOUR_RETURN = 3;

    public static final int MINUTE_RETURN = 4;

    public static final int SECOND_RETURN = 5;

    public static long getBetween(String beginTime, String endTime, String formatPattern, int returnPattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        Date beginDate = simpleDateFormat.parse(beginTime);
        Date endDate = simpleDateFormat.parse(endTime);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        switch (returnPattern) {
            case YEAR_RETURN:
                return getByField(beginCalendar, endCalendar, Calendar.YEAR);
            case MONTH_RETURN:
                return getByField(beginCalendar, endCalendar, Calendar.YEAR) * 12 + getByField(beginCalendar, endCalendar, Calendar.MONTH);
            case DAY_RETURN:
                return getTime(beginDate, endDate) / (24 * 60 * 60 * 1000);
            case HOUR_RETURN:
                return getTime(beginDate, endDate) / (60 * 60 * 1000);
            case MINUTE_RETURN:
                return getTime(beginDate, endDate) / (60 * 1000);
            case SECOND_RETURN:
                return getTime(beginDate, endDate) / 1000;
            default:
                return 0;
        }
    }

    private static long getByField(Calendar beginCalendar, Calendar endCalendar, int calendarField) {
        return endCalendar.get(calendarField) - beginCalendar.get(calendarField);
    }

    private static long getTime(Date beginDate, Date endDate) {
        return endDate.getTime() - beginDate.getTime();
    }

    public static String getToday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + (month > 9 ? month : ("0" + month)) + "-" + day;
    }

    /**
     * 获取前n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    public static void clear(Context context) {
        SharePreferenceUtils.putDataSharedPreferences(context, "organName", "");
        //登录认证标志rzzt：1，通过；0，失败；
        SharePreferenceUtils.putDataSharedPreferences(context, "rzzt", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "username", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "userid", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "lxfs", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "mapx", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "mapy", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "organid", "");
        SharePreferenceUtils.putDataSharedPreferences(context, "isLeader", "");

        ConstData.isNewDisasterVoiceOpen = true;
        ConstData.isNotifyVoiceOpen = true;
        ConstData.isRollCallVoiceOpen = true;
        SharePreferenceUtils.putDataSharedPreferences(context, "isNewDisasterVoiceOpen", ConstData.isNewDisasterVoiceOpen);
        SharePreferenceUtils.putDataSharedPreferences(context, "isNotifyVoiceOpen", ConstData.isNotifyVoiceOpen);
        SharePreferenceUtils.putDataSharedPreferences(context, "isRollCallVoiceOpen", ConstData.isRollCallVoiceOpen);
        ConstData.loginName = "";
        ConstData.loginPassword = "";
        ConstData.username = "";
        ConstData.userid = "";
        ConstData.isLeader = "";
        ConstData.ORGANID = "";
        ConstData.ORGAN_NAME = "";
        ConstData.mapx = "";
        ConstData.mapy = "";
        ConstData.address = "";
        ConstData.userSig = "";
    }
}

