package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telewave.battlecommand.bean.ChemicalInfo;
import com.telewave.business.twe.R;

import java.util.List;


/**
 * 重点单位 适配器
 *
 * @author zhangjun
 * @date 2019/8/6
 */
public class ChemicalInfoAdapter extends RecyclerView.Adapter<ChemicalInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<ChemicalInfo> mData;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ChemicalInfoAdapter(Context mContext, List<ChemicalInfo> data) {
        super();
        this.mContext = mContext;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.from(mContext).inflate(R.layout.chemical_info_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ChemicalInfo tempInfo = mData.get(position);

        holder.mTvName.setText(tempInfo.getCname());

        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private TextView mTvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView.findViewById(R.id.ll_chemical_item);
            mTvName = (TextView) itemView.findViewById(R.id.chemical_info_name);
        }
    }

}
