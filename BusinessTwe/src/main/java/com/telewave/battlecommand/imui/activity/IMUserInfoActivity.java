package com.telewave.battlecommand.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.parse.ContactInfoParseObj;
import com.telewave.battlecommand.trtc.JoinRoomOperate;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.SampleUser;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户详情
 *
 * @author PF-NAN
 * @date 2019-07-26
 */
public class IMUserInfoActivity extends BaseActivity {

    private String mUserId;
    private TextView tv_userName;
    private TextView tv_userAcount;
    private TextView tv_userOrg;
    private LinearLayout ll_rootView;
    private ChatLayout mChatLayout;
    private ContactInfoObj contactInfoObj;
    private int mUserType = 2;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_userinfo);

        mUserType = getIntent().getIntExtra(IMKeys.INTENT_TAG, 2);

        TUIKitLog.e("setUpViewAndData--------------");

        ll_rootView = findViewById(R.id.ll_rootView);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userAcount = findViewById(R.id.tv_userAcount);
        tv_userOrg = findViewById(R.id.tv_userOrg);
        mUserId = getIntent().getStringExtra(IMKeys.INTENT_ID);
        // 从布局文件中获取聊天面板
        mChatLayout = findViewById(R.id.chat_layout);
        // 单聊面板的默认UI和交互初始化
        mChatLayout.initDefault();
        initEvent();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TUIKitLog.e("onNewIntent--------------");
        // 单聊面板的默认UI和交互初始化
        mChatLayout.initDefault();
        initEvent();
        initData();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        findViewById(R.id.ll_userInfoReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.ll_sendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ConstData.userid.equals(mUserId)) {
                    Intent intent = new Intent(IMUserInfoActivity.this, IMSingleActivity.class);
                    intent.putExtra(IMKeys.INTENT_ID, mUserId);
                    startActivity(intent);
                } else {
                    ToastUtils.toastShortMessage("不能和自己发消息");
                }
            }
        });
        findViewById(R.id.ll_sendAudioAndVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ConstData.userid.equals(mUserId)) {
                    choiceCallType();
                } else {
                    ToastUtils.toastShortMessage("不能和自己音视频通话");
                }
            }
        });
    }

    /**
     * 获取用户资料
     */
    private void initData() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.imGetUserInfo, RequestMethod.GET);
        request.add("userId", mUserId);
        request.add("userType", String.valueOf(mUserType));
        NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                if (!TextUtils.isEmpty(result)) {
                    ContactInfoParseObj contactInfoParseObj = IMJsonUtil.parseJsonToBean(result, ContactInfoParseObj.class);
                    if (null != contactInfoParseObj && null != contactInfoParseObj.data) {
                        ll_rootView.setVisibility(View.VISIBLE);
                        contactInfoObj = contactInfoParseObj.data;
                        tv_userName.setText(contactInfoObj.username);
                        tv_userAcount.setText(TextUtils.concat("账号：", contactInfoObj.loginname));
                        tv_userOrg.setText(TextUtils.concat("单位：", contactInfoObj.organname));

                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setType(TIMConversationType.C2C);
                        chatInfo.setId(mUserId);
                        chatInfo.setChatName(contactInfoObj.username);
                        // 传入ChatInfo的实例，这个实例必须包含必要的聊天信息，一般从调用方传入
                        mChatLayout.setChatInfo(chatInfo);
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
            }
        }, false, false);
    }

    private void choiceCallType() {
        View view = LayoutInflater.from(IMUserInfoActivity.this).inflate(R.layout.choice_call_type, null);
        LinearLayout audioCallLayout = (LinearLayout) view.findViewById(R.id.audio_call_layout);
        LinearLayout videoCallLayout = (LinearLayout) view.findViewById(R.id.video_call_layout);
        AlertDialog.Builder builder = new AlertDialog.Builder(IMUserInfoActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final List<SampleUser> sampleUserList = new ArrayList<>();
        SampleUser sampleUser = new SampleUser(contactInfoObj.userid, contactInfoObj.username);
        sampleUserList.add(sampleUser);
        audioCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMUserInfoActivity.this);
                joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_ONE_AUDIO_CALL,
                        Constents.AUDIO_CALL_MESSAGE_DESC, sampleUserList);
                dialog.dismiss();
            }
        });
        videoCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMUserInfoActivity.this);
                joinRoomUtil.onTRTCCallJoinRoom(mChatLayout, Constents.ONE_TO_ONE_VIDEO_CALL,
                        Constents.VIDEO_CALL_MESSAGE_DESC, sampleUserList);
                dialog.dismiss();
            }
        });

    }
}
