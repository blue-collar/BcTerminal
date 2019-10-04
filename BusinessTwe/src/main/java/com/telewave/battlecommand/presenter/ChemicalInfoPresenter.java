package com.telewave.battlecommand.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.ChemicalInfo;
import com.telewave.battlecommand.bean.SmDcmaterialParam;
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

public class ChemicalInfoPresenter extends BasePresenter<IDirectionContract.IChemicalInfoView>
        implements IDirectionContract.IChemicalInfoPresenter {

    private static final String TAG = ChemicalInfoPresenter.class.getSimpleName();


    private List<ChemicalInfo> chemicalInfoList = new ArrayList<>();

    //获取从服务器返回的总分页书
    private int pageTotalNum = 0;

    private Subscription chemicalListSubscription;

    public List<ChemicalInfo> getChemicalInfoList() {
        return chemicalInfoList;
    }

    public int getPageTotalNum() {
        return pageTotalNum;
    }

    public ChemicalInfoPresenter(IDirectionContract.IChemicalInfoView view) {
        super(view);
    }


    @Override
    public void onDetachView() {

    }


    @Override
    public void getChemicalList(String chineseName, int currentPage, int pageSize, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (chemicalListSubscription != null && !chemicalListSubscription.isUnsubscribed()) {
            removeSubscription(chemicalListSubscription);
            chemicalListSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getChemicalList, RequestMethod.POST);

        SmDcmaterialParam tempInfo = new SmDcmaterialParam();
        if (!TextUtils.isEmpty(chineseName)) {
            tempInfo.setCname(chineseName);
        }
        if (currentPage != 0) {
            tempInfo.setPageNo(currentPage);
        }
        if (pageSize != 0) {
            tempInfo.setPageSize(pageSize);
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(tempInfo);

//        Log.e("NoHttpDebugTag", "开始危化品列表请求----------" + jsonStr);
        request.setDefineRequestBodyForJson(jsonStr);

        request.setCacheKey(ConstData.KNOWLEDGE_CHEMICAL_CACHEKDY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        chemicalListSubscription = RxHttpUtils.request(request).
                concatMap(new Func1<Response<String>, Observable<ChemicalInfo>>() {
                    @Override
                    public Observable<ChemicalInfo> call(Response<String> stringResponse) {
                        String result = stringResponse.get();
                        Log.e(TAG, "call: " + result);
                        if (result != null) {
                            try {
                                ResponseBean responseBean = new ResponseBean(result);
                                if (responseBean.isSuccess()) {
                                    Gson gson = new Gson();
                                    String data = responseBean.getData();
                                    JSONObject obj = new JSONObject(data);
                                    String count = obj.getString("count");
                                    pageTotalNum = obj.getInt("last");
                                    if (!count.equals("0")) {
                                        String str = obj.getString("list");
                                        chemicalInfoList = gson.fromJson(str, new TypeToken<List<ChemicalInfo>>() {
                                        }.getType());

                                    } else {
                                        return Observable.error(new Throwable("暂无危化品数据"));
                                    }
                                    Log.e(TAG, "call11: " + chemicalInfoList.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.from(chemicalInfoList);
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
            }
        }).doOnNext(new Action1<ChemicalInfo>() {

            @Override
            public void call(ChemicalInfo chemicalInfo) {

            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ChemicalInfo>() {
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
                            if (chemicalInfoList.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            } else {
                                mViewRef.get().onChemicalListCompleted(chemicalInfoList);
                            }
                        }
                    }

                    @Override
                    public void onNext(ChemicalInfo chemicalInfo) {
                        if (isViewAttached()) {

                        }
                    }
                });
        addSubscription(chemicalListSubscription);
    }


    @Override
    public void clearData() {

    }

}
