package com.telewave.battlecommand.imui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.imui.activity.IMForwardingSelectActivity;
import com.telewave.business.twe.R;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;

import java.util.List;


/**
 * 转发群组列表
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMForwardingSelectGroupFragment extends Fragment {

    private View mView;
    private LinearLayout ll_groupItemSelectRoot;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.im_fragment_im_forwarding_select_group, container, false);
        initView();
        return mView;
    }


    private void initView() {
        ll_groupItemSelectRoot = mView.findViewById(R.id.ll_groupItemSelectRoot);
    }

    /**
     * 加载群组信息
     */
    public void initData() {
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                initContactView(timGroupBaseInfos);

            }
        });


    }

    private void initContactView(List<TIMGroupBaseInfo> timGroupBaseInfos) {
        ll_groupItemSelectRoot.removeAllViews();
        if (null != timGroupBaseInfos && !timGroupBaseInfos.isEmpty()) {
            for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                View inflate = View.inflate(getActivity(), R.layout.im_view_group_item, null);
                TextView tv_groupName = inflate.findViewById(R.id.tv_groupName);
                inflate.findViewById(R.id.iv_next).setVisibility(View.INVISIBLE);
                tv_groupName.setText(timGroupBaseInfo.getGroupName());
                initGroupItemEvent(timGroupBaseInfo, inflate);
                ll_groupItemSelectRoot.addView(inflate);
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
                TUIKitDialog tuiKitDialog = new TUIKitDialog(getActivity())
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确认转发该消息到群组 '" + timGroupBaseInfo.getGroupName() + "'？")
                        .setDialogWidth(0.8f);
                tuiKitDialog.getBtn_neg().setTextColor(getResources().getColor(R.color.top_bar));
                tuiKitDialog.getBtn_pos().setTextColor(getResources().getColor(R.color.top_bar));

                tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((IMForwardingSelectActivity) getActivity()).forwardingMessage(timGroupBaseInfo.getGroupId(), false);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

            }
        });
    }

}