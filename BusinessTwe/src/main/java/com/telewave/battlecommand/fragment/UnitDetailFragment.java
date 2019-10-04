package com.telewave.battlecommand.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telewave.battlecommand.bean.PlanUnitDetail;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.presenter.UnitDetailPresenter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;

/**
 * 信息详情
 *
 * @author zhangjun
 * @date 2019/8/12
 */
@SuppressLint("ValidFragment")
public class UnitDetailFragment extends BaseFragment implements IDirectionContract.IPlanUnitDetailView {

    private View mView;

    private TextView importUnitName;
    private TextView importUnitAddress;
    private TextView importUnitDutyphone;
    private TextView importUnitContactman;
    private TextView importUnitArtificialperson;
    private TextView importUnitHoldarea;
    private TextView importUnitWorkers;
    private TextView importUnitFixedassets;
    private TextView importUnitAqglrdh;
    private TextView importUnitXfaqglr;
    private TextView importUnitAqglrsfz;
    private TextView importUnitAqzrrdh;
    private TextView importUnitAqzrrsfz;
    private TextView importUnitCjsj;
    private TextView importUnitDlwz;
    private TextView importUnitDwclsj;
    private TextView importUnitFrdb;
    private TextView importUnitFrdbdh;
    private TextView importUnitJzmj;
    private TextView importUnitYzbm;
    private TextView importUnitZjzxfglr;
    private TextView importUnitZjzxfglrdh;
    private TextView importUnitZjzxfglrsfz;

    private UnitDetailPresenter mPresenter;
    private String importUnitId;
    private Context context;
    private String tabTitle;

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        mPresenter = new UnitDetailPresenter(this);
        initData();
    }


    public UnitDetailFragment(Context context, String title, String id) {
        this.context = context;
        this.tabTitle = title;
        this.importUnitId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_plan_unit_detail, container, false);
        initView();

        return mView;
    }

    private void initView() {

        importUnitName = mView.findViewById(R.id.import_unit_name);
        importUnitAddress = mView.findViewById(R.id.import_unit_address);
        importUnitDutyphone = mView.findViewById(R.id.import_unit_dutyphone);
        importUnitContactman = mView.findViewById(R.id.import_unit_contactman);
        importUnitArtificialperson = mView.findViewById(R.id.import_unit_aartificialperson);
        importUnitHoldarea = mView.findViewById(R.id.import_unit_holdarea);
        importUnitWorkers = mView.findViewById(R.id.import_unit_workers);
        importUnitFixedassets = mView.findViewById(R.id.import_unit_fixedassets);
        importUnitAqglrdh = mView.findViewById(R.id.import_unit_aqglrdh);
        importUnitXfaqglr = mView.findViewById(R.id.import_unit_xfaqglr);
        importUnitAqglrsfz = mView.findViewById(R.id.import_unit_aqglrsfz);
        importUnitAqzrrdh = mView.findViewById(R.id.import_unit_aqzrrdh);
        importUnitAqzrrsfz = mView.findViewById(R.id.import_unit_aqzrrsfz);
        importUnitCjsj = mView.findViewById(R.id.import_unit_cjsj);
        importUnitDlwz = mView.findViewById(R.id.import_unit_dlwz);
        importUnitDwclsj = mView.findViewById(R.id.import_unit_dwclsj);
        importUnitFrdb = mView.findViewById(R.id.import_unit_frdb);
        importUnitFrdbdh = mView.findViewById(R.id.import_unit_frdbdh);
        importUnitJzmj = mView.findViewById(R.id.import_unit_jzmj);
        importUnitYzbm = mView.findViewById(R.id.import_unit_yzbm);
        importUnitZjzxfglr = mView.findViewById(R.id.import_unit_zjzxfglr);
        importUnitZjzxfglrdh = mView.findViewById(R.id.import_unit_zjzxfglrdh);
        importUnitZjzxfglrsfz = mView.findViewById(R.id.import_unit_zjzxfglrsfz);

    }

    private void initData() {
        if (!TextUtils.isEmpty(importUnitId)) {
            mPresenter.getPlanUnitDetailInfo(importUnitId, true);
        }
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

    private void setData(PlanUnitDetail info) {
        importUnitName.setText(info.getObjectname() != null ? info.getObjectname() : "");
        importUnitAddress.setText(info.getObjectaddr() != null ? info.getObjectaddr() : "");
        importUnitDutyphone.setText(info.getDutyphone() != null ? info.getDutyphone() : "");
        importUnitContactman.setText(info.getContactman() != null ? info.getContactman() : "");
        importUnitArtificialperson.setText(info.getAartificialperson() != null ? info.getAartificialperson() : "");
        importUnitHoldarea.setText(info.getHoldarea() != null ? info.getHoldarea() : "");
        importUnitWorkers.setText(info.getWorkers() != null ? info.getWorkers() : "");
        importUnitFixedassets.setText(info.getFixedassets() != null ? info.getFixedassets() : "");
        importUnitAqglrdh.setText(info.getAqglrdh() != null ? info.getAqglrdh() : "");
        importUnitXfaqglr.setText(info.getXfaqglr() != null ? info.getXfaqglr() : "");
        importUnitAqglrsfz.setText(info.getAqglrsfz() != null ? info.getAqglrsfz() : "");
        importUnitAqzrrdh.setText(info.getAqzrrdh() != null ? info.getAqzrrdh() : "");
        importUnitAqzrrsfz.setText(info.getAqzrrsfz() != null ? info.getAqzrrsfz() : "");
        importUnitCjsj.setText(info.getCjsj() != null ? info.getCjsj() : "");
        importUnitDlwz.setText(info.getDlwz() != null ? info.getDlwz() : "");
        importUnitDwclsj.setText(info.getDwclsj() != null ? info.getDwclsj() : "");
        importUnitFrdb.setText(info.getFrdb() != null ? info.getFrdb() : "");
        importUnitFrdbdh.setText(info.getFrdbdh() != null ? info.getFrdbdh() : "");
        importUnitJzmj.setText(info.getJzmj() != null ? info.getJzmj() : "");
        importUnitYzbm.setText(info.getYzbm() != null ? info.getYzbm() : "");
        importUnitZjzxfglr.setText(info.getZjzxfglr() != null ? info.getZjzxfglr() : "");
        importUnitZjzxfglrdh.setText(info.getZjzxfglrdh() != null ? info.getZjzxfglrdh() : "");
        importUnitZjzxfglrsfz.setText(info.getZjzxfglrsfz() != null ? info.getZjzxfglrsfz() : "");

    }

    @Override
    public void onPlanUnitDetailCompleted(PlanUnitDetail unitDetail) {
        if (unitDetail != null) {
            setData(unitDetail);
        } else {
            ToastUtils.toastShortMessage("暂无数据");
        }
    }
}
