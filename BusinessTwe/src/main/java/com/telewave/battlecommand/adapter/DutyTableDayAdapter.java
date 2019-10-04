package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.OrganInfo;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * @author liwh
 * @date 2019/1/10
 */
public class DutyTableDayAdapter extends BaseAdapter {

    private List<OrganInfo> organInfos;
    private Context mcontext;
    //默认选中第一个
    private int selectItem = 0;

    public DutyTableDayAdapter(Context context, List<OrganInfo> organInfos) {
        this.mcontext = context;
        this.organInfos = organInfos;
    }

    @Override
    public int getCount() {
        return organInfos == null ? 0 : organInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.organInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.duty_table_day_list_item, null);
            holder = new ViewHolder();
            holder.dutyTableOrgan = (TextView) convertView.findViewById(R.id.duty_table_organ);
            holder.dutyTableMsg = (TextView) convertView.findViewById(R.id.duty_table_msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrganInfo organInfo = organInfos.get(position);
        holder.dutyTableOrgan.setText(organInfo.getName());
        holder.dutyTableMsg.setText(organInfo.getType());

        return convertView;
    }


    static class ViewHolder {
        private TextView dutyTableOrgan;
        private TextView dutyTableMsg;
    }

}
