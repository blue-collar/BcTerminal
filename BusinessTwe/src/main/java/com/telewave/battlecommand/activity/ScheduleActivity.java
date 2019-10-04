/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.telewave.battlecommand.adapter.ScheduleAdapter;
import com.telewave.battlecommand.bean.Schedule;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DateUtils;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.KeyBoardHiddenUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.Tips;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.lib.widget.util.DateTimePickDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 水源信息
 */
public class ScheduleActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_schedule_list;
    private List<Schedule> list;
    private ScheduleAdapter adapter;
    private TextView tv_time, tv_filter, tv_schedule_title;

    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_schedule_list);
        initView();
        initData();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new ScheduleAdapter(list, this);
        lv_schedule_list = findViewById(R.id.lv_schedule_list);
        lv_schedule_list.setAdapter(adapter);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_time = findViewById(R.id.tv_time);
        tv_filter = findViewById(R.id.tv_filter);
        tv_time.setOnClickListener(this);
        tv_filter.setOnClickListener(this);
        tv_schedule_title = findViewById(R.id.tv_schedule_title);
        tv_time.setText(DateUtils.getDate());
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.tv_filter == vId) {
            initData();
        } else if (R.id.tv_time == vId) {
            KeyBoardHiddenUtils.closeInput(this);
            DateTimePickDialogUtil startDialog = new DateTimePickDialogUtil(this);
            startDialog.dateTimePicKDialog(tv_time, "", DateTimePickDialogUtil.SELECT_START_TIME);
        } else {
        }
    }

    private void initData() {
        Tips.showProgress(this, "正在请求，请稍后。。。");
        OkGo.<String>post(ConstData.urlManager.getSchedule)//
                .tag(this)//
                .params("organId", SharePreferenceUtils.getDataSharedPreferences(this, "organid"))
                .params("findDate", tv_time.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Tips.dismissProgress();
                        String data = response.body();
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            if (code != 1) {
                                return;
                            }

                            tv_schedule_title.setText(SharePreferenceUtils.getDataSharedPreferences(ScheduleActivity.this, "organName")
                                    + "值班安排表("
                                    + tv_time.getText().toString()
                                    + ")"
                            );
                            JSONArray array = object.getJSONArray("data");
                            list.clear();
                            list.addAll(JsonUtils.stringToList(array.toString(), Schedule.class));
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Tips.dismissProgress();
                        super.onError(response);
                    }
                });
    }

}
