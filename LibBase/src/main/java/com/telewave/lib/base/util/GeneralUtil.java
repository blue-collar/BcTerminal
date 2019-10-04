package com.telewave.lib.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.telewave.lib.base.util.ToastUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtil {

    /**
     * 检查内存卡是否存在
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    /**
     * 获取屏幕内容高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int result = 0;
        int resourceId = activity.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        int screenHeight = dm.heightPixels - result;
        return screenHeight;
    }

    /**
     * 获取屏幕宽
     */
    public static int getWindowWidth(Context ctx) {
        Display screenSize = ((WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = screenSize.getWidth();
        return width;
    }

    /**
     * 获取屏幕高
     */
    public static int getWindowHeight(Context ctx) {
        Display screenSize = ((WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = screenSize.getHeight();
        return height;
    }


    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 价格转换
     */
    public static String getProductPrice(Double price) {

        DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
        String newPrice = decimalFormat.format(price);// format 返回的是字符串
        return newPrice;
    }


    /**
     * 实现文本复制功能
     *
     * @param content
     */
    @SuppressLint("NewApi")
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间
     */
    public static String getNewTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 获取当前时间
     */
    public static String getNewTimes() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-ddHH:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 获取当前日期
     */
    public static String getNewDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 判定是否需要隐藏
     */
    public static boolean isHideInput(View v, MotionEvent ev) {

        if (v != null && (v instanceof EditText)) {

            int[] l = {0, 0};

            v.getLocationInWindow(l);

            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left

                    + v.getWidth();

            if (ev.getX() > left && ev.getX() < right && ev.getY() > top

                    && ev.getY() < bottom) {

                return false;

            } else {

                return true;

            }

        }

        return false;

    }

    /**
     * 隐藏软键盘
     */
    public static void HideSoftInput(IBinder token, Context context) {

        if (token != null) {

            InputMethodManager manager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            manager.hideSoftInputFromWindow(token,

                    InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }

    /**
     * 关闭键盘
     *
     * @param context
     */
    public static void closeSoftInputKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     */
    public static void openSoftInputKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 跳转拨号页面
     */
    public static void gotoPhone(Context context, String phoneNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNum));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static int px2dip(Context context, int pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 输入框获取焦点时清空默认文字，失去焦点后显示
     */
    public static void closeEditHint(EditText etView, boolean hasFocus) {
        String hint;

        if (hasFocus) {

            hint = etView.getHint().toString();

            etView.setTag(hint);

            etView.setHint("");

        } else {

            hint = etView.getTag().toString();

            etView.setHint(hint);

        }

    }

    /**
     * 验证手机号码格式
     */
    public static boolean isPhoneNum(String mobiles) {

        // 第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9
        String telRegex = "[1][3578]\\d{9}";
        if (isEmpty(mobiles)) {
            return false;
        }
        return mobiles.matches(telRegex);
    }

    /*
     * 判断输入内容是否包含中文
     */
    public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否包含特殊字符
     */
    public static boolean isTeShuChar(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.find();

    }

    /**
     * 判断是否包含特殊字符
     */
    public static boolean hasTeShuChar(String str) {
        String regEx = "[`~_～!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？0123456789]";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.find();

    }

    public static void showToast(Context conext, String content) {
        ToastUtils.toastShortMessage(content);
    }

    /**
     * 获取之间的随机数
     */
    public static int getRandomNum(int maxNum) {
        double num = Math.random() * maxNum;
        long newNum = Math.round(num);
        return (int) newNum;
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    /**
     * 时间转毫秒
     */
    public static final int IS_GRAB = 0;
    public static final int NO_START = 1;
    public static final int IS_END = 2;

    public static int compareTime(String realTime, String timeFirst, String timeLast) {
        int flag = 0;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date real = null;
        Date d1 = null;
        Date d2 = null;
        try {
            real = df.parse(realTime);
            d1 = df.parse(timeFirst);
            d2 = df.parse(timeLast);

            if ((real.getTime() - d1.getTime()) < 0) {

                return NO_START;
            } else if ((real.getTime() - d1.getTime()) >= 0 && (d2.getTime() - real.getTime()) >= 0) {
                return IS_GRAB;
            } else {
                return IS_END;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return flag;
    }


    /**
     * 获取时间差距
     */
    public static long getTimeGap(String timeFirst, String timeLast) {
        long timegap = 0;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(timeFirst);
            d2 = df.parse(timeLast);

            timegap = d2.getTime() - d1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timegap;
    }

    /**
     * 时间转天、小时、分、秒
     */

    public static String getTimeStr(long timeGap) {

//        timeGap = 1325676000;

        String time;
        int minute = 1000 * 60;
        int hours = minute * 60;
        int day = hours * 24;

        long dayStr = 0;
        long hoursStr = 0;
        long minuteStr = 0;
        long secondStr = 0;


        if (timeGap >= day) {

            dayStr = timeGap / day;
            timeGap = timeGap - (day * dayStr);

        }

        if (timeGap >= hours) {

            hoursStr = timeGap / hours;
            timeGap = timeGap - (hours * hoursStr);

        }

        if (timeGap >= minute) {

            minuteStr = timeGap / minute;
            timeGap = timeGap - (minute * minuteStr);

        }

        if (timeGap >= 1000) {

            secondStr = timeGap / 1000;

        }

        time = dayStr + "天" + hoursStr + "小时" + minuteStr + "分" + secondStr + "秒";

        return time;
    }


    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 获取当前时间
     */
    public static String getTimeStr() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyyMMddhhmmss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
