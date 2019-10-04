package com.telewave.battlecommand.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;

import com.telewave.battlecommand.trtc.TRTCAudioCallActivity;
import com.telewave.business.twe.R;
import com.telewave.lib.base.Constents;

/**
 * 语音悬浮窗服务
 */
public class FloatAudioWindowService extends Service {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private LayoutInflater inflater;
    //浮动布局view
    private View mFloatingLayout;
    //容器父布局
    private FrameLayout smallSizePreviewLayout;
    //时间计时控件
    private Chronometer floatAudioCallTimeChronometer;

    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();//设置悬浮窗基本参数（位置、宽高等）
        initFloating();//悬浮框点击事件的处理
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new FloatAudioWindowService.MyBinder();
    }

    public class MyBinder extends Binder {
        public FloatAudioWindowService getService() {
            return FloatAudioWindowService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingLayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mFloatingLayout);
            mOnFloatClickListener = null;
            mFloatingLayout = null;
            Constents.isShowFloatWindow = false;
        }
    }

    /**
     * 设置悬浮框基本参数（位置、宽高等）
     */
    private void initWindow() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置好悬浮窗的参数
        wmParams = getParams();
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        wmParams.x = 70;
        wmParams.y = 210;
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(getApplicationContext());
        // 获取浮动窗口视图所在布局
        mFloatingLayout = inflater.inflate(R.layout.alert_float_audio_layout, null);
        // 添加悬浮窗的视图
        mWindowManager.addView(mFloatingLayout, wmParams);
    }


    private WindowManager.LayoutParams getParams() {
        wmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //设置可以显示在状态栏上
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return wmParams;
    }

    private void initFloating() {
        smallSizePreviewLayout = mFloatingLayout.findViewById(R.id.audio_small_size_preview);
        floatAudioCallTimeChronometer = mFloatingLayout.findViewById(R.id.float_audio_call_time_chronometer);
        floatAudioCallTimeChronometer.setBase(Constents.audioCallStartTime);
        floatAudioCallTimeChronometer.start();
        Constents.isShowFloatWindow = true;
        //悬浮框触摸事件，设置悬浮框可拖动
        smallSizePreviewLayout.setOnTouchListener(new FloatingListener());

        setOnFloatClickListener(new OnFloatClickListener() {
            @Override
            public void onSingleClick(View view) {
                //在这里实现点击重新回到Activity
                Intent intent = new Intent(FloatAudioWindowService.this, TRTCAudioCallActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick() {

            }
        });

    }

    public void closeWindow() {
        if (mFloatingLayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mFloatingLayout);
            mOnFloatClickListener = null;
            mFloatingLayout = null;
            Constents.isShowFloatWindow = false;
        }
    }

    float downX = 0;
    float downY = 0;
    int oddOffsetX = 0;
    int oddOffsetY = 0;
    /**
     * 是否移动
     */
    private boolean isMove = true;
    private OnFloatClickListener mOnFloatClickListener;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isMove = false;
            if (mOnFloatClickListener != null) {
                mWindowManager.updateViewLayout(mFloatingLayout, wmParams);
                mOnFloatClickListener.onLongClick();
            }
        }
    };

    private class FloatingListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    oddOffsetX = wmParams.x;
                    oddOffsetY = wmParams.y;
                    Log.e("ScannerFloatView", "点击按下悬浮框==downX" + downX + ";downY=" + downY + ";oddOffsetX=" + oddOffsetX + ";oddOffsetY=" + oddOffsetY);
                    handler.postDelayed(runnable, ViewConfiguration.getLongPressTimeout());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isMove) {
                        break;
                    }
                    float moveX = event.getX();
                    float moveY = event.getY();
                    //不除以3，拖动的view抖动的有点厉害
                    wmParams.x += (moveX - downX) / 3;
                    wmParams.y += (moveY - downY) / 3;
                    Log.e("ScannerFloatView", "点击“移动”悬浮框==moveX" + moveX + ";moveY=" + moveY + ";wmParams.x=" + wmParams.x + ";wmParams.y=" + wmParams.y);
                    Log.e("ScannerFloatView", "移动的距离==x:" + Math.abs(moveX - downX) + ";y==" + Math.abs(moveY - downY));
                    int scaledTouchSlop = ViewConfiguration.get(FloatAudioWindowService.this).getScaledTouchSlop();
                    //移动超过了阈值，表示移动了
                    if (Math.abs(moveX - downX) > scaledTouchSlop || Math.abs(moveY - downY) > scaledTouchSlop) {
                        isMove = true;
                        //移除runnable
                        handler.removeCallbacks(runnable);
                        if (mFloatingLayout != null) {
                            mWindowManager.updateViewLayout(mFloatingLayout, wmParams);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    int newOffsetX = wmParams.x;
                    int newOffsetY = wmParams.y;

                    handler.removeCallbacks(runnable);
                    Log.e("ScannerFloatView", "点击“抬起”悬浮框==newOffsetX=" + newOffsetX + ";newOffsetY=" + newOffsetY + ";oddOffsetX=" + oddOffsetX + ";oddOffsetY=" + oddOffsetY);
                    Log.e("ScannerFloatView", "移动的距离==x:" + Math.abs(newOffsetX - oddOffsetX) + ";y==" + Math.abs(newOffsetY - oddOffsetY));
                    if (Math.abs(newOffsetX - oddOffsetX) <= 20 || Math.abs(newOffsetY - oddOffsetY) <= 20) {
                        isMove = true;
                        if (mOnFloatClickListener != null) {
                            if (mFloatingLayout != null) {
                                mWindowManager.updateViewLayout(mFloatingLayout, wmParams);
                            }
                            mOnFloatClickListener.onSingleClick(mFloatingLayout);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("ScannerFloatView", "取消");
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    public interface OnFloatClickListener {
        void onSingleClick(View view);

        void onLongClick();

    }

    public void setOnFloatClickListener(OnFloatClickListener onFloatClickListener) {
        mOnFloatClickListener = onFloatClickListener;
    }


}