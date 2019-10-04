package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.Schedule;
import com.telewave.business.twe.R;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {
    private List<Schedule> list;
    private Context context;

    public ScheduleAdapter(List<Schedule> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_list, null);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_position = convertView.findViewById(R.id.tv_position);
        viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
        viewHolder.tv_position.setText(list.get(position).getLxmc());
        viewHolder.tv_name.setText(list.get(position).getYhxm());


        return convertView;
    }

    class ViewHolder {
        TextView tv_position, tv_name;

    }
}
