package com.telewave.battlecommand.imui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;


/**
 * fragment适配器
 *
 * @author PF-NAN
 * @date 2017/12/20.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private CharSequence[] mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, CharSequence[] titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        container.invalidate();
        super.finishUpdate(container);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (null == mTitles) {
            return super.getPageTitle(position);
        } else {
            return mTitles[position];
        }
    }
}
