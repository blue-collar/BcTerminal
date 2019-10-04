package com.telewave.battlecommand.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.DutyTableDayAdapter;
import com.telewave.battlecommand.bean.OrganInfo;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.CalendarUtils;
import com.telewave.lib.base.util.DoubleClickUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;

/**
 * 值班表 日 Fragment
 *
 * @author liwh
 * @date 2019/1/15
 */
public class DutyTableDayFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DutyTableDayFragment";
    private View mView;
    private LinearLayout choiceCalendarLayout;
    private TextView calendarTextView, dayTimeTextView;
    private TableLayout tableLayout;
    private ListView dutyTableListView;

    private int year;
    private int month;
    private int day;
    private String choiceTime;

    private static final int GET_PARENT_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;
    private static final int GET_NO_DATA = 0x1003;


    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_PARENT_SUCCESS:
                    List<OrganInfo> organInfoListTemp = (List<OrganInfo>) msg.obj;
                    if (organInfoListTemp == null || organInfoListTemp.isEmpty()) {
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        DutyTableDayAdapter dutyTableDayAdapter = new DutyTableDayAdapter(getActivity(), organInfoListTemp);
                        dutyTableListView.setAdapter(dutyTableDayAdapter);
                        //控制行数
                        for (int row = 0; row < organInfoListTemp.size(); row++) {
                            TableRow tabRow = new TableRow(getActivity());
                            //控制列数
                            for (int col = 0; col < 2; col++) {
                                TextView tv = new TextView(getActivity());
                                if (col == 0) {
                                    tv.setText(organInfoListTemp.get(row).getName());
                                    tv.setGravity(Gravity.CENTER);
                                    tv.setTextColor(Color.WHITE); //字体颜色
                                    tv.setBackgroundColor(Color.BLUE); //背景色
                                } else if (col == 1) {
                                    tv.setText(organInfoListTemp.get(row).getId());
                                    tv.setGravity(Gravity.LEFT);
                                    tv.setTextColor(Color.BLACK); //字体颜色
                                    tv.setBackgroundColor(Color.WHITE); //背景色
                                }
                                tabRow.addView(tv);

                            }
                            LinearLayout.LayoutParams lp = new TableRow.LayoutParams(-2, -2);
                            lp.setMargins(1, 1, 1, 1);
                            tabRow.setLayoutParams(lp);
                            tableLayout.addView(tabRow);
                        }

                    }
                    break;
                case GET_NO_DATA:
                    ToastUtils.toastShortMessage("暂无数据");
                    break;
                case GET_FAIL:
                    ToastUtils.toastShortMessage("获取失败");
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_duty_table_day, container, false);
        initView();
        choiceTime = CalendarUtils.getCurrentDate();
        getOrganList();
        return mView;
    }

    private void initView() {
        choiceCalendarLayout = (LinearLayout) mView.findViewById(R.id.choice_calendar_layout);
        calendarTextView = (TextView) mView.findViewById(R.id.calendar_textview);
        dayTimeTextView = (TextView) mView.findViewById(R.id.day_time_textview);
        dutyTableListView = (ListView) mView.findViewById(R.id.duty_table_list);
        tableLayout = (TableLayout) mView.findViewById(R.id.tab_01);
        choiceCalendarLayout.setOnClickListener(this);
        calendarTextView.setText(CalendarUtils.getCurrentDate());
        dayTimeTextView.setText(CalendarUtils.getCurrentDate() + CalendarUtils.getWeek(CalendarUtils.getCurrentDate()));
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.choice_calendar_layout == vId) {
            Dialog dialog = null;
            if (year != 0 && month != 0 && day != 0) {
                dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearTemp,
                                                  int monthOfYear, int dayOfMonth) {
                                String monthTemp;
                                String dayTemp;
                                if ((monthOfYear + 1) < 10) {
                                    monthTemp = "0" + (monthOfYear + 1);
                                } else {
                                    monthTemp = "" + (monthOfYear + 1);
                                }
                                if (dayOfMonth < 10) {
                                    dayTemp = "0" + dayOfMonth;
                                } else {
                                    dayTemp = "" + dayOfMonth;
                                }
                                year = yearTemp;
                                month = monthOfYear + 1;
                                day = dayOfMonth;
                                choiceTime = yearTemp + "-" + monthTemp + "-" + dayTemp;
                                calendarTextView.setText(choiceTime);
                                dayTimeTextView.setText(choiceTime + CalendarUtils.getWeek(choiceTime));
//                                    getQianDaoList(ConstData.WZID, choiceTime);
//                                    getUnQianDaoMember(ConstData.WZID, choiceTime);
                            }
                            // 表示默认的年月日
                        }, year, month - 1, day);
            } else {
                Calendar calendar = Calendar.getInstance();
                final int thisYear = calendar.get(Calendar.YEAR);
                final int thisMonth = calendar.get(Calendar.MONTH);
                final int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearTemp,
                                                  int monthOfYear, int dayOfMonth) {
                                String monthTemp;
                                String dayTemp;
                                if ((monthOfYear + 1) < 10) {
                                    monthTemp = "0" + (monthOfYear + 1);
                                } else {
                                    monthTemp = "" + (monthOfYear + 1);
                                }
                                if (dayOfMonth < 10) {
                                    dayTemp = "0" + dayOfMonth;
                                } else {
                                    dayTemp = "" + dayOfMonth;
                                }
                                year = yearTemp;
                                month = monthOfYear + 1;
                                day = dayOfMonth;
                                choiceTime = yearTemp + "-" + monthTemp + "-" + dayTemp;
                                calendarTextView.setText(choiceTime);
                                dayTimeTextView.setText(choiceTime + CalendarUtils.getWeek(choiceTime));
//                                    getQianDaoList(ConstData.WZID, choiceTime);
//                                    getUnQianDaoMember(ConstData.WZID, choiceTime);
                            }
                            // 表示默认的年月日
                        }, thisYear, thisMonth, thisDay);
            }
            if (DoubleClickUtils.isFastDoubleClick()) {
                return;
            } else {
                dialog.show();
            }
        } else {
        }
    }

    //获取机构列表信息
    private void getOrganList() {
        String httpUrl = "http://telewave-sx.uicp.net:15805/twmfs/TelewaveMFS/sys/findChildByOfficeId";
        Request<String> request = NoHttp.createStringRequest(httpUrl, RequestMethod.GET);
        // 添加请求参数
        request.add("id", "");
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
                            List<OrganInfo> mOrganInfoList = gson.fromJson(responseBean.getData(), new TypeToken<List<OrganInfo>>() {
                            }.getType());
                            if (null == mOrganInfoList || mOrganInfoList.size() == 0) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_NO_DATA;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_PARENT_SUCCESS;
                                msg.obj = mOrganInfoList;
                                mHandler.sendMessage(msg);
                            }
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

            }
        }, true, true);
    }
}