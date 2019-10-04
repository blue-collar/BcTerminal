package com.telewave.battlecommand.directboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.telewave.battlecommand.directboard.ConSpData;
import com.telewave.battlecommand.directboard.TRTCLiveBoardViewLayout;
import com.telewave.battlecommand.trtc.TRTCMoreDialog;
import com.telewave.battlecommand.trtc.TRTCSettingDialog;
import com.telewave.battlecommand.trtc.customVideo.TestRenderVideoFrame;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.TRTCStatistics;
import com.tls.tls_sigature.tls_sigature;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.telewave.battlecommand.directboard.activity.LiveBoardSetActivity.DEFAULT_BITRATE;
import static com.telewave.battlecommand.directboard.activity.LiveBoardSetActivity.DEFAULT_FPS;


/**
 * 观众进入直播页面
 */
public class AudienceLiveBoardActivity extends Activity implements View.OnClickListener,
        TRTCSettingDialog.ISettingListener, TRTCMoreDialog.IMoreListener,
        TRTCLiveBoardViewLayout.ITRTCVideoViewLayoutListener {
    private final static String TAG = AudienceLiveBoardActivity.class.getSimpleName();

    private boolean bEnableAudio = true;

    private TextView tvRoomId;
    private ImageView ivVoice;
    private TRTCLiveBoardViewLayout mVideoViewLayout;
    //通话计时
    private Chronometer callTimeChronometer;

    private TRTCCloudDef.TRTCParams trtcParams;     /// TRTC SDK 视频通话房间进入所必须的参数
    private TRTCCloud trtcCloud;              /// TRTC SDK 实例对象
    private TRTCCloudListenerImpl trtcListener;    /// TRTC SDK 回调监听

    private HashSet<String> mRoomMembers = new HashSet<>();

    private int mSdkAppId = -1;
    private String trtcCallFrom;
    private String trtcCallType;
    private int roomId;
    private String liveUserId;
    private String liveUserName;
    private String userSig;

    private static class VideoStream {
        String userId;
        int streamType;

        public boolean equals(Object obj) {
            if (obj == null || userId == null) return false;
            VideoStream stream = (VideoStream) obj;
            return (this.streamType == stream.streamType && this.userId.equals(stream.userId));
        }
    }

    private ArrayList<VideoStream> mVideosInRoom = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        long mSdkAppIdTemp = Constents.APPKEY;
        mSdkAppId = Integer.parseInt(String.valueOf(mSdkAppIdTemp));
        tls_sigature.GenTLSSignatureResult result = tls_sigature.genSig(Constents.APPKEY, ConstData.userid, Constents.PRIVATE_KEY);
        if (result != null && !TextUtils.isEmpty(result.urlSig)) {
            userSig = result.urlSig;
        }
        //获取前一个页面得到的进房参数
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomId", 0);
        liveUserId = intent.getStringExtra("liveUserId");
        liveUserName = intent.getStringExtra("liveUserName");
        trtcParams = new TRTCCloudDef.TRTCParams(mSdkAppId, ConstData.userid, userSig, roomId, "", "");
        trtcParams.role = TRTCCloudDef.TRTCRoleAudience;
        //初始化 UI 控件
        initView();

        //创建 TRTC SDK 实例
        trtcListener = new TRTCCloudListenerImpl(this);
        trtcCloud = TRTCCloud.sharedInstance(this);
        trtcCloud.setListener(trtcListener);

        //开始进入视频通话房间
        enterRoom();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trtcCloud.setListener(null);
        TRTCCloud.destroySharedInstance();
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
        setContentView(R.layout.activity_audience_live_board);
        initClickableLayout(R.id.ll_voice);
        initClickableLayout(R.id.live_close_layout);

        mVideoViewLayout = (TRTCLiveBoardViewLayout) findViewById(R.id.ll_mainview);
        mVideoViewLayout.setUserId(liveUserId);
        mVideoViewLayout.setListener(this);
        callTimeChronometer = (Chronometer) findViewById(R.id.call_time_chronometer);
        ivVoice = (ImageView) findViewById(R.id.iv_mic);
        tvRoomId = (TextView) findViewById(R.id.tv_room_id);
        tvRoomId.setText(liveUserName + "的直播间");
    }

    private LinearLayout initClickableLayout(int resId) {
        LinearLayout layout = (LinearLayout) findViewById(resId);
        layout.setOnClickListener(this);
        return layout;
    }

    /**
     * 设置视频通话的视频参数：需要 TRTCSettingDialog 提供的分辨率、帧率和流畅模式等参数
     */
    private void setTRTCCloudParam() {
        // 大画面的编码器参数设置
        // 设置视频编码参数，包括分辨率、帧率、码率等等
        // 注意（1）：不要在码率很低的情况下设置很高的分辨率，会出现较大的马赛克
        // 注意（2）：不要设置超过25FPS以上的帧率，因为电影才使用24FPS，我们一般推荐15FPS，这样能将更多的码率分配给画质
        SharedPreferences shareInfo = getSharedPreferences(ConSpData.PER_DATA, 0);
        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = shareInfo.getInt(ConSpData.PER_RESOLUTION, TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360);
        encParam.videoFps = shareInfo.getInt(ConSpData.PER_VIDEOFPS, DEFAULT_FPS);
        encParam.videoBitrate = shareInfo.getInt(ConSpData.PER_VIDEOBITRATE, DEFAULT_BITRATE);
        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
        trtcCloud.setVideoEncoderParam(encParam);

        TRTCCloudDef.TRTCNetworkQosParam qosParam = new TRTCCloudDef.TRTCNetworkQosParam();
        qosParam.controlMode = TRTCCloudDef.VIDEO_QOS_CONTROL_SERVER;
        qosParam.preference = TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_CLEAR;
        trtcCloud.setNetworkQosParam(qosParam);

        trtcCloud.setPriorRemoteVideoStreamType(TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);

    }

    /**
     * 加入视频房间：需要 TRTCNewViewActivity 提供的  TRTCParams 函数
     */
    private void enterRoom() {
        // 预览前配置默认参数
        setTRTCCloudParam();

//        setVideoFillMode(true);

        trtcCloud.setRemoteViewFillMode(liveUserId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);

        mVideosInRoom.clear();
        mRoomMembers.clear();

        trtcCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_LIVE);

    }

    /**
     * 主播结束
     * 退出视频房间
     */
    private void autoExitRoom() {
        if (trtcCloud != null) {
            trtcCloud.exitRoom();
        }
        ToastUtils.toastShortMessage("该主播直播已结束");
        finish();
    }

    /**
     * 自己退出视频房间
     */
    private void exitRoom() {
        if (trtcCloud != null) {
            trtcCloud.exitRoom();
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_voice) {
            onEnableAnchorAudio();
        } else if (v.getId() == R.id.live_close_layout) {
            exitRoom();
        }
    }

    /**
     * 开启/关闭
     * 远端主播音频
     */
    private void onEnableAnchorAudio() {
        bEnableAudio = !bEnableAudio;
        trtcCloud.muteRemoteAudio(liveUserId, !bEnableAudio);
        ivVoice.setImageResource(bEnableAudio ? R.mipmap.mic_enable : R.mipmap.mic_disable);
    }


    @Override
    public void onComplete() {
        setTRTCCloudParam();
        setVideoFillMode(true);
//        moreDlg.updateVideoFillMode(true);
    }

    /**
     * SDK内部状态回调
     */
    static class TRTCCloudListenerImpl extends TRTCCloudListener implements TRTCCloudListener.TRTCVideoRenderListener {

        private WeakReference<AudienceLiveBoardActivity> mContext;
        private HashMap<String, TestRenderVideoFrame> mCustomRender;

        public TRTCCloudListenerImpl(AudienceLiveBoardActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
            mCustomRender = new HashMap<>(10);
        }

        /**
         * 加入房间
         */
        @Override
        public void onEnterRoom(long elapsed) {
            final AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                activity.mVideoViewLayout.onRoomEnter();
                activity.updateCloudMixtureParams();
                activity.callTimeChronometer.setBase(SystemClock.elapsedRealtime());
                activity.callTimeChronometer.start();
            }
        }

        /**
         * 离开房间
         */
        @Override
        public void onExitRoom(int reason) {
            AudienceLiveBoardActivity activity = mContext.get();
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
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity == null) return;
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
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                // 创建一个View用来显示新的一路画面
                TXCloudVideoView renderView = activity.mVideoViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
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
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                if (activity.liveUserId.equals(userId)) {
                    activity.autoExitRoom();
                }
                //停止观看画面
                activity.trtcCloud.stopRemoteView(userId);
                activity.trtcCloud.stopRemoteSubStreamView(userId);
                //更新视频UI
                activity.mVideoViewLayout.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                activity.mVideoViewLayout.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
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
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                if (available) {
                    final TXCloudVideoView renderView = activity.mVideoViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
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
                    //activity.mVideoViewLayout.onMemberLeave(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);

                    activity.mRoomMembers.remove(userId);
                    activity.updateCloudMixtureParams();
                }
                activity.mVideoViewLayout.updateVideoStatus(userId, available);
            }

        }

        @Override
        public void onUserSubStreamAvailable(final String userId, boolean available) {
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                if (available) {
                    final TXCloudVideoView renderView = activity.mVideoViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
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
                    activity.mVideoViewLayout.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
                }
            }
        }

        /**
         * 有用户屏蔽了声音
         */
        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                if (available) {
                    final TXCloudVideoView renderView = activity.mVideoViewLayout.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
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
            AudienceLiveBoardActivity activity = mContext.get();
            Log.e(TAG, "onFirstVideoFrame: 77777777777777777777777");
            if (activity != null) {
                activity.mVideoViewLayout.freshToolbarLayoutOnMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
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
//            mContext.get().mVideoViewLayout.resetAudioVolume();
            for (int i = 0; i < userVolumes.size(); ++i) {
                mContext.get().mVideoViewLayout.updateAudioVolume(userVolumes.get(i).userId, userVolumes.get(i).volume);
            }
        }

        @Override
        public void onStatistics(TRTCStatistics statics) {

        }

        @Override
        public void onConnectOtherRoom(final String userID, final int err, final String errMsg) {
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {

            }
        }

        @Override
        public void onDisConnectOtherRoom(final int err, final String errMsg) {
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {

            }
        }

        @Override
        public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
            AudienceLiveBoardActivity activity = mContext.get();
            if (activity != null) {
                activity.mVideoViewLayout.updateNetworkQuality(localQuality.userId, localQuality.quality);
                for (TRTCCloudDef.TRTCQuality qualityInfo : remoteQuality) {
                    activity.mVideoViewLayout.updateNetworkQuality(qualityInfo.userId, qualityInfo.quality);
                }
            }
        }
    }

    @Override
    public void onEnableRemoteVideo(final String userId, boolean enable) {
        if (enable) {
            final TXCloudVideoView renderView = mVideoViewLayout.getCloudVideoViewByUseId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            if (renderView != null) {
                trtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                trtcCloud.startRemoteView(userId, renderView);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        renderView.setUserId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                        mVideoViewLayout.freshToolbarLayoutOnMemberEnter(userId);
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
    public void onChangeVideoShowFrame(String userId, String userName) {
        tvRoomId.setText(userName);
    }

    @Override
    public void onSwitchCamera(boolean bCameraFront) {
        trtcCloud.switchCamera();
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
            trtcCloud.enableAudioVolumeEvaluation(300);
            mVideoViewLayout.showAllAudioVolumeProgressBar();
        } else {
            trtcCloud.enableAudioVolumeEvaluation(0);
            mVideoViewLayout.hideAllAudioVolumeProgressBar();
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

        int resolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
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
        config.videoGOP = 1;
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
