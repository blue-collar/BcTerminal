package com.telewave.battlecommand.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * 输入IP地址、端口号连接高级页面
 *
 * @author liwh
 * @date 2019/3/20
 */
public class InputLinkAddressMoreActivity extends BaseActivity {

    EditText inputIPNumberOne;
    EditText inputPortNumberOne;
    EditText inputIPNumberTwo;
    EditText inputPortNumberTwo;
    EditText inputIPNumberThree;
    EditText inputPortNumberThree;
    //确认按钮
    Button ensure;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input_link_address_more);
        initView();
    }

    private void initView() {
        inputIPNumberOne = (EditText) findViewById(R.id.ip_number_one);
        inputPortNumberOne = (EditText) findViewById(R.id.port_number_one);
        inputIPNumberTwo = (EditText) findViewById(R.id.ip_number_two);
        inputPortNumberTwo = (EditText) findViewById(R.id.port_number_two);
        inputIPNumberThree = (EditText) findViewById(R.id.ip_number_three);
        inputPortNumberThree = (EditText) findViewById(R.id.port_number_three);

        ensure = (Button) findViewById(R.id.btn_ok);
        ensure.setOnClickListener(myClickListener);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputLinkAddressMoreActivity.this, FirstLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int vId = v.getId();
            if (R.id.btn_ok == vId) {
                String ipNumberOne = inputIPNumberOne.getText().toString();
                String portNumberOne = inputPortNumberOne.getText().toString();
                String ipNumberTwo = inputIPNumberTwo.getText().toString();
                String portNumberTwo = inputPortNumberTwo.getText().toString();
                String ipNumberThree = inputIPNumberThree.getText().toString();
                String portNumberThree = inputPortNumberThree.getText().toString();
                boolean isOneOK = false;
                boolean isTwoOK = false;
                boolean isThreeOK = false;

                if (!TextUtils.isEmpty(ipNumberOne) && !TextUtils.isEmpty(portNumberOne)) {
                    if (StringUtils.ipCheck(ipNumberOne)) {
                        ConstData.urlManager.serverIp = ipNumberOne;
                        ConstData.urlManager.serverPort = portNumberOne;

                        isOneOK = true;
                    } else {
                        ToastUtils.toastShortMessage("管理后台配置IP地址不合法");
                        return;
                    }
                } else if (TextUtils.isEmpty(ipNumberOne)) {
                    ToastUtils.toastShortMessage("管理后台配置IP地址不能为空");
                    return;
                } else if (TextUtils.isEmpty(portNumberOne)) {
                    ToastUtils.toastShortMessage("管理后台配置端口号不能为空");
                    return;
                }

                if (!TextUtils.isEmpty(ipNumberTwo) && !TextUtils.isEmpty(portNumberTwo)) {
                    if (StringUtils.ipCheck(ipNumberTwo)) {
                        ConstData.urlManager.activemqIp = ipNumberTwo;
                        ConstData.urlManager.activemqPort = portNumberTwo;
                        isTwoOK = true;
                    } else {
                        ToastUtils.toastShortMessage("ActiveMQ配置IP地址不合法");
                        return;
                    }
                } else if (TextUtils.isEmpty(ipNumberTwo)) {
                    ToastUtils.toastShortMessage("ActiveMQ配置IP地址不能为空");
                    return;
                } else if (TextUtils.isEmpty(portNumberTwo)) {
                    ToastUtils.toastShortMessage("ActiveMQ配置端口号不能为空");
                    return;
                }

                if (!TextUtils.isEmpty(ipNumberThree) && !TextUtils.isEmpty(portNumberThree)) {
                    if (StringUtils.ipCheck(ipNumberThree)) {
                        ConstData.urlManager.appWebIp = ipNumberThree;
                        ConstData.urlManager.appWebPort = portNumberThree;
                        isThreeOK = true;
                    } else {
                        ToastUtils.toastShortMessage("APP页面后台配置IP地址不合法");
                        return;
                    }
                } else if (TextUtils.isEmpty(ipNumberThree)) {
                    ToastUtils.toastShortMessage("APP页面后台配置IP地址不能为空");
                    return;
                } else if (TextUtils.isEmpty(portNumberThree)) {
                    ToastUtils.toastShortMessage("APP页面后台配置端口号不能为空");
                    return;
                }

                if (isOneOK && isTwoOK && isThreeOK) {
                    SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "server_ip", ipNumberOne);
                    SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "server_port", portNumberOne);
                    SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "activemq_ip", ipNumberTwo);
                    SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "activemq_port", portNumberTwo);
                    SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "appweb_ip", ipNumberThree);
                    SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "appweb_port", portNumberThree);
                    ConstData.urlManager.setBaseUrl(ConstData.urlManager.serverIp, ConstData.urlManager.serverPort, ConstData.urlManager.activemqIp,
                            ConstData.urlManager.activemqPort, ConstData.urlManager.appWebIp, ConstData.urlManager.appWebPort);
                    checkRecodResult();
                }
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            } else {
            }
        }
    };


    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int MOBILE_INFO = 0x0011;

    private void checkRecodResult() {
        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.httpCheckMobileInfo, RequestMethod.POST);
        // 添加请求参数
        request.add("imei", ConstData.deviceId);
        request.add("imsi", ConstData.deviceNumber);

        Log.e("result", "checkRecodResult: " + ConstData.deviceId);
        Log.e("result", "checkRecodResult: " + ConstData.deviceNumber);
        NoHttpManager.getRequestInstance().add(InputLinkAddressMoreActivity.this, MOBILE_INFO, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, Response<String> response) {
                if (what == MOBILE_INFO) {
                    // 响应结果
                    String result = response.get();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            ToastUtils.toastShortMessage(responseBean.getMsg());
                            //将终端认证信息设定为true
                            ConstData.isTerminalChecked = true;
                            SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "terminalrzzt", ConstData.isTerminalChecked);
                            Intent intent = new Intent(InputLinkAddressMoreActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (responseBean.getCode() == HttpCode.TERMINAL_HAVE_BOUND) {
                                //将终端认证信息设定为true
                                ConstData.isTerminalChecked = true;
                                SharePreferenceUtils.putDataSharedPreferences(InputLinkAddressMoreActivity.this, "terminalrzzt", ConstData.isTerminalChecked);
                                Intent intent = new Intent(InputLinkAddressMoreActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            ToastUtils.toastShortMessage(responseBean.getMsg());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
            }
        }, false, true);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(InputLinkAddressMoreActivity.this, FirstLoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
