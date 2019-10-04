package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.PlanUnitDetail;
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

public class UnitDetailPresenter extends BasePresenter<IDirectionContract.IPlanUnitDetailView>
        implements IDirectionContract.IPlanUnitDetailPresenter {

    private static final String TAG = "UnitDetailPresenter";

    private PlanUnitDetail unitDetail = new PlanUnitDetail();

    private Subscription planDetailSubscription;

    public UnitDetailPresenter(IDirectionContract.IPlanUnitDetailView view) {
        super(view);
    }

    public PlanUnitDetail getUnitDetail() {
        return unitDetail;
    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void getPlanUnitDetailInfo(String organId, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (planDetailSubscription != null && !planDetailSubscription.isUnsubscribed()) {
            removeSubscription(planDetailSubscription);
            planDetailSubscription = null;
        }

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getPlanUnitDetail + organId, RequestMethod.GET);

//        String url = request.url();
//        Log.e("NoHttpDebugTag", "单位详情----------" + url);
        request.setCacheKey(ConstData.KNOWLEDGE_UNIT_DETAIL_CACHEKDY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        planDetailSubscription = RxHttpUtils.request(request).map(new Func1<Response<String>, PlanUnitDetail>() {
            @Override
            public PlanUnitDetail call(Response<String> stringResponse) {
                String result = stringResponse.get();
                Log.e(TAG, "call: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            unitDetail = gson.fromJson(responseBean.getData(), PlanUnitDetail.class);
                            Log.e(TAG, "call11: " + unitDetail.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return unitDetail;
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).doOnNext(new Action1<PlanUnitDetail>() {

            @Override
            public void call(PlanUnitDetail unitDetail) {
//                disasterDetailInfo = disasterDetail;
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<PlanUnitDetail>() {
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
                            if (unitDetail == null) {
                                if (isShowProgress) {
                                    mViewRef.get().showErrorMsg("暂无数据");
                                }
                            } else {
                                mViewRef.get().onPlanUnitDetailCompleted(unitDetail);
                            }
                        }
                    }

                    @Override
                    public void onNext(PlanUnitDetail unitDetail) {
                        if (isViewAttached()) {
//                    mViewRef.get().onDisasterDetailCompleted(disasterDetail);
                        }
                    }
                });
        addSubscription(planDetailSubscription);
    }


    @Override
    public void clearData() {

    }

}
