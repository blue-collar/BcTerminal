package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.telewave.battlecommand.bean.NineSmallPlace;
import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;


/**
 * 九小场所详情
 *
 * @author zhangjun
 * @date 2019-08-07
 */
public class NinePlaceDetailActivity extends BaseActivity {

    private TextView mTvName, mTvAddress, mTvUnitType, mTvLongitudeY, mTvLatitudeX, mTvServiceScope, mTvFax, mTvDutyPhone, mTvOrgan;
    private NineSmallPlace info;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_nine_place_detail);

        initView();
        initData();
    }

    private void initView() {

        mTvName = (TextView) findViewById(R.id.nine_place_name);
        mTvAddress = (TextView) findViewById(R.id.nine_place_address);
        mTvUnitType = (TextView) findViewById(R.id.nine_place_unit_type);
        mTvLongitudeY = (TextView) findViewById(R.id.nine_place_longitude);
        mTvLatitudeX = (TextView) findViewById(R.id.nine_place_latitude);
        mTvServiceScope = (TextView) findViewById(R.id.nine_place_service_scope);
        mTvFax = (TextView) findViewById(R.id.nine_place_fax);
        mTvDutyPhone = (TextView) findViewById(R.id.nine_place_duty_phone);
        mTvOrgan = (TextView) findViewById(R.id.nine_place_organ);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        info = (NineSmallPlace) getIntent().getSerializableExtra("NineSmallPlace");
        if (info != null) {
            mTvName.setText(info.getPlaceName());
            mTvAddress.setText(info.getAddress());
            mTvUnitType.setText(info.getUnitType());
            mTvLongitudeY.setText(info.getDecimalY());
            mTvLatitudeX.setText(info.getLatitudeX());
            mTvServiceScope.setText(info.getServiceScope());
            mTvFax.setText(info.getUnitFax());
            mTvDutyPhone.setText(info.getDutyPhone());
            mTvOrgan.setText(info.getOrgan().getName());
        }

    }
}
