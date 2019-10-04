package com.telewave.battlecommand.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.ImportUnit;
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

public class PlanUnitInfoPresenter extends BasePresenter<IDirectionContract.IPlanUnitView>
        implements IDirectionContract.IPlanUnitInfoPresenter {

    private static final String TAG = PlanUnitInfoPresenter.class.getSimpleName();


    private List<ImportUnit> unitInfoList = new ArrayList<>();

    //获取从服务器返回的总分页书
    private int pageTotalNum = 0;

    private Subscription unitListSubscription;

    public List<ImportUnit> getUnitInfoList() {
        return unitInfoList;
    }

    public int getPageTotalNum() {
        return pageTotalNum;
    }

    public PlanUnitInfoPresenter(IDirectionContract.IPlanUnitView view) {
        super(view);
    }


    @Override
    public void onDetachView() {

    }


    @Override
    public void getPlanUnitInfo(String organId, String name, int currentPage, int pageSize, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (unitListSubscription != null && !unitListSubscription.isUnsubscribed()) {
            removeSubscription(unitListSubscription);
            unitListSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getPlanUnitList, RequestMethod.GET);

        request.add("objname", name);
        request.add("officeId", organId);
        request.add("pageNo", currentPage);
        request.add("pageSize", pageSize);
        request.setCacheKey(ConstData.KNOWLEDGE_UNIT_PLAN_CACHEKDY);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        unitListSubscription = RxHttpUtils.request(request).
                concatMap(new Func1<Response<String>, Observable<ImportUnit>>() {
                    @Override
                    public Observable<ImportUnit> call(Response<String> stringResponse) {
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
                                        unitInfoList = gson.fromJson(str, new TypeToken<List<ImportUnit>>() {
                                        }.getType());

                                    } else {
                                        return Observable.error(new Throwable("暂无灾情数据"));
                                    }
                                    Log.e(TAG, "call11: " + unitInfoList.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.from(unitInfoList);
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
            }
        }).doOnNext(new Action1<ImportUnit>() {

            @Override
            public void call(ImportUnit unit) {
//                disasterInfoList.add(disasterInfo);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<ImportUnit>() {
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
                            if (unitInfoList.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            } else {
                                mViewRef.get().onPlanUnitListCompleted(unitInfoList);
                            }
                        }
                    }

                    @Override
                    public void onNext(ImportUnit disasterInfo) {
                        if (isViewAttached()) {
//                    disasterInfoList.add(disasterInfo);
//                    mViewRef.get().onDisasterListCompleted(disasterInfoList);
                        }
                    }
                });
        addSubscription(unitListSubscription);
    }


    @Override
    public void clearData() {

    }

}
