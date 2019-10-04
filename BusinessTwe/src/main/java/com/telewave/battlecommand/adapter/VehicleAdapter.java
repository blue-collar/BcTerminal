package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.VehicleDispatch;
import com.telewave.business.twe.R;

import java.util.List;

public class VehicleAdapter extends BaseAdapter {

    private Context mContext;
    private List<VehicleDispatch> mVehicles;


    public VehicleAdapter(Context context, List<VehicleDispatch> data) {
        this.mContext = context;
        this.mVehicles = data;
    }


    @Override
    public int getCount() {
        return mVehicles.size();
    }

    @Override
    public Object getItem(int position) {
        return mVehicles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_vehicle_dispatch, null);
            holder = new ViewHolder();
            holder.mTvOrganName = (TextView) convertView.findViewById(R.id.tv_organ_name);
            holder.mTvVehicleName = (TextView) convertView.findViewById(R.id.tv_vehicle_name);
            holder.mTvDispatchTime = (TextView) convertView.findViewById(R.id.tv_vehicle_dispatch_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VehicleDispatch vehicleDispatch = mVehicles.get(position);
        holder.mTvOrganName.setText(vehicleDispatch.getXfjgdm().getName());
        holder.mTvVehicleName.setText(vehicleDispatch.getClmc() + "(" + vehicleDispatch.getCphm() + ")");
        holder.mTvDispatchTime.setText(vehicleDispatch.getZdcdd().getFssj());
        return convertView;
    }

    static class ViewHolder {
        private TextView mTvOrganName;
        private TextView mTvVehicleName;
        private TextView mTvDispatchTime;
    }
}
