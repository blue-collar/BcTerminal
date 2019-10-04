package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;
import com.tencent.qcloud.tim.uikit.utils.OpenAutoStartUtil;


/**
 * 不同手机系统
 * 运行APP 自启动 白名单 开启Activity
 *
 * @author liwh
 * @date 2019/05/22
 */
public class OpenAutoStartActivity extends BaseActivity {
    private static final String TAG = "OpenAutoStartActivity";
    private TextView tipsTextView;


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_open_auto_start);
        initView();
        String phoneTip = OpenAutoStartUtil.getMobileFriendTip();
        if (!TextUtils.isEmpty(phoneTip)) {
            tipsTextView.setText(phoneTip);
        }
    }

    public void initView() {
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.yunxing_set_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAutoStartUtil.goStartUpPage(OpenAutoStartActivity.this);
            }
        });
        tipsTextView = (TextView) findViewById(R.id.text_tips);
    }

}