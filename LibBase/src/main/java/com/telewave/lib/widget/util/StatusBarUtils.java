package com.telewave.lib.widget.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.telewave.lib.base.util.DeviceUtils;
import com.telewave.lib.log.TweLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: rick_tan
 * @Date: 19-7-21
 * @Version: v1.0
 * @Des StatusBarUtils
 */
public class StatusBarUtils {

    private static final String TAG = "StatusBarUtils";

    /**
     * 是否开启沉浸式状态栏
     */
    public static void openStatusBarImmersiveMode(Activity activity,int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 因为EMUI3.1系统与这种沉浸式方案API有点冲突，会没有沉浸式效果。
                // 所以这里加了判断，EMUI3.1系统不清除FLAG_TRANSLUCENT_STATUS
                if (!isEMUI3_1()) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    public static void setStatusBarDarkMode(Activity activity, boolean isDark) {
        /*if (false&&DeviceUtils.isMiui()) {
            MIUISetStatusBarLightMode(activity, isDark);
        } else */
        if (DeviceUtils.isFlyme()) {
            FlymeSetStatusBarLightMode(activity, isDark);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarBgColor(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 是否设置状态栏字体颜色
     */
    public static void setStatusBarColor(Activity activity, boolean isDark) {
        /*if (DeviceUtils.isMiui()) {
            MIUISetStatusBarLightMode(activity, isDark);
        }else*/
        if (DeviceUtils.isFlyme()) {
            FlymeSetStatusBarLightMode(activity, isDark);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            {
                if (isDark) {
                    activity.getWindow()
                            .getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    activity.getWindow()
                            .getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        }
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param activity 需要设置的窗口
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            Window window = activity.getWindow();
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param activity 需要设置的窗口
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            Window window = activity.getWindow();
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField(
                        "MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static boolean isEMUI3_1() {
        return "EmotionUI_3.1".equals(getEmuiVersion());
    }

    private static String getEmuiVersion() {
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, "ro.build.version.emui");
        } catch (Exception e) {
            TweLog.e(TAG, e.toString());
        }
        return "";
    }

    /**
     * 设置标题栏为沉浸式
     *
     * @param toolbar                  ToolBar
     * @param statusBarBackgroundColor 用于沉浸式的状态栏颜色
     */
    public static void setStatusBarBackgroundColor(Activity activity, final Toolbar toolbar,
                                                   final int statusBarBackgroundColor) {
        setStatusBarBackgroundColor(activity, toolbar, statusBarBackgroundColor);
    }

    /**
     * 设置标题栏为沉浸式
     *
     * @param topPlaceholderView       可以为ToolBar, 自定义标题栏，如果不需要标题，可传0.1dp左右的背景为透明的View作为占位
     * @param statusBarBackgroundColor 用于沉浸式的状态栏颜色
     */
    public static void setStatusBarBackgroundColor(Activity activity, final View topPlaceholderView,
                                                   final int statusBarBackgroundColor) {
        //sdk版本在[4.4,5.0)
        if (isKitKatBetweenLollipop()) {
            if (topPlaceholderView != null) {
                //设置状态栏透明
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置topPlaceholderView的高度(原有高度+statusBar高度)
                ViewGroup.LayoutParams params = topPlaceholderView.getLayoutParams();
                int statusBarHeight = getStatusBarHeight(activity);
                params.height += statusBarHeight;
                topPlaceholderView.setLayoutParams(params);
                //设置paddingTop,以达到状态栏不遮挡ToolBar的内容
                topPlaceholderView.setPadding(topPlaceholderView.getPaddingLeft(),
                        topPlaceholderView.getPaddingTop()
                                + getStatusBarHeight(activity), topPlaceholderView.getPaddingRight(), topPlaceholderView
                                .getPaddingBottom());
                topPlaceholderView.setBackgroundColor(statusBarBackgroundColor);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            topPlaceholderView.setVisibility(View.GONE);
            activity.getWindow().setStatusBarColor(statusBarBackgroundColor);
        } else {
            //低于4.4的。不做处理
        }
    }

    /**
     * 获取状态栏的高度
     */
    protected static int getStatusBarHeight(Context context) {
        return getSystemComponentDimen(context, "status_bar_height");
    }

    /**
     * 获取系统组件高度
     *
     * @param context   上下文
     * @param dimenName 组件id ，如status_bar_height
     * @return 组件高度
     */
    private static int getSystemComponentDimen(Context context, String dimenName) {
        int statusHight = 0;
        try {
            //反射拿到android.R.internal.R$dimen内部类
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            //创建dimen实例
            Object object = clazz.newInstance();
            //拿到dimenName对应的变量名，在拿到其对应的值并转为int值
            String heightStr = clazz.getField(dimenName).get(object).toString();
            int height = Integer.parseInt(heightStr);
            //dp--->dx
            statusHight = context.getResources().getDimensionPixelSize(height);
            return statusHight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHight;
    }

    /**
     * 判断版本号是否在[4.4, 5)
     */
    public static boolean isKitKatBetweenLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

}
