package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.telewave.battlecommand.fragment.FireExpertListFragment;
import com.telewave.battlecommand.fragment.SocialExpertListFragment;
import com.telewave.battlecommand.imui.FragmentAdapter;
import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ExpertLibActivity extends BaseActivity {

    private TabLayout tl_navigation;
    private ViewPager vp_select;

    private CharSequence[] titles = {"消防专家", "社会专家"};
    private FragmentAdapter mAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private FireExpertListFragment mFireExpertListFragment;
    private SocialExpertListFragment mSocialExpertListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_expert_lib);

        initView();

//        initData();
    }

    private void initView() {
        tl_navigation = (TabLayout) findViewById(R.id.tl_navigation);
        vp_select = (ViewPager) findViewById(R.id.vp_select);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ll_searchExpert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExpertLibActivity.this, ExpertSearchActivity.class));
            }
        });

        vp_select.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFireExpertListFragment.initData();
                } else if (position == 1) {
                    mSocialExpertListFragment.initData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFireExpertListFragment = new FireExpertListFragment();
        mSocialExpertListFragment = new SocialExpertListFragment();
        fragments.add(mFireExpertListFragment);
        fragments.add(mSocialExpertListFragment);
//        mAdapter = new FragmentAdapter(getFragmentManager(), fragments, titles);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vp_select.setAdapter(mAdapter);
        tl_navigation.setupWithViewPager(vp_select);

    }


}
