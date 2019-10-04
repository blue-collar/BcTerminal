package com.telewave.battlecommand.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.telewave.business.twe.R;

/**
 * Created by wc on 2016/10/31.
 */
public class ChatVoiceRecordingAnimView extends ImageView {

    private AlphaAnimation anima;

    private AnimationDrawable animationDrawable;

    private boolean isRunning = false;

    private int a[] = {R.mipmap.amp1, R.mipmap.amp2, R.mipmap.amp3
            , R.mipmap.amp4, R.mipmap.amp5, R.mipmap.amp6, R.mipmap.amp7};

    public ChatVoiceRecordingAnimView(Context context) {
        super(context);
        init();
    }

    public ChatVoiceRecordingAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatVoiceRecordingAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        anima = new AlphaAnimation(0.1F, 0.1F);
        anima.setDuration(1000L);
        anima.setRepeatCount(AlphaAnimation.INFINITE);
        anima.setRepeatMode(AlphaAnimation.REVERSE);

        animationDrawable = new AnimationDrawable();
        for (int i = 0; i < a.length; i++) {
            Drawable drawable = getResources().getDrawable(a[i]);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            animationDrawable.addFrame(drawable, 300);
        }
        animationDrawable.setOneShot(false);
        animationDrawable.setVisible(true, true);
    }

    private final void startVoiceRecordAnimation() {
        if (!isRunning) {
            isRunning = true;
//            setAnimation(anima);
//            anima.startNow();
            setImageDrawable(this.animationDrawable);
            this.animationDrawable.start();
        }
    }

    private final void StopVoiceRecordAnimation() {
        if (anima != null && anima.isInitialized()) {
            setAnimation(null);
        }
        isRunning = false;
        setImageDrawable(null);
        setBackgroundDrawable(null);
        this.animationDrawable.stop();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startVoiceRecordAnimation();
        } else
            StopVoiceRecordAnimation();
    }
}
