package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.telewave.battlecommand.http.HttpCode;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.StringUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


public class ScanCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final String TAG = ScanCodeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    //是否开启闪光灯 默认关闭
    private boolean isFlashOn = false;
    private ImageView flashLightImg;
    //从相册获取图片并扫描
    private TextView pictureScan;
    private ZXingView mZXingView;

    @Override
    public void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scan_code);
        mZXingView = findViewById(R.id.zxingview);
        mZXingView.setDelegate(ScanCodeActivity.this);
        flashLightImg = (ImageView) findViewById(R.id.flash_light_img);
        pictureScan = (TextView) findViewById(R.id.scan_text);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanCodeActivity.this, FirstLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        flashLightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    flashLightImg.setImageResource(R.mipmap.flash_on);
                    mZXingView.closeFlashlight(); // 关闭闪光灯
                    isFlashOn = false;
                } else {
                    flashLightImg.setImageResource(R.mipmap.flash_off);
                    mZXingView.openFlashlight(); // 打开闪光灯
                    isFlashOn = true;
                }
            }
        });

        pictureScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相册
                PictureSelector.create(ScanCodeActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageSpanCount(4)
                        .isCamera(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFlashOn) {
            flashLightImg.setImageResource(R.mipmap.flash_on);
            isFlashOn = false;
        }
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        vibrate();
        if (TextUtils.isEmpty(result)) {
            ToastUtils.toastShortMessage("未发现二维码");
            mZXingView.startSpot(); // 开始识别
        } else {
            // 数据返回
            String recode = reCode(result.toString());
            checkRecodResult(recode);
        }

    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> images = PictureSelector.obtainMultipleResult(data);
                    final String picturePath = images.get(0).getPath();
                    // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
                    mZXingView.decodeQRCode(picturePath);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int MOBILE_INFO = 0x0011;

    private void checkRecodResult(final String recode) {
        //解密license，并设置IP
        String[] license = StringUtils.deCryptLicense(recode);
        if (license == null) {
            ToastUtils.toastShortMessage("license解析错误，请重新扫描");
            mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
            return;
        }
        Log.e("license", license[0]);
        Log.e("license", license[1]);
        Log.e("license", license[2]);
        Log.e("license", license[3]);
        Log.e("license", license[4]);
        Log.e("license", license[5]);


        ConstData.urlManager.serverIp = license[0];
        ConstData.urlManager.serverPort = license[1];
        ConstData.urlManager.activemqIp = license[2];
        ConstData.urlManager.activemqPort = license[3];
        ConstData.urlManager.appWebIp = license[4];
        ConstData.urlManager.appWebPort = license[5];
        ConstData.urlManager.setBaseUrl(ConstData.urlManager.serverIp, ConstData.urlManager.serverPort, ConstData.urlManager.activemqIp,
                ConstData.urlManager.activemqPort, ConstData.urlManager.appWebIp, ConstData.urlManager.appWebPort);
        //保存ip\port
        SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "server_ip", ConstData.urlManager.serverIp);
        SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "server_port", ConstData.urlManager.serverPort);
        SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "activemq_ip", ConstData.urlManager.activemqIp);
        SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "activemq_port", ConstData.urlManager.activemqPort);
        SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "appweb_ip", ConstData.urlManager.appWebIp);
        SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "appweb_port", ConstData.urlManager.appWebPort);

        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.httpCheckMobileInfo, RequestMethod.POST);
        // 添加请求参数
        request.add("imei", ConstData.deviceId);
        request.add("imsi", ConstData.deviceNumber);

        Log.e("result", "checkRecodResult: " + ConstData.deviceId);
        Log.e("result", "checkRecodResult: " + ConstData.deviceNumber);
        NoHttpManager.getRequestInstance().add(ScanCodeActivity.this, MOBILE_INFO, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, Response<String> response) {
                if (what == MOBILE_INFO) {
                    // 响应结果
                    String result = response.get();
                    Log.e("result", "onSucceed: " + result);
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            ToastUtils.toastShortMessage("终端绑定成功");
                            //将终端认证信息设定为true
                            ConstData.isTerminalChecked = true;
                            SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "terminalrzzt", ConstData.isTerminalChecked);
                            Intent intent = new Intent(ScanCodeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (responseBean.getCode() == HttpCode.TERMINAL_HAVE_BOUND) {
                                ToastUtils.toastShortMessage(responseBean.getMsg());
                                //将终端认证信息设定为true
                                ConstData.isTerminalChecked = true;
                                SharePreferenceUtils.putDataSharedPreferences(ScanCodeActivity.this, "terminalrzzt", ConstData.isTerminalChecked);
                                Intent intent = new Intent(ScanCodeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.toastShortMessage(responseBean.getMsg());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
            }
        }, false, true);

    }

    /**
     * 中文乱码
     * 暂时解决大部分的中文乱码
     *
     * @return
     */
    private String reCode(String str) {
        String formart = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return formart;
    }
}