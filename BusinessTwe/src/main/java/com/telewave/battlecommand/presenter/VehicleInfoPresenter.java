package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.VehicleDispatch;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.http.RxHttpUtils;
import com.telewave.battlecommand.http.SimpleRxSubscriber;
import com.telewave.lib.base.ConstData;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class VehicleInfoPresenter extends BasePresenter<IDirectionContract.VehicleView>
        implements IDirectionContract.IDisasterVehicleInfoPresenter {

    private static final String TAG = "VehicleInfoPresenter";

    private List<VehicleDispatch> vehicleDispatchList = new ArrayList<>();

    private Subscription disasterVehicleSubscription;

    public VehicleInfoPresenter(IDirectionContract.VehicleView view) {
        super(view);
    }


    public List<VehicleDispatch> getVehicleDispatchList() {
        return vehicleDispatchList;
    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void getDisasterVehicleInfo(String organId, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterVehicleSubscription != null && !disasterVehicleSubscription.isUnsubscribed()) {
            removeSubscription(disasterVehicleSubscription);
            disasterVehicleSubscription = null;
        }

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getVehicleList, RequestMethod.GET);
        request.add("alarmId", organId);

//        String url = request.url();
//        Log.e("NoHttpDebugTag", "车辆详情----------" + url);
        request.setCacheKey(ConstData.DISASTER_DETAIL_VEHICLE_CACHEKDY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterVehicleSubscription = RxHttpUtils.request(request).
                concatMap(new Func1<Response<String>, Observable<VehicleDispatch>>() {
                    @Override
                    public Observable<VehicleDispatch> call(Response<String> stringResponse) {
                        String result = stringResponse.get();
                        Log.e(TAG, "call: " + result);
                        if (result != null) {
                            try {
                                ResponseBean responseBean = new ResponseBean(result);
                                if (responseBean.isSuccess()) {
                                    Gson gson = new Gson();
                                    vehicleDispatchList = gson.fromJson(responseBean.getData(), new TypeToken<List<VehicleDispatch>>() {
                                    }.getType());
                                    if (vehicleDispatchList == null || vehicleDispatchList.size() == 0) {
                                        return Observable.error(new Throwable("暂无车辆数据"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.from(vehicleDispatchList);
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).doOnNext(new Action1<VehicleDispatch>() {

            @Override
            public void call(VehicleDispatch vehicleDispatch) {
//                vehicleDispatchList.add(vehicleDispatch);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<VehicleDispatch>() {
                    @Override
                    protected void dismissDialogOnError() {
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            mViewRef.get().showErrorMsg("暂无数据");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //异常等待对画框
                        if (isViewAttached()) {
                            mViewRef.get().dismissWaitDialog();
                            if (vehicleDispatchList == null || vehicleDispatchList.size() == 0) {
                                if (isShowProgress) {
                                    mViewRef.get().showErrorMsg("暂无数据");
                                }
                            } else {
                                mViewRef.get().onVehicleCompleted(vehicleDispatchList);
                            }
                        }
                    }

                    @Override
                    public void onNext(VehicleDispatch vehicleDispatch) {
                        if (isViewAttached()) {
//                    mViewRef.get().onVehicleCompleted(vehicleDispatch);
                        }
                    }
                });
        addSubscription(disasterVehicleSubscription);
    }


    @Override
    public void clearData() {

    }


}
