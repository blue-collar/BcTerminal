package com.telewave.battlecommand.imui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.business.twe.R;


/**
 * 文件预览
 *
 * @author PF-NAN
 * @date 2019-07-28
 */
public class IMFilePreviewActivity extends BaseActivity {
    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_filepreview);
        initView();

        initEvent();
    }

    private void initView() {
        TextView tv_fileName = findViewById(R.id.tv_fileName);
        TextView tv_fileDes = findViewById(R.id.tv_fileDes);
        String fileName = getIntent().getStringExtra(IMKeys.INTENT_TAG);
        String fileDes = getIntent().getStringExtra(IMKeys.INTENT_DES);
        tv_fileName.setText(null == fileName ? "" : fileName);
        tv_fileDes.setText(TextUtils.concat("文件路径：", null == fileDes ? "" : fileDes));

    }

    private void initEvent() {
        findViewById(R.id.ll_filePreviewReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
