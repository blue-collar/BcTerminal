package com.telewave.lib.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.telewave.lib.base.AppProxy;

/**
 * @author liwh
 * @date 2018/9/7
 */
public class SharePreferenceUtils {

    public static final String SAVE_USER = AppProxy.getInstance().getAPPLICATION_ID();// 保存信息的xml文件名


    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SAVE_USER, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
    }

    public static void putDataSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void putDataSharedPreferences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void putDataSharedPreferences(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static String getDataSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    //返回默认值false
    public static boolean getBooleanDataSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }

    //返回默认值true
    public static boolean getOtherBooleanDataSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(key, true);
    }

    public static int getIntDataSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (key.equals("uniqLogin")) {
            return sharedPreferences.getInt(key, 0);
        } else {
            return sharedPreferences.getInt(key, 1);
        }
    }

}
