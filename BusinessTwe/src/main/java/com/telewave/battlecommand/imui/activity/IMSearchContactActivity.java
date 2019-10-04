package com.telewave.battlecommand.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
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


/**
 * 搜索联系人
 *
 * @author PF-NAN
 * @date 2019-07-21
 */
public class IMSearchContactActivity extends BaseActivity {

    private EditText et_search;
    private LinearLayout ll_contactItemRoot;

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_im_search_contact);
        initView();
        initEvent();
    }


    private void initView() {
        et_search = findViewById(R.id.et_search);
        ll_contactItemRoot = findViewById(R.id.ll_contactItemRoot);
    }

    private void initEvent() {
        findViewById(R.id.ll_searchContactReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                finish();
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(et_search.getText().toString().trim())) {
                        searchContact();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 处理搜索逻辑
     */
    private void searchContact() {
        closeKeyboard();
        ll_contactItemRoot.removeAllViews();
        String searchContent = et_search.getText().toString().trim();
        if (!TextUtils.isEmpty(searchContent)) {
            TUIKitLog.e("--===这里搜索联系人列表--=account=" + searchContent + "==organId==" + ConstData.ORGANID + "===url===" + ConstData.urlManager.addressAddressList);
            // 创建请求对象
            Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.addressAddressList, RequestMethod.POST);
            // 添加请求参数
            request.add("account", searchContent);
            request.add("appType", "2");
            request.add("organId", ConstData.ORGANID);
//
            NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    // 响应结果
                    String result = response.get();
                    TUIKitLog.e("--===onSucceed--==" + result);
                    if (!TextUtils.isEmpty(result)) {
                        ContactListParseObj contactListParseObj = IMJsonUtil.parseJsonToBean(result, ContactListParseObj.class);
                        if (null != contactListParseObj) {
                            mSearchContactInfoObjs.clear();
                            if (null != contactListParseObj.data && !contactListParseObj.data.isEmpty()) {
                                for (ContactInfoObj contactInfoObj : contactListParseObj.data) {
                                    boolean isHas = false;
                                    for (int i = 0; i < mSearchContactInfoObjs.size(); i++) {
                                        if (TextUtils.equals(contactInfoObj.userid, mSearchContactInfoObjs.get(i).userid)) {
                                            isHas = true;
                                            break;
                                        }
                                    }
                                    if (!isHas) {
                                        mSearchContactInfoObjs.add(contactInfoObj);
                                    }
                                }
                            }
                            if (null != contactListParseObj.data1 && !contactListParseObj.data1.isEmpty()) {
                                for (ContactInfoObj contactInfoObj : contactListParseObj.data1) {
                                    boolean isHas = false;
                                    for (int i = 0; i < mSearchContactInfoObjs.size(); i++) {
                                        if (TextUtils.equals(contactInfoObj.userid, mSearchContactInfoObjs.get(i).userid)) {
                                            isHas = true;
                                            break;
                                        }
                                    }
                                    if (!isHas) {
                                        mSearchContactInfoObjs.add(contactInfoObj);
                                    }
                                }
                            }
                            Collections.sort(mSearchContactInfoObjs);
                            initSearchResutView(mSearchContactInfoObjs);
                        }
                    }
                }

                @Override
                public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                    TUIKitLog.e("--===onFailed--==" + tag);

                }
            }, false, true);
        }
    }

    /*需要展示的联系人*/
    private List<ContactInfoObj> mSearchContactInfoObjs = new ArrayList<>();

    /**
     * 初始化搜索到的联系人数据
     *
     * @param contactInfoObjs
     */
    private void initSearchResutView(List<ContactInfoObj> contactInfoObjs) {
        if (null != contactInfoObjs && !contactInfoObjs.isEmpty()) {
            for (ContactInfoObj contact : contactInfoObjs) {
                View inflate = View.inflate(this, R.layout.im_view_contact_item, null);
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

                ll_contactItemRoot.addView(inflate);
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
                /*跳转详情*/
                Intent intent = new Intent(IMSearchContactActivity.this, IMUserInfoActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, contact.userid);
                startActivity(intent);
            }
        });
    }

}
