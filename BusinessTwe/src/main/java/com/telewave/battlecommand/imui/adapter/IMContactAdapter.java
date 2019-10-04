package com.telewave.battlecommand.imui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.imui.activity.IMUserInfoActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.other.ContactOutObj;
import com.telewave.battlecommand.imui.fragment.IMContactFragment;
import com.telewave.business.twe.R;

import java.util.List;
import java.util.Locale;

import static com.baidu.navisdk.util.jar.JarUtils.getResources;


/**
 * 联系人适配器
 *
 * @author PF-NAN
 * @date 2019-08-05
 */
public class IMContactAdapter extends RecyclerView.Adapter<IMContactAdapter.IMContactHolder> {
    private IMContactFragment mIMContactFragment;
    private List<ContactOutObj> mContactOutObjs;

    public IMContactAdapter(IMContactFragment imContactFragment, List<ContactOutObj> contactOutObjs) {
        this.mIMContactFragment = imContactFragment;
        this.mContactOutObjs = contactOutObjs;
    }


    @NonNull
    @Override
    public IMContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(mIMContactFragment.getActivity(), R.layout.im_adapter_im_contact_out, null);
        return new IMContactHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IMContactHolder holder, int position) {
        ContactOutObj contactOutObj = mContactOutObjs.get(position);
        holder.tv_outName.setText(contactOutObj.name);
        holder.tv_outState.setText(String.format(Locale.CHINA, "%d/%d", contactOutObj.onlineNumber, contactOutObj.totalNumber));
        initEvent(contactOutObj, holder.ll_outRoot, holder.ll_outContactRoot, holder.iv_outState);
        initChildView(contactOutObj, holder.ll_outContactRoot);
    }

    /**
     * @param contactOutObj
     * @param ll_outRoot
     * @param ll_outContactRoot
     * @param iv_outState
     */
    private void initEvent(final ContactOutObj contactOutObj, LinearLayout ll_outRoot, final LinearLayout ll_outContactRoot, final ImageView iv_outState) {
        ll_outRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ll_outContactRoot.getVisibility() == View.VISIBLE) {
                    ll_outContactRoot.setVisibility(View.GONE);
                    iv_outState.setSelected(false);
                } else {
                    ll_outContactRoot.setVisibility(View.VISIBLE);
                    iv_outState.setSelected(true);
                    initChildView(contactOutObj, ll_outContactRoot);
                }
            }
        });
    }

    /**
     * 初始化自控件
     *
     * @param contactOutObj
     * @param ll_outContactRoot
     */
    private void initChildView(ContactOutObj contactOutObj, LinearLayout ll_outContactRoot) {
        ll_outContactRoot.removeAllViews();
        if (null != contactOutObj && null != contactOutObj.contactInfoObjs && !contactOutObj.contactInfoObjs.isEmpty()) {
            List<ContactInfoObj> contactInfoObjs = contactOutObj.contactInfoObjs;
            for (int i = 0; i < contactInfoObjs.size(); i++) {
                ContactInfoObj contactInfoObj = contactInfoObjs.get(i);
                View inflate = View.inflate(mIMContactFragment.getActivity(), R.layout.im_view_contact_item, null);
                TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                TextView tv_userDes = inflate.findViewById(R.id.tv_userDes);
                TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                View v_userLine1 = inflate.findViewById(R.id.v_userLine1);
                View v_userLine2 = inflate.findViewById(R.id.v_userLine2);
                v_userLine2.setVisibility(View.GONE);
                v_userLine1.setVisibility(View.VISIBLE);
                tv_userName.setText(contactInfoObj.name);
                tv_userDes.setText(contactInfoObj.organName);
                if (TextUtils.equals(contactInfoObj.online, "1")) {
                    tv_onlineState.setText("(在线)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.color_55D03C));
                } else {
                    tv_onlineState.setText("(离线)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));

                }
                initContactItemEvent(contactInfoObj, inflate);
                if (contactOutObj.userType == 0) {
                    if (mIMContactFragment.isShowUnOnline()) {
                        ll_outContactRoot.addView(inflate);
                    } else if (TextUtils.equals(contactInfoObj.online, "1")) {
                        ll_outContactRoot.addView(inflate);
                    }
                } else {
                    ll_outContactRoot.addView(inflate);
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
                Intent intent = new Intent(mIMContactFragment.getActivity(), IMUserInfoActivity.class);
                intent.putExtra(IMKeys.INTENT_ID, contact.userid);
                mIMContactFragment.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactOutObjs.size();
    }

    public class IMContactHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_outRoot;
        private TextView tv_outName;
        private TextView tv_outState;
        private ImageView iv_outState;
        private LinearLayout ll_outContactRoot;

        public IMContactHolder(View itemView) {
            super(itemView);
            ll_outRoot = itemView.findViewById(R.id.ll_outRoot);
            tv_outName = itemView.findViewById(R.id.tv_outName);
            tv_outState = itemView.findViewById(R.id.tv_outState);
            iv_outState = itemView.findViewById(R.id.iv_outState);
            ll_outContactRoot = itemView.findViewById(R.id.ll_outContactRoot);
        }
    }
}
