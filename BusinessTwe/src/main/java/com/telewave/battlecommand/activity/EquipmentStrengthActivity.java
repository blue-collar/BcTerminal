package com.telewave.battlecommand.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.TreeListViewAdapter;
import com.telewave.battlecommand.bean.Deviceinfo;
import com.telewave.battlecommand.bean.SearchDeviceReq;
import com.telewave.battlecommand.bean.TreeData;
import com.telewave.battlecommand.treelist.adapter.SimpleTreeAdapter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.BitmapUtils;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.Tips;
import com.telewave.lib.widget.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class EquipmentStrengthActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_select;
    protected List<Node> mDatas = new ArrayList<Node>();
    private PermissionHelper permissionHelper;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private double myLatitude;
    private double myLongitude;

    MapView mMapView;
    BaiduMap mBaiduMap;

    private LinearLayout esSearchLayout;
    private ImageView searchShowIv;
    private ImageView esLocationIv;
    private LinearLayout startSearchLayout;
    private TextView tv_team;
    private ListView lv_select;
    private TreeListViewAdapter mAdapter;
    private List<TreeData> treeData = new ArrayList<>();
    private EditText et_device_name;
    private String mId;

    @Override
    public void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_equipment_strength);
        //权限申请帮助类
        permissionHelper = new PermissionHelper(this);
        initView();
//        initMyLocation();
        initDatas();
    }

    private void initView() {
        ll_select = findViewById(R.id.ll_select);
        tv_team = findViewById(R.id.tv_team);
        tv_team.setOnClickListener(this);
        esSearchLayout = (LinearLayout) findViewById(R.id.es_search_layout);
        searchShowIv = (ImageView) findViewById(R.id.search_show_iv);
        esLocationIv = (ImageView) findViewById(R.id.es_location_iv);
        startSearchLayout = (LinearLayout) findViewById(R.id.start_search_layout);
        mMapView = (MapView) findViewById(R.id.equipment_strength_map);
        mBaiduMap = mMapView.getMap();
        // 显示地图上比例尺
        mMapView.showScaleControl(true);
        // 不显示地图缩放控件（按钮控制栏）
        mMapView.showZoomControls(false);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15.0f).build()));

        searchShowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esSearchLayout.getVisibility() == View.VISIBLE) {
                    esSearchLayout.setVisibility(View.GONE);
                } else {
                    esSearchLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        esLocationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMyLocation();
            }
        });
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //增加map的拖动监听器
/*
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //纠偏移动地图后的位置经纬度数据
                myLatitude = mapStatus.target.latitude;
                myLongitude = mapStatus.target.longitude;
                Log.i("Micro", "myLatitude：" + myLatitude + "-----" + "myLongitude" + myLongitude);
//                LatLng ptCenter = new LatLng(myLatitude, myLongitude);
            }
        });
*/

        startSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esSearchLayout.setVisibility(View.GONE);
//                加载搜索数据
                search();
            }
        });
        et_device_name = findViewById(R.id.et_device_name);
        lv_select = findViewById(R.id.lv_select);
        lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mId = treeData.get(position).getId();
                tv_team.setText(treeData.get(position).getName());
                ll_select.setVisibility(View.GONE);
                myLatitude = treeData.get(position).getMapZ();
                myLongitude = treeData.get(position).getMapX();
                LatLng cenpt = new LatLng(myLatitude, myLongitude);
                if (cenpt != null) {
                    //定义地图状态
                    MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    //改变地图状态
                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                }
            }
        });


    }

    private List<Deviceinfo> deviceinfoList = new ArrayList<>();

    private void search() {
        Tips.showProgress(EquipmentStrengthActivity.this, "正在加载。。。");
        SearchDeviceReq searchDeviceReq = new SearchDeviceReq();
        searchDeviceReq.setGisx(myLongitude);  //(114.91859900)
        searchDeviceReq.setGisy(myLatitude);//27.83713660
        searchDeviceReq.setOrganname(tv_team.getText().toString().trim());//  "新余市消防支队渝水区大队平安路中队
        searchDeviceReq.setDistance("100");
        searchDeviceReq.setSsxfjgid(mId);// //241371a3d5874132b3dabd7925e0f9e1
        searchDeviceReq.setZbmc((et_device_name.getText().toString().trim()));//"压缩空气泡沫消防车"
        String json = JsonUtils.objectToString(searchDeviceReq);
        OkGo.<String>post(ConstData.urlManager.searchDevice)//
                .tag(this)//
                .upJson(JsonUtils.objectToString(searchDeviceReq))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Tips.dismissProgress();
                        String data = response.body();
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            JSONArray array = object.getJSONArray("data");
                            if (array != null) {
                                deviceinfoList.addAll(JsonUtils.stringToList(array.toString(), Deviceinfo.class));
                                markWzStation(deviceinfoList);
                                Log.i("", deviceinfoList.size() + "");
                            }
                        } catch (JSONException e) {
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


    private void markWzStation(List<Deviceinfo> deviceinfoList) {
        for (int i = 0; i < deviceinfoList.size(); i++) {
            Deviceinfo deviceinfo = deviceinfoList.get(i);
            if (!TextUtils.isEmpty(deviceinfo.getGisx()) && !TextUtils.isEmpty(deviceinfo.getGisy())) {
                LatLng latLng = new LatLng(Double.parseDouble(deviceinfo.getGisy()), Double.parseDouble(deviceinfo.getGisx()));

                Bitmap viewBitmap = BitmapUtils.getViewBitmap(makeDisasterPlotView(deviceinfo.getZbmc()));
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(viewBitmap);

                final MarkerOptions options = new MarkerOptions().perspective(true)
                        .position(latLng)
                        .icon(bitmapDescriptor)
                        .zIndex(9)
                        .draggable(false);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(latLng)
                        .zoom(10)
                        .build()));
                Marker marker = (Marker) mBaiduMap.addOverlay(options);
            }
        }

    }

    /**
     * 生成灾情标记view
     *
     * @param markName
     */
    private View makeDisasterPlotView(String markName) {
        View view = LayoutInflater.from(EquipmentStrengthActivity.this).inflate(R.layout.disaster_plot_layout, null);
        ImageView disasterPlotIv = (ImageView) view.findViewById(R.id.disaster_plot_iv);
        TextView disasterPlotTv = (TextView) view.findViewById(R.id.disaster_plot_text);
        disasterPlotTv.setText(markName);
        disasterPlotIv.setBackgroundResource(R.mipmap.nine_small_place_checked);
        return view;
    }

    private void initMyLocation() {
        permissionHelper.requestPermissions("需要定位权限", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permissions) {
                if (mLocClient == null) {
                    mLocClient = new LocationClient(EquipmentStrengthActivity.this.getApplicationContext());
                    mLocClient.registerLocationListener(myListener);
                    LocationClientOption options = new LocationClientOption();
                    options.setOpenGps(true);
                    options.setCoorType("bd09ll");
                    options.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                    //值为零，不会间隔自动定位
                    options.setScanSpan(0);
                    options.disableCache(true);
                    options.setIsNeedAddress(true);
                    mLocClient.setLocOption(options);
                    mLocClient.start();
                } else {
                    mLocClient.requestLocation();
                }
            }

            @Override
            public void doAfterDenied(String... permissions) {
//                permissionHelper.openPermissionsSetting("请到设置>权限中打开定位权限");
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.tv_team == vId) {
//                Intent intent = new Intent(EquipmentStrengthActivity.this, ListViewActivity.class);
//                startActivity(intent);
            if (ll_select.getVisibility() == View.GONE) {
                ll_select.setVisibility(View.VISIBLE);
            } else {
                ll_select.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation != null && mBaiduMap != null) {
                //获取自己所在位置的经纬度
                myLatitude = bdLocation.getLatitude();
                myLongitude = bdLocation.getLongitude();
                MyLocationData data = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius()).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).direction(50).build();
                mBaiduMap.setMyLocationData(data);
                LatLng ll = new LatLng(myLatitude, myLongitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.zoom(15).target(ll);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                Log.e("onReceiveLocation", "myLatitude: " + myLatitude);
                Log.e("onReceiveLocation", "myLongitude: " + myLongitude);
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
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    private void initDatas() {
        Tips.showProgress(EquipmentStrengthActivity.this, "正在加载。。。");
        OkGo.<String>post(ConstData.urlManager.getTeam)//
                .tag(this)//
                .params("officeId", SharePreferenceUtils.getDataSharedPreferences(EquipmentStrengthActivity.this, "organid"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Tips.dismissProgress();
                        String data = response.body();
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            JSONArray array = object.getJSONArray("data");

                            if (array != null) {
                                treeData.addAll(JsonUtils.stringToList(array.toString(), TreeData.class));
                                initNode();
                            }
                        } catch (JSONException e) {
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

    private void initNode() {
        tv_team.setText(treeData.get(0).getName());
        mId = treeData.get(0).getId();
        myLatitude = treeData.get(0).getMapZ();
        myLongitude = treeData.get(0).getMapX();
        LatLng cenpt = new LatLng(myLatitude, myLongitude);
        if (cenpt != null) {
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }


        if (treeData.size() == 1) {
            tv_team.setEnabled(false);
            return;
        }
        for (int i = 0; i < treeData.size(); i++) {
            mDatas.add(new Node(treeData.get(i).getId(), treeData.get(i).getParentId(), treeData.get(i).getName()));
        }

        mAdapter = new SimpleTreeAdapter(lv_select, EquipmentStrengthActivity.this,
                mDatas, 1, R.mipmap.head_icon, R.mipmap.head_icon);
        lv_select.setAdapter(mAdapter);
    }

}
