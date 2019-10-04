package com.telewave.battlecommand.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.telewave.battlecommand.activity.EquipmentStrengthActivity;
import com.telewave.battlecommand.activity.HydrantMsgActivity;
import com.telewave.battlecommand.activity.KnowledgeBaseActivity;
import com.telewave.battlecommand.activity.ScheduleActivity;
import com.telewave.battlecommand.activity.SystemSetActivity;
import com.telewave.battlecommand.directboard.activity.DirectBroadCastActivity;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.SharePreferenceUtils;


/**
 * 我
 *
 * @author liwh
 * @date 2018/12/12
 */
public class MyPageFragment extends Fragment {
    private static final String TAG = "MyPageFragment";

    private View mView;
    private TextView showNameTv, accoutNameTv;
    private RelativeLayout hydrantCollectLayout, knowledgeBaseLayout, equipmentStrengthLayout;
    private RelativeLayout directBroadCastLayout, dutyTableLayout, systemSetLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_page, container, false);
        initView();
        return mView;
    }

    private void initView() {
        showNameTv = (TextView) mView.findViewById(R.id.show_name_text);
        accoutNameTv = (TextView) mView.findViewById(R.id.accout_name_text);
        equipmentStrengthLayout = (RelativeLayout) mView.findViewById(R.id.equipment_strength_layout);
        hydrantCollectLayout = (RelativeLayout) mView.findViewById(R.id.hydrant_collect_layout);
        knowledgeBaseLayout = (RelativeLayout) mView.findViewById(R.id.knowledge_base_layout);
        directBroadCastLayout = (RelativeLayout) mView.findViewById(R.id.direct_broadcast_layout);
        dutyTableLayout = (RelativeLayout) mView.findViewById(R.id.duty_table_layout);
        systemSetLayout = (RelativeLayout) mView.findViewById(R.id.system_set_layout);
        equipmentStrengthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EquipmentStrengthActivity.class);
                startActivity(intent);
            }
        });
        hydrantCollectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HydrantMsgActivity.class);
                startActivity(intent);
            }
        });
        knowledgeBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KnowledgeBaseActivity.class);
                startActivity(intent);
            }
        });
        directBroadCastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DirectBroadCastActivity.class);
                startActivity(intent);
            }
        });
        dutyTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                startActivity(intent);
            }
        });
        systemSetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SystemSetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String userName = SharePreferenceUtils.getDataSharedPreferences(getActivity(), "username");
        String loginName = SharePreferenceUtils.getDataSharedPreferences(getActivity(), "loginname");
        showNameTv.setText(userName);
        accoutNameTv.setText("账号:" + loginName);
    }
}