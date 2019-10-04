package com.telewave.battlecommand.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.Version;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.HttpResponseUtil;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.service.UpdateService;
import com.telewave.battlecommand.view.MyDialog;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DoubleClickUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.File;
import java.lang.ref.WeakReference;

import static com.telewave.lib.base.util.AppUtils.compareVersion;
import static com.telewave.lib.base.util.AppUtils.installApk;

/**
 * 关于系统页
 */
public class AboutSystemActivity extends BaseActivity {
    private static final String TAG = "AboutSystemActivity";

    private static final int GET_UPDATE_SUCCESS = 0x1001;
    private static final int GET_UPDATE_FAIL = 0x1002;
    private static final int GET_NO_UPDATE = 0x1003;

    private TextView currentVersion;
    private RelativeLayout checkUpdateLayout;
    private RelativeLayout updateRecordLayout;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_UPDATE_SUCCESS:
                    ToastUtils.toastShortMessage("获取版本信息成功");
                    break;
                case GET_NO_UPDATE:
                    ToastUtils.toastShortMessage("已是最新版本");
                    break;
                case GET_UPDATE_FAIL:
                    ToastUtils.toastShortMessage("获取版本信息失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());


    @Override
    public void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_system);
        initView();
    }


    private void initView() {
        currentVersion = (TextView) findViewById(R.id.system_about_current);
        checkUpdateLayout = (RelativeLayout) findViewById(R.id.check_update_layout);
        updateRecordLayout = (RelativeLayout) findViewById(R.id.update_record_layout);
        currentVersion.setText(getString(R.string.app_name) + "V" + AppProxy.getInstance().getVERSION_NAME());
        /**
         * 点击检查更新
         *
         */
        checkUpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewVersion();
            }
        });
        updateRecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(AboutSystemActivity.this, UpdateVersionRecordActivity.class);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //获取新的版本信息
    private void getNewVersion() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getAppUpdateInfo, RequestMethod.GET);
        //此处版本号可以传任意值
        request.add("version", AppProxy.getInstance().getVERSION_NAME());
        request.add("type", 2);
        NoHttpManager.getRequestInstance().add(AboutSystemActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            Version version = gson.fromJson(responseBean.getData(), Version.class);
                            String versionStr = version.getDataversion();
                            if (versionStr.substring(0, 1).equals("V") || versionStr.substring(0, 1).equals("v")) {
                                versionStr = versionStr.substring(1);
                            }
                            String currentVersionStr = AppProxy.getInstance().getVERSION_NAME();
                            if (compareVersion(versionStr, currentVersionStr) > 0) {
                                File updateSystemDir = new File(ConstData.UPDATE_SYSTEM_DIR);
                                if (!updateSystemDir.exists()) {
                                    updateSystemDir.mkdirs();
                                }
                                File installFile = new File(ConstData.UPDATE_SYSTEM_DIR, getString(R.string.app_name) + version.getDataversion() + ".apk");
                                if (installFile.exists()) {
                                    isInstallDialog(installFile, version.getDataversion());
                                } else {
                                    updateDialog(version);
                                }
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_NO_UPDATE;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_UPDATE_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                HttpResponseUtil.showResponse(AboutSystemActivity.this, exception, "获取版本信息失败");
                Message msg = mHandler.obtainMessage();
                msg.what = GET_UPDATE_FAIL;
                mHandler.sendMessage(msg);
            }
        }, true, true);
    }

    public void isInstallDialog(final File installFile, String content) {
        View view = LayoutInflater.from(AboutSystemActivity.this).inflate(R.layout.new_version_exist, null);
        TextView versionContent = (TextView) view.findViewById(R.id.new_version_content);
        LinearLayout checkedLayout = (LinearLayout) view.findViewById(R.id.btn_checked_layout);
        LinearLayout ignoreLayout = (LinearLayout) view.findViewById(R.id.btn_ignore_layout);
        AlertDialog.Builder builder = new AlertDialog.Builder(AboutSystemActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        versionContent.setText("已下载最新版版本" + content + "是否安装？");
        checkedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApk(installFile, AboutSystemActivity.this);
                dialog.dismiss();
            }
        });
        ignoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 更新dialog
     */
    public void updateDialog(final Version version) {
        View dialogView = LayoutInflater.from(AboutSystemActivity.this).inflate(
                R.layout.lib_update_app_dialog, null);
        TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        TextView tv_update_info = (TextView) dialogView.findViewById(R.id.tv_update_info);
        //进度条
//        final NumberProgressBar mNumberProgressBar = (NumberProgressBar) dialogView.findViewById(R.id.npb);
        final Dialog dialog = new MyDialog(AboutSystemActivity.this, dialogView, R.style.UpdateAppDialog);
        //设置点击外部区域不消失
        dialog.setCancelable(false);
        // 把自定义view加上去
        dialog.getWindow().setContentView(dialogView);
        dialog.show();

        tv_title.setText("是否升级到" + version.getDataversion() + "版本");
        tv_update_info.setText(version.getUpdateInfo());
        // 点击屏幕外侧，dialog不消失
        dialog.setCanceledOnTouchOutside(false);
        Button btnOK = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toastShortMessage("正在后台下载...");
                Intent updateIntent = new Intent(AboutSystemActivity.this, UpdateService.class);
                updateIntent.putExtra("version", version.getDataversion());
                updateIntent.putExtra("down_url", ConstData.urlManager.baseFilesURL + version.getDownUrl());
                startService(updateIntent);
                dialog.dismiss();
            }
        });
        LinearLayout ll_close = (LinearLayout) dialogView.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}

