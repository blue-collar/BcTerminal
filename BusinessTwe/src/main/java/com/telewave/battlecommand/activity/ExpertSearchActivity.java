package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.ExpertInfo;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.widget.BaseActivity;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.util.List;


/**
 * 搜索专家
 *
 * @author zhangjun
 * @date 2019-07-21
 */
public class ExpertSearchActivity extends BaseActivity {

    private EditText et_search;
    private LinearLayout ll_expertItemRoot;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_expert_search);
        initView();
        initEvent();
    }


    private void initView() {
        et_search = findViewById(R.id.et_search);
        ll_expertItemRoot = findViewById(R.id.ll_expertItemRoot);
    }

    private void initEvent() {
        findViewById(R.id.ll_searchContactReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                finish();
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(et_search.getText().toString().trim())) {
                        searchContact();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 处理搜索逻辑
     */
    private void searchContact() {
        closeKeyboard();
        ll_expertItemRoot.removeAllViews();
        String searchContent = et_search.getText().toString().trim();
        if (!TextUtils.isEmpty(searchContent)) {
            TUIKitLog.e("--===这里搜索联系人列表--=account=" + searchContent + "==organId==" + ConstData.ORGANID + "===url===" + ConstData.urlManager.addressAddressList);
            // 创建请求对象
            Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFireExpertList, RequestMethod.GET);
            // 添加请求参数
            request.add("name", searchContent);

            NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    // 响应结果
                    String result = response.get();
                    List<ExpertInfo> list = null;
                    if (result != null) {
                        Gson gson = new Gson();
                        try {
                            ResponseBean responseBean = new ResponseBean(result);
                            if (responseBean.isSuccess()) {
                                if (!TextUtils.isEmpty(responseBean.getData())) {
                                    list = gson.fromJson(responseBean.getData(), new TypeToken<List<ExpertInfo>>() {
                                    }.getType());

                                } else if (!TextUtils.isEmpty(responseBean.getData1())) {
                                    list = gson.fromJson(responseBean.getData(), new TypeToken<List<ExpertInfo>>() {
                                    }.getType());

                                }
                                if (list != null) {
                                    initSearchResutView(list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                    TUIKitLog.e("--===onFailed--==" + tag);

                }
            }, false, false);
        }
    }


    private void initSearchResutView(List<ExpertInfo> infoList) {
        for (ExpertInfo info : infoList) {
            View inflate = View.inflate(this, R.layout.view_expert_item, null);
            TextView tv_userName = inflate.findViewById(R.id.tv_userName);
            TextView tv_userDes = inflate.findViewById(R.id.tv_userDes);
            tv_userName.setText(info.getXm());
            tv_userDes.setText(info.getShzjly().getZjlydm().getCodeName());

            ll_expertItemRoot.addView(inflate);
        }
    }


}
