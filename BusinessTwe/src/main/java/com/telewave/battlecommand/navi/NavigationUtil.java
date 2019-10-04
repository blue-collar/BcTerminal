package com.telewave.battlecommand.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.telewave.lib.base.ConstData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @title 百度导航工具类
 * @date 2017/11/20
 */
public class NavigationUtil {

    private Activity activity;

    /**
     * 系统SD卡根目录路径
     */
    private String mSDCardPath;


    /**
     * 标识初始化是否成功
     */
    private boolean hasInitSuccess = false;

    private String APP_FOLDER_NAME = "";

    static final String ROUTE_PLAN_NODE = "routePlanNode";

    private BNRoutePlanNode mStartNode = null;
    private BNRoutePlanNode mEndNode = null;


    public NavigationUtil(Activity activity, BNRoutePlanNode mStartNode, BNRoutePlanNode mEndNode) {
        this.activity = activity;
        this.mStartNode = mStartNode;
        this.mEndNode = mEndNode;
//        APP_FOLDER_NAME = activity.getString(R.string.app_name);
//        if (initDirs()) {
//            initNavi();
//        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
            hasInitSuccess = true;
            return;
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(activity,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(activity.getApplicationContext(),
                                "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(activity.getApplicationContext(),
                                "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed(int errCode) {
                        Toast.makeText(activity.getApplicationContext(),
                                "百度导航引擎初始化失败 " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(activity.getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, ConstData.TTSAppID);

    }


    public void routePlanToNavi(final int lineNumber) {
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(mStartNode);
        list.add(mEndNode);

//        BaiduNaviManagerFactory.getCommonSettingManager().setCarNum(activity, "粤B66666");
        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_TIME_FIRST,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(activity.getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(activity.getApplicationContext(),
                                        "算路成功", Toast.LENGTH_SHORT).show();
                                // 躲避限行消息
                                Bundle infoBundle = (Bundle) msg.obj;
                                if (infoBundle != null) {
                                    String info = infoBundle.getString(
                                            BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO
                                    );
                                    Log.d("OnSdkDemo", "info = " + info);
                                }
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(activity.getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                BaiduNaviManagerFactory.getRoutePlanManager()
                                        .removeRequestByHandler(this);
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(activity.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity, DrvingGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                                BaiduNaviManagerFactory.getRouteGuideManager().selectRoute(lineNumber);
                                BaiduNaviManagerFactory.getRoutePlanManager().removeRequestByHandler(this);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }


    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

}