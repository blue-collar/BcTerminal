package com.telewave.lib.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @Author: rick_tan
 * @Date: 19-7-21
 * @Version: v1.0
 * @Des BaseFragment
 */
public class BaseFragment extends Fragment {

    /**
     * TAG：可供日志打印使用
     */
    protected String TAG;

    /**
     * 上下文
     */
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        TAG = getTag();
    }


}
