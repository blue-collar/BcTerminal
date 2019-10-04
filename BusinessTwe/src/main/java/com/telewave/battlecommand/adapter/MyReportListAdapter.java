package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.Water;
import com.telewave.business.twe.R;

import java.util.List;

public class MyReportListAdapter extends BaseAdapter {
    private List<Water> list;
    private Context context;

    public MyReportListAdapter(List<Water> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_water_list, null);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv_team_name = convertView.findViewById(R.id.tv_team_name);
        viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
        viewHolder.tv_address = convertView.findViewById(R.id.tv_address);
        viewHolder.tv_status = convertView.findViewById(R.id.tv_status);
        viewHolder.tv_address.setText(list.get(position).getSydz());
        viewHolder.tv_time.setText(list.get(position).getUpdateDate());
        viewHolder.tv_team_name.setText(list.get(position).getSymc());
        viewHolder.tv_status.setText(getStatus(list.get(position).getShzt()));
       /* viewHolder.ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LocationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("jd", list.get(position).getGisX());
                bundle.putString("wd", list.get(position).getGisY());
                bundle.putString("address", list.get(position).getZhdd());
                intent.putExtra(ConstData.BUNDLE_KEY,bundle);
                context.startActivity(intent);
            }
        });*/


        return convertView;
    }

    private String getStatus(String code) {
        String status = "";
        switch (code) {
            case "0":
                status = "已归档";
                break;
            case "2":
                status = "待审核";
                break;
            case "3":
                status = "已驳回";
                break;
        }
        return status;
    }

    class ViewHolder {
        TextView tv_address, tv_time, tv_team_name, tv_status;

    }
}
