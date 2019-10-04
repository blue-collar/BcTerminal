package com.telewave.lib.router.interceptor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.telewave.lib.router.RouterPath;

import java.lang.ref.WeakReference;

/**
 * @Author: rick_tan
 * @Date: 19-7-29
 * @Version: v1.0
 * @Des InterceptorActivity
 */
@Route(path = RouterPath.Path.PATH_INTERCEPTOR_ACTIVITY)
public class InterceptorActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        if (InterceptorUtil.instance().isValid()) {
            WeakReference<Activity> activityWeakReference = InterceptorUtil.instance().getActivity();
            if (activityWeakReference != null
                    && !InterceptorUtil.isActivityDestroy(activityWeakReference.get())
            ) {
                InterceptorUtil.instance().continueService(activityWeakReference.get());
            } else {
                InterceptorUtil.instance().continueService(this);

            }
        } else {
            InterceptorUtil.instance().clearCallback();
        }
        finish();
    }
}
