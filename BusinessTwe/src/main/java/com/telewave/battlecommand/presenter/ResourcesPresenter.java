package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.ResourceEmergencyUnit;
import com.telewave.battlecommand.bean.ResourceFireOrgan;
import com.telewave.battlecommand.bean.ResourceHighBuild;
import com.telewave.battlecommand.bean.ResourceHydrant;
import com.telewave.battlecommand.bean.ResourceImportantUnit;
import com.telewave.battlecommand.bean.ResourceLogisticUnit;
import com.telewave.battlecommand.bean.ResourceMicroStation;
import com.telewave.battlecommand.bean.ResourceNineSmallPlace;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.RxHttpUtils;
import com.telewave.battlecommand.http.SimpleRxSubscriber;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.map.util.PositionUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 周边资源代理类的实现类
 *
 * @author liwh
 * @date 2018/12/25
 */
public class ResourcesPresenter extends BasePresenter<IDirectionContract.IDirectionView>
        implements IDirectionContract.IDisasterResourcesPresenter {
    private static final String TAG = "ResourcesPresenter";
    /**
     * 获取消火栓请求类型
     */
    public static final String GET_HYDRANT_TYPE = "mfs:mhjy_syjbxx";
    /**
     * 获取重点单位请求类型
     */
    public static final String GET_IMPORT_UNIT_TYPE = "mfs:obj_objectinfo";
    /**
     * 获取消防机构请求类型
     */
    public static final String GET_FIRE_ORGAN_TYPE = "mfs:sys_office";
    /**
     * 获取微型消防站请求类型
     */
    public static final String GET_MICROSTATION_TYPE = "mfs:b_wxxfz";

    /**
     * 获取应急单位请求类型
     */
    public static final String GET_EMERGENCY_UNIT_TYPE = "mfs:emergency_unit";

    /**
     * 获取联动单位请求类型
     */
    public static final String GET_LOGISTIC_UNIT_TYPE = "mfs:logistic_unit";
    /**
     * 获取高层建筑请求类型
     */
    public static final String GET_HIGH_BUILD_TYPE = "mfs:high_buildings";
    /**
     * 获取九小场所请求类型
     */
    public static final String GET_NINE_SMALL_PLACE_TYPE = "mfs:nine_place";

    private List<ResourceHydrant> mHydrantLists = new ArrayList<>();
    private List<ResourceImportantUnit> mImportantUnitLists = new ArrayList<>();
    private List<ResourceFireOrgan> mFireOrganLists = new ArrayList<>();
    private List<ResourceMicroStation> mMicroStationLists = new ArrayList<>();
    private List<ResourceEmergencyUnit> mEmergencyUnitLists = new ArrayList<>();
    private List<ResourceLogisticUnit> mLogisticUnitLists = new ArrayList<>();
    private List<ResourceHighBuild> mHighBuildLists = new ArrayList<>();
    private List<ResourceNineSmallPlace> mNineSmallPlaceLists = new ArrayList<>();

    private Subscription hydrantSubscription;
    private Subscription importantUnitSubscription;
    private Subscription fireOrganSubscription;
    private Subscription microStationSubscription;
    private Subscription emergencyUnitSubscription;
    private Subscription logisticUnitSubscription;
    private Subscription highBuildSubscription;
    private Subscription nineSmallPlaceSubscription;

    public ResourcesPresenter(IDirectionContract.IDirectionView view) {
        super(view);
    }


    public List<ResourceHydrant> getmHydrantLists() {
        return mHydrantLists;
    }

    public List<ResourceImportantUnit> getmImportantUnitLists() {
        return mImportantUnitLists;
    }

    public List<ResourceFireOrgan> getmFireOrganLists() {
        return mFireOrganLists;
    }

    public List<ResourceMicroStation> getmMicroStationLists() {
        return mMicroStationLists;
    }

    public List<ResourceEmergencyUnit> getmEmergencyUnitLists() {
        return mEmergencyUnitLists;
    }

    public List<ResourceLogisticUnit> getmLogisticUnitLists() {
        return mLogisticUnitLists;
    }

    public List<ResourceHighBuild> getmHighBuildLists() {
        return mHighBuildLists;
    }

    public List<ResourceNineSmallPlace> getmNineSmallPlaceLists() {
        return mNineSmallPlaceLists;
    }

    /**
     * 获取资源 消防栓(水源)
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceHydrantData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (hydrantSubscription != null && !hydrantSubscription.isUnsubscribed()) {
            removeSubscription(hydrantSubscription);
            hydrantSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_HYDRANT_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.HYDRANT_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        hydrantSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceHydrant>>() {
                    @Override
                    public Observable<ResourceHydrant> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceHydrant> hydrantLists = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {

                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    hydrantLists = gson.fromJson(listBean, new TypeToken<List<ResourceHydrant>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + hydrantLists.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || hydrantLists == null || hydrantLists.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(hydrantLists);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mHydrantLists == null) {
                            mHydrantLists = new ArrayList<>();
                        }
                        mHydrantLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceHydrant>() {
                    @Override
                    public void call(ResourceHydrant resourceHydrant) {
                        mHydrantLists.add(resourceHydrant);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceHydrant>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mHydrantLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceHydrant resourceHydrant) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceHydrant.getGeometry().getCoordinates().get(1),
                                resourceHydrant.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkHydrantTarget(latLng, resourceHydrant);
                        }
                    }
                });
        addSubscription(hydrantSubscription);
    }

    /**
     * 获取资源 重点单位
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceImportUnitData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (importantUnitSubscription != null && !importantUnitSubscription.isUnsubscribed()) {
            removeSubscription(importantUnitSubscription);
            importantUnitSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_IMPORT_UNIT_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.IMPORTANT_UNIT_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        importantUnitSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceImportantUnit>>() {
                    @Override
                    public Observable<ResourceImportantUnit> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceImportantUnit> importantUnitList = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    importantUnitList = gson.fromJson(listBean, new TypeToken<List<ResourceImportantUnit>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + importantUnitList.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || importantUnitList == null || importantUnitList.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(importantUnitList);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mImportantUnitLists == null) {
                            mImportantUnitLists = new ArrayList<>();
                        }
                        mImportantUnitLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceImportantUnit>() {
                    @Override
                    public void call(ResourceImportantUnit resourceImportantUnit) {
                        mImportantUnitLists.add(resourceImportantUnit);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceImportantUnit>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mImportantUnitLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceImportantUnit resourceImportantUnit) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceImportantUnit.getGeometry().getCoordinates().get(1),
                                resourceImportantUnit.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkImportantUnitTarget(latLng, resourceImportantUnit);
                        }
                    }
                });
        addSubscription(importantUnitSubscription);
    }

    @Override
    public void getResourceFireOrganData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (fireOrganSubscription != null && !fireOrganSubscription.isUnsubscribed()) {
            removeSubscription(fireOrganSubscription);
            fireOrganSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_FIRE_ORGAN_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.FIRE_ORGAN_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        fireOrganSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceFireOrgan>>() {
                    @Override
                    public Observable<ResourceFireOrgan> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceFireOrgan> importantOrgans = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    importantOrgans = gson.fromJson(listBean, new TypeToken<List<ResourceFireOrgan>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + importantOrgans.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || importantOrgans == null || importantOrgans.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(importantOrgans);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mFireOrganLists == null) {
                            mFireOrganLists = new ArrayList<>();
                        }
                        mFireOrganLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceFireOrgan>() {
                    @Override
                    public void call(ResourceFireOrgan resourceImportantOrgan) {
                        mFireOrganLists.add(resourceImportantOrgan);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceFireOrgan>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mFireOrganLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceFireOrgan resourceImportantOrgan) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceImportantOrgan.getGeometry().getCoordinates().get(1),
                                resourceImportantOrgan.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkFireOrganTarget(latLng, resourceImportantOrgan);
                        }
                    }
                });
        addSubscription(fireOrganSubscription);
    }

    /**
     * 获取微站数据
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceMicroStationData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (microStationSubscription != null && !microStationSubscription.isUnsubscribed()) {
            removeSubscription(microStationSubscription);
            microStationSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_MICROSTATION_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.MICRO_STATION_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        microStationSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceMicroStation>>() {
                    @Override
                    public Observable<ResourceMicroStation> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceMicroStation> microStations = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    microStations = gson.fromJson(listBean, new TypeToken<List<ResourceMicroStation>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + microStations.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || microStations == null || microStations.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(microStations);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mMicroStationLists == null) {
                            mMicroStationLists = new ArrayList<>();
                        }
                        mMicroStationLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceMicroStation>() {
                    @Override
                    public void call(ResourceMicroStation resourceMicroStation) {
                        mMicroStationLists.add(resourceMicroStation);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceMicroStation>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mMicroStationLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceMicroStation resourceMicroStation) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceMicroStation.getGeometry().getCoordinates().get(1),
                                resourceMicroStation.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkMicroStationTarget(latLng, resourceMicroStation);
                        }
                    }
                });
        addSubscription(microStationSubscription);
    }

    /**
     * 获取应急单位数据
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceEmergencyUnitData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (emergencyUnitSubscription != null && !emergencyUnitSubscription.isUnsubscribed()) {
            removeSubscription(emergencyUnitSubscription);
            emergencyUnitSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_EMERGENCY_UNIT_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.EMERGENCY_UNIT_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        emergencyUnitSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceEmergencyUnit>>() {
                    @Override
                    public Observable<ResourceEmergencyUnit> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceEmergencyUnit> emergencyUnits = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    emergencyUnits = gson.fromJson(listBean, new TypeToken<List<ResourceEmergencyUnit>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + emergencyUnits.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || emergencyUnits == null || emergencyUnits.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(emergencyUnits);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mEmergencyUnitLists == null) {
                            mEmergencyUnitLists = new ArrayList<>();
                        }
                        mEmergencyUnitLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceEmergencyUnit>() {
                    @Override
                    public void call(ResourceEmergencyUnit resourceEmergencyUnit) {
                        mEmergencyUnitLists.add(resourceEmergencyUnit);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceEmergencyUnit>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mEmergencyUnitLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceEmergencyUnit resourceEmergencyUnit) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceEmergencyUnit.getGeometry().getCoordinates().get(1),
                                resourceEmergencyUnit.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkEmergencyUnitTarget(latLng, resourceEmergencyUnit);
                        }
                    }
                });
        addSubscription(emergencyUnitSubscription);
    }

    /**
     * 获取联勤单位数据
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceLogisticUnitData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (logisticUnitSubscription != null && !logisticUnitSubscription.isUnsubscribed()) {
            removeSubscription(logisticUnitSubscription);
            logisticUnitSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_LOGISTIC_UNIT_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.LOGISTIC_UNIT_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        logisticUnitSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceLogisticUnit>>() {
                    @Override
                    public Observable<ResourceLogisticUnit> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceLogisticUnit> logisticUnits = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    logisticUnits = gson.fromJson(listBean, new TypeToken<List<ResourceLogisticUnit>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + logisticUnits.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || logisticUnits == null || logisticUnits.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(logisticUnits);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mLogisticUnitLists == null) {
                            mLogisticUnitLists = new ArrayList<>();
                        }
                        mLogisticUnitLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceLogisticUnit>() {
                    @Override
                    public void call(ResourceLogisticUnit resourceLogisticUnit) {
                        mLogisticUnitLists.add(resourceLogisticUnit);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceLogisticUnit>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mLogisticUnitLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceLogisticUnit resourceLogisticUnit) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceLogisticUnit.getGeometry().getCoordinates().get(1),
                                resourceLogisticUnit.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkLogisticUnitTarget(latLng, resourceLogisticUnit);
                        }
                    }
                });
        addSubscription(logisticUnitSubscription);
    }

    /**
     * 获取高层建筑数据
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceHighBuildData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (highBuildSubscription != null && !highBuildSubscription.isUnsubscribed()) {
            removeSubscription(highBuildSubscription);
            highBuildSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_HIGH_BUILD_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.HIGH_BUILD_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        highBuildSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceHighBuild>>() {
                    @Override
                    public Observable<ResourceHighBuild> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceHighBuild> highBuilds = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    highBuilds = gson.fromJson(listBean, new TypeToken<List<ResourceHighBuild>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + highBuilds.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || highBuilds == null || highBuilds.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(highBuilds);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mHighBuildLists == null) {
                            mHighBuildLists = new ArrayList<>();
                        }
                        mHighBuildLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceHighBuild>() {
                    @Override
                    public void call(ResourceHighBuild resourceHighBuild) {
                        mHighBuildLists.add(resourceHighBuild);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceHighBuild>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mHighBuildLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceHighBuild resourceHighBuild) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceHighBuild.getGeometry().getCoordinates().get(1),
                                resourceHighBuild.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkHighBuildTarget(latLng, resourceHighBuild);
                        }
                    }
                });
        addSubscription(highBuildSubscription);
    }

    /**
     * 获取九小场所数据
     *
     * @param distance       距离目标地点的距离
     * @param longitude      经度
     * @param latitude       纬度
     * @param isShowProgress
     */
    @Override
    public void getResourceNineSmallPlaceData(double distance, double longitude, double latitude, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }
        //若正在订阅,则取消其
        if (nineSmallPlaceSubscription != null && !nineSmallPlaceSubscription.isUnsubscribed()) {
            removeSubscription(nineSmallPlaceSubscription);
            nineSmallPlaceSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.baseResourceURL, RequestMethod.POST);
        request.add("service", "WFS")
                .add("version", "1.0.0")
                .add("request", "GetFeature")
                .add("typeName", GET_NINE_SMALL_PLACE_TYPE)
                .add("maxFeatures", "999")
                .add("startIndex", "0")
                .add("outputFormat", "application/json")
                .add("sortBy", "ID")
                .add("srsName", "EPSG:4326")
                .add("viewparams", "X:" + longitude + ";Y:" + latitude)
                .add("CQL_FILTER", "1=1 AND DISTANCE <" + distance);

        request.setCacheKey(ConstData.NINE_SMALL_PLACE_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //异常等待对画框
        nineSmallPlaceSubscription = RxHttpUtils.request(request)
                .concatMap(new Func1<Response<String>, Observable<ResourceNineSmallPlace>>() {
                    @Override
                    public Observable<ResourceNineSmallPlace> call(Response<String> jsonObjectResponse) {
                        String result = jsonObjectResponse.get();
                        Gson gson = new Gson();
                        List<ResourceNineSmallPlace> nineSmallPlaces = null;
                        int totalNumber = 0;
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                totalNumber = jsonObject.getInt("totalFeatures");
                                Log.e(TAG, "call: " + totalNumber);
                                if (totalNumber != 0) {
                                    String listBean = jsonObject.getString("features");
                                    nineSmallPlaces = gson.fromJson(listBean, new TypeToken<List<ResourceNineSmallPlace>>() {
                                    }.getType());
                                    Log.e(TAG, "call: " + nineSmallPlaces.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (totalNumber == 0 || nineSmallPlaces == null || nineSmallPlaces.size() == 0) {
                            return Observable.error(new Throwable("暂无数据"));
                        }
                        return Observable.from(nineSmallPlaces);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mNineSmallPlaceLists == null) {
                            mNineSmallPlaceLists = new ArrayList<>();
                        }
                        mNineSmallPlaceLists.clear();
                    }
                })
                .doOnNext(new Action1<ResourceNineSmallPlace>() {
                    @Override
                    public void call(ResourceNineSmallPlace resourceNineSmallPlace) {
                        mNineSmallPlaceLists.add(resourceNineSmallPlace);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
//                        HydrantDao.getInstance().insertList(mHydrantLists, eventSign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ResourceNineSmallPlace>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (mNineSmallPlaceLists.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            }
                        }
                    }

                    @Override
                    public void onNext(ResourceNineSmallPlace resourceNineSmallPlace) {
                        LatLng latLng = PositionUtils.Gps84_To_bd09(resourceNineSmallPlace.getGeometry().getCoordinates().get(1),
                                resourceNineSmallPlace.getGeometry().getCoordinates().get(0));
                        if (isViewAttached()) {
                            mViewRef.get().onMarkNineSmallPlaceTarget(latLng, resourceNineSmallPlace);
                        }
                    }
                });
        addSubscription(nineSmallPlaceSubscription);
    }

    //在父类中的DetachView中已经执行了订阅关系的解除
    @Override
    public void onDetachView() {
        clearData();
    }

    //清除数据
    @Override
    public void clearData() {
        if (mHydrantLists != null && !mHydrantLists.isEmpty()) {
            mHydrantLists.clear();
        }
        if (mImportantUnitLists != null && !mImportantUnitLists.isEmpty()) {
            mImportantUnitLists.clear();
        }
        if (mFireOrganLists != null && !mFireOrganLists.isEmpty()) {
            mFireOrganLists.clear();
        }
        if (mMicroStationLists != null && !mMicroStationLists.isEmpty()) {
            mMicroStationLists.clear();
        }
        if (mLogisticUnitLists != null && !mLogisticUnitLists.isEmpty()) {
            mLogisticUnitLists.clear();
        }
        if (mEmergencyUnitLists != null && !mEmergencyUnitLists.isEmpty()) {
            mEmergencyUnitLists.clear();
        }
        if (mHighBuildLists != null && !mHighBuildLists.isEmpty()) {
            mHighBuildLists.clear();
        }
        if (mNineSmallPlaceLists != null && !mNineSmallPlaceLists.isEmpty()) {
            mNineSmallPlaceLists.clear();
        }
    }
}
