package com.telewave.battlecommand.imui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.telewave.battlecommand.fragment.ContactListFragment;
import com.telewave.business.twe.R;


/**
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_imtest);


        ContactListFragment ll_fragemnt = (ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.f_contentFragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.hide(ll_fragemnt);
        fragmentTransaction.show(ll_fragemnt);
//        ll_fragemnt.setUserVisibleHint(true);
    }
}
