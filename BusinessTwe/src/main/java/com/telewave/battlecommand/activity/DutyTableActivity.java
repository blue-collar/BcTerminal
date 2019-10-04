package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.telewave.business.twe.R;
import com.telewave.lib.widget.BaseActivity;


/**
 * 值班表Tab主页面
 *
 * @author liwh
 * @date 2019/4/4
 */
public class DutyTableActivity extends BaseActivity {

    private RadioGroup mGroup;
    private FrameLayout dutyTableDayLayout;
    private FrameLayout dutyTableWeekLayout;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_duty_table_main);
        mGroup = (RadioGroup) findViewById(R.id.group);
        dutyTableDayLayout = (FrameLayout) findViewById(R.id.duty_table_day_layout);
        dutyTableWeekLayout = (FrameLayout) findViewById(R.id.duty_table_week_layout);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private class CheckedChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.one == checkedId) {
                group.findViewById(R.id.one).setBackgroundResource(R.color.duty_table_item_checked);
                group.findViewById(R.id.two).setBackgroundResource(R.color.microstation_bg);
                dutyTableDayLayout.setVisibility(View.VISIBLE);
                dutyTableWeekLayout.setVisibility(View.GONE);
            } else if (R.id.two == checkedId) {
                group.findViewById(R.id.one).setBackgroundResource(R.color.microstation_bg);
                group.findViewById(R.id.two).setBackgroundResource(R.color.duty_table_item_checked);
                dutyTableDayLayout.setVisibility(View.GONE);
                dutyTableWeekLayout.setVisibility(View.VISIBLE);
            } else {
            }
        }
    }

}
