package com.telewave.battlecommand.imui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.activity.IMForwardingSelectActivity;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.parse.ContactListParseObj;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.Collections;


/**
 * 转发联系人列表
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMForwardingSelectContactFragment extends Fragment {
    private View mView;
    private LinearLayout ll_contactSelectItemRoot;

    private ArrayList<ContactInfoObj> mContact = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.im_fragment_im_forwarding_select_contact, container, false);
        ll_contactSelectItemRoot = mView.findViewById(R.id.ll_contactSelectItemRoot);
        initData();
        return mView;
    }


    public void initData() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.addressAddressList, RequestMethod.POST);
        request.add("appType", "2");
        request.add("organId", ConstData.ORGANID);
        NoHttpManager.getRequestInstance().add(getActivity(), 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                if (!TextUtils.isEmpty(result)) {
                    ContactListParseObj contactListParseObj = IMJsonUtil.parseJsonToBean(result, ContactListParseObj.class);

                    if (null != contactListParseObj) {
                        if (null != contactListParseObj.data) {
                            mContact.clear();
                            mContact.addAll(contactListParseObj.data);
                        }
                        if (null != contactListParseObj.data1) {
                            for (ContactInfoObj contactInfoObj : contactListParseObj.data1) {
                                boolean isAdd = true;
                                for (ContactInfoObj innerContactInfoObj : mContact) {
                                    if (TextUtils.equals(innerContactInfoObj.userid, contactInfoObj.userid)) {
                                        isAdd = false;
                                        break;
                                    }
                                }
                                if (isAdd) {
                                    mContact.add(contactInfoObj);
                                }
                            }
                        }
                    }
                }
                Collections.sort(mContact);
                initContactSelectItemView();

            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {

            }
        }, false, false);

    }

    /**
     * 初始化人员控件数据
     */
    private void initContactSelectItemView() {
        ll_contactSelectItemRoot.removeAllViews();

        if (null != mContact && !mContact.isEmpty()) {
            for (ContactInfoObj contact : mContact) {
                View inflate = View.inflate(getActivity(), R.layout.im_view_contact_item, null);
                inflate.findViewById(R.id.iv_selectContactNext).setVisibility(View.INVISIBLE);
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
                ll_contactSelectItemRoot.addView(inflate);

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
                TUIKitDialog tuiKitDialog = new TUIKitDialog(getActivity())
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确认转发该消息给 '" + contact.name + "'？")
                        .setDialogWidth(0.8f);
                tuiKitDialog.getBtn_neg().setTextColor(getResources().getColor(R.color.top_bar));
                tuiKitDialog.getBtn_pos().setTextColor(getResources().getColor(R.color.top_bar));

                tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((IMForwardingSelectActivity) getActivity()).forwardingMessage(contact.userid, true);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();

            }
        });
    }
}
