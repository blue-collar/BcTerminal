package com.telewave.battlecommand.directboard.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * 直播外层实体
 *
 * @author liwh
 * @date 2019-08-09
 */
public class LiveBoardOutObj {
    /*昵称*/
    public String name;
    /*正在直播集合*/
    public List<LiveBoardBean> liveBoardInfoObjs;

    public int onLiveNumber;
    public int totalNumber;

    /**
     * 构造处理数据
     *
     * @param name
     * @param liveBoardInfoObjs
     */
    public LiveBoardOutObj(String name, List<LiveBoardBean> liveBoardInfoObjs) {
        this.name = name;
        this.liveBoardInfoObjs = liveBoardInfoObjs;
        if (null != this.liveBoardInfoObjs && !this.liveBoardInfoObjs.isEmpty()) {
//            Collections.sort(liveBoardInfoObjs);

            onLiveNumber = 0;
            for (LiveBoardBean liveingBean : this.liveBoardInfoObjs) {
                if (TextUtils.equals(liveingBean.getIsOpenLive(), "1")) {
                    onLiveNumber += 1;
                }
            }
            totalNumber = this.liveBoardInfoObjs.size();
        } else {
            onLiveNumber = 0;
            totalNumber = 0;
        }

    }
}
