package com.telewave.battlecommand.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.telewave.business.twe.R;


/**
 * Note:
 * Created by liwh on 2016/11/3 9:14.
 */

public class CheckableImageButton extends ImageButton {

    private boolean isChecked;
    private int checkedResourceId;
    private int normalResourceId;
    //能否重复改变状态
    private boolean canRepeat;
    //是否是第一次点击,因为即使不能重复更改状态,但是第一次是可以改变的
    private boolean canClick = true;
    //之前是没有选中的,这样做的为了防止获取不到第一手的状态.因为点击事件在up中获取,如果处在不能重复改变状态下
    //状态永远都是true
    private boolean preStatus = false;
    private boolean canTouchable = true;
    private boolean origanchecked;

    public CheckableImageButton(Context context) {
        this(context, null);
    }

    public CheckableImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CheckableImageButton);
        isChecked = array.getBoolean(R.styleable.CheckableImageButton_isChecked, false);
        checkedResourceId = array.getResourceId(R.styleable.CheckableImageButton_checked_bg, R.mipmap.toggle_btn_checked);
        normalResourceId = array.getResourceId(R.styleable.CheckableImageButton_normal_bg, R.mipmap.toggle_btn_unchecked);
        canRepeat = array.getBoolean(R.styleable.CheckableImageButton_canrepeat, true);
        canTouchable = array.getBoolean(R.styleable.CheckableImageButton_canTouchable, true);
        array.recycle();
        //刚开始两种状态是一样的
        origanchecked = preStatus = isChecked;
        if (isChecked) {
            setBackgroundResource(checkedResourceId);

        } else {
            setBackgroundResource(normalResourceId);

        }

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (canTouchable) {
                    if (canClick) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //设置成跟之前不一样的状态
                            setChecked(!isChecked);
                            if (listener != null) {
                                listener.onButtonStatusChanged(CheckableImageButton.this, isChecked);
                            }
                        }
                    }
                    if (!canRepeat) {
                        canClick = false;
                    }
                }
                return false;
            }
        });
    }

    private void setColorStatus(boolean isChecked) {
        if (isChecked) {
            setBackgroundResource(checkedResourceId);
        } else {
            setBackgroundResource(normalResourceId);
        }
    }

    //返回现在的状态
    public boolean isChecked() {
        return isChecked;
    }

    //更改状态
    public void setChecked(boolean isChecked) {
        preStatus = this.isChecked;
        this.isChecked = isChecked;
        setColorStatus(isChecked);
        //在不能重复的状态下改变了,说明以后再也不能改变了
        if (!canRepeat && this.isChecked != origanchecked) {
            setCanTouchable(false);
        }
    }

    //这个是直接修改的,包含之前的状态,适用于直接set之后就不能点击的那种
    public void setChecked(boolean isChecked, boolean preStatus) {
        setChecked(isChecked);
        this.preStatus = preStatus;
    }

    //返回之前的状态
    public boolean getPreStatus() {
        return preStatus;
    }

    //清楚标志
    public void resetStatus() {
        preStatus = isChecked = origanchecked;
        canClick = true;
        setChecked(origanchecked);
        setCanTouchable(true);
    }

    public void setCanTouchable(boolean canTouchable) {
        this.canTouchable = canTouchable;
    }

    public boolean getTouchable() {
        return this.canTouchable;
    }

    public interface OnStatusChangedListener {
        void onButtonStatusChanged(View v, boolean isChecked);
    }

    private OnStatusChangedListener listener;

    public void setOnStatusChangedListener(OnStatusChangedListener listener) {
        this.listener = listener;
    }
}
