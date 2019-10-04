package com.telewave.battlecommand.imui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.imui.FragmentAdapter;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.fragment.IMForwardingSelectContactFragment;
import com.telewave.battlecommand.imui.fragment.IMForwardingSelectGroupFragment;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 转发选择人员或者群组
 *
 * @author PF-NAN
 * @date 2019-07-30
 */
public class IMForwardingSelectActivity extends BaseActivity {

    private TabLayout tl_navigation;
    private ViewPager vp_select;
    private IMForwardingSelectContactFragment imForwardingSelectContactFragment;
    private IMForwardingSelectGroupFragment imForwardingSelectGroupFragment;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_forwarding_select);
        tl_navigation = findViewById(R.id.tl_navigation);
        vp_select = findViewById(R.id.vp_select);
        initEvent();
        initData();
    }

    private void initData() {
        ConstData.userSig = SharePreferenceUtils.getDataSharedPreferences(this, "userSig");
        CharSequence[] titles = {"联系人", "群组"};
        List<Fragment> fragments = new ArrayList<>();
        imForwardingSelectContactFragment = new IMForwardingSelectContactFragment();
        fragments.add(imForwardingSelectContactFragment);
        imForwardingSelectGroupFragment = new IMForwardingSelectGroupFragment();
        fragments.add(imForwardingSelectGroupFragment);
        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vp_select.setAdapter(mAdapter);
        tl_navigation.setupWithViewPager(vp_select);
        imForwardingSelectContactFragment.initData();

    }

    private void initEvent() {
        findViewById(R.id.ll_editNameReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vp_select.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imForwardingSelectContactFragment.initData();
                } else if (position == 1) {
                    imForwardingSelectGroupFragment.initData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 转发处理
     *
     * @param conversionId
     * @param isC2C
     */
    public void forwardingMessage(String conversionId, boolean isC2C) {
        if (!TextUtils.isEmpty(conversionId)) {
            Intent intent = new Intent();
            intent.putExtra(IMKeys.INTENT_ID, conversionId);
            intent.putExtra(IMKeys.INTENT_TAG, isC2C);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }
}
