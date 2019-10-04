package com.telewave.battlecommand.directboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telewave.battlecommand.directboard.activity.AudienceLiveBoardActivity;
import com.telewave.battlecommand.directboard.bean.AllLiveBean;
import com.telewave.business.twe.R;

import java.util.List;
import java.util.Locale;

import static com.baidu.navisdk.util.jar.JarUtils.getResources;


/**
 * 总队所有
 * 直播
 * 适配器
 *
 * @author liwh
 * @date 2019-08-09
 */
public class AllLiveBoardAdapter extends RecyclerView.Adapter<AllLiveBoardAdapter.LiveBoardMemberHolder> {
    private Context context;
    private List<AllLiveBean> mLiveBoardOutObjs;

    public AllLiveBoardAdapter(Context context, List<AllLiveBean> mLiveBoardOutObjs) {
        this.context = context;
        this.mLiveBoardOutObjs = mLiveBoardOutObjs;
    }


    @NonNull
    @Override
    public LiveBoardMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.live_board_adapter_zongdui_organ, null);
        return new LiveBoardMemberHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveBoardMemberHolder holder, int position) {
        AllLiveBean allLiveBean = mLiveBoardOutObjs.get(position);
        holder.tv_outName.setText(allLiveBean.getName());
        holder.tv_outState.setText(String.format(Locale.CHINA, "%d/%d", allLiveBean.getOpenNum(), allLiveBean.getAllNum()));
        initZongDuiChildEvent(allLiveBean, holder.ll_outRoot, holder.ll_outContactRoot, holder.iv_outState);
    }

    /**
     * @param allLiveBean
     * @param ll_outRoot
     * @param ll_outContactRoot
     * @param iv_outState
     */
    private void initZongDuiChildEvent(final AllLiveBean allLiveBean, LinearLayout ll_outRoot, final LinearLayout ll_outContactRoot, final ImageView iv_outState) {
        ll_outRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_outContactRoot.getVisibility() == View.VISIBLE) {
                    ll_outContactRoot.setVisibility(View.GONE);
                    iv_outState.setSelected(false);
                } else {
                    ll_outContactRoot.setVisibility(View.VISIBLE);
                    iv_outState.setSelected(true);
                    initZongDuiChildView(allLiveBean, ll_outContactRoot);
                }
            }
        });
    }

    /**
     * @param liveingBean
     * @param ll_outRoot
     * @param ll_outContactRoot
     * @param iv_outState
     */
    private void initZhiDuiChildEvent(final AllLiveBean.ListBeanXXX liveingBean, LinearLayout ll_outRoot, final LinearLayout ll_outContactRoot, final ImageView iv_outState) {
        ll_outRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_outContactRoot.getVisibility() == View.VISIBLE) {
                    ll_outContactRoot.setVisibility(View.GONE);
                    iv_outState.setSelected(false);
                } else {
                    ll_outContactRoot.setVisibility(View.VISIBLE);
                    iv_outState.setSelected(true);
                    initZhiDuiChildView(liveingBean, ll_outContactRoot);
                }
            }
        });
    }

    /**
     * @param liveingBean
     * @param ll_outRoot
     * @param ll_outContactRoot
     * @param iv_outState
     */
    private void initDaDuiChildEvent(final AllLiveBean.ListBeanXXX.ListBeanXX liveingBean, LinearLayout ll_outRoot, final LinearLayout ll_outContactRoot, final ImageView iv_outState) {
        ll_outRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_outContactRoot.getVisibility() == View.VISIBLE) {
                    ll_outContactRoot.setVisibility(View.GONE);
                    iv_outState.setSelected(false);
                } else {
                    ll_outContactRoot.setVisibility(View.VISIBLE);
                    iv_outState.setSelected(true);
                    initDaDuiChildView(liveingBean, ll_outContactRoot);
                }
            }
        });
    }

    /**
     * @param liveingBean
     * @param ll_outRoot
     * @param ll_outContactRoot
     * @param iv_outState
     */
    private void initLastEvent(final AllLiveBean.ListBeanXXX.ListBeanXX.ListBeanX liveingBean, LinearLayout ll_outRoot, final LinearLayout ll_outContactRoot, final ImageView iv_outState) {
        ll_outRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_outContactRoot.getVisibility() == View.VISIBLE) {
                    ll_outContactRoot.setVisibility(View.GONE);
                    iv_outState.setSelected(false);
                } else {
                    ll_outContactRoot.setVisibility(View.VISIBLE);
                    iv_outState.setSelected(true);
                    initLastView(liveingBean, ll_outContactRoot);
                }
            }
        });
    }

    /**
     * 初始化总队孩子自控件
     *
     * @param allLiveBean
     * @param ll_outContactRoot
     */
    private void initZongDuiChildView(AllLiveBean allLiveBean, LinearLayout ll_outContactRoot) {
        ll_outContactRoot.removeAllViews();
        if (null != allLiveBean && null != allLiveBean.getList() && !allLiveBean.getList().isEmpty()) {
            List<AllLiveBean.ListBeanXXX> liveingBeans = allLiveBean.getList();
            for (int i = 0; i < liveingBeans.size(); i++) {
                AllLiveBean.ListBeanXXX liveingBean = liveingBeans.get(i);
                //支队下不可能直接有中队，所以只判断有没有人就可以
                if ("99".equals(liveingBean.getCategory())) {
                    View inflate = View.inflate(context, R.layout.live_board_view_organ_item, null);
                    TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                    TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                    tv_userName.setText(liveingBean.getName());
                    if (TextUtils.equals(liveingBean.getIsOpenLive(), "1")) {
                        tv_onlineState.setText("(直播中)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.top_bar));
                        initLiveItemEvent(liveingBean.getRoomNumber(), liveingBean.getId(),
                                liveingBean.getName(), inflate);
                    } else {
                        tv_onlineState.setText("(未直播)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
                    }
                    ll_outContactRoot.addView(inflate);
                } else {
                    View inflate = View.inflate(context, R.layout.live_board_adapter_zhidui_organ, null);
                    LinearLayout ll_outRoot = inflate.findViewById(R.id.ll_outRoot);
                    TextView tv_outName = inflate.findViewById(R.id.tv_outName);
                    TextView tv_outState = inflate.findViewById(R.id.tv_outState);
                    ImageView iv_outState = inflate.findViewById(R.id.iv_outState);
                    LinearLayout ll_outContactRoot1 = inflate.findViewById(R.id.ll_outContactRoot);

                    tv_outName.setText(liveingBean.getName());
                    tv_outState.setText(String.format(Locale.CHINA, "%d/%d", liveingBean.getOpenNum(), liveingBean.getAllNum()));
                    ll_outContactRoot.addView(inflate);
                    initZhiDuiChildEvent(liveingBean, ll_outRoot, ll_outContactRoot1, iv_outState);
                }
            }
        }
    }

    /**
     * 初始化支队孩子自控件
     *
     * @param liveingBean
     * @param ll_outContactRoot
     */
    private void initZhiDuiChildView(AllLiveBean.ListBeanXXX liveingBean, LinearLayout ll_outContactRoot) {
        ll_outContactRoot.removeAllViews();
        if (null != liveingBean && null != liveingBean.getList() && !liveingBean.getList().isEmpty()) {
            List<AllLiveBean.ListBeanXXX.ListBeanXX> liveingBeans = liveingBean.getList();
            for (int i = 0; i < liveingBeans.size(); i++) {
                AllLiveBean.ListBeanXXX.ListBeanXX liveingBean1 = liveingBeans.get(i);
                if ("99".equals(liveingBean1.getCategory())) {
                    View inflate = View.inflate(context, R.layout.live_board_view_organ_item, null);
                    TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                    TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                    tv_userName.setText(liveingBean1.getName());
                    if (TextUtils.equals(liveingBean1.getIsOpenLive(), "1")) {
                        tv_onlineState.setText("(直播中)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.top_bar));
                        initLiveItemEvent(liveingBean1.getRoomNumber(), liveingBean1.getId(),
                                liveingBean1.getName(), inflate);
                    } else {
                        tv_onlineState.setText("(未直播)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
                    }
                    ll_outContactRoot.addView(inflate);
                } else {
                    View inflate = View.inflate(context, R.layout.live_board_adapter_dadui_organ, null);

                    LinearLayout ll_outRoot = inflate.findViewById(R.id.ll_outRoot);
                    TextView tv_outName = inflate.findViewById(R.id.tv_outName);
                    TextView tv_outState = inflate.findViewById(R.id.tv_outState);
                    ImageView iv_outState = inflate.findViewById(R.id.iv_outState);
                    LinearLayout ll_outContactRoot1 = inflate.findViewById(R.id.ll_outContactRoot);

                    tv_outName.setText(liveingBean1.getName());
                    tv_outState.setText(String.format(Locale.CHINA, "%d/%d", liveingBean1.getOpenNum(), liveingBean1.getAllNum()));
                    ll_outContactRoot.addView(inflate);
                    initDaDuiChildEvent(liveingBean1, ll_outRoot, ll_outContactRoot1, iv_outState);
                }
            }
        }
    }

    /**
     * 初始化大队孩子自控件
     *
     * @param liveingBean
     * @param ll_outContactRoot
     */
    private void initDaDuiChildView(AllLiveBean.ListBeanXXX.ListBeanXX liveingBean, LinearLayout ll_outContactRoot) {
        ll_outContactRoot.removeAllViews();
        if (null != liveingBean && null != liveingBean.getList() && !liveingBean.getList().isEmpty()) {
            List<AllLiveBean.ListBeanXXX.ListBeanXX.ListBeanX> liveingBeans = liveingBean.getList();
            for (int i = 0; i < liveingBeans.size(); i++) {
                AllLiveBean.ListBeanXXX.ListBeanXX.ListBeanX liveingBean2 = liveingBeans.get(i);
                if ("99".equals(liveingBean2.getCategory())) {
                    View inflate = View.inflate(context, R.layout.live_board_view_organ_item, null);
                    TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                    TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                    tv_userName.setText(liveingBean2.getName());
                    if (TextUtils.equals(liveingBean2.getIsOpenLive(), "1")) {
                        tv_onlineState.setText("(直播中)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.top_bar));
                        initLiveItemEvent(liveingBean2.getRoomNumber(), liveingBean2.getId(),
                                liveingBean2.getName(), inflate);
                    } else {
                        tv_onlineState.setText("(未直播)");
                        tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
                    }
                    ll_outContactRoot.addView(inflate);
                } else {
                    View inflate = View.inflate(context, R.layout.live_board_adapter_zhongdui_organ, null);

                    LinearLayout ll_outRoot = inflate.findViewById(R.id.ll_outRoot);
                    TextView tv_outName = inflate.findViewById(R.id.tv_outName);
                    TextView tv_outState = inflate.findViewById(R.id.tv_outState);
                    ImageView iv_outState = inflate.findViewById(R.id.iv_outState);
                    LinearLayout ll_outContactRoot1 = inflate.findViewById(R.id.ll_outContactRoot);

                    tv_outName.setText(liveingBean2.getName());
                    tv_outState.setText(String.format(Locale.CHINA, "%d/%d", liveingBean2.getOpenNum(), liveingBean2.getAllNum()));
                    ll_outContactRoot.addView(inflate);
                    initLastEvent(liveingBean2, ll_outRoot, ll_outContactRoot1, iv_outState);
                }
            }
        }
    }

    /**
     * 初始化自控件
     *
     * @param liveingBean
     * @param ll_outContactRoot
     */
    private void initLastView(AllLiveBean.ListBeanXXX.ListBeanXX.ListBeanX liveingBean, LinearLayout ll_outContactRoot) {
        ll_outContactRoot.removeAllViews();
        if (null != liveingBean && null != liveingBean.getList() && !liveingBean.getList().isEmpty()) {
            List<AllLiveBean.ListBeanXXX.ListBeanXX.ListBeanX.ListBean> liveingBeans = liveingBean.getList();
            for (int i = 0; i < liveingBeans.size(); i++) {
                AllLiveBean.ListBeanXXX.ListBeanXX.ListBeanX.ListBean liveingBean3 = liveingBeans.get(i);
                View inflate = View.inflate(context, R.layout.live_board_view_organ_item, null);
                TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                tv_userName.setText(liveingBean3.getName());
                if (TextUtils.equals(liveingBean3.getIsOpenLive(), "1")) {
                    tv_onlineState.setText("(直播中)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.top_bar));
                    initLiveItemEvent(liveingBean3.getRoomNumber(), liveingBean3.getId(),
                            liveingBean3.getName(), inflate);
                } else {
                    tv_onlineState.setText("(未直播)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                ll_outContactRoot.addView(inflate);
            }
        }
    }


    /**
     * item 点击事件 直接跳转观众席进入直播
     *
     * @param roomNumber
     * @param liveUserId
     * @param liveUserName
     * @param inflate
     */
    private void initLiveItemEvent(final String roomNumber, final String liveUserId, final String liveUserName, View inflate) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int roomId = Integer.parseInt(roomNumber);
                Intent intent = new Intent(context, AudienceLiveBoardActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("liveUserId", liveUserId);
                intent.putExtra("liveUserName", liveUserName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLiveBoardOutObjs.size();
    }

    public class LiveBoardMemberHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_outRoot;
        private TextView tv_outName;
        private TextView tv_outState;
        private ImageView iv_outState;
        private LinearLayout ll_outContactRoot;

        public LiveBoardMemberHolder(View itemView) {
            super(itemView);
            ll_outRoot = itemView.findViewById(R.id.ll_outRoot);
            tv_outName = itemView.findViewById(R.id.tv_outName);
            tv_outState = itemView.findViewById(R.id.tv_outState);
            iv_outState = itemView.findViewById(R.id.iv_outState);
            ll_outContactRoot = itemView.findViewById(R.id.ll_outContactRoot);
        }
    }
}
