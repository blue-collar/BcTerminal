package com.telewave.battlecommand.trtc;

/**
 * @author liwh
 * @date 2019/6/13
 */
public class TRTCCallMessageManager {

    private static TRTCCallMessageManager trtcCallMessageManager;

    private TRTCCallMessageManager() {
    }

    public static TRTCCallMessageManager getInstance() {
        if (trtcCallMessageManager == null) {
            trtcCallMessageManager = new TRTCCallMessageManager();
        }
        return trtcCallMessageManager;
    }

    /********************************* 音频通话取消回调 ***************************************************/
    private TRTCAudioCallMessageCancelListener trtcAudioCallMessageCancelListener;

    public void setTRTCAudioCallMessageListener(TRTCAudioCallMessageCancelListener trtcAudioCallMessageCancelListener) {
        this.trtcAudioCallMessageCancelListener = trtcAudioCallMessageCancelListener;
    }

    public void sendTRTCAudioCallMessageCancel() {
        if (this.trtcAudioCallMessageCancelListener != null) {
            this.trtcAudioCallMessageCancelListener.onTRTCAudioCallMessageCancel();
        }
    }

    /**
     * 腾讯实时音频通话取消
     */
    public interface TRTCAudioCallMessageCancelListener {
        public void onTRTCAudioCallMessageCancel();
    }


    /********************************* 视频通话取消回调 ***************************************************/
    private TRTCVideoCallMessageCancelListener trtcVideoCallMessageCancelListener;

    public void setTRTCVideoCallMessageListener(TRTCVideoCallMessageCancelListener trtcVideoCallMessageCancelListener) {
        this.trtcVideoCallMessageCancelListener = trtcVideoCallMessageCancelListener;
    }

    public void sendTRTCVideoCallMessageCancel() {
        if (this.trtcVideoCallMessageCancelListener != null) {
            this.trtcVideoCallMessageCancelListener.onTRTCVideoCallMessageCancel();
        }
    }

    /**
     * 腾讯实时视频通话取消
     */
    public interface TRTCVideoCallMessageCancelListener {
        public void onTRTCVideoCallMessageCancel();
    }


    /********************************* 记录当前通话类型回调 ***************************************************/
    private RecordCurrentTRTCCallTypeListener recordCurrentTRTCCallTypeListener;

    public void setRecordCurrentTRTCCallTypeListener(RecordCurrentTRTCCallTypeListener recordCurrentTRTCCallTypeListener) {
        this.recordCurrentTRTCCallTypeListener = recordCurrentTRTCCallTypeListener;
    }

    public void sendRecordCurrentTRTCCallType() {
        if (this.recordCurrentTRTCCallTypeListener != null) {
            this.recordCurrentTRTCCallTypeListener.onCurrentTRTCCallType();
        }
    }

    /**
     * 当第三方呼入
     * 记录此时正在通话类型
     */
    public interface RecordCurrentTRTCCallTypeListener {
        public void onCurrentTRTCCallType();
    }


}
