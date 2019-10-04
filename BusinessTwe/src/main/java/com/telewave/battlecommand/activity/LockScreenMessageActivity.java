package com.telewave.battlecommand.activity;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.MessageType;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;
import com.telewave.business.twe.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 锁屏消息内容的activity
 */
public class LockScreenMessageActivity extends AppCompatActivity {
    private static final String TAG = "LockScreenMessage";
    private TextView timeTv;
    private TextView addressTv;
    private TextView organTv;
    private TextView gradeTv;
    private NewDisasterInfo.MsgContentBean msgContentBean;
    private CallPoliceMessage callPoliceMessage;
    private String msgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate:启动了消息内容的activity ");
        //四个标志位顾名思义，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕。这样当Activity启动的时候，它会解锁并亮屏显示。
        Window window = getWindow();
        //在Android 9.0 小米MIUI系统上加这个才有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            //四个标志位顾名思义，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕。这样当Activity启动的时候，它会解锁并亮屏显示。
            //注释 WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD 加入的话谷歌原生Android 7.0设备接收一次，后面的接收不到
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
//                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
        }
        //使用手机的背景
        Drawable wallPaper = WallpaperManager.getInstance(this).getDrawable();
        window.setBackgroundDrawable(wallPaper);
        setContentView(R.layout.activity_lock_screen_message);
        initView();
        msgType = getIntent().getStringExtra("msgType");
        if (msgType.equals(MessageType.NEW_DISASTER_INFO)) {
            NewDisasterInfo.MsgContentBean msgContentBean = (NewDisasterInfo.MsgContentBean)
                    getIntent().getSerializableExtra("msgContentBean");
            if (msgContentBean != null && msgContentBean.getZq() != null) {
                addressTv.setText(msgContentBean.getZq().getZhdd());
                organTv.setText("主管中队：" + msgContentBean.getZq().getXqzdjgmc());
                gradeTv.setText("灾害等级：" + msgContentBean.getZq().getZhdjdm() + "级");
                timeTv.setText(getTime());
            }
        } else if (msgType.equals(MessageType.RECEIVE_CALL_POLICE_MESSAGE)) {
            CallPoliceMessage callPoliceMessage = (CallPoliceMessage)
                    getIntent().getSerializableExtra("msgContentBean");
            if (callPoliceMessage != null && callPoliceMessage.getZq() != null) {
                addressTv.setText(callPoliceMessage.getZq().getZhdd());
                organTv.setText("主管中队：" + callPoliceMessage.getZq().getXqzdjgmc());
                gradeTv.setText("灾害等级：" + callPoliceMessage.getZq().getZhdjdm() + "级");
                timeTv.setText(getTime());
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Log.e(TAG, "onNewIntent: 66");
        //获取电源管理器对象
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();  //点亮屏幕
            wl.release();  //任务结束后释放
        }
        msgType = intent.getStringExtra("msgType");
        if (msgType.equals(MessageType.NEW_DISASTER_INFO)) {
            NewDisasterInfo.MsgContentBean msgContentBean = (NewDisasterInfo.MsgContentBean)
                    intent.getSerializableExtra("msgContentBean");
            if (msgContentBean != null && msgContentBean.getZq() != null) {
                addressTv.setText(msgContentBean.getZq().getZhdd());
                organTv.setText("主管中队：" + msgContentBean.getZq().getXqzdjgmc());
                gradeTv.setText("灾害等级：" + msgContentBean.getZq().getZhdjdm() + "级");
                timeTv.setText(getTime());
            }
        } else if (msgType.equals(MessageType.RECEIVE_CALL_POLICE_MESSAGE)) {
            CallPoliceMessage callPoliceMessage = (CallPoliceMessage)
                    intent.getSerializableExtra("msgContentBean");
            if (callPoliceMessage != null && callPoliceMessage.getZq() != null) {
                addressTv.setText(callPoliceMessage.getZq().getZhdd());
                organTv.setText("主管中队：" + callPoliceMessage.getZq().getXqzdjgmc());
                gradeTv.setText("灾害等级：" + callPoliceMessage.getZq().getZhdjdm() + "级");
                timeTv.setText(getTime());
            }
        }
    }

    private void initView() {
        timeTv = (TextView) findViewById(R.id.new_disaster_message_time_tv);
        addressTv = (TextView) findViewById(R.id.new_disaster_message_address_tv);
        organTv = (TextView) findViewById(R.id.new_disaster_message_organ_tv);
        gradeTv = (TextView) findViewById(R.id.new_disaster_message_grade_tv);
        findViewById(R.id.message_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先解锁系统自带锁屏服务，放在锁屏界面里面
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                //解锁
                keyguardManager.newKeyguardLock("FxLock").disableKeyguard();

                Intent intent = new Intent(LockScreenMessageActivity.this, MainActivity.class);
                if (msgType.equals(MessageType.NEW_DISASTER_INFO)) {
                    //在这里传递参数
                    intent.putExtra("type", 1);
                    intent.putExtra("msgType", MessageType.NEW_DISASTER_INFO);
                    intent.putExtra("msgContentBean", msgContentBean);
                } else if (msgType.equals(MessageType.RECEIVE_CALL_POLICE_MESSAGE)) {
                    //在这里传递参数
                    intent.putExtra("type", 1);
                    intent.putExtra("msgType", MessageType.RECEIVE_CALL_POLICE_MESSAGE);
                    intent.putExtra("msgContentBean", callPoliceMessage);
                }
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先解锁系统自带锁屏服务，放在锁屏界面里面
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                //反解锁
                keyguardManager.newKeyguardLock("FxLock").reenableKeyguard();
                finish();
            }
        });
    }


    private static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE);
        return format.format(new Date());
    }

}