package com.tencent.qcloud.tim.uikit.modules.conversation.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.lib.base.util.DateTimeUtil;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationIconView;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationCommonHolder extends ConversationBaseHolder {

    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected ConversationIconView conversationIconView;

    public ConversationCommonHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
    }

    public void layoutViews(ConversationInfo conversation, int position) {
        MessageInfo lastMsg = conversation.getLastMessage();
        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.top_conversation_color));
        } else {
            leftItemLayout.setBackgroundColor(Color.WHITE);
        }
        conversationIconView.setIconUrls(null); // 如果自己要设置url，这行代码需要删除
        if (conversation.isGroup()) {
            if (mAdapter.mGroupItemImage == 0) {
                if (mAdapter.mIsShowItemRoundIcon) {
                    conversationIconView.setBitmapResId(R.drawable.conversation_group);
                } else {
                    conversationIconView.setDefaultImageResId(R.drawable.conversation_group);
                }
            } else {
                conversationIconView.setImageResource(mAdapter.mGroupItemImage);
            }

        } else {
            if (mAdapter.mSingleItemImage == 0) {
                if (mAdapter.mIsShowItemRoundIcon) {
                    conversationIconView.setBitmapResId(R.drawable.conversation_c2c);
                } else {
                    conversationIconView.setDefaultImageResId(R.drawable.conversation_c2c);
                }
            } else {
                conversationIconView.setImageResource(mAdapter.mSingleItemImage);
            }

        }
        initConversationTitle(titleText, conversation);
        messageText.setText("");
        timelineText.setText("");
        if (lastMsg != null) {
            if (lastMsg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
                if (lastMsg.isSelf())
                    messageText.setText("您撤回了一条消息");
                else if (lastMsg.isGroup()) {
                    messageText.setText(lastMsg.getFromUser() + "撤回了一条消息");
                } else {
                    messageText.setText("对方撤回了一条消息");
                }

            } else {
                if (lastMsg.getExtra() != null)
                    messageText.setText(lastMsg.getExtra().toString());
            }

            timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(lastMsg.getMsgTime())));
        }


        if (conversation.getUnRead() > 0) {
            unreadText.setVisibility(View.VISIBLE);
            unreadText.setText("" + conversation.getUnRead());
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (mAdapter.mDateTextSize != 0) {
            timelineText.setTextSize(mAdapter.mDateTextSize);
        }
        if (mAdapter.mBottomTextSize != 0) {
            messageText.setTextSize(mAdapter.mBottomTextSize);
        }
        if (mAdapter.mTopTextSize != 0) {
            titleText.setTextSize(mAdapter.mTopTextSize);
        }
//        if (mIsShowUnreadDot) {
//            holder.unreadText.setVisibility(View.GONE);
//        }

        //// 由子类设置指定消息类型的views
        layoutVariableViews(conversation, position);
    }

    /**
     * 初始化会话标题
     * 获取用户资料后设置用户标题
     * 获取群组资料后设置群组昵称
     *
     * @param titleText
     * @param conversation
     */
    private void initConversationTitle(final TextView titleText, final ConversationInfo conversation) {
        titleText.setText(conversation.getTitle());
        List<String> ids = new ArrayList<>();
        ids.add(conversation.getId());
        if (conversation.isGroup()) {
            TIMGroupManager.getInstance().getGroupInfo(ids, new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                @Override
                public void onError(final int code, final String desc) {
                    titleText.setText(conversation.getTitle());
                }

                @Override
                public void onSuccess(final List<TIMGroupDetailInfo> timGroupDetailInfos) {
                    if (timGroupDetailInfos.size() > 0) {
                        titleText.setText(timGroupDetailInfos.get(0).getGroupName());

                    } else {
                        titleText.setText(conversation.getTitle());
                    }
                }
            });
        } else {
            TIMFriendshipManager.getInstance().getUsersProfile(ids, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int code, String desc) {
                    titleText.setText(conversation.getTitle());
                }

                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    if (timUserProfiles.size() > 0) {
                        titleText.setText(timUserProfiles.get(0).getNickName());

                    } else {
                        titleText.setText(conversation.getTitle());

                    }
                }
            });

        }
    }

    public void layoutVariableViews(ConversationInfo conversationInfo, int position) {

    }
}
