package com.telewave.battlecommand.http;


import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import rx.Observable;
import rx.Subscriber;

/**
 * Note: RxHttp请求
 * Created by Yuan on 2016/10/29,13:33.
 */

public class RxHttpUtils {
    private static final String TAG = "RxHttpUtils";

    public static <T> Observable<Response<T>> request(final Request<T> request) {
        return Observable.create(new Observable.OnSubscribe<Response<T>>() {
            @Override
            public void call(Subscriber<? super Response<T>> subscriber) {
                Response<T> response = NoHttp.startRequestSync(request);
                if (response.isSucceed() && response.get() != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(response.getException());
                }
                subscriber.onCompleted();
            }
        });
    }

    public static <T> Observable<Response<T>> requestWithNoReportError(final Request<T> request) {
        return Observable.create(new Observable.OnSubscribe<Response<T>>() {
            @Override
            public void call(Subscriber<? super Response<T>> subscriber) {
                Response<T> response = NoHttp.startRequestSync(request);
                if (response.isSucceed() && response.get() != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
