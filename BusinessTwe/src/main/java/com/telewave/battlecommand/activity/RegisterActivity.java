package com.telewave.battlecommand.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.MemberInfo;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.RegexUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;

/**
 * 注册页面
 *
 * @author liwh
 * @date 2019/1/7
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private Button backBtn;
    private TextView returnLoginTextView;
    private ClearEditText registerPhoneEditText;
    private ClearEditText registerNameEditText;
    private ClearEditText registerPasswordEditText;
    private ClearEditText registerLastPasswordEditText;
    private CheckBox checkbox;
    private Button registerBtn;
    private TextView choiceWeizhanName;

    private String login_name;
    private String login_password;
    private String name;
    private boolean isCheckFuwuXieyi = true;
    private String choiceWeiZhanId;

    private static final int REGISTER_SUCCESS = 0x1001;
    private static final int REGISTER_FAIL = 0x1002;
    private static final int REGISTER_REQUEST_FAIL = 0x1003;

    public static final int GET_REGISTER_WEIZHAN = 0x1004;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case REGISTER_SUCCESS:
                    ToastUtils.toastShortMessage("注册成功，可以登录了！");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                    break;
                case REGISTER_FAIL:
                    String failStr = (String) msg.obj;
                    ToastUtils.toastShortMessage(failStr);
                    break;
                case REGISTER_REQUEST_FAIL:
                    ToastUtils.toastShortMessage("注册失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        initView();
        setTextChangedListener();
        if (getIntent() != null) {
            String weizhanName = getIntent().getStringExtra("weiZhanName");
            choiceWeiZhanId = getIntent().getStringExtra("weiZhanId");
            choiceWeizhanName.setText(weizhanName);
        }
    }

    private void initView() {
        returnLoginTextView = (TextView) findViewById(R.id.return_login_textview);
        registerPhoneEditText = (ClearEditText) findViewById(R.id.register_phone_input);
        choiceWeizhanName = (TextView) findViewById(R.id.choice_weizhan_name);
        registerNameEditText = (ClearEditText) findViewById(R.id.register_name_input);
        registerPasswordEditText = (ClearEditText) findViewById(R.id.register_password_input);
        registerLastPasswordEditText = (ClearEditText) findViewById(R.id.register_password_last_input);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        registerBtn = (Button) findViewById(R.id.register_btn);
        returnLoginTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        returnLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckFuwuXieyi = isChecked;
            }
        });

        findViewById(R.id.choice_weizhan_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, RegisterWeiZhanChoiceActivity.class);
                startActivityForResult(intent, GET_REGISTER_WEIZHAN);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckFuwuXieyi) {
                    ToastUtils.toastShortMessage("您还没勾选同意微站联动终端服务协议");
                } else {
                    //判断手机号
                    if (TextUtils.isEmpty(registerPhoneEditText.getText().toString())) {
                        ToastUtils.toastShortMessage(getResources().getString(R.string.register_phone_null));
                        return;
                    } else {
                        if (!RegexUtils.isMobile(registerPhoneEditText.getText().toString())) {
                            ToastUtils.toastShortMessage(getResources().getString(R.string.register_phone_error));
                            return;
                        }
                    }

                    //判断名字
                    if (TextUtils.isEmpty(registerNameEditText.getText().toString())) {
                        ToastUtils.toastShortMessage(getResources().getString(R.string.register_name_null));
                        return;
                    }

                    //判断密码
                    if (TextUtils.isEmpty(registerPasswordEditText.getText().toString())) {
                        ToastUtils.toastShortMessage(getResources().getString(R.string.register_pwd_null));
                        return;
                    } else {
                        if (registerPasswordEditText.getText().length() < 6 || registerPasswordEditText.getText().length() > 15) {
                            ToastUtils.toastShortMessage(getResources().getString(R.string.register_pwd_length_error));
                            return;
                        }
                    }

                    //判断确认密码
                    if (TextUtils.isEmpty(registerLastPasswordEditText.getText())) {
                        ToastUtils.toastShortMessage(getResources().getString(R.string.register_last_pwd_null));
                        return;
                    } else {
                        if (registerLastPasswordEditText.getText().length() < 6 || registerLastPasswordEditText.getText().length() > 15) {
                            ToastUtils.toastShortMessage(getResources().getString(R.string.register_pwd_length_error));
                            return;
                        }
                    }

                    //判断 密码与确认密码
                    if (!TextUtils.equals(registerPasswordEditText.getText().toString(), registerLastPasswordEditText.getText().toString())) {
                        registerLastPasswordEditText.setTextColor(Color.RED);
                        ToastUtils.toastShortMessage(getResources().getString(R.string.register_pwd_error));
                        return;
                    } else {
                        registerLastPasswordEditText.setTextColor(Color.BLACK);
                    }
                    login_name = registerPhoneEditText.getText().toString().trim();
                    login_password = registerPasswordEditText.getText().toString().trim();
                    name = registerNameEditText.getText().toString().trim();

                    MemberInfo memberInfo = new MemberInfo();
                    memberInfo.setLoginName(login_name);
                    memberInfo.setPassword(login_password);
                    memberInfo.setMobilephone(login_name);
                    memberInfo.setName(name);
                    memberInfo.setFirestation(new MemberInfo.FirestationBean(choiceWeiZhanId));
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(memberInfo);
                    Log.e(TAG, "onClick: " + jsonStr);
                    Log.e(TAG, "choiceWeiZhanId: " + choiceWeiZhanId);
                    submitRegister(jsonStr);
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

    // 设置TextChangedListener事件
    private void setTextChangedListener() {
        registerPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPhoneState();
            }
        });
        registerNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNameState();
            }
        });
        registerPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordState();
            }
        });
        registerLastPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checklastPwdState();
            }
        });


    }

    // 验证手机号输入状态
    public void checkPhoneState() {
        if (TextUtils.isEmpty(registerPhoneEditText.getText())) {
            registerPhoneEditText.setHint(R.string.register_phone_null);
            registerPhoneEditText.setShakeAnimation();
        }
    }

    // 验证姓名输入状态
    public void checkNameState() {
        if (TextUtils.isEmpty(registerNameEditText.getText())) {
            registerNameEditText.setHint(R.string.register_name_null);
            registerNameEditText.setShakeAnimation();
        }
    }

    // 验证密码输入状态
    public void checkPasswordState() {
        if (TextUtils.isEmpty(registerPasswordEditText.getText())) {
            registerPasswordEditText.setHint(R.string.register_pwd_null);
            registerPasswordEditText.setShakeAnimation();
        }
    }

    // 验证再次输入的密码输入状态
    public void checklastPwdState() {
        if (TextUtils.isEmpty(registerLastPasswordEditText.getText())) {
            registerLastPasswordEditText.setHint(R.string.register_last_pwd_null);
            registerLastPasswordEditText.setShakeAnimation();
        }
    }


    //提交注册
    private void submitRegister(String jsonStr) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.registerAccountUrl, RequestMethod.POST);
        request.setDefineRequestBodyForJson(jsonStr);
        NoHttpManager.getRequestInstance().add(RegisterActivity.this, 0, request, new HttpListener<String>() {
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
                            msg.what = REGISTER_SUCCESS;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = REGISTER_FAIL;
                            msg.obj = responseBean.getMsg();
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
                msg.what = REGISTER_REQUEST_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_REGISTER_WEIZHAN) {
                Bundle bundle = data.getExtras();
                String weizhanName = bundle.getString("name");
                choiceWeiZhanId = bundle.getString("id");
                choiceWeizhanName.setText(weizhanName);
                ToastUtils.toastShortMessage("选择微站为：" + weizhanName);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //一定要有这个方法，否则getIntent收不到数据
        setIntent(intent);
        String weizhanName = getIntent().getStringExtra("weiZhanName");
        choiceWeiZhanId = getIntent().getStringExtra("weiZhanId");
        choiceWeizhanName.setText(weizhanName);
        ToastUtils.toastShortMessage("选择微站为：" + weizhanName);
    }
}
