package com.telewave.battlecommand.imui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.imui.activity.IMGroupActivity;
import com.telewave.battlecommand.imui.activity.IMSelectContactActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.business.twe.R;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.util.List;


/**
 * 群组列表
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMGroupListFragment extends Fragment {

    private View mView;
    private LinearLayout ll_groupItemRoot;
    private IMGroupListReceiver mReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mReceiver = new IMGroupListReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter("REFRESH_GROUP_LIST"));
        mView = inflater.inflate(R.layout.im_fragment_im_grouplist, container, false);
        initView();
        initEvent();
        return mView;
    }

    private void initEvent() {
        mView.findViewById(R.id.tv_createGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), IMSelectContactActivity.class), 1001);
            }
        });
    }

    private void initView() {
        ll_groupItemRoot = mView.findViewById(R.id.ll_groupItemRoot);
    }

    /**
     * 加载群组信息
     */
    public void initData() {
        if (this.isVisible()) {
            TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(code + "--onError-----" + desc);

                }

                @Override
                public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                    TUIKitLog.e("--onSuccess-----" + timGroupBaseInfos.toString());
                    initContactView(timGroupBaseInfos);

                }
            });
        }
    }

    private void initContactView(List<TIMGroupBaseInfo> timGroupBaseInfos) {
        if (null != ll_groupItemRoot) {
            ll_groupItemRoot.removeAllViews();
            if (null != timGroupBaseInfos && !timGroupBaseInfos.isEmpty()) {
                for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                    View inflate = View.inflate(getActivity(), R.layout.im_view_group_item, null);
                    TextView tv_groupName = inflate.findViewById(R.id.tv_groupName);
                    tv_groupName.setText(timGroupBaseInfo.getGroupName());
                    initGroupItemEvent(timGroupBaseInfo, inflate);
                    ll_groupItemRoot.addView(inflate);
                }
            }
        }

    }

    /**
     * 跳转到群聊
     *
     * @param timGroupBaseInfo
     * @param inflate
     */
    private void initGroupItemEvent(final TIMGroupBaseInfo timGroupBaseInfo, View inflate) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IMGroupActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, timGroupBaseInfo.getGroupId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1001);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TUIKitLog.e("---------------222222---");

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mReceiver) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        }
    }

    private class IMGroupListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent && TextUtils.equals("REFRESH_GROUP_LIST", intent.getAction())) {
                TUIKitLog.e("--收到广播-----" + intent.getAction());

                TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e(code + "--onError-----" + desc);

                    }

                    @Override
                    public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                        TUIKitLog.e("--onSuccess-----" + timGroupBaseInfos.toString());
                        initContactView(timGroupBaseInfos);

                    }
                });
            }
        }
    }
}