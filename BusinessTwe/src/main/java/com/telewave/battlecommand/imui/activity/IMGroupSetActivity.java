package com.telewave.battlecommand.imui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.WaitDialog;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.other.EditGroupUserObj;
import com.telewave.battlecommand.imui.bean.parse.IMBooleanParseObj;
import com.telewave.battlecommand.imui.service.IMGroupOperate;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ScreenUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoProvider;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * q群组设置
 *
 * @author PF-NAN
 * @date 2019-07-24
 */
public class IMGroupSetActivity extends BaseActivity {
    private WaitDialog mWaitDialog;

    private String mGroupId;
    private GroupInfoProvider mGroupInfoProvider;
    private TextView tv_groupName;
    private TextView tv_deleteGroup;
    private ToggleButton tbtn_groupTop;
    private TextView tv_toViewMore;
    private GroupInfo mGroupInfo;
    private LinearLayout ll_userRootView;
    private List<TIMUserProfile> mUserProfiles = new ArrayList<>();
    private EditGroupUserObj mAddOperateObj;
    private EditGroupUserObj mMinusOperateObj;

    private ArrayList<String> mUserIds = new ArrayList<>();
    private LinearLayout ll_editGroupName;
    private ImageView iv_editGroupName;
    private TextView tv_noGroupNotice;
    private TextView tv_groupNotice;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_groupset);
        mWaitDialog = new WaitDialog(this);
        mWaitDialog.setCancelable(true);
        mGroupId = getIntent().getStringExtra(IMKeys.INTENT_ID);
        mGroupInfoProvider = new GroupInfoProvider();
        showLoading();
        initView();
        initEvent();
        initGroupData();
        initIsDissGroupData();
    }

    /**
     * 初始化是否可以进行群组解散或者退出
     */
    private void initIsDissGroupData() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.imIsDissGroup, RequestMethod.GET);
        request.add("groupId", mGroupId);
        NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                TUIKitLog.e("======" + result);
                if (!TextUtils.isEmpty(result)) {
                    IMBooleanParseObj imBooleanParseObj = IMJsonUtil.parseJsonToBean(result, IMBooleanParseObj.class);
                    tv_deleteGroup.setVisibility(null != imBooleanParseObj && null != imBooleanParseObj.data && !imBooleanParseObj.data ? View.GONE : View.VISIBLE);
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                TUIKitLog.e(url + "=====onFailed=" + tag);
            }
        }, false, false);
    }

    /**
     * 设置事件
     */
    private void initEvent() {
        findViewById(R.id.ll_groupSetReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_editGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mGroupInfo && mGroupInfo.isOwner()) {
                    Intent editGroupNameIntent = new Intent(IMGroupSetActivity.this, IMEditGroupNameActivity.class);
                    editGroupNameIntent.putExtra(IMKeys.INTENT_ID, mGroupId);
                    editGroupNameIntent.putExtra(IMKeys.INTENT_TAG, mGroupInfo.getGroupName());
                    startActivityForResult(editGroupNameIntent, 1001);
                }
            }
        });
        tbtn_groupTop.setTextOff("");
        tbtn_groupTop.setTextOn("");
        tbtn_groupTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isTop) {
                if (null != mGroupInfo) {
                    mGroupInfoProvider.setTopConversation(isTop);
                    mGroupInfo.setTopChat(isTop);
                }
            }
        });
        tv_toViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mGroupInfo) {
                    initGroupUserView(true);
                }
            }
        });

        tv_deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGroupInfo != null) {
                    if (mGroupInfo.isOwner()) {
                        TUIKitDialog tuiKitDialog = new TUIKitDialog(IMGroupSetActivity.this)
                                .builder()
                                .setCancelable(true)
                                .setCancelOutside(true)
                                .setTitle("您确认解散该群?")
                                .setDialogWidth(0.8f);
                        tuiKitDialog.getBtn_neg().setTextColor(getResources().getColor(R.color.top_bar));
                        tuiKitDialog.getBtn_pos().setTextColor(getResources().getColor(R.color.top_bar));
                        tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteGroup();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    } else {
                        TUIKitDialog tuiKitDialog = new TUIKitDialog(IMGroupSetActivity.this)
                                .builder()
                                .setCancelable(true)
                                .setCancelOutside(true)
                                .setTitle("您确认退出该群？")
                                .setDialogWidth(0.8f);
                        tuiKitDialog.getBtn_neg().setTextColor(getResources().getColor(R.color.top_bar));
                        tuiKitDialog.getBtn_pos().setTextColor(getResources().getColor(R.color.top_bar));

                        tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quitGroup();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                }
            }
        });

    }

    private void initView() {
        ll_userRootView = findViewById(R.id.ll_userRootView);
        tv_groupName = findViewById(R.id.tv_groupName);
        tv_deleteGroup = findViewById(R.id.tv_deleteGroup);
        tbtn_groupTop = findViewById(R.id.tbtn_groupTop);
        tv_toViewMore = findViewById(R.id.tv_toViewMore);
        ll_editGroupName = findViewById(R.id.ll_editGroupName);
        iv_editGroupName = findViewById(R.id.iv_editGroupName);
        tv_noGroupNotice = findViewById(R.id.tv_noGroupNotice);
        tv_groupNotice = findViewById(R.id.tv_groupNotice);

    }

    /**
     * 初始化数据
     */
    private void initGroupData() {
        mAddOperateObj = new EditGroupUserObj(0);
        mMinusOperateObj = new EditGroupUserObj(1);
        mGroupInfoProvider.loadGroupInfo(mGroupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mGroupInfo = (GroupInfo) data;
                TUIKitLog.e("===groupDetailInfo====" + mGroupInfo.toString());
                IMGroupOperate.getInstance().getGroupMembers(mGroupInfo.getMemberDetails(), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e("getUsersProfile failed: " + code + " desc");
                        closeLoading();
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result) {
                        closeLoading();
                        mUserProfiles.clear();
                        mUserIds.clear();
                        if (null != result) {
                            mUserProfiles.addAll(result);
                        }
                        for (int i = 0; i < mUserProfiles.size(); i++) {
                            mUserIds.add(mUserProfiles.get(i).getIdentifier());
                        }
                        initGroupUserView(false);
                    }
                });
                tv_groupName.setText(mGroupInfo.getGroupName());
                tv_deleteGroup.setText(mGroupInfo.isOwner() ? "解散" : "退出");
                tbtn_groupTop.setChecked(mGroupInfo.isTopChat());
                iv_editGroupName.setVisibility(mGroupInfo.isOwner() ? View.VISIBLE : View.GONE);
                tv_noGroupNotice.setVisibility(TextUtils.isEmpty(mGroupInfo.getNotice()) ? View.VISIBLE : View.INVISIBLE);
                tv_groupNotice.setVisibility(TextUtils.isEmpty(mGroupInfo.getNotice()) ? View.GONE : View.VISIBLE);
                tv_groupNotice.setText(mGroupInfo.getNotice());
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                closeLoading();
            }
        });
    }

    private int mUserAvatarWH = (ScreenUtils.getScreenWidth(AppProxy.getContext()) - ScreenUtils.getPxByDp(90)) / 5;
    private int mAvatarMargin = ScreenUtils.getPxByDp(15);

    /**
     * 初始化群组成员
     *
     * @param isShowAll
     */
    private void initGroupUserView(boolean isShowAll) {
        ll_userRootView.removeAllViews();
        if (isShowAll) {
            mUserProfiles.add(mAddOperateObj);
            if (mGroupInfo.isOwner()) {
                mUserProfiles.add(mMinusOperateObj);
            }
            tv_toViewMore.setVisibility(View.GONE);

        } else {
            List<TIMUserProfile> cacheTIMUserProfiles = new ArrayList<>();
            int maxIndex = mUserProfiles.size();
            if (mGroupInfo.isOwner()) {
                maxIndex = maxIndex > 18 ? 18 : maxIndex;
                tv_toViewMore.setVisibility(mUserProfiles.size() > 18 ? View.VISIBLE : View.GONE);
            } else {
                maxIndex = maxIndex > 19 ? 19 : maxIndex;
                tv_toViewMore.setVisibility(mUserProfiles.size() > 19 ? View.VISIBLE : View.GONE);
            }
            for (int i = 0; i < maxIndex; i++) {
                cacheTIMUserProfiles.add(mUserProfiles.get(i));
            }
            cacheTIMUserProfiles.add(mAddOperateObj);
            if (mGroupInfo.isOwner()) {
                cacheTIMUserProfiles.add(mMinusOperateObj);
            }
            mUserProfiles.clear();
            mUserProfiles.addAll(cacheTIMUserProfiles);
        }
        LinearLayout lineChildView = new LinearLayout(this);
        lineChildView.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < mUserProfiles.size(); i++) {
            TIMUserProfile userProfile = mUserProfiles.get(i);
            View inflate = View.inflate(this, R.layout.im_view_groupuser_item, null);
            LinearLayout.LayoutParams inflateLayoutParams = new LinearLayout.LayoutParams(mUserAvatarWH, LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView iv_userAvatar = inflate.findViewById(R.id.iv_userAvatar);
            ViewGroup.LayoutParams avatarParams = iv_userAvatar.getLayoutParams();
            avatarParams.width = mUserAvatarWH;
            avatarParams.height = mUserAvatarWH;
            TextView tv_userName = inflate.findViewById(R.id.tv_userName);
            if (userProfile instanceof EditGroupUserObj) {
                EditGroupUserObj editGroupUserObj = (EditGroupUserObj) userProfile;
                if (editGroupUserObj.type == 0) {
                    tv_userName.setVisibility(View.INVISIBLE);
                    iv_userAvatar.setImageResource(R.mipmap.im_icon_add);
                    inflate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*添加群成员操作*/
                            Intent addIntent = new Intent(IMGroupSetActivity.this, IMSelectContactActivity.class);
                            addIntent.putStringArrayListExtra(IMKeys.INTENT_IDS, mUserIds);
                            addIntent.putExtra(IMKeys.INTENT_TAG, false);
                            addIntent.putExtra(IMKeys.INTENT_ID, mGroupId);
                            startActivityForResult(addIntent, 1002);
                        }
                    });
                } else {
                    tv_userName.setVisibility(View.INVISIBLE);
                    iv_userAvatar.setImageResource(R.mipmap.im_icon_minus);
                    inflate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*删除群成员操作*/
                            Intent deleteMemberIntent = new Intent(IMGroupSetActivity.this, IMGroupMemberEditActivity.class);
                            deleteMemberIntent.putExtra(IMKeys.INTENT_ID, mGroupId);
                            startActivityForResult(deleteMemberIntent, 1003);
                        }
                    });
                }
            } else {
                tv_userName.setVisibility(View.VISIBLE);
                tv_userName.setText(userProfile.getNickName());
                iv_userAvatar.setImageResource(R.mipmap.im_ic_member);
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*点击用户头像暂不处理*/
                    }
                });
            }
            if (i % 5 == 0) {
                inflateLayoutParams.leftMargin = 0;
                lineChildView = new LinearLayout(this);
                lineChildView.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lineLayoutParams.topMargin = mAvatarMargin;
                lineChildView.setLayoutParams(lineLayoutParams);
                lineChildView.removeAllViews();
                ll_userRootView.addView(lineChildView);
            } else {
                inflateLayoutParams.leftMargin = mAvatarMargin;
            }
            inflate.setLayoutParams(inflateLayoutParams);
            lineChildView.addView(inflate);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                /*修改群昵称*/
                String groupName = data.getStringExtra(IMKeys.INTENT_TAG);
                if (!TextUtils.isEmpty(groupName)) {
                    mGroupInfoProvider.modifyGroupInfo(data.getStringExtra(IMKeys.INTENT_TAG), TUIKitConstants.Group.MODIFY_GROUP_NAME, new IUIKitCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            tv_groupName.setText((String) data);
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            ToastUtils.toastLongMessage("昵称修改失败,请稍后再试!");
                        }
                    });
                }
            } else if (requestCode == 1002) {
                initGroupData();
                String message = data.getStringExtra(IMKeys.INTENT_DES);
                GroupChatManagerKit.getInstance().sendNoticeMessage(mGroupId, message, new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                    }
                });

            } else if (requestCode == 1003) {
                initGroupData();
            }
        }
    }

    /**
     * 解散群组
     */
    private void deleteGroup() {
        mGroupInfoProvider.deleteGroup(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                LocalBroadcastManager.getInstance(IMGroupSetActivity.this).sendBroadcast(new Intent("REFRESH_GROUP_LIST"));
                setResult(Activity.RESULT_OK);
                IMGroupSetActivity.this.finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e(module + "----onError===" + errCode + "======" + errMsg);
                ToastUtils.toastLongMessage("解散群组失败,请稍后再试!");
            }
        });
    }

    /**
     * 退出群组
     */
    public void quitGroup() {
        mGroupInfoProvider.quitGroup(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                LocalBroadcastManager.getInstance(IMGroupSetActivity.this).sendBroadcast(new Intent("REFRESH_GROUP_LIST"));
                setResult(Activity.RESULT_OK);
                IMGroupSetActivity.this.finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtils.toastLongMessage("解散群组失败,请稍后再试!");

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
