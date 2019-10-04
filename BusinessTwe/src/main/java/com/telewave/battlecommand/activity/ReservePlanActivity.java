package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.ReservePlanAdapter;
import com.telewave.battlecommand.bean.ReservePlan;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
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
 * 预案列表页面
 *
 * @author liwh
 * @date 2019/1/9
 */
public class ReservePlanActivity extends BaseActivity {

    private static final String TAG = "ReservePlanActivity";
    private ListView reservePlanListView;
    private View emptyView;
    private List<ReservePlan> reservePlanList = new ArrayList<ReservePlan>();
    private ReservePlanAdapter reservePlanAdapter;
    private String importUnitId;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    List<ReservePlan> reservePlanListTemp = (List<ReservePlan>) msg.obj;
                    if (reservePlanListTemp == null || reservePlanListTemp.isEmpty()) {
                        reservePlanListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        reservePlanList.clear();
                        reservePlanList.addAll(reservePlanListTemp);
                        reservePlanAdapter = new ReservePlanAdapter(ReservePlanActivity.this, reservePlanList);
                        reservePlanListView.setAdapter(reservePlanAdapter);
                    }
                    break;
                case GET_FAIL:
                    ToastUtils.toastShortMessage("获取失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reserve_plan);
        initView();
        importUnitId = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(importUnitId)) {
            getReservePlanList(importUnitId);
        }
    }

    public void initView() {
        reservePlanListView = (ListView) findViewById(R.id.reserve_plan_listview);
        emptyView = findViewById(R.id.listview_empty_view);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //获取重点单位预案列表信息
    private void getReservePlanList(String objid) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getReservePlanList, RequestMethod.GET);
        // 添加请求参数
        request.add("objid", objid);
        NoHttpManager.getRequestInstance().add(ReservePlanActivity.this, 0, request, new HttpListener<String>() {
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
                            List<ReservePlan> mReservePlanList = gson.fromJson(responseBean.getData(), new TypeToken<List<ReservePlan>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SUCCESS;
                            msg.obj = mReservePlanList;
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