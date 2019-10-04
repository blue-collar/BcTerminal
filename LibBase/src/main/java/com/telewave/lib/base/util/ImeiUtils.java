package com.telewave.lib.base.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

class ImeiUtils {

    public static ArrayList getImeiList(Context context) {
        ArrayList resultList = new ArrayList();
        ArrayList addList = new ArrayList();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String deviceId = telephonyManager.getDeviceId();

        if (isValidImei(deviceId)) {
            addList.add(deviceId);
            resultList.add(deviceId);
        }

        if (Build.VERSION.SDK_INT >= 22) {
            if (Build.VERSION.SDK_INT >= 23 && telephonyManager.getPhoneCount() == 2) {
                String deviceId2 = telephonyManager.getDeviceId(1);
                if (!TextUtils.isEmpty(deviceId2) && isValidImei(deviceId2) && !addList.contains(deviceId2)) {
                    resultList.add(deviceId2);
                }
            }
        } else {
            String phoneDeviceId;
            TelephonyManager telephonyManagerPhone;
            try {
                telephonyManagerPhone = (TelephonyManager) context.getSystemService("phone1");
                phoneDeviceId = telephonyManagerPhone.getDeviceId();
                if (!TextUtils.isEmpty(phoneDeviceId) && isValidImei(phoneDeviceId) && !addList.contains(phoneDeviceId)) {
                    addList.add(phoneDeviceId);
                    resultList.add(phoneDeviceId);
                }
            } catch (Exception e) {

            }

            try {
                telephonyManagerPhone = (TelephonyManager) context.getSystemService("phone2");
                phoneDeviceId = telephonyManagerPhone.getDeviceId();
                if (!TextUtils.isEmpty(phoneDeviceId) && isValidImei(phoneDeviceId) && !addList.contains(phoneDeviceId)) {
                    addList.add(phoneDeviceId);
                    resultList.add(phoneDeviceId);
                }
            } catch (Exception e) {
            }

            ArrayList<String> deviceIdList = getDeviceIdList(context, "getDeviceIdGemini");
            if (deviceIdList == null || deviceIdList.size() == 0) {
                deviceIdList = getDeviceIdList(context, "getDefault");
            }
            if (deviceIdList == null || deviceIdList.size() == 0) {
                deviceIdList = getDeviceIdListMsim(context);
            }
            if (deviceIdList == null || deviceIdList.size() == 0) {
                deviceIdList = getDeviceIdListFactory(context);
            }

            if (deviceIdList != null && deviceIdList.size() > 0) {
                for (int i = 0; i < deviceIdList.size(); ++i) {
                    String item = deviceIdList.get(i);
                    if (!addList.contains(item)) {
                        addList.add(item);
                        resultList.add(item);
                    }
                }
            }
        }

        return resultList;
    }

    private static boolean isValidImei(String imei) {
        int length = imei.length();
        return length > 10 && length < 20 && unMonotonous(imei);
    }

    //仅仅用于判断是否全是同一个字符的imei，这种情况不会出现在正常设备上
    private static boolean unMonotonous(String value) {
        final int size = value.length();
        if (size < 1)
            return false;

        final char fisrtChar = value.charAt(0);
        for (int i = 0; i < size; ++i) {
            char item = value.charAt(i);
            if (fisrtChar != item) {
                return true;
            }
        }

        return false;
    }

    /**
     * MTK芯片
     *
     * @param context
     * @param methodName
     * @return
     */
    private static ArrayList getDeviceIdList(Context context, String methodName) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class phoneClass = Class.forName("com.android.internal.telephony.Phone");

            int id0 = 0;
            int id1 = 1;
            try {
                Field fieldSim1 = phoneClass.getField("GEMINI_SIM_1");
                fieldSim1.setAccessible(true);
                id0 = (Integer) fieldSim1.get((Object) null);
                Field fieldSim2 = phoneClass.getField("GEMINI_SIM_2");
                fieldSim2.setAccessible(true);
                id1 = (Integer) fieldSim2.get((Object) null);
            } catch (Exception e) {
            }

            Method method = TelephonyManager.class.getDeclaredMethod(methodName, Integer.TYPE);
            if (telephonyManager != null && method != null) {
                String diviceId0 = ((String) method.invoke(telephonyManager, id0)).trim();
                ArrayList<String> result = new ArrayList();
                if (isValidImei(diviceId0)) {
                    result.add(diviceId0);
                }

                String diviceId1 = ((String) method.invoke(telephonyManager, id1)).trim();
                if (isValidImei(diviceId1)) {
                    result.add(diviceId1);
                }
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 展讯芯片
     *
     * @param context
     * @return
     */
    private static ArrayList getDeviceIdListFactory(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId0 = telephonyManager.getDeviceId().trim();

            ArrayList list = new ArrayList();
            if (isValidImei(deviceId0)) {
                list.add(deviceId0);
            }

            Class aClass = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method method = aClass.getMethod("getServiceName", String.class, Integer.TYPE);
            String serviceName = (String) method.invoke(aClass, "phone", 1);
            if (!TextUtils.isEmpty(serviceName) && !serviceName.equals(Context.TELEPHONY_SERVICE)) {
                telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
                String deviceId1 = telephonyManager.getDeviceId().trim();
                if (isValidImei(deviceId1)) {
                    list.add(deviceId1);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 高通芯片
     *
     * @param context
     * @return
     */
    private static ArrayList getDeviceIdListMsim(Context context) {
        try {
            Class mSimCalss = Class.forName("android.telephony.MSimTelephonyManager");
            Object service = context.getSystemService("phone_msim");
            Method method = mSimCalss.getMethod("getDeviceId", Integer.TYPE);
            String deviceId0 = ((String) method.invoke(service, 0)).trim();
            ArrayList result = new ArrayList();
            if (isValidImei(deviceId0)) {
                result.add(deviceId0);
            }
            String deviceId1 = ((String) method.invoke(service, 1)).trim();
            if (isValidImei(deviceId1)) {
                result.add(deviceId1);
            }

            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
