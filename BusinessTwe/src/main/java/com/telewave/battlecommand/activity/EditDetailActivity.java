package com.telewave.battlecommand.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.telewave.battlecommand.bean.DisasterDetail;
import com.telewave.battlecommand.bean.FireSummaryInfo;
import com.telewave.battlecommand.bean.ZhddHczj;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.presenter.FireSummaryInfoPresenter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditDetailActivity extends BaseStatusActivity implements View.OnClickListener, IDirectionContract.FireSummaryView {

    private EditText mEtBustArea, mEtDetailTrappedPeople, mEtTrappedPeople, mEtEvacuatedPeople, mEtWaterCost, mEtPropertyLoss, mEtBreathCost;
    private EditText mEtFirePlace;
    private EditText mEtDeathPeople, mEtRescuedPeople, mEtBubble, mEtProtectedProperty, mEtFireDistrict, mEtDescription;
    private TextView mTvSure, mTvCancel;
    private TextView mTvAlarmTime, mTvAlarmPerson, mTvPhoneNumber, mTvAlarmType;
    private TextView mTvAddress, mTvCombustion, mTvChargeUnit, mTvSmog, mTvDisasterRange, mTvState;
    private TextView mEtAcceptedOrderTime, mEtArrivedPresentTime, mEtPutWaterTime, mEtPutFireTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String currentTime;
    private String mAccOrderTime, mPresentTime, mPutWaterTime, mPutFireTime;
    private String disasterId;
    private DisasterDetail disasterDetail;
    private FireSummaryInfoPresenter mSummaryInfoPresenter;
    private boolean isShowProgress = true;

    private ZhddHczj fireSummaryInfo;//火场总结

    private TimePickerView mPvOrderTime, mPvPresentTime, mPvWaterTime, mPvFireTime;

    private static final int UPLOAD_SUMMARY_INFO_SUCCESS = 0x1060;
    private static final int UPLOAD_SUMMARY_INFO_FAIL = 0x1062;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case UPLOAD_SUMMARY_INFO_SUCCESS:
                    ToastUtils.toastShortMessage("上传成功");

                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("fireArea", fireSummaryInfo.getRsmj());
                    bundle.putString("trappedPeople", fireSummaryInfo.getBkrs());
                    bundle.putInt("hurtPeople", fireSummaryInfo.getQzssrs());
                    bundle.putInt("deadPeople", fireSummaryInfo.getQzswrs());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();//传值结束

                    break;
                case UPLOAD_SUMMARY_INFO_FAIL:
                    ToastUtils.toastShortMessage("上传失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_detail;
    }

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        super.setUpViewAndData(savedInstanceState);
        mSummaryInfoPresenter = new FireSummaryInfoPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        mTvSure = (TextView) findViewById(R.id.tv_detail_edit_sure);
        mTvCancel = (TextView) findViewById(R.id.tv_detail_edit_cancel);

        mTvAlarmTime = (TextView) findViewById(R.id.tv_alarmed_time);
        mTvAlarmPerson = (TextView) findViewById(R.id.tv_alarmed_person);
        mTvPhoneNumber = (TextView) findViewById(R.id.tv_contact_phone);
        mTvAlarmType = (TextView) findViewById(R.id.tv_alarm_type);
        mTvAddress = (TextView) findViewById(R.id.tv_disaster_address);
        mTvCombustion = (TextView) findViewById(R.id.tv_disaster_combustion);
        mTvChargeUnit = (TextView) findViewById(R.id.tv_charge_organ);
        mTvSmog = (TextView) findViewById(R.id.tv_edit_smog);
        mTvDisasterRange = (TextView) findViewById(R.id.tv_disaster_range);
        mTvState = (TextView) findViewById(R.id.tv_disaster_state);
        mEtBustArea = (EditText) findViewById(R.id.tv_disaster_detail_bust_area);

        mEtDetailTrappedPeople = (EditText) findViewById(R.id.et_disaster_detail_trapped_people);
        mEtTrappedPeople = (EditText) findViewById(R.id.et_summary_trapped_people);
        mEtEvacuatedPeople = (EditText) findViewById(R.id.et_summary_evacuated_people);
        mEtWaterCost = (EditText) findViewById(R.id.et_summary_water_cost);
        mEtPropertyLoss = (EditText) findViewById(R.id.et_summary_property_loss);
        mEtBreathCost = (EditText) findViewById(R.id.et_summary_breath_number);
        mEtFirePlace = (EditText) findViewById(R.id.et_summary_fire_place);
        mEtAcceptedOrderTime = (TextView) findViewById(R.id.et_summary_accepted_order_time);
        mEtArrivedPresentTime = (TextView) findViewById(R.id.et_summary_arrived_present_time);
        mEtPutWaterTime = (TextView) findViewById(R.id.et_summary_put_water_time);
        mEtPutFireTime = (TextView) findViewById(R.id.et_summary_put_fire_time);
        mEtDeathPeople = (EditText) findViewById(R.id.et_summary_death_people);
        mEtRescuedPeople = (EditText) findViewById(R.id.et_summary_rescued_people);
        mEtBubble = (EditText) findViewById(R.id.et_summary_bubble_cost);
        mEtProtectedProperty = (EditText) findViewById(R.id.et_summary_protected_property_sum);
        mEtFireDistrict = (EditText) findViewById(R.id.et_summary_fire_district);
        mEtDescription = (EditText) findViewById(R.id.et_disaster_description);

        mTvSure.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);

        mEtAcceptedOrderTime.setOnClickListener(this);
        mEtArrivedPresentTime.setOnClickListener(this);
        mEtPutWaterTime.setOnClickListener(this);
        mEtPutFireTime.setOnClickListener(this);

        currentTime = sdf.format(new Date());


    }

    private void initFireTimePicker() {//Dialog 模式下，在底部弹出

        mPvFireTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mEtPutFireTime.setText(getTime(date));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .build();

        //设置初始时间
        if (!TextUtils.isEmpty(mEtPutFireTime.getText().toString().trim())) {
            mPutFireTime = mEtPutFireTime.getText().toString().trim();
            try {
                Date orderDate = sdf.parse(mPutFireTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate);
                mPvFireTime.setDate(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Dialog mDialog = mPvFireTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvFireTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private void initWaterTimePicker() {//Dialog 模式下，在底部弹出

        mPvWaterTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mEtPutWaterTime.setText(getTime(date));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .build();

        //设置初始时间
        if (!TextUtils.isEmpty(mEtPutWaterTime.getText().toString().trim())) {
            mPutWaterTime = mEtPutWaterTime.getText().toString().trim();
            try {
                Date orderDate = sdf.parse(mPutWaterTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate);
                mPvWaterTime.setDate(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Dialog mDialog = mPvWaterTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvWaterTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private void initPresentTimePicker() {//Dialog 模式下，在底部弹出

        mPvPresentTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mEtArrivedPresentTime.setText(getTime(date));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .build();
        //设置初始时间
        if (!TextUtils.isEmpty(mEtArrivedPresentTime.getText().toString().trim())) {
            mPresentTime = mEtArrivedPresentTime.getText().toString().trim();
            try {
                Date orderDate = sdf.parse(mPresentTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate);
                mPvPresentTime.setDate(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Dialog mDialog = mPvPresentTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvPresentTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private void initOrderTimePicker() {//Dialog 模式下，在底部弹出

        mPvOrderTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mEtAcceptedOrderTime.setText(getTime(date));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .build();
        //设置初始时间
        if (!TextUtils.isEmpty(mEtAcceptedOrderTime.getText().toString().trim())) {
            mAccOrderTime = mEtAcceptedOrderTime.getText().toString().trim();
            try {
                Date orderDate = sdf.parse(mAccOrderTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate);
                mPvOrderTime.setDate(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Dialog mDialog = mPvOrderTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvOrderTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private void initData() {

        disasterDetail = (DisasterDetail) getIntent().getSerializableExtra("disasterDetail");
        disasterId = getIntent().getStringExtra("disasterId");

        if (disasterId != null && !disasterId.equals("")) {
            mSummaryInfoPresenter.getDisasterSummaryInfo(disasterId, isShowProgress);
        }
        if (disasterDetail != null) {
            setTransferData(disasterDetail);
        }

    }

    private void setTransferData(DisasterDetail info) {
        mTvAlarmTime.setText(info.getBjsj());
        mTvAlarmPerson.setText(info.getBjr());
        mTvPhoneNumber.setText(info.getBjdh());
        mEtDetailTrappedPeople.setText(info.getBkrs());

        if (info.getZqlx() != null) {
            mTvAlarmType.setText(info.getZqlx().getCodeName());
        }
        mTvAddress.setText(info.getZhdd());
        if (info.getExtend() != null && info.getExtend().getFirematterclass() != null) {
            mTvCombustion.setText(info.getExtend().getFirematterclass().getCodeName());
        }
        if (info.getOragn() != null) {
            mTvChargeUnit.setText(info.getOragn().getName());
        }
        if (info.getExtend() != null && info.getExtend().getSmogstatus() != null) {
            mTvSmog.setText(info.getExtend().getSmogstatus().getCodeName() != null ?
                    info.getExtend().getSmogstatus().getCodeName() : "");
        }
        if (info.getZhdj() != null) {
            mTvDisasterRange.setText(info.getZhdj().getCodeName());
        }
        if (info.getRsmj() != null) {
            mEtBustArea.setText(info.getRsmj());
        }
        if (info.getZqzt() != null) {
            mTvState.setText(info.getZqzt().getCodeName());
        }
        if (info.getZqms() != null) {
            mEtDescription.setText(info.getZqms());
        }
    }

    private ZhddHczj onGetFireSummaryInfo() {
        ZhddHczj temp = new ZhddHczj();
        if (!TextUtils.isEmpty(mEtDetailTrappedPeople.getText().toString().trim())) {
            temp.setBkrs(mEtDetailTrappedPeople.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(mEtBustArea.getText().toString().trim())) {
            temp.setRsmj(mEtBustArea.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(disasterDetail.getZqzt().toString())) {
            temp.setZqzt(disasterDetail.getZqzt().getCodeName());
        }

        if (!TextUtils.isEmpty(disasterId)) {
            temp.setZquuid(disasterId);
        }
        if (!TextUtils.isEmpty(mEtTrappedPeople.getText().toString().trim())) {
            temp.setQzssrs(Integer.valueOf(mEtTrappedPeople.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(mEtEvacuatedPeople.getText().toString().trim())) {
            temp.setSsrs(mEtEvacuatedPeople.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(mEtWaterCost.getText().toString().trim())) {
            temp.setSxh(Integer.valueOf(mEtWaterCost.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(mEtPropertyLoss.getText().toString().trim())) {
            temp.setZjccss(mEtPropertyLoss.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(mEtBreathCost.getText().toString().trim())) {
            temp.setXhkhqsl(Integer.valueOf(mEtBreathCost.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(mEtFirePlace.getText().toString().trim())) {
            temp.setRscs(mEtFirePlace.getText().toString().trim());
        }
        mAccOrderTime = mEtAcceptedOrderTime.getText().toString().trim();
        if (!TextUtils.isEmpty(mAccOrderTime)) {
            temp.setJsmlsjStr(mAccOrderTime);
        }
        mPresentTime = mEtArrivedPresentTime.getText().toString().trim();
        if (!TextUtils.isEmpty(mPresentTime)) {
            temp.setDdxcsjStr(mPresentTime);
        }
        mPutWaterTime = mEtPutWaterTime.getText().toString().trim();
        if (!TextUtils.isEmpty(mPutWaterTime)) {
            temp.setDccssjStr(mPutWaterTime);
        }
        mPutFireTime = mEtPutFireTime.getText().toString().trim();
        if (!TextUtils.isEmpty(mPutFireTime)) {
            temp.setPhsjStr(mPutFireTime);
        }
        if (!TextUtils.isEmpty(mEtDeathPeople.getText().toString().trim())) {
            temp.setQzswrs(Integer.valueOf(mEtDeathPeople.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(mEtRescuedPeople.getText().toString().trim())) {
            temp.setJjrs(Integer.valueOf(mEtRescuedPeople.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(mEtBubble.getText().toString().trim())) {
            temp.setPmxh(mEtBubble.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(mEtProtectedProperty.getText().toString().trim())) {
            temp.setBhcc(mEtProtectedProperty.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(mEtFireDistrict.getText().toString().trim())) {
            temp.setHzdyfl(mEtFireDistrict.getText().toString().trim());
        }

        return temp;
    }

    private void onUploadDetailInfo() {

        fireSummaryInfo = onGetFireSummaryInfo();

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.uploadDisasterDetail, RequestMethod.POST);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(fireSummaryInfo);

        Log.e("NoHttpDebugTag", "开始灾情----------" + jsonStr);
        request.setDefineRequestBodyForJson(jsonStr);
        request.setCacheKey(ConstData.DISASTER_LIST_TYPE);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        NoHttpManager.getRequestInstance().add(EditDetailActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> stringResponse) {
                String result = stringResponse.get();
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            String str = responseBean.getData();
                            if (str.equals("true")) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPLOAD_SUMMARY_INFO_SUCCESS;
                                //msg.obj = tempInfo;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPLOAD_SUMMARY_INFO_FAIL;
                                mHandler.sendMessage(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                msg.what = UPLOAD_SUMMARY_INFO_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    @Override
    public void onClick(View view) {
        final int vId = view.getId();
        if (R.id.tv_detail_edit_sure == vId) {
//                ToastUtils.toastShortMessage("确定");
            onUploadDetailInfo();
        } else if (R.id.tv_detail_edit_cancel == vId) {
//                ToastUtils.toastShortMessage("取消");
            finish();
        } else if (R.id.et_summary_accepted_order_time == vId) {//接收命令时间
            mPvOrderTime.show(mEtAcceptedOrderTime);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
        } else if (R.id.et_summary_arrived_present_time == vId) {//到达现场时间
            mPvPresentTime.show(mEtArrivedPresentTime);
        } else if (R.id.et_summary_put_water_time == vId) {//到场扑水时间
            mPvWaterTime.show(mEtPutWaterTime);
        } else if (R.id.et_summary_put_fire_time == vId) {//扑火时间
            mPvFireTime.show(mEtPutFireTime);
        } else {
        }
    }

    private void getData(FireSummaryInfo info) {

        mEtTrappedPeople.setText(info.getQzssrs());
        mEtEvacuatedPeople.setText(info.getSsrs());
        mEtWaterCost.setText(info.getSxh());
        mEtPropertyLoss.setText(info.getZjccss());
        mEtBreathCost.setText(info.getXhkhqsl());
        mEtFirePlace.setText(info.getRscs());
        mEtAcceptedOrderTime.setText(info.getJsmlsj());
        mEtArrivedPresentTime.setText(info.getDdxcsj());
        mEtPutWaterTime.setText(info.getDccssj());
        mEtPutFireTime.setText(info.getPhsj());
        mEtDeathPeople.setText(info.getQzswrs());
        mEtRescuedPeople.setText(info.getJjrs());
        mEtBubble.setText(info.getPmxh());
        mEtProtectedProperty.setText(info.getBhcc());
        mEtFireDistrict.setText(info.getHzdyfl());

        initOrderTimePicker();
        initPresentTimePicker();
        initWaterTimePicker();
        initFireTimePicker();
    }

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(EditDetailActivity.this, "正在加载,请稍后...");
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
            getData(info);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
