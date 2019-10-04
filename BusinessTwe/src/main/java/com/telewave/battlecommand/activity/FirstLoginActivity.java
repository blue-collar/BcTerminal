package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.router.RouterPath;
import com.telewave.lib.widget.util.StatusBarUtil;

@Route(path = RouterPath.LOGIN_FIRST_PATH)
public class FirstLoginActivity extends AppCompatActivity {

    //扫描二维码
    private LinearLayout scanCode;
    //输入连接地址
    private LinearLayout inputLinkAddress;
    //选择服务器连接
    private LinearLayout choiceServerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        StatusBarUtil.setTransparent(this);
        // 非正常启动流程，直接重新初始化应用界面
        if (AppProxy.APP_STATUS != AppProxy.APP_STATUS_NORMAL) {
            AppProxy.getInstance().getAbnormalStartCallback().rbnormalStartReInitApp();
            return;
        } else {
            //正常启动流程 子Activity初始化界面
            initView();
        }
    }

    private void initView() {
        scanCode = (LinearLayout) findViewById(R.id.first_login_scan);
        inputLinkAddress = (LinearLayout) findViewById(R.id.input_link_address);
        choiceServerLink = (LinearLayout) findViewById(R.id.choice_server_link);
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(FirstLoginActivity.this, ScanCodeActivity.class);
                startActivity(intentScan);
                finish();
            }
        });
        inputLinkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstLoginActivity.this, InputLinkAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });
        choiceServerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstLoginActivity.this, ChoiceServerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.toastShortMessage("再按一次程序将退到后台运行");
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
