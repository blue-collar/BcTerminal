package com.telewave.lib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.telewave.lib.base.R;
import com.telewave.lib.widget.toolbar.TweToolbar;
import com.telewave.lib.widget.util.IMActivityUtil;
import com.telewave.lib.widget.util.StatusBarUtils;

import java.lang.reflect.Method;

/**
 * @Author: rick_tan
 * @Date: 19-7-21
 * @Version: v1.0
 * @Des BaseActivity
 */
public class BaseActivity extends AppCompatActivity {
    protected static final String FRAGMENTS_TAG = "android:support:fragments";
    protected TweToolbar mToolbar;
    private View mStatusBar_PlaceHolder = null;

    private static int mSysStatusBarHeight = 0;
    private static int mTweStatusBarHeight = 0;
    private boolean isImmersiveMode = false;
    private int statusbarColor = Color.TRANSPARENT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //首先从主题获取是否需要沉浸式状态栏
        final TypedArray ta = obtainStyledAttributes(R.styleable.ImmersiveStatus);
        isImmersiveMode = ta.getBoolean(R.styleable.ImmersiveStatus_immersivemode, false);
        statusbarColor = ta.getInt(R.styleable.ImmersiveStatus_statusbarcolor, Color.TRANSPARENT);
        ta.recycle();

        if (isImmersiveMode && (getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) <= 0) {
            Toast.makeText(this, "is ImmersiveMode", Toast.LENGTH_SHORT).show();
            StatusBarUtils.openStatusBarImmersiveMode(this, statusbarColor);
        }
        setUpViewAndData(savedInstanceState);
        IMActivityUtil.addActivity(this);
    }

    //仅仅是用于兼容twe之前的版本
    protected void setUpViewAndData(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMActivityUtil.removeActivity(this);
    }

    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getStatusBarHeight();
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.twe_tool_bar);
        mStatusBar_PlaceHolder = findViewById(R.id.status_bar_place_holder);
        if (null == getSupportActionBar() && null != mToolbar) {
            setSupportActionBar(mToolbar);
            checkStatusBarHeight(isImmersiveMode);
        } else {
            if (null != mStatusBar_PlaceHolder) {
                mStatusBar_PlaceHolder.setVisibility(View.GONE);
            }

            if (null != mToolbar) {
                mToolbar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 校准顶部系统状态栏高度
     */
    private void checkStatusBarHeight(boolean isImmersiveMode) {
        if (0 < mSysStatusBarHeight && mSysStatusBarHeight != mTweStatusBarHeight) {
            if (null != mStatusBar_PlaceHolder) {
                ViewGroup.LayoutParams lp = mStatusBar_PlaceHolder.getLayoutParams();
                lp.height = mSysStatusBarHeight;
                mStatusBar_PlaceHolder.setLayoutParams(lp);
            }
        }

        if (!isImmersiveMode && null != mStatusBar_PlaceHolder) {
            mStatusBar_PlaceHolder.setVisibility(View.GONE);
        }
    }

    public void forceShowStatusBarPlaceHolder() {
        if (null != mStatusBar_PlaceHolder) {
            mStatusBar_PlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private void getStatusBarHeight() {
        if (mSysStatusBarHeight == 0) {
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            mSysStatusBarHeight = rect.top;
            if (0 == mSysStatusBarHeight) {
                try {
                    Class<?> localClass = Class.forName("com.android.internal.R$dimen");
                    Object object = localClass.newInstance();
                    int height = Integer.parseInt(localClass.getField("status_bar_height").get(object).toString());
                    mSysStatusBarHeight = getResources().getDimensionPixelSize(height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (mTweStatusBarHeight == 0) {
            mTweStatusBarHeight = (int) getResources().getDimension(R.dimen.status_bar_height);
        }
    }

    protected void setSubtitle(CharSequence title) {
        if (null != mToolbar) {
            mToolbar.setSubtitle(title);
        }
    }

    protected void setSubtitleColor(int textColor) {
        if (null != mToolbar) {
            mToolbar.setSubtitleTextColor(textColor);
        }
    }

    @Override
    public void setTitleColor(int textColor) {
        if (null != mToolbar) {
            mToolbar.setTitleTextColor(textColor);
        } else {
            super.setTitleColor(textColor);
        }
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        if (null != mToolbar) {
            mToolbar.setNavigationIcon(icon);
        }
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        if (null != mToolbar) {
            mToolbar.setNavigationIcon(resId);
        }
    }

    public void setNavigationOnClickListener(View.OnClickListener listener) {
        if (null != mToolbar) {
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    public void setLogo(int resId) {
        if (null != mToolbar) {
            mToolbar.setLogo(resId);
        }
    }

    public void setLogo(Drawable drawable) {
        if (null != mToolbar) {
            mToolbar.setLogo(drawable);
        }
    }


    protected int getOptionsMenuID() {
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final int menuRes = getOptionsMenuID();
        if (0 == menuRes)
            return super.onCreateOptionsMenu(menu);
        else {
            // 绑定toobar跟menu
            getMenuInflater().inflate(menuRes, menu);
            return true;
        }
    }

    /**
     * 让showAsAction="never"属性的菜单也可以显示图标
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener listener) {
        if (mToolbar != null) {
            mToolbar.setOnMenuItemClickListener(listener);
        }
    }
}
