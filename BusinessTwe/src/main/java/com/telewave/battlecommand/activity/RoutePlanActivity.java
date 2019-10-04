/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.telewave.battlecommand.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.telewave.battlecommand.navi.BNaviGuideActivity;
import com.telewave.battlecommand.navi.NavigationUtil;
import com.telewave.battlecommand.navi.WNaviGuideActivity;
import com.telewave.battlecommand.overlayutil.BikingRouteOverlay;
import com.telewave.battlecommand.overlayutil.DrivingRouteOverlay;
import com.telewave.battlecommand.overlayutil.OverlayManager;
import com.telewave.battlecommand.overlayutil.WalkingRouteOverlay;
import com.telewave.battlecommand.view.MultiLineRadioGroup;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ScreenUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.map.util.MapUtil;
import com.telewave.lib.widget.BaseActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 路径规划
 */
public class RoutePlanActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    private final static String TAG = RoutePlanActivity.class.getSimpleName();
    private TextView mStartNavi = null;
    private LinearLayout showRouteLineLayout = null;
    private RouteLine route = null;
    private OverlayManager routeOverlay = null;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaidumap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    private LatLng startLatLng = null;
    private String startAddress = null;
    private LatLng endLatLng = null;
    private String endAddress = null;

    private LinearLayout mLayout_tab0;
    private LinearLayout mLayout_tab1;
    private LinearLayout mLayout_tab2;

    WalkingRouteResult nowResultwalk = null;
    BikingRouteResult nowResultbike = null;
    DrivingRouteResult nowResultdrive = null;

    //是否选中了驾车模式
    private boolean isCheckedCar = true;
    //是否选中了骑行模式
    private boolean isCheckedBike = false;
    //是否选中了步行模式
    private boolean isCheckedWalk = false;

    private BikeNaviLaunchParam bikeParam;
    private WalkNaviLaunchParam walkParam;

    private BNRoutePlanNode mStartNode = null;
    private BNRoutePlanNode mEndNode = null;

    private int driveRoutePlanType = 0;

    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_plan);
        mStartNavi = (TextView) findViewById(R.id.start_navi_btn);
        showRouteLineLayout = (LinearLayout) findViewById(R.id.show_route_line_layout);
        final MultiLineRadioGroup multiLineRadioGroup = (MultiLineRadioGroup) findViewById(R.id.navi_type_group);
        startLatLng = getIntent().getParcelableExtra("StartLatLng");
        startAddress = getIntent().getStringExtra("StartAddress");
        endLatLng = getIntent().getParcelableExtra("EndLatLng");
        endAddress = getIntent().getStringExtra("EndAddress");
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.route_map);
        mBaidumap = mMapView.getMap();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(true);

        //开启交通图 路况
        mBaidumap.setTrafficEnabled(true);

        //设置放大缩小组件位置
        mMapView.getChildAt(2).setPadding(0, 0, 0, ScreenUtils.dip2px(RoutePlanActivity.this, 50));

        multiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MultiLineRadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.diameter_type_car) {
                    isCheckedCar = true;
                    isCheckedBike = false;
                    isCheckedWalk = false;
                    searchButtonProcess();
                } else if (checkedId == R.id.diameter_type_bike) {
                    isCheckedCar = false;
                    isCheckedBike = true;
                    isCheckedWalk = false;
                    searchButtonProcess();
                } else if (checkedId == R.id.diameter_type_walk) {
                    isCheckedCar = false;
                    isCheckedBike = false;
                    isCheckedWalk = true;
                    searchButtonProcess();
                }
            }
        });

        mLayout_tab0 = findViewById(R.id.route_0);
        mLayout_tab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_tab0.setSelected(true);
                mLayout_tab1.setSelected(false);
                mLayout_tab2.setSelected(false);
                routeOverlay.removeFromMap();
                if (isCheckedCar) {
                    driveRoutePlanType = 0;
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultdrive.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
                if (isCheckedBike) {
                    BikingRouteOverlay overlay = new BikingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultbike.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }

                if (isCheckedWalk) {
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultwalk.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
        });
        mLayout_tab1 = findViewById(R.id.route_1);
        mLayout_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_tab0.setSelected(false);
                mLayout_tab1.setSelected(true);
                mLayout_tab2.setSelected(false);
                routeOverlay.removeFromMap();

                if (isCheckedCar) {
                    driveRoutePlanType = 1;
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultdrive.getRouteLines().get(1));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }

                if (isCheckedBike) {
                    BikingRouteOverlay overlay = new BikingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultbike.getRouteLines().get(1));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
                if (isCheckedWalk) {
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultwalk.getRouteLines().get(1));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
        });
        mLayout_tab2 = findViewById(R.id.route_2);
        mLayout_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_tab0.setSelected(false);
                mLayout_tab1.setSelected(false);
                mLayout_tab2.setSelected(true);
                routeOverlay.removeFromMap();

                if (isCheckedCar) {
                    driveRoutePlanType = 2;
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultdrive.getRouteLines().get(2));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }

                if (isCheckedBike) {
                    BikingRouteOverlay overlay = new BikingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultbike.getRouteLines().get(2));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }

                if (isCheckedWalk) {
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaidumap);
                    routeOverlay = overlay;
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(nowResultwalk.getRouteLines().get(2));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
        });

        mLayout_tab0.setSelected(true);

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (startLatLng != null && endLatLng != null) {
            /*构造导航起终点参数对象*/
            BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
            bikeStartNode.setLocation(startLatLng);
            BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
            bikeEndNode.setLocation(endLatLng);
            bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

            WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
            walkStartNode.setLocation(startLatLng);
            WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
            walkEndNode.setLocation(endLatLng);
            walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

            mStartNode = new BNRoutePlanNode.Builder()
                    .latitude(startLatLng.latitude)
                    .longitude(startLatLng.longitude)
                    .coordinateType(BNRoutePlanNode.CoordinateType.BD09LL)
                    .build();
            mEndNode = new BNRoutePlanNode.Builder()
                    .latitude(endLatLng.latitude)
                    .longitude(endLatLng.longitude)
                    .coordinateType(BNRoutePlanNode.CoordinateType.BD09LL)
                    .build();
            searchButtonProcess();
        }
        ProgressDialogUtils.showDialog(RoutePlanActivity.this, "正在加载中...");
    }

    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess() {
        // 重置浏览节点的路线数据
        route = null;
        mBaidumap.clear();
        // 处理搜索按钮响应
        PlanNode stNode = PlanNode.withLocation(startLatLng);
        PlanNode enNode = PlanNode.withLocation(endLatLng);

        // 实际使用中请对起点终点城市进行正确的设定
//        mSearch.drivingSearch((new DrivingRoutePlanOption())
//                .from(stNode).to(enNode).policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST)
//                .trafficPolicy(DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC));
        ProgressDialogUtils.showDialog(RoutePlanActivity.this, "正在加载中...");
        if (isCheckedCar) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
        if (isCheckedBike) {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
        if (isCheckedWalk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }

    /**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    public void onClick(View v) {
        showChoiceMap();
    }


    private void showChoiceMap() {
        View bottomView = View.inflate(RoutePlanActivity.this, R.layout.layout_bottom_choice_map_dialog, null);
        TextView mBaidu = (TextView) bottomView.findViewById(R.id.tv_baidu);
        TextView mGaode = (TextView) bottomView.findViewById(R.id.tv_gaode);
        TextView mZidai = (TextView) bottomView.findViewById(R.id.tv_zidai);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        final PopupWindow pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int vId = view.getId();
                if (R.id.tv_baidu == vId) {
                    if (isCheckedCar) {
                        if (MapUtil.isBaiduMapInstalled()) {
                            MapUtil.openBaiDuDrinveNavi(RoutePlanActivity.this, startLatLng.latitude,
                                    startLatLng.longitude, startAddress,
                                    endLatLng.latitude, endLatLng.longitude, endAddress);
                        } else {
                            Toast.makeText(RoutePlanActivity.this, "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isCheckedBike) {
                        if (MapUtil.isBaiduMapInstalled()) {
                            MapUtil.openBaiDuBikeNavi(RoutePlanActivity.this, startLatLng.latitude,
                                    startLatLng.longitude, startAddress,
                                    endLatLng.latitude, endLatLng.longitude, endAddress);
                        } else {
                            Toast.makeText(RoutePlanActivity.this, "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isCheckedWalk) {
                        if (MapUtil.isBaiduMapInstalled()) {
                            MapUtil.openBaiDuWikeNavi(RoutePlanActivity.this, startLatLng.latitude,
                                    startLatLng.longitude, startAddress,
                                    endLatLng.latitude, endLatLng.longitude, endAddress);
                        } else {
                            Toast.makeText(RoutePlanActivity.this, "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (R.id.tv_gaode == vId) {
                    if (MapUtil.isGdMapInstalled()) {
                        MapUtil.openGaoDeNavi(RoutePlanActivity.this, startLatLng.latitude,
                                startLatLng.longitude, startAddress,
                                endLatLng.latitude, endLatLng.longitude, endAddress);
                    } else {
                        Toast.makeText(RoutePlanActivity.this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
                    }
                } else if (R.id.tv_zidai == vId) {
                    if (isCheckedCar) {
                        Log.e(TAG, "onClick: " + driveRoutePlanType);
                        NavigationUtil navigationUtil = new NavigationUtil(RoutePlanActivity.this, mStartNode, mEndNode);
                        navigationUtil.routePlanToNavi(driveRoutePlanType);
                    }
                    if (isCheckedBike) {
                        startBikeNavi();
                    }
                    if (isCheckedWalk) {
                        walkParam.extraNaviMode(0);
                        startWalkNavi();
                    }
                } else if (R.id.tv_cancel == vId) {
                    if (pop != null && pop.isShowing()) {
                        pop.dismiss();
                    }
                } else {
                }

                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
            }
        };

        mBaidu.setOnClickListener(clickListener);
        mGaode.setOnClickListener(clickListener);
        mZidai.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        ProgressDialogUtils.dismissDialog();
        if (null == result) {
            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            AlertDialog.Builder builder = new AlertDialog.Builder(RoutePlanActivity.this);
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }

        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            showRouteLineLayout.setVisibility(View.GONE);
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            if (result.getRouteLines().size() > 0) {
                nowResultwalk = result;
                mStartNavi.setVisibility(View.VISIBLE);
                showRouteLineLayout.setVisibility(View.VISIBLE);
                List<WalkingRouteLine> walkingRouteLines = result.getRouteLines();

                if (walkingRouteLines.size() > 0 && walkingRouteLines.get(0) != null) {
                    mLayout_tab0.setVisibility(View.VISIBLE);
                    initWalkRouteLineTabView(mLayout_tab0, 0, walkingRouteLines.get(0));
                }

                if (walkingRouteLines.size() > 1 && walkingRouteLines.get(1) != null) {
                    mLayout_tab1.setVisibility(View.VISIBLE);
                    initWalkRouteLineTabView(mLayout_tab1, 1, walkingRouteLines.get(1));
                } else {
                    mLayout_tab1.setVisibility(View.GONE);
                }

                if (walkingRouteLines.size() > 2 && walkingRouteLines.get(2) != null) {
                    mLayout_tab2.setVisibility(View.VISIBLE);
                    initWalkRouteLineTabView(mLayout_tab2, 2, walkingRouteLines.get(2));
                } else {
                    mLayout_tab2.setVisibility(View.GONE);
                }

                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                showRouteLineLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        ProgressDialogUtils.dismissDialog();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showRouteLineLayout.setVisibility(View.GONE);
            ToastUtils.toastShortMessage("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            Log.e("onGetDrivingRouteResult", "onGetDrivingRouteResult: 23444444444");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (result.getRouteLines().size() > 0) {
                nowResultdrive = result;
                mStartNavi.setVisibility(View.VISIBLE);
                showRouteLineLayout.setVisibility(View.VISIBLE);
                List<DrivingRouteLine> drivingRouteLines = result.getRouteLines();
                Collections.sort(drivingRouteLines, new Comparator<DrivingRouteLine>() {
                    /*
                     * int compare(Person p1, Person p2) 返回一个基本类型的整型，
                     * 返回负数表示：p1 小于p2，
                     * 返回0 表示：p1和p2相等，
                     * 返回正数表示：p1大于p2
                     */
                    public int compare(DrivingRouteLine p1, DrivingRouteLine p2) {
                        //按照Person的年龄进行升序排列
                        if (p1.getDuration() > p2.getDuration()) {
                            return 1;
                        }
                        if (p1.getDuration() == p2.getDuration()) {
                            return 0;
                        }
                        return -1;
                    }
                });

//                Log.e(TAG, "onGetDrivingRouteResult: " + drivingRouteLines.size());
//                Log.e(TAG, "onGetDrivingRouteResult1: " + drivingRouteLines.get(0).describeContents());
//                Log.e(TAG, "onGetDrivingRouteResult1: " + drivingRouteLines.get(0).getDistance());
//                Log.e(TAG, "onGetDrivingRouteResult1: " + drivingRouteLines.get(0).getDuration());
//                Log.e(TAG, "onGetDrivingRouteResult2: " + drivingRouteLines.get(1).describeContents());
//                Log.e(TAG, "onGetDrivingRouteResult2: " + drivingRouteLines.get(1).getDistance());
//                Log.e(TAG, "onGetDrivingRouteResult2: " + drivingRouteLines.get(1).getDuration());
//                Log.e(TAG, "onGetDrivingRouteResult3: " + drivingRouteLines.get(2).describeContents());
//                Log.e(TAG, "onGetDrivingRouteResult3: " + drivingRouteLines.get(2).getDistance());
//                Log.e(TAG, "onGetDrivingRouteResult3: " + drivingRouteLines.get(2).getDuration());

                if (drivingRouteLines.size() > 0 && drivingRouteLines.get(0) != null) {
                    mLayout_tab0.setVisibility(View.VISIBLE);
                    initDrivingRouteLineTabView(mLayout_tab0, 0, drivingRouteLines.get(0));
                }

                if (drivingRouteLines.size() > 1 && drivingRouteLines.get(1) != null) {
                    mLayout_tab1.setVisibility(View.VISIBLE);
                    initDrivingRouteLineTabView(mLayout_tab1, 1, drivingRouteLines.get(1));
                } else {
                    mLayout_tab1.setVisibility(View.GONE);
                }

                if (drivingRouteLines.size() > 2 && drivingRouteLines.get(2) != null) {
                    mLayout_tab2.setVisibility(View.VISIBLE);
                    initDrivingRouteLineTabView(mLayout_tab2, 2, drivingRouteLines.get(2));
                } else {
                    mLayout_tab2.setVisibility(View.GONE);
                }

                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                showRouteLineLayout.setVisibility(View.GONE);
                Log.d("route result", "结果数<0");
                return;
            }
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {
        ProgressDialogUtils.dismissDialog();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showRouteLineLayout.setVisibility(View.GONE);
            ToastUtils.toastShortMessage("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            AlertDialog.Builder builder = new AlertDialog.Builder(RoutePlanActivity.this);
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (result.getRouteLines().size() > 0) {
                nowResultbike = result;
                mStartNavi.setVisibility(View.VISIBLE);
                showRouteLineLayout.setVisibility(View.VISIBLE);
                List<BikingRouteLine> bikingRouteLines = result.getRouteLines();

                if (bikingRouteLines.size() > 0 && bikingRouteLines.get(0) != null) {
                    mLayout_tab0.setVisibility(View.VISIBLE);
                    initBikeRouteLineTabView(mLayout_tab0, 0, bikingRouteLines.get(0));
                }

                if (bikingRouteLines.size() > 1 && bikingRouteLines.get(1) != null) {
                    mLayout_tab1.setVisibility(View.VISIBLE);
                    initBikeRouteLineTabView(mLayout_tab1, 1, bikingRouteLines.get(1));
                } else {
                    mLayout_tab1.setVisibility(View.GONE);
                }

                if (bikingRouteLines.size() > 2 && bikingRouteLines.get(2) != null) {
                    mLayout_tab2.setVisibility(View.VISIBLE);
                    initBikeRouteLineTabView(mLayout_tab2, 2, bikingRouteLines.get(2));
                } else {
                    mLayout_tab2.setVisibility(View.GONE);
                }

                BikingRouteOverlay overlay = new BikingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                showRouteLineLayout.setVisibility(View.GONE);
                Log.d("route result", "结果数<0");
                return;
            }
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }


    private void initDrivingRouteLineTabView(LinearLayout layout_tab, int i, DrivingRouteLine drivingRouteLine) {
        TextView prefer = layout_tab.findViewById(R.id.prefer);
        prefer.setText("路线" + (i + 1));
        TextView mDringTime = layout_tab.findViewById(R.id.time);
        int time = drivingRouteLine.getDuration();
        if (time / 3600 == 0) {
            mDringTime.setText(+time / 60 + "分钟");
        } else {
            mDringTime.setText(time / 3600 + "小时" + (time % 3600) / 60 + "分钟");
        }
        TextView mDringDistance = layout_tab.findViewById(R.id.distance);
        int distance = drivingRouteLine.getDistance();
        if (distance > 1000) {
            distance = Math.round(distance / 100) / 10;
            mDringDistance.setText(distance + "公里");
        } else {
            mDringDistance.setText(distance + "米");
        }
        TextView mDringLightnum = layout_tab.findViewById(R.id.traffic_light);
        mDringLightnum.setText(drivingRouteLine.getLightNum() + "个红绿灯");
    }

    private void initBikeRouteLineTabView(LinearLayout layout_tab, int i, BikingRouteLine bikingRouteLine) {
        TextView prefer = layout_tab.findViewById(R.id.prefer);
        prefer.setText("路线" + (i + 1));
        TextView mDringTime = layout_tab.findViewById(R.id.time);
        int time = bikingRouteLine.getDuration();
        if (time / 3600 == 0) {
            mDringTime.setText(+time / 60 + "分钟");
        } else {
            mDringTime.setText(time / 3600 + "小时" + (time % 3600) / 60 + "分钟");
        }

        TextView mDringDistance = layout_tab.findViewById(R.id.distance);
        int distance = bikingRouteLine.getDistance();
        if (distance > 1000) {
            distance = Math.round(distance / 100) / 10;
            mDringDistance.setText(distance + "公里");
        } else {
            mDringDistance.setText(distance + "米");
        }
        TextView mDringLightnum = layout_tab.findViewById(R.id.traffic_light);
        mDringLightnum.setVisibility(View.GONE);
    }

    private void initWalkRouteLineTabView(LinearLayout layout_tab, int i, WalkingRouteLine walkingRouteLine) {
        TextView prefer = layout_tab.findViewById(R.id.prefer);
        prefer.setText("路线" + (i + 1));
        TextView mDringTime = layout_tab.findViewById(R.id.time);
        int time = walkingRouteLine.getDuration();
        if (time / 3600 == 0) {
            mDringTime.setText(+time / 60 + "分钟");
        } else {
            mDringTime.setText(time / 3600 + "小时" + (time % 3600) / 60 + "分钟");
        }

        TextView mDringDistance = layout_tab.findViewById(R.id.distance);
        int distance = walkingRouteLine.getDistance();
        if (distance > 1000) {
            distance = Math.round(distance / 100) / 10;
            mDringDistance.setText(distance + "公里");
        } else {
            mDringDistance.setText(distance + "米");
        }
        TextView mDringLightnum = layout_tab.findViewById(R.id.traffic_light);
        mDringLightnum.setVisibility(View.GONE);
    }

    /**
     * 开始骑行导航
     */
    private void startBikeNavi() {
        Log.d(TAG, "startBikeNavi");
        try {
            BikeNavigateHelper.getInstance().initNaviEngine(this, new IBEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "BikeNavi engineInitSuccess");
                    routePlanWithBikeParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "BikeNavi engineInitFail");
                    BikeNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 开始步行导航
     */
    private void startWalkNavi() {
        Log.d(TAG, "startWalkNavi");
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "WalkNavi engineInitSuccess");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "WalkNavi engineInitFail");
                    WalkNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 发起骑行导航算路
     */
    private void routePlanWithBikeParam() {
        BikeNavigateHelper.getInstance().routePlanWithRouteNode(bikeParam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "BikeNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d(TAG, "BikeNavi onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(RoutePlanActivity.this, BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d(TAG, "BikeNavi onRoutePlanFail");
            }

        });
    }

    /**
     * 发起步行导航算路
     */
    private void routePlanWithWalkParam() {
        WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "WalkNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {

                Log.d(TAG, "onRoutePlanSuccess");

                Intent intent = new Intent();
                intent.setClass(RoutePlanActivity.this, WNaviGuideActivity.class);
                startActivity(intent);

            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail");
            }

        });
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        super.onDestroy();
    }


}
