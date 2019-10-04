package com.telewave.battlecommand.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.telewave.business.twe.R;

/**
 * Created by wc on 2016/10/29.
 */
public class ChatVoicePlayAnimView extends ImageView {

    private Context context;

    private Animation anim;

    private AnimationDrawable animDrawableSend;

    private AnimationDrawable animDrawableRecieve;

    /**
     *
     */
    private int mDuration = 300;


    private boolean isRunning = false;

    private boolean IsSend = true;

    /**
     * @param context
     */
    public ChatVoicePlayAnimView(Context context) {
        super(context);
        this.context = context;
        initCCPAnimImageView();
    }

    /**
     * @param context
     * @param attrs
     */
    public ChatVoicePlayAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCCPAnimImageView();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ChatVoicePlayAnimView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initCCPAnimImageView();
    }


    /**
     *
     */
    public void initCCPAnimImageView() {

        anim = new AlphaAnimation(0.1F, 0.1F);
        anim.setDuration(1000L);
        anim.setRepeatCount(AlphaAnimation.INFINITE);
        anim.setRepeatMode(AlphaAnimation.REVERSE);

        // chatting from animation
        animDrawableRecieve = new AnimationDrawable();
        Drawable chattingFDrawale1 = getResources().getDrawable(R.mipmap.chatfrom_voice_playing_f1);
        chattingFDrawale1.setBounds(0, 0, chattingFDrawale1.getIntrinsicWidth(), chattingFDrawale1.getIntrinsicHeight());
        animDrawableRecieve.addFrame(chattingFDrawale1, mDuration);

        Drawable chattingFDrawale2 = getResources().getDrawable(R.mipmap.chatfrom_voice_playing_f2);
        chattingFDrawale2.setBounds(0, 0, chattingFDrawale2.getIntrinsicWidth(), chattingFDrawale2.getIntrinsicHeight());
        animDrawableRecieve.addFrame(chattingFDrawale2, mDuration);

        Drawable chattingFDrawale3 = getResources().getDrawable(R.mipmap.chatfrom_voice_playing_f3);
        chattingFDrawale3.setBounds(0, 0, chattingFDrawale3.getIntrinsicWidth(), chattingFDrawale3.getIntrinsicHeight());
        animDrawableRecieve.addFrame(chattingFDrawale3, mDuration);
        animDrawableRecieve.setOneShot(false);
        animDrawableRecieve.setVisible(true, true);

        // chatting to animation
        animDrawableSend = new AnimationDrawable();
        Drawable chattingTDrawable_1 = getResources().getDrawable(R.mipmap.chatto_voice_playing_f1);
        chattingTDrawable_1.setBounds(0, 0, chattingTDrawable_1.getIntrinsicWidth(), chattingTDrawable_1.getIntrinsicHeight());
        animDrawableSend.addFrame(chattingTDrawable_1, mDuration);

        Drawable chattingTDrawable_2 = getResources().getDrawable(R.mipmap.chatto_voice_playing_f2);
        chattingTDrawable_2.setBounds(0, 0, chattingTDrawable_2.getIntrinsicWidth(), chattingTDrawable_2.getIntrinsicHeight());
        animDrawableSend.addFrame(chattingTDrawable_2, mDuration);

        Drawable chattingTDrawable_3 = getResources().getDrawable(R.mipmap.chatto_voice_playing_f3);
        chattingTDrawable_3.setBounds(0, 0, chattingTDrawable_3.getIntrinsicWidth(), chattingTDrawable_3.getIntrinsicHeight());
        animDrawableSend.addFrame(chattingTDrawable_3, mDuration);
        animDrawableSend.setOneShot(false);
        animDrawableSend.setVisible(true, true);
    }

    public void setIsSend(boolean isSend) {
        IsSend = isSend;
    }

    public final void startVoiceAnimation() {
        if (!isRunning) {
            isRunning = true;
            if (IsSend) {
                setImageDrawable(animDrawableSend);
                animDrawableSend.stop();
                animDrawableSend.start();
                return;
            }
            setImageDrawable(animDrawableRecieve);
            animDrawableRecieve.stop();
            animDrawableRecieve.start();
        }
    }

    /**
     *
     */
    public final void stopVoiceAnimation() {
        if (anim != null && anim.isInitialized()) {
            setAnimation(null);
        }
        isRunning = false;
        setBackgroundDrawable(null);
        if (IsSend) {
            setImageDrawable(getResources().getDrawable(R.mipmap.chatto_voice_playing));
        } else {
            setImageDrawable(getResources().getDrawable(R.mipmap.chatfrom_voice_playing));
        }

        this.animDrawableSend.stop();
        this.animDrawableRecieve.stop();
    }


}
