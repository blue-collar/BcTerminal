package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.telewave.battlecommand.fragment.UnitDetailFragment;
import com.telewave.battlecommand.fragment.UnitFileFragment;
import com.telewave.battlecommand.fragment.UnitFlatFragment;
import com.telewave.battlecommand.fragment.UnitOutLineFragment;
import com.telewave.battlecommand.fragment.UnitOuterHydrantFragment;
import com.telewave.battlecommand.fragment.UnitOutwardFragment;
import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanUnitDetailActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private MyAdapter adapter;
    private List<String> tabs = new ArrayList<>();

    private String planOrganId;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_plan_unit_detail);

        initView();
        initData();
    }

    private void initView() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));
        //设置分割线
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData() {

        planOrganId = getIntent().getStringExtra("organId");

        tabs = Arrays.asList(new String[]{"信息", "概况图", "外围水源", "外观图", "平面图", "附件"});

        list = new ArrayList<>();
        list.add(new UnitDetailFragment(this, tabs.get(0), planOrganId));
        list.add(new UnitOutLineFragment(this, tabs.get(1), planOrganId));
        list.add(new UnitOuterHydrantFragment(this, tabs.get(2), planOrganId));
        list.add(new UnitOutwardFragment(this, tabs.get(3), planOrganId));
        list.add(new UnitFlatFragment(this, tabs.get(4), planOrganId));
        list.add(new UnitFileFragment(this, tabs.get(5), planOrganId));

        adapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);//关联ViewPager和TabLayout

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(5);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置TabLayout的模式
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
