package com.telewave.battlecommand.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.DisposalProcedureInfo;
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
 * 处置规程
 *
 * @author liwh
 * @date 2018/12/20
 */
public class DisposalProceduresActivity extends BaseActivity {
    private static final String TAG = "DisposalProceActivity";
    //    private X5WebView mWebView;
    private LinearLayout mLayoutDisposalContent;

    private String dictCode = "CZGC";
    private String pId = "1";
    private String isChildAll = "true";
    private String isIncludeSelf = "false";

    private List<DisposalProcedureInfo> mDisposalList = new ArrayList<>();

    private static final int GET_DISPOSAL_INFO_SUCCESS = 0X1210;
    private static final int GET_DISPOSAL_INFO_FAIL = 0X1220;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_DISPOSAL_INFO_SUCCESS:
                    List<DisposalProcedureInfo> tempList = (List<DisposalProcedureInfo>) msg.obj;
                    if (tempList == null || tempList.isEmpty()) {

                        ToastUtils.toastShortMessage("暂无数据");
                    } else {

                        mDisposalList.addAll((List<DisposalProcedureInfo>) msg.obj);
                        //获得根节点下的数据
                        List<DisposalProcedureInfo> rootList = onGetList();
                        initDisposalData(rootList);

                    }
                    break;
                case GET_DISPOSAL_INFO_FAIL:
                    ToastUtils.toastShortMessage("获取处置规程信息失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_disposal_procedures);

        initView();
        initData();

    }

    private void initView() {
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLayoutDisposalContent = findViewById(R.id.ll_disposal_procedure);


    }

    private void initData() {

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getDisposalProcedureList, RequestMethod.GET);
        request.add("dictCode", dictCode);
        request.add("pId", pId);
        request.add("isChildAll", isChildAll);
        request.add("isIncludeSelf", isIncludeSelf);

        NoHttpManager.getRequestInstance().add(DisposalProceduresActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                List<DisposalProcedureInfo> list = null;
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<DisposalProcedureInfo>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_DISPOSAL_INFO_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_DISPOSAL_INFO_FAIL;
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
                msg.what = GET_DISPOSAL_INFO_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, false);
    }


    /**
     * 获得根节点下的数据
     *
     * @return
     */
    private List<DisposalProcedureInfo> onGetList() {
        List<DisposalProcedureInfo> tempList = new ArrayList<>();
        for (DisposalProcedureInfo info : mDisposalList) {
            if (info.getPId().equals("1")) {
                tempList.add(info);
            }
        }

        return tempList;
    }

    /**
     * 获得子节点下的数据
     *
     * @param tempInfo
     * @return
     */
    private List<DisposalProcedureInfo> onGetSubList(DisposalProcedureInfo tempInfo) {
        List<DisposalProcedureInfo> tempList = new ArrayList<>();
        for (DisposalProcedureInfo info : mDisposalList) {
            if (tempInfo.getId().equals(info.getPId())) {
                tempList.add(info);
            }
        }

        return tempList;
    }

    private void initDisposalData(List<DisposalProcedureInfo> infoList) {
        if (mLayoutDisposalContent != null) {
            mLayoutDisposalContent.removeAllViews();
            for (int i = 0; i < infoList.size(); i++) {
                DisposalProcedureInfo tempInfo = infoList.get(i);
                List<DisposalProcedureInfo> list = onGetSubList(tempInfo);

                View disposalInflate = View.inflate(DisposalProceduresActivity.this, R.layout.view_disposal_procedure_item, null);
                TextView mTvDisposalName = disposalInflate.findViewById(R.id.tv_disposal_name);
                ImageView iv_czOperateState = disposalInflate.findViewById(R.id.iv_disposal_OperateState);
                LinearLayout mLayoutDisposal = disposalInflate.findViewById(R.id.ll_czViewRoot);
                mTvDisposalName.setText(tempInfo.getCodeName());
                initDisposalEvent(disposalInflate, iv_czOperateState, mLayoutDisposal, list);
                mLayoutDisposalContent.addView(disposalInflate);
            }
        }
    }

    private void initDisposalEvent(View viewInflate, final ImageView iv_czOperateState, final LinearLayout mLayoutDisposal, final List<DisposalProcedureInfo> infoList) {
        viewInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_czOperateState.setSelected(!iv_czOperateState.isSelected());
                mLayoutDisposal.setVisibility(iv_czOperateState.isSelected() ? View.VISIBLE : View.GONE);
                initSubView(mLayoutDisposal, infoList);
            }
        });
    }

    private void initSubView(LinearLayout mLayoutDisposal, final List<DisposalProcedureInfo> infoList) {
        mLayoutDisposal.removeAllViews();
        for (int i = 0; i < infoList.size(); i++) {
            DisposalProcedureInfo tempInfo = infoList.get(i);

            View subDisposalInflate = View.inflate(DisposalProceduresActivity.this, R.layout.view_sub_disposal_procedure_item, null);
            TextView mTvDisposalName = subDisposalInflate.findViewById(R.id.tv_disposal_name);
            mTvDisposalName.setText(tempInfo.getCodeName());
            initSubViewEvent(subDisposalInflate, infoList, i);
            mLayoutDisposal.addView(subDisposalInflate);
        }
    }

    private void initSubViewEvent(View subDisposalInflate, final List<DisposalProcedureInfo> infoList, final int position) {
        subDisposalInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toastShortMessage("点击：" + infoList.get(position).getCodeName());
                Intent intent = new Intent(DisposalProceduresActivity.this, DisposalProcedureDetailActivity.class);
                intent.putExtra("DisposalId", infoList.get(position).getCodeValue());
                startActivity(intent);
            }
        });
    }
}