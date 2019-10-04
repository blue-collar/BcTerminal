package com.tencent.qcloud.tim.uikit.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

/**
 * 开启各品牌手机自启动。白名单工具类
 *
 * @author liwh
 * @date 2019/5/22
 */
public class OpenAutoStartUtil {

    public static final String TAG = "OpenAutoStartUtil";
    public static final String HUAWEI = "huawei";
    public static final String HONOR = "honor";
    public static final String XIAOMI = "xiaomi";
    public static final String OPPO = "oppo";
    public static final String VIVO = "vivo";
    public static final String THREESIXZERO = "360";
    public static final String MEIZU = "meizu";
    public static final String SAMSUNG = "samsung";
    public static final String ONEPLUS = "oneplus";
    public static final String ZTE = "zte";
    public static final String LETV = "letv";

    /**
     * Get Mobile Type
     *
     * @return
     */
    public static String getMobileType() {
        return Build.MANUFACTURER;
    }


    /**
     * 去自启动或受保护或白名单页面
     *
     * @param context
     */
    public static void goStartUpPage(Context context) {
        int androidVersion = getAndroidVersion();
        Log.i(TAG, "androidVersion is: " + androidVersion);
        if (androidVersion <= 0) {
            Log.e(TAG, "androidVersion <= 0");
            return;
        }
        String phoneType = getMobileType();
        Log.i(TAG, "phoneType is :" + phoneType);
        if (TextUtils.isEmpty(phoneType)) {
            Log.e(TAG, "phoneType is null or phoneType is '' ");
            return;
        }
        phoneType = phoneType.trim().toLowerCase();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = null;
        try {
            switch (phoneType) {
                //华为、荣耀手机是一样的系统
                case HUAWEI:
                    componentName = getHuaWeiComponentName(androidVersion);
                    break;
                case HONOR:
                    componentName = getHuaWeiComponentName(androidVersion);
                    break;
                case XIAOMI:
                    componentName = getXiaoMiComponentName(androidVersion);
                    break;
                case OPPO:
                    componentName = getOppoComponentName(androidVersion);
                    break;
                case VIVO:
                    componentName = getVivoComponentName(androidVersion);
                    break;
                case THREESIXZERO:
                    componentName = get360ComponentName(androidVersion);
                    break;
                case MEIZU:
                    componentName = getMeizuComponentName(androidVersion);
                    break;
                case SAMSUNG:
                    componentName = getSamsungComponentName(androidVersion);
                    break;
                case ONEPLUS:
                    componentName = getOnePlusComponentName(androidVersion);
                    break;
                case ZTE:
                    componentName = getZTEComponentName(androidVersion);
                    break;
                case LETV:
                    componentName = getLetvComponentName(androidVersion);
                    break;
                default:
                    // 以上主流机型，针对于其他设备，调整当前系统app查看详情界面
                    // 在此根据用户手机当前版本跳转系统设置界面
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                    }
                    break;
            }
            intent.setComponent(componentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "goStartUpPage has Exception");
            //发送异常去设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 不同手机保活友好提示
     */
    public static String getMobileFriendTip() {
        String phoneTip = null;
        String phoneType = getMobileType();
        Log.i(TAG, "phoneType is :" + phoneType);
        if (TextUtils.isEmpty(phoneType)) {
            Log.e(TAG, "phoneType is null or phoneType is '' ");
            return null;
        }
        phoneType = phoneType.trim().toLowerCase();
        switch (phoneType) {
            case HUAWEI:
                phoneTip = "华为手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置关闭自动管理，开启允许自启动、允许后台活动，如已设置完成请忽略。";
                break;
            case HONOR:
                phoneTip = "荣耀手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置关闭自动管理，开启允许自启动、允许后台活动，如已设置完成请忽略。";
                break;
            case XIAOMI:
                phoneTip = "小米手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置自启动，如已设置完成请忽略。";
                break;
            case OPPO:
                phoneTip = "OPPO手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，进入权限隐私->自启动管理，然后需要将App设置开启自启动，如已设置完成请忽略。";
                break;
            case VIVO:
                phoneTip = "vivo手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置自启动，如已设置完成请忽略。";
                break;
            case THREESIXZERO:
                phoneTip = "360手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置开启锁屏清理白名单，如已设置完成请忽略。";
                break;
            case MEIZU:
                phoneTip = "魅族手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置允许后台运行，如已设置完成请忽略。";
                break;
            case SAMSUNG:
                phoneTip = "三星手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，进入智能管理器->应用程序管理->管理自动运行，然后需要将App设置开启自启动，如已设置完成请忽略。";
                break;
            case ONEPLUS:
                phoneTip = "一加手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置允许后台运行，如已设置完成请忽略。";
                break;
            case ZTE:
                phoneTip = "中兴在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要进入自启动管理将App设置自启动，如已设置完成请忽略。";
                break;
            case LETV:
                phoneTip = "乐视手机在黑屏待机后会自动清理后台运行的软件，这样影响了我们正常通信服务，需要将App设置自启动，如已设置完成请忽略。";
                break;
            default:
                break;
        }
        return phoneTip;
    }

    /**
     * 获取手机名称
     */
    public static String getMobileName() {
        String moibleName = null;
        String phoneType = getMobileType();
        Log.i(TAG, "phoneType is :" + phoneType);
        if (TextUtils.isEmpty(phoneType)) {
            Log.e(TAG, "phoneType is null or phoneType is '' ");
            return null;
        }
        phoneType = phoneType.trim().toLowerCase();
        switch (phoneType) {
            case HUAWEI:
                moibleName = "华为";
                break;
            case HONOR:
                moibleName = "荣耀";
                break;
            case XIAOMI:
                moibleName = "小米";
                break;
            case OPPO:
                moibleName = "OPPO";
                break;
            case VIVO:
                moibleName = "vivo";
                break;
            case THREESIXZERO:
                moibleName = "360";
                break;
            case MEIZU:
                moibleName = "魅族";
                break;
            case SAMSUNG:
                moibleName = "三星";
                break;
            case ONEPLUS:
                moibleName = "一加";
                break;
            case ZTE:
                moibleName = "中兴";
                break;
            case LETV:
                moibleName = "乐视";
                break;
            default:
                break;
        }
        return moibleName;
    }

    /**
     * 华为手机
     */
    public static ComponentName getHuaWeiComponentName(int androidVersion) {
        ComponentName componentName = null;
        if (androidVersion >= 9) {
            //华为9.0 自启动页面
            componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } else if (androidVersion >= 8) {
            //华为8.0 自启动页面
            componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity");
        } else {
            //华为7.0以下包括7.0打开受保护app页面
            componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
        }
        return componentName;
    }


    /**
     * 小米手机
     */
    public static ComponentName getXiaoMiComponentName(int androidVersion) {
        ComponentName componentName = null;
        //红米Note4 android 6.0 小米手机Android 8.0 小米8.0.1 测试通过
        componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
        return componentName;
    }

    /**
     * 魅族手机
     *
     * @param androidVersion
     * @return
     */
    public static ComponentName getMeizuComponentName(int androidVersion) {
        ComponentName componentName = null;
        //魅族7.0.1
        componentName = ComponentName.unflattenFromString("com.meizu.safe" + "/.permission.SmartBGActivity");
        return componentName;
    }

    /**
     * 360手机
     *
     * @param androidVersion
     * @return
     */
    public static ComponentName get360ComponentName(int androidVersion) {
        ComponentName componentName = null;
        // 360手机 360 N6 Android 7.1.1 有自启动和"锁屏清理百名单",自启动没啥用,然后跳到"锁屏清理百名单" 测试成功
        componentName = ComponentName.unflattenFromString("com.qiku.android.security" + "/.ui.activity.memclean.MemoryCleanSettingActivity");
        return componentName;
    }

    /**
     * oppo手机
     *
     * @param androidVersion
     * @return
     */
    public static ComponentName getOppoComponentName(int androidVersion) {
        ComponentName componentName = null;//oppo R15  Android 8.1.0 测试通过 oppo R11 Android 7.1.1
        componentName = new ComponentName("com.coloros.safecenter", "com.coloros.privacypermissionsentry.PermissionTopActivity");
        return componentName;
    }

    /**
     * 三星手机Galaxy S6 edge Android 7.0手机
     */
    public static ComponentName getSamsungComponentName(int androidVersion) {
        ComponentName componentName = null;//三星手机Galaxy S6 edge Android 7.0手机跳自启动有权限问题,所以会跳"智能管理器"页面
        componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity");
        return componentName;
    }

    /**
     * vivo手机
     *
     * @param androidVersion
     * @return
     */
    public static ComponentName getVivoComponentName(int androidVersion) {
        ComponentName componentName = null;
        if (androidVersion < 6) {
            //vivo5.0.2打开百名单页面
            componentName = new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
            //这个是去i管家界面,后面再讨论是否去这个主界面
//            componentName = new ComponentName("com.iqoo.secure", "com.iqoo.secure.MainActivity");
        } else {
            //vivo x9i Android 7.1.2
            componentName = new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity");
        }
        return componentName;
    }

    /**
     * 一加手机
     */
    public static ComponentName getOnePlusComponentName(int androidVersion) {
        ComponentName componentName = null;
        componentName = new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity");
        return componentName;
    }

    /**
     * 中兴手机
     */
    public static ComponentName getZTEComponentName(int androidVersion) {
        ComponentName componentName = null;
        componentName = ComponentName.unflattenFromString("com.zte.heartyservice" + "/.autorun.AppAutoRunManager");
        return componentName;
    }

    /**
     * 乐视手机
     */
    public static ComponentName getLetvComponentName(int androidVersion) {
        ComponentName componentName = null;
        componentName = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity");
        return componentName;
    }


    /**
     * 得到当前手机安卓系统版本号,只保留最前面的一位数
     *
     * @return
     */
    public static int getAndroidVersion() {
        String androidVersion = Build.VERSION.RELEASE;
        if (androidVersion == null)
            return 0;
        //获取第一个小数点前面的数字
        Log.e(TAG, "androidVersion is" + androidVersion);
        String[] versions = androidVersion.split("\\.");
        int value = 0;
        if (versions != null && versions.length > 0) {
            try {
                value = Integer.parseInt(versions[0]);
                Log.e(TAG, "getAndroidVersion value is:" + value);
                return value;
            } catch (Exception e) {
                Log.e(TAG, "get android version fail");
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
}
