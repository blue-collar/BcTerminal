package com.telewave.battlecommand.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Note: 基础的代理类
 * Created by Yuan on 2016/10/28.
 */

public abstract class BasePresenter<T> {
    protected Reference<T> mViewRef;
    protected CompositeSubscription mSubscriptions;

    public BasePresenter() {
    }

    public BasePresenter(T view) {
        mViewRef = new WeakReference<T>(view);
    }

//    public void attachView(T view)
//    {
//        mViewRef = new WeakReference<T>(view);
//    }

    protected T getView() {
        return mViewRef.get();
    }

    protected void addSubscription(Subscription subscrption) {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscrption);
    }

    /**
     * 移除订阅
     *
     * @param subscription 订阅消息
     */
    protected void removeSubscription(Subscription subscription) {
        if (mSubscriptions != null && subscription != null) {
            mSubscriptions.remove(subscription);
        }
    }

    /**
     * 判断是否已经和view产生了关联
     *
     * @return
     */
    public boolean isViewAttached() {
        return mViewRef != null && getView() != null;
    }

    /**
     * 在view的onDestroy中要调用这个方法
     */
    public void detachView() {
        if (isViewAttached()) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (mSubscriptions != null && mSubscriptions.hasSubscriptions()) {
            mSubscriptions.clear();
        }
    }


    /**
     * 销毁工作,一般来说是销毁一些list数据等,至于订阅,已经在父类中实现了
     */
    public abstract void onDetachView();

}