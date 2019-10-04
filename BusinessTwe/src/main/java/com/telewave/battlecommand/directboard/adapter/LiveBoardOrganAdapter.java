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
import com.telewave.battlecommand.directboard.bean.LiveBoardBean;
import com.telewave.battlecommand.directboard.bean.LiveBoardOutObj;
import com.telewave.business.twe.R;

import java.util.List;
import java.util.Locale;

import static com.baidu.navisdk.util.jar.JarUtils.getResources;


/**
 * 本单位直播
 * 适配器
 *
 * @author liwh
 * @date 2019-08-09
 */
public class LiveBoardOrganAdapter extends RecyclerView.Adapter<LiveBoardOrganAdapter.LiveBoardMemberHolder> {
    private Context context;
    private List<LiveBoardOutObj> mLiveBoardOutObjs;

    public LiveBoardOrganAdapter(Context context, List<LiveBoardOutObj> mLiveBoardOutObjs) {
        this.context = context;
        this.mLiveBoardOutObjs = mLiveBoardOutObjs;
    }


    @NonNull
    @Override
    public LiveBoardMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.live_board_adapter_organ_out, null);
        return new LiveBoardMemberHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveBoardMemberHolder holder, int position) {
        LiveBoardOutObj liveBoardOutObj = mLiveBoardOutObjs.get(position);
        holder.tv_outName.setText(liveBoardOutObj.name);
        holder.tv_outState.setText(String.format(Locale.CHINA, "%d/%d", liveBoardOutObj.onLiveNumber, liveBoardOutObj.totalNumber));
        initEvent(liveBoardOutObj, holder.ll_outRoot, holder.ll_outContactRoot, holder.iv_outState);
        initChildView(liveBoardOutObj, holder.ll_outContactRoot);
    }

    /**
     * @param liveBoardOutObj
     * @param ll_outRoot
     * @param ll_outContactRoot
     * @param iv_outState
     */
    private void initEvent(final LiveBoardOutObj liveBoardOutObj, LinearLayout ll_outRoot, final LinearLayout ll_outContactRoot, final ImageView iv_outState) {
        ll_outRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_outContactRoot.getVisibility() == View.VISIBLE) {
                    ll_outContactRoot.setVisibility(View.GONE);
                    iv_outState.setSelected(false);
                } else {
                    ll_outContactRoot.setVisibility(View.VISIBLE);
                    iv_outState.setSelected(true);
                    initChildView(liveBoardOutObj, ll_outContactRoot);
                }
            }
        });
    }

    /**
     * 初始化自控件
     *
     * @param liveBoardOutObj
     * @param ll_outContactRoot
     */
    private void initChildView(LiveBoardOutObj liveBoardOutObj, LinearLayout ll_outContactRoot) {
        ll_outContactRoot.removeAllViews();
        if (null != liveBoardOutObj && null != liveBoardOutObj.liveBoardInfoObjs && !liveBoardOutObj.liveBoardInfoObjs.isEmpty()) {
            List<LiveBoardBean> liveingBeans = liveBoardOutObj.liveBoardInfoObjs;
            for (int i = 0; i < liveingBeans.size(); i++) {
                LiveBoardBean liveingBean = liveingBeans.get(i);
                View inflate = View.inflate(context, R.layout.live_board_view_organ_item, null);
                TextView tv_userName = inflate.findViewById(R.id.tv_userName);
                TextView tv_onlineState = inflate.findViewById(R.id.tv_onlineState);
                tv_userName.setText(liveingBean.getLiveUsername());
                if (TextUtils.equals(liveingBean.getIsOpenLive(), "1")) {
                    tv_onlineState.setText("(直播中)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.top_bar));
                    initContactItemEvent(liveingBean, inflate);
                } else {
                    tv_onlineState.setText("(未直播)");
                    tv_onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                ll_outContactRoot.addView(inflate);
            }
        }
    }


    /**
     * item 点击事件 直接跳转单聊
     *
     * @param liveingBean
     * @param inflate
     */
    private void initContactItemEvent(final LiveBoardBean liveingBean, View inflate) {
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int roomId = Integer.parseInt(liveingBean.getRoomNumber());
                String liveUserId = liveingBean.getLiveUserid();
                String liveUserName = liveingBean.getLiveUsername();
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
