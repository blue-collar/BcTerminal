package com.telewave.battlecommand.imui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.http.WaitDialog;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.service.IMGroupOperate;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 群成员维护
 *
 * @author PF-NAN
 * @date 2019-07-26
 */
public class IMGroupMemberEditActivity extends BaseActivity {
    private List<String> mSelectIds = new ArrayList<>();
    private LinearLayout ll_contactSelectItemRoot;
    private WaitDialog mWaitDialog;
    private String mGroupId;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_groupmember);
        ll_contactSelectItemRoot = findViewById(R.id.ll_contactSelectItemRoot);
        mWaitDialog = new WaitDialog(this);
        mWaitDialog.setCancelable(true);
        initEvent();
        initData();
    }

    private void initData() {
        mGroupId = getIntent().getStringExtra(IMKeys.INTENT_ID);
        if (!TextUtils.isEmpty(mGroupId)) {
            showLoading();
            /*获取群组成员信息*/
            TIMGroupManager.getInstance().getGroupMembers(mGroupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
                @Override
                public void onError(int code, String desc) {
                    closeLoading();
                }

                @Override
                public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                    IMGroupOperate.getInstance().getTimGroupMembers(timGroupMemberInfos, new TIMValueCallBack<List<TIMUserProfile>>() {
                        @Override
                        public void onError(int code, String desc) {
                            closeLoading();
                        }

                        @Override
                        public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                            closeLoading();
                            initUserView(timUserProfiles);
                        }
                    });
                }
            });
        }
    }


    private void initEvent() {
        findViewById(R.id.ll_editMemberReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mSelectIds && !mSelectIds.isEmpty()) {

                    TUIKitDialog tuiKitDialog = new TUIKitDialog(IMGroupMemberEditActivity.this)
                            .builder()
                            .setCancelable(true)
                            .setCancelOutside(true)
                            .setTitle("您确认删除所选成员?")
                            .setDialogWidth(0.8f);
                    tuiKitDialog.getBtn_neg().setTextColor(getResources().getColor(R.color.top_bar));
                    tuiKitDialog.getBtn_pos().setTextColor(getResources().getColor(R.color.top_bar));
                    tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteGroupMember();
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            }
        });
    }

    /**
     * 初始化用户资料数据
     *
     * @param timUserProfiles
     */
    private void initUserView(List<TIMUserProfile> timUserProfiles) {
        ll_contactSelectItemRoot.removeAllViews();
        String userName = SharePreferenceUtils.getDataSharedPreferences(IMGroupMemberEditActivity.this, "username");
        if (null != timUserProfiles && !timUserProfiles.isEmpty()) {
            for (TIMUserProfile timUserProfile : timUserProfiles) {
                if (!TextUtils.equals(timUserProfile.getNickName(), userName)) {
                    View inflate = View.inflate(this, R.layout.im_view_user_edit_item, null);
                    TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                    ImageView iv_selectState = inflate.findViewById(R.id.iv_selectState);
                    tv_userName.setText(timUserProfile.getNickName());
                    initItemEvent(inflate, iv_selectState, timUserProfile);
                    ll_contactSelectItemRoot.addView(inflate);
                }
            }

        }
    }

    private void initItemEvent(View inflate, final ImageView iv_selectState, final TIMUserProfile timUserProfile) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_selectState.setSelected(!iv_selectState.isSelected());
                if (iv_selectState.isSelected()) {
                    mSelectIds.add(timUserProfile.getIdentifier());
                } else {
                    for (int i = 0; i < mSelectIds.size(); i++) {
                        if (TextUtils.equals(mSelectIds.get(i), timUserProfile.getIdentifier())) {
                            mSelectIds.remove(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * 删除所选成员
     */
    private void deleteGroupMember() {
        TIMGroupManager.DeleteMemberParam param = new TIMGroupManager.DeleteMemberParam(mGroupId, mSelectIds);
        TIMGroupManager.getInstance().deleteGroupMember(param, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtils.toastLongMessage("删除成员失败,请稍后再试!");
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }


    private void showLoading() {
        if (null != mWaitDialog && !mWaitDialog.isShowing()) {
            mWaitDialog.show();
        }
    }

    public void closeLoading() {
        if (null != mWaitDialog) {
            mWaitDialog.dismiss();
        }
    }


}
