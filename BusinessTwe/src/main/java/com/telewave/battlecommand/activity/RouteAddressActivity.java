/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.telewave.battlecommand.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.telewave.battlecommand.adapter.NaviHistoryAdapter;
import com.telewave.battlecommand.db.NaviMsg;
import com.telewave.battlecommand.db.NaviMsgDao;
import com.telewave.battlecommand.db.manager.NaviMsgDaoManager;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 灾情路径导航地址页
 */
public class RouteAddressActivity extends BaseActivity {

    public static final int GET_ADDRESS = 0x1001;
    public static final int CLICK_START = 1;
    public static final int CLICK_END = 2;
    private TextView startAddressTv, endAddressTv;
    private ImageView upDownChangeIv;
    private TextView mClearNaviHistory = null;
    private TextView mStartRountPlan = null;

    private PermissionHelper permissionHelper;

    private LocationClient mLocationClient;
    private double myLatitude;
    private double myLongitude;
    private String currentAddress;
    private LatLng currentLatLng;
    private MyLocationListener mylocationListener = new MyLocationListener();

    private ListView naviHistoryListView;

    private LatLng endLatLng = null;
    private String endAddress = null;

    private LatLng startLatLngTmp = null;
    private String startAddressTmp = null;
    private LatLng endLatLngTmp = null;
    private String endAddressTmp = null;

    private List<NaviMsg> naviMsgs;
    private NaviHistoryAdapter naviHistoryAdapter;


    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_address);
        //权限申请帮助类
        permissionHelper = new PermissionHelper(this);
        endLatLng = getIntent().getParcelableExtra("enableLatLng");
        endAddress = getIntent().getStringExtra("enableAddress");
        naviMsgs = new ArrayList<>();
        endLatLngTmp = endLatLng;
        endAddressTmp = endAddress;
        initView();
        initMyLocation();
        endAddressTv.setText(endAddress);
    }

    private void initView() {
        startAddressTv = (TextView) findViewById(R.id.start_address_text);
        endAddressTv = (TextView) findViewById(R.id.end_address_text);
        upDownChangeIv = (ImageView) findViewById(R.id.up_down_change_iv);
        naviHistoryListView = (ListView) findViewById(R.id.navi_history_list);
        mClearNaviHistory = (TextView) findViewById(R.id.clear_navi_history);
        mStartRountPlan = (TextView) findViewById(R.id.start_rountplan_btn);
        findViewById(R.id.start_address_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RouteAddressActivity.this, RoutePlanChoiceAddressActivity.class);
                intent.putExtra("isBegin", CLICK_START);
                intent.putExtra("Address", currentAddress);
                intent.putExtra("LatLng", currentLatLng);
                startActivityForResult(intent, GET_ADDRESS);
            }
        });
        findViewById(R.id.end_address_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RouteAddressActivity.this, RoutePlanChoiceAddressActivity.class);
                intent.putExtra("isBegin", CLICK_END);
                intent.putExtra("Address", endAddress);
                intent.putExtra("LatLng", endLatLng);
                startActivityForResult(intent, GET_ADDRESS);
            }
        });

        upDownChangeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAddress = endAddressTmp;
                currentLatLng = endLatLngTmp;
                startAddressTv.setText(currentAddress);

                endAddress = startAddressTmp;
                endLatLng = startLatLngTmp;
                endAddressTv.setText(endAddress);

                startAddressTmp = currentAddress;
                startLatLngTmp = currentLatLng;
                endAddressTmp = endAddress;
                endLatLngTmp = endLatLng;

            }
        });
        mStartRountPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endLatLng != null) {
                    Intent intent = new Intent(RouteAddressActivity.this, RoutePlanActivity.class);
                    intent.putExtra("StartLatLng", currentLatLng);
                    intent.putExtra("StartAddress", currentAddress);
                    intent.putExtra("EndLatLng", endLatLng);
                    intent.putExtra("EndAddress", endAddress);
                    startActivity(intent);
                    NaviMsg naviMsg = new NaviMsg(currentAddress, currentLatLng.latitude, currentLatLng.longitude,
                            endAddress, endLatLng.latitude, endLatLng.longitude);
                    //消息入库
                    NaviMsgDaoManager.getInstance().getmDaoSession().getNaviMsgDao().insertOrReplace(naviMsg);
                } else {
                    ToastUtils.toastShortMessage("请选择导航终点");
                }
            }
        });
        naviHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NaviMsg naviMsg = naviMsgs.get(position);
                currentAddress = naviMsg.getStartAddress();
                currentLatLng = new LatLng(naviMsg.getStartLat(), naviMsg.getStartLon());
                startAddressTv.setText(currentAddress);
                startLatLngTmp = currentLatLng;
                startAddressTmp = currentAddress;

                endAddress = naviMsg.getEndAddress();
                endLatLng = new LatLng(naviMsg.getEndLat(), naviMsg.getEndLon());
                endAddressTv.setText(endAddress);
                endLatLngTmp = endLatLng;
                endAddressTmp = endAddress;
            }
        });
        mClearNaviHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaviMsgDaoManager.getInstance().getmDaoSession().getNaviMsgDao().deleteAll();
                naviMsgs.clear();
                naviHistoryAdapter.notifyDataSetChanged();
                mClearNaviHistory.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initMyLocation() {
        permissionHelper.requestPermissions("需要定位权限", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permissions) {
                if (mLocationClient == null) {
                    mLocationClient = new LocationClient(RouteAddressActivity.this.getApplicationContext());
                    mLocationClient.registerLocationListener(mylocationListener);
                    LocationClientOption options = new LocationClientOption();
                    options.setOpenGps(true);
                    options.setCoorType("bd09ll");
                    options.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                    options.disableCache(true);
                    options.setScanSpan(0);
                    options.setIsNeedAddress(true);
                    mLocationClient.setLocOption(options);
                    mLocationClient.start();
                } else {
                    mLocationClient.requestLocation();
                }
            }

            @Override
            public void doAfterDenied(String... permissions) {
//                permissionHelper.openPermissionsSetting("请到设置>权限中打开定位权限");
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 定位监听者
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null) {
                //获取自己所在位置的经纬度
                myLatitude = bdLocation.getLatitude();
                myLongitude = bdLocation.getLongitude();
                currentAddress = bdLocation.getAddrStr();
                currentLatLng = new LatLng(myLatitude, myLongitude);
                startAddressTv.setText(currentAddress);

                startLatLngTmp = currentLatLng;
                startAddressTmp = currentAddress;
                Log.e("onReceiveLocation", "onReceiveLocation: " + currentAddress);
                Log.e("onReceiveLocation", "myLatitude: " + myLatitude + "myLongitude:" + myLongitude);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_ADDRESS) {
                Bundle bundle = data.getExtras();
                PoiInfo poiInfo = bundle.getParcelable("poiInfo");
                int isBegin = bundle.getInt("isBegin", 0);
                if (isBegin == CLICK_START) {
                    startAddressTv.setText(poiInfo.name);
                    currentAddress = poiInfo.name;
                    currentLatLng = poiInfo.location;
                    startLatLngTmp = currentLatLng;
                    startAddressTmp = currentAddress;
                } else if (isBegin == CLICK_END) {
                    endAddressTv.setText(poiInfo.name);
                    endAddress = poiInfo.name;
                    endLatLng = poiInfo.location;
                    endLatLngTmp = endLatLng;
                    endAddressTmp = endAddress;
                }
            }
        }
    }

    /**
     * 权限处理
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        naviMsgs = NaviMsgDaoManager.getInstance().getmDaoSession().getNaviMsgDao()
                .queryBuilder().orderDesc(NaviMsgDao.Properties.Id).build().list();
        if (naviMsgs.size() > 0) {
            mClearNaviHistory.setVisibility(View.VISIBLE);
        } else {
            mClearNaviHistory.setVisibility(View.GONE);
        }
        naviHistoryAdapter = new NaviHistoryAdapter(RouteAddressActivity.this, naviMsgs);
        naviHistoryListView.setAdapter(naviHistoryAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
