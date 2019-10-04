package com.telewave.battlecommand.fragment;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.telewave.battlecommand.ResidentNotificationHelper;
import com.telewave.battlecommand.activity.DisasterDetailActivity;
import com.telewave.battlecommand.activity.MainActivity;
import com.telewave.battlecommand.activity.RouteAddressActivity;
import com.telewave.battlecommand.bean.EmergencyUnitItem;
import com.telewave.battlecommand.bean.FireOrganItem;
import com.telewave.battlecommand.bean.HighBuildItem;
import com.telewave.battlecommand.bean.HydrantItem;
import com.telewave.battlecommand.bean.ImportantUnitItem;
import com.telewave.battlecommand.bean.LogisticUnitItem;
import com.telewave.battlecommand.bean.MicroStationItem;
import com.telewave.battlecommand.bean.NineSmallPlaceItem;
import com.telewave.battlecommand.bean.ResourceEmergencyUnit;
import com.telewave.battlecommand.bean.ResourceFireOrgan;
import com.telewave.battlecommand.bean.ResourceHighBuild;
import com.telewave.battlecommand.bean.ResourceHydrant;
import com.telewave.battlecommand.bean.ResourceImportantUnit;
import com.telewave.battlecommand.bean.ResourceLogisticUnit;
import com.telewave.battlecommand.bean.ResourceMicroStation;
import com.telewave.battlecommand.bean.ResourceNineSmallPlace;
import com.telewave.battlecommand.clusterutil.clustering.Cluster;
import com.telewave.battlecommand.clusterutil.clustering.ClusterManager;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.contract.Listener;
import com.telewave.battlecommand.contract.ListenerManager;
import com.telewave.battlecommand.mqtt.MqttListener.MqttListener;
import com.telewave.battlecommand.mqtt.MqttListener.MqttListenerManager;
import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.MessageType;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;
import com.telewave.battlecommand.presenter.ResourcesPresenter;
import com.telewave.battlecommand.sunmi.AidlUtil;
import com.telewave.battlecommand.view.CheckableImageButton;
import com.telewave.battlecommand.view.ClearAutoCompleteTextView;
import com.telewave.battlecommand.view.MultiLineRadioGroup;
import com.telewave.business.twe.R;
import com.telewave.control.api.ControlAPI;
import com.telewave.control.serialport.SerialPortInstance;
import com.telewave.control.serialport.SerialPortOpt;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.BitmapUtils;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.map.util.LocationLevelUtils;
import com.telewave.lib.map.util.PositionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 联动处置
 *
 * @author liwh
 * @date 2018/12/12
 */
public class RescueDisposalFragment extends Fragment implements IDirectionContract.IDirectionView,
        OnGetSuggestionResultListener, Listener.RescueDisposalReLoadPositionListener,
        MqttListener.NewDisasterInfoListener, MqttListener.ReceiveCallPoliceInfoListener {
    private static final String TAG = "RescueDisposalFragment";
    //灾情地点
    private BitmapDescriptor newDisasterDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_new_disaster);
    private BitmapDescriptor iconDescripter = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
    private View mView;
    private FrameLayout map_container;
    private CheckableImageButton fireOrganCIB, hydrantCIB, microStationCIB, roadConditionCIB;
    private LinearLayout moreTypeLayout, clearMarkerLayout, resourceScopeLayout;
    private ImageButton navigationBtn;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private List<String> suggest;
    private List<SuggestionResult.SuggestionInfo> suggestionInfoList;
    private String currentCity = "";
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private AutoCompleteTextView aetAddressInput;
    private TextView btgetSearchTv;
    private ArrayAdapter<String> sugAdapter = null;
    private LatLng enableLatLng = null;
    private String enableAddress = null;
    private Marker mCurrentMarker = null;

    //是否选中了显示水源/消防栓按钮
    private boolean isCheckedHydrant = false;
    //是否选中了显示消防队按钮
    private boolean isCheckedFireOrgan = false;
    //是否选中了显示微站按钮
    private boolean isCheckedMicroStation = false;
    //是否选中了显示九小场所按钮
    private boolean isCheckedNineSmallPlace = false;
    //是否选中了显示重点单位按钮
    private boolean isCheckedImportUnit = false;
    //是否选中了显示高层建筑按钮
    private boolean isCheckedHighBuild = false;
    //是否选中了显示联勤单位按钮
    private boolean isCheckedLogisticUnit = false;
    //是否选中了显示应急单位按钮
    private boolean isCheckedEmergencyUnit = false;
    /**
     * 默认距离
     */
    private int distance = 5;
    /**
     * 默认第一项选中
     */
    private int scopeCheckId = R.id.diameter_type_one_btn;
    private Overlay circleOverly;
    /**
     * 定位接收者
     */
    private MyLocationListener mylocationListener = new MyLocationListener();
    private PermissionHelper permissionHelper;

    private InfoWindow mInfoWindow;
    private ResourcesPresenter mResourcesPresenter;

    private ClusterManager<HydrantItem> mHydrantItemClusterManager;
    private ClusterManager<FireOrganItem> mFireOrganItemClusterManager;
    private ClusterManager<MicroStationItem> mMicroStationItemClusterManager;
    private ClusterManager<ImportantUnitItem> mImportantUnitItemClusterManager;
    private ClusterManager<EmergencyUnitItem> mEmergencyUnitItemClusterManager;
    private ClusterManager<LogisticUnitItem> mLogisticUnitItemClusterManager;
    private ClusterManager<HighBuildItem> mHighBuildItemClusterManager;
    private ClusterManager<NineSmallPlaceItem> mNineSmallPlaceItemClusterManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rescue_disposal, container, false);
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //权限申请帮助类
        permissionHelper = new PermissionHelper(this);
        mResourcesPresenter = new ResourcesPresenter(this);
        ListenerManager.getInstance().setRescueDisposalReLoadPositionListener(this);
        MqttListenerManager.getInstance().setReceiveCallPoliceInfoListener(this);
        MqttListenerManager.getInstance().setNewDisasterInfoListener(this);
        initMapView();
        initView();
        initMyLocation();
        initPoiSearch();

        // 定义消防栓/水源点聚合管理类ClusterManager
        mHydrantItemClusterManager = new ClusterManager<HydrantItem>(getActivity(), mBaiduMap);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mHydrantItemClusterManager);

        mHydrantItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<HydrantItem>() {
            @Override
            public boolean onClusterClick(Cluster<HydrantItem> cluster) {
//                if (cluster.getItems().size() > 0) {
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    for (HydrantItem hydrantItem : cluster.getItems()) {
//                        builder.include(hydrantItem.getPosition());
//                    }
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
//                            .newLatLngBounds(builder.build()));
//                }
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mHydrantItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<HydrantItem>() {
            @Override
            public boolean onClusterItemClick(HydrantItem item) {
                ResourceHydrant resourceHydrant = item.getResourceHydrant();
                showDialogOfmarker(ConstData.RESOURCE_HYDRANT_TYPE, null, resourceHydrant);
                return false;
            }
        });

        // 定义消防机构点聚合管理类ClusterManager
        mFireOrganItemClusterManager = new ClusterManager<FireOrganItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_fire_organ);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mFireOrganItemClusterManager);

        mFireOrganItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<FireOrganItem>() {
            @Override
            public boolean onClusterClick(Cluster<FireOrganItem> cluster) {
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mFireOrganItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<FireOrganItem>() {
            @Override
            public boolean onClusterItemClick(FireOrganItem item) {
                ResourceFireOrgan resourceFireOrgan = item.getResourceFireOrgan();
                showDialogOfmarker(ConstData.RESOURCE_FIRE_ORGAN_TYPE, null, resourceFireOrgan);
                return false;
            }
        });

        // 定义微站点聚合管理类ClusterManager
        mMicroStationItemClusterManager = new ClusterManager<MicroStationItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_micro_station);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mMicroStationItemClusterManager);

        mMicroStationItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MicroStationItem>() {
            @Override
            public boolean onClusterClick(Cluster<MicroStationItem> cluster) {
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mMicroStationItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MicroStationItem>() {
            @Override
            public boolean onClusterItemClick(MicroStationItem item) {
                ResourceMicroStation resourceMicroStation = item.getResourceMicroStation();
                showDialogOfmarker(ConstData.RESOURCE_MICRO_STATION_TYPE, null, resourceMicroStation);
                return false;
            }
        });

        // 定义点聚合管理类ClusterManager
        mImportantUnitItemClusterManager = new ClusterManager<ImportantUnitItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_import_unit);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mImportantUnitItemClusterManager);

        mImportantUnitItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ImportantUnitItem>() {
            @Override
            public boolean onClusterClick(Cluster<ImportantUnitItem> cluster) {
                if (cluster.getItems().size() > 0) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (ImportantUnitItem importantUnitItem : cluster.getItems()) {
                        builder.include(importantUnitItem.getPosition());
                    }
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                            .newLatLngBounds(builder.build()));
                }
                return false;
            }
        });
        mImportantUnitItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ImportantUnitItem>() {
            @Override
            public boolean onClusterItemClick(ImportantUnitItem item) {
                ResourceImportantUnit resourceImportantUnit = item.getResourceImportantUnit();
                showDialogOfmarker(ConstData.RESOURCE_IMPORTANT_UNIT_TYPE, null, resourceImportantUnit);
                return false;
            }
        });

        // 定义点聚合管理类ClusterManager
        mEmergencyUnitItemClusterManager = new ClusterManager<EmergencyUnitItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_emergency_unit);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mEmergencyUnitItemClusterManager);

        mEmergencyUnitItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<EmergencyUnitItem>() {
            @Override
            public boolean onClusterClick(Cluster<EmergencyUnitItem> cluster) {
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mEmergencyUnitItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<EmergencyUnitItem>() {
            @Override
            public boolean onClusterItemClick(EmergencyUnitItem item) {
                ResourceEmergencyUnit resourceEmergencyUnit = item.getResourceEmergencyUnit();
                showDialogOfmarker(ConstData.RESOURCE_EMERGENCY_UNIT_TYPE, null, resourceEmergencyUnit);
                return false;
            }
        });

        // 定义点聚合管理类ClusterManager
        mLogisticUnitItemClusterManager = new ClusterManager<LogisticUnitItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_logistic_unit);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mLogisticUnitItemClusterManager);

        mLogisticUnitItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<LogisticUnitItem>() {
            @Override
            public boolean onClusterClick(Cluster<LogisticUnitItem> cluster) {
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mLogisticUnitItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<LogisticUnitItem>() {
            @Override
            public boolean onClusterItemClick(LogisticUnitItem item) {
                ResourceLogisticUnit resourceLogisticUnit = item.getResourceLogisticUnit();
                showDialogOfmarker(ConstData.RESOURCE_LOGISTIC_UNIT_TYPE, null, resourceLogisticUnit);
                return false;
            }
        });

        // 定义点聚合管理类ClusterManager
        mHighBuildItemClusterManager = new ClusterManager<HighBuildItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_high_build);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mHighBuildItemClusterManager);

        mHighBuildItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<HighBuildItem>() {
            @Override
            public boolean onClusterClick(Cluster<HighBuildItem> cluster) {
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mHighBuildItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<HighBuildItem>() {
            @Override
            public boolean onClusterItemClick(HighBuildItem item) {
                ResourceHighBuild resourceHighBuild = item.getResourceHighBuild();
                showDialogOfmarker(ConstData.RESOURCE_HIGH_BUILD_TYPE, null, resourceHighBuild);
                return false;
            }
        });


        // 定义点聚合管理类ClusterManager
        mNineSmallPlaceItemClusterManager = new ClusterManager<NineSmallPlaceItem>(getActivity(), mBaiduMap, R.drawable.map_cluster_bg_nine_small_place);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mNineSmallPlaceItemClusterManager);

        mNineSmallPlaceItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<NineSmallPlaceItem>() {
            @Override
            public boolean onClusterClick(Cluster<NineSmallPlaceItem> cluster) {
                float level = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level + 1).build()));
                return false;
            }
        });
        mNineSmallPlaceItemClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<NineSmallPlaceItem>() {
            @Override
            public boolean onClusterItemClick(NineSmallPlaceItem item) {
                ResourceNineSmallPlace resourceNineSmallPlace = item.getResourceNineSmallPlace();
                showDialogOfmarker(ConstData.RESOURCE_NINE_SMALL_PLACE_TYPE, null, resourceNineSmallPlace);
                return false;
            }
        });
    }

    private void initMapView() {
        map_container = (FrameLayout) mView.findViewById(R.id.micro_station_map_container);
        BaiduMapOptions options = new BaiduMapOptions().overlookingGesturesEnabled(false).rotateGesturesEnabled(false);
        mapView = new MapView(getActivity(), options);
        mBaiduMap = mapView.getMap();
        map_container.addView(mapView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mapView.removeViewAt(1);
        mapView.showScaleControl(true);
        mapView.showZoomControls(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15.0f).build()));

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                if (bundle != null) {
                    String disasterId = bundle.getString("disasterId");
                    if (disasterId != null) {
                        Intent intent = new Intent(getActivity(), DisasterDetailActivity.class);
                        intent.putExtra("disasterId", disasterId);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mBaiduMap.hideInfoWindow();
                }
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                if (mFireOrganItemClusterManager != null && mFireOrganItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mFireOrganItemClusterManager.handlerCluster();
                }
                if (mHydrantItemClusterManager != null && mHydrantItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mHydrantItemClusterManager.handlerCluster();
                }
                if (mMicroStationItemClusterManager != null && mMicroStationItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mMicroStationItemClusterManager.handlerCluster();
                }
                if (mImportantUnitItemClusterManager != null && mImportantUnitItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mImportantUnitItemClusterManager.handlerCluster();
                }
                if (mLogisticUnitItemClusterManager != null && mLogisticUnitItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mLogisticUnitItemClusterManager.handlerCluster();
                }
                if (mEmergencyUnitItemClusterManager != null && mEmergencyUnitItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mEmergencyUnitItemClusterManager.handlerCluster();
                }
                if (mHighBuildItemClusterManager != null && mHighBuildItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mHighBuildItemClusterManager.handlerCluster();
                }
                if (mNineSmallPlaceItemClusterManager != null && mNineSmallPlaceItemClusterManager.getAlgorithm().getItems().size() > 0) {
                    mNineSmallPlaceItemClusterManager.handlerCluster();
                }
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });

        if (!TextUtils.isEmpty(ConstData.mapx) && !TextUtils.isEmpty(ConstData.mapy)) {
            LatLng cenLatLng = PositionUtils.Gps84_To_bd09(Double.parseDouble(ConstData.mapy), Double.parseDouble(ConstData.mapx));
            if (cenLatLng != null) {

                MapStatus.Builder builder = new MapStatus.Builder();
                builder.zoom(15).target(cenLatLng);
                mCurrentMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
                        .position(cenLatLng).icon(iconDescripter));
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                enableLatLng = cenLatLng;
                enableAddress = ConstData.address;
            }
        }
    }

    private void initView() {
        aetAddressInput = (ClearAutoCompleteTextView) mView.findViewById(R.id.et_address_input);
        btgetSearchTv = (TextView) mView.findViewById(R.id.btget_search_tv);
        aetAddressInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                }
                mBaiduMap.clear();
                LatLng latLng = suggestionInfoList.get(position).pt;
                if (latLng != null) {
                    enableLatLng = latLng;
                    //suggestionInfoList.get(position).address经常为空，所以选用key
//                enableAddress = suggestionInfoList.get(position).address;
                    enableAddress = suggestionInfoList.get(position).key;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.zoom(15).target(latLng);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    Bitmap viewBitmap = BitmapUtils.getViewBitmap(makeDisasterPlotView(null, enableAddress));
                    BitmapDescriptor normalDescripter = BitmapDescriptorFactory.fromBitmap(viewBitmap);
                    final MarkerOptions options = new MarkerOptions().perspective(true)
                            .position(latLng).icon(normalDescripter).zIndex(9).draggable(false);
                    mBaiduMap.addOverlay(options);
                } else {
                    ToastUtils.toastShortMessage("未找到该位置");
                }
            }
        });
        btgetSearchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                }
                String keystr = aetAddressInput.getText().toString();
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(keystr).city(currentCity));
            }
        });

        aetAddressInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    }
                    String keystr = aetAddressInput.getText().toString().trim();
                    if (TextUtils.isEmpty(keystr)) {
                        ToastUtils.toastShortMessage("请输入您想要搜索的信息");
                        return true;
                    }
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(keystr).city(currentCity));
                    return true;
                }
                return false;
            }
        });

        fireOrganCIB = (CheckableImageButton) mView.findViewById(R.id.ck_fire_organ);
        hydrantCIB = (CheckableImageButton) mView.findViewById(R.id.ck_hydrant_marker);
        microStationCIB = (CheckableImageButton) mView.findViewById(R.id.ck_micro_station);
        roadConditionCIB = (CheckableImageButton) mView.findViewById(R.id.ck_road_condition);
        moreTypeLayout = (LinearLayout) mView.findViewById(R.id.more_type_layout);
        resourceScopeLayout = (LinearLayout) mView.findViewById(R.id.resource_scope_layout);
        clearMarkerLayout = (LinearLayout) mView.findViewById(R.id.clear_marker_layout);
        navigationBtn = (ImageButton) mView.findViewById(R.id.btn_navigation);

        fireOrganCIB.setChecked(isCheckedFireOrgan);
        hydrantCIB.setChecked(isCheckedHydrant);
        microStationCIB.setChecked(isCheckedMicroStation);

        fireOrganCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedFireOrgan = isChecked;
                clearItemCluster(mFireOrganItemClusterManager);
                if (isCheckedFireOrgan) {
                    getResourceFireOrgan(distance);
                }
            }
        });
        hydrantCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedHydrant = isChecked;
                clearItemCluster(mHydrantItemClusterManager);
                if (isCheckedHydrant) {
                    getResourceHydrant(distance);
                }
            }
        });
        microStationCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedMicroStation = isChecked;
                clearItemCluster(mMicroStationItemClusterManager);
                if (isCheckedMicroStation) {
                    getResourceMicroStation(distance);
                }
            }
        });
        roadConditionCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                if (isChecked) {
                    //开启交通图
                    mBaiduMap.setTrafficEnabled(true);
                } else {
                    //开启交通图
                    mBaiduMap.setTrafficEnabled(false);
                }
            }
        });
        moreTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiceMoreType();
            }
        });
        clearMarkerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNoChecked();
                clearMarker();
            }
        });
        resourceScopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiceScope();
            }
        });
        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RouteAddressActivity.class);
                intent.putExtra("enableLatLng", enableLatLng);
                intent.putExtra("enableAddress", enableAddress);
                startActivity(intent);
            }
        });
    }

    @Override
    public void reLoadPosition(String disasterId, String disasterType, String mapx, String mapy, String address) {
        if (!TextUtils.isEmpty(mapx) && !TextUtils.isEmpty(mapy)) {
            //此处不需要转换 都是灾情那边传过来的也是百度坐标
            LatLng cenLatLng = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));
            if (cenLatLng != null) {

                /**
                 * 2019/08/12新增
                 * 将上一次的聚合标记、和选中状态清除
                 */
                setNoChecked();
                clearMarker();

                mBaiduMap.clear();
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.zoom(15).target(cenLatLng);
                Bitmap viewBitmap = BitmapUtils.getViewBitmap(makeDisasterPlotView(disasterType, null));
                BitmapDescriptor disasterDescripter = BitmapDescriptorFactory.fromBitmap(viewBitmap);
                mCurrentMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
                        .position(cenLatLng).icon(disasterDescripter));
                Bundle bundle = new Bundle();
                bundle.putString("disasterId", disasterId);
                mCurrentMarker.setExtraInfo(bundle);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                enableLatLng = cenLatLng;
                enableAddress = address;
            }
        }
    }


    private void initMyLocation() {
        permissionHelper.requestPermissions("需要定位权限", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permissions) {
                if (mLocationClient == null) {
                    mLocationClient = new LocationClient(getActivity().getApplicationContext());
                    mLocationClient.registerLocationListener(mylocationListener);
                    LocationClientOption options = new LocationClientOption();
                    options.setOpenGps(true);
                    options.setCoorType("bd09ll");
                    options.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                    //值为零，不会间隔自动定位
                    options.setScanSpan(0);
                    options.disableCache(true);
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
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 定位监听者
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null && mBaiduMap != null) {
                if (bdLocation.getCity() != null) {
                    currentCity = bdLocation.getCity();
                }
//                MyLocationData data = new MyLocationData.Builder()
//                        .accuracy(bdLocation.getRadius()).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).direction(50).build();
//                mBaiduMap.setMyLocationData(data);
//                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                enableLatLng = latLng;
//                enableAddress = bdLocation.getAddrStr();
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.zoom(15).target(latLng);
//                OverlayOptions options = new MarkerOptions().position(latLng).icon(iconDescripter);
//                mCurrentMarker = (Marker) mBaiduMap.addOverlay(options);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            }
        }

    }

    private void initPoiSearch() {
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        sugAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        aetAddressInput.setAdapter(sugAdapter);
        aetAddressInput.setThreshold(1);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        aetAddressInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString()).city(currentCity));
            }
        });
    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        suggestionInfoList = new ArrayList<SuggestionResult.SuggestionInfo>();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
                suggestionInfoList.add(info);
            }
        }
        sugAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggest);
        aetAddressInput.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    /**
     * 生成灾情标记view
     *
     * @param zqlxdm
     */
    public View makeDisasterPlotView(String zqlxdm, String showText) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.disaster_plot_layout, null);
        ImageView disasterPlotIv = (ImageView) view.findViewById(R.id.disaster_plot_iv);
        TextView disasterPlotTv = (TextView) view.findViewById(R.id.disaster_plot_text);
        String zqlxStr = "";
        int resId = 0;
        if (zqlxdm != null) {
            switch (zqlxdm) {
                case "10000":
                    zqlxStr = "火灾扑救";
                    resId = R.mipmap.ic_home_fire;
                    break;
                case "20000":
                    zqlxStr = "抢险救援";
                    resId = R.mipmap.ic_home_rescue;
                    break;
                case "30000":
                    zqlxStr = "反恐排爆";
                    resId = R.mipmap.ic_home_anti_terrorism;
                    break;
                case "40000":
                    zqlxStr = "公务执勤";
                    resId = R.mipmap.ic_home_official_attendance;
                    break;
                case "50000":
                    zqlxStr = "社会救助";
                    resId = R.mipmap.ic_home_social_rescue;
                    break;
                case "60000":
                    zqlxStr = "其他出动";
                    resId = R.mipmap.ic_home_others;
                    break;
                case "70000":
                    zqlxStr = "演练测试";
                    resId = R.mipmap.ic_home_police_drill;
                    break;
                default:
                    break;
            }
        } else {
            zqlxStr = showText;
            resId = R.mipmap.icon_marka;
        }
        disasterPlotTv.setText(zqlxStr);
        disasterPlotIv.setBackgroundResource(resId);
        return view;
    }

    private void showChoiceMoreType() {
        View moreTypeView = View.inflate(getActivity(), R.layout.more_type_layout, null);
        CheckableImageButton importUnitCIB = (CheckableImageButton) moreTypeView.findViewById(R.id.ck_import_unit);
        CheckableImageButton nineSmallCIB = (CheckableImageButton) moreTypeView.findViewById(R.id.ck_nine_small_place);
        CheckableImageButton highBuildCIB = (CheckableImageButton) moreTypeView.findViewById(R.id.ck_high_build);
        CheckableImageButton logisticUnitCIB = (CheckableImageButton) moreTypeView.findViewById(R.id.ck_logistic_unit);
        CheckableImageButton emergencyUnitCIB = (CheckableImageButton) moreTypeView.findViewById(R.id.ck_emergency_unit);

        importUnitCIB.setChecked(isCheckedImportUnit);
        nineSmallCIB.setChecked(isCheckedNineSmallPlace);
        highBuildCIB.setChecked(isCheckedHighBuild);
        logisticUnitCIB.setChecked(isCheckedLogisticUnit);
        emergencyUnitCIB.setChecked(isCheckedEmergencyUnit);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setView(moreTypeView, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        importUnitCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedImportUnit = isChecked;
                clearItemCluster(mImportantUnitItemClusterManager);
                if (isCheckedImportUnit) {
                    getResourceImportUnit(distance);
                }
            }
        });
        nineSmallCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedNineSmallPlace = isChecked;
                clearItemCluster(mNineSmallPlaceItemClusterManager);
                if (isCheckedNineSmallPlace) {
                    getResourceNineSmallPlace(distance);
                }
            }
        });
        highBuildCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedHighBuild = isChecked;
                clearItemCluster(mHighBuildItemClusterManager);
                if (isCheckedHighBuild) {
                    getResourceHighBuild(distance);
                }
            }
        });
        logisticUnitCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedLogisticUnit = isChecked;
                clearItemCluster(mLogisticUnitItemClusterManager);
                if (isCheckedLogisticUnit) {
                    getResourceLogisticUnit(distance);
                }
            }
        });
        emergencyUnitCIB.setOnStatusChangedListener(new CheckableImageButton.OnStatusChangedListener() {
            @Override
            public void onButtonStatusChanged(View v, boolean isChecked) {
                isCheckedEmergencyUnit = isChecked;
                clearItemCluster(mEmergencyUnitItemClusterManager);
                if (isCheckedEmergencyUnit) {
                    getResourceEmergencyUnit(distance);
                }
            }
        });
    }

    private void showChoiceScope() {
        View bottomView = View.inflate(getActivity(), R.layout.resource_scope_layout, null);
        final MultiLineRadioGroup mapLayerGroup = (MultiLineRadioGroup) bottomView.findViewById(R.id.resource_scope_group);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setView(bottomView, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        mapLayerGroup.check(scopeCheckId);
        mapLayerGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MultiLineRadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.diameter_type_one_btn) {
                    distance = 5;
                    changeRaduisReLoadData();
                } else if (checkedId == R.id.diameter_type_two_btn) {
                    distance = 10;
                    changeRaduisReLoadData();
                } else if (checkedId == R.id.diameter_type_three_btn) {
                    distance = 15;
                    changeRaduisReLoadData();
                } else if (checkedId == R.id.diameter_type_four_btn) {
                    distance = 20;
                    changeRaduisReLoadData();
                } else if (checkedId == R.id.diameter_type_five_btn) {
                    distance = 50;
                    changeRaduisReLoadData();
                }
                scopeCheckId = mapLayerGroup.getCheckedRadioButtonId();
            }
        });
    }

    @Override
    public void onMarkHydrantTarget(LatLng latLng, ResourceHydrant resourceHydrant) {
        Log.e("onMarkHydrantTarget", "onMarkHydrantTarget: " + latLng.toString());
        HydrantItem hydrantItem = new HydrantItem(latLng, resourceHydrant);
        mHydrantItemClusterManager.addItem(hydrantItem);
        mHydrantItemClusterManager.cluster();
    }

    @Override
    public void onMarkFireOrganTarget(LatLng latLng, ResourceFireOrgan resourceImportantOrgan) {
        Log.e("onMarkTarget", "onMarkFireOrganTarget: " + latLng.toString());
        FireOrganItem importantOrganItem = new FireOrganItem(latLng, resourceImportantOrgan);
        mFireOrganItemClusterManager.addItem(importantOrganItem);
        mFireOrganItemClusterManager.cluster();
    }

    @Override
    public void onMarkImportantUnitTarget(LatLng latLng, ResourceImportantUnit resourceImportantUnit) {
        Log.e("onMarkTarget", "onMarkImportUnitTarget: " + latLng.toString());
        ImportantUnitItem importantUnitItem = new ImportantUnitItem(latLng, resourceImportantUnit);
        mImportantUnitItemClusterManager.addItem(importantUnitItem);
        mImportantUnitItemClusterManager.cluster();
    }

    @Override
    public void onMarkMicroStationTarget(LatLng latLng, ResourceMicroStation resourceMicroStation) {
        Log.e("onMarkTarget", "onMarkMicroStationTarget: " + latLng.toString());
        MicroStationItem microStationItem = new MicroStationItem(latLng, resourceMicroStation);
        mMicroStationItemClusterManager.addItem(microStationItem);
        mMicroStationItemClusterManager.cluster();
    }

    @Override
    public void onMarkEmergencyUnitTarget(LatLng latLng, ResourceEmergencyUnit resourceEmergencyUnit) {
        Log.e("onMarkTarget", "onMarkEmergencyUnitTarget: " + latLng.toString());
        EmergencyUnitItem emergencyUnitItem = new EmergencyUnitItem(latLng, resourceEmergencyUnit);
        mEmergencyUnitItemClusterManager.addItem(emergencyUnitItem);
        mEmergencyUnitItemClusterManager.cluster();
    }

    @Override
    public void onMarkLogisticUnitTarget(LatLng latLng, ResourceLogisticUnit resourceLogisticUnit) {
        Log.e("onMarkTarget", "onMarkLogisticUnitTarget: " + latLng.toString());
        LogisticUnitItem logisticUnitItem = new LogisticUnitItem(latLng, resourceLogisticUnit);
        mLogisticUnitItemClusterManager.addItem(logisticUnitItem);
        mLogisticUnitItemClusterManager.cluster();
    }

    @Override
    public void onMarkHighBuildTarget(LatLng latLng, ResourceHighBuild resourceHighBuild) {
        Log.e("onMarkTarget", "onMarkHighBuildTarget: " + latLng.toString());
        HighBuildItem highBuildItem = new HighBuildItem(latLng, resourceHighBuild);
        mHighBuildItemClusterManager.addItem(highBuildItem);
        mHighBuildItemClusterManager.cluster();
    }

    @Override
    public void onMarkNineSmallPlaceTarget(LatLng latLng, ResourceNineSmallPlace resourceNineSmallPlace) {
        Log.e("onMarkTarget", "onMarkNineSmallPlaceTarget: " + latLng.toString());
        NineSmallPlaceItem nineSmallPlaceItem = new NineSmallPlaceItem(latLng, resourceNineSmallPlace);
        mNineSmallPlaceItemClusterManager.addItem(nineSmallPlaceItem);
        mNineSmallPlaceItemClusterManager.cluster();
    }

    @Override
    public void onMarkTarget(LatLng latLng, String type, int order, String disasterType) {

    }


    @Override
    public void onNewDisasterInfoArrived(final NewDisasterInfo.MsgContentBean msgContentBean) {
        final NewDisasterInfo.MsgContentBean.ZqBean info = msgContentBean.getZq();
        if (info != null) {
            if (ConstData.isMainActivtyFrontShow) {
                Log.e("onNewArrived", "onNewDisasterInfoArrived: 1965");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showNewDisasterArrivedDialog(info);
                    }
                });
            } else {
                Log.e("onNewArrived", "onNewDisasterInfoArrived: 2019");
                KeyguardManager mKeyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
                /**
                 * 如果flag为false，表示目前未锁屏
                 */
                boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
                //设计思想：屏幕未锁屏就发送通知信息，锁屏就发送广播
                if (!flag) {
                    //发送通知消息
                    ResidentNotificationHelper.sendNewDisasterNotice(getActivity(), msgContentBean);
                } else {
                    Intent intent = new Intent();
                    intent.setAction("com.telewave.battlecommand.LockScreenMsgReceiver");
                    intent.setComponent(new ComponentName("com.telewave.battlecommand", "com.telewave.business.twe.Receiver.LockScreenMsgReceiver"));
                    intent.putExtra("msgContentBean", msgContentBean);
                    intent.putExtra("msgType", MessageType.NEW_DISASTER_INFO);
                    //发送广播
                    getActivity().sendBroadcast(intent);
                    Log.e("onNewArrived", "onNewDisasterInfoArrived: 2034");
                }
            }
            if (ConstData.isNewDisasterVoiceOpen) {
                String zhdjdm = info.getZhdjdm();
                ringSound(zhdjdm);
            }

            /**
             * 2019/08/01新警情来了打印出来
             */
            if (ConstData.isPrintNewDisaster) {
                sunmiPrintDisasterData(info);
            }
            /**
             * 2019/08/14新增 新警情来了 打开告警控制器1、2、3个通道
             */
            openAlarmControl();

        }
    }


    @Override
    public void onCallPoliceInfo(CallPoliceMessage callPoliceMessage) {
        final CallPoliceMessage.ZqBean info = callPoliceMessage.getZq();
        if (info != null) {
            if (ConstData.isMainActivtyFrontShow) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCallPoliceArrivedDialog(info);
                    }
                });
            } else {
                KeyguardManager mKeyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
                /**
                 * 如果flag为false，表示目前未锁屏
                 */
                boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
                //设计思想：屏幕未锁屏就发送通知信息，锁屏就发送广播
                if (!flag) {
                    //发送通知消息
                    ResidentNotificationHelper.sendNewCallPoliceNotice(getActivity(), callPoliceMessage);
                } else {
                    Intent intent = new Intent();
                    intent.setAction("com.telewave.battlecommand.LockScreenMsgReceiver");
                    intent.setComponent(new ComponentName("com.telewave.battlecommand", "com.telewave.business.twe.Receiver.LockScreenMsgReceiver"));
                    intent.putExtra("callPoliceMessage", callPoliceMessage);
                    intent.putExtra("msgType", MessageType.RECEIVE_CALL_POLICE_MESSAGE);
                    //发送广播
                    getActivity().sendBroadcast(intent);
                }
            }
            if (ConstData.isNewDisasterVoiceOpen) {
                String zhdjdm = info.getZhdjdm();
                ringSound(zhdjdm);
            }
            /**
             * 2019/08/01新报警来了打印出来
             */
            if (ConstData.isPrintNewDisaster) {
                sunmiPrintCallPoliceData(info);
            }
        }
    }

    /**
     * 打开联动告警控制盒
     */
    public void openAlarmControl() {
        //默认 端口/dev/ttyHSL1   波特率 9600，如果要修改端口和波特率，就用这个构造方法 ComA = new SerialControl("/dev/ttyHSL2",11200);
        final SerialPortInstance serialPortInstance = new SerialPortInstance();
        //打开串口
        try {
            SerialPortOpt.openComPort(getActivity(), serialPortInstance);
        } catch (SecurityException e) {
            ToastUtils.toastShortMessage("打开串口失败:没有串口读/写权限!");
        } catch (InvalidParameterException e) {
            ToastUtils.toastShortMessage("打开串口失败:参数错误!");
        }

        SerialPortOpt.sendPortData(serialPortInstance, ControlAPI.ctrOffSetOpen(0));
        SerialPortOpt.sendPortData(serialPortInstance, ControlAPI.ctrOffSetOpen(1));
        SerialPortOpt.sendPortData(serialPortInstance, ControlAPI.ctrOffSetOpen(2));
        /**
         * 倒计时10秒后关闭打开的告警控制器通道
         */
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SerialPortOpt.sendPortData(serialPortInstance, ControlAPI.ctrOffSetClose(0));
                SerialPortOpt.sendPortData(serialPortInstance, ControlAPI.ctrOffSetClose(1));
                SerialPortOpt.sendPortData(serialPortInstance, ControlAPI.ctrOffSetClose(2));
            }
        }, 10 * 1000);

    }

    private void ringSound(String zhdjdm) {
        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // 停止 开启 停止 开启
        final long[] pattern = {100, 400, 100, 400, 100, 400};
        final MediaPlayer player = new MediaPlayer();
        player.reset();
        player.setVolume(1.0f, 1.0f);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                vibrator.vibrate(pattern, -1);
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
                vibrator.cancel();
            }
        });
        switch (zhdjdm) {
            case "1":
            case "2":
                try {
                    AssetFileDescriptor assetFileDescriptor = getActivity().getAssets().openFd("normal_disaster.mp3");
                    player.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    player.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "3":
            case "4":
            case "5":
                try {
                    AssetFileDescriptor assetFileDescriptor = getActivity().getAssets().openFd("important_disaster.mp3");
                    player.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    player.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 接收到调派警情  弹出Dialog
     *
     * @param zqBean
     */
    protected void showNewDisasterArrivedDialog(final NewDisasterInfo.MsgContentBean.ZqBean zqBean) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_disaster_arrived, null);
        TextView tvMarkAddress = (TextView) view.findViewById(R.id.tv_new_disaster_address);
        TextView tvMarkTime = (TextView) view.findViewById(R.id.tv_new_disaster_time);
        TextView tvMarkType = (TextView) view.findViewById(R.id.tv_new_disaster_type);
        Button btnChecked = (Button) view.findViewById(R.id.btn_checked);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tvMarkAddress.setText(zqBean.getZhdd());
        tvMarkTime.setText(zqBean.getBjsj());
        tvMarkType.setText(getDisasterTypeByStr(zqBean.getZqlxdm()));
        btnChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.selectViewPagerIndex(2);
                reLoadPosition(zqBean.getAlarmid(), zqBean.getZqlxdm(), zqBean.getGis_x() + "", zqBean.getGis_y() + "", zqBean.getZhdd());
                dialog.dismiss();
            }
        });
    }

    /**
     * 接收到新报警  弹出Dialog
     *
     * @param zqBean
     */
    protected void showCallPoliceArrivedDialog(final CallPoliceMessage.ZqBean zqBean) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_disaster_arrived, null);
        TextView tvMarkAddress = (TextView) view.findViewById(R.id.tv_new_disaster_address);
        TextView tvMarkTime = (TextView) view.findViewById(R.id.tv_new_disaster_time);
        TextView tvMarkType = (TextView) view.findViewById(R.id.tv_new_disaster_type);
        Button btnChecked = (Button) view.findViewById(R.id.btn_checked);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tvMarkAddress.setText(zqBean.getZhdd());
        tvMarkTime.setText(zqBean.getBjsj());
        tvMarkType.setText(getDisasterTypeByStr(zqBean.getZqlxdm()));
        btnChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.selectViewPagerIndex(2);
                reLoadPosition(zqBean.getAlarmid(), zqBean.getZqlxdm(), zqBean.getGis_x() + "", zqBean.getGis_y() + "", zqBean.getZhdd());
                dialog.dismiss();
            }
        });
    }

    /**
     * 打印调派警情
     *
     * @param zqBean
     */
    public void sunmiPrintDisasterData(NewDisasterInfo.MsgContentBean.ZqBean zqBean) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接警时间：" + (!TextUtils.isEmpty(zqBean.getBjsj()) ? zqBean.getBjsj() : "") + "\r\n")
                .append("报警人：" + (!TextUtils.isEmpty(zqBean.getLxr()) ? zqBean.getLxr() : "") + "\r\n")
                .append("联系方式：" + (!TextUtils.isEmpty(zqBean.getLxdh()) ? zqBean.getLxdh() : "") + "\r\n")
                .append("灾害单位：" + (!TextUtils.isEmpty(zqBean.getZddwmc()) ? zqBean.getZddwmc() : "") + "\r\n")
                .append("灾害地址：" + (!TextUtils.isEmpty(zqBean.getZhdd()) ? zqBean.getZhdd() : "") + "\r\n")
                .append("灾害等级：" + (!TextUtils.isEmpty(zqBean.getZhdjdm()) && !TextUtils.isEmpty(getDisasterGradeByStr(zqBean.getZhdjdm())) ? getDisasterGradeByStr(zqBean.getZhdjdm()) : "") + "\r\n")
                .append("灾害类型：" + (!TextUtils.isEmpty(zqBean.getZqlxdm()) && !TextUtils.isEmpty(getDisasterTypeByStr(zqBean.getZqlxdm())) ? getDisasterTypeByStr(zqBean.getZqlxdm()) : "") + "\r\n")
                .append("灾情描述：" + (!TextUtils.isEmpty(zqBean.getZqms()) ? zqBean.getZqms() : "") + "\r\n")
                .append("烟雾情况：" + (!TextUtils.isEmpty(zqBean.getSmogstatus()) ? zqBean.getSmogstatus() : "") + "\r\n")
                .append("灾害状况：" + (!TextUtils.isEmpty(zqBean.getFirematterclass()) ? zqBean.getFirematterclass() : "") + "\r\n")
                .append("主管中队：" + (!TextUtils.isEmpty(zqBean.getXqzdjgmc()) ? zqBean.getXqzdjgmc() : "") + "\r\n")
                .append("\r\n");
        Log.e(TAG, "sunmiPrintDisasterData: " + stringBuffer.toString());
        AidlUtil.getInstance().printTitleText("灾情信息" + "\r\n");
        AidlUtil.getInstance().print1Line();
        AidlUtil.getInstance().printStrText("********************************************" + "\r\n");
        AidlUtil.getInstance().printAddressText((!TextUtils.isEmpty(zqBean.getZhdd()) ? zqBean.getZhdd() : "") + "\r\n");
        AidlUtil.getInstance().printStrText("********************************************" + "\r\n");
        AidlUtil.getInstance().printContentText(stringBuffer.toString());
        AidlUtil.getInstance().print1Line();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 160;
        options.inDensity = 160;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg13, options);
        AidlUtil.getInstance().printRightBitmap(bitmap);
        AidlUtil.getInstance().print3Line();
        AidlUtil.getInstance().cutterPage();

    }

    /**
     * 打印报警信息
     *
     * @param zqBean
     */
    public void sunmiPrintCallPoliceData(CallPoliceMessage.ZqBean zqBean) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("接警时间：" + (!TextUtils.isEmpty(zqBean.getBjsj()) ? zqBean.getBjsj() : "") + "\r\n")
                .append("报警人：" + (!TextUtils.isEmpty(zqBean.getBjr()) ? zqBean.getBjr() : "") + "\r\n")
                .append("联系方式：" + (!TextUtils.isEmpty(zqBean.getBjdh()) ? zqBean.getBjdh() : "") + "\r\n")
                .append("灾害单位：" + (!TextUtils.isEmpty(zqBean.getZddwmc()) ? zqBean.getZddwmc() : "") + "\r\n")
                .append("灾害地址：" + (!TextUtils.isEmpty(zqBean.getZhdd()) ? zqBean.getZhdd() : "") + "\r\n")
                .append("灾害等级：" + (!TextUtils.isEmpty(zqBean.getZhdjdm()) && !TextUtils.isEmpty(getDisasterGradeByStr(zqBean.getZhdjdm())) ? getDisasterGradeByStr(zqBean.getZhdjdm()) : "") + "\r\n")
                .append("灾害类型：" + (!TextUtils.isEmpty(zqBean.getZqlxdm()) && !TextUtils.isEmpty(getDisasterTypeByStr(zqBean.getZqlxdm())) ? getDisasterTypeByStr(zqBean.getZqlxdm()) : "") + "\r\n")
                .append("灾情描述：" + (!TextUtils.isEmpty(zqBean.getZqms()) ? zqBean.getZqms() : "") + "\r\n")
                .append("烟雾情况：" + (!TextUtils.isEmpty(zqBean.getSmogstatus()) ? zqBean.getSmogstatus() : "") + "\r\n")
                .append("灾害状况：" + (!TextUtils.isEmpty(zqBean.getFirematterclass()) ? zqBean.getFirematterclass() : "") + "\r\n")
                .append("主管中队：" + (!TextUtils.isEmpty(zqBean.getXqzdjgname()) ? zqBean.getXqzdjgname() : "") + "\r\n")
                .append("\r\n");
        Log.e(TAG, "sunmiPrintDisasterData: " + stringBuffer.toString());
        AidlUtil.getInstance().printTitleText("灾情信息" + "\r\n");
        AidlUtil.getInstance().print1Line();
        AidlUtil.getInstance().printStrText("********************************************" + "\r\n");
        AidlUtil.getInstance().printAddressText((!TextUtils.isEmpty(zqBean.getZhdd()) ? zqBean.getZhdd() : "") + "\r\n");
        AidlUtil.getInstance().printStrText("********************************************" + "\r\n");
        AidlUtil.getInstance().printContentText(stringBuffer.toString());
        AidlUtil.getInstance().print1Line();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 160;
        options.inDensity = 160;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg13, options);
        AidlUtil.getInstance().printRightBitmap(bitmap);
        AidlUtil.getInstance().print3Line();
        AidlUtil.getInstance().cutterPage();

    }

    /**
     * 通过字符串取出灾情类型
     *
     * @param zqlxdm
     * @return
     */
    private String getDisasterTypeByStr(String zqlxdm) {
        String zqlxStr = "";
        if ("10000".equals(zqlxdm)) {
            zqlxStr = "火灾扑救";
        } else if ("20000".equals(zqlxdm)) {
            zqlxStr = "抢险救援";
        } else if ("30000".equals(zqlxdm)) {
            zqlxStr = "反恐排爆";
        } else if ("40000".equals(zqlxdm)) {
            zqlxStr = "公务执勤";
        } else if ("50000".equals(zqlxdm)) {
            zqlxStr = "社会救助";
        } else if ("60000".equals(zqlxdm)) {
            zqlxStr = "其他出动";
        } else if ("70000".equals(zqlxdm)) {
            zqlxStr = "演练测试";
        }
        return zqlxStr;
    }

    /**
     * 通过字符串取出灾情等级
     *
     * @param zqdjdm
     * @return
     */
    private String getDisasterGradeByStr(String zqdjdm) {
        String zqdjStr = "";
        if ("1".equals(zqdjdm)) {
            zqdjStr = "一级";
        } else if ("2".equals(zqdjdm)) {
            zqdjStr = "二级";
        } else if ("30000".equals(zqdjdm)) {
            zqdjStr = "三级";
        } else if ("40000".equals(zqdjdm)) {
            zqdjStr = "四级";
        } else if ("50000".equals(zqdjdm)) {
            zqdjStr = "五级";
        }
        return zqdjStr;
    }

    /**
     * 改变地图直径范围，重新加载
     */
    protected void changeRaduisReLoadData() {
        clearMarker();
//        initDisasterAddress(true);
        Log.e(TAG, "changeRaduisReLoadData: " + isCheckedHydrant);
        if (isCheckedHydrant) {
            getResourceHydrant(distance);
        }
        if (isCheckedFireOrgan) {
            getResourceFireOrgan(distance);
        }
        if (isCheckedMicroStation) {
            getResourceMicroStation(distance);
        }

        if (isCheckedImportUnit) {
            getResourceImportUnit(distance);
        }

        if (isCheckedEmergencyUnit) {
            getResourceEmergencyUnit(distance);
        }

        if (isCheckedLogisticUnit) {
            getResourceLogisticUnit(distance);
        }
        if (isCheckedHighBuild) {
            getResourceHighBuild(distance);
        }
        if (isCheckedNineSmallPlace) {
            getResourceNineSmallPlace(distance);
        }
    }


    /**
     * 获取资源
     * 消防队/消防机构
     *
     * @param choiceDistance
     */
    public void getResourceFireOrgan(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceFireOrganData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceFireOrganData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 消防栓/水源
     *
     * @param choiceDistance
     */
    public void getResourceHydrant(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceHydrantData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceHydrantData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 微站
     *
     * @param choiceDistance
     */
    public void getResourceMicroStation(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceMicroStationData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceMicroStationData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 重点单位
     *
     * @param choiceDistance
     */
    public void getResourceImportUnit(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceImportUnitData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceImportUnitData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 应急单位
     *
     * @param choiceDistance
     */
    public void getResourceEmergencyUnit(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceEmergencyUnitData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceEmergencyUnitData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 联勤单位
     *
     * @param choiceDistance
     */
    public void getResourceLogisticUnit(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceLogisticUnitData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceLogisticUnitData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 高层建筑
     *
     * @param choiceDistance
     */
    public void getResourceHighBuild(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceHighBuildData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceHighBuildData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 获取资源
     * 九小场所
     *
     * @param choiceDistance
     */
    public void getResourceNineSmallPlace(int choiceDistance) {
        if (enableLatLng != null) {
//            mResourcesPresenter.getResourceNineSmallPlaceData(choiceDistance * 100, enableLatLng.longitude,
//                    enableLatLng.latitude, false);
            PositionUtils.GPS gps = PositionUtils.bd09_To_Gps84(enableLatLng.latitude, enableLatLng.longitude);
            if (gps != null) {
                mResourcesPresenter.getResourceNineSmallPlaceData(choiceDistance * 100, gps.getWgLon(),
                        gps.getWgLat(), false);
            }
        }
    }

    /**
     * 是否是第一次标记灾情地址,如果不进行区分的话,就会再每一次点击灾情地址按钮的时候都会画一个圈圈,会造成圈圈越来越不透明
     *
     * @param isFirst 是否是第一次进行灾情初始化
     */
    private void initDisasterAddress(boolean isFirst) {
        //显示等级-转换
        float level = LocationLevelUtils.returnCurZoom(distance * 100);
        Log.e(TAG, "initDisasterAddress: " + level);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(enableLatLng).zoom(level).build()));
        if (isFirst) {
            if (enableLatLng != null) {
                addCircel(enableLatLng, distance * 100);
            }
        }
    }

    /**
     * @param latlng 灾害地点的经纬度
     * @param radius 圆圈的半径
     */
    private void addCircel(LatLng latlng, int radius) {
        if (circleOverly != null) {
            circleOverly.remove();
        }
        int color = Color.argb(64, 243, 210, 202);
        OverlayOptions options = new CircleOptions().center(latlng).stroke(new Stroke(3, Color.argb(128, 249, 161, 146))).radius(radius).fillColor(color);
        circleOverly = mBaiduMap.addOverlay(options);
    }

    /**
     * 这个方法是用来进行资源的展示,可以选择两种表现形式,一种是传递序号,这个时候会从list中读取,此时第三个随便赋值,
     * 同理,第二种是使用第三个参数而不是用第二个参数
     *
     * @param type   展示资源的类型
     * @param order  展示资源的序号
     * @param object 展示资源的对象
     */
    private void showDialogOfmarker(final String type, final Object order, final Object object) {
        LinearLayout view = null;
        LatLng latLng = null;
        switch (type) {
            case ConstData.RESOURCE_HYDRANT_TYPE:
                ResourceHydrant hydrantTmp = (ResourceHydrant) object;
                if (hydrantTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_hydrant, null);
                    TextView hydrant_name = view.findViewById(R.id.hydrant_name);
                    ImageView waterDav = view.findViewById(R.id.ibtn_water_dav);
                    waterDav.setVisibility(View.GONE);
                    TextView hydrant_address = view.findViewById(R.id.hydrant_address);
                    TextView hydrant_qsxs = view.findViewById(R.id.hydrant_qsxs);
                    TextView hydrant_organ_name = view.findViewById(R.id.hydrant_organ_name);
                    TextView hydrant_gwxs = view.findViewById(R.id.hydrant_gwxs);
                    TextView hydrant_gwzj = view.findViewById(R.id.hydrant_gwzj);
                    TextView hydrant_gwyl = view.findViewById(R.id.hydrant_gwyl);
                    TextView hydrant_ll = view.findViewById(R.id.hydrant_ll);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    if ("1".equals(hydrantTmp.getProperties().getKYZT())) {
                        hydrant_name.setText(hydrantTmp.getProperties().getRESOURCENAME() + "   " + "可用");
                    } else {
                        hydrant_name.setText(hydrantTmp.getProperties().getRESOURCENAME() + "   " + "不可用");
                    }
                    hydrant_address.setText("地址 : " + hydrantTmp.getProperties().getADDRESS());
                    hydrant_qsxs.setText("取水形式 : " + hydrantTmp.getProperties().getQSXS());
                    hydrant_organ_name.setText("所属机构 : " + hydrantTmp.getProperties().getORGANNAME());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(hydrantTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");

//                    hydrant_gwxs.setText("管网形式 : " + hydrantTmp.getGwxs());
//                    hydrant_gwzj.setText("管网直径 : " + hydrantTmp.getGwzj());
//                    hydrant_gwyl.setText("管网压力 : " + hydrantTmp.getGwyl());
//                    hydrant_ll.setText("流量 : " + hydrantTmp.getLl());
                    latLng = PositionUtils.Gps84_To_bd09(hydrantTmp.getGeometry().getCoordinates().get(1),
                            hydrantTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_FIRE_ORGAN_TYPE:
                ResourceFireOrgan importantOrganTmp = (ResourceFireOrgan) object;
                if (importantOrganTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    name.setText(importantOrganTmp.getProperties().getRESOURCENAME());
                    source_address.setText("简称：" + importantOrganTmp.getProperties().getSHORTNAME());
                    source_left1.setText("联系人 : " + importantOrganTmp.getProperties().getCONTACTS());
                    source_left2.setText("联系电话 : " + importantOrganTmp.getProperties().getCONTACTNUMBER());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(importantOrganTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(importantOrganTmp.getGeometry().getCoordinates().get(1),
                            importantOrganTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_IMPORTANT_UNIT_TYPE:
                ResourceImportantUnit importantUnitTmp = (ResourceImportantUnit) object;
                if (importantUnitTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    source_address.setText(importantUnitTmp.getProperties().getADDRESS());
                    name.setText(importantUnitTmp.getProperties().getRESOURCENAME());
                    source_left1.setText("联系人 : " + importantUnitTmp.getProperties().getCONTACTS());
                    source_left2.setText("联系电话 : " + importantUnitTmp.getProperties().getCONTACTNUMBER());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(importantUnitTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(importantUnitTmp.getGeometry().getCoordinates().get(1),
                            importantUnitTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_MICRO_STATION_TYPE:
                ResourceMicroStation resourceMicroStationTmp = (ResourceMicroStation) object;
                if (resourceMicroStationTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    source_address.setText(resourceMicroStationTmp.getProperties().getADDRESS());
                    name.setText(resourceMicroStationTmp.getProperties().getRESOURCENAME());
                    if (TextUtils.isEmpty(resourceMicroStationTmp.getProperties().getCONTACTS())) {
                        source_left1.setText("联系人 : " + "暂无");
                        source_left2.setText("联系电话 : " + "暂无");
                    } else {
                        source_left1.setText("联系人 : " + resourceMicroStationTmp.getProperties().getCONTACTS());
                        source_left2.setText("联系电话 : " + resourceMicroStationTmp.getProperties().getCONTACTNUMBER());
                    }
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(resourceMicroStationTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(resourceMicroStationTmp.getGeometry().getCoordinates().get(1),
                            resourceMicroStationTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_EMERGENCY_UNIT_TYPE:
                ResourceEmergencyUnit resourceEmergencyUnitTmp = (ResourceEmergencyUnit) object;
                if (resourceEmergencyUnitTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    source_address.setText(resourceEmergencyUnitTmp.getProperties().getDWDZ());
                    name.setText(resourceEmergencyUnitTmp.getProperties().getDWMC());
                    source_left1.setText("联系人 : " + resourceEmergencyUnitTmp.getProperties().getLXR());
                    source_left2.setText("联系电话 : " + resourceEmergencyUnitTmp.getProperties().getLXDH());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(resourceEmergencyUnitTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(resourceEmergencyUnitTmp.getGeometry().getCoordinates().get(1),
                            resourceEmergencyUnitTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_LOGISTIC_UNIT_TYPE:
                ResourceLogisticUnit resourceLogisticUnitTmp = (ResourceLogisticUnit) object;
                if (resourceLogisticUnitTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    source_address.setText("维护消防机构 : " + resourceLogisticUnitTmp.getProperties().getWHXFJGMC());
                    name.setText(resourceLogisticUnitTmp.getProperties().getDWMC());
                    source_left1.setText("单位保障类型 : " + resourceLogisticUnitTmp.getProperties().getBZNL());
                    source_left2.setText("值班联系电话 : " + resourceLogisticUnitTmp.getProperties().getYJZBDH());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(resourceLogisticUnitTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(resourceLogisticUnitTmp.getGeometry().getCoordinates().get(1),
                            resourceLogisticUnitTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_HIGH_BUILD_TYPE:
                ResourceHighBuild resourceHighBuildTmp = (ResourceHighBuild) object;
                if (resourceHighBuildTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    source_address.setText(resourceHighBuildTmp.getProperties().getADDRESS());
                    name.setText(resourceHighBuildTmp.getProperties().getORGANNAME());
                    source_left1.setText("联系人 : " + resourceHighBuildTmp.getProperties().getCONTACTS());
                    source_left2.setText("联系电话 : " + resourceHighBuildTmp.getProperties().getCONTACTNUMBER());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(resourceHighBuildTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(resourceHighBuildTmp.getGeometry().getCoordinates().get(1),
                            resourceHighBuildTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            case ConstData.RESOURCE_NINE_SMALL_PLACE_TYPE:
                ResourceNineSmallPlace resourceNineSmallPlaceTmp = (ResourceNineSmallPlace) object;
                if (resourceNineSmallPlaceTmp != null) {
                    view = (LinearLayout) View.inflate(getActivity(), R.layout.source_popwindow_unit, null);
                    TextView name = view.findViewById(R.id.Name);
                    TextView source_address = view.findViewById(R.id.source_address);
                    TextView source_left1 = view.findViewById(R.id.source_left1);
                    TextView source_left2 = view.findViewById(R.id.source_left2);
                    TextView source_left3 = view.findViewById(R.id.source_left3);
                    LinearLayout linear_source_pup_right = view.findViewById(R.id.linear_source_pup_right);
                    linear_source_pup_right.setVisibility(View.GONE);
                    source_address.setText(resourceNineSmallPlaceTmp.getProperties().getADDRESS());
                    name.setText(resourceNineSmallPlaceTmp.getProperties().getORGANNAME());
                    source_left1.setText("联系人 : " + resourceNineSmallPlaceTmp.getProperties().getCONTACTS());
                    source_left2.setText("联系电话 : " + resourceNineSmallPlaceTmp.getProperties().getCONTACTNUMBER());
                    BigDecimal bigDecimal = new BigDecimal(Double.toString(resourceNineSmallPlaceTmp.getProperties().getDISTANCE()));
                    source_left3.setText("距离 : " + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "米");
                    latLng = PositionUtils.Gps84_To_bd09(resourceNineSmallPlaceTmp.getGeometry().getCoordinates().get(1),
                            resourceNineSmallPlaceTmp.getGeometry().getCoordinates().get(0));
                }
                break;
            default:
                break;
        }
        if (view != null) {
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //第三个参数代表显示的位置的竖直上距离偏移量,-50代表显示在上方-50px的地方
            //不是这两种类型的创建一般类型的infowindow,如果是这两种类型的话就创建出比较特殊的类型
            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), latLng, -60, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    mBaiduMap.hideInfoWindow();
                }
            });
            mBaiduMap.showInfoWindow(mInfoWindow);
        }
    }

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
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

    /**
     * 清除界面上的标记物,用来加载更过历史灾情的时候进行清除再标记
     */
    @Override
    public void clearMapOverlays() {
        if (mBaiduMap != null) {
            mBaiduMap.clear();
        }
    }

    /**
     * 清理各种类型百度地图点聚合Item数据
     */
    protected void clearItemCluster(ClusterManager tClusterManager) {
        if (tClusterManager != null && tClusterManager.getAlgorithm().getItems().size() > 0) {
            tClusterManager.clearItems();
            tClusterManager.cluster();
        }
    }

    private void clearMarker() {
        /**
         * 清理上一次已经标记
         */
        clearItemCluster(mFireOrganItemClusterManager);
        clearItemCluster(mHydrantItemClusterManager);
        clearItemCluster(mMicroStationItemClusterManager);
        clearItemCluster(mImportantUnitItemClusterManager);
        clearItemCluster(mLogisticUnitItemClusterManager);
        clearItemCluster(mEmergencyUnitItemClusterManager);
        clearItemCluster(mHighBuildItemClusterManager);
        clearItemCluster(mNineSmallPlaceItemClusterManager);
    }

    /**
     * 将所有标识变量设置为false
     * 将外部三个Check设置为未选中(更多里的五个打开时会判断)
     */
    private void setNoChecked() {
        isCheckedHydrant = false;
        isCheckedFireOrgan = false;
        isCheckedMicroStation = false;
        isCheckedNineSmallPlace = false;
        isCheckedImportUnit = false;
        isCheckedHighBuild = false;
        isCheckedLogisticUnit = false;
        isCheckedEmergencyUnit = false;
        fireOrganCIB.setChecked(isCheckedFireOrgan);
        hydrantCIB.setChecked(isCheckedHydrant);
        microStationCIB.setChecked(isCheckedMicroStation);
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mylocationListener);
            mLocationClient = null;
        }
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
            mPoiSearch = null;
        }
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
            mSuggestionSearch = null;
        }
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

}