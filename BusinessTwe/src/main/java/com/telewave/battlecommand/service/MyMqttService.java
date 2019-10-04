package com.telewave.battlecommand.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.telewave.battlecommand.mqtt.MqttFilter.MqttFilter;
import com.telewave.lib.base.ConstData;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyMqttService extends Service {

    public static final String TAG = MyMqttService.class.getSimpleName();

    private static MqttAndroidClient mqttAndroidClient;
    private static final String MQTT_URL_FORMAT = "tcp://%s:%d";
    private static final String DEVICE_ID_FORMAT = "Andr_%s";
    private static final int qos = 2;
    private static String subscribeTopic = ConstData.WebToAppTp;
    private static String publishTopic = ConstData.AppToWebTp;
    private static int MessageId = 0x0001;
    private ExecutorService mExecutor = Executors.newCachedThreadPool();

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, "notification_id").build();
            startForeground(Integer.MAX_VALUE, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String url = String.format(Locale.US, MQTT_URL_FORMAT, ConstData.urlManager.activemqIp, ConstData.URLManager.MqPort);
        final String deviceId = String.format(Locale.US, DEVICE_ID_FORMAT, ConstData.deviceId);
        initMqtt(url, deviceId);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启服务
     */
    public static void startService(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(new Intent(mContext, MyMqttService.class));
        } else {
            mContext.startService(new Intent(mContext, MyMqttService.class));
        }
    }


    private void initMqtt(String url, String deviceId) {
        mqttAndroidClient = new MqttAndroidClient(this, url, deviceId + "01", MqttAndroidClient.Ack.AUTO_ACK);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    subscribeToTopic(subscribeTopic);
                    subscribeToTopic(publishTopic);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.e(TAG, "connectionLost: " + "连接断开了");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                Log.e(TAG, "CallBack messageArrived: " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        if (!mqttAndroidClient.isConnected() && isConnectIsNormal()) {
            connectServer();
        }
    }

    /**
     * 连接服务器
     */
    public void connectServer() {
        //设置连接参数
        final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        //自动重连
        mqttConnectOptions.setAutomaticReconnect(true);
        //连接超时的时间
        mqttConnectOptions.setConnectionTimeout(60);
        //设置会话心跳时间 单位为秒
        mqttConnectOptions.setKeepAliveInterval(5);
        //断开连接之后,删除对应的主题.如果要实现断网重连之后重新接收消息,要设置为false的
        /**
         * 2019/01/17
         * 解决 每次登陆未结案的警情里面都会把系统已经结案的警情逐一提醒一遍
         */
        mqttConnectOptions.setCleanSession(true);
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.e(TAG, "Mq连接成功");
                    //保存断开连接的时候的缓存消息
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    //允许使用缓冲区
                    disconnectedBufferOptions.setBufferEnabled(true);
                    //缓冲区大小
                    disconnectedBufferOptions.setBufferSize(1024);
                    //是持久的缓冲区
                    disconnectedBufferOptions.setPersistBuffer(true);
                    //当缓冲区满的时候,删除旧的消息
                    disconnectedBufferOptions.setDeleteOldestMessages(true);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic(subscribeTopic);
                    subscribeToTopic(publishTopic);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e(TAG, "Connect onFailure: " + "连接失败了\n " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "connectServer: " + "通讯服务器连接超时,正在重连...");
        }
    }

    //订阅消息,用于订阅指定的主题
    private void subscribeToTopic(String topic) {
        if (mqttAndroidClient == null) {
            return;
        }
        try {
            //订阅的qos和发布的qos共同决定了最终的qos,qos范围有0,1,2,代表最多受到一次(不保证是否接受到),1(最少一次,可能会造成重复接受),2(只有一次,对于一个messageId,
            //只会受到一条这样的消息,但是消费的资源也是最多的.
            //最终的qos为订阅qos和发布的消息qos中最小的那个,比如:0和1取0,1和1取1,2和1取1
            mqttAndroidClient.subscribe(topic, qos, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, final MqttMessage message) throws Exception {
//                    Log.e(TAG, "messageArrived: " + new String(message.getPayload()));
                    //将消息交于mqttFilter处理
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            MqttFilter.MsgHandle(new String(message.getPayload()));
                        }
                    });
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "messageArrived: " + e.toString());
            e.printStackTrace();
        }
    }


    //发布消息,用于给指定的主题发送消息
    public static void publishMessage(final String message) {
        if (!isConnected() || TextUtils.isEmpty(message)) {
            return;
        }
        try {
            //新建订阅的消息类
            MqttMessage mqttMessage = new MqttMessage(message.getBytes("utf-8"));
            //设置id,实现断网重连重新接收消息的话一定要设置id
            mqttMessage.setId(MessageId++);
            //设置消息到达形式,只保证一次
            mqttMessage.setQos(2);
            //消息发送出去之后不保留,如果保留的话.之后新连接进来的客户端同样会受到这些消息的
            mqttMessage.setRetained(false);
            mqttAndroidClient.publish(publishTopic, mqttMessage, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.e(TAG, " publish onSuccess: " + "发送成功");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e(TAG, " publish failer: " + "发送失败");
                }
            });
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //断开与服务器的连接
    public void disconnectMqttConnection() {
        if (mqttAndroidClient == null) {
            return;
        }
        try {
            //断开连接
            mqttAndroidClient.disconnect();
            Log.e(TAG, "disconnectMqttConnection: " + "断开连接了");
        } catch (MqttException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            mqttAndroidClient = null;
        }
    }

    //返回客户端是否是连接的状态
    public static boolean isConnected() {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }


    @Override
    public void onDestroy() {
        stopSelf();
        disconnectMqttConnection();
        super.onDestroy();
    }


    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getName(), "onBind");
        return new CustomBinder();
    }


    public class CustomBinder extends Binder {
        public MyMqttService getService() {
            return MyMqttService.this;
        }
    }

}
