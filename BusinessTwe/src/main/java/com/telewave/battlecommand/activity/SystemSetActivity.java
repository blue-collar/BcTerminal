package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.imui.service.IMOperate;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.lib.widget.util.IMActivityUtil;
import com.telewave.battlecommand.utils.GlideCacheUtil;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DateUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.utils.OpenAutoStartUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;

/**
 * 系统设置
 */
public class SystemSetActivity extends BaseActivity {
    private static final String TAG = "SystemSetActivity";

    private ToggleButton newDisasterVoiceToggleBtn;
    private ToggleButton notifyVoiceToggleBtn;
    private ToggleButton rollcallVoiceToggleBtn;
    private ToggleButton printNewDisasterToggleBtn;
    private RelativeLayout yunxingSetLayout;
    private RelativeLayout clearCacheLayout;
    private RelativeLayout aboutSystemLayout;
    private TextView cacheSizeTextView;
    private TextView aboutSystemVersionTv;
    private TextView exitLayoutTv;

    private static final int LOGOUT_SUCCESS = 0x1004;
    private static final int LOGOUT_FAIL = 0x1005;

    private static final int CLEAR_CACHE = 0x1006;

    private static final int LOGIN_TYPE = 2;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case LOGOUT_SUCCESS:
                    DateUtils.clear(SystemSetActivity.this);
                    IMActivityUtil.finishAllActivity();
                    Intent intent = new Intent(SystemSetActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case LOGOUT_FAIL:
                    IMOperate.getInstance().login(SystemSetActivity.this);
                    ToastUtils.toastShortMessage("退出失败,请稍后再试!");
                    break;
                case CLEAR_CACHE:
                    ProgressDialogUtils.dismissDialog();
                    getCacheSize();
                    ToastUtils.toastShortMessage("清除本地缓存成功");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_set);
        initView();
        String moibleName = OpenAutoStartUtil.getMobileName();
        if (!TextUtils.isEmpty(moibleName)) {
            yunxingSetLayout.setVisibility(View.VISIBLE);
        } else {
            yunxingSetLayout.setVisibility(View.GONE);
        }
    }

    private void initView() {
        newDisasterVoiceToggleBtn = (ToggleButton) findViewById(R.id.new_disaster_voice_toggle_btn);
        newDisasterVoiceToggleBtn.setChecked(ConstData.isNewDisasterVoiceOpen);
        notifyVoiceToggleBtn = (ToggleButton) findViewById(R.id.notifiy_voice_toggle_btn);
        notifyVoiceToggleBtn.setChecked(ConstData.isNotifyVoiceOpen);
        rollcallVoiceToggleBtn = (ToggleButton) findViewById(R.id.rollcall_voice_toggle_btn);
        rollcallVoiceToggleBtn.setChecked(ConstData.isRollCallVoiceOpen);
        printNewDisasterToggleBtn = (ToggleButton) findViewById(R.id.print_new_disaster_btn);
        printNewDisasterToggleBtn.setChecked(ConstData.isPrintNewDisaster);
        yunxingSetLayout = (RelativeLayout) findViewById(R.id.yunxing_set_layout);
        clearCacheLayout = (RelativeLayout) findViewById(R.id.clear_cache_layout);
        aboutSystemLayout = (RelativeLayout) findViewById(R.id.about_system_layout);
        cacheSizeTextView = (TextView) findViewById(R.id.cache_size_textview);
        aboutSystemVersionTv = (TextView) findViewById(R.id.about_system_version);
        exitLayoutTv = (TextView) findViewById(R.id.exit_layout_textview);

        /**
         * 是否开启新警情声音提示
         */
        newDisasterVoiceToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConstData.isNewDisasterVoiceOpen = true;
                } else {
                    ConstData.isNewDisasterVoiceOpen = false;
                }
                SharePreferenceUtils.putDataSharedPreferences(SystemSetActivity.this, "isNewDisasterVoiceOpen", ConstData.isNewDisasterVoiceOpen);
            }
        });
        /**
         * 是否开启通知声音提示
         */
        notifyVoiceToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConstData.isNotifyVoiceOpen = true;
                } else {
                    ConstData.isNotifyVoiceOpen = false;
                }
                SharePreferenceUtils.putDataSharedPreferences(SystemSetActivity.this, "isNotifyVoiceOpen", ConstData.isNotifyVoiceOpen);
            }
        });
        /**
         * 是否开启通知声音提示
         */
        rollcallVoiceToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConstData.isRollCallVoiceOpen = true;
                } else {
                    ConstData.isRollCallVoiceOpen = false;
                }
                SharePreferenceUtils.putDataSharedPreferences(SystemSetActivity.this, "isRollCallVoiceOpen", ConstData.isRollCallVoiceOpen);
            }
        });
        /**
         * 是否打开新警情信息打印
         */
        printNewDisasterToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String brand = SystemProperties.get("ro.product.brand");
                if ("SUNMI".equals(brand)) {
                    if (isChecked) {
                        ConstData.isPrintNewDisaster = true;
                    } else {
                        ConstData.isPrintNewDisaster = false;
                    }
                    SharePreferenceUtils.putDataSharedPreferences(SystemSetActivity.this, "isPrintNewDisaster", ConstData.isPrintNewDisaster);
                } else {
                    ConstData.isPrintNewDisaster = false;
                    printNewDisasterToggleBtn.setChecked(false);
                    ToastUtils.toastShortMessage("当前设备不支持打印功能，无法开启");
                }
            }
        });
        yunxingSetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemSetActivity.this, OpenAutoStartActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 点击清理缓存
         *
         */
        clearCacheLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(SystemSetActivity.this).inflate(R.layout.custom_dialog, null);
                TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
                TextView dialog_msg = (TextView) dialogView
                        .findViewById(R.id.dialog_msg);
                tv_title.setText("温馨提示");
                dialog_msg.setText("您确定要清除本地缓存?");
                AlertDialog.Builder builder = new AlertDialog.Builder(SystemSetActivity.this);
                final AlertDialog dialog = builder.create();
                dialog.setView(dialogView, 0, 0, 0, 0);
                // 点击屏幕外侧，dialog不消失
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button btnOK = (Button) dialogView.findViewById(R.id.btn_ok);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialogUtils.showDialog(SystemSetActivity.this, "正在清除缓存数据...");
                        new Thread(new clearCache()).start();
                        dialog.dismiss();
                    }
                });
                Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        aboutSystemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemSetActivity.this, AboutSystemActivity.class);
                startActivity(intent);
            }
        });
        aboutSystemVersionTv.setText("V" + AppProxy.getInstance().getVERSION_NAME());
        /**
         * 点击退出登录
         */
        exitLayoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutViewWindow();
            }
        });
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCacheSize();
    }


    //退出登录
    private void logoutSystem(String loginName) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.logoutUrl, RequestMethod.POST);
        request.add("account", loginName);
        request.add("loginType", LOGIN_TYPE);
        NoHttpManager.getRequestInstance().add(SystemSetActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = LOGOUT_SUCCESS;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = LOGOUT_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                Log.e(TAG, "onFailed: " + url);
                msg.what = LOGOUT_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    private void showLogOutViewWindow() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null);
        TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        TextView dialog_msg = (TextView) dialogView
                .findViewById(R.id.dialog_msg);
        tv_title.setText("温馨提示");
        dialog_msg.setText("确认退出系统?");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setView(dialogView, 0, 0, 0, 0);
        // 点击屏幕外侧，dialog不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button btnOK = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMOperate.getInstance().loginOut(new IMOperate.IMLoginOutInter() {
                    @Override
                    public void success() {
                        String loginName = SharePreferenceUtils.getDataSharedPreferences(SystemSetActivity.this, "loginname");
                        logoutSystem(loginName);
                    }
                });
                dialog.dismiss();

            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 再创建一个内部类，用于清理内存，实现了一个Runnable，清理完后发一个消息
     */

    class clearCache implements Runnable {

        @Override
        public void run() {
            try {
                GlideCacheUtil.clearImageAllCache(SystemSetActivity.this);
                Thread.sleep(3000);
                if (GlideCacheUtil.getCacheSize(getApplicationContext()).startsWith("0KB")) {
                    mHandler.sendEmptyMessage(CLEAR_CACHE);
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    /**
     * 获取缓存大小
     */
    private void getCacheSize() {
        String cacheSizeStr = null;
        try {
            cacheSizeStr = GlideCacheUtil.getCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheSizeTextView.setText(cacheSizeStr);
    }
}
