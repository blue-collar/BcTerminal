package com.telewave.battlecommand.imui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telewave.battlecommand.imui.activity.IMGroupActivity;
import com.telewave.battlecommand.imui.activity.IMSingleActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.util.IMDialogUtil;
import com.telewave.business.twe.R;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;


/**
 * 会话列表
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMSessionFragment extends Fragment {

    private View mView;
    private ConversationLayout mConversationLayout;
    private TitleBarLayout mTitleBarLayout;

    private ConversationListLayout mConversationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.im_fragment_im_session, container, false);
        mConversationLayout = mView.findViewById(R.id.conversation_layout);
        mTitleBarLayout = mConversationLayout.findViewById(R.id.conversation_title);
        mTitleBarLayout.setVisibility(View.GONE);
        mConversationList = mConversationLayout.getConversationList();
        initData();
        return mView;
    }

    /**
     * 初始化控件
     */
    public void initData() {
        mConversationLayout.initDefault();
        mConversationList.setSingleItemImage(R.mipmap.im_ic_member);
        mConversationList.setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), conversationInfo.isGroup() ? IMGroupActivity.class : IMSingleActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, conversationInfo.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1001);
            }
        });
        mConversationList.setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                View longClickOperate = View.inflate(getActivity(), R.layout.im_view_session_operate, null);
                TextView tv_topOperate = longClickOperate.findViewById(R.id.tv_topOperate);
                TextView tv_deleteOperate = longClickOperate.findViewById(R.id.tv_deleteOperate);

                if (conversationInfo.isTop()) {
                    tv_topOperate.setText("取消置顶");
                } else {
                    tv_topOperate.setText("置顶聊天");
                }
                Dialog dialogOperate = IMDialogUtil.getDialog(longClickOperate, 0, 0, Gravity.CENTER, true);
                initLongClickEvent(dialogOperate, tv_topOperate, tv_deleteOperate, position, conversationInfo);
                dialogOperate.show();
            }
        });


    }

    /**
     * 初始化长按事件
     *
     * @param dialogOperate
     * @param tv_topOperate
     * @param tv_deleteOperate
     * @param position
     * @param conversationInfo
     */
    private void initLongClickEvent(final Dialog dialogOperate, TextView tv_topOperate, TextView tv_deleteOperate, final int position, final ConversationInfo conversationInfo) {
        tv_topOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*置顶会话*/
                mConversationLayout.setConversationTop(position, conversationInfo);
                dialogOperate.dismiss();
            }
        });
        tv_deleteOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*删除会话*/
                mConversationLayout.deleteConversation(position, conversationInfo);
                dialogOperate.dismiss();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
