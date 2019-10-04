package com.telewave.lib.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码验证
 *
 * @author Brian
 * 2015年11月4日 下午3:42:42
 */
public class RegexUtils {
    /**
     * 手机号验证
     *
     * @param phonenumber
     * @return 验证通过返回true
     */
    public static boolean isMobile(String phonenumber) {
        boolean b = false;
        if (phonenumber == null || "".equals(phonenumber)) {
            return b;
        }
        Pattern pattern = null;
        Matcher m = null;
        pattern = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = pattern.matcher(phonenumber);
        b = m.matches();
        return b;
    }

    /**
     * 检验是否是邮箱
     */
    public static boolean isEmail(String mail) {
        boolean b = false;
        if (mail == null || "".equals(mail)) {
            return b;
        }
        Pattern pattern = null;
        Matcher m = null;
        pattern = Pattern.compile("^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"); // 验证邮箱
        m = pattern.matcher(mail);
        b = m.matches();
        return b;
    }

    /**
     * 检测是否为汉字
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }
}
