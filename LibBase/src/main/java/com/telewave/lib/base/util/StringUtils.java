package com.telewave.lib.base.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理
 */

public class StringUtils {
    //包含中文
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    //只包含字母和数字
    public static boolean isLetterAndNumber(String str) {
        Pattern p = Pattern.compile("^[\\w]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //c0-a8-a6-3c-1f90-c0-a8-a6-3d-5
    public static boolean isFomater(String str) {
        Pattern p = Pattern.compile("^[\\w]+[-\\w]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @param newversion
     * @param currentversion
     * @return if version1 > version2, return 1, if equal, return 0, else return
     * -1
     */
    public static int VersionComparison(String newversion, String currentversion) {
        if (TextUtils.isEmpty(newversion) || TextUtils.isEmpty(currentversion)) {
            return -1;
        }

        int index1 = 0;
        int index2 = 0;
        while (index1 < newversion.length() && index2 < currentversion.length()) {
            int[] number1 = getValue(newversion, index1);
            int[] number2 = getValue(currentversion, index2);

            if (number1[0] < number2[0]) {
                return -1;
            } else if (number1[0] > number2[0]) {
                return 1;
            } else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == newversion.length() && index2 == currentversion.length())
            return 0;
        if (index1 < newversion.length())
            return 1;
        else
            return -1;
    }

    /**
     * @param version
     * @param index   the starting point
     * @return the number between two dots, and the index of the dot
     */
    private static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }

    //解密扫码得到的字符串
    public static String[] deCryptLicense(String recode) {
        String desResult = "";
        DesUtils des = new DesUtils("sundun");
        desResult = des.decrypt(recode);
        try {
            String[] str = desResult.split("-");
            StringBuilder ip1 = new StringBuilder("");
            StringBuilder port1 = new StringBuilder("");
            StringBuilder ip2 = new StringBuilder("");
            StringBuilder port2 = new StringBuilder("");
            StringBuilder ip3 = new StringBuilder("");
            StringBuilder port3 = new StringBuilder("");
            StringBuilder licenseNum = new StringBuilder("");
            StringBuilder mqip = new StringBuilder("");
            for (int i = 0; i < str.length; i++) {
                String strSub = str[i];
                int number = Integer.parseInt(strSub, 16);
                //0-3位 管理后台IP
                //第4位 管理后台端口号
                //5-8位 ActiveMQ通信IP
                //第9位 ActiveMQ通信端口号
                //10-13位 APP中web页面IP
                //第14位 APP中web页面端口号
                if (i < 3) {
                    ip1.append(number + ".");
                } else if (i == 3) {
                    ip1.append(number);
                } else if (i == 4) {
                    port1.append(number);
                } else if (i < 8) {
                    ip2.append(number + ".");
                } else if (i == 8) {
                    ip2.append(number);
                } else if (i == 9) {
                    port2.append(number);
                } else if (i < 13) {
                    ip3.append(number + ".");
                } else if (i == 13) {
                    ip3.append(number);
                } else {
                    port3.append(number);
                }
            }
            return new String[]{String.valueOf(ip1), String.valueOf(port1), String.valueOf(ip2), String.valueOf(port2), String.valueOf(ip3), String.valueOf(port3)};
        } catch (Exception e) {
            return null;
        }
    }
}
