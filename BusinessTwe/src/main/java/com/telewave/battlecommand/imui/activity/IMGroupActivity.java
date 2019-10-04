package com.telewave.battlecommand.imui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.service.IMGroupOperate;
import com.telewave.battlecommand.trtc.JoinRoomOperate;
import com.telewave.battlecommand.trtc.bean.TRTCCallMessage;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.SampleUser;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreActionUnit;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoProvider;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * chat
 *
 * @author PF-NAN
 * @date 2019-07-23
 */
public class IMGroupActivity extends BaseActivity {
    private ChatLayout mChatLayout;
    private String mGroupId;
    private TitleBarLayout mTitleBar;
    private MessageLayout mMessageLayout;
    private GroupInfoProvider mGroupInfoProvider;
    private List<SampleUser> mSampleUserList = new ArrayList<>();
    private GroupInfo mGroupInfo;
    private TIMMessage mForwardingTIMMessage;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im);
        mGroupId = getIntent().getStringExtra(IMKeys.INTENT_ID);
        if (TextUtils.isEmpty(mGroupId)) {
            ToastUtils.toastLongMessage("群组id不能为空");
            finish();
            return;
        }

        mGroupInfoProvider = new GroupInfoProvider();
        mGroupInfoProvider.loadGroupInfo(mGroupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mGroupInfo = (GroupInfo) data;
                TUIKitLog.e("===groupDetailInfo====" + mGroupInfo.toString());
                ConstData.mCurrentGroupId = mGroupId;
                ConstData.mCurrentGroupName = mGroupInfo.getGroupName();
                initView();
                initEvent();
                initGroupUserData();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
            }
        });


    }

    private void initEvent() {
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        mTitleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IMGroupActivity.this, IMGroupSetActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, mGroupId);
                startActivityForResult(intent, 1001);
            }
        });
        mMessageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {


            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                Intent intent = new Intent(IMGroupActivity.this, IMUserInfoActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, messageInfo.getFromUser());
                startActivity(intent);
            }

            @Override
            public void onMessageClick(View view, MessageInfo messageInfo) {
                if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_LOCATION) {
                    TIMMessage timMessage = messageInfo.getTIMMessage();
                    TIMLocationElem element = (TIMLocationElem) timMessage.getElement(0);
                    Intent showLocationIntent = new Intent(IMGroupActivity.this, IMLocationActivity.class);
                    showLocationIntent.putExtra("latitude", element.getLatitude());
                    showLocationIntent.putExtra("longitude", element.getLongitude());
                    startActivity(showLocationIntent);
                } else if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_FILE) {
                    TIMFileElem fileElem = (TIMFileElem) messageInfo.getTIMMessage().getElement(0);
                    Intent fileIntent = new Intent(IMGroupActivity.this, IMFilePreviewActivity.class);
                    fileIntent.putExtra(IMKeys.INTENT_TAG, fileElem.getFileName());
                    fileIntent.putExtra(IMKeys.INTENT_DES, messageInfo.getDataPath());
                    startActivity(fileIntent);
                }
            }

            @Override
            public void onForwardingMessageClick(MessageInfo messageInfo) {
                mForwardingTIMMessage = new TIMMessage();
                boolean isCopySuccess = mForwardingTIMMessage.copyFrom(messageInfo.getTIMMessage());
                if (isCopySuccess) {
                    Intent forwardingIntent = new Intent(IMGroupActivity.this, IMForwardingSelectActivity.class);
                    startActivityForResult(forwardingIntent, 1003);
                }

            }
        });
    }

    private InputLayout mInputLayout;

    private void initView() {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.Group);
        chatInfo.setId(mGroupId);
        chatInfo.setChatName(mGroupInfo.getGroupName());
        // 从布局文件中获取聊天面板
        mChatLayout = findViewById(R.id.chat_layout);
        mTitleBar = mChatLayout.getTitleBar();
        mMessageLayout = mChatLayout.getMessageLayout();
        mInputLayout = mChatLayout.getInputLayout();
        mChatLayout.initDefault();

        mTitleBar.setLeftIcon(R.mipmap.back_pic);
        mTitleBar.setRightIcon(R.mipmap.icon_more_dot);
        TextView page_title = mTitleBar.findViewById(R.id.page_title);
        page_title.setTextColor(getResources().getColor(R.color.color_FFFFFF));
        page_title.setTextSize(18);
        mTitleBar.findViewById(R.id.page_title_layout).setBackgroundColor(getResources().getColor(R.color.top_bar));
        // 传入ChatInfo的实例，这个实例必须包含必要的聊天信息，一般从调用方传入
        mChatLayout.setChatInfo(chatInfo);
        // 设置自定义的消息渲染时的回调
        mMessageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());

        addAudioCallInputMore();
        addVideoCallInputMore();

        initExtSendListener();
        mChatLayout.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化群成员数据
     */
    private void initGroupUserData() {
        mGroupInfoProvider.loadGroupInfo(mGroupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                GroupInfo mGroupInfo = (GroupInfo) data;
                TUIKitLog.e("===groupDetailInfo====" + mGroupInfo.toString());
                IMGroupOperate.getInstance().getGroupMembers(mGroupInfo.getMemberDetails(), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e("getUsersProfile failed: " + code + " desc");
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result) {
                        mSampleUserList.clear();
                        if (null != result) {
                            for (int i = 0; i < result.size(); i++) {
                                mSampleUserList.add(new SampleUser(result.get(i).getIdentifier(), result.get(i).getNickName()));
                            }
                        }
                    }
                });

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
            }
        });
    }

    private void addAudioCallInputMore() {
        // 从ChatLayout里获取InputLayout
        InputLayout inputLayout = mChatLayout.getInputLayout();
        // 定义一个动作单元
        InputMoreActionUnit unit = new InputMoreActionUnit();
        // 设置单元的图标
        unit.setIconResId(R.drawable.ic_more_voice_call);
        // 设置单元的文字标题
        unit.setTitleId(R.string.im_more_audio_call_string);
        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConstData.isEnterTRTCCALL) {
                    JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMGroupActivity.this);
                    joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_MULTIPE_AUDIO_CALL,
                            Constents.AUDIO_CALL_MESSAGE_DESC, mSampleUserList);
                } else {
                    ToastUtils.toastShortMessage("正在通话中，无法再次发起");
                }
            }
        });
        // 把定义好的单元增加到更多面板
        inputLayout.addAction(unit);
    }


    private void addVideoCallInputMore() {
        // 从ChatLayout里获取InputLayout
        InputLayout inputLayout = mChatLayout.getInputLayout();
        // 定义一个动作单元
        InputMoreActionUnit unit = new InputMoreActionUnit();
        // 设置单元的图标
        unit.setIconResId(R.drawable.ic_more_video_call);
        // 设置单元的文字标题
        unit.setTitleId(R.string.im_more_video_call_string);
        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConstData.isEnterTRTCCALL) {
                    JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMGroupActivity.this);
                    joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_MULTIPE_VIDEO_CALL,
                            Constents.VIDEO_CALL_MESSAGE_DESC, mSampleUserList);
                } else {
                    ToastUtils.toastShortMessage("正在通话中，无法再次发起");
                }
            }
        });
        // 把定义好的单元增加到更多面板
        inputLayout.addAction(unit);
    }

    /**
     * 初始化扩展消息发送监听
     */
    private void initExtSendListener() {
        mInputLayout.setExtSendInputListener(new InputLayout.ExtSendInputListener() {
            @Override
            public void startSendLocation() {
                Intent locationIntent = new Intent(IMGroupActivity.this, IMChoiceAddressActivity.class);
                startActivityForResult(locationIntent, 1002);
            }
        });
    }

    public class CustomMessageDraw implements IOnCustomMessageDrawListener {

        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param parent 自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info   消息的具体信息
         */
        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info) {
            if (null != parent && null != info) {
                // 获取到自定义消息的json数据
                TIMCustomElem elem = (TIMCustomElem) info.getTIMMessage().getElement(0);
                String data = new String(elem.getData());
                View view = LayoutInflater.from(IMGroupActivity.this).inflate(R.layout.custom_trtc_call_message_item, null, false);
                // 把自定义消息view添加到TUIKit内部的父容器里
                parent.addMessageContentView(view);
                // 自定义消息view的实现，这里仅仅展示文本信息，并且实现跳转语音视频通话
                TextView textView = view.findViewById(R.id.custom_trtc_call_text);
                ImageView imageView = view.findViewById(R.id.custom_trtc_call_img);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String msgType = jsonObject.getString("msgType");
                    if (!TextUtils.isEmpty(msgType)) {
                        if (TextUtils.equals(msgType, Constents.AUDIO_CALL_MESSAGE_DECLINE_DESC)) {
                            textView.setText("语音通话未接通");
                            imageView.setVisibility(View.VISIBLE);
                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMGroupActivity.this);
                                    joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_ONE_AUDIO_CALL,
                                            Constents.AUDIO_CALL_MESSAGE_DESC, mSampleUserList);
                                }
                            });
                        } else if (TextUtils.equals(msgType, Constents.VIDEO_CALL_MESSAGE_DECLINE_DESC)) {
                            textView.setText("视频通话未接通");
                            imageView.setVisibility(View.VISIBLE);
                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMGroupActivity.this);
                                    joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_ONE_VIDEO_CALL,
                                            Constents.VIDEO_CALL_MESSAGE_DESC, mSampleUserList);
                                }
                            });
                        } else {
                            // 自定义的json数据，需要解析成bean实例
                            String msgContent = jsonObject.getString("msgContent");
                            final TRTCCallMessage trtcCallMessage = new Gson().fromJson(msgContent, TRTCCallMessage.class);
                            if (trtcCallMessage != null) {
                                if (TextUtils.equals(trtcCallMessage.getTrtcCallType(), Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
                                    textView.setText("语音通话");
                                    imageView.setVisibility(View.VISIBLE);
                                    textView.setClickable(true);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMGroupActivity.this);
                                            joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_MULTIPE_AUDIO_CALL,
                                                    Constents.AUDIO_CALL_MESSAGE_DESC, mSampleUserList);
                                        }
                                    });
                                } else if (TextUtils.equals(trtcCallMessage.getTrtcCallType(), Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
                                    textView.setText("视频通话");
                                    imageView.setVisibility(View.VISIBLE);
                                    textView.setClickable(true);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMGroupActivity.this);
                                            joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_MULTIPE_VIDEO_CALL,
                                                    Constents.VIDEO_CALL_MESSAGE_DESC, mSampleUserList);
                                        }
                                    });
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        } else if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
            PoiInfo poiInfo = data.getParcelableExtra("poiInfo");
            if (null != poiInfo) {
                mInputLayout.sendLocation(poiInfo.address, poiInfo.location.latitude, poiInfo.location.longitude);
            }
        } else if (requestCode == 1003 && resultCode == Activity.RESULT_OK) {
            String conversionId = data.getStringExtra(IMKeys.INTENT_ID);
            boolean isC2C = data.getBooleanExtra(IMKeys.INTENT_TAG, true);
            TIMConversation conversation = TIMManager.getInstance().getConversation(isC2C ? TIMConversationType.C2C : TIMConversationType.Group, conversionId);
            conversation.sendMessage(mForwardingTIMMessage, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int code, String desc) {
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    TUIKitLog.e("消息转发成功===");
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mChatLayout) {
            mChatLayout.exitChat();
        }
    }
}
