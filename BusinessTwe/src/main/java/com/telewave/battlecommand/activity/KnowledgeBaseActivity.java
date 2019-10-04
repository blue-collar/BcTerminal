package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.telewave.business.twe.R;
import com.telewave.lib.base.util.DoubleClickUtils;
import com.telewave.lib.widget.BaseActivity;

public class KnowledgeBaseActivity extends BaseActivity {


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_knowledge_base);
        initView();
    }

    private void initView() {


        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 点击处置规程
         */
        findViewById(R.id.disposal_procedures_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, DisposalProceduresActivity.class);
                    startActivity(intent);
                }
            }
        });
        /**
         * 点击危化品
         */
        findViewById(R.id.dangerous_chemical_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, DangerChemicalActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 专家库
         */
        findViewById(R.id.professional_person_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, ExpertLibActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 预案库
         */
        findViewById(R.id.plan_lib_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, YuAnPlanActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 九小场所
         */
        findViewById(R.id.knowledge_nine_small_place_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, NineSmallPlaceActivity.class);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.disaster_statistics_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, DisasterStatisticActivity.class);
                    startActivity(intent);
                }
            }
        });
        /**
         * 点击重点单位
         */
        findViewById(R.id.important_unit_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, ImportUnitActivity.class);
                    startActivity(intent);
                }
            }
        });
        /**
         * 点击政策规范
         */
        findViewById(R.id.policy_law_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, PolicyGuiFanActivity.class);
                    startActivity(intent);
                }
            }
        });
        /**
         * 点击案例库
         */
        findViewById(R.id.case_example_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(KnowledgeBaseActivity.this, ExampleLibActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
