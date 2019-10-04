package com.telewave.battlecommand.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.FireSummaryInfo;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.presenter.FireSummaryInfoPresenter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;

/**
 * <p>类说明: 实现火场总结的数据获取</p>
 *
 * @author 张军
 * @version 1.0
 * @since 2019-07-26
 */

public class FireSummaryFragment extends BaseFragment implements IDirectionContract.FireSummaryView {

    private View mView;
    private TextView mTvTrappedPeopl, mTvEvacuatedPeople, mTvWaterCost, mTvPropertyLoss, mTvBreathCost;
    private TextView mTvFirePlace, mTvAcceptedOrderTime, mTvArrivedPresentTime, mTvPutWaterTime, mTvPutFireTime;
    private TextView mTvDeathPeople, mTvRescuedPeople, mTvProtectedProperty, mTvFireDistrict;

    private Context context;
    private String tabTitle;
    private String disasterId;
    private FireSummaryInfoPresenter mSummaryInfoPresenter;
    private boolean isShowProgress = true;

    private static final int GET_FIRE_SUMMARY_SUCCESS = 1023;
    private static final int GET_FIRE_SUMMARY_FAIL = 1025;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_FIRE_SUMMARY_SUCCESS:
                    FireSummaryInfo tempInfo = (FireSummaryInfo) msg.obj;
                    if (tempInfo != null) {
                        setData(tempInfo);
                    }
                    break;
                case GET_FIRE_SUMMARY_FAIL:
                    ToastUtils.toastShortMessage("获取火场总结失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    public FireSummaryFragment() {

    }

    @SuppressLint("ValidFragment")
    public FireSummaryFragment(Context context, String tabTitle, String id) {
        this.context = context;
        this.tabTitle = tabTitle;
        this.disasterId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_fire_summary_detail, container, false);

        initView();

        return mView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
//            if (!ProgressDialogUtils.isDialogShowing()) {
//                ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
//            }
        } else {
//            if (ProgressDialogUtils.isDialogShowing()) {
//                ProgressDialogUtils.dismissDialog();
//            }
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        mSummaryInfoPresenter = new FireSummaryInfoPresenter(this);
        initData();
    }

    private void initView() {
        mTvTrappedPeopl = mView.findViewById(R.id.tv_summary_trapped_people);
        mTvEvacuatedPeople = mView.findViewById(R.id.tv_summary_evacuated_people);
        mTvWaterCost = mView.findViewById(R.id.tv_summary_water_cost);
        mTvPropertyLoss = mView.findViewById(R.id.tv_summary_property_loss);
        mTvBreathCost = mView.findViewById(R.id.tv_summary_breath_number);
        mTvFirePlace = mView.findViewById(R.id.tv_summary_fire_place);
        mTvAcceptedOrderTime = mView.findViewById(R.id.tv_summary_accepted_order_time);
        mTvArrivedPresentTime = mView.findViewById(R.id.tv_summary_arrived_present_time);
        mTvPutWaterTime = mView.findViewById(R.id.tv_summary_put_water_time);
        mTvPutFireTime = mView.findViewById(R.id.tv_summary_put_fire_time);
        mTvDeathPeople = mView.findViewById(R.id.tv_summary_death_people);
        mTvRescuedPeople = mView.findViewById(R.id.tv_summary_rescued_people);
        mTvProtectedProperty = mView.findViewById(R.id.tv_summary_protected_property_sum);
        mTvFireDistrict = mView.findViewById(R.id.tv_summary_fire_district);
    }

    private void initData() {
        if (disasterId != null && !disasterId.equals("")) {
//            onGetFireSummaryInfo(disasterId);
            mSummaryInfoPresenter.getDisasterSummaryInfo(disasterId, isShowProgress);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mSummaryInfoPresenter = new FireSummaryInfoPresenter(this);
//        initData();
    }

    private void onGetFireSummaryInfo(String id) {

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFireSummaryDetail, RequestMethod.GET);

        request.add("alarmId", id);
        String url = request.url();

        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                TUIKitLog.e("NNNoHttpDebugTag", "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            FireSummaryInfo tempInfo = gson.fromJson(responseBean.getData(), FireSummaryInfo.class);

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_FIRE_SUMMARY_SUCCESS;
                            msg.obj = tempInfo;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_FIRE_SUMMARY_FAIL;
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
                msg.what = GET_FIRE_SUMMARY_FAIL;
                mHandler.sendMessage(msg);

            }
        }, true, true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void setData(FireSummaryInfo info) {

        mTvTrappedPeopl.setText(info.getQzssrs());
        mTvEvacuatedPeople.setText(info.getSsrs());
        mTvWaterCost.setText(info.getSxh());
        mTvPropertyLoss.setText(info.getZjccss());
        mTvBreathCost.setText(info.getXhkhqsl());
        mTvFirePlace.setText(info.getRscs());
        mTvAcceptedOrderTime.setText(info.getJsmlsj());
        mTvArrivedPresentTime.setText(info.getDdxcsj());
        mTvPutWaterTime.setText(info.getDccssj());
        mTvPutFireTime.setText(info.getPhsj());
        mTvDeathPeople.setText(info.getQzswrs());
        mTvRescuedPeople.setText(info.getJjrs());
        mTvProtectedProperty.setText(info.getBhcc());
        mTvFireDistrict.setText(info.getHzdyfl());
    }

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
        }
    }

    /**
     * 隐藏掉等待对话框
     */
    @Override
    public void dismissWaitDialog() {
        if (ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.dismissDialog();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissWaitDialog();
        ToastUtils.toastShortMessage(msg);
    }

    @Override
    public void onFireSummaryCompleted(FireSummaryInfo info) {
        if (info != null) {
            setData(info);
        }
    }
}
