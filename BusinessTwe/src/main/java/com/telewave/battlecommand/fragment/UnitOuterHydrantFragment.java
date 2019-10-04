package com.telewave.battlecommand.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telewave.battlecommand.activity.ImagePagerActivity;
import com.telewave.battlecommand.adapter.PlanUnitPhotoAdapter;
import com.telewave.battlecommand.bean.FjxxListBean;
import com.telewave.battlecommand.bean.PlanUnitDetail;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.presenter.UnitDetailPresenter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_INDEX;
import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_URLS;

/**
 * 外围水源
 *
 * @author zhangjun
 * @date 2019/8/13
 */

@SuppressLint("ValidFragment")
public class UnitOuterHydrantFragment extends BaseFragment implements IDirectionContract.IPlanUnitDetailView {

    private static final String TAG = UnitOuterHydrantFragment.class.getSimpleName();

    private View mView;
    private String importUnitId;
    private Context context;
    private String tabTitle;

    private RecyclerView mPicReclView;
    private List<FjxxListBean> picInfos = new ArrayList<>();
    private List<String> mPathDatas = new ArrayList<>();//图片路径
    private PlanUnitPhotoAdapter mPicAdapter;
    private int mMaxNum = 6;

    private UnitDetailPresenter mPresenter;

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        mPresenter = new UnitDetailPresenter(this);
        initData();
    }


    public UnitOuterHydrantFragment(Context context, String title, String id) {
        this.context = context;
        this.tabTitle = title;
        this.importUnitId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_plan_unit_outer_hydrant, container, false);
        initView();

        return mView;
    }

    private void initView() {

        mPicReclView = mView.findViewById(R.id.plan_unit_outer_hydrant_pic);

        //设定十六宫格
        GridLayoutManager picLayoutManager = new GridLayoutManager(getActivity(), 4);
        mPicReclView.setLayoutManager(picLayoutManager);
        mPicAdapter = new PlanUnitPhotoAdapter(getActivity(), picInfos, mMaxNum);
        mPicReclView.setAdapter(mPicAdapter);
        mPicAdapter.setOnItemClickListener(onItemClickListener);

    }

    private void initData() {
        if (!TextUtils.isEmpty(importUnitId)) {

        }
    }

    private PlanUnitPhotoAdapter.OnItemClickListener onItemClickListener = new PlanUnitPhotoAdapter.OnItemClickListener() {
        @Override
        public void onPreViewItemClick(View v, int position) {

            if (mPathDatas == null || mPathDatas.isEmpty()) {
                ToastUtils.toastShortMessage("图片暂未下载成功");
                return;
            }
            imageBrower(position, mPathDatas);
        }
    };

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls
     */
    protected void imageBrower(int position, List<String> urls) {
        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, (Serializable) urls);
        intent.putExtra(EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
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
    }

    /**
     * 组合URL
     */
    private void combineUrl() {

        if (picInfos != null) {
            for (int i = 0; i < picInfos.size(); i++) {
                FjxxListBean tempInfo = picInfos.get(i);

                String url = "http://" + ConstData.urlManager.serverIp + ":" + ConstData.urlManager.serverPort
                        + "/files/" + tempInfo.getFjdz();
                picInfos.get(i).setUrl(url);
            }
        }

    }

    private void onGetPicFromUrl(final FjxxListBean temp, String url) {

        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, ConstData.CHAT_PIC_DIR, "", true, false);

        NoHttpManager.getDownloadInstance().add(0, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                Log.e(TAG, "onDownloadError: " + exception);
                ToastUtils.toastShortMessage("下载图片失败");
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                Log.e(TAG, "onStart: " + "isResume :" + isResume + "rangeSize :" + rangeSize + "allCount: " + allCount);

            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                Log.e(TAG, "onProgress: " + progress + "fileCount :" + fileCount + "speed :" + speed);

            }

            @Override
            public void onFinish(int what, String filePath) {
                Log.e(TAG, "onFinish: ");
//                ToastUtils.toastShortMessage("下载图片完成！");

                temp.setFilePhonePath(filePath);
                mPathDatas.add(filePath);

                mPicAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancel(int what) {
                Log.e(TAG, "onCancel: ");

            }
        });
    }

    @Override
    public void onPlanUnitDetailCompleted(PlanUnitDetail unitDetail) {
        if (unitDetail != null && !unitDetail.getYaglMhdwFjxxList().isEmpty()) {
            for (FjxxListBean tempInfo : unitDetail.getYaglMhdwFjxxList()) {
                if (tempInfo.getFjlx().equals("05")) {
                    picInfos.add(tempInfo);
                }
            }

            combineUrl();

            for (int i = 0; i < picInfos.size(); i++) {
                FjxxListBean temp = picInfos.get(i);
                String url = temp.getUrl();
                onGetPicFromUrl(temp, url);
            }

            if (!picInfos.isEmpty()) {
                mPicAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.toastShortMessage("暂无数据");
            }
        }
    }
}
