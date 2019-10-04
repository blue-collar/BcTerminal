package com.telewave.lib.base.util;


/**
 * 防止多次快速点击
 *
 * @author liwh
 * @date 2017/12/10
 */
public class DoubleClickUtils {
    private static long lastClickTime;

    // 避免按钮重复点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        // 1秒中之内只允许点击1次
        if (timeD < 1000) {
            return true;
        }
        return false;
    }
}
