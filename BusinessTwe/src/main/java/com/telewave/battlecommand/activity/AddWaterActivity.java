package com.telewave.battlecommand.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.telewave.battlecommand.bean.Water;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.Tips;
import com.telewave.lib.map.util.PositionUtils;
import com.telewave.lib.widget.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddWaterActivity extends BaseActivity implements OnGetGeoCoderResultListener, View.OnClickListener {
    private PermissionHelper permissionHelper;
    private LocationClient mLocationClient;
    private MyLocationListener mylocationListener = new MyLocationListener();
    private double myLatitude;
    private double myLongitude;
    private EditText et_quality, et_name, et_road;
    private TextView tv_address, tv_commit, tv_title;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor iconDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
    private GeoCoder mSearch = null;
    private Spinner sp_nature, sp_status, sp_type, sp_method;
    private MyAdapter adapterNature, adapterStatus, adapterType, adapterMethod;
    private List<WaterData> arrayNature, arrayStatus, arrayType, arrayMethod;
    private Water water;
    private String nature, status, type, method;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_water);
        initViews();

        initGeoCoderSearch();
        if (getIntent().getExtras() != null) {
            String data = (String) getIntent().getExtras().get("water");
            water = (Water) JsonUtils.stringToObject(data, Water.class);
            if (water != null) {
                tv_title.setText("水源编辑");
                setData();
            }
        } else {
            initMyLocation();
        }


    }

    private void setData() {
        et_quality.setText(water.getSyzt());
        et_name.setText(water.getSymc());
        tv_address.setText(water.getSydz());
        et_road.setText(water.getSsldmc());
        LatLng latLng = PositionUtils.Gps84_To_bd09(water.getGisY(), water.getGisX());
        myLatitude = latLng.latitude;
        myLongitude = latLng.longitude;
        LatLng cenpt = new LatLng(myLatitude, myLongitude);
        if (cenpt != null) {
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }


        for (int i = 0; i < arrayStatus.size(); i++) {
            if (water.getKyzt().equals(arrayStatus.get(i).code)) {
                sp_status.setSelection(i);
                status = arrayStatus.get(i).code;
            }
        }
        for (int i = 0; i < arrayNature.size(); i++) {
            if (water.getXz().equals(arrayNature.get(i).code)) {
                sp_nature.setSelection(i);
                nature = arrayNature.get(i).code;
            }
        }

        for (int i = 0; i < arrayType.size(); i++) {
            if (water.getSylx().equals(arrayType.get(i).code)) {
                sp_type.setSelection(i);
                type = arrayType.get(i).code;
            }
        }

        for (int i = 0; i < arrayMethod.size(); i++) {
            if (water.getQsxs().equals(arrayMethod.get(i).code)) {
                sp_method.setSelection(i);
                method = arrayMethod.get(i).code;
            }
        }

    }

    private void initViews() {
        water = new Water();
        arrayMethod = new ArrayList<>();
        WaterData dataMethod2 = new WaterData("03", "自拉水");
        WaterData dataMethod4 = new WaterData("99", "其他");
        WaterData dataMethod3 = new WaterData("02", "自抽水");
        WaterData dataMethod1 = new WaterData("01", "市政供水");
        arrayMethod.add(dataMethod1);
        arrayMethod.add(dataMethod2);
        arrayMethod.add(dataMethod3);
        arrayMethod.add(dataMethod4);


        arrayNature = new ArrayList<>();
        WaterData dataNature1 = new WaterData("1112", "市政小区");
        WaterData dataNature2 = new WaterData("1111", "市政道路");
        WaterData dataNature3 = new WaterData("1122", "单位室内");
        WaterData dataNature4 = new WaterData("1121", "单位室外");
        arrayNature.add(dataNature1);
        arrayNature.add(dataNature2);
        arrayNature.add(dataNature3);
        arrayNature.add(dataNature4);

        arrayStatus = new ArrayList<>();
        WaterData dataStatus1 = new WaterData("1", "可用");
        WaterData dataStatus2 = new WaterData("0", "不可用");
        arrayStatus.add(dataStatus1);
        arrayStatus.add(dataStatus2);

        arrayType = new ArrayList<>();
        WaterData dataType1 = new WaterData("1110", "消火栓");
        WaterData dataType2 = new WaterData("1200", "消防水鹤");
        WaterData dataType3 = new WaterData("1300", "消防水池");
        WaterData dataType4 = new WaterData("2100", "消防取水码头");
        arrayType.add(dataType1);
        arrayType.add(dataType2);
        arrayType.add(dataType3);
        arrayType.add(dataType4);
        nature = arrayNature.get(0).code;
        type = arrayType.get(0).code;
        status = arrayStatus.get(0).code;
        method = arrayMethod.get(0).code;
        sp_method = findViewById(R.id.sp_method);
        adapterMethod = new MyAdapter(arrayMethod);
        sp_method.setAdapter(adapterMethod);
        sp_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                deviceSn = devices.get(position).deviceSn;
                method = arrayMethod.get(position).code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_nature = findViewById(R.id.sp_nature);
        adapterNature = new MyAdapter(arrayNature);
        //设置spinner中每个 item 的样式
//        adapterNature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp_nature.setAdapter(adapterNature);
        sp_nature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                deviceSn = devices.get(position).deviceSn;
                nature = arrayNature.get(position).code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_status = findViewById(R.id.sp_status);
        adapterStatus = new MyAdapter(arrayStatus);
        //设置spinner中每个 item 的样式
        // adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp_status.setAdapter(adapterStatus);
        sp_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                deviceSn = devices.get(position).deviceSn;
                status = arrayStatus.get(position).code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_type = findViewById(R.id.sp_type);

        adapterType = new MyAdapter(arrayType);
        //设置spinner中每个 item 的样式
        // adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp_type.setAdapter(adapterType);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                deviceSn = devices.get(position).deviceSn;
                type = arrayType.get(position).code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tv_title = findViewById(R.id.tv_title);
        et_quality = findViewById(R.id.et_quality);
        et_name = findViewById(R.id.et_name);
        et_road = findViewById(R.id.et_road);
        tv_address = findViewById(R.id.tv_address);
        mapView = findViewById(R.id.bmapView);
        // 不显示地图上比例尺
        mapView.showScaleControl(false);
        // 显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(true);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15.0f).build()));
        mBaiduMap.setMyLocationEnabled(true);


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
                // 反Geo搜索
                LatLng ptCenter = new LatLng(myLatitude, myLongitude);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
                Log.i("Micro", "myLatitude：" + myLatitude + "-----" + "myLongitude" + myLongitude);

            }
        });

        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化GeoCoder模块
     */
    private void initGeoCoderSearch() {
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        tv_address.setText(result.getAddress());
    }

    private void initMyLocation() {
        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestPermissions("需要定位权限", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permissions) {
                if (mLocationClient == null) {
                    mLocationClient = new LocationClient(getApplicationContext());
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

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.tv_commit == vId) {
            //提交
            water.setSymc(et_name.getText().toString().trim());
            water.setSydz(tv_address.getText().toString().trim());
            water.setQsxs(method);
            water.setKyzt(status);
            water.setSylx(type);
            water.setXz(nature);
            water.setSyzt(et_quality.getText().toString().trim());
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(myLatitude, myLongitude);
            if (gps != null) {
                water.setGisX(gps.getWgLon());
                water.setGisY(gps.getWgLat());
            }
            water.setSsldmc(et_road.getText().toString().trim());
            water.setCreateId(SharePreferenceUtils.getDataSharedPreferences(this, "userid"));

            commit();
        } else {
        }
    }

    private void commit() {
        Tips.showProgress(this, "正在请求，请稍后。。。");
        String json = JsonUtils.objectToString(water);
        OkGo.<String>post(ConstData.urlManager.addWater)
                .tag(this)
                .upJson(JsonUtils.objectToString(water))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Tips.dismissProgress();
                        String data = response.body();
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            if (code == 1) {
                                Tips.showToast(AddWaterActivity.this, object.getString("msg"));
                                finish();
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

    /**
     * 定位监听者
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null) {
                if (bdLocation.getCity() != null) {
                    //获取自己所在位置的经纬度
                    myLatitude = bdLocation.getLatitude();
                    myLongitude = bdLocation.getLongitude();
                    String currentAddress = bdLocation.getAddrStr();
                    tv_address.setText(currentAddress);
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
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        private List<WaterData> list;

        public MyAdapter(List<WaterData> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AddWaterActivity.this).inflate(R.layout.item_water_data_list, null);
            TextView textView = convertView.findViewById(R.id.text1);
            textView.setText(list.get(position).name);
            return convertView;
        }
    }

    class WaterData {
        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public WaterData(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }

}
