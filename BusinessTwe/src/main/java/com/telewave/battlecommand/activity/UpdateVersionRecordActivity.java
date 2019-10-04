package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.telewave.battlecommand.view.X5WebView;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.widget.BaseActivity;


/**
 * 关于系统里面更新记录页面
 *
 * @author liwh
 * @date 2019/1/23
 */
public class UpdateVersionRecordActivity extends BaseActivity {
    private static final String TAG = "UpdateRecordActivity";
    private X5WebView mWebView;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_record);
        mWebView = (X5WebView) findViewById(R.id.x5webView);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });
        Log.e(TAG, "onCreate: " + ConstData.urlManager.getAppUpdateRecord + "?mcType=2");
        mWebView.loadUrl(ConstData.urlManager.getAppUpdateRecord + "?mcType=2");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}