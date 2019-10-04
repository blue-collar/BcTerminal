package com.telewave.battlecommand.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.battlecommand.bean.DisasterType;
import com.telewave.battlecommand.bean.ZhddZqxx;
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

public class DisasterInfoPresenter extends BasePresenter<IDirectionContract.IDisasterView>
        implements IDirectionContract.IDisasterInfoPresenter {

    private static final String TAG = "DisasterInfoPresenter";
    /**
     * 获取消火栓请求类型
     */
    public static final String GEY_DISASTER_TYPE_COUNT = "/TelewaveMFS/alarm/zqxx/countPage";

    private DisasterType mType = new DisasterType();
    private List<DisasterInfo> disasterInfoList = new ArrayList<>();

    //获取从服务器返回的总分页书
    private int pageTotalNum = 0;

    private Subscription disasterTypeSubscription;
    private Subscription disasterListSubscription;

    public DisasterType getmType() {
        return mType;
    }

    public List<DisasterInfo> getDisasterInfoList() {
        return disasterInfoList;
    }

    public int getPageTotalNum() {
        return pageTotalNum;
    }

    public DisasterInfoPresenter(IDirectionContract.IDisasterView view) {
        super(view);
    }


    @Override
    public void onDetachView() {

    }


    @Override
    public void getDisasterDetailInfo(String organId, boolean isShowProgress) {

    }

    @Override
    public void getDisasterTypeCount(String organid, String startTime, String endTime, final boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterTypeSubscription != null && !disasterTypeSubscription.isUnsubscribed()) {
            removeSubscription(disasterTypeSubscription);
            disasterTypeSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getDisasterTypeStats, RequestMethod.POST);

        Gson gson = new Gson();
        ZhddZqxx zhddZqxx = new ZhddZqxx();
        if (!TextUtils.isEmpty(organid)) {
            zhddZqxx.setOfficeId(organid);
        }
        if (!TextUtils.isEmpty(startTime)) {
            zhddZqxx.setBeginBjsj(startTime);
        }
        if (!TextUtils.isEmpty(endTime)) {
            zhddZqxx.setEndBjsj(endTime);
        }
//        if (!TextUtils.isEmpty(address)) {
//            zhddZqxx.setZhdd(address);
//        }
//        if(!TextUtils.isEmpty(type)){
//            zhddZqxx.setZqlxdm(type);
//        }
//        if(!TextUtils.isEmpty(state)){
//            zhddZqxx.setUnfinished(state);
//        }

        String jsonStr = gson.toJson(zhddZqxx);

        request.setDefineRequestBodyForJson(jsonStr);
        String url = request.url();
        Log.e("NoHttpDebugTag", "开始灾情统计----------" + url);
        request.setCacheKey(ConstData.DISASTER_LIST_TYPE);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterTypeSubscription = RxHttpUtils.request(request).
                map(new Func1<Response<String>, DisasterType>() {
                    @Override
                    public DisasterType call(Response<String> stringResponse) {
                        String result = stringResponse.get();
                        Log.e(TAG, "call: " + result);
                        if (result != null) {
                            try {
                                ResponseBean responseBean = new ResponseBean(result);
                                if (responseBean.isSuccess()) {
                                    Gson gson = new Gson();
                                    String data = responseBean.getData();
                                    mType = gson.fromJson(data, DisasterType.class);
//
                                    Log.e(TAG, "call11: " + mType.toString());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return mType;
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
//            if (mType == null) {
//                mType = new DisasterType();
//            }
            }
        }).doOnNext(new Action1<DisasterType>() {

            @Override
            public void call(DisasterType disasterType) {
//                mType = disasterType;
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<DisasterType>() {
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
                            if (mType == null) {
                                if (isShowProgress) {
                                    mViewRef.get().showErrorMsg("暂无数据");
                                }
                            } else {
                                mViewRef.get().onDisasterTypeLoadingCompleted(mType);
                            }
                        }
                    }

                    @Override
                    public void onNext(DisasterType disasterType) {
                        if (isViewAttached()) {
//                    mViewRef.get().onDisasterTypeLoadingCompleted(disasterType);
                        }
                    }
                });
        addSubscription(disasterTypeSubscription);
    }

    @Override
    public void getDisasterList(String organId, String startTime, String endTime, String address, String type, String state, int currentPage, int pageSize, boolean isShowProgress) {
        if (mViewRef == null || mViewRef.get() == null) {
            return;
        }
        if (isViewAttached() && isShowProgress) {
            mViewRef.get().showWaitDialog();
        }

        //若正在订阅,则取消其
        if (disasterListSubscription != null && !disasterListSubscription.isUnsubscribed()) {
            removeSubscription(disasterListSubscription);
            disasterListSubscription = null;
        }

        final Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getDisasterInfo, RequestMethod.POST);

        Gson gson = new Gson();
        ZhddZqxx zhddZqxx = new ZhddZqxx();
        if (!TextUtils.isEmpty(organId)) {
            zhddZqxx.setOfficeId(organId);
        }
        if (!TextUtils.isEmpty(startTime)) {
            zhddZqxx.setBeginBjsj(startTime);
        }
        if (!TextUtils.isEmpty(endTime)) {
            zhddZqxx.setEndBjsj(endTime);
        }
        if (!TextUtils.isEmpty(address)) {
            zhddZqxx.setZhdd(address);
        }
        if (!TextUtils.isEmpty(type)) {
            zhddZqxx.setZqlxdm(type);
        }
        if (!TextUtils.isEmpty(state)) {
            zhddZqxx.setUnfinished(state);
        }

        zhddZqxx.setPageNo(currentPage);
        zhddZqxx.setPageSize(pageSize);
        String jsonStr = gson.toJson(zhddZqxx);

        request.setDefineRequestBodyForJson(jsonStr);
        String url = request.url();
        Log.e("NoHttpDebugTag", "开始灾情列表----------" + url);
        request.setCacheKey(ConstData.DISASTER_LIST);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        disasterListSubscription = RxHttpUtils.request(request).
                concatMap(new Func1<Response<String>, Observable<DisasterInfo>>() {
                    @Override
                    public Observable<DisasterInfo> call(Response<String> stringResponse) {
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
                                        disasterInfoList = gson.fromJson(str, new TypeToken<List<DisasterInfo>>() {
                                        }.getType());

                                    } else {
                                        return Observable.error(new Throwable("暂无灾情数据"));
                                    }
                                    Log.e(TAG, "call11: " + disasterInfoList.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.from(disasterInfoList);
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
            }
        }).doOnNext(new Action1<DisasterInfo>() {

            @Override
            public void call(DisasterInfo disasterInfo) {
//                disasterInfoList.add(disasterInfo);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleRxSubscriber<DisasterInfo>() {
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
                            if (disasterInfoList.size() == 0) {
                                mViewRef.get().showErrorMsg("暂无数据");
                            } else {
                                mViewRef.get().onDisasterListCompleted(disasterInfoList);
                            }
                        }
                    }

                    @Override
                    public void onNext(DisasterInfo disasterInfo) {
                        if (isViewAttached()) {
//                    disasterInfoList.add(disasterInfo);
//                    mViewRef.get().onDisasterListCompleted(disasterInfoList);
                        }
                    }
                });
        addSubscription(disasterListSubscription);
    }

    @Override
    public void getDisasterInfoDetail(String organId, boolean isShowInfoWindow) {

    }

    @Override
    public void clearData() {

    }
}
