package com.telewave.lib.widget.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Locale;

/**
 * @Author: rick_tan
 * @Date: 19-7-21
 * @Version: v1.0
 * @Des FragmentUtils
 */
public class FragmentUtils {

    /**
     * 通过show/hide方法来切换fragment
     *
     * @param currentFragment 当前fragment
     * @param clazz           要切换到的Fragment的class对象
     * @param index           fragment索引
     * @return 切换到的Fragment
     */
    public static Fragment switchFragmentBySH(Context context, FragmentManager fragmentManager,
                                              Fragment currentFragment, int containerId, Class clazz, int index) {
        return switchFragmentBySH(context, fragmentManager, currentFragment, containerId, clazz,
                index, null);
    }

    /**
     * 通过show/hide方法来切换fragment
     *
     * @param currentFragment 当前fragment
     * @param clazz           要切换到的Fragment的class对象
     * @param index           fragment索引
     * @param args            fragment参数
     * @return 切换到的Fragment
     */
    public static Fragment switchFragmentBySH(Context context, FragmentManager fragmentManager,
                                              Fragment currentFragment, int containerId, Class clazz, int index, Bundle args) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null != currentFragment) {
            transaction.hide(currentFragment);
        }
        String tag = getTag(clazz, index);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            fragment = Fragment.instantiate(context, clazz.getName(), args);
            transaction.add(containerId, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        return fragment;
    }

    /**
     * 根据class和索引查找fragment
     */
    public static Fragment findFragmentByTag(FragmentManager fragmentManager, Class clazz,
                                             int index) {
        return fragmentManager.findFragmentByTag(getTag(clazz, index));
    }

    /**
     * 生成Fragment的tag
     */
    private static String getTag(Class clazz, int index) {
        return String.format(Locale.getDefault(), "%s_%d", clazz.getName(), index);
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment,
                                   int containerViewId, int enter, int exit, int popEnter, int popExit, String tag,
                                   boolean addToBackStack, String backStackName) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, popEnter, popExit); // 动画设置必须在添加等操作前面才有效果
        if (addToBackStack) {
            transaction.addToBackStack(backStackName);
        }
        transaction.add(containerViewId, fragment, tag);
        transaction.commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment,
                                   int containerViewId, int enter, int exit, int popEnter, int popExit,
                                   boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, popEnter, popExit); // 动画设置必须在添加等操作前面才有效果
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment,
                                   int containerViewId, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment,
                                   int containerViewId, boolean addToBackStack, String backStackName) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(backStackName);
        }
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }
}
