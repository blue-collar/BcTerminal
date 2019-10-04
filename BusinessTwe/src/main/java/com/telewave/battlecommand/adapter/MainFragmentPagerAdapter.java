package com.telewave.battlecommand.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 主页面Fragment Adapter
 */
public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentsList;

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public Fragment getItem(int location) {
        return fragmentsList.get(location);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    // 初始化每个页卡选项
    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        return super.instantiateItem(arg0, arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
