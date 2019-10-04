package com.telewave.battlecommand.trtc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
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
import com.telewave.battlecommand.service.FloatVideoWindowService;
import com.telewave.battlecommand.trtc.customVideo.TestRenderVideoFrame;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.SampleUser;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.util.ActivityUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.liteav.renderer.TXCGLSurfaceView;
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
import java.util.List;


/**
 * Module:   TRTCMainActivity
 * <p>
 * Function: 使用TRTC SDK完成 1v1 和 1vn 的视频通话功能
 * <p>
 * 1. 支持九宫格平铺和前后叠加两种不同的视频画面布局方式，该部分由 TRTCVideoViewLayout 来计算每个视频画面的位置排布和大小尺寸
 * <p>
 * 2. 支持对视频通话的分辨率、帧率和流畅模式进行调整，该部分由 TRTCSettingDialog 来实现
 * <p>
 * 3. 创建或者加入某一个通话房间，需要先指定 roomId 和 userId，这部分由 TRTCNewActivity 来实现
 */
public class TRTCVideoCallActivity extends Activity implements View.OnClickListener,
        TRTCSettingDialog.ISettingListener, TRTCMoreDialog.IMoreListener,
        TRTCVideoViewLayout.ITRTCVideoViewLayoutListener, TRTCVideoViewLayout.OnVideoToChatClickListener,
        TRTCCallMessageManager.TRTCVideoCallMessageCancelListener {
    private final static String TAG = TRTCVideoCallActivity.class.getSimpleName();

    private boolean bEnableVideo = true, bEnableAudio = true;
    private boolean mCameraFront = true;

    private TextView tvRoomId;
    private ImageView ivCamera, ivVoice;
    private TRTCVideoViewLayout mVideoViewLayout;
    //通话计时
    private Chronometer callTimeChronometer;

    // TRTC SDK 视频通话房间进入所必须的参数
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
    private String userSig;

    private CountDownTimer countDownTimer;

    private ImageView trtcSmallIv;
    private String currentBigUserId = ConstData.userid;
    private HomeWatcher mHomeWatcher;
    private boolean mServiceBound = false;

    /**
     * 不包含自己的接收人列表（单聊情况）
     */
    private List<SampleUser> receiveUsers = new ArrayList<>();

    private static class VideoStream {
        String userId;
        int streamType;

        @Override
        public boolean equals(Object obj) {
            if (obj == null || userId == null) return false;
            VideoStream stream = (VideoStream) obj;
            return (this.streamType == stream.streamType && this.userId.equals(stream.userId));
        }
    }

    /**
     * 定义服务绑定的回调 开启视频通话服务连接
     */
    private ServiceConnection mVideoCallServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatVideoWindowService.MyBinder binder = (FloatVideoWindowService.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ArrayList<VideoStream> mVideosInRoom = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityUtil.addDestoryActivityToMap(TRTCVideoCallActivity.this, TAG);
        TRTCCallMessageManager.getInstance().setTRTCVideoCallMessageListener(this);

        //获取前一个页面得到的进房参数
        Intent intent = getIntent();
        long mSdkAppIdTemp = intent.getLongExtra("sdkAppId", 0);
        mSdkAppId = Integer.parseInt(String.valueOf(mSdkAppIdTemp));
        roomId = intent.getIntExtra("roomId", 0);
        trtcCallFrom = intent.getStringExtra("trtcCallFrom");
        trtcCallType = intent.getStringExtra("trtcCallType");
        ConstData.currentTrtcCallType = trtcCallType;
        ConstData.currentRoomId = roomId + "";
        receiveUsers = (List<SampleUser>) getIntent().getSerializableExtra("receiveUserList");
        userSig = intent.getStringExtra("userSig");
        trtcParams = new TRTCCloudDef.TRTCParams(mSdkAppId, ConstData.userid, userSig, roomId, "", "");
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor;

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
                if (!TRTCVideoCallActivity.this.isFinishing() && ConstData.enterRoomUserIdSet.size() > 0) {
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                //倒计时全部结束执行操作
                if (!TRTCVideoCallActivity.this.isFinishing() && ConstData.enterRoomUserIdSet.size() == 0) {
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
                    startVideoService();
                }
            }

            @Override
            public void onRecentAppsPressed() {
                //最近app任务列表按键
                if (!Constents.isShowFloatWindow) {
                    startVideoService();
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
        trtcCloud.setListener(null);
        TRTCCloud.destroySharedInstance();
        ConstData.isEnterTRTCCALL = false;
        //解绑 不显示悬浮框
        if (mServiceBound) {
            unbindService(mVideoCallServiceConnection);
            mServiceBound = false;
        }
        if (mHomeWatcher != null) {
            mHomeWatcher.stopWatch();// 在销毁时停止监听，不然会报错的。
        }
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
        setContentView(R.layout.activity_trtc_video);
        trtcSmallIv = (ImageView) findViewById(R.id.trtc_small_iv);
        trtcSmallIv.setOnClickListener(this);
        initClickableLayout(R.id.ll_camera);
        initClickableLayout(R.id.ll_voice);
        initClickableLayout(R.id.ll_add);
        initClickableLayout(R.id.ll_change_camera);

        mVideoViewLayout = (TRTCVideoViewLayout) findViewById(R.id.video_ll_mainview);
        mVideoViewLayout.setUserId(trtcParams.userId);
        mVideoViewLayout.setListener(this);
        mVideoViewLayout.setOnVideoToChatListener(this);
        callTimeChronometer = (Chronometer) findViewById(R.id.call_time_chronometer);
        ivVoice = (ImageView) findViewById(R.id.iv_mic);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        tvRoomId = (TextView) findViewById(R.id.tv_room_id);
        tvRoomId.setText(ConstData.username + "（自己）");
        findViewById(R.id.video_ring_off_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitRoom();
                /**
                 * 单人通话时
                 * 新增主叫方在接收方未接听前挂断时
                 * 发送消息给接收方 让接收方取消响铃页面或者 来电弹框
                 */
                if (trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)) {
                    //ConstData.enterRoomUserIdSet.size() == 0表示还没有接收方加入房间
                    if (ConstData.enterRoomUserIdSet.size() == 0) {
                        sendDeclineMsg();
                    }
                }
            }
        });
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
        // 设置视频编码参数，包括分辨率、帧率、码率等等，这些编码参数来自于 TRTCSettingDialog 的设置
        // 注意（1）：不要在码率很低的情况下设置很高的分辨率，会出现较大的马赛克
        // 注意（2）：不要设置超过25FPS以上的帧率，因为电影才使用24FPS，我们一般推荐15FPS，这样能将更多的码率分配给画质
        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
        encParam.videoFps = 15;
        encParam.videoBitrate = 600;
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
        // 开启视频采集预览
        if (trtcParams.role == TRTCCloudDef.TRTCRoleAnchor) {
            startLocalVideo(true);
        }
        trtcCloud.setBeautyStyle(TRTCCloudDef.TRTC_BEAUTY_STYLE_SMOOTH, 5, 5, 5);

        if (trtcParams.role == TRTCCloudDef.TRTCRoleAnchor) {
            trtcCloud.startLocalAudio();
        }

        setVideoFillMode(true);
        setVideoRotation(true);
        enableAudioHandFree(true);
        enableGSensor(true);
        enableAudioVolumeEvaluation(false);
        /**
         * 2019/08/08
         * 默认打开是前置摄像头
         * 前置摄像头就设置镜像 true
         */
        enableVideoEncMirror(true);

        setLocalViewMirrorMode(TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_AUTO);

        mVideosInRoom.clear();
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
            startVideoService();
        } else if (v.getId() == R.id.ll_camera) {
            onEnableVideo();
        } else if (v.getId() == R.id.ll_voice) {
            onEnableAudio();
        } else if (v.getId() == R.id.ll_add) {
            if (Constents.ONE_TO_ONE_AUDIO_CALL.equals(trtcCallType)
                    || Constents.ONE_TO_ONE_VIDEO_CALL.equals(trtcCallType)) {
                ToastUtils.toastShortMessage("单聊时不能添加其他成员");
            } else {
                onOpenChoiceMember();
            }
        } else if (v.getId() == R.id.ll_change_camera) {
            onChangeCamera();
        }
    }

    /**
     * 发送挂断/拒接电话消息
     */
    private void sendDeclineMsg() {
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        /**
         * 挂断/拒接语音、视频通话消息
         * msgContent不放内容
         */
        String msgStr = null;
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
            msgStr = JsonUtils.toJson(Constents.AUDIO_CALL_MESSAGE_DECLINE_DESC, null);
        } else if (trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
            msgStr = JsonUtils.toJson(Constents.VIDEO_CALL_MESSAGE_DECLINE_DESC, null);
        }
        ele.setData(msgStr.getBytes());
        timMessage.addElement(ele);

        String receiveUserId = null;
        if (!receiveUsers.isEmpty()) {
            SampleUser sampleUser = receiveUsers.get(0);
            receiveUserId = sampleUser.getUserid();
        }
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C, receiveUserId);
        //发送消息
        conversation.sendOnlineMessage(timMessage, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                Log.d("NNN", "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e("NNN", "SendMsg ok");
            }
        });
    }

    /**
     * 开启悬浮Video服务
     */
    private void startVideoService() {
        //最小化Activity
        moveTaskToBack(true);
        Constents.mVideoViewLayout = mVideoViewLayout;
        //开启服务显示悬浮框
        Intent floatVideoIntent = new Intent(this, FloatVideoWindowService.class);
        floatVideoIntent.putExtra("userId", currentBigUserId);
        mServiceBound = bindService(floatVideoIntent, mVideoCallServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //不显示悬浮框
        if (mServiceBound) {
            unbindService(mVideoCallServiceConnection);
            mServiceBound = false;
        }
        TXCloudVideoView txCloudVideoView = mVideoViewLayout.getCloudVideoViewByUseId(currentBigUserId);
        if (txCloudVideoView == null) {
            txCloudVideoView = mVideoViewLayout.getCloudVideoViewByIndex(0);
        }
        if (ConstData.userid.equals(currentBigUserId)) {
            TXCGLSurfaceView mTXCGLSurfaceView = txCloudVideoView.getGLSurfaceView();
            if (mTXCGLSurfaceView != null && mTXCGLSurfaceView.getParent() != null) {
                ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
                txCloudVideoView.addVideoView(mTXCGLSurfaceView);
            }
        } else {
            TextureView mTextureView = txCloudVideoView.getVideoView();
            if (mTextureView != null && mTextureView.getParent() != null) {
                ((ViewGroup) mTextureView.getParent()).removeView(mTextureView);
                txCloudVideoView.addVideoView(mTextureView);
            }
        }
    }


    /**
     * 开启/关闭视频上行
     */
    private void onEnableVideo() {
        bEnableVideo = !bEnableVideo;
        startLocalVideo(bEnableVideo);
        mVideoViewLayout.updateVideoStatus(trtcParams.userId, bEnableVideo);
        ivCamera.setImageResource(bEnableVideo ? R.mipmap.remote_video_enable : R.mipmap.remote_video_disable);
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
        Intent intent = new Intent(TRTCVideoCallActivity.this, IMSelectGroupMemberActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("trtcCallType", trtcCallType);
        startActivity(intent);
    }

    /**
     * 点击切换摄像头
     */
    private void onChangeCamera() {
        mCameraFront = !mCameraFront;
        onSwitchCamera(mCameraFront);
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

        private WeakReference<TRTCVideoCallActivity> mContext;
        private HashMap<String, TestRenderVideoFrame> mCustomRender;

        public TRTCCloudListenerImpl(TRTCVideoCallActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
            mCustomRender = new HashMap<>(10);
        }

        /**
         * 加入房间
         */
        @Override
        public void onEnterRoom(long elapsed) {
            final TRTCVideoCallActivity activity = mContext.get();
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
            TRTCVideoCallActivity activity = mContext.get();
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
            TRTCVideoCallActivity activity = mContext.get();
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
            TRTCVideoCallActivity activity = mContext.get();
            ConstData.enterRoomUserIdSet.add(userId);
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
            TRTCVideoCallActivity activity = mContext.get();
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
            TRTCVideoCallActivity activity = mContext.get();
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
                activity.mVideoViewLayout.updateVideoStatus(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, available);
            }

        }

        @Override
        public void onUserSubStreamAvailable(final String userId, boolean available) {
            TRTCVideoCallActivity activity = mContext.get();
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
                activity.updateCloudMixtureParams();
            }
        }

        /**
         * 有用户屏蔽了声音
         */
        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            TRTCVideoCallActivity activity = mContext.get();
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
            TRTCVideoCallActivity activity = mContext.get();
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
            TRTCVideoCallActivity activity = mContext.get();
            if (activity != null) {

            }
        }

        @Override
        public void onDisConnectOtherRoom(final int err, final String errMsg) {
            TRTCVideoCallActivity activity = mContext.get();
            if (activity != null) {

            }
        }

        @Override
        public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
            TRTCVideoCallActivity activity = mContext.get();
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
            final TXCloudVideoView renderView = mVideoViewLayout.getCloudVideoViewByUseId(userId);
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
        currentBigUserId = userId;
        tvRoomId.setText(userName);
    }

    @Override
    public void onSwitchCamera(boolean bCameraFront) {
        trtcCloud.switchCamera();
        /**
         * 2019/08/08
         * 此处增加判断
         * 前置摄像头就设置镜像 true
         * 后置摄像头就不设置镜像 false
         */
        if (bCameraFront) {
            enableVideoEncMirror(true);
        } else {
            enableVideoEncMirror(false);
        }
    }

    /**
     * 视频里点击进入和某人聊天
     *
     * @param userId
     */
    @Override
    public void onVideoToChatClick(String userId) {
        Intent chatIntent = new Intent(TRTCVideoCallActivity.this, IMSingleActivity.class);
        chatIntent.putExtra(IMKeys.INTENT_ID, userId);
        startActivity(chatIntent);
        if (!Constents.isShowFloatWindow) {
            startVideoService();
        }
    }

    /**
     * 拒接视频通话回调
     */
    @Override
    public void onTRTCVideoCallMessageCancel() {
        exitRoom();
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


    private void startLocalVideo(boolean enable) {
        TXCloudVideoView localVideoView = mVideoViewLayout.getCloudVideoViewByUseId(trtcParams.userId);
        if (localVideoView == null) {
            localVideoView = mVideoViewLayout.getFreeCloudVideoView();
        }
        localVideoView.setUserId(trtcParams.userId);
        localVideoView.setVisibility(View.VISIBLE);
        if (enable) {
            // 设置 TRTC SDK 的状态
            trtcCloud.enableCustomVideoCapture(false);
            //启动SDK摄像头采集和渲染
            trtcCloud.startLocalPreview(mCameraFront, localVideoView);
        } else {
            trtcCloud.stopLocalPreview();
        }
    }
}
