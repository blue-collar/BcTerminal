package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.telewave.battlecommand.bean.LoginInfo;
import com.telewave.battlecommand.http.HttpCode;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.HttpResponseUtil;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.service.IMOperate;
import com.telewave.battlecommand.utils.ShowWindowUtils;
import com.telewave.battlecommand.view.ClearAutoCompleteTextView;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.AppUtils;
import com.telewave.lib.base.util.ParseJsonUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.router.RouterPath;
import com.telewave.lib.widget.util.StatusBarUtil;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;

import static com.telewave.lib.base.util.SharePreferenceUtils.getBooleanDataSharedPreferences;
import static com.telewave.lib.base.util.SharePreferenceUtils.getDataSharedPreferences;


/**
 * @author liwh
 * @date 2018/12/18
 */
@Route(path = RouterPath.LOGIN_MAIN_PATH)
public class LoginActivity extends AppCompatActivity {

    //登录
    private Button login;
    private ClearAutoCompleteTextView loginNameEdit;
    private ClearEditText loginPasswordEdit;
    private CheckBox autoLoginCb;

    private TextView registerTextView;

    private String loginName, loginPassword;

    private String shareUsername;
    private String sharePassword;

    //用户认证状态
    private String rzzt = "";
    //用户名称
    private String username;
    //用户Id
    private String userid;
    //联系方式
    private String lxfs;
    //微站所属机构Id
    private String organId;
    //微站所属机构名称
    private String organName;
    //经纬度
    private String mapx, mapy;
    //微站地址
    private String address;
    private String userSig;

    private String isLeader;

    private static final int TOLOGIN = 1;
    private static final int LOGIN_TYPE = 2;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TOLOGIN:
                    if (!ProgressDialogUtils.isDialogShowing()) {
                        ProgressDialogUtils.showDialog(LoginActivity.this, "正在保存登录信息...");
                    }
                    saveDataShared();
                    intentHome();
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
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTransparent(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 非正常启动流程，直接重新初始化应用界面
        if (AppProxy.APP_STATUS != AppProxy.APP_STATUS_NORMAL) {
            AppProxy.getInstance().getAbnormalStartCallback().rbnormalStartReInitApp();
            return;
        } else {
            //正常启动流程 子Activity初始化界面
            initView();
        }
    }

    private void initView() {
        login = (Button) findViewById(R.id.login_submit);
        login.setOnClickListener(myClickListener);

        autoLoginCb = (CheckBox) findViewById(R.id.cb_auto_login);
        loginNameEdit = (ClearAutoCompleteTextView) findViewById(R.id.user_username);
        loginPasswordEdit = (ClearEditText) findViewById(R.id.user_password);
        registerTextView = (TextView) findViewById(R.id.tv_register);

        shareUsername = getDataSharedPreferences(LoginActivity.this, "loginname");
        sharePassword = getDataSharedPreferences(LoginActivity.this, "password");
        //将sharedprefence的用户名密码设置在输入框中
        if (!shareUsername.equals("")) {
            loginNameEdit.setText(shareUsername);
            loginPasswordEdit.setText(sharePassword);
        }
        autoLoginCb.setChecked(getBooleanDataSharedPreferences(LoginActivity.this, "auto_login"));
        setLoginEnable(true);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveDataShared() {
        ConstData.loginName = loginName;
        ConstData.loginPassword = loginPassword;
        ConstData.username = username;
        ConstData.phoneNumber = lxfs;
        ConstData.userid = userid;
        ConstData.ORGANID = organId;
        ConstData.mapx = mapx;
        ConstData.mapy = mapy;
        ConstData.isLeader = isLeader;
        ConstData.address = address;
        ConstData.userSig = userSig;
        ConstData.ORGAN_NAME = organName;
        /**
         *  则将用户信息加入SharedPreferences
         */
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "organName", ConstData.ORGAN_NAME);
        //登录认证标志rzzt：1，通过；0，失败；
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "rzzt", rzzt);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "auto_login", autoLoginCb.isChecked());
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "username", username);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "userid", userid);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "lxfs", lxfs);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "mapx", mapx);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "mapy", mapy);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "organid", organId);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "isLeader", isLeader);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "address", address);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "userSig", userSig);

        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "loginname", loginName);
        SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "password", loginPassword);

    }

    private View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int vId = v.getId();
            if (R.id.login_submit == vId) {
//                    setLoginEnable(false);
                toLogin();
            } else {
            }
        }
    };

    private void setLoginEnable(boolean isable) {
        login.setEnabled(isable);
        loginNameEdit.setEnabled(isable);
        loginPasswordEdit.setEnabled(isable);
    }

    private void toLogin() {
        loginName = loginNameEdit.getText().toString();
        loginPassword = loginPasswordEdit.getText().toString();
        if (loginName.equals("") && loginPassword.equals("")) {
            setLoginEnable(true);
            ToastUtils.toastShortMessage("请输入正确的用户名和密码");
            return;
        }
        if (loginName.equals("")) {
            setLoginEnable(true);
//            LoginCount = LoginCount + 1;
            ToastUtils.toastShortMessage("请输入正确的用户名");
            return;
        }
        if (loginPassword.equals("")) {
            setLoginEnable(true);
            ToastUtils.toastShortMessage("请输入正确的密码");
            return;
        }
        /***
         * 登录判断：
         *
         */
        checkLogin(loginName, loginPassword);
    }

    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int USER_INFO = 0x0011;

    private void checkLogin(String userName, String userPassword) {
        TUIKitLog.e("--==登录url=--==" + ConstData.urlManager.httpCheckLoginInfo);

        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.httpCheckLoginInfo, RequestMethod.POST);
        if (ConstData.deviceNumber == null) {
            ConstData.deviceNumber = "";
        }
        // 添加请求参数
        request.add("account", userName);
        request.add("password", userPassword);
        request.add("loginType", LOGIN_TYPE);
//        request.add("versioncode", "");
        //机器码
        request.add("imei", ConstData.deviceId);
        //国际移动用户识别码
        request.add("imsi", ConstData.deviceNumber);
        request.add("phone", ConstData.phoneNumber);
        NoHttpManager.getRequestInstance().add(LoginActivity.this, USER_INFO, request, onResponseListener, false, true);
    }

    private HttpListener onResponseListener = new HttpListener() {
        @Override
        public void onSucceed(int what, Response response) {
            if (what == USER_INFO) {
                // 响应结果
                String result = (String) response.get();
                Log.e("result", "onSucceed: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            LoginInfo loginInfo = (LoginInfo) ParseJsonUtils.getObjectFromArrayJson(result, LoginInfo.class, "data");
                            clearLoginInfo();
                            Log.e("onSucceed", "onSucceed: " + loginInfo.toString());
                            rzzt = loginInfo.getRzzt();
                            if (rzzt.equals("1")) {
                                ToastUtils.toastShortMessage(responseBean.getMsg());
                                username = loginInfo.getUsername();
                                userid = loginInfo.getUserid();
                                lxfs = loginInfo.getLxfs();
                                mapx = String.valueOf(loginInfo.getMapx());
                                mapy = String.valueOf(loginInfo.getMapy());
                                organId = loginInfo.getOrganid();
                                isLeader = loginInfo.getIsLeader();
                                address = loginInfo.getAddress();
                                userSig = loginInfo.getUserSig();
                                organName = loginInfo.getOrganName();
                                Message msg = mHandler.obtainMessage();
                                msg.what = TOLOGIN;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            //终端被后台解绑
                            if (responseBean.getCode() == HttpCode.TERMINAL_NOT_MATCH_REGISTER) {
                                Intent intent = new Intent(LoginActivity.this, FirstLoginActivity.class);
                                startActivity(intent);
                            } else if (responseBean.getCode() == HttpCode.USER_NOT_EXIST) {
                                /**
                                 * 将保存的用户名、密码也清除
                                 */
                                SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "loginname", "");
                                SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "password", "");

                                /**
                                 * 将保存的用户名也清楚
                                 */
                                SharePreferenceUtils.putDataSharedPreferences(LoginActivity.this, "loginuser", "");
                                loginNameEdit.setText("");
                                loginPasswordEdit.setText("");
                                loginName = null;
                                loginPassword = null;
                            }
                            ToastUtils.toastShortMessage(responseBean.getMsg());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
            HttpResponseUtil.showResponse(LoginActivity.this, exception, "请重新登录。");
            setLoginEnable(true);
            Log.e("result", "onFailed: " + url);
            Log.e("result", "onFailed: " + exception);
        }
    };

    private void clearLoginInfo() {
        rzzt = "";
        organId = "";
        username = "";
        userid = "";
        lxfs = "";
        mapx = "";
        mapy = "";
        organId = "";
        address = "";
        userSig = "";
    }


    private void intentHome() {
        IMOperate.getInstance().login(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                IMOperate.getInstance().updateUserProfile();
                /*从登录进首页必须先登录IM*/
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(IMKeys.INTENT_TAG, true);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errCode == 6208 || errCode == 70001) {
                    IMOperate.getInstance().login(LoginActivity.this);
                }
            }
        });


    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.toastShortMessage("再按一次程序将退到后台运行");
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProgressDialogUtils.dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground(LoginActivity.this)) {
            ShowWindowUtils.dismisWindow();
        }
    }
}
