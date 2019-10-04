/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.telewave.battlecommand.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.telewave.battlecommand.adapter.MyReportListAdapter;
import com.telewave.battlecommand.bean.Water;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.Tips;
import com.telewave.lib.widget.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 水源信息
 */
public class HydrantMsgActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_search;
    private ImageView iv_search;
    private ListView hydrant_list_view;
    private RefreshLayout mRefreshLayout;
    private int listPage = 1;
    private List<Water> list;
    private MyReportListAdapter adapter;
    private int listSize = 15;
    private ImageView hydrant_add_iv;

    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_hydrant_msg);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        listPage = 1;
        initData();
    }

    private void initView() {
        hydrant_add_iv = findViewById(R.id.hydrant_add_iv);
        hydrant_add_iv.setOnClickListener(this);
        list = new ArrayList<>();
        adapter = new MyReportListAdapter(list, this);
        hydrant_list_view = findViewById(R.id.hydrant_list_view);
        hydrant_list_view.setAdapter(adapter);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);

        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                Log.i("list:", "=========loadmore======");
                listPage++;
                initData();
            }

            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                Log.i("list:", "========refresh=======");
                listPage = 1;
                initData();

            }
        });
        hydrant_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HydrantMsgActivity.this, AddWaterActivity.class);
                intent.putExtra("water", JsonUtils.objectToString(list.get(position)));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.iv_search == vId) {
//                if(!et_search.getText().toString().trim().equals("")){
            listPage = 1;
            initData();
//                }
        } else if (R.id.hydrant_add_iv == vId) {
            startActivity(new Intent(this, AddWaterActivity.class));
        } else {
        }
    }

    private void initData() {
        String text = et_search.getText().toString().trim();
        Tips.showProgress(this, "正在请求，请稍后。。。");
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", et_search.getText().toString().trim());
        map.put("pageNo", listPage);
        map.put("pageSize", listSize);
        map.put("createId", SharePreferenceUtils.getDataSharedPreferences(this, "userid"));
        OkGo.<String>post(ConstData.urlManager.getWaterList)//
                .tag(this)//
                .upJson(JsonUtils.objectToString(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Tips.dismissProgress();
                        String data = response.body();
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            JSONArray array = object.getJSONObject("data").getJSONArray("list");

                            Double totalCount = object.getJSONObject("data").getDouble("count") / listSize;
                            int totalpage = (int) Math.ceil(totalCount);
                            if (array != null) {
                                //分开第一页和其他页
                                if (listPage == 1) {
                                    list.clear();
                                    list.addAll(JsonUtils.stringToList(array.toString(), Water.class));
                                } else {
                                    list.addAll(JsonUtils.stringToList(array.toString(), Water.class));
                                }
                                adapter.notifyDataSetChanged();

                                if (listPage >= totalpage) {
                                    mRefreshLayout.finishLoadmoreWithNoMoreData();
                                } else {
                                    mRefreshLayout.resetNoMoreData();
                                }
                            }
                        } catch (JSONException e) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                            e.printStackTrace();
                        }
                        mRefreshLayout.finishLoadmore();
                        mRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Tips.dismissProgress();
                        mRefreshLayout.finishLoadmore();
                        mRefreshLayout.finishRefresh();
                        super.onError(response);
                    }
                });
    }

}
