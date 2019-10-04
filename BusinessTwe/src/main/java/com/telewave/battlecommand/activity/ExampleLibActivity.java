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
import com.telewave.battlecommand.adapter.ExampleLibAdapter;
import com.telewave.battlecommand.bean.ExampleBean;
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
 * 案例库
 *
 * @author liwh
 * @date 2019/1/28
 */
public class ExampleLibActivity extends BaseActivity {
    private static final String TAG = "ExampleLibActivity";

    private ClearEditText mEtName;
    private Button mBtnSearch;

    private ListView exampleLibListView;
    private View emptyView;
    private List<ExampleBean> exampleBeanList = new ArrayList<ExampleBean>();
    private ExampleLibAdapter exampleLibAdapter;
    private String name;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;
    private static final int GET_SEARCH_SUCCESS = 0X1070;
    private static final int GET_SEARCH_FAIL = 0X1072;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    List<ExampleBean> exampleBeanListTemp = (List<ExampleBean>) msg.obj;
                    if (exampleBeanListTemp == null || exampleBeanListTemp.isEmpty()) {
                        exampleLibListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        exampleBeanList.clear();
                        exampleBeanList.addAll((List<ExampleBean>) msg.obj);
                        exampleLibAdapter.notifyDataSetChanged();
                    }
                    break;
                case GET_FAIL:
                    ToastUtils.toastShortMessage("获取失败");

                    break;
                case GET_SEARCH_SUCCESS:
                    exampleBeanList.clear();
                    List<ExampleBean> tempInfos = (List<ExampleBean>) msg.obj;
                    if (tempInfos == null || tempInfos.isEmpty()) {
                        exampleLibListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        exampleBeanList.addAll(tempInfos);
                        exampleLibAdapter.notifyDataSetChanged();
                    }

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
        setContentView(R.layout.activity_example_lib);
        initView();
        getExampleList();
    }

    public void initView() {
        mEtName = (ClearEditText) findViewById(R.id.edit_example_name_search);
        mBtnSearch = (Button) findViewById(R.id.btn_example_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    getSearchExampleInfoFromName(name);
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
                        getSearchExampleInfoFromName(name);
                    } else {
                        ToastUtils.toastShortMessage("输入不能为空");
                    }
                }
                return false;
            }
        });

        exampleLibListView = (ListView) findViewById(R.id.example_lib_listview);
        emptyView = findViewById(R.id.listview_empty_view);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exampleLibAdapter = new ExampleLibAdapter(ExampleLibActivity.this, exampleBeanList);
        exampleLibListView.setAdapter(exampleLibAdapter);
    }

    /**
     * 通过名称搜索
     *
     * @param name
     */
    private void getSearchExampleInfoFromName(String name) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getSearchExampleList, RequestMethod.GET);
        request.add("title", name);

        NoHttpManager.getRequestInstance().add(ExampleLibActivity.this, 0, request, new HttpListener<String>() {
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
                            List<ExampleBean> exampleBeanList = gson.fromJson(responseBean.getData(), new TypeToken<List<ExampleBean>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SEARCH_SUCCESS;
                            msg.obj = exampleBeanList;
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

    //获取案列列表信息
    private void getExampleList() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getExampleList, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(ExampleLibActivity.this, 0, request, new HttpListener<String>() {
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
                            List<ExampleBean> exampleBeanList = gson.fromJson(responseBean.getData(), new TypeToken<List<ExampleBean>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SUCCESS;
                            msg.obj = exampleBeanList;
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