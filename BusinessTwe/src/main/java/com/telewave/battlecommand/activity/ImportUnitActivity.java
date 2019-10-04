package com.telewave.battlecommand.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.telewave.battlecommand.adapter.ImportUnitAdapter;
import com.telewave.battlecommand.bean.ImportUnit;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.presenter.ImportantUnitInfoPresenter;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DivderItemDecoration;
import com.telewave.lib.base.util.KeyBoardHiddenUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yuan.refreshlayout.NormalRefreshViewHolder;
import com.yuan.refreshlayout.RefreshLayout;
import com.yuan.refreshlayout.RefreshViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 重点单位
 *
 * @author zhangjun
 * @date 2019/8/6
 */
public class ImportUnitActivity extends BaseActivity implements IDirectionContract.IUnitView, View.OnClickListener {

    private static final String TAG = "ImportUnitActivity";
    private ClearEditText mEtName;
    private Button mBtnSearch;
    private List<ImportUnit> importUnitList = new ArrayList<ImportUnit>();
    private ImportUnitAdapter importUnitAdapter;
    private ImportantUnitInfoPresenter mPresenter;

    private boolean isPullLoadData = false;
    private boolean isLoadMoreData = true;
    private RefreshLayout unitRefresh;
    private RecyclerView unitReclView;
    private String name;//单位名称
    private String organId;
    private int currentPage = 1;
    private int pageSize = 10;
    private int pageTotalNum = 0;//获取从服务器返回的总分页数
    private boolean isShowProgress = false;//不展示进度条
    private int count = 0;//判断是否首次进入


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_import_unit);
        mPresenter = new ImportantUnitInfoPresenter(this);

        initView();
        initData();
    }

    public void initView() {

        mEtName = (ClearEditText) findViewById(R.id.edit_unit_name_search);
        mBtnSearch = (Button) findViewById(R.id.btn_unit_search);
        mBtnSearch.setOnClickListener(this);
        mEtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                KeyBoardHiddenUtils.closeInput(mEtName);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    name = mEtName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        mPresenter.getImportUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
                    }
                }
                return false;
            }
        });

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        unitReclView = (RecyclerView) findViewById(R.id.rv_unit_list);
        unitRefresh = (RefreshLayout) findViewById(R.id.unit_refresh);

        unitRefresh.shouldHandleRecyclerViewLoadingMore(unitReclView);
        unitRefresh.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                isPullLoadData = true;
                currentPage = 1;
                mPresenter.getImportUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                currentPage++;
                if (isLoadMoreData) {
                    mPresenter.getImportUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
                    return true;
                } else {
                    unitRefresh.endRefreshing();
                    unitRefresh.endLoadingMore();
                    return false;
                }
            }
        });
        RefreshViewHolder holder = new NormalRefreshViewHolder(ImportUnitActivity.this, true);
        unitRefresh.setRefreshViewHolder(holder);

        importUnitAdapter = new ImportUnitAdapter(ImportUnitActivity.this, importUnitList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ImportUnitActivity.this);
        //设置布局管理器
        unitReclView.setLayoutManager(linearLayoutManager);
        //设置为垂直布局，这也是默认的
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        unitReclView.addItemDecoration(new DivderItemDecoration(ImportUnitActivity.this, LinearLayoutManager.VERTICAL));
        importUnitAdapter.setOnItemClickListener(onItemClickListener);
        unitReclView.setAdapter(importUnitAdapter);

    }

    private void initData() {
        organId = ConstData.ORGANID;
        ++count;
        if (!TextUtils.isEmpty(organId)) {
            mPresenter.getImportUnitInfo(organId, name, currentPage, pageSize, isShowProgress);
        }
    }

    private ImportUnitAdapter.OnItemClickListener onItemClickListener = new ImportUnitAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(ImportUnitActivity.this, ImportUnitDetailActivity.class);
            intent.putExtra("id", importUnitList.get(position).getId());
            startActivity(intent);
        }
    };

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(ImportUnitActivity.this, "正在加载,请稍后...");
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
    public void onUnitListCompleted(List<ImportUnit> info) {

        Log.e(TAG, "onUnitListCompleted: " + currentPage);
        if (info == null || info.isEmpty()) {
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
        importUnitList.addAll(info);
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

    @Override
    public void onClick(View view) {
        final int vId = view.getId();
        if (R.id.btn_unit_search == vId) {
            name = mEtName.getText().toString().trim();
            mPresenter.getImportUnitInfo(organId, name, currentPage, pageSize, isShowProgress);

        } else {
        }
    }
}