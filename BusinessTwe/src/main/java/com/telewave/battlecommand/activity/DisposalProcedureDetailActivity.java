package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.DisposalDetail;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.view.AutoSplitTextView;
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

public class DisposalProcedureDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvOverView, mTvPhysicalFeature, mTvDisposal, mTvAction, mTvCollapse;
    private List<TextView> mLabels = new ArrayList<>();
    private LinearLayout mLayoutOverView, mLayoutFeature, mLayoutDisposal, mLayoutAction, mLayoutCollapse;
    private List<View> detailViews = new ArrayList<>();

    private AutoSplitTextView mTvOverViewDetail, mTvFeatureDetail, mTvActionDetail, mTvCollapseDetail;
    private LinearLayout mLayoutDisposalDetails;

    private DisposalDetail detailInfo = new DisposalDetail();
    private String disposalId;

    private static final int GET_DISPOSAL_PROCEDURE_DETAIL_SUCCESS = 0x1310;
    private static final int GET_DISPOSAL_PROCEDURE_DETAIL_FAIL = 0x1320;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_DISPOSAL_PROCEDURE_DETAIL_SUCCESS:
                    if (msg.obj != null) {

                        detailInfo = (DisposalDetail) msg.obj;
                        if (detailInfo != null) {
                            setData(detailInfo);
                        }
                    }

                    break;
                case GET_DISPOSAL_PROCEDURE_DETAIL_FAIL:
                    ToastUtils.toastShortMessage("获取处置规程信息失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_disposal_procedure_detail);

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

        mTvOverView = findViewById(R.id.tv_disposal_overview_info);
        mTvPhysicalFeature = findViewById(R.id.tv_disposal_physical_feature);
        mTvDisposal = findViewById(R.id.tv_disposal_procedure);
        mTvAction = findViewById(R.id.tv_disposal_action);
        mTvCollapse = findViewById(R.id.tv_disposal_collapse);
        mLabels.add(mTvOverView);
        mLabels.add(mTvPhysicalFeature);
        mLabels.add(mTvDisposal);
        mLabels.add(mTvAction);
        mLabels.add(mTvCollapse);
        mTvOverView.setOnClickListener(this);
        mTvPhysicalFeature.setOnClickListener(this);
        mTvDisposal.setOnClickListener(this);
        mTvAction.setOnClickListener(this);
        mTvCollapse.setOnClickListener(this);

        mLayoutOverView = findViewById(R.id.ll_disposal_overview_info);
        mLayoutFeature = findViewById(R.id.ll_disposal_feature_info);
        mLayoutDisposal = findViewById(R.id.ll_disposal_procedure_info);
        mLayoutAction = findViewById(R.id.ll_disposal_action_info);
        mLayoutCollapse = findViewById(R.id.ll_disposal_collapse_info);
        detailViews.add(mLayoutOverView);
        detailViews.add(mLayoutFeature);
        detailViews.add(mLayoutDisposal);
        detailViews.add(mLayoutAction);
        detailViews.add(mLayoutCollapse);

        mTvOverViewDetail = findViewById(R.id.tv_disposal_detail_overview);
        mTvFeatureDetail = findViewById(R.id.tv_disposal_detail_feature);
        mTvActionDetail = findViewById(R.id.tv_disposal_detail_action);
        mTvCollapseDetail = findViewById(R.id.tv_disposal_detail_collapse);
        mLayoutDisposalDetails = findViewById(R.id.ll_disposal_detail_info);

    }

    private void initData() {
        disposalId = getIntent().getStringExtra("DisposalId");

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getDisposalProcedureDetail + disposalId, RequestMethod.GET);

//        String url = request.url();
//        Log.e("NoHttpDebugTag", "开始处置规程详情----------" + url);
        NoHttpManager.getRequestInstance().add(DisposalProcedureDetailActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                DisposalDetail tempInfo = null;
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            tempInfo = gson.fromJson(responseBean.getData(), DisposalDetail.class);

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_DISPOSAL_PROCEDURE_DETAIL_SUCCESS;
                            msg.obj = tempInfo;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_DISPOSAL_PROCEDURE_DETAIL_FAIL;
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
                msg.what = GET_DISPOSAL_PROCEDURE_DETAIL_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, false);

    }

    private void setData(DisposalDetail info) {
        mTvOverViewDetail.setText(info.getGs() != null ? "\u3000\u3000" + info.getGs() : "");
        mTvFeatureDetail.setText(info.getLhxz() != null ? "\u3000\u3000" + info.getLhxz() : "");
        mTvActionDetail.setText(info.getXdyq() != null ? "\u3000\u3000" + info.getXdyq() : "");
        mTvCollapseDetail.setText(info.getTtyy() != null ? "\u3000\u3000" + info.getTtyy() : "");

        initDisposalProcedureDetail();
    }

    private void initDisposalProcedureDetail() {
        if (mLayoutDisposalDetails != null) {
            mLayoutDisposalDetails.removeAllViews();
            for (int i = 0; i < detailInfo.getCzcxList().size(); i++) {
                DisposalDetail.CzcxListBean tempBean = detailInfo.getCzcxList().get(i);

                View disposalInflate = View.inflate(DisposalProcedureDetailActivity.this, R.layout.view_disposal_procedure_item, null);
                TextView mTvDisposalName = disposalInflate.findViewById(R.id.tv_disposal_name);
                ImageView iv_czOperateState = disposalInflate.findViewById(R.id.iv_disposal_OperateState);
                LinearLayout mLayoutDisposal = disposalInflate.findViewById(R.id.ll_czViewRoot);
                mTvDisposalName.setText(tempBean.getBt());
                initDisposalProcedureItemEvent(disposalInflate, iv_czOperateState, mLayoutDisposal, tempBean.getNr());
                mLayoutDisposalDetails.addView(disposalInflate);
            }
        }
    }

    private void initDisposalProcedureItemEvent(View viewInflate, final ImageView iv_czOperateState, final LinearLayout mLayoutDisposal, final String content) {
        viewInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_czOperateState.setSelected(!iv_czOperateState.isSelected());
                mLayoutDisposal.setVisibility(iv_czOperateState.isSelected() ? View.VISIBLE : View.GONE);
                initSubView(mLayoutDisposal, content);
            }
        });
    }

    private void initSubView(LinearLayout mLayoutDisposal, String content) {
        mLayoutDisposal.removeAllViews();
        View subDisposalInflate = View.inflate(DisposalProcedureDetailActivity.this, R.layout.view_sub_disposal_procedure_detail_item, null);
        AutoSplitTextView mTvDisposalName = subDisposalInflate.findViewById(R.id.tv_disposal_name);
        mTvDisposalName.setText(content);
        mLayoutDisposal.addView(subDisposalInflate);
    }

    /**
     * 设置标签背景颜色
     *
     * @param textView
     */
    private void setLabelsBackgroundColor(TextView textView) {
        for (TextView view : mLabels) {
            if (view == textView) {
                view.setTextColor(getResources().getColor(R.color.red));
                view.setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                view.setTextColor(getResources().getColor(R.color.black));
                view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            }
        }
    }

    private void setDetailViewVisible(View tempView) {
        for (View view : detailViews) {
            if (view == tempView) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.tv_disposal_overview_info == vId) {        //概述
            setLabelsBackgroundColor(mTvOverView);
            setDetailViewVisible(mLayoutOverView);
        } else if (R.id.tv_disposal_physical_feature == vId) {  //物理特性
            setLabelsBackgroundColor(mTvPhysicalFeature);
            setDetailViewVisible(mLayoutFeature);
        } else if (R.id.tv_disposal_procedure == vId) {     //处置措施
            setLabelsBackgroundColor(mTvDisposal);
            setDetailViewVisible(mLayoutDisposal);
        } else if (R.id.tv_disposal_action == vId) {        //行动要求
            setLabelsBackgroundColor(mTvAction);
            setDetailViewVisible(mLayoutAction);
        } else if (R.id.tv_disposal_collapse == vId) {      //坍塌原因
            setLabelsBackgroundColor(mTvCollapse);
            setDetailViewVisible(mLayoutCollapse);
        } else {
        }
    }
}
