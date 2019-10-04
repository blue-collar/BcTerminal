package com.telewave.lib.widget.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.telewave.lib.base.R;

/**
 * @Author: rick_tan
 * @Date: 19-7-21
 * @Version: v1.0
 * @Des 自定义工具栏
 */
public class TweToolbar extends android.support.v7.widget.Toolbar {
    protected TextView mTitleTextView;
    protected TextView mSubtitleTextView;

    protected CharSequence mTitleText;
    protected CharSequence mSubtitleText;

    public TweToolbar(Context context) {
        this(context, null);
    }

    public TweToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public TweToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TweToolbar, defStyleAttr, 0);
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                mTitleTextView = findViewById(R.id.twe_toolbar_title);
            }
        }

        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }

        mTitleText = title;
    }

    @Override
    public void setSubtitle(int resId) {
        setSubtitle(getContext().getText(resId));
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        if (mSubtitleTextView == null) {
            mSubtitleTextView = findViewById(R.id.twe_toolbar_subtitle);
        }

        if (mSubtitleTextView != null) {
            if (!TextUtils.isEmpty(subtitle)) {
                mSubtitleTextView.setText(subtitle);
                mSubtitleTextView.setVisibility(View.VISIBLE);
            } else {
                mSubtitleTextView.setVisibility(View.GONE);
            }
        }

        mSubtitleText = subtitle;
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        super.setTitleTextAppearance(context, resId);
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setSubtitleTextAppearance(Context context, int resId) {
        super.setSubtitleTextAppearance(context, resId);
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        super.setTitleTextColor(color);
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    @Override
    public void setSubtitleTextColor(int color) {
        super.setSubtitleTextColor(color);
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextColor(color);
        }
    }
}