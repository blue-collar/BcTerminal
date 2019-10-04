package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.NewFireDocument;
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

public class FireDocumentInfoPresenter extends BasePresenter<IDirectionContract.FireDocumentView>
        implements IDirectionContract.IDisasterFireDocumentInfoPresenter {

    private static final String TAG = "FireDocumentInfoPresenter";

    private List<NewFireDocument> fireDocumentList = new ArrayList<>();

    private Subscription disasterDocumentSubcription;

    public FireDocumentInfoPresenter(IDirectionContract.FireDocumentView view) {
        super(view);
    }

    public List<NewFireDocument> getFireDocumentList() {
        return fireDocumentList;
    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void getDisasterFireDocumentInfo(String organId, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterDocumentSubcription != null && !disasterDocumentSubcription.isUnsubscribed()) {
            removeSubscription(disasterDocumentSubcription);
            disasterDocumentSubcription = null;
        }

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFireDocumentDetail, RequestMethod.GET);

        request.add("alarmId", organId);

        String url = request.url();
        Log.e("NoHttpDebugTag", "火场文书详情----------" + url);
        request.setCacheKey(ConstData.DISASTER_DETAIL_FIRE_DOCUMENT_CACHEKEY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterDocumentSubcription = RxHttpUtils.request(request).
                concatMap(new Func1<Response<String>, Observable<NewFireDocument>>() {
                    @Override
                    public Observable<NewFireDocument> call(Response<String> stringResponse) {
                        String result = stringResponse.get();
                        if (result != null) {
                            try {
                                ResponseBean responseBean = new ResponseBean(result);
                                if (responseBean.isSuccess()) {
                                    Gson gson = new Gson();
                                    fireDocumentList = gson.fromJson(responseBean.getData(), new TypeToken<List<NewFireDocument>>() {
                                    }.getType());
                                    if (fireDocumentList == null) {
                                        return Observable.error(new Throwable("暂无火场文书数据"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.from(fireDocumentList);
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
            }
        }).doOnNext(new Action1<NewFireDocument>() {

            @Override
            public void call(NewFireDocument fireDocument) {
//                fireDocumentList.add(fireDocument);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<NewFireDocument>() {
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
                            if (fireDocumentList == null || fireDocumentList.size() == 0) {
                                if (isShowProgress) {
                                    mViewRef.get().showErrorMsg("暂无数据");
                                }
                            } else {
                                mViewRef.get().onFireDocumentCompleted(fireDocumentList);
                            }
                        }
                    }

                    @Override
                    public void onNext(NewFireDocument fireDocument) {
                        if (isViewAttached()) {
//                    mViewRef.get().onFireDocumentCompleted(disasterDetail);
                        }
                    }
                });
        addSubscription(disasterDocumentSubcription);
    }


    @Override
    public void clearData() {

    }

}
