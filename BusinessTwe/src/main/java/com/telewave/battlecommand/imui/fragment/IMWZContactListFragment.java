package com.telewave.battlecommand.imui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.activity.IMUserInfoActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.WZInfoObj;
import com.telewave.battlecommand.imui.bean.ZDInfoObj;
import com.telewave.battlecommand.imui.bean.parse.WZContactListParseObj;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;
import java.util.Locale;


/**
 * @author PF-NAN
 * @date 2019-08-02
 */
public class IMWZContactListFragment extends Fragment {
    private View mView;
    private LinearLayout ll_zdListRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.im_fragment_im_wzcontactlist, container, false);
        ll_zdListRoot = mView.findViewById(R.id.ll_zdListRoot);

        initData();
        return mView;
    }

    public void initData() {
        if (this.isVisible()) {
            Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.findAllWzAndMember, RequestMethod.POST);
            request.setDefineRequestBodyForJson("{}");
            NoHttpManager.getRequestInstance().add(getActivity(), 1001, request, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    String result = response.get();
                    WZContactListParseObj wzContactListParseObj = IMJsonUtil.parseJsonToBean(result, WZContactListParseObj.class);
                    initZDView(wzContactListParseObj);

                }

                @Override
                public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                    TUIKitLog.e(url + "=====onFailed=" + tag);
                }
            }, false, false);
        }
    }

    /**
     * 初始化中队控件
     *
     * @param wzContactListParseObj
     */
    private void initZDView(WZContactListParseObj wzContactListParseObj) {
        if (null != ll_zdListRoot) {
            ll_zdListRoot.removeAllViews();
            if (null != wzContactListParseObj && null != wzContactListParseObj.data && !wzContactListParseObj.data.isEmpty()) {
                List<ZDInfoObj> zdInfoObjs = wzContactListParseObj.data;
                for (int i = 0; i < zdInfoObjs.size(); i++) {
                    ZDInfoObj zdInfoObj = zdInfoObjs.get(i);
                    View zdInflate = View.inflate(getActivity(), R.layout.im_view_zd_item, null);
                    TextView tv_zdName = zdInflate.findViewById(R.id.tv_zdName);
                    TextView tv_zdWzNumber = zdInflate.findViewById(R.id.tv_zdWzNumber);
                    ImageView iv_zdOperateState = zdInflate.findViewById(R.id.iv_zdOperateState);
                    LinearLayout ll_wzViewRoot = zdInflate.findViewById(R.id.ll_wzViewRoot);
                    tv_zdName.setText(zdInfoObj.name);
                    tv_zdWzNumber.setText(null == zdInfoObj.wzList ? " (0)" : (String.format(Locale.CHINA, " (%d)", zdInfoObj.wzList.size())));
                    initZdWzEvent(zdInflate, iv_zdOperateState, ll_wzViewRoot, zdInfoObj);
                    ll_zdListRoot.addView(zdInflate);
                }
            }
        }
    }

    /**
     * 初始化中队微站显示隐藏逻辑
     *
     * @param zdInflate
     * @param iv_zdOperateState
     * @param ll_wzViewRoot
     * @param zdInfoObj
     */
    private void initZdWzEvent(View zdInflate, final ImageView iv_zdOperateState, final LinearLayout ll_wzViewRoot, final ZDInfoObj zdInfoObj) {
        zdInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_zdOperateState.setSelected(!iv_zdOperateState.isSelected());
                ll_wzViewRoot.setVisibility(iv_zdOperateState.isSelected() ? View.VISIBLE : View.GONE);
                initWZView(ll_wzViewRoot, zdInfoObj.wzList);
            }
        });
    }

    /**
     * 初始化微站数据列表
     *
     * @param ll_wzViewRoot
     * @param wzList
     */
    private void initWZView(LinearLayout ll_wzViewRoot, List<WZInfoObj> wzList) {
        ll_wzViewRoot.removeAllViews();
        if (null != wzList && !wzList.isEmpty()) {
            for (int i = 0; i < wzList.size(); i++) {
                WZInfoObj wzInfoObj = wzList.get(i);
                View wzInflate = View.inflate(getActivity(), R.layout.im_view_wz_item, null);
                TextView tv_wzName = wzInflate.findViewById(R.id.tv_wzName);
                TextView tv_wzDes = wzInflate.findViewById(R.id.tv_wzDes);
                TextView tv_wzUserNumber = wzInflate.findViewById(R.id.tv_wzUserNumber);
                LinearLayout ll_wzChildViewRoot = wzInflate.findViewById(R.id.ll_wzChildViewRoot);
                ImageView iv_wzOperateState = wzInflate.findViewById(R.id.iv_wzOperateState);
                tv_wzName.setText(wzInfoObj.xfzmc);
                tv_wzDes.setText(wzInfoObj.xfzdz);
                tv_wzUserNumber.setText(null == wzInfoObj.mfsList ? " (0)" : (String.format(Locale.CHINA, " (%d)", wzInfoObj.mfsList.size())));
                initWZContactEvent(wzInflate, iv_wzOperateState, ll_wzChildViewRoot, wzInfoObj);
                ll_wzViewRoot.addView(wzInflate);
            }
        }
    }

    /**
     * 初始化微站事件
     *
     * @param iv_wzOperateState
     * @param ll_wzChildViewRoot
     * @param wzInfoObj
     */
    private void initWZContactEvent(View wzInflate, final ImageView iv_wzOperateState,
                                    final LinearLayout ll_wzChildViewRoot, final WZInfoObj wzInfoObj) {
        wzInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_wzOperateState.setSelected(!iv_wzOperateState.isSelected());
                ll_wzChildViewRoot.setVisibility(iv_wzOperateState.isSelected() ? View.VISIBLE : View.GONE);
                initWZContactView(ll_wzChildViewRoot, wzInfoObj.mfsList);
            }
        });
    }

    /**
     * 初始化微站联系人
     *
     * @param ll_wzChildViewRoot
     * @param mfsList
     */
    private void initWZContactView(LinearLayout
                                           ll_wzChildViewRoot, List<WZInfoObj.MfsListObj> mfsList) {
        if (ll_wzChildViewRoot.getVisibility() == View.VISIBLE) {
            ll_wzChildViewRoot.removeAllViews();
            if (null != mfsList && !mfsList.isEmpty()) {
                for (int i = 0; i < mfsList.size(); i++) {
                    WZInfoObj.MfsListObj mfsListObj = mfsList.get(i);
                    View contactInflate = View.inflate(getActivity(), R.layout.im_view_contact_item, null);
                    TextView tv_userName = contactInflate.findViewById(R.id.tv_userName);
                    TextView tv_userDes = contactInflate.findViewById(R.id.tv_userDes);
                    TextView tv_onlineState = contactInflate.findViewById(R.id.tv_onlineState);
                    tv_userName.setText(mfsListObj.name);
                    tv_userDes.setText(mfsListObj.postduty);
                    if (TextUtils.equals(mfsListObj.online, "1")) {
                        tv_onlineState.setText("(在线)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.color_55D03C));
                    } else {
                        tv_onlineState.setText("(离线)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));

                    }
                    initContactItemEvent(mfsListObj, contactInflate);
                    ll_wzChildViewRoot.addView(contactInflate);
                }
            }
        }

    }

    /**
     * item 点击事件 直接跳转单聊
     *
     * @param mfsListObj
     * @param inflate
     */
    private void initContactItemEvent(final WZInfoObj.MfsListObj mfsListObj, View inflate) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IMUserInfoActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, mfsListObj.id);
                intent.putExtra(IMKeys.INTENT_TAG, 1);
                startActivity(intent);
            }
        });
    }
}
