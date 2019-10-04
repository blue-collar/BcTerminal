package com.telewave.battlecommand.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.activity.ImagePagerActivity;
import com.telewave.battlecommand.adapter.ReservePlanAdapter;
import com.telewave.battlecommand.bean.ReservePlan;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_INDEX;
import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_URLS;

/**
 * 预案附件
 *
 * @author zhangjun
 * @date 2019/8/13
 */

@SuppressLint("ValidFragment")
public class UnitFileFragment extends BaseFragment {

    private static final String TAG = "UnitFileFragment";

    private ListView reservePlanListView;
    private View emptyView;
    private List<ReservePlan> reservePlanList = new ArrayList<>();
    private ReservePlanAdapter reservePlanAdapter;

    private View mView;
    private String importUnitId;
    private Context context;
    private String tabTitle;

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
                        reservePlanAdapter = new ReservePlanAdapter(getActivity(), reservePlanList);
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
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

        initData();
    }


    public UnitFileFragment(Context context, String title, String id) {
        this.context = context;
        this.tabTitle = title;
        this.importUnitId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_reserve_plan, container, false);
        initView();

        return mView;
    }

    private void initView() {
        reservePlanListView = mView.findViewById(R.id.reserve_plan_listview);
        emptyView = mView.findViewById(R.id.listview_empty_view);

    }

    private void initData() {
        if (!TextUtils.isEmpty(importUnitId)) {
            getReservePlanList(importUnitId);
        }
    }

    //获取重点单位预案列表信息
    private void getReservePlanList(String objid) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getReservePlanList, RequestMethod.GET);
        // 添加请求参数
        request.add("objid", objid);
        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
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

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls
     */
    protected void imageBrower(int position, List<String> urls) {
        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, (Serializable) urls);
        intent.putExtra(EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }
}
