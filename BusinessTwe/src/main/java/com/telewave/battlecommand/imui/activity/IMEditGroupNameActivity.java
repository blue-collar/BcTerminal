package com.telewave.battlecommand.imui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.business.twe.R;


/**
 * 修改群组昵称
 *
 * @author PF-NAN
 * @date 2019-07-24
 */
public class IMEditGroupNameActivity extends BaseActivity {

    private String groupId;
    private String groupName;
    private TextView et_groupName;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_edit_groupname);
        groupId = getIntent().getStringExtra(IMKeys.INTENT_ID);
        groupName = getIntent().getStringExtra(IMKeys.INTENT_TAG);
        initView();
        initEvent();
    }

    private void initView() {
        et_groupName = findViewById(R.id.et_groupName);
        et_groupName.setHint(groupName);
    }

    private void initEvent() {
        findViewById(R.id.ll_editNameReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = et_groupName.getText().toString().trim();
                if (!TextUtils.isEmpty(groupName)) {
                    Intent intent = new Intent();
                    intent.putExtra(IMKeys.INTENT_TAG, groupName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
