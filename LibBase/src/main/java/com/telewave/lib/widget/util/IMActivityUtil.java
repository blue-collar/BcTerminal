package com.telewave.lib.widget.util;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 项目Activity管理
 *
 * @author PF-NAN
 * @date 2017/8/7
 */
public class IMActivityUtil {


    private static WeakReference<Activity> mCurrentActivity;
    private static ArrayList<Activity> mActivities = new ArrayList<>();

    /**
     * 获取当前显示的Activity
     *
     * @return
     */
    public static Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (null != mCurrentActivity) {
            currentActivity = mCurrentActivity.get();
        }
        return currentActivity;
    }

    /**
     * 设置当前显示Activity
     *
     * @param activity
     */
    public static void setCurrentActivity(Activity activity) {
        mCurrentActivity = new WeakReference<>(activity);
    }

    /**
     * 添加
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity);
        }

    }

    /**
     * 移出指定
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    /**
     * 关闭指定
     *
     * @param activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            activity.finish();
            mActivities.remove(activity);
        }
    }

    /**
     * 关闭所有
     */
    public static void finishAllActivity() {
        for (Activity activity : mActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
        mActivities.clear();
    }

}
