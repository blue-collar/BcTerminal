package com.telewave.battlecommand.imui.service;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.activity.IMGroupActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.IMParseBaseObj;
import com.telewave.battlecommand.imui.bean.params.IMBindGroupObj;
import com.telewave.battlecommand.imui.bean.parse.IMStringParseObj;
import com.telewave.battlecommand.imui.util.IMTimeUtil;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.tencent.imsdk.TIMGroupAddOpt;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 * 聊天是绑定校验维护
 *
 * @author PF-NAN
 * @date 2019-07-27
 */
public class GroupBindCheckOperate {

    private static GroupBindCheckOperate mInstance;
    private WeakReference<Activity> mWeakActivity = null;

    private GroupBindCheckOperate() {
    }

    public static GroupBindCheckOperate getInstance() {
        if (null == mInstance) {
            synchronized (GroupBindCheckOperate.class) {
                if (null == mInstance) {
                    mInstance = new GroupBindCheckOperate();
                }
            }
        }
        return mInstance;
    }

    /**
     * 校验该警情是否绑定了群组
     *
     * @param activity
     * @param disasterInfo
     */
    public void checkGroup(Activity activity, final DisasterInfo disasterInfo) {
        mWeakActivity = new WeakReference<>(activity);
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.imGetGroupInfo, RequestMethod.POST);
        request.add("zqid", disasterInfo.getId());
        request.add("groupType", "2");
        NoHttpManager.getRequestInstance().add(mWeakActivity.get(), 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                TUIKitLog.e("======" + result);
                if (!TextUtils.isEmpty(result)) {
                    IMStringParseObj imParseBaseObj = IMJsonUtil.parseJsonToBean(result, IMStringParseObj.class);
                    if (null != imParseBaseObj) {
                        if (TextUtils.equals("1", imParseBaseObj.code)) {
                            checkIsJoinedGroup(imParseBaseObj.data);
                        } else {
                            createChatRoot(disasterInfo);
                        }
                    } else {
                        createChatRoot(disasterInfo);
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                TUIKitLog.e(url + "=====onFailed=" + tag);
            }
        }, false, false);
    }

    /**
     * 判断是否已经加入了该群组
     *
     * @param groupId
     */
    private void checkIsJoinedGroup(final String groupId) {
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(code + "===判断是否已经加入了该群组===onError====" + desc + "============" + groupId);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {

                if (null == timGroupBaseInfos || timGroupBaseInfos.isEmpty()) {
                    TUIKitLog.e("===判断是否已经加入了该群组===onSuccess========111========" + timGroupBaseInfos);

                    joinToGroup(groupId);
                } else {
                    boolean isJoinedGroup = false;
                    for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                        if (TextUtils.equals(timGroupBaseInfo.getGroupId(), groupId)) {
                            isJoinedGroup = true;
                            break;
                        }
                    }
                    TUIKitLog.e(groupId + "===判断是否已经加入了该群组===onSuccess========222========" + isJoinedGroup);

                    if (isJoinedGroup) {
                        /*需要处理警情是否结束了*/
                        Intent groupIntent = new Intent(mWeakActivity.get(), IMGroupActivity.class);
                        groupIntent.putExtra(IMKeys.INTENT_ID, groupId);
                        mWeakActivity.get().startActivity(groupIntent);
                    } else {
                        joinToGroup(groupId);
                    }
                }
            }
        });
    }

    /**
     * 主动加入群组
     *
     * @param groupId
     */
    private void joinToGroup(final String groupId) {
        String userName = SharePreferenceUtils.getDataSharedPreferences(mWeakActivity.get(), "username");
        GroupChatManagerKit.getInstance().applyJoinGroup(groupId, userName, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                TUIKitLog.e("addGroupMembers faionSuccessled, code: " + data);
                Intent groupIntent = new Intent(mWeakActivity.get(), IMGroupActivity.class);
                groupIntent.putExtra(IMKeys.INTENT_ID, groupId);
                mWeakActivity.get().startActivity(groupIntent);

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e(module + "====addGroupMembers failed, code: " + errCode + "|desc: " + errMsg);


            }
        });
    }

    private String mCreateGroupName;

    /**
     * 创建聊天室
     *
     * @param disasterInfo
     */
    private synchronized void createChatRoot(final DisasterInfo disasterInfo) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setJoinType((int) TIMGroupAddOpt.TIM_GROUP_ADD_ANY.getValue());
        mCreateGroupName = IMTimeUtil.getTimeStr(System.currentTimeMillis(), "yyyy-MM-dd");
        if (!TextUtils.isEmpty(disasterInfo.getZhdd())) {
            mCreateGroupName = disasterInfo.getZhdd().length() > 10 ? (disasterInfo.getZhdd().substring(0, 7) + "...") : disasterInfo.getZhdd();
        }
        groupInfo.setGroupName(mCreateGroupName);
        groupInfo.setGroupType(TUIKitConstants.GroupType.TYPE_CHAT_ROOM);
        groupInfo.setNotice(String.format(Locale.CHINA, "警情地址：%s", disasterInfo.getZhdd()));
        String userName = SharePreferenceUtils.getDataSharedPreferences(mWeakActivity.get(), "username");
        GroupChatManagerKit.getInstance().createGroupChat(groupInfo, userName, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                TUIKitLog.e("创建成功了======" + data);
                bindGroup(disasterInfo, (String) data, mCreateGroupName);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("-----创建警情讨论组失败--------" + module + "======" + errCode + "=======" + errMsg + "=======");
            }
        });


    }

    /**
     * 绑定群组到警情
     *
     * @param disasterInfo
     * @param groupId
     * @param mGroupName
     */
    private void bindGroup(DisasterInfo disasterInfo, final String groupId, final String mGroupName) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.imSaveGroupInfo, RequestMethod.POST);
        String paramsJson = IMJsonUtil.obj2Json(new IMBindGroupObj(groupId, mGroupName, disasterInfo.getId()));
        request.setDefineRequestBodyForJson(paramsJson);
        NoHttpManager.getRequestInstance().add(mWeakActivity.get(), 1002, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                if (!TextUtils.isEmpty(result)) {
                    IMParseBaseObj imParseBaseObj = IMJsonUtil.parseJsonToBean(result, IMParseBaseObj.class);
                    if (null != imParseBaseObj && TextUtils.equals(imParseBaseObj.code, "1")) {
                        /*创建并绑定成功了，进入讨论组*/
                        Intent groupIntent = new Intent(mWeakActivity.get(), IMGroupActivity.class);
                        groupIntent.putExtra(IMKeys.INTENT_ID, groupId);
                        mWeakActivity.get().startActivity(groupIntent);
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                TUIKitLog.e(url + "=====onFailed=" + tag);
            }
        }, false, false);

    }

}
