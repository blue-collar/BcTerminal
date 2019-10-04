package com.telewave.battlecommand.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.DisasterStatisticBean;
import com.telewave.battlecommand.bean.UnitDisasterCount;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * 警情柱状图展示
 *
 * @author zhangjun
 * @date 2019-08-08
 */

public class DisasterStatisticActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvStartTime, mTvEndTime;
    private ImageView mImgRefresh;
    private LinearLayout mLayoutBack;

    private ColumnChartView mColumnChartView;
    private ColumnChartData data;             //存放柱状图数据的对象

    //折线图 点数据集合
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    //折线图 X坐标数据集合
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    //折线图 Y坐标数据集合
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();

    List<Column> columns = new ArrayList<>();
    List<SubcolumnValue> values = new ArrayList<>();
    List<AxisValue> axisValues = new ArrayList<>();

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    private TimePickerView mStartTimePickerView, mEndTimePickerView;
    private String officeId;
    private String startTime, endTime;
    private String currentTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<UnitDisasterCount> mData = new ArrayList<>();

    private static final int GET_DISASTER_STATISTIC_SUCCESS = 0X2021;
    private static final int GET_DISASTER_STATISTIC_FAIL = 0X2023;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_DISASTER_STATISTIC_SUCCESS:
                    if (msg.obj != null) {
                        List<UnitDisasterCount> list = (List<UnitDisasterCount>) msg.obj;
                        mData.clear();
                        mData.addAll(list);
                        generateDisasterCountData();
                    }

                    break;
                case GET_DISASTER_STATISTIC_FAIL:

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_disaster_statiscic);

        initView();
        initData();

    }

    private void initView() {
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mImgRefresh = (ImageView) findViewById(R.id.iv_refresh);
        mLayoutBack = (LinearLayout) findViewById(R.id.back_layout);
        mTvStartTime.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        mImgRefresh.setOnClickListener(this);
        mLayoutBack.setOnClickListener(this);

        mTvStartTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
        currentTime = sdf.format(new Date());
        mTvEndTime.setText(currentTime);

        initStartTimePicker();
        initEndTimePicker();

        mColumnChartView = findViewById(R.id.unit_column_chart_view);

    }


    private void generateDisasterCountData() {

        columns.clear();
        for (int i = 0; i < mData.size(); ++i) {
            UnitDisasterCount tempInfo = mData.get(i);

            SubcolumnValue tempValue = new SubcolumnValue();
            tempValue.setColor(getResources().getColor(R.color.blue));
            tempValue.setValue((float) tempInfo.getNum());
            tempValue.setLabel(tempInfo.getName());
            values.add(tempValue);

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

//        mPointValues.clear();
        mAxisXValues.clear();
        mAxisYValues.clear();
        Axis axisX = new Axis();
        axisX.setTextColor(Color.BLACK);
//        axisX.setName("各单位名称");
        Axis axisYLeft = new Axis().setHasLines(true);
        axisYLeft.setTextColor(Color.BLACK);
        axisYLeft.setName("警情数");
        float maxPoint = getMax(mData);

        for (int i = 0; i < maxPoint; i++) {
            AxisValue axisValue = new AxisValue(i);
            mAxisYValues.add(axisValue);
        }
        for (int i = 0; i < mData.size(); i++) {
//            mPointValues.add(new PointValue(i, mData.get(i).getNum()));
            mAxisXValues.add(new AxisValue(i).setLabel(mData.get(i).getName()));
        }

        axisX.setValues(mAxisXValues);
        axisYLeft.setValues(mAxisYValues);
        axisX.setHasTiltedLabels(true);//斜着显示
        axisX.setHasLines(true);//X轴分割线
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisYLeft);

        data.setValueLabelBackgroundColor(Color.BLACK);
//        data.setFillRatio(0.5f);
        mColumnChartView.setColumnChartData(data);

        mColumnChartView.setZoomEnabled(true);
        mColumnChartView.setScrollEnabled(true);
        mColumnChartView.setInteractive(true);
//        mColumnChartView.setMaxZoom(mData.size()/6 + 66);
//        mColumnChartView.setOnValueTouchListener(new LineTouchListener());
        mColumnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        //viewport必须设置在setLineChartData后面，设置一个当前viewport，再设置一个maxviewport，
        //就可以实现滚动，高度要设置数据的上下限
        //设置是否允许在动画进行中或设置完表格数据后，自动计算viewport的大小。如果禁止，则需要可以手动设置。
        mColumnChartView.setViewportCalculationEnabled(true);
        Viewport viewport = new Viewport(0, mColumnChartView.getMaximumViewport().height() * 2.5f, mAxisXValues.size() > 5 ? 5 : mAxisXValues.size(), 0);
//        mColumnChartView.setCurrentViewport(viewport);
        mColumnChartView.moveTo(0, 0);
        mColumnChartView.setCurrentViewport(viewport);

        Viewport v = new Viewport(mColumnChartView.getMaximumViewport());
        //设置最大化的viewport （chartdata）后再调用
        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,
        // 不然显示的坐标数据是不能左右滑动查看更多数据的
//        mColumnChartView.setMaximumViewport(v);
        //左边起始位置 轴
        v.top = 30;
        v.left = 0;
        v.right = mData.size();
        v.bottom = 0;
        //左右滑动
        mColumnChartView.setCurrentViewport(v);

    }


    private void initData() {

        officeId = ConstData.ORGANID;
        onGetStatisticData(startTime, endTime, officeId);
    }

    private void onGetStatisticData(String startTime, String endTime, String officeId) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getDisasterStatistic, RequestMethod.POST);
        DisasterStatisticBean tempInfo = new DisasterStatisticBean();
        tempInfo.setBeginBjsj(startTime);
        tempInfo.setEndBjsj(endTime);
        tempInfo.setOfficeId(officeId);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(tempInfo);
        request.setDefineRequestBodyForJson(jsonStr);

        NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                List<UnitDisasterCount> list = null;
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<UnitDisasterCount>>() {
                            }.getType());

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_DISASTER_STATISTIC_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_DISASTER_STATISTIC_FAIL;
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
                msg.what = GET_DISASTER_STATISTIC_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }


    private void initStartTimePicker() {//Dialog 模式下，在底部弹出

        mStartTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTvStartTime.setText(getTime(date));

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
        if (!TextUtils.isEmpty(mTvStartTime.getText().toString().trim())) {
            startTime = mTvStartTime.getText().toString().trim();
            try {
                Date orderDate = sdf.parse(startTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate);
                mStartTimePickerView.setDate(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Dialog mDialog = mStartTimePickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mStartTimePickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private void initEndTimePicker() {//Dialog 模式下，在底部弹出

        mEndTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTvEndTime.setText(getTime(date));

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
        if (!TextUtils.isEmpty(mTvEndTime.getText().toString().trim())) {
            endTime = mTvEndTime.getText().toString().trim();
            try {
                Date orderDate = sdf.parse(endTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate);
                mEndTimePickerView.setDate(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Dialog mDialog = mEndTimePickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mEndTimePickerView.getDialogContainerLayout().setLayoutParams(params);

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

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.back_layout == vId) {
            finish();
        } else if (R.id.tv_start_time == vId) {     //开始时间
            mStartTimePickerView.show(mTvStartTime);
        } else if (R.id.tv_end_time == vId) {       //结束时间
            mEndTimePickerView.show(mTvEndTime);
        } else if (R.id.iv_refresh == vId) {
            startTime = mTvStartTime.getText().toString().trim();
            endTime = mTvEndTime.getText().toString().trim();
            if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                onGetStatisticData(startTime, endTime, officeId);
            }
        } else {
        }

    }


    /**
     * 获取Y轴最大值
     *
     * @param staticDisasterCountBeanList
     * @return
     */
    public float getMax(List<UnitDisasterCount> staticDisasterCountBeanList) {
        float max = 0;
        for (int i = 0; i < staticDisasterCountBeanList.size(); i++) {
            max = Math.max(max, staticDisasterCountBeanList.get(i).getNum());
        }
        return max;
    }
}
