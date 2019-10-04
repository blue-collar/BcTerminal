package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.DisasterDetail;
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

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DisasterDetailPresenter extends BasePresenter<IDirectionContract.DisasterDetailView>
        implements IDirectionContract.IDisasterDetailInfoPresenter {

    private static final String TAG = "DisasterDetailPresenter";

    private DisasterDetail disasterDetailInfo = new DisasterDetail();

    private Subscription disasterDetailSubscription;

    public DisasterDetailPresenter(IDirectionContract.DisasterDetailView view) {
        super(view);
    }

    public DisasterDetail getDisasterDetailInfo() {
        return disasterDetailInfo;
    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void getDisasterDetailInfo(String organId, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterDetailSubscription != null && !disasterDetailSubscription.isUnsubscribed()) {
            removeSubscription(disasterDetailSubscription);
            disasterDetailSubscription = null;
        }

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getDisasterInfoDetail + organId, RequestMethod.GET);

//        String url = request.url();
//        Log.e("NoHttpDebugTag", "灾害详情----------" + url);
        request.setCacheKey(ConstData.DISASTER_DETAIL_TYPE);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterDetailSubscription = RxHttpUtils.request(request).map(new Func1<Response<String>, DisasterDetail>() {
            @Override
            public DisasterDetail call(Response<String> stringResponse) {
                String result = stringResponse.get();
                Log.e(TAG, "call: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            disasterDetailInfo = gson.fromJson(responseBean.getData(), DisasterDetail.class);
                            Log.e(TAG, "call11: " + disasterDetailInfo.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return disasterDetailInfo;
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).doOnNext(new Action1<DisasterDetail>() {

            @Override
            public void call(DisasterDetail disasterDetail) {
//                disasterDetailInfo = disasterDetail;
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<DisasterDetail>() {
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
                            if (disasterDetailInfo == null) {
                                if (isShowProgress) {
                                    mViewRef.get().showErrorMsg("暂无数据");
                                }
                            } else {
                                mViewRef.get().onDisasterDetailCompleted(disasterDetailInfo);
                            }
                        }
                    }

                    @Override
                    public void onNext(DisasterDetail disasterDetail) {
                        if (isViewAttached()) {
//                    mViewRef.get().onDisasterDetailCompleted(disasterDetail);
                        }
                    }
                });
        addSubscription(disasterDetailSubscription);
    }


    @Override
    public void clearData() {

    }
}
