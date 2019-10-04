package com.telewave.battlecommand.contract;


import com.tencent.rtmp.ui.TXCloudVideoView;

public class Listener {


    //取消request接口
    public interface canclerequestCallBackListener {
        public void canclerequestCallBack();
    }


    //RescueDisposalFragment 重新置放地图位置监听
    public interface RescueDisposalReLoadPositionListener {
        public void reLoadPosition(String disasterId, String disasterType, String mapx, String mapy, String address);
    }

    public interface OnCustomClickListener {
        void onCustomClick(TXCloudVideoView view);
    }

}
