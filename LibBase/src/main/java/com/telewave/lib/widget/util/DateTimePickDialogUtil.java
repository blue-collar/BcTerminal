package com.telewave.lib.widget.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.telewave.lib.base.R;
import com.telewave.lib.base.util.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 日期时间选择控件 使用方法： private EditText inputDate;//需要设置的日期时间文本编辑框 private String
 * initDateTime="2012年9月3日 14:44",//初始日期时间值 在点击事件中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 *
 * @author
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 * dateTimePicKDialog=new
 * DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 * dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * <p>
 * } });
 */
public class DateTimePickDialogUtil implements OnDateChangedListener, OnTimeChangedListener {
    private DatePicker datePicker;
    private AlertDialog ad;
    private String dateTime;
    private String initDateTime;
    private Activity activity;
    private SimpleDateFormat sdf;
    public static final int SELECT_START_TIME = 0x01;
    public static final int SELECT_END_TIME = 0x02;

    /**
     * 日期时间弹出选择框构造函数
     *
     * @param activity     ：调用的父activity
     * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
     */
    public DateTimePickDialogUtil(Activity activity, String initDateTime) {
        this.activity = activity;
        this.initDateTime = initDateTime;
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    }

    public DateTimePickDialogUtil(Activity activity) {
        this.activity = activity;
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    }

    public void init(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar
                    .get(Calendar.DAY_OF_MONTH);
        }

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @param inputDate :为需要设置的日期时间文本编辑框
     * @return
     */
    public void dateTimePicKDialog(final EditText inputDate, final String Time, final int mode) {
        LinearLayout dateTimeLayout = (LinearLayout) activity.getLayoutInflater()
                .inflate(R.layout.common_datetime, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            datePicker.setSpinnersShown(true);
        } else {
            datePicker.setSpinnersShown(false);
        }
        init(datePicker);

        ad = new AlertDialog.Builder(activity).setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            if (!TextUtils.isEmpty(Time)) {
                                if (mode == SELECT_START_TIME) {
                                    if (sdf.parse(Time).before(sdf.parse(dateTime))) {
                                        ToastUtils.toastShortMessage("不能晚于终止时间");
                                        datePicker.clearFocus();
                                        inputDate.setText("");
                                    } else {
                                        inputDate.setText(dateTime);
                                    }
                                } else if (mode == SELECT_END_TIME) {
                                    if (sdf.parse(Time).after(sdf.parse(dateTime))) {
                                        ToastUtils.toastShortMessage("不能早于起始时间");
                                        datePicker.clearFocus();
                                        inputDate.setText("");
                                    } else {
                                        inputDate.setText(dateTime);
                                    }
                                }
                            } else {
                                inputDate.setText(dateTime);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        datePicker.clearFocus();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        datePicker.clearFocus();
                        inputDate.setText("");
                    }
                })
                .show();

        onDateChanged(null, 0, 0, 0);
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @param inputDate :为需要设置的日期时间文本编辑框
     * @return
     */
    public void dateTimePicKDialog(final TextView inputDate, final String Time, final int mode) {
        LinearLayout dateTimeLayout = (LinearLayout) activity.getLayoutInflater()
                .inflate(R.layout.common_datetime, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            datePicker.setSpinnersShown(true);
        } else {
            datePicker.setSpinnersShown(false);
        }
        init(datePicker);

        ad = new AlertDialog.Builder(activity).setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            if (!TextUtils.isEmpty(Time)) {
                                if (mode == SELECT_START_TIME) {
                                    if (sdf.parse(Time).before(sdf.parse(dateTime))) {
                                        ToastUtils.toastShortMessage("不能晚于终止时间");
                                        datePicker.clearFocus();
                                        inputDate.setText("");
                                    } else {
                                        inputDate.setText(dateTime);
                                    }
                                } else if (mode == SELECT_END_TIME) {
                                    if (sdf.parse(Time).after(sdf.parse(dateTime))) {
                                        ToastUtils.toastShortMessage("不能早于起始时间");
                                        datePicker.clearFocus();
                                        inputDate.setText("");
                                    } else {
                                        inputDate.setText(dateTime);
                                    }
                                }
                            } else {
                                inputDate.setText(dateTime);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        datePicker.clearFocus();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        datePicker.clearFocus();
                        inputDate.setText("");
                    }
                })
                .show();

        onDateChanged(null, 0, 0, 0);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, 0, 0);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();

        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        dateTime = sdf.format(calendar.getTime());

        ad.setTitle(dateTime);
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {

        String[] str = initDateTime.split("-");
        Calendar calendar = Calendar.getInstance();
//
//		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
//		String date = spliteString(initDateTime, "-", "index", "front"); // 日期
//
//		String yearStr = spliteString(date, "-", "index", "front"); // 年份
//		String monthAndDay = spliteString(date, "-", "index", "back"); // 月日
//
//		String monthStr = spliteString(monthAndDay, "-", "index", "front"); // 月
//		String dayStr = spliteString(monthAndDay, "-", "index", "back"); // 日

        int currentYear = Integer.parseInt(str[0]);
        int currentMonth = Integer.parseInt(str[1]);
        int currentDay = Integer.parseInt(str[2]);

        calendar.set(currentYear, currentMonth - 1, currentDay);
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern, String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1) {
                result = srcStr.substring(0, loc); // 截取子串
            }
        } else {
            if (loc != -1) {
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
            }
        }
        return result;
    }

}
