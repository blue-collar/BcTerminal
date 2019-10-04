package com.telewave.battlecommand.contract;

import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {


    private static ListenerManager mListenerManager;

    private ListenerManager() {
    }

    public static ListenerManager getInstance() {
        if (mListenerManager == null) {
            mListenerManager = new ListenerManager();
        }
        return mListenerManager;
    }

    //取消request接口
    private List<Listener.canclerequestCallBackListener> canclerequestCallBackListeners = new ArrayList<>();

    public void addCanclerequestCallBackListener(Listener.canclerequestCallBackListener canclerequestCallBackListener) {
        this.canclerequestCallBackListeners.add(canclerequestCallBackListener);
    }

    public void SendCanclerequestCallBackListener() {
        for (int i = 0; i < canclerequestCallBackListeners.size(); i++) {
            if (canclerequestCallBackListeners.get(i) != null) {
                canclerequestCallBackListeners.get(i).canclerequestCallBack();
            }
        }
    }


    //首页点击 位置按钮  跳转RescueDisposalFragment 更新地图位置
    private Listener.RescueDisposalReLoadPositionListener rescueDisposalReLoadPositionListener;

    public void setRescueDisposalReLoadPositionListener(Listener.RescueDisposalReLoadPositionListener rescueDisposalReLoadPositionListener) {
        this.rescueDisposalReLoadPositionListener = rescueDisposalReLoadPositionListener;
    }

    /**
     * @param mapx
     * @param mapy
     * @param address
     */
    public void sendRescueDisposalReLoadPositionData(String disasterId, String disasterType, String mapx, String mapy, String address) {
        if (rescueDisposalReLoadPositionListener != null) {
            rescueDisposalReLoadPositionListener.reLoadPosition(disasterId, disasterType, mapx, mapy, address);
        }
    }

    private Listener.OnCustomClickListener mClickListener;

    public void setOnCustomClickListener(Listener.OnCustomClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void sendOnCustomClickListener(TXCloudVideoView view) {
        if (mClickListener != null) {
            mClickListener.onCustomClick(view);
        }
    }
}
