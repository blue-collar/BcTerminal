package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.PolicyGuiFanAdapter;
import com.telewave.battlecommand.bean.PolicyGuiFan;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.KeyBoardHiddenUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 政策规范
 *
 * @author liwh
 * @date 2018/12/20
 */
public class PolicyGuiFanActivity extends BaseActivity {
    private static final String TAG = "PolicyGuiFanActivity";
    private ClearEditText mEtName;
    private Button mBtnSearch;
    private ListView policyGuiFanListView;
    private View emptyView;
    private List<PolicyGuiFan> policyGuiFanList = new ArrayList<PolicyGuiFan>();
    private PolicyGuiFanAdapter policyGuiFanAdapter;
    private String name;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;
    private static final int GET_SEARCH_SUCCESS = 0x1060;
    private static final int GET_SEARCH_FAIL = 0x1062;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    List<PolicyGuiFan> policyGuiFanListTemp = (List<PolicyGuiFan>) msg.obj;
                    if (policyGuiFanListTemp == null || policyGuiFanListTemp.isEmpty()) {
                        policyGuiFanListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        policyGuiFanList.clear();
                        policyGuiFanList.addAll((List<PolicyGuiFan>) msg.obj);
                        policyGuiFanAdapter.notifyDataSetChanged();
                    }
                    break;
                case GET_FAIL:
                    ToastUtils.toastShortMessage("获取失败");
                    break;
                case GET_SEARCH_SUCCESS:
                    policyGuiFanList.clear();
                    List<PolicyGuiFan> tempInfos = (List<PolicyGuiFan>) msg.obj;
                    if (tempInfos == null || tempInfos.isEmpty()) {
                        policyGuiFanListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        policyGuiFanList.addAll(tempInfos);
                    }
                    policyGuiFanAdapter.notifyDataSetChanged();

                    break;
                case GET_SEARCH_FAIL:
                    ToastUtils.toastShortMessage("查询失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_policy_guifan);
        initView();
        getPolicyGuiFanList();
    }

    public void initView() {
        mEtName = (ClearEditText) findViewById(R.id.edit_policy_name_search);
        mBtnSearch = (Button) findViewById(R.id.btn_policy_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    getSearchPolicyListFromName(name);
                } else {
                    ToastUtils.toastShortMessage("输入不能为空");
                }
            }
        });
        KeyBoardHiddenUtils.closeInput(mEtName);
        mEtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                KeyBoardHiddenUtils.closeInput(mEtName);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    name = mEtName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        getSearchPolicyListFromName(name);
                    } else {
                        ToastUtils.toastShortMessage("输入不能为空");
                    }
                }
                return false;
            }
        });

        policyGuiFanListView = (ListView) findViewById(R.id.policy_guifan_listview);
        emptyView = findViewById(R.id.listview_empty_view);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        policyGuiFanAdapter = new PolicyGuiFanAdapter(PolicyGuiFanActivity.this, policyGuiFanList);
        policyGuiFanListView.setAdapter(policyGuiFanAdapter);
    }

    /**
     * 搜索
     */
    private void getSearchPolicyListFromName(String name) {

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getSearchPolicyList, RequestMethod.GET);
        request.add("title", name);

        NoHttpManager.getRequestInstance().add(PolicyGuiFanActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            List<PolicyGuiFan> policyGuiFanList = gson.fromJson(responseBean.getData(), new TypeToken<List<PolicyGuiFan>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SEARCH_SUCCESS;
                            msg.obj = policyGuiFanList;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SEARCH_FAIL;
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
                msg.what = GET_SEARCH_FAIL;
                mHandler.sendMessage(msg);
            }
        }, true, true);
    }

    //获取政策规范列表信息
    private void getPolicyGuiFanList() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getPolicyguifanList, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(PolicyGuiFanActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            List<PolicyGuiFan> policyGuiFanList = gson.fromJson(responseBean.getData(), new TypeToken<List<PolicyGuiFan>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SUCCESS;
                            msg.obj = policyGuiFanList;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_FAIL;
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
                msg.what = GET_FAIL;
                mHandler.sendMessage(msg);
            }
        }, true, true);
    }
}