package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;

/**
 * 选择服务器地址
 *
 * @author liwh
 * @date 2019/1/4
 */
public class ChoiceServerActivity extends BaseActivity {

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_choice_server);
        initView();
    }

    private void initView() {
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceServerActivity.this, FirstLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(ChoiceServerActivity.this, FirstLoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
