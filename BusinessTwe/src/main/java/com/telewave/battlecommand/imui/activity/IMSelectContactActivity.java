package com.telewave.battlecommand.imui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.parse.ContactListParseObj;
import com.telewave.battlecommand.imui.service.IMGroupOperate;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


/**
 * 创建讨论组、选择人员
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMSelectContactActivity extends BaseActivity {
    /*是否为创建群组标记*/
    private boolean mIsCreateGroup = true;
    private String mCommitDes = "邀请";
    private LinearLayout ll_contactSelectItemRoot;
    private ArrayList<ContactInfoObj> mContactSelect = new ArrayList<>();
    private ArrayList<ContactInfoObj> mContact = new ArrayList<>();
    private TextView tv_commentOperate;
    /*已经存在的用户id*/
    private ArrayList<String> mUserIds;
    private String mGroupId;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_select_contact);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mUserIds = getIntent().getStringArrayListExtra(IMKeys.INTENT_IDS);
        mGroupId = getIntent().getStringExtra(IMKeys.INTENT_ID);
        mIsCreateGroup = getIntent().getBooleanExtra(IMKeys.INTENT_TAG, true);
        ll_contactSelectItemRoot = findViewById(R.id.ll_contactSelectItemRoot);
        tv_commentOperate = findViewById(R.id.tv_commentOperate);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(mIsCreateGroup ? "+讨论组" : "选择人员");
        mCommitDes = mIsCreateGroup ? "创建" : "邀请";

        if (null == mUserIds) {
            mUserIds = new ArrayList<>();
        }
        mUserIds.add(ConstData.userid);

    }

    private void initEvent() {
        findViewById(R.id.ll_selectContactReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_commentOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsCreateGroup) {
                    IMGroupOperate.getInstance().createGroup(IMSelectContactActivity.this, mContactSelect);
                } else {
                    IMGroupOperate.getInstance().inviteMemberToGroup(IMSelectContactActivity.this, mGroupId, mContactSelect);
                }
            }
        });
    }


    private void initData() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.addressAddressList, RequestMethod.POST);
        request.add("appType", "2");
        request.add("organId", ConstData.ORGANID);
        NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
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


    private void initContactSelectItemView() {


        if (null != mUserIds && !mUserIds.isEmpty()) {
            /*过滤掉不可选择的用户*/
            for (String userIded : mUserIds) {
                for (int i = 0; i < mContact.size(); i++) {
                    if (TextUtils.equals(mContact.get(i).userid, userIded)) {
                        mContact.remove(i);
                        break;
                    }
                }
            }
        }
        ll_contactSelectItemRoot.removeAllViews();
        if (null != mContact && !mContact.isEmpty()) {
            for (ContactInfoObj contact : mContact) {
                View inflate = View.inflate(this, R.layout.im_view_contact_select_item, null);
                TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                TextView tv_userDes = inflate.findViewById(R.id.tv_userDes);
                ImageView iv_selectState = inflate.findViewById(R.id.iv_selectState);
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
                iv_selectState.setSelected(false);
                initContactItemEvent(contact, inflate, iv_selectState);
                ll_contactSelectItemRoot.addView(inflate);
            }
        }
    }


    /**
     * item 点击事件
     *
     * @param contact
     * @param inflate
     */
    private void initContactItemEvent(final ContactInfoObj contact, View inflate, final ImageView iv_selectState) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_selectState.setSelected(!iv_selectState.isSelected());
                if (iv_selectState.isSelected()) {
                    mContactSelect.add(contact);
                } else {
                    mContactSelect.remove(contact);
                }
                tv_commentOperate.setText(null == mContactSelect || mContactSelect.isEmpty() ? mCommitDes : String.format(Locale.CHINA, "%s(已选%d人)", mCommitDes, mContactSelect.size()));
                tv_commentOperate.setVisibility(null == mContactSelect || mContactSelect.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
    }

}
