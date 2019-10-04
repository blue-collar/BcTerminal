package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telewave.battlecommand.bean.YuAnPlanBean;
import com.telewave.business.twe.R;

import java.util.List;

public class YuAnPlanAdapter extends BaseAdapter {

    private Context mContext;
    private List<YuAnPlanBean.DataBean> yuAnPlanList;

    public YuAnPlanAdapter(Context mContext, List<YuAnPlanBean.DataBean> list) {
        super();
        this.mContext = mContext;
        this.yuAnPlanList = list;
    }

    @Override
    public int getCount() {
        return this.yuAnPlanList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.yuAnPlanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.yu_an_item, null);
            holder = new ViewHolder();
            holder.organName = (TextView) convertView.findViewById(R.id.tv_organ_Name);
            holder.organNumber = (TextView) convertView.findViewById(R.id.tv_organ_Number);
            holder.checkOut = (TextView) convertView.findViewById(R.id.tv_check_out);
            holder.mImgState = (ImageView) convertView.findViewById(R.id.iv_organ_OperateState);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final YuAnPlanBean.DataBean yuAnPlan = yuAnPlanList.get(position);
        holder.organName.setText(yuAnPlan.getName());
        holder.organNumber.setText((yuAnPlan.getNum() == 0 ? "(0)" : "(" + yuAnPlan.getNum() + ")"));

        holder.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCheckOutListener != null) {
                    onItemCheckOutListener.onItemCheckOut(v, position);
                }
            }
        });

        holder.mImgState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCheckOutListener != null) {
                    onItemCheckOutListener.onItemStateChanged(v, position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView organName;
        private TextView organNumber;
        private TextView checkOut;
        private ImageView mImgState;
    }

    private OnItemCheckOutListener onItemCheckOutListener;

    public void setOnItemCheckOutListener(OnItemCheckOutListener onItemCheckOutListener) {
        this.onItemCheckOutListener = onItemCheckOutListener;
    }

    public interface OnItemCheckOutListener {
        void onItemCheckOut(View view, int position);

        void onItemStateChanged(View view, int position);
    }
}
