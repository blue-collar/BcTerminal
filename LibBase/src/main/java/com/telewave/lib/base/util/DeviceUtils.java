package com.telewave.lib.base.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import com.telewave.lib.base.AppProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des 获取设备信息
 */
public class DeviceUtils {

    private static Map<String, String> sDeviceInfoMap = null;  //cache

    private static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static Map<String, String> getDeviceInfo(Context context) {
        if (sDeviceInfoMap == null) {
            sDeviceInfoMap = new HashMap<>();
            try {
                android.telephony.TelephonyManager tm =
                        (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = null;
                if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                    deviceId = tm.getDeviceId();
                }
                String mac = null;

                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();
                    byte[] addr = networkInterface.getHardwareAddress();

                    if (addr == null || addr.length == 0) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b : addr) {
                        sb.append(String.format("%02x:", b));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    mac = sb.toString();
                }

                sDeviceInfoMap.put("mac", mac);
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = mac;
                }
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                            android.provider.Settings.Secure.ANDROID_ID);
                }
                sDeviceInfoMap.put("deviceId", deviceId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sDeviceInfoMap;
    }


    public static String getDeviceIp() {
        WifiManager wm = (WifiManager) AppProxy.getApplication().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (!wm.isWifiEnabled()) {
            return getIpAddressString();
        }
        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip = iToIp(ipAdd);
        return ip;
    }

    private static String iToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /*获取设备IPv4地址对应的字符串*/
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*获取设备WiFi网络IPv4地址对应的字符串*/
    public static String getWiFiIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                if (netI.getDisplayName().equals("wlan0") || netI.getDisplayName().equals("eth0")) {
                    for (Enumeration<InetAddress> enumIpAddr = netI
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isXiaomiMobilePhone() {
        return "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isVivoMobilePhone() {
        return "vivo".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isHuaweiMobilePhone() {
        return "huawei".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isMeizuMobilePhone() {
        return "meizu".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isLocationNeadGPSDevice() {
        return isHuaweiMobilePhone();
    }

    public static final String ROM_MIUI = "MIUI";
    public static final String ROM_EMUI = "EMUI";
    public static final String ROM_FLYME = "FLYME";
    public static final String ROM_OPPO = "OPPO";
    public static final String ROM_SMARTISAN = "SMARTISAN";
    public static final String ROM_VIVO = "VIVO";
    public static final String ROM_QIKU = "QIKU";

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private static String sName;
    private static String sVersion;

    public static boolean isEmui() {
        return check(ROM_EMUI);
    }

    public static boolean isMiui() {
        return check(ROM_MIUI);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean is360() {
        return check(ROM_QIKU) || check("360");
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static boolean isMui5() {
        boolean isMui5 = false;
        String product = Build.PRODUCT;
        if (product.equals("meizu_15_CN")) {
            isMui5 = true;
        }
        return isMui5;
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    private static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        }

        if (!TextUtils.isEmpty(sVersion = getSystemProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getSystemProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getSystemProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getSystemProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getSystemProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        return sName.equals(rom);
    }

    public static String getSystemProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            //TweLog.e(TAG, "Unable to read prop " + name, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }
}
