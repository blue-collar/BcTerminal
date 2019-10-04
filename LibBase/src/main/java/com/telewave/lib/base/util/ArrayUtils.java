package com.telewave.lib.base.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: rick_tan
 * @Date: 19-7-23
 * @Version: v1.0
 * @Des 数组工具类
 */
public final class ArrayUtils {
    private ArrayUtils() {
        throw new AssertionError("no instances");
    }

    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked") T[] c =
                (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    /**
     * 交错合并数组
     */
    public static <T> List<T> cross(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>();
        result.addAll(a);
        result.addAll(b); // 初始化数组大小, 否则无法使用set
        for (int i = 0, j; i < a.size(); i++) {
            j = 2 * i;
            if (j > 2 * b.size()) {
                j = b.size() + i;
            }
            result.set(j, a.get(i));
        }
        for (int i = 0, j; i < b.size(); i++) {
            j = 2 * i + 1;
            if (j > 2 * a.size()) {
                j = a.size() + i;
            }
            result.set(j, b.get(i));
        }
        return result;
    }

    public static int idealLongArraySize(int need) {
        return idealByteArraySize(need * 8) / 8;
    }

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++)
            if (need <= (1 << i) - 12)
                return (1 << i) - 12;

        return need;
    }
}
