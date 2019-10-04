package com.telewave.battlecommand.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telewave.battlecommand.imui.FragmentAdapter;
import com.telewave.battlecommand.imui.activity.IMSearchContactActivity;
import com.telewave.battlecommand.imui.fragment.IMContactFragment;
import com.telewave.battlecommand.imui.fragment.IMGroupListFragment;
import com.telewave.battlecommand.imui.fragment.IMSessionFragment;
import com.telewave.battlecommand.imui.fragment.IMWZContactListFragment;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 通讯录
 *
 * @author liwh
 * @date 2018/12/12
 */
public class ContactListFragment extends Fragment {

    private TabLayout tl_navigation;
    private ViewPager vp_select;


    private View mView;
    private IMSessionFragment mIMSessionFragment;
    private IMContactFragment mIMContactListFragment;
    private IMWZContactListFragment mIMWZContactListFragment;
    private IMGroupListFragment mIMGroupListFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initView();
        return mView;
    }

    private void initView() {
        ConstData.userSig = SharePreferenceUtils.getDataSharedPreferences(getContext(), "userSig");
        tl_navigation = mView.findViewById(R.id.tl_navigation);
        vp_select = mView.findViewById(R.id.vp_select);
        initEvent();
        CharSequence[] titles = {"消息", "联系人", "微站联系人", "群组"};
        mIMSessionFragment = new IMSessionFragment();
        mIMContactListFragment = new IMContactFragment();
        mIMWZContactListFragment = new IMWZContactListFragment();
        mIMGroupListFragment = new IMGroupListFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(mIMSessionFragment);
        fragments.add(mIMContactListFragment);
        fragments.add(mIMWZContactListFragment);
        fragments.add(mIMGroupListFragment);
        FragmentAdapter mAdapter = new FragmentAdapter(getFragmentManager(), fragments, titles);
        vp_select.setAdapter(mAdapter);
        tl_navigation.setupWithViewPager(vp_select);

    }

    private void initEvent() {
        mView.findViewById(R.id.ll_searchContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IMSearchContactActivity.class));
            }
        });

        vp_select.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
//                    mIMSessionFragment.initData();
                } else if (position == 1) {
                    mIMContactListFragment.initData();
                } else if (position == 2) {
                    mIMWZContactListFragment.initData();
                } else if (position == 3) {
                    mIMGroupListFragment.initData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}