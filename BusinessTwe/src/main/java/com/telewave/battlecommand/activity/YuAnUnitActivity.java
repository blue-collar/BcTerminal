package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.telewave.battlecommand.adapter.GridImportUnitAdapter;
import com.telewave.battlecommand.bean.ImportUnit;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.presenter.PlanUnitInfoPresenter;
import com.telewave.battlecommand.utils.GridSpacingItemDecoration;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yuan.refreshlayout.NormalRefreshViewHolder;
import com.yuan.refreshlayout.RefreshLayout;
import com.yuan.refreshlayout.RefreshViewHolder;

import java.util.ArrayList;
import java.util.List;

public class YuAnUnitActivity extends BaseActivity implements IDirectionContract.IPlanUnitView {

    private static final String TAG = "YuAnUnitActivity";
    private List<ImportUnit> importUnitList = new ArrayList<>();
    private GridImportUnitAdapter importUnitAdapter;
    private PlanUnitInfoPresenter mPresenter;

    private boolean isPullLoadData = false;
    private boolean isLoadMoreData = true;
    private RefreshLayout unitRefresh;
    private RecyclerView unitReclView;
    private String name;//单位名称
    private String organId;
    private int currentPage = 1;
    private int pageSize = 10;
    private int pageTotalNum = 0;//获取从服务器返回的总分页数
    private boolean isShowProgress = true;
    private int count = 0;//判断是否首次进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_yu_an_unit);
        mPresenter = new PlanUnitInfoPresenter(this);

        initView();
        initData();

    }

    private void initView() {

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        unitReclView = (RecyclerView) findViewById(R.id.rv_yu_an_unit_list);
        unitRefresh = (RefreshLayout) findViewById(R.id.yu_an_unit_refresh);

        unitRefresh.shouldHandleRecyclerViewLoadingMore(unitReclView);
        unitRefresh.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                isPullLoadData = true;
                currentPage = 1;
                mPresenter.getPlanUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                currentPage++;
                if (isLoadMoreData) {
                    mPresenter.getPlanUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
                    return true;
                } else {
                    unitRefresh.endRefreshing();
                    unitRefresh.endLoadingMore();
                    return false;
                }
            }
        });
        RefreshViewHolder holder = new NormalRefreshViewHolder(YuAnUnitActivity.this, true);
        unitRefresh.setRefreshViewHolder(holder);

        importUnitAdapter = new GridImportUnitAdapter(YuAnUnitActivity.this, importUnitList);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);

        unitReclView.setLayoutManager(layoutManage);

        unitReclView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.dp_5), true));
        importUnitAdapter.setOnItemClickListener(onItemClickListener);
        unitReclView.setAdapter(importUnitAdapter);

    }

    private void initData() {
        organId = getIntent().getStringExtra("officeId");
        if (!TextUtils.isEmpty(organId)) {
            mPresenter.getPlanUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
        }
    }


    private GridImportUnitAdapter.OnItemClickListener onItemClickListener = new GridImportUnitAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(YuAnUnitActivity.this, PlanUnitDetailActivity.class);
            intent.putExtra("organId", importUnitList.get(position).getId());
            startActivity(intent);
        }
    };

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(YuAnUnitActivity.this, "正在加载,请稍后...");
        }
    }

    /**
     * 隐藏掉等待对话框
     */
    @Override
    public void dismissWaitDialog() {
        if (ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.dismissDialog();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissWaitDialog();
        ToastUtils.toastShortMessage(msg);
        importUnitList.clear();
        importUnitAdapter.notifyDataSetChanged();
        if (isPullLoadData) {
            unitRefresh.endRefreshing();
        }
    }

    @Override
    public void onPlanUnitListCompleted(List<ImportUnit> list) {

        Log.e(TAG, "onUnitListCompleted: " + currentPage);
        if (list == null || list.isEmpty()) {
            if (isLoadMoreData) {
                ToastUtils.toastShortMessage("没有更多的数据");
            } else {
                ToastUtils.toastShortMessage("暂无数据");
            }
            //不是加载更多的话,就不会将原来的清除掉
            if (!isLoadMoreData) {
                importUnitList.clear();
                importUnitAdapter.notifyDataSetChanged();
            }
            unitRefresh.endRefreshing();
            unitRefresh.endLoadingMore();
            return;
        }
        if (isPullLoadData) {
            importUnitList.clear();
        }
        if (!isLoadMoreData) {
            importUnitList.clear();
        }
        importUnitList.addAll(list);
        importUnitAdapter.notifyDataSetChanged();
        unitRefresh.endRefreshing();
        unitRefresh.endLoadingMore();
        pageTotalNum = mPresenter.getPageTotalNum();
        if (count == 1) {
            ++count;
            ToastUtils.toastShortMessage("加载完成");
        } else {
            if (currentPage >= pageTotalNum) {
                ToastUtils.toastShortMessage("已加载全部");
                isLoadMoreData = false;
            } else {
                ToastUtils.toastShortMessage("加载完成");
                isLoadMoreData = true;
                isPullLoadData = false;
            }
        }
    }


}
