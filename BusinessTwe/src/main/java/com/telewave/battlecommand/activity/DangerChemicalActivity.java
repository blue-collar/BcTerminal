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

import com.telewave.battlecommand.adapter.ChemicalInfoAdapter;
import com.telewave.battlecommand.bean.ChemicalInfo;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.presenter.ChemicalInfoPresenter;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
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
 * 危化品
 *
 * @author liwh
 * @date 2018/12/20
 */
public class DangerChemicalActivity extends BaseActivity implements IDirectionContract.IChemicalInfoView {

    private static final String TAG = "DangerChemicalActivity";
    //    private X5WebView mWebView;
    private ClearEditText mEtName;
    private Button mBtnSearch;
    private ChemicalInfoPresenter mPresenter;
    private List<ChemicalInfo> mChemicalInfoList = new ArrayList<>();
    private ChemicalInfoAdapter mChemicalInfoAdapter;

    private boolean isPullLoadData = false;
    private boolean isLoadMoreData = true;
    private RefreshLayout dangerRefresh;
    private RecyclerView dangerReclView;
    private String name;//危化品名称
    //    private String organId;
    private int currentPage = 1;
    private int pageSize = 20;
    private int pageTotalNum = 0;//获取从服务器返回的总分页数
    private boolean isShowProgress = true;
    private int count = 0;//判断是否首次进入


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_danger_chemical);
        mPresenter = new ChemicalInfoPresenter(this);

        initView();
        initData();
    }

    private void initView() {

        mEtName = (ClearEditText) findViewById(R.id.edit_danger_name_search);
        mBtnSearch = (Button) findViewById(R.id.btn_danger_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    mPresenter.getChemicalList(name, currentPage, pageSize, isShowProgress);
                } else {
                    ToastUtils.toastShortMessage("输入不能为空");
                }
            }
        });

        mEtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                KeyBoardHiddenUtils.closeInput(mEtName);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    name = mEtName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        mPresenter.getChemicalList(name, currentPage, pageSize, isShowProgress);
                    } else {
                        ToastUtils.toastShortMessage("输入不能为空");
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

        dangerReclView = (RecyclerView) findViewById(R.id.rv_danger_list);
        dangerRefresh = (RefreshLayout) findViewById(R.id.danger_refresh);

        dangerRefresh.shouldHandleRecyclerViewLoadingMore(dangerReclView);
        dangerRefresh.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                isPullLoadData = true;
                currentPage = 1;
                mPresenter.getChemicalList(name, currentPage, pageSize, isShowProgress);
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                currentPage++;
                if (isLoadMoreData) {
                    mPresenter.getChemicalList(name, currentPage, pageSize, isShowProgress);
                    return true;
                } else {
                    dangerRefresh.endRefreshing();
                    dangerRefresh.endLoadingMore();
                    return false;
                }
            }
        });
        RefreshViewHolder holder = new NormalRefreshViewHolder(DangerChemicalActivity.this, true);
        dangerRefresh.setRefreshViewHolder(holder);

        mChemicalInfoAdapter = new ChemicalInfoAdapter(this, mChemicalInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DangerChemicalActivity.this);
        //设置布局管理器
        dangerReclView.setLayoutManager(linearLayoutManager);
        //设置为垂直布局，这也是默认的
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dangerReclView.addItemDecoration(new DivderItemDecoration(DangerChemicalActivity.this, LinearLayoutManager.VERTICAL));
        mChemicalInfoAdapter.setOnItemClickListener(onItemClickListener);
        dangerReclView.setAdapter(mChemicalInfoAdapter);
    }

    private void initData() {
        ++count;
        mPresenter.getChemicalList(name, currentPage, pageSize, isShowProgress);
    }


    private ChemicalInfoAdapter.OnItemClickListener onItemClickListener = new ChemicalInfoAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//            ToastUtils.toastShortMessage("点击：" + position);
            Intent intent = new Intent(DangerChemicalActivity.this, ChemicalDetailActivity.class);
            intent.putExtra("ChemicalId", mChemicalInfoList.get(position).getId());
            startActivity(intent);

        }
    };

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(DangerChemicalActivity.this, "正在加载,请稍后...");
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
        mChemicalInfoList.clear();
        mChemicalInfoAdapter.notifyDataSetChanged();
        if (isPullLoadData) {
            dangerRefresh.endRefreshing();
        }
    }

    @Override
    public void onChemicalListCompleted(List<ChemicalInfo> list) {

        Log.e(TAG, "onChemicalListCompleted: " + currentPage);
        if (list == null || list.isEmpty()) {
            if (isLoadMoreData) {
                ToastUtils.toastShortMessage("没有更多的数据");
            } else {
                ToastUtils.toastShortMessage("暂无数据");
            }
            //不是加载更多的话,就不会将原来的清除掉
            if (!isLoadMoreData) {
                mChemicalInfoList.clear();
                mChemicalInfoAdapter.notifyDataSetChanged();
            }
            dangerRefresh.endRefreshing();
            dangerRefresh.endLoadingMore();
            return;
        }
        if (isPullLoadData) {
            mChemicalInfoList.clear();
        }
        if (!isLoadMoreData) {
            mChemicalInfoList.clear();
        }
        mChemicalInfoList.addAll(list);
        mChemicalInfoAdapter.notifyDataSetChanged();
        dangerRefresh.endRefreshing();
        dangerRefresh.endLoadingMore();
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