package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.adapter.YuAnPlanAdapter;
import com.telewave.battlecommand.bean.YuAnPlanBean;
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
import java.util.Locale;

public class YuAnPlanActivity extends BaseActivity {

    private static final String TAG = "YuAnPlanActivity";
    private ListView yuAnPlanListView;
    private View emptyView;
    private LinearLayout ll_zdListRoot;
    private YuAnPlanBean mData = new YuAnPlanBean();
    private List<YuAnPlanBean.DataBean> mUpperOrganList = new ArrayList<>();
    private List<YuAnPlanBean.Data1Bean> mOrganList = new ArrayList<>();
    private YuAnPlanAdapter yuAnPlanAdapter;

    private static final int GET_DATA_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_DATA_SUCCESS:
                    YuAnPlanBean tempData = (YuAnPlanBean) msg.obj;
                    if (tempData == null) {
                        yuAnPlanListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        mData = tempData;
                        initZDView(mData);
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
        setContentView(R.layout.activity_yu_an_plan);
        initView();

        getPlanList();
    }

    public void initView() {
        yuAnPlanListView = (ListView) findViewById(R.id.yu_an_listview);
        emptyView = findViewById(R.id.listview_empty_view);
        ll_zdListRoot = (LinearLayout) findViewById(R.id.ll_zdListRoot);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    //获取重点单位列表信息
    private void getPlanList() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getPlanOrganList, RequestMethod.GET);
        // 添加请求参数
//        request.add("wzid", wzid);
        NoHttpManager.getRequestInstance().add(YuAnPlanActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                YuAnPlanBean yuAnPlanBean = null;
                Message msg = mHandler.obtainMessage();
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            yuAnPlanBean = gson.fromJson(result, YuAnPlanBean.class);

                            msg.what = GET_DATA_SUCCESS;
                            msg.obj = yuAnPlanBean;
                            mHandler.sendMessage(msg);
                        } else {

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
     * 初始化中队控件
     */
    private void initZDView(YuAnPlanBean info) {

        ll_zdListRoot.removeAllViews();
        if (null != info && null != info.getData() && !info.getData().isEmpty()) {
            final List<YuAnPlanBean.DataBean> zdInfoList = info.getData();
            for (int i = 0; i < zdInfoList.size(); i++) {
                YuAnPlanBean.DataBean tempInfo = zdInfoList.get(i);
                View zdInflate = View.inflate(this, R.layout.yu_an_item, null);
                TextView tv_zdName = zdInflate.findViewById(R.id.tv_organ_Name);
                TextView tv_zdWzNumber = zdInflate.findViewById(R.id.tv_organ_Number);
                TextView tvOrganCheck = zdInflate.findViewById(R.id.tv_check_out);
                ImageView iv_zdOperateState = zdInflate.findViewById(R.id.iv_organ_OperateState);
                LinearLayout ll_wzViewRoot = zdInflate.findViewById(R.id.ll_wzViewRoot);
                tv_zdName.setText(tempInfo.getName());
                tv_zdWzNumber.setText(0 == tempInfo.getNum() ? " (0)" : (String.format(Locale.CHINA, " (%d)", tempInfo.getNum())));

                initZdWzEvent(zdInflate, tvOrganCheck, iv_zdOperateState, ll_wzViewRoot, info, i);
                ll_zdListRoot.addView(zdInflate);
            }
        }

    }


    /**
     * 初始化中队微站显示隐藏逻辑
     *
     * @param zdInflate
     * @param iv_zdOperateState
     * @param ll_wzViewRoot
     */
    private void initZdWzEvent(View zdInflate, final TextView tvOrganCheck, final ImageView iv_zdOperateState, final LinearLayout ll_wzViewRoot, final YuAnPlanBean info, final int position) {
        zdInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_zdOperateState.setSelected(!iv_zdOperateState.isSelected());
                ll_wzViewRoot.setVisibility(iv_zdOperateState.isSelected() ? View.VISIBLE : View.GONE);
                initWZView(ll_wzViewRoot, info.getData1());
            }
        });
        tvOrganCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toastShortMessage("查看" + position);
                Intent intent = new Intent(YuAnPlanActivity.this, YuAnUnitActivity.class);
                intent.putExtra("officeId", info.getData().get(0).getOfficeId());
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化微站数据列表
     *
     * @param ll_wzViewRoot
     * @param list
     */
    private void initWZView(LinearLayout ll_wzViewRoot, List<YuAnPlanBean.DataBean> list) {
        ll_wzViewRoot.removeAllViews();
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                YuAnPlanBean.DataBean tempInfo = list.get(i);

                View wzInflate = View.inflate(this, R.layout.yu_an_sub_item, null);
                TextView tv_wzName = wzInflate.findViewById(R.id.tv_organ_Name);
                TextView tv_wzUserNumber = wzInflate.findViewById(R.id.tv_organ_Number);
                TextView tv_OrganCheck = wzInflate.findViewById(R.id.tv_check_out);
                ImageView iv_wzOperateState = wzInflate.findViewById(R.id.iv_organ_OperateState);
                tv_wzName.setText(tempInfo.getName());
                tv_wzUserNumber.setText(0 == list.size() ? " (0)" : (String.format(Locale.CHINA, " (%d)", list.size())));
                initWZContactEvent(tv_OrganCheck, list, i);
                ll_wzViewRoot.addView(wzInflate);
            }
        }
    }

    /**
     * 初始化微站事件
     *
     * @param tvOrganCheck
     * @param list
     */
    private void initWZContactEvent(TextView tvOrganCheck, final List<YuAnPlanBean.DataBean> list, final int position) {
        tvOrganCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.toastShortMessage("查看" + position);
                Intent intent = new Intent(YuAnPlanActivity.this, YuAnUnitActivity.class);
                intent.putExtra("officeId", list.get(position).getOfficeId());
                startActivity(intent);

            }
        });
    }

}
