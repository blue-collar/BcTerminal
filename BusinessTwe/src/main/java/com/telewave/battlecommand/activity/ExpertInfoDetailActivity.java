package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.telewave.battlecommand.bean.ExpertInfo;
import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;


public class ExpertInfoDetailActivity extends BaseActivity {

    private TextView mTvName;
    private TextView mTvSfz;
    private TextView mTvBirthDate;
    private TextView mTvSex;
    private TextView mTvBirthPlace;
    private TextView mTvUnit;
    private TextView mTvPosition;
    private TextView mTvEducation;
    private TextView mTvField;
    private TextView mTvAddress;
    private TextView mTvPostCode;
    private TextView mTvPostAddress;
    private TextView mTvHometelephone;
    private TextView mTvMobilePhone;
    private TextView mTvOfficePhone;

    private ExpertInfo expertInfo;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_expert_info_detail);

        initView();
        initData();
    }

    private void initView() {
        mTvName = findViewById(R.id.expert_name);
        mTvSfz = findViewById(R.id.expert_sfzh);
        mTvBirthDate = findViewById(R.id.expert_birth_date);
        mTvSex = findViewById(R.id.expert_sex);
        mTvBirthPlace = findViewById(R.id.expert_birth_place);
        mTvUnit = findViewById(R.id.expert_unit);
        mTvPosition = findViewById(R.id.expert_position);
        mTvEducation = findViewById(R.id.expert_education);
        mTvField = findViewById(R.id.expert_field);
        mTvAddress = findViewById(R.id.expert_address);
        mTvPostCode = findViewById(R.id.expert_post_code);
        mTvPostAddress = findViewById(R.id.expert_post_address);
        mTvHometelephone = findViewById(R.id.expert_home_telephone);
        mTvMobilePhone = findViewById(R.id.expert_mobile_phone);
        mTvOfficePhone = findViewById(R.id.expert_office_phone);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

        expertInfo = (ExpertInfo) getIntent().getSerializableExtra("ExpertInfo");
        if (expertInfo != null) {
            mTvName.setText(expertInfo.getXm() != null ? expertInfo.getXm() : "");
            mTvSfz.setText(expertInfo.getSfzh() != null ? expertInfo.getSfzh() : "");
            mTvBirthDate.setText(expertInfo.getCsrq() != null ? expertInfo.getCsrq() : "");
            mTvSex.setText(expertInfo.getXb() != null ? expertInfo.getXb() : "");
            mTvBirthPlace.setText(expertInfo.getJg() != null ? expertInfo.getJg() : "");
            mTvUnit.setText(expertInfo.getSsdw() != null ? expertInfo.getSsdw() : "");
            mTvPosition.setText(expertInfo.getZw() != null ? expertInfo.getZw() : "");
            mTvEducation.setText(expertInfo.getXl() != null ? expertInfo.getXl() : "");
            mTvAddress.setText(expertInfo.getZd() != null ? expertInfo.getZd() : "");
            mTvPostCode.setText(expertInfo.getYzbm() != null ? expertInfo.getYzbm() : "");
            mTvPostAddress.setText(expertInfo.getTxdz() != null ? expertInfo.getTxdz() : "");
            mTvHometelephone.setText(expertInfo.getJtdh() != null ? expertInfo.getJtdh() : "");
            mTvMobilePhone.setText(expertInfo.getYddh() != null ? expertInfo.getYddh() : "");
            mTvOfficePhone.setText(expertInfo.getBgdh() != null ? expertInfo.getBgdh() : "");

            if (expertInfo.getShzjly() != null && expertInfo.getShzjly().getZjlydm() != null) {
                mTvField.setText(expertInfo.getShzjly().getZjlydm().getCodeName());
            }
        }
    }
}
