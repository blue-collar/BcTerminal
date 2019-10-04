package com.telewave.battlecommand.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.ResidentNotificationHelper;
import com.telewave.battlecommand.adapter.MainFragmentPagerAdapter;
import com.telewave.battlecommand.bean.Version;
import com.telewave.battlecommand.contract.FragmentContract;
import com.telewave.battlecommand.contract.ListenerManager;
import com.telewave.battlecommand.fragment.ContactListFragment;
import com.telewave.battlecommand.fragment.IndexPageFragment;
import com.telewave.battlecommand.fragment.MyPageFragment;
import com.telewave.battlecommand.fragment.RescueDisposalFragment;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.HttpResponseUtil;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.service.IMOperate;
import com.telewave.battlecommand.imui.util.IMInformOperate;
import com.telewave.battlecommand.mqtt.MqttListener.MqttListener;
import com.telewave.battlecommand.mqtt.MqttListener.MqttListenerManager;
import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.MessageType;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyMemberMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyWeiZhanMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ReceiveRollCallMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ZqSureMessage;
import com.telewave.battlecommand.service.MyMqttService;
import com.telewave.battlecommand.service.UpdateService;
import com.telewave.battlecommand.view.CustomViewPager;
import com.telewave.battlecommand.view.MyDialog;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.AppUtils;
import com.telewave.lib.base.util.BottomNavigationViewHelper;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.util.FinishActivityManager;
import com.tencent.qcloud.tim.uikit.component.UnreadCountTextView;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseStatusActivity implements FragmentContract.F2A,
        MqttListener.NotifyMemberListener, MqttListener.NotifyWeiZhanListener,
        MqttListener.RollCallListener, ConversationManagerKit.MessageUnreadWatcher {

    private String TAG = MainActivity.class.getSimpleName();
    private CustomViewPager mViewPager;
    private BottomNavigationView bottomNavigation;

    private IndexPageFragment indexPageFragment;
    private ContactListFragment contactListFragment;
    private RescueDisposalFragment rescueDisposalFragment;
    private MyPageFragment myPageFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private static final int GET_UPDATE_SUCCESS = 0x1001;
    private static final int GET_UPDATE_FAIL = 0x1002;
    private static final int GET_NO_UPDATE = 0x1003;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final int mId = item.getItemId();
            if (R.id.navigation_index_page == mId) {
                mViewPager.setCurrentItem(0);
                return true;
            } else if (R.id.navigation_contact_list == mId) {
                mViewPager.setCurrentItem(1);
                return true;
            } else if (R.id.navigation_rescue_disposal == mId) {
                mViewPager.setCurrentItem(2);
                return true;
            } else if (R.id.navigation_my_page == mId) {
                mViewPager.setCurrentItem(3);
                return true;
            } else {
            }
            return false;
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        super.setUpViewAndData(savedInstanceState);
        IMOperate.getInstance().setIMEventListener(this);
        boolean isLoginJump = getIntent().getBooleanExtra(IMKeys.INTENT_TAG, false);
        if (!isLoginJump) {
            IMOperate.getInstance().login(this);
        }
        /*未读消息监视器*/
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        setContentView(getLayoutId());
        MqttListenerManager.getInstance().setNotifyMemberListener(this);
        MqttListenerManager.getInstance().setNotifyWeiZhanListener(this);
        MqttListenerManager.getInstance().setRollCallListener(this);
        initView();
        getNewVersion();
        /*从application移入到这里，原因在于首次装上app，需要获取一系列权限，如创建文件夹，图片下载需要指定创建好的文件目录，否则会下载本地失败，聊天页面从而获取不到图片、表情*/
        FileUtil.initPath();

        //此处加入MainActivity,方便在SystemSetActivity退出登录，结束MainActivity可以重新初始化MainActivity
        FinishActivityManager.getManager().addActivity(MainActivity.this);
    }

    private void initView() {
        IMOperate.getInstance().initHuaWeiPush(this);
//        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        mToolbarTitle.setText("消防E通");
        mViewPager = (CustomViewPager) findViewById(R.id.id_viewpager);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //设置这句就可以让 android:icon="@drawable/bottom_navigation_four_selector"生效
        bottomNavigation.setItemIconTintList(null);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                MenuItem menuItem = bottomNavigation.getMenu().getItem(position);
                menuItem.setChecked(true);
                if (position == 0) {
//                    setToolBarTitle("消防E通");
                    // 返回键逻辑与宿主Activity返回键逻辑一样
                    setInterception(false);
                } else if (position == 1) {
                    /**
                     *  独立处理返回键逻辑
                     *  这个地方注释了
                     *  移步到进入灾情列表在设置
                     */
                    setInterception(false);
//                    setToolBarTitle("通讯录");
                } else if (position == 2) {
                    // 返回键逻辑与宿主Activity返回键逻辑一样
                    setInterception(false);
//                    setToolBarTitle("联动处置");
                } else if (position == 3) {
                    // 返回键逻辑与宿主Activity返回键逻辑一样
                    setInterception(false);
//                    setToolBarTitle("我");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        indexPageFragment = new IndexPageFragment();
        contactListFragment = new ContactListFragment();
        rescueDisposalFragment = new RescueDisposalFragment();
        myPageFragment = new MyPageFragment();

        fragments.add(indexPageFragment);
        fragments.add(contactListFragment);
        fragments.add(rescueDisposalFragment);
        fragments.add(myPageFragment);

        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mainFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mViewPager.setNoScroll(true);
        //去除滑动效果
        mViewPager.setViewPagerScrollSpeed();
        initUnNumberView();
    }


    @Override
    public void selectViewPagerIndex(int index) {
        mViewPager.setCurrentItem(index);
    }

    /**
     * 接收到指定成员的通知
     *
     * @param message
     */
    @Override
    public void onNotifyMemberArrived(final NotifyMemberMessage message) {
        if (ConstData.isMainActivtyFrontShow) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_notify_arrived, null);
                    TextView notifyTime = (TextView) view.findViewById(R.id.notify_time);
                    TextView notifyFrom = (TextView) view.findViewById(R.id.notify_from);
                    TextView notifyContent = (TextView) view.findViewById(R.id.notify_content);
                    LinearLayout checkedLayout = (LinearLayout) view.findViewById(R.id.btn_checked_layout);
                    LinearLayout ignoreLayout = (LinearLayout) view.findViewById(R.id.btn_ignore_layout);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog dialog = builder.create();
                    dialog.setView(view, 0, 0, 0, 0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    notifyTime.setText(message.getSendtime());
                    notifyFrom.setText("来源:" + message.getOrganname());
                    notifyContent.setText(message.getContent());
                    checkedLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, NotifyDetailActivity.class);
                            intent.putExtra("id", message.getNoticeid());
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    ignoreLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            ResidentNotificationHelper.sendNotifyNotice(MainActivity.this, message);
        }
        if (ConstData.isNotifyVoiceOpen) {
            receiveNotifyVoice("receive_notify_man.mp3");
        }
    }

    /**
     * 接收到指定微站的通知
     *
     * @param message
     */
    @Override
    public void onNotifyWeiZhanArrived(final NotifyWeiZhanMessage message) {
        if (ConstData.isMainActivtyFrontShow) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_notify_arrived, null);
                    TextView notifyTime = (TextView) view.findViewById(R.id.notify_time);
                    TextView notifyFrom = (TextView) view.findViewById(R.id.notify_from);
                    TextView notifyContent = (TextView) view.findViewById(R.id.notify_content);
                    LinearLayout checkedLayout = (LinearLayout) view.findViewById(R.id.btn_checked_layout);
                    LinearLayout ignoreLayout = (LinearLayout) view.findViewById(R.id.btn_ignore_layout);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog dialog = builder.create();
                    dialog.setView(view, 0, 0, 0, 0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    notifyTime.setText(message.getSendtime());
                    notifyFrom.setText("来源:" + message.getOrganname());
                    notifyContent.setText(message.getContent());
                    checkedLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, NotifyDetailActivity.class);
                            intent.putExtra("id", message.getNoticeid());
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    ignoreLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            ResidentNotificationHelper.sendNotifyNotice(MainActivity.this, message);
        }
        if (ConstData.isNotifyVoiceOpen) {
            receiveNotifyVoice("receive_notify_man.mp3");
        }
    }

    /**
     * 接收到点名消息
     *
     * @param message
     */
    @Override
    public void onRollCallArrived(final ReceiveRollCallMessage message) {
        if (ConstData.isMainActivtyFrontShow) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_rollcall_arrived, null);
                    TextView rollCallTime = (TextView) view.findViewById(R.id.rollcall_time);
                    TextView rollCallFrom = (TextView) view.findViewById(R.id.rollcall_from);
                    TextView rollCallContent = (TextView) view.findViewById(R.id.rollcall_content);
                    LinearLayout checkedLayout = (LinearLayout) view.findViewById(R.id.btn_checked_layout);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog dialog = builder.create();
                    dialog.setView(view, 0, 0, 0, 0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    rollCallTime.setText(message.getCalltime());
                    rollCallFrom.setText("来源:" + message.getOrganname());
//                rollCallContent.setText("收到点名通知，请及时应答!\r\n" + "点名截止时间:" + message.getCutofftime());
                    checkedLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, RollCallAnswerActivity.class);
                            intent.putExtra("ReceiveRollCallMessage", message);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            ResidentNotificationHelper.sendRollCallNotice(MainActivity.this, message);
        }
        if (ConstData.isRollCallVoiceOpen) {
            receiveNotifyVoice("receive_rollcall_man.mp3");
        }
    }

    public void receiveNotifyVoice(String voiceName) {
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // 停止 开启 停止 开启
        final long[] pattern = {100, 400, 100, 400, 100, 400};
        final MediaPlayer player = new MediaPlayer();
        player.reset();
        player.setVolume(1.0f, 1.0f);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                vibrator.vibrate(pattern, -1);
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
                vibrator.cancel();
            }
        });
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(voiceName);
//            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("receive_notify_man.mp3");
            player.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor
                    .getStartOffset(), assetFileDescriptor.getLength());
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //一定要有这个方法，否则getIntent收不到数据
        setIntent(intent);
        Log.e("onNewArrived", "onNewIntent");
        int type = intent.getIntExtra("type", 0);
        Log.e("onNewArrived", "type" + type);
        /**
         * 新警情消息
         */
        if (type == 1) {
            String eventSign = intent.getStringExtra("eventSign");
            String msgType = intent.getStringExtra("msgType");

            if (msgType.equals(MessageType.NEW_DISASTER_INFO)) {
                NewDisasterInfo.MsgContentBean info = (NewDisasterInfo.MsgContentBean)
                        intent.getSerializableExtra("msgContentBean");
                if (info != null && info.getZq() != null) {
                    NewDisasterInfo.MsgContentBean.ZqBean zqBean = info.getZq();
                    String disasterId = zqBean.getAlarmid();
                    String disasterType = zqBean.getZqlxdm();
                    String mapx = zqBean.getGis_x() + "";
                    String mapy = zqBean.getGis_y() + "";
                    String address = zqBean.getZhdd();
                    ListenerManager.getInstance().sendRescueDisposalReLoadPositionData(disasterId, disasterType, mapx, mapy, address);
                    /**
                     * 2019/05/09加入
                     * 收到新警情
                     * 第二次回执
                     */
                    ZqSureMessage zqSureMessage = new ZqSureMessage();
                    zqSureMessage.setBeanData(ZqSureMessage.STATUS_SURE, info);
                    MyMqttService.publishMessage(zqSureMessage.toJson());
                }
            } else if (msgType.equals(MessageType.RECEIVE_CALL_POLICE_MESSAGE)) {
                CallPoliceMessage callPoliceMessage = (CallPoliceMessage) intent.getSerializableExtra("msgContentBean");
                if (callPoliceMessage != null && callPoliceMessage.getZq() != null) {
                    CallPoliceMessage.ZqBean zqBean = callPoliceMessage.getZq();
                    String disasterId = zqBean.getAlarmid();
                    String disasterType = zqBean.getZqlxdm();
                    String mapx = zqBean.getGis_x() + "";
                    String mapy = zqBean.getGis_y() + "";
                    String address = zqBean.getZhdd();
                    ListenerManager.getInstance().sendRescueDisposalReLoadPositionData(disasterId, disasterType, mapx, mapy, address);
                }
            }
            mViewPager.setCurrentItem(2);
        }
        /**
         * 指定成员通知
         */
        else if (type == 2 || type == 3) {
            String noticeId = intent.getStringExtra("id");
            Intent notifyIntent = new Intent(MainActivity.this, NotifyDetailActivity.class);
            notifyIntent.putExtra("id", noticeId);
            startActivity(notifyIntent);
        } else if (type == 4) {
            ReceiveRollCallMessage receiveRollCallMessage = (ReceiveRollCallMessage) getIntent().getSerializableExtra("ReceiveRollCallMessage");
            Intent rollCallIntent = new Intent(MainActivity.this, RollCallAnswerActivity.class);
            rollCallIntent.putExtra("ReceiveRollCallMessage", receiveRollCallMessage);
            startActivity(rollCallIntent);
        } else if (type == 5) {
            CallPoliceMessage info = (CallPoliceMessage) intent.getSerializableExtra("msgContentBean");
            CallPoliceMessage.ZqBean zqBean = info.getZq();
            if (zqBean != null) {
                mViewPager.setCurrentItem(2);
                rescueDisposalFragment.reLoadPosition(zqBean.getAlarmid(), zqBean.getZqlxdm(), zqBean.getGis_x() + "", zqBean.getGis_y() + "", zqBean.getZhdd());
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ConstData.isMainActivtyFrontShow = true;
        IMInformOperate.getInstance().reset();
    }

    @Override
    public void onPause() {
        Log.e("onNewArrived", "onPause");
        super.onPause();
        ConstData.isMainActivtyFrontShow = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化未读View
     */
    private void initUnNumberView() {
        try {
            //获取整个的NavigationView
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
            //这里就是获取所添加的每一个Tab(或者叫menu)，
            View tab = menuView.getChildAt(1);
            BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
            //加载我们的角标View，新创建的一个布局
            View badge = LayoutInflater.from(this).inflate(R.layout.im_unnumber_badge, menuView, false);
            tv_unNumber = badge.findViewById(R.id.tv_msg_count);
            //添加到Tab上
            itemView.addView(badge);
        } catch (Exception ex) {
            TUIKitLog.e("====" + ex.getMessage());
        }
    }

    private UnreadCountTextView tv_unNumber;

    @Override
    public void updateUnread(int count) {
        if (null != tv_unNumber) {

            TUIKitLog.e("========更新未读数量=========" + count);
            tv_unNumber.setText(String.valueOf(count));
            tv_unNumber.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        }

    }


    //获取新的版本信息
    private void getNewVersion() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getAppUpdateInfo, RequestMethod.GET);
        //此处版本号可以传任意值
        request.add("version", AppProxy.getInstance().getVERSION_NAME());
        request.add("type", 2);
        NoHttpManager.getRequestInstance().add(MainActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            Version version = gson.fromJson(responseBean.getData(), Version.class);
                            String versionStr = version.getDataversion();
                            if (versionStr.substring(0, 1).equals("V") || versionStr.substring(0, 1).equals("v")) {
                                versionStr = versionStr.substring(1);
                            }
                            String currentVersionStr = AppProxy.getInstance().getVERSION_NAME();
                            if (AppUtils.compareVersion(versionStr, currentVersionStr) > 0) {
                                File updateSystemDir = new File(ConstData.UPDATE_SYSTEM_DIR);
                                if (!updateSystemDir.exists()) {
                                    updateSystemDir.mkdirs();
                                }
                                File installFile = new File(ConstData.UPDATE_SYSTEM_DIR, getString(R.string.app_name) + version.getDataversion() + ".apk");
                                if (installFile.exists()) {
                                    isInstallDialog(installFile, version.getDataversion());
                                } else {
                                    updateDialog(version);
                                }
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_NO_UPDATE;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_UPDATE_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                HttpResponseUtil.showResponse(MainActivity.this, exception, "获取版本信息失败");
                Message msg = mHandler.obtainMessage();
                msg.what = GET_UPDATE_FAIL;
                mHandler.sendMessage(msg);
            }
        }, true, true);
    }

    public void isInstallDialog(final File installFile, String content) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_version_exist, null);
        TextView versionContent = (TextView) view.findViewById(R.id.new_version_content);
        LinearLayout checkedLayout = (LinearLayout) view.findViewById(R.id.btn_checked_layout);
        LinearLayout ignoreLayout = (LinearLayout) view.findViewById(R.id.btn_ignore_layout);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        versionContent.setText("已下载最新版版本" + content + "是否安装？");
        checkedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.installApk(installFile, MainActivity.this);
                dialog.dismiss();
            }
        });
        ignoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 更新dialog
     */
    public void updateDialog(final Version version) {
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.lib_update_app_dialog, null);
        TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        TextView tv_update_info = (TextView) dialogView.findViewById(R.id.tv_update_info);
        //进度条
//        final NumberProgressBar mNumberProgressBar = (NumberProgressBar) dialogView.findViewById(R.id.npb);
        final Dialog dialog = new MyDialog(MainActivity.this, dialogView, R.style.UpdateAppDialog);
        //设置点击外部区域不消失
        dialog.setCancelable(false);
        // 把自定义view加上去
        dialog.getWindow().setContentView(dialogView);
        dialog.show();

        tv_title.setText("是否升级到" + version.getDataversion() + "版本");
        tv_update_info.setText(version.getUpdateInfo());
        // 点击屏幕外侧，dialog不消失
        dialog.setCanceledOnTouchOutside(false);
        Button btnOK = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toastShortMessage("正在后台下载...");
                Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
                updateIntent.putExtra("version", version.getDataversion());
                updateIntent.putExtra("down_url", ConstData.urlManager.baseFilesURL + version.getDownUrl());
                startService(updateIntent);
                dialog.dismiss();
            }
        });
        LinearLayout ll_close = (LinearLayout) dialogView.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_UPDATE_SUCCESS:
//                    ToastUtils.toastShortMessage("获取版本信息成功");
                    break;
                case GET_NO_UPDATE:
//                    ToastUtils.toastShortMessage("已是最新版本");
                    break;
                case GET_UPDATE_FAIL:
//                    ToastUtils.toastShortMessage("获取版本信息失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());


}
