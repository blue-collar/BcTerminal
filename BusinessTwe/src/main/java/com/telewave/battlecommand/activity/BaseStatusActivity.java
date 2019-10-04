package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.telewave.battlecommand.contract.FragmentBackListener;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;

import java.lang.reflect.Field;

/**
 * Activity基类
 */

public abstract class BaseStatusActivity extends BaseActivity {


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(getLayoutId());
        setmarquee();
    }


    private void setmarquee() {
        ViewConfiguration configuration = ViewConfiguration.get(this);
        Class claz = configuration.getClass();
        try {
            Field field = claz.getDeclaredField("mFadingMarqueeEnabled");
            field.setAccessible(true);
            field.set(configuration, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    private long exitTime = 0;


    //Fragment返回键监听
    private FragmentBackListener backListener;
    private boolean isInterception = false;

    public FragmentBackListener getBackListener() {
        return backListener;
    }

    public void setBackListener(FragmentBackListener backListener) {
        this.backListener = backListener;
    }

    public boolean isInterception() {
        return isInterception;
    }

    public void setInterception(boolean isInterception) {
        this.isInterception = isInterception;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isInterception()) {
                if (backListener != null) {
                    backListener.onBackForward();
                    return false;
                }
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtils.toastShortMessage("再按一次程序将退到后台运行");
                    exitTime = System.currentTimeMillis();
                } else {
                    /**
                     * 2018/10/25
                     * 按返回键改为程序退到后台，不退出
                     */
                    moveTaskToBack(false);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
