package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.FightPicInfo;
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

public class DisasterFightPicPresenter extends BasePresenter<IDirectionContract.FightPicView>
        implements IDirectionContract.IDisasterFightPicPresenter {

    private static final String TAG = "DisasterDetailPresenter";

    private List<FightPicInfo> disasterFightPicList = new ArrayList<>();

    private Subscription disasterFightPicSubcription;

    public DisasterFightPicPresenter(IDirectionContract.FightPicView view) {
        super(view);
    }

    public List<FightPicInfo> getDisasterFightPicList() {
        return disasterFightPicList;
    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void getDisasterFightPicInfo(String organId, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterFightPicSubcription != null && !disasterFightPicSubcription.isUnsubscribed()) {
            removeSubscription(disasterFightPicSubcription);
            disasterFightPicSubcription = null;
        }

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFightPicList, RequestMethod.GET);

        request.add("zqid", organId);

//        String url = request.url();
        request.setCacheKey(ConstData.DISASTER_DETAIL_FIGHT_PIC_CACHEKDY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterFightPicSubcription = RxHttpUtils.request(request).
                concatMap(new Func1<Response<String>, Observable<FightPicInfo>>() {
                    @Override
                    public Observable<FightPicInfo> call(Response<String> stringResponse) {
                        String result = stringResponse.get();
                        Log.e(TAG, "call: " + result);
//                        List<FightPicInfo> list = null;

                        if (result != null) {
                            try {
                                ResponseBean responseBean = new ResponseBean(result);
                                if (responseBean.isSuccess()) {
                                    Gson gson = new Gson();
                                    disasterFightPicList = gson.fromJson(responseBean.getData(), new TypeToken<List<FightPicInfo>>() {
                                    }.getType());
                                    if (disasterFightPicList == null) {
                                        return Observable.error(new Throwable("暂无作战图数据"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.from(disasterFightPicList);
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).doOnNext(new Action1<FightPicInfo>() {

            @Override
            public void call(FightPicInfo picInfo) {
//                disasterFightPicList.add(picInfo);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<FightPicInfo>() {
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
                            if (isShowProgress) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            } else {
                                if (disasterFightPicList != null) {
                                    mViewRef.get().onFightPicCompleted(disasterFightPicList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(FightPicInfo picInfo) {
                        if (isViewAttached()) {
//                    mViewRef.get().onFightPicCompleted(disasterDetail);
                        }
                    }
                });
        addSubscription(disasterFightPicSubcription);
    }


    @Override
    public void clearData() {

    }

}
