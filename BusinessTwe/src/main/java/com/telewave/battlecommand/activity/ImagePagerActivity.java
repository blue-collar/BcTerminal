package com.telewave.battlecommand.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.telewave.battlecommand.adapter.HackyViewPager;
import com.telewave.battlecommand.adapter.ImageViewPagerAdapter;
import com.telewave.business.twe.R;

import java.util.List;

import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_INDEX;
import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_URLS;

/**
 * 图片查看
 */

public class ImagePagerActivity extends AppCompatActivity {

    private static final String STATE_POSITION = "STATE_POSITION";

    private HackyViewPager mPager;
    private ImageViewPagerAdapter mAdapter;
    private int pagerPosition;
    private TextView indicator;

    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        initView();

        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {

            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(pagerPosition);
    }


    protected void initView() {
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(R.id.view_pager_image);
        mAdapter = new ImageViewPagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }


}
