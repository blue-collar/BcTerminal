package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.battlecommand.fragment.DisasterDetailFragment;
import com.telewave.battlecommand.fragment.FightPicFragment;
import com.telewave.battlecommand.fragment.FireDocumentFragment;
import com.telewave.battlecommand.fragment.FireSummaryFragment;
import com.telewave.battlecommand.fragment.VehicleFragment;
import com.telewave.business.twe.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisasterDetailActivity extends BaseStatusActivity {

    private TabLayout tabLayout;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private MyAdapter adapter;
    private List<String> tabs;
    private String disasterId;
    private DisasterInfo disasterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_disaster_detail;
    }

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        super.setUpViewAndData(savedInstanceState);
//        setContentView(getLayoutId());
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
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider));//设置分割线的样式
//        linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

        disasterId = getIntent().getStringExtra("disasterId");
        disasterInfo = (DisasterInfo) getIntent().getSerializableExtra("DisasterInfo");
        tabs = new ArrayList<>();
        tabs = Arrays.asList(new String[]{"警情详情", "出动车辆", "火场文书", "火场总结", "作战图"});

        list = new ArrayList<>();
        list.add(new DisasterDetailFragment(this, tabs.get(0), disasterId, disasterInfo));
        list.add(new VehicleFragment(this, tabs.get(1), disasterId));
        list.add(new FireDocumentFragment(this, tabs.get(2), disasterId));
        list.add(new FireSummaryFragment(this, tabs.get(3), disasterId));
        list.add(new FightPicFragment(this, tabs.get(4), disasterId));

        adapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);//关联ViewPager和TabLayout

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);
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
            //super.destroyItem(container, position, object);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
