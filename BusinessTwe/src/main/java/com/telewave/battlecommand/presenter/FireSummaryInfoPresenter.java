package com.telewave.battlecommand.presenter;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.FireSummaryInfo;
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

public class FireSummaryInfoPresenter extends BasePresenter<IDirectionContract.FireSummaryView>
        implements IDirectionContract.IDisasterSummaryInfoPresenter {

    private static final String TAG = "FireSummaryInfoPresenter";

    private FireSummaryInfo summaryInfo = new FireSummaryInfo();

    private Subscription disasterSummarySubscription;

    public FireSummaryInfoPresenter(IDirectionContract.FireSummaryView view) {
        super(view);
    }

    public FireSummaryInfo getSummaryInfo() {
        return summaryInfo;
    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void getDisasterSummaryInfo(String organId, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterSummarySubscription != null && !disasterSummarySubscription.isUnsubscribed()) {
            removeSubscription(disasterSummarySubscription);
            disasterSummarySubscription = null;
        }

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFireSummaryDetail, RequestMethod.GET);
        request.add("alarmId", organId);

//        String url = request.url();
//        Log.e("NoHttpDebugTag", "火场总结详情----------" + url);
        request.setCacheKey(ConstData.DISASTER_DETAIL_FIRE_SUMMARY_CACHEKDY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterSummarySubscription = RxHttpUtils.request(request).
                map(new Func1<Response<String>, FireSummaryInfo>() {
                    @Override
                    public FireSummaryInfo call(Response<String> stringResponse) {
                        String result = stringResponse.get();
//                        Log.e(TAG, "call: " + result);
                        if (result != null) {
                            try {
                                ResponseBean responseBean = new ResponseBean(result);
                                if (responseBean.isSuccess()) {
                                    Gson gson = new Gson();
                                    summaryInfo = gson.fromJson(responseBean.getData(), FireSummaryInfo.class);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return summaryInfo;
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).doOnNext(new Action1<FireSummaryInfo>() {

            @Override
            public void call(FireSummaryInfo info) {
//                summaryInfo = info;
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<FireSummaryInfo>() {
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
                            if (summaryInfo == null) {
                                if (isShowProgress) {
                                    mViewRef.get().showErrorMsg("暂无数据");
                                }
                            } else {
                                mViewRef.get().onFireSummaryCompleted(summaryInfo);
                            }
                        }
                    }

                    @Override
                    public void onNext(FireSummaryInfo summaryInfo) {
                        if (isViewAttached()) {
//                    mViewRef.get().onFireSummaryCompleted(summaryInfo);
                        }
                    }
                });
        addSubscription(disasterSummarySubscription);
    }


    @Override
    public void clearData() {

    }

}
