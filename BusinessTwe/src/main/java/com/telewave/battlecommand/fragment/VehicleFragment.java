package com.telewave.battlecommand.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.VehicleAdapter;
import com.telewave.battlecommand.bean.VehicleDispatch;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.presenter.VehicleInfoPresenter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>类说明: 实现作战车辆的获取</p>
 *
 * @author 张军
 * @version 1.0
 * @since 2019-07-26
 */

public class VehicleFragment extends BaseFragment implements IDirectionContract.VehicleView {

    private View mView;
    private TextView mTvSum;
    private ListView mLviewVehicles;
    private VehicleAdapter vehicleAdapter;

    private Context context;
    private String tabTitle;
    private String disasterId;
    private List<VehicleDispatch> vehicleDispatches = new ArrayList<>();
    private VehicleInfoPresenter mVehicleInfoPresenter;
    private boolean isShowProgress = true;

    private final static int GET_VEHICLE_SUCCESS = 1031;
    private final static int GET_VEHICLE_FAIL = 1033;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_VEHICLE_SUCCESS:
                    List<VehicleDispatch> tempInfo = (List<VehicleDispatch>) msg.obj;
                    if (tempInfo != null) {
                        vehicleDispatches.clear();
                        vehicleDispatches.addAll(tempInfo);

                        setData(tempInfo);
                    }
                    break;
                case GET_VEHICLE_FAIL:
                    ToastUtils.toastShortMessage("获取警情统计失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    public VehicleFragment() {

    }

    @SuppressLint("ValidFragment")
    public VehicleFragment(Context context, String tabTitle, String id) {
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

        mView = inflater.inflate(R.layout.fragment_vehicle_detail, container, false);

        initView();

        return mView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {

        } else {

        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        mVehicleInfoPresenter = new VehicleInfoPresenter(this);
        initData();
    }

    private void initView() {
        mTvSum = mView.findViewById(R.id.tv_vehicle_num);
        mLviewVehicles = mView.findViewById(R.id.lv_vehicle_list);
        vehicleAdapter = new VehicleAdapter(getActivity(), vehicleDispatches);
        mLviewVehicles.setAdapter(vehicleAdapter);
    }

    private void initData() {
        if (disasterId != null && !disasterId.equals("")) {
//            onGetFireDocumentInfo(disasterId);
            mVehicleInfoPresenter.getDisasterVehicleInfo(disasterId, isShowProgress);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mVehicleInfoPresenter = new VehicleInfoPresenter(this);
//        initData();
    }

    private void onGetFireDocumentInfo(String id) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getVehicleList, RequestMethod.GET);

        request.add("alarmId", id);
        String url = request.url();
//        Log.e("NoHttpDebugTag", "url: " + url);
        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e("NoHttpDebugTag", "result: " + result);
                List<VehicleDispatch> list = null;
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();

                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<VehicleDispatch>>() {
                            }.getType());

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_VEHICLE_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_VEHICLE_FAIL;
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
                msg.what = GET_VEHICLE_FAIL;
                mHandler.sendMessage(msg);

            }
        }, true, true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void setData(List<VehicleDispatch> info) {
        if (info != null) {
            vehicleDispatches.clear();
            vehicleDispatches.addAll(info);
            vehicleAdapter.notifyDataSetChanged();
            mTvSum.setText(vehicleDispatches.size() + "辆");
        }

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
//        CustomToast.makeText(getActivity(), msg, 2000);
//        vehicleDispatches.clear();
//        vehicleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVehicleCompleted(List<VehicleDispatch> info) {

        if (info != null) {
            vehicleDispatches.clear();
            vehicleDispatches.addAll(info);
            vehicleAdapter.notifyDataSetChanged();
            mTvSum.setText(vehicleDispatches.size() + "辆");
        }
    }
}
