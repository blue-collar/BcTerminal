package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.business.twe.R;

import java.util.List;

public class DisasterInfoRecyclerAdapter extends RecyclerView.Adapter<DisasterInfoRecyclerAdapter.MyViewHolder> {

    private List<DisasterInfo> mDatas;
    private Context context;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLocated(View view, int position);

        void onChatted(View view, int position);

        void onNavigated(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public DisasterInfoRecyclerAdapter(Context context, List<DisasterInfo> data) {
        this.context = context;
        this.mDatas = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<DisasterInfo> data) {
        this.mDatas = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_reyclerview_disaster_info, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        //view.setOnClickListener(this);
        //holder.imgEdit.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        DisasterInfo disasterInfo = mDatas.get(position);

        if (disasterInfo.getZqlx() != null && !disasterInfo.getZqlx().equals("") && disasterInfo.getZhdj() != null && !disasterInfo.getZhdj().equals("")) {
            holder.mTvDisasterType.setText(disasterInfo.getZqlx().getCodeName() + "(" + disasterInfo.getZhdj().getCodeName() + ")");
        } else if (disasterInfo.getZqlx() != null && !disasterInfo.getZqlx().equals("")) {
            holder.mTvDisasterType.setText(disasterInfo.getZqlx().getCodeName() + "(" + "暂无" + ")");
        } else if (disasterInfo.getZhdj() != null && !disasterInfo.getZhdj().equals("")) {
            holder.mTvDisasterType.setText("暂无类别" + "(" + disasterInfo.getZhdj().getCodeName() + ")");
        }
        if (!TextUtils.isEmpty(disasterInfo.getBjsj())) {
            holder.mTvDisasterTime.setText("[" + disasterInfo.getBjsj() + "]");
        } else {
            holder.mTvDisasterTime.setText("[" + "暂无报警时间" + "]");
        }
        if (disasterInfo.getXqzdjgdm() != null && !disasterInfo.getXqzdjgdm().equals("")) {
            holder.mTvDisasterTeamName.setText(disasterInfo.getXqzdjgdm().getName());
        } else {
            holder.mTvDisasterTeamName.setText("暂无");
        }
        if (!TextUtils.isEmpty(disasterInfo.getZhdd())) {
            holder.mTvDisasterAddress.setText(disasterInfo.getZhdd());
        } else {
            holder.mTvDisasterAddress.setText("暂无灾害地址");
        }

        if (disasterInfo.getZqlx().getCodeName().equals("火灾扑救")) {
            holder.mImgType.setImageResource(R.mipmap.ic_home_fire);
        } else if (disasterInfo.getZqlx().getCodeName().equals("社会救助")) {
            holder.mImgType.setImageResource(R.mipmap.ic_home_social_rescue);
        } else if (disasterInfo.getZqlx().getCodeName().equals("抢险救援")) {
            holder.mImgType.setImageResource(R.mipmap.ic_home_rescue);
        } else if (disasterInfo.getZqlx().getCodeName().equals("反恐排爆")) {
            holder.mImgType.setImageResource(R.mipmap.ic_home_anti_terrorism);
        } else if (disasterInfo.getZqlx().getCodeName().equals("公务出勤")) {
            holder.mImgType.setImageResource(R.mipmap.ic_home_official_attendance);
        } else if (disasterInfo.getZqlx().getCodeName().equals("其他出动")) {
            holder.mImgType.setImageResource(R.mipmap.ic_home_others);
        } else {
            holder.mImgType.setImageResource(R.mipmap.ic_home_fire);
        }

        holder.disasterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });

        holder.mLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onClick", "onClick: 65555555555555");
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLocated(view, position);
                }
            }
        });

        holder.mLayoutChatMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onChatted(view, position);
                }
            }
        });

        holder.mLayoutNavigtaion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onNavigated(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View disasterView;
        private ImageView mImgType;
        private TextView mTvDisasterType;
        private TextView mTvDisasterTime;
        //        private TextView mTvDisasterTeam;
        private TextView mTvDisasterTeamName;
        private TextView mTvDisasterAddress;
        private View mLayoutLocation;
        private View mLayoutChatMsg;
        private View mLayoutNavigtaion;
//        private ImageView mImgLocation;
//        private ImageView mImgChatMsg;
//        private ImageView mImgNavigation;

        public MyViewHolder(View itemView) {
            super(itemView);

            disasterView = itemView.findViewById(R.id.rl_item_view);
            mImgType = itemView.findViewById(R.id.iv_disaster_type);
            mTvDisasterType = itemView.findViewById(R.id.tv_disaster_type);
            mTvDisasterTime = itemView.findViewById(R.id.tv_disaster_time);
//            mTvDisasterTeam = itemView.findViewById(R.id.tv_disaster_team);
            mTvDisasterTeamName = itemView.findViewById(R.id.tv_disaster_team_name);
            mTvDisasterAddress = itemView.findViewById(R.id.tv_disaster_address);
            mLayoutLocation = itemView.findViewById(R.id.ll_disaster_location);
            mLayoutChatMsg = itemView.findViewById(R.id.ll_disaster_chat);
            mLayoutNavigtaion = itemView.findViewById(R.id.ll_disaster_navigation);

        }
    }
}
