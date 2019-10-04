package com.telewave.battlecommand.imui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.activity.IMUserInfoActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.parse.ContactListParseObj;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * 联系人列表
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMContactListFragment extends Fragment {
    private View mView;
    private LinearLayout ll_onlineContactItemRoot;
    private List<ContactInfoObj> onlineContacts;
    private List<ContactInfoObj> orgContacts;
    private LinearLayout ll_orgContactItemRoot;
    private ImageView iv_onlineNext;
    private ImageView iv_orgUserList;
    private TextView tv_onlineUserState;
    private TextView tv_orgUserState;
    private CheckBox cb_selectState;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.im_fragment_im_contactlist, container, false);
        initView();
        initEvent();
        return mView;
    }

    private void initEvent() {
        /*在线人员状态*/
        mView.findViewById(R.id.ll_onlineUserList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_onlineContactItemRoot.getVisibility() == View.VISIBLE) {
                    ll_onlineContactItemRoot.setVisibility(View.GONE);
                    iv_onlineNext.setSelected(false);

                } else {
                    ll_onlineContactItemRoot.setVisibility(View.VISIBLE);
                    iv_onlineNext.setSelected(true);
                    initContactView(0, onlineContacts);
                }
            }
        });
        /*本单位人员状态*/
        mView.findViewById(R.id.ll_orgUserList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_orgContactItemRoot.getVisibility() == View.VISIBLE) {
                    ll_orgContactItemRoot.setVisibility(View.GONE);
                    iv_orgUserList.setSelected(false);
                } else {
                    ll_orgContactItemRoot.setVisibility(View.VISIBLE);
                    initContactView(1, orgContacts);
                    iv_orgUserList.setSelected(true);
                }
            }
        });
        cb_selectState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                initContactView(0, onlineContacts);
            }
        });
    }

    private void initView() {
        ll_onlineContactItemRoot = mView.findViewById(R.id.ll_onlineContactItemRoot);
        ll_orgContactItemRoot = mView.findViewById(R.id.ll_orgContactItemRoot);
        iv_onlineNext = mView.findViewById(R.id.iv_onlineNext);
        iv_orgUserList = mView.findViewById(R.id.iv_orgUserList);
        tv_onlineUserState = mView.findViewById(R.id.tv_onlineUserState);
        tv_orgUserState = mView.findViewById(R.id.tv_orgUserState);
        cb_selectState = mView.findViewById(R.id.cb_selectState);
    }


    public void initData() {
        onlineContacts = new ArrayList<>();
        orgContacts = new ArrayList<>();
        requestData();
    }

    private void requestData() {
        if (this.isVisible()) {
            Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.addressAddressList, RequestMethod.POST);
            request.add("appType", "2");
            request.add("organId", ConstData.ORGANID);
            NoHttpManager.getRequestInstance().add(getActivity(), 1001, request, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    String result = response.get();
                    TUIKitLog.e("======" + result);
                    if (!TextUtils.isEmpty(result)) {
                        ContactListParseObj contactListParseObj = IMJsonUtil.parseJsonToBean(result, ContactListParseObj.class);
                        if (null != contactListParseObj) {
                            if (null != contactListParseObj.data) {
                                onlineContacts.clear();
                                onlineContacts.addAll(contactListParseObj.data);
                                int onlineNum = 0;
                                for (ContactInfoObj orgContact : onlineContacts) {
                                    if (TextUtils.equals(orgContact.online, "1")) {
                                        onlineNum += 1;
                                    }
                                }
                                Collections.sort(onlineContacts);
                                tv_onlineUserState.setText(String.format(Locale.CHINA, "%d/%d", onlineNum, onlineContacts.size()));
                                initContactView(0, onlineContacts);
                            }
                            if (null != contactListParseObj.data1) {
                                orgContacts.clear();
                                orgContacts.addAll(contactListParseObj.data1);

                                int onlineNum = 0;
                                for (ContactInfoObj orgContact : orgContacts) {
                                    if (TextUtils.equals(orgContact.online, "1")) {
                                        onlineNum += 1;
                                    }
                                }
                                Collections.sort(orgContacts);
                                tv_orgUserState.setText(String.format(Locale.CHINA, "%d/%d", onlineNum, orgContacts.size()));
                                initContactView(1, orgContacts);

                            }
                        }
                    }
                }

                @Override
                public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                    TUIKitLog.e(url + "=====onFailed=" + tag);
                }
            }, false, false);
        }
    }

    /**
     * 初始化人员控件数据
     *
     * @param tag
     * @param contacts
     */
    private void initContactView(@IntRange(from = 0, to = 1) int tag, List<ContactInfoObj> contacts) {
        if (tag == 0) {
            ll_onlineContactItemRoot.removeAllViews();
        } else {
            ll_orgContactItemRoot.removeAllViews();
        }
        if (null != contacts && !contacts.isEmpty()) {
            for (ContactInfoObj contact : contacts) {
                View inflate = View.inflate(getActivity(), R.layout.im_view_contact_item, null);
                TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                TextView tv_userDes = inflate.findViewById(R.id.tv_userDes);
                TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                tv_userName.setText(contact.name);
                tv_userDes.setText(contact.organName);
                if (TextUtils.equals(contact.online, "1")) {
                    tv_onlineState.setText("(在线)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.color_55D03C));
                } else {
                    tv_onlineState.setText("(离线)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));

                }
                initContactItemEvent(contact, inflate);
                if (tag == 0) {
                    if (cb_selectState.isChecked()) {
                        ll_onlineContactItemRoot.addView(inflate);
                    } else if (TextUtils.equals(contact.online, "1")) {
                        ll_onlineContactItemRoot.addView(inflate);
                    }
                } else {
                    ll_orgContactItemRoot.addView(inflate);
                }
            }
        }

    }

    /**
     * item 点击事件 直接跳转单聊
     *
     * @param contact
     * @param inflate
     */
    private void initContactItemEvent(final ContactInfoObj contact, View inflate) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IMUserInfoActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, contact.userid);
                startActivity(intent);
            }
        });
    }
}
