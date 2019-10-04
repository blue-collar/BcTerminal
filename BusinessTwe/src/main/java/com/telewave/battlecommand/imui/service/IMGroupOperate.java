package com.telewave.battlecommand.imui.service;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.IMParseBaseObj;
import com.telewave.lib.base.ConstData;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupAddOpt;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 群组操作
 *
 * @author PF-NAN
 * @date 2019-07-24
 */
public class IMGroupOperate {


    private static IMGroupOperate mInstance;
    private WeakReference<Activity> mWeakActivity = null;

    private IMGroupOperate() {
    }

    public static IMGroupOperate getInstance() {
        if (null == mInstance) {
            synchronized (IMGroupOperate.class) {
                if (null == mInstance) {
                    mInstance = new IMGroupOperate();
                }
            }
        }
        return mInstance;
    }

    /**
     * 创建群组
     *
     * @param contactInfoObjs
     */
    public void createGroup(Activity activity, List<ContactInfoObj> contactInfoObjs) {
        mWeakActivity = new WeakReference<>(activity);
        if (null != contactInfoObjs && !contactInfoObjs.isEmpty()) {
            StringBuilder groupName = new StringBuilder(ConstData.username);
            List<GroupMemberInfo> groupMemberInfos = new ArrayList<>();
            for (int i = 0; i < contactInfoObjs.size(); i++) {
                ContactInfoObj contactInfoObj = contactInfoObjs.get(i);
                groupName.append(TextUtils.concat("、", contactInfoObj.name));
                GroupMemberInfo groupMemberInfo = new GroupMemberInfo();
                groupMemberInfo.setAccount(contactInfoObj.userid);
                groupMemberInfos.add(groupMemberInfo);
            }
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setJoinType((int) TIMGroupAddOpt.TIM_GROUP_ADD_ANY.getValue());
            final String createGroupName = groupName.length() > 10 ? (groupName.substring(0, 7) + "...") : groupName.toString();

            groupInfo.setGroupName(createGroupName);
            groupInfo.setGroupType(TUIKitConstants.GroupType.TYPE_CHAT_ROOM);
            groupInfo.setMemberDetails(groupMemberInfos);
            groupInfo.setNotice("");
            GroupChatManagerKit.getInstance().createGroupChat(groupInfo, ConstData.username, new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    mWeakActivity.get().setResult(Activity.RESULT_OK);
                    mWeakActivity.get().finish();
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    TUIKitLog.e("-----创建失败--------" + module + "======" + errCode + "=======" + errMsg + "=======" + createGroupName);
                    Toast.makeText(mWeakActivity.get(), "创建失败,请稍后再试!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 批量获取群组用户资料
     *
     * @param memberDetails
     * @param callBack
     */
    public void getGroupMembers(List<GroupMemberInfo> memberDetails, TIMValueCallBack<List<TIMUserProfile>> callBack) {
        if (null != memberDetails && !memberDetails.isEmpty()) {
            /*待获取用户资料的用户列表*/
            List<String> users = new ArrayList<String>();
            for (GroupMemberInfo memberDetail : memberDetails) {
                users.add(memberDetail.getAccount());
            }
            /*获取用户资料*/
            TIMFriendshipManager.getInstance().getUsersProfile(users, true, callBack);
        }
    }

    /**
     * 批量获取群组用户资料
     *
     * @param timGroupMemberInfos
     * @param callBack
     */
    public void getTimGroupMembers(List<TIMGroupMemberInfo> timGroupMemberInfos, TIMValueCallBack<List<TIMUserProfile>> callBack) {
        if (null != timGroupMemberInfos && !timGroupMemberInfos.isEmpty()) {
            /*待获取用户资料的用户列表*/
            List<String> users = new ArrayList<String>();
            for (TIMGroupMemberInfo memberDetail : timGroupMemberInfos) {
                users.add(memberDetail.getUser());
            }
            /*获取用户资料*/
            TIMFriendshipManager.getInstance().getUsersProfile(users, true, callBack);
        }
    }

    /**
     * 邀请用户加群
     *
     * @param activity
     * @param groupId
     * @param contactSelect
     */
    public void inviteMemberToGroup(Activity activity, String groupId, ArrayList<ContactInfoObj> contactSelect) {
        mWeakActivity = new WeakReference<>(activity);
        if (null != contactSelect && !contactSelect.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < contactSelect.size(); i++) {
                ContactInfoObj contactInfoObj = contactSelect.get(i);
                ids.append(contactInfoObj.userid);
                if (i != contactSelect.size() - 1) {
                    ids.append(",");
                }
            }
            Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.imAddGroupMember, RequestMethod.POST);
            request.add("groupId", groupId);
            request.add("userids", ids.toString());
            request.add("userType", "2");
            NoHttpManager.getRequestInstance().add(mWeakActivity.get(), 1001, request, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    String result = response.get();
                    TUIKitLog.e("===result===" + result);
                    if (!TextUtils.isEmpty(result)) {
                        IMParseBaseObj parseBaseObj = IMJsonUtil.parseJsonToBean(result, IMParseBaseObj.class);
                        if (null != parseBaseObj && TextUtils.equals(parseBaseObj.code, "1")) {
                            /*邀请成功*/
                            Intent intent = new Intent();
                            intent.putExtra(IMKeys.INTENT_DES, parseBaseObj.msg);
                            mWeakActivity.get().setResult(Activity.RESULT_OK, intent);
                            mWeakActivity.get().finish();
                        }
                    }
                }

                @Override
                public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                    TUIKitLog.e(url + "=====onFailed=" + tag);
                }
            }, false, true);
        }
    }

}
