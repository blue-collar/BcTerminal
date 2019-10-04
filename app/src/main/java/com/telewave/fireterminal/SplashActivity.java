package com.telewave.fireterminal;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.telewave.battlecommand.R;
import com.telewave.battlecommand.activity.MainActivity;
import com.telewave.battlecommand.utils.ShowWindowUtils;
import com.telewave.battlecommand.utils.fw_permission.FloatWinPermissionCompat;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.log.TweLog;
import com.telewave.lib.router.BaseJumper;
import com.telewave.lib.router.RouterPath;
import com.telewave.lib.widget.BaseActivity;
import com.wcl.notchfit.NotchFit;
import com.wcl.notchfit.args.NotchProperty;
import com.wcl.notchfit.args.NotchScreenType;
import com.wcl.notchfit.core.OnNotchCallBack;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = RouterPath.SPLASH_ACTIVITY_PATH)
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    //权限申请帮助类
    private PermissionHelper permissionHelper;

    private static final int INIT_TAG = 1;
    private static final int NO_DEVICEID = 2;
    //是否获取悬浮窗权限
    private boolean isGrantAlertWindow = false;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case INIT_TAG:
                    setGuidBtn();
                    break;
                case NO_DEVICEID:
                    ShowWindowUtils.showLoginExit(SplashActivity.this, ShowWindowUtils.NO_DEVICEID);
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        // 当前类不是该Task的根部，那么之前启动
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        // App正常的启动，设置App的启动状态为正常启动
        AppProxy.APP_STATUS = AppProxy.APP_STATUS_NORMAL;
        //刘海屏适配
        NotchFit.fit(this, NotchScreenType.FULL_SCREEN, new OnNotchCallBack() {
            @Override
            public void onNotchReady(NotchProperty notchProperty) {
                if (notchProperty.isNotchEnable()) {
                    //通过获取的notchHeight刘海高度参数，对UI布局进行适配
                    int notchHeight = notchProperty.getNotchHeight();
                }
            }
        });
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            isGrantAlertWindow = Settings.canDrawOverlays(this);
////            没有授权进行授权回调
//            if (!isGrantAlertWindow) {
//                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 10);
//            }
//        } else {
//            isGrantAlertWindow = true;
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isGrantAlertWindow = FloatWinPermissionCompat.getInstance().check(SplashActivity.this);
//            没有授权进行授权回调
            if (!isGrantAlertWindow) {
                checkPermissionAndShow();
            }
        } else {
            isGrantAlertWindow = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGrantAlertWindow) {
            initTerminalInfo();
        }
    }

    //终端认证状态判断
    private boolean isTerminalRzzt() {
        ConstData.isTerminalChecked = SharePreferenceUtils.getBooleanDataSharedPreferences(SplashActivity.this, "terminalrzzt");
        return ConstData.isTerminalChecked;
    }

    //用户账号认证状态判断
    private boolean isUserRzzt() {
        String rzzt = SharePreferenceUtils.getDataSharedPreferences(SplashActivity.this, "rzzt");
        boolean autoLogin = SharePreferenceUtils.getOtherBooleanDataSharedPreferences(SplashActivity.this, "auto_login");
        if (rzzt.equals("1") && autoLogin) {
            return true;
        } else {
            return false;
        }
    }

    private void setGuidBtn() {
        if (isTerminalRzzt()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isUserRzzt()) {
                        TweLog.e(TAG, "跳转到主页");
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        TweLog.e(TAG, "跳转到登录页");
                        BaseJumper.jump(RouterPath.LOGIN_MAIN_PATH);    // SplashActivity.this, -1, null
                    }

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }, 1500);
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    TweLog.e(TAG, "跳转到登录向导页");
                    BaseJumper.jump(RouterPath.LOGIN_FIRST_PATH);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }, 1500);
        }
    }

    private void initTerminalInfo() {
        permissionHelper = new PermissionHelper(this);
        //申请SDcard权限,当申请通过之后
        permissionHelper.requestPermissions(getString(R.string.permission_hint_msg), new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                /* 获取设备ID 、号码*/
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                String deviceID = telephonyManager.getDeviceId();
                TweLog.i(TAG, "deviceID is " + deviceID);
                if (deviceID == null) {
                    //android.provider.Settings;
                    deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    TweLog.i(TAG, "deviceID is " + deviceID);
                }
                final String subscriberId = telephonyManager.getSubscriberId();
                TweLog.i(TAG, "subscriberId is " + subscriberId);
                if (!TextUtils.isEmpty(deviceID)) {
                    ConstData.deviceId = deviceID;
                    ConstData.deviceNumber = subscriberId;
                    init();
                } else if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    TweLog.e(TAG, "no READ_PHONE_STATE permission, will return~");
                    return;
                } else if (!TextUtils.isEmpty(subscriberId)) {
                    ConstData.deviceId = subscriberId;
                    ConstData.deviceNumber = subscriberId;
                    init();
                } else {
                    String brand = SystemProperties.get("ro.product.brand");
                    TweLog.i(TAG, "brand is " + brand);
                    if ("SUNMI".equals(brand)) {
                        String sunmi = null;
                        try {
                            Class c = Class.forName("android.os.SystemProperties");
                            Method get = c.getMethod("get", String.class);
                            sunmi = (String) get.invoke(c, "ro.serialno");
                            TweLog.e(TAG, "sunmi the sn:" + (String) get.invoke(c, "ro.serialno"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ConstData.deviceId = sunmi;
                        ConstData.deviceNumber = sunmi;
                        init();
                    } else {
                        if (getUniquePhoneId() != null) {
                            ConstData.deviceId = getUniquePhoneId();
                            ConstData.deviceNumber = getUniquePhoneId();
                            init();
                        } else {
                            mHandler.sendEmptyMessage(NO_DEVICEID);
                        }
                    }
                }

                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(metrics);
                ConstData.screenWidth = metrics.widthPixels;
                ConstData.screenHeight = metrics.heightPixels;
            }

            @Override
            public void doAfterDenied(String... ermission) {
//                permissionHelper.openPermissionsSetting("权限已经被关闭,请手动打开");
            }
        }, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void init() {
        ConstData.initFromSp(SplashActivity.this);

        mHandler.sendEmptyMessage(INIT_TAG);
    }

    //将权限交给permissionhelper处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 启动页 屏蔽返回按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getUniquePhoneId() {
        String uniquePhoneId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        TweLog.e(TAG, "uniquePhoneId:" + uniquePhoneId);
        return uniquePhoneId;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            isGrantAlertWindow = FloatWinPermissionCompat.getInstance().check(SplashActivity.this);
            if (!isGrantAlertWindow) {
                ToastUtils.toastShortMessage("应用需要悬浮窗口权限");
                System.exit(0);
            } else {
                initTerminalInfo();
            }
        } else {
            initTerminalInfo();
        }
    }

    protected void checkPermissionAndShow() {
        // 检查是否已经授权
        if (!FloatWinPermissionCompat.getInstance().check(SplashActivity.this)) {
            // 授权提示
            new AlertDialog.Builder(SplashActivity.this).setTitle(getString(R.string.permission_float_win_msg))
                    .setMessage("你的手机没有授权" + getString(R.string.app_name) + "获得悬浮窗权限，部分功能将无法正常使用")
                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 显示授权界面
//                            try {
//                                FloatWinPermissionCompat.getInstance().apply(SplashActivity.this);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName())), 10);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();
        }
    }
}

