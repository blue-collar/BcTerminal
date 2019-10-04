package com.telewave.lib.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.telewave.lib.log.impl.TweLogReceiverImpl;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des 用于接收和处理Log相关广播消息
 */
public class TweLogReceiver extends BroadcastReceiver {

    public final static String ACTION_FORCE_LOG = "twe.intent.action.FORCE_LOG";

    public final static String ACTION_TRACE_LOG = "twe.intent.action.TRACE_LOG";

    public final static String FORCE_LOG_FLAG = "FORCE_LOG_FLAG";

    public final static String TRACE_LOG_FLAG = "TRACE_LOG_FLAG";

    private TweLogReceiverImpl mReceiver = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiver == null) {
            mReceiver = new TweLogReceiverImpl();
        }
        mReceiver.onReceive(context, intent);
    }
}
