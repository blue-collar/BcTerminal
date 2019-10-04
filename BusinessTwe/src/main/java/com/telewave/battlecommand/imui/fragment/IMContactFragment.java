package com.telewave.battlecommand.imui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.adapter.IMContactAdapter;
import com.telewave.battlecommand.imui.bean.other.ContactOutObj;
import com.telewave.battlecommand.imui.bean.parse.ContactListParseObj;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yuan.refreshlayout.NormalRefreshViewHolder;
import com.yuan.refreshlayout.RefreshLayout;
import com.yuan.refreshlayout.RefreshViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 联系人
 *
 * @author PF-NAN
 * @date 2019-08-05
 */
public class IMContactFragment extends Fragment {
    private View mView;
    private RefreshLayout rl_refreshContact;
    private RecyclerView rc_contact;
    private IMContactAdapter mIMContactAdapter;
    private CheckBox cb_selectState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.im_fragment_im_contact, container, false);
        rl_refreshContact = mView.findViewById(R.id.rl_refreshContact);
        rc_contact = mView.findViewById(R.id.rc_contact);
        cb_selectState = mView.findViewById(R.id.cb_selectState);

        initEvent();
        return mView;
    }

    private void initEvent() {
        rl_refreshContact.shouldHandleRecyclerViewLoadingMore(rc_contact);
        rl_refreshContact.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                initData();
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                rl_refreshContact.endRefreshing();
                rl_refreshContact.endLoadingMore();
                return false;
            }
        });
        RefreshViewHolder holder = new NormalRefreshViewHolder(getActivity(), true);
        rl_refreshContact.setRefreshViewHolder(holder);
        rc_contact.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIMContactAdapter = new IMContactAdapter(this, mContactOutObjs);
        rc_contact.setAdapter(mIMContactAdapter);
        cb_selectState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (null != mIMContactAdapter) {
                    mIMContactAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private List<ContactOutObj> mContactOutObjs = new ArrayList<>();

    /**
     * 初始化数据
     */
    public void initData() {

        if (this.isVisible()) {
            Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.addressAddressList, RequestMethod.POST);
            request.add("appType", "2");
            request.add("organId", ConstData.ORGANID);
            NoHttpManager.getRequestInstance().add(getActivity(), 1001, request, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    String result = response.get();
                    if (!TextUtils.isEmpty(result)) {
                        ContactListParseObj contactListParseObj = IMJsonUtil.parseJsonToBean(result, ContactListParseObj.class);
                        if (null != contactListParseObj && null != mContactOutObjs && null != mIMContactAdapter) {
                            mContactOutObjs.clear();
                            ContactOutObj onlineContactOutObj = new ContactOutObj("在线人员", contactListParseObj.data);
                            mContactOutObjs.add(onlineContactOutObj);
                            ContactOutObj orgContactOutObj = new ContactOutObj("本单位人员", contactListParseObj.data1);
                            mContactOutObjs.add(orgContactOutObj);
                            mIMContactAdapter.notifyDataSetChanged();
                        }
                    }
                    if (null != rl_refreshContact) {
                        rl_refreshContact.endRefreshing();
                        rl_refreshContact.endLoadingMore();
                    }
                }

                @Override
                public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                    if (null != rl_refreshContact) {
                        rl_refreshContact.endRefreshing();
                        rl_refreshContact.endLoadingMore();
                    }
                }
            }, false, false);

        }
    }

    /**
     * 获取选择状态
     *
     * @return
     */
    public boolean isShowUnOnline() {
        if (null != cb_selectState) {
            return cb_selectState.isChecked();
        } else {
            return false;
        }
    }
}
