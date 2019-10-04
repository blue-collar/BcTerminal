package com.telewave.battlecommand.trtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.telewave.battlecommand.contract.HomeWatcher;
import com.telewave.battlecommand.imui.activity.IMSelectGroupMemberActivity;
import com.telewave.battlecommand.imui.activity.IMSingleActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.service.FloatAudioWindowService;
import com.telewave.battlecommand.trtc.customVideo.TestRenderVideoFrame;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.util.ActivityUtil;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.TRTCStatistics;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 腾讯实时音视频
 * 语音通话
 */
public class TRTCAudioCallActivity extends Activity implements View.OnClickListener,
        TRTCSettingDialog.ISettingListener, TRTCMoreDialog.IMoreListener,
        TRTCAudioViewLayout.ITRTCVideoViewLayoutListener, TRTCAudioViewLayout.OnAudioToChatClickListener,
        TRTCCallMessageManager.TRTCAudioCallMessageCancelListener {
    private final static String TAG = TRTCAudioCallActivity.class.getSimpleName();

    private boolean bEnableAudio = true;
    private TextView tvRoomId;
    private ImageView ivVoice;
    private TRTCSettingDialog settingDlg;
    private TRTCMoreDialog moreDlg;
    private TRTCAudioViewLayout mAudioViewLayout;
    //通话计时
    private Chronometer callTimeChronometer;

    /**
     * TRTC SDK 视频通话房间进入所必须的参数
     */
    private TRTCCloudDef.TRTCParams trtcParams;
    // TRTC SDK 实例对象
    private TRTCCloud trtcCloud;
    // TRTC SDK 回调监听
    private TRTCCloudListenerImpl trtcListener;

    private HashSet<String> mRoomMembers = new HashSet<>();

    private int mSdkAppId = -1;
    private String trtcCallFrom;
    private String trtcCallType;
    private int roomId;

    private CountDownTimer countDownTimer;
    private String userName;

    private Intent floatAudioIntent;
    private ImageView trtcSmallIv;
    private HomeWatcher mHomeWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityUtil.addDestoryActivityToMap(TRTCAudioCallActivity.this, TAG);
        TRTCCallMessageManager.getInstance().setTRTCAudioCallMessageListener(this);

        //获取前一个页面得到的进房参数
        Intent intent = getIntent();
        long mSdkAppIdTemp = intent.getLongExtra("sdkAppId", 0);
        mSdkAppId = Integer.parseInt(String.valueOf(mSdkAppIdTemp));
        roomId = intent.getIntExtra("roomId", 0);
        trtcCallFrom = intent.getStringExtra("trtcCallFrom");
        trtcCallType = intent.getStringExtra("trtcCallType");
        ConstData.currentTrtcCallType = trtcCallType;
        ConstData.currentRoomId = roomId + "";
        String userSig = intent.getStringExtra("userSig");
        String userId = SharePreferenceUtils.getDataSharedPreferences(TRTCAudioCallActivity.this, "userid");
        trtcParams = new TRTCCloudDef.TRTCParams(mSdkAppId, userId, userSig, roomId, "", "");

        //初始化 UI 控件
        initView();
        //创建 TRTC SDK 实例
        trtcListener = new TRTCCloudListenerImpl(this);
        trtcCloud = TRTCCloud.sharedInstance(this);
        trtcCloud.setListener(trtcListener);
        //开始进入视频通话房间
        enterRoom();

        /** 倒计时30秒，一次1秒 */
        countDownTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                if (!TRTCAudioCallActivity.this.isFinishing() && ConstData.enterRoomUserIdSet.size() > 0) {
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                //倒计时全部结束执行操作
                if (!TRTCAudioCallActivity.this.isFinishing() && ConstData.enterRoomUserIdSet.size() == 0) {
                    exitRoom();
                }
            }
        };
        countDownTimer.start();

        /**
         * home键监听相关
         */
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                //按了HOME键
                //如果悬浮窗没有显示 就开启服务展示悬浮窗
                if (!Constents.isShowFloatWindow) {
                    startAudioService();
                }
            }

            @Override
            public void onRecentAppsPressed() {
                //最近app任务列表按键
                if (!Constents.isShowFloatWindow) {
                    startAudioService();
                }
            }

        });
        mHomeWatcher.startWatch();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (trtcCloud != null) {
            trtcCloud = null;
        }
        //销毁 trtc 实例
        TRTCCloud.destroySharedInstance();
        ConstData.isEnterTRTCCALL = false;
        //不显示悬浮框
        if (floatAudioIntent != null) {
            stopService(floatAudioIntent);
        }
        if (mHomeWatcher != null) {
            mHomeWatcher.stopWatch();// 在销毁时停止监听，不然会报错的。
        }
//        ActivityUtil.destoryActivity(TAG);
//        unregisterReceiver(receiverTest);

    }

    /**
     * 重写onBackPressed
     * 屏蔽返回键
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();//要去掉这句
    }

    /**
     * 初始化界面控件，包括主要的视频显示View，以及底部的一排功能按钮
     */
    private void initView() {
        setContentView(R.layout.activity_trtc_audio);
        trtcSmallIv = (ImageView) findViewById(R.id.trtc_small_iv);
        trtcSmallIv.setOnClickListener(this);
        initClickableLayout(R.id.ll_voice);
        initClickableLayout(R.id.ll_add);

        mAudioViewLayout = (TRTCAudioViewLayout) findViewById(R.id.audio_ll_mainview);
        mAudioViewLayout.setUserId(trtcParams.userId);
        mAudioViewLayout.setListener(this);
        mAudioViewLayout.setOnAudioToChatClickListener(this);
        TXCloudVideoView localVideoView = mAudioViewLayout.getCloudVideoViewByIndex(0);
        localVideoView.setUserId(trtcParams.userId);
        callTimeChronometer = (Chronometer) findViewById(R.id.call_time_chronometer);
        ivVoice = (ImageView) findViewById(R.id.iv_mic);
        tvRoomId = (TextView) findViewById(R.id.tv_room_id);
        userName = SharePreferenceUtils.getDataSharedPreferences(TRTCAudioCallActivity.this, "username");
        tvRoomId.setText(userName + "（自己）");
        settingDlg = new TRTCSettingDialog(this, this);
        moreDlg = new TRTCMoreDialog(this, this);
        findViewById(R.id.audio_ring_off_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitRoom();
            }
        });

    }

    private LinearLayout initClickableLayout(int resId) {
        LinearLayout layout = (LinearLayout) findViewById(resId);
        layout.setOnClickListener(this);
        return layout;
    }


    /**
     * 加入视频房间：需要 TRTCNewViewActivity 提供的  TRTCParams 函数
     */
    private void enterRoom() {
        trtcCloud.startLocalAudio();
        trtcCloud.setGSensorMode(TRTCCloudDef.TRTC_GSENSOR_MODE_UIFIXLAYOUT);
        mRoomMembers.clear();
        trtcCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);

    }

    /**
     * 退出视频房间
     */
    private void exitRoom() {
        if (trtcCloud != null) {
            trtcCloud.exitRoom();
        }
        ToastUtils.toastShortMessage("通话已结束");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.trtc_small_iv) {
            startAudioService();
        } else if (v.getId() == R.id.ll_voice) {
            onEnableAudio();
        } else if (v.getId() == R.id.ll_add) {
            if (Constents.ONE_TO_ONE_AUDIO_CALL.equals(trtcCallType)
                    || Constents.ONE_TO_ONE_VIDEO_CALL.equals(trtcCallType)) {
                ToastUtils.toastShortMessage("单聊时不能添加其他成员");
            } else {
                onOpenChoiceMember();
            }
        }
    }

    /**
     * 开启悬浮Audio服务
     */
    private void startAudioService() {
        //最小化Activity
        moveTaskToBack(true);
        //开启服务显示悬浮框
        floatAudioIntent = new Intent(this, FloatAudioWindowService.class);
        startService(floatAudioIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //不显示悬浮框
        if (floatAudioIntent != null) {
            stopService(floatAudioIntent);
        }
    }

    /**
     * 开启/关闭音频上行
     */
    private void onEnableAudio() {
        bEnableAudio = !bEnableAudio;
        trtcCloud.muteLocalAudio(!bEnableAudio);
        ivVoice.setImageResource(bEnableAudio ? R.mipmap.mic_enable : R.mipmap.mic_disable);
    }

    /**
     * 进入选择微站选人页面
     */
    private void onOpenChoiceMember() {
        Intent intent = new Intent(TRTCAudioCallActivity.this, IMSelectGroupMemberActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("trtcCallType", trtcCallType);
        startActivity(intent);
    }

    @Override
    public void onComplete() {
        setVideoFillMode(settingDlg.isVideoVertical());
        moreDlg.updateVideoFillMode(settingDlg.isVideoVertical());
    }

    /**
     * SDK内部状态回调
     */
    static class TRTCCloudListenerImpl extends TRTCCloudListener implements TRTCCloudListener.TRTCVideoRenderListener {

        private WeakReference<TRTCAudioCallActivity> mContext;
        private HashMap<String, TestRenderVideoFrame> mCustomRender;

        public TRTCCloudListenerImpl(TRTCAudioCallActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
            mCustomRender = new HashMap<>(10);
        }

        /**
         * 加入房间
         */
        @Override
        public void onEnterRoom(long elapsed) {
            final TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {
                activity.mAudioViewLayout.onRoomEnter();
                activity.updateCloudMixtureParams();
                Constents.audioCallStartTime = SystemClock.elapsedRealtime();
                activity.callTimeChronometer.setBase(Constents.audioCallStartTime);
                activity.callTimeChronometer.start();

            }
        }

        /**
         * 离开房间
         */
        @Override
        public void onExitRoom(int reason) {
            TRTCAudioCallActivity activity = mContext.get();
            ConstData.enterRoomUserIdSet.clear();
            ConstData.receiveUserSet.clear();
            ConstData.isEnterTRTCCALL = false;
            if (activity != null) {
                activity.callTimeChronometer.stop();
                activity.finish();
            }
        }

        /**
         * ERROR 大多是不可恢复的错误，需要通过 UI 提示用户
         */
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            TRTCAudioCallActivity activity = mContext.get();
            if (activity == null) {
                return;
            }
            if (errCode == TXLiteAVCode.ERR_ROOM_REQUEST_TOKEN_HTTPS_TIMEOUT ||
                    errCode == TXLiteAVCode.ERR_ROOM_REQUEST_IP_TIMEOUT ||
                    errCode == TXLiteAVCode.ERR_ROOM_REQUEST_ENTER_ROOM_TIMEOUT) {
                Toast.makeText(activity, "进房超时，请检查网络或稍后重试:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_ROOM_REQUEST_TOKEN_INVALID_PARAMETER ||
                    errCode == TXLiteAVCode.ERR_ENTER_ROOM_PARAM_NULL ||
                    errCode == TXLiteAVCode.ERR_SDK_APPID_INVALID ||
                    errCode == TXLiteAVCode.ERR_ROOM_ID_INVALID ||
                    errCode == TXLiteAVCode.ERR_USER_ID_INVALID ||
                    errCode == TXLiteAVCode.ERR_USER_SIG_INVALID) {
                Toast.makeText(activity, "进房参数错误:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_ACCIP_LIST_EMPTY ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_UNPACKING_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_TOKEN_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_ALLOCATE_ACCESS_FAILED ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_GENERATE_SIGN_FAILED ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_TOKEN_TIMEOUT ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_INVALID_COMMAND ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_GENERATE_KEN_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_GENERATE_TOKEN_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_DATABASE ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_BAD_ROOMID ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_BAD_SCENE_OR_ROLE ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_ROOMID_EXCHANGE_FAILED ||
                    errCode == TXLiteAVCode.ERR_SERVER_INFO_STRGROUP_HAS_INVALID_CHARS ||
                    errCode == TXLiteAVCode.ERR_SERVER_ACC_TOKEN_TIMEOUT ||
                    errCode == TXLiteAVCode.ERR_SERVER_ACC_SIGN_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_ACC_SIGN_TIMEOUT ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_INVALID_ROOMID ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_CREATE_ROOM_FAILED ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_SIGN_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_SIGN_TIMEOUT ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_ADD_USER_FAILED ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_FIND_USER_FAILED ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_SWITCH_TERMINATION_FREQUENTLY ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_LOCATION_NOT_EXIST ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_ROUTE_TABLE_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_INVALID_PARAMETER) {
                Toast.makeText(activity, "进房失败，请稍后重试:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_SERVER_CENTER_ROOM_FULL ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_REACH_PROXY_MAX) {
                Toast.makeText(activity, "进房失败，房间满了，请稍后重试:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_SERVER_CENTER_ROOM_ID_TOO_LONG) {
                Toast.makeText(activity, "进房失败，roomID超出有效范围:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_SERVER_ACC_ROOM_NOT_EXIST ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_ROOM_NOT_EXIST) {
                Toast.makeText(activity, "进房失败，请确认房间号正确:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_SERVER_INFO_SERVICE_SUSPENDED) {
                Toast.makeText(activity, "进房失败，请确认腾讯云实时音视频账号状态是否欠费:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode == TXLiteAVCode.ERR_SERVER_INFO_PRIVILEGE_FLAG_ERROR ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_NO_PRIVILEDGE_CREATE_ROOM ||
                    errCode == TXLiteAVCode.ERR_SERVER_CENTER_NO_PRIVILEDGE_ENTER_ROOM) {
                Toast.makeText(activity, "进房失败，无权限进入房间:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }

            if (errCode <= TXLiteAVCode.ERR_SERVER_SSO_SIG_EXPIRED &&
                    errCode >= TXLiteAVCode.ERR_SERVER_SSO_INTERNAL_ERROR) {
                // 错误参考 https://cloud.tencent.com/document/product/269/1671#.E5.B8.90.E5.8F.B7.E7.B3.BB.E7.BB.9F
                Toast.makeText(activity, "进房失败，userSig错误:" + errCode + "[" + errMsg + "]", Toast.LENGTH_SHORT).show();
                activity.exitRoom();
                return;
            }
            Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
        }

        /**
         * WARNING 大多是一些可以忽略的事件通知，SDK内部会启动一定的补救机制
         */
        @Override
        public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onWarning");
        }

        /**
         * 有新的用户加入了当前视频房间
         */
        @Override
        public void onUserEnter(String userId) {
            TRTCAudioCallActivity activity = mContext.get();
            ConstData.enterRoomUserIdSet.add(userId);
            Log.e(TAG, "onUserEnter: " + userId);
            Log.e(TAG, "onUserEnter: " + ConstData.enterRoomUserIdSet.size());
            if (activity != null) {
                // 创建一个View用来显示新的一路画面
                TXCloudVideoView renderView = activity.mAudioViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                if (renderView != null) {
                    // 设置仪表盘数据显示
                    renderView.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * 有用户离开了当前视频房间
         */
        @Override
        public void onUserExit(String userId, int reason) {
            TRTCAudioCallActivity activity = mContext.get();
            ConstData.enterRoomUserIdSet.remove(userId);
            if (activity != null) {
                if (activity.trtcCallFrom.equals(userId)) {
                    activity.exitRoom();
                } else {
                    if (ConstData.enterRoomUserIdSet.size() == 0) {
                        activity.exitRoom();
                    }
                }

                //停止观看画面
                activity.trtcCloud.stopRemoteView(userId);
                activity.trtcCloud.stopRemoteSubStreamView(userId);
                //更新视频UI
                activity.mAudioViewLayout.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                activity.mAudioViewLayout.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
                activity.mRoomMembers.remove(userId);
                activity.updateCloudMixtureParams();
                TestRenderVideoFrame customRender = mCustomRender.get(userId);
                if (customRender != null) {
                    customRender.stop();
                    mCustomRender.remove(userId);
                }
            }
        }

        /**
         * 有用户屏蔽了画面
         */
        @Override
        public void onUserVideoAvailable(final String userId, boolean available) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {
                if (available) {
                    final TXCloudVideoView renderView = activity.mAudioViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                    if (renderView != null) {
                        // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
                        activity.trtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                        activity.trtcCloud.startRemoteView(userId, renderView);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                renderView.setUserId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                            }
                        });
                    }

                    activity.mRoomMembers.add(userId);
                    activity.updateCloudMixtureParams();
                } else {
                    activity.trtcCloud.stopRemoteView(userId);
                    //activity.mAudioViewLayout.onMemberLeave(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);

                    activity.mRoomMembers.remove(userId);
                    activity.updateCloudMixtureParams();
                }
                activity.mAudioViewLayout.updateVideoStatus(userId, available);
            }

        }

        @Override
        public void onUserSubStreamAvailable(final String userId, boolean available) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {
                if (available) {
                    final TXCloudVideoView renderView = activity.mAudioViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
                    if (renderView != null) {
                        // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
                        activity.trtcCloud.setRemoteSubStreamViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                        activity.trtcCloud.startRemoteSubStreamView(userId, renderView);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                renderView.setUserId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
                            }
                        });
                    }

                } else {
                    activity.trtcCloud.stopRemoteSubStreamView(userId);
                    activity.mAudioViewLayout.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
                }
            }
        }

        /**
         * 有用户屏蔽了声音
         */
        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {
                if (available) {
                    final TXCloudVideoView renderView = activity.mAudioViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                    if (renderView != null) {
                        renderView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }


        /**
         * 首帧渲染回调
         */
        @Override
        public void onFirstVideoFrame(String userId, int streamType, int width, int height) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {
                activity.mAudioViewLayout.freshToolbarLayoutOnMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            }
        }

        @Override
        public void onStartPublishCDNStream(int err, String errMsg) {

        }

        @Override
        public void onStopPublishCDNStream(int err, String errMsg) {

        }

        @Override
        public void onRenderVideoFrame(String userId, int streamType, TRTCCloudDef.TRTCVideoFrame frame) {
//            Log.w(TAG, String.format("onRenderVideoFrame userId: %s, type: %d",userId, streamType));
        }

        @Override
        public void onUserVoiceVolume(ArrayList<TRTCCloudDef.TRTCVolumeInfo> userVolumes, int totalVolume) {
            for (int i = 0; i < userVolumes.size(); ++i) {
                mContext.get().mAudioViewLayout.updateAudioVolume(userVolumes.get(i).userId, userVolumes.get(i).volume);
            }
        }

        @Override
        public void onStatistics(TRTCStatistics statics) {

        }

        @Override
        public void onConnectOtherRoom(final String userID, final int err, final String errMsg) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {

            }
        }

        @Override
        public void onDisConnectOtherRoom(final int err, final String errMsg) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {

            }
        }

        @Override
        public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
            TRTCAudioCallActivity activity = mContext.get();
            if (activity != null) {
                activity.mAudioViewLayout.updateNetworkQuality(localQuality.userId, localQuality.quality);
                for (TRTCCloudDef.TRTCQuality qualityInfo : remoteQuality) {
                    activity.mAudioViewLayout.updateNetworkQuality(qualityInfo.userId, qualityInfo.quality);
                }
            }
        }
    }

    @Override
    public void onEnableRemoteVideo(final String userId, boolean enable) {
        if (enable) {
            final TXCloudVideoView renderView = mAudioViewLayout.getCloudVideoViewByUseId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            if (renderView != null) {
                trtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                trtcCloud.startRemoteView(userId, renderView);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        renderView.setUserId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                        mAudioViewLayout.freshToolbarLayoutOnMemberEnter(userId);
                    }
                });
            }
        } else {
            trtcCloud.stopRemoteView(userId);
        }
    }

    @Override
    public void onEnableRemoteAudio(String userId, boolean enable) {
        trtcCloud.muteRemoteAudio(userId, !enable);
    }

    @Override
    public void onChangeVideoFillMode(String userId, boolean adjustMode) {
        trtcCloud.setRemoteViewFillMode(userId, adjustMode ? TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT : TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);
    }

    @Override
    public void onChangeVideoShowFrame(String userName) {
        tvRoomId.setText(userName);
    }

    @Override
    public void onSwitchCamera(boolean bCameraFront) {
        trtcCloud.switchCamera();
    }

    @Override
    public void onAudioToChatClick(String userId) {
        Intent chatIntent = new Intent(TRTCAudioCallActivity.this, IMSingleActivity.class);
        chatIntent.putExtra(IMKeys.INTENT_ID, userId);
        startActivity(chatIntent);
        if (!Constents.isShowFloatWindow) {
            startAudioService();
        }
    }

    @Override
    public void onFillModeChange(boolean bFillMode) {
        setVideoFillMode(bFillMode);
    }

    @Override
    public void onVideoRotationChange(boolean bVertical) {
        setVideoRotation(bVertical);
    }

    @Override
    public void onEnableAudioCapture(boolean bEnable) {
        enableAudioCapture(bEnable);
    }

    @Override
    public void onEnableAudioHandFree(boolean bEnable) {
        enableAudioHandFree(bEnable);
    }

    @Override
    public void onMirrorLocalVideo(int localViewMirror) {
        setLocalViewMirrorMode(localViewMirror);
    }

    @Override
    public void onMirrorRemoteVideo(boolean bMirror) {
        enableVideoEncMirror(bMirror);
    }

    @Override
    public void onEnableGSensor(boolean bEnable) {
        enableGSensor(bEnable);
    }

    @Override
    public void onEnableAudioVolumeEvaluation(boolean bEnable) {
        enableAudioVolumeEvaluation(bEnable);
    }

    @Override
    public void onEnableCloudMixture(boolean bEnable) {
        updateCloudMixtureParams();
    }

    @Override
    public void onTRTCAudioCallMessageCancel() {
        exitRoom();
    }

    private void setVideoFillMode(boolean bFillMode) {
        if (bFillMode) {
            trtcCloud.setLocalViewFillMode(TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);
        } else {
            trtcCloud.setLocalViewFillMode(TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
        }
    }

    private void setVideoRotation(boolean bVertical) {
        if (bVertical) {
            trtcCloud.setLocalViewRotation(TRTCCloudDef.TRTC_VIDEO_ROTATION_0);
        } else {
            trtcCloud.setLocalViewRotation(TRTCCloudDef.TRTC_VIDEO_ROTATION_90);
        }
    }

    private void enableAudioCapture(boolean bEnable) {
        if (bEnable) {
            trtcCloud.startLocalAudio();
        } else {
            trtcCloud.stopLocalAudio();
        }
    }

    private void enableAudioHandFree(boolean bEnable) {
        if (bEnable) {
            trtcCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_SPEAKER);
        } else {
            trtcCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_EARPIECE);
        }
    }

    private void enableVideoEncMirror(boolean bMirror) {
        trtcCloud.setVideoEncoderMirror(bMirror);
    }

    private void setLocalViewMirrorMode(int mirrorMode) {
        trtcCloud.setLocalViewMirror(mirrorMode);
    }

    private void enableGSensor(boolean bEnable) {
        if (bEnable) {
            trtcCloud.setGSensorMode(TRTCCloudDef.TRTC_GSENSOR_MODE_UIFIXLAYOUT);
        } else {
            trtcCloud.setGSensorMode(TRTCCloudDef.TRTC_GSENSOR_MODE_DISABLE);
        }
    }

    private void enableAudioVolumeEvaluation(boolean bEnable) {
        if (bEnable) {
            trtcCloud.enableAudioVolumeEvaluation(200);
        } else {
            trtcCloud.enableAudioVolumeEvaluation(0);
            mAudioViewLayout.hideAudioVolumeProgressBar();
        }
    }

    private void updateCloudMixtureParams() {
        // 背景大画面宽高
        int videoWidth = 720;
        int videoHeight = 1280;

        // 小画面宽高
        int subWidth = 180;
        int subHeight = 320;

        int offsetX = 5;
        int offsetY = 50;

        int bitrate = 200;

        int resolution = settingDlg.getResolution();
        switch (resolution) {
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_160_160: {
                videoWidth = 160;
                videoHeight = 160;
                subWidth = 27;
                subHeight = 48;
                offsetY = 20;
                bitrate = 200;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_180: {
                videoWidth = 192;
                videoHeight = 336;
                subWidth = 54;
                subHeight = 96;
                offsetY = 30;
                bitrate = 400;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_240: {
                videoWidth = 240;
                videoHeight = 320;
                subWidth = 54;
                subHeight = 96;
                bitrate = 400;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_480_480: {
                videoWidth = 480;
                videoHeight = 480;
                subWidth = 72;
                subHeight = 128;
                bitrate = 600;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360: {
                videoWidth = 368;
                videoHeight = 640;
                subWidth = 90;
                subHeight = 160;
                bitrate = 800;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_480: {
                videoWidth = 480;
                videoHeight = 640;
                subWidth = 90;
                subHeight = 160;
                bitrate = 800;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540: {
                videoWidth = 544;
                videoHeight = 960;
                subWidth = 171;
                subHeight = 304;
                bitrate = 1000;
                break;
            }
            case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_1280_720: {
                videoWidth = 720;
                videoHeight = 1280;
                subWidth = 180;
                subHeight = 320;
                bitrate = 1500;
                break;
            }
            default:
                break;
        }

        TRTCCloudDef.TRTCTranscodingConfig config = new TRTCCloudDef.TRTCTranscodingConfig();
        config.appId = -1;  // 请从"实时音视频"控制台的帐号信息中获取
        config.bizId = -1;  // 请进入 "实时音视频"控制台 https://console.cloud.tencent.com/rav，点击对应的应用，然后进入“帐号信息”菜单中，复制“直播信息”模块中的"bizid"
        config.videoWidth = videoWidth;
        config.videoHeight = videoHeight;
        config.videoGOP = 3;

        config.videoWidth = videoWidth;
        config.videoHeight = videoHeight;
        config.videoGOP = 3;
        config.videoFramerate = 15;
        config.videoBitrate = bitrate;
        config.audioSampleRate = 48000;
        config.audioBitrate = 64;
        config.audioChannels = 1;

        // 设置混流后主播的画面位置
        TRTCCloudDef.TRTCMixUser broadCaster = new TRTCCloudDef.TRTCMixUser();
        broadCaster.userId = trtcParams.userId; // 以主播uid为broadcaster为例
        broadCaster.zOrder = 0;
        broadCaster.x = 0;
        broadCaster.y = 0;
        broadCaster.width = videoWidth;
        broadCaster.height = videoHeight;

        config.mixUsers = new ArrayList<>();
        config.mixUsers.add(broadCaster);

        // 设置混流后各个小画面的位置
        if (moreDlg.isEnableCloudMixture()) {
            int index = 0;
            for (String userId : mRoomMembers) {
                TRTCCloudDef.TRTCMixUser audience = new TRTCCloudDef.TRTCMixUser();
                audience.userId = userId;
                audience.zOrder = 1 + index;
                if (index < 3) {
                    // 前三个小画面靠右从下往上铺
                    audience.x = videoWidth - offsetX - subWidth;
                    audience.y = videoHeight - offsetY - index * subHeight - subHeight;
                    audience.width = subWidth;
                    audience.height = subHeight;
                } else if (index < 6) {
                    // 后三个小画面靠左从下往上铺
                    audience.x = offsetX;
                    audience.y = videoHeight - offsetY - (index - 3) * subHeight - subHeight;
                    audience.width = subWidth;
                    audience.height = subHeight;
                } else {
                    // 最多只叠加六个小画面
                }

                config.mixUsers.add(audience);
                ++index;
            }
        }

        trtcCloud.setMixTranscodingConfig(config);
    }

    protected String stringToMd5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
