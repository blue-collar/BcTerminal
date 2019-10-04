package com.telewave.battlecommand.db.manager;


import com.telewave.battlecommand.db.DaoMaster;
import com.telewave.battlecommand.db.DaoSession;
import com.telewave.lib.base.AppProxy;

/**
 * Created by liwh on 2017/3/3,10:50.
 */

public class NaviMsgDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static NaviMsgDaoManager INSTANCE;

    private NaviMsgDaoManager() {
        init();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static NaviMsgDaoManager getInstance() {
        if (INSTANCE == null) {
            synchronized (NaviMsgDaoManager.class) {//保证异步处理安全操作
                if (INSTANCE == null) {
                    INSTANCE = new NaviMsgDaoManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化数据
     */
    private void init() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(AppProxy.getApplication().getApplicationContext(),
                "navi_msg.db");
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        return mDaoMaster.newSession();
    }
}
