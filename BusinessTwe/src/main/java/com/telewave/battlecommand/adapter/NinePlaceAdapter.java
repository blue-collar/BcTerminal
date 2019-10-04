package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.NineSmallPlace;
import com.telewave.business.twe.R;

import java.util.List;

public class NinePlaceAdapter extends BaseAdapter {

    private List<NineSmallPlace> mDatas;
    private Context context;

    public NinePlaceAdapter(Context context, List<NineSmallPlace> data) {
        this.mDatas = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.nine_place_item, null);
            holder.tv_address = convertView.findViewById(R.id.tv_nine_place_address);
            holder.tv_name = convertView.findViewById(R.id.tv_nine_place_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NineSmallPlace tempInfo = mDatas.get(position);

        if (!TextUtils.isEmpty(tempInfo.getAddress())) {
            holder.tv_address.setText(tempInfo.getAddress());
        }
        if (!TextUtils.isEmpty(tempInfo.getPlaceName())) {
            holder.tv_name.setText(tempInfo.getPlaceName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_address;
    }

}
