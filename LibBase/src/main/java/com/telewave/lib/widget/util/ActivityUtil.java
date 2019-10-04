package com.telewave.lib.widget.util;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Activity帮助类
 */
public class ActivityUtil {

    private static Map<String, Activity> activityMap = new HashMap<>();

    //将Activity添加到队列中
    public static void addDestoryActivityToMap(Activity activity, String activityName) {
        activityMap.put(activityName, activity);
    }

    //根据名字遍历Activity是否存在
    public static boolean isActivityExist(String activityName) {
        boolean isExist = false;
        Set<String> keySet = activityMap.keySet();
        Log.i("DestroyActivityUtil", keySet.size() + "");
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    isExist = true;
                }
            }
        }
        return isExist;
    }

    //根据名字销毁制定Activity
    public static void destoryActivity(String activityName) {
        Set<String> keySet = activityMap.keySet();
        Log.i("DestroyActivityUtil", keySet.size() + "");
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    activityMap.get(key).finish();
                }
            }
        }
    }

    //清除所有Activity
    public static void destoryAllActivity() {
        Set<String> keySet = activityMap.keySet();
        Log.i("DestroyActivityUtil", keySet.size() + "");
        if (keySet.size() > 0) {
            for (String key : keySet) {
                activityMap.get(key).finish();
            }
        }
    }

}