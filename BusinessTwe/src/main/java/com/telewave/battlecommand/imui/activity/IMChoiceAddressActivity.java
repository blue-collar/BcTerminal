package com.telewave.battlecommand.imui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.activity.RoutePlanSearchAddressActivity;
import com.telewave.battlecommand.adapter.LocationAdapter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * IM选择位置
 *
 * @author liwh
 * @date 2019/1/10
 */
public class IMChoiceAddressActivity extends BaseActivity {

    public static final int GET_SEARCH_ADDRESS = 0x1001;
    private MapView mMapView = null;
    public LocationClient mLocationClient = null;
    private BaiduMap mBaiduMap;
    private LocationClientOption mOption;
    // 是否首次定位
    boolean isFirstLoc = true;
    private ListView lv_view;
    private LocationAdapter mAdapper;
    private ImageView qiandaoAddressSearch;
    private TextView qiandaoAddressSure;
    private PermissionHelper permissionHelper;

    private Marker mCurrentMarker = null;

    private PoiInfo poiInfo = null;
    private PoiSearch mPoiSearch = null;

    private LatLng currentLatLng = null;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_plan_choice_address);
        //权限申请帮助类
        permissionHelper = new PermissionHelper(this);
        initView();
        initMap();
        initMyLocation();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        lv_view = (ListView) findViewById(R.id.lv_view);
        qiandaoAddressSearch = (ImageView) findViewById(R.id.qiandao_address_search);
        qiandaoAddressSure = (TextView) findViewById(R.id.qiandao_address_sure);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        qiandaoAddressSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poiInfo != null) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("poiInfo", poiInfo);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();//传值结束
                } else {
                    ToastUtils.toastShortMessage("您还没选定地点");
                }
            }
        });
        qiandaoAddressSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IMChoiceAddressActivity.this, RoutePlanSearchAddressActivity.class);
                intent.putExtra("currentLatLng", currentLatLng);
                startActivityForResult(intent, GET_SEARCH_ADDRESS);
            }
        });
    }

    private void initMap() {
        if (mBaiduMap == null) {
            mBaiduMap = mMapView.getMap();
        }
        setMapStatus();
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.refreshDrawableState();
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
    }

    private void setMapStatus() {
        UiSettings settings = mBaiduMap.getUiSettings();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        mMapView.setLayoutParams(param);
        //开放一切手势功能
        settings.setAllGesturesEnabled(true);
        //隐藏地图上的比例尺
        mMapView.showScaleControl(false);
        //显示地图上的缩放控件
        mMapView.showZoomControls(true);
    }

    private void initMyLocation() {
        permissionHelper.requestPermissions("需要定位权限", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permissions) {
                if (mLocationClient == null) {
                    mLocationClient = new LocationClient(IMChoiceAddressActivity.this);
                    mLocationClient.registerLocationListener(mBDLocationListener);
                    mLocationClient.setLocOption(getDefaultLocationClientOption());
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


    public LocationClientOption getDefaultLocationClientOption() {
        mOption = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setCoorType("bd09ll");
        //mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        //可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        mOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        mOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setLocationNotify(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.SetIgnoreCacheException(false);

        return mOption;
    }

    // POI搜索监听器
    private OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        List<PoiInfo> mInfoList = new ArrayList();

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                Toast.makeText(IMChoiceAddressActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                List<PoiInfo> infos = poiResult.getAllPoi();
                // 将周边信息加入表
                if (infos != null) {
                    mInfoList.clear();
                    mInfoList.addAll(infos);
                }
                //给poiInfo赋值第一条，因为第一条默认选中，防止不选，点击确定按钮程序崩溃
                poiInfo = (PoiInfo) mInfoList.get(0);
                //地图标注
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
                OverlayOptions options = new MarkerOptions().position(poiInfo.location).icon(bitmapDescriptor);
                mCurrentMarker = (Marker) mBaiduMap.addOverlay(options);
                //设置地图中心点以及缩放级别
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(poiInfo.location, 18);
                mBaiduMap.animateMapStatus(u);
                currentLatLng = poiInfo.location;
                // 通知适配数据已改变
                mAdapper = new LocationAdapter(IMChoiceAddressActivity.this, mInfoList);
                mAdapper.setOnSelectedListener(new LocationAdapter.OnSelectedListener() {
                    @Override
                    public void onItemSelected(int position) {
                        poiInfo = (PoiInfo) mAdapper.getItem(position);
                        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(poiInfo.location);
                        mBaiduMap.animateMapStatus(mapStatusUpdate);
                        mCurrentMarker.setPosition(poiInfo.location);
                        currentLatLng = poiInfo.location;
                        mAdapper.notifyDataSetChanged();
                    }
                });
                lv_view.setAdapter(mAdapper);
            }
        }


        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }
    };

    private BDLocationListener mBDLocationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                PoiNearbySearchOption option = new PoiNearbySearchOption();
                option.keyword(location.getAddress().address);
                option.sortType(PoiSortType.distance_from_near_to_far);
                option.location(locationLatLng);
                option.radius(500);
                //设置每页容量，默认为每页100条
                option.pageCapacity(100);
                mPoiSearch.searchNearby(option);

            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_SEARCH_ADDRESS) {
                Bundle bundle = data.getExtras();
                PoiInfo poiInfoTemp = bundle.getParcelable("poiInfo");
                //从QianDaoAddressSearchActivity选择某一项回来，移动地图到相应的位置
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(poiInfoTemp.location);
                mBaiduMap.setMapStatus(mapStatusUpdate);
                mCurrentMarker.setPosition(poiInfoTemp.location);
                currentLatLng = poiInfoTemp.location;

                //更新适配器数据
                List<PoiInfo> mInfoList = new ArrayList();
                mInfoList.clear();
                mInfoList.add(poiInfoTemp);
                //同时 也给poiInfo赋值第一条，因为第一条默认选中，防止不选，点击确定按钮程序崩溃
                poiInfo = (PoiInfo) mInfoList.get(0);

                //通知适配数据已改变
                mAdapper = new LocationAdapter(IMChoiceAddressActivity.this, mInfoList);
                mAdapper.setOnSelectedListener(new LocationAdapter.OnSelectedListener() {
                    @Override
                    public void onItemSelected(int position) {
                        poiInfo = (PoiInfo) mAdapper.getItem(position);
                        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(poiInfo.location);
                        mBaiduMap.animateMapStatus(mapStatusUpdate);
                        mCurrentMarker.setPosition(poiInfo.location);
                        currentLatLng = poiInfo.location;
                        mAdapper.notifyDataSetChanged();
                    }
                });
                lv_view.setAdapter(mAdapper);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
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
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
            mLocationClient = null;
        }
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
            mPoiSearch = null;
        }
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}

