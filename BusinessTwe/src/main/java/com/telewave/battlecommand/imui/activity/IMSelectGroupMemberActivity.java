package com.telewave.battlecommand.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.parse.ContactListParseObj;
import com.telewave.battlecommand.trtc.JoinRoomOperate;
import com.telewave.battlecommand.trtc.TRTCAudioCallActivity;
import com.telewave.battlecommand.trtc.TRTCVideoCallActivity;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.SampleUser;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 选择人员
 * 发送语音、视频通话
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMSelectGroupMemberActivity extends BaseActivity {
    private LinearLayout ll_contactSelectItemRoot;
    private ArrayList<SampleUser> mContactSelect = new ArrayList<>();
    private ArrayList<ContactInfoObj> mContact = new ArrayList<>();
    private TextView tv_commentOperate;
    /*已经存在的用户id*/
    private List<String> mUserIds = new ArrayList<>();
    private List<SampleUser> mGroupUsers = new ArrayList<>();
    private int roomId;
    private String trtcCallType;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_select_group_member);
        roomId = getIntent().getIntExtra("roomId", 0);
        trtcCallType = getIntent().getStringExtra("trtcCallType");
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        ll_contactSelectItemRoot = findViewById(R.id.ll_contactSelectItemRoot);
        tv_commentOperate = findViewById(R.id.tv_commentOperate);
        //对于ConstData.enterRoomUserIdSet来说，是除了自己以外的其他成员
        //此处再加入当前登录的账号
        mUserIds.addAll(ConstData.enterRoomUserIdSet);
        mUserIds.add(ConstData.userid);
        mGroupUsers.addAll(ConstData.receiveUserSet);

    }

    /**
     * 重写onBackPressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ConstData.isEnterTRTCCALL) {
            Intent intent = null;
            if (trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
                intent = new Intent(IMSelectGroupMemberActivity.this, TRTCAudioCallActivity.class);
            } else if (trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
                intent = new Intent(IMSelectGroupMemberActivity.this, TRTCVideoCallActivity.class);
            }
            startActivity(intent);
        }
    }

    private void initEvent() {
        findViewById(R.id.ll_selectGroupMemberReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (ConstData.isEnterTRTCCALL) {
                    Intent intent = null;
                    if (trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
                        intent = new Intent(IMSelectGroupMemberActivity.this, TRTCAudioCallActivity.class);
                    } else if (trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
                        intent = new Intent(IMSelectGroupMemberActivity.this, TRTCVideoCallActivity.class);
                    }
                    startActivity(intent);
                }
            }
        });
        tv_commentOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TIMConversation conversation = TIMManager.getInstance().getConversation(
                        TIMConversationType.Group, ConstData.mCurrentGroupId);
                JoinRoomOperate joinRoomUtil = new JoinRoomOperate(IMSelectGroupMemberActivity.this, roomId);
                if (trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
                    joinRoomUtil.onTRTCCallJoinRoom(conversation, Constents.ONE_TO_MULTIPE_AUDIO_CALL,
                            Constents.AUDIO_CALL_MESSAGE_DESC, mContactSelect);
                } else if (trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
                    joinRoomUtil.onTRTCCallJoinRoom(conversation, Constents.ONE_TO_MULTIPE_VIDEO_CALL,
                            Constents.VIDEO_CALL_MESSAGE_DESC, mContactSelect);
                }
                finish();
            }
        });
    }


    private void initData() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.addressAddressList, RequestMethod.POST);
        request.add("appType", "2");
        request.add("organId", ConstData.ORGANID);
        NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                Log.e("WWW", "onSucceed: " + result);
                if (!TextUtils.isEmpty(result)) {
                    ContactListParseObj contactListParseObj = IMJsonUtil.parseJsonToBean(result, ContactListParseObj.class);

                    List<ContactInfoObj> contactInfoObjs = new ArrayList<>();
                    if (null != contactListParseObj) {
                        if (null != contactListParseObj.data) {
                            contactInfoObjs.addAll(contactListParseObj.data);
                        }
                        Log.e("WWW", "contactInfoObjs.size: " + contactInfoObjs.size());
                        Log.e("WWW", "mGroupUsers.size: " + mGroupUsers.size());
                        if (null != mGroupUsers) {
                            for (SampleUser sampleUser : mGroupUsers) {
                                for (ContactInfoObj innerContactInfoObj : contactInfoObjs) {
                                    if (TextUtils.equals(innerContactInfoObj.userid, sampleUser.getUserid())) {
                                        mContact.add(innerContactInfoObj);
                                    }
                                }
                            }
                        }
                    }
                }
                Collections.sort(mContact);
                initContactSelectItemView();

            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {

            }
        }, false, false);

    }


    private void initContactSelectItemView() {
        if (null != mUserIds && !mUserIds.isEmpty()) {
            /*过滤掉不可选择的用户*/
            for (String userIded : mUserIds) {
                for (int i = 0; i < mContact.size(); i++) {
                    if (TextUtils.equals(mContact.get(i).userid, userIded)) {
                        mContact.remove(i);
                        break;
                    }
                }
            }
        }
        ll_contactSelectItemRoot.removeAllViews();
        if (null != mGroupUsers && !mGroupUsers.isEmpty()) {
            for (ContactInfoObj contact : mContact) {
                View inflate = View.inflate(this, R.layout.im_view_contact_select_item, null);
                TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                TextView tv_userDes = inflate.findViewById(R.id.tv_userDes);
                ImageView iv_selectState = inflate.findViewById(R.id.iv_selectState);
                TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);

                tv_userName.setText(contact.name);
                tv_userDes.setText(contact.organName);
                if (TextUtils.equals(contact.online, "1")) {
                    tv_onlineState.setText("(在线)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.color_55D03C));
                } else {
                    tv_onlineState.setText("(离线)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                iv_selectState.setSelected(false);
                initContactItemEvent(contact, inflate, iv_selectState);
                ll_contactSelectItemRoot.addView(inflate);
            }
        }
    }


    /**
     * item 点击事件
     *
     * @param contact
     * @param inflate
     */
    private void initContactItemEvent(final ContactInfoObj contact, View inflate, final ImageView iv_selectState) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_selectState.setSelected(!iv_selectState.isSelected());
                if (iv_selectState.isSelected()) {
                    SampleUser sampleUser = new SampleUser(contact.userid, contact.username);
                    mContactSelect.add(sampleUser);
                    Log.e("initContactItemEvent", "onClick2: " + mContactSelect.size());
                } else {
                    for (int i = 0; i < mContactSelect.size(); i++) {
                        SampleUser sampleUserTemp = mContactSelect.get(i);
                        if (contact.userid.equals(sampleUserTemp.getUserid())) {
                            mContactSelect.remove(sampleUserTemp);
                        }
                    }
                }
                Log.e("initContactItemEvent", "onClick4: " + mContactSelect.size());
                tv_commentOperate.setVisibility(null == mContactSelect || mContactSelect.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
    }

}
