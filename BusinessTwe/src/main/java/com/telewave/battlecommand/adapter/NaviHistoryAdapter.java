package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.db.NaviMsg;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * 导航历史适配器
 *
 * @author liwh
 * @date 2019/07/23
 */
public class NaviHistoryAdapter extends BaseAdapter {

    private List<NaviMsg> naviMsgList;
    private Context mcontext;
    //默认选中第一个
    private int selectItem = 0;

    public NaviHistoryAdapter(Context context, List<NaviMsg> naviMsgList) {
        this.mcontext = context;
        this.naviMsgList = naviMsgList;
    }

    @Override
    public int getCount() {
        return naviMsgList == null ? 0 : naviMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.naviMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_navi_history, null);
            holder = new ViewHolder();
            holder.tvStartAddress = (TextView) convertView.findViewById(R.id.tv_start_address);
            holder.tvEndAddress = (TextView) convertView.findViewById(R.id.tv_end_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NaviMsg naviMsg = naviMsgList.get(position);
        holder.tvStartAddress.setText(naviMsg.getStartAddress());
        holder.tvEndAddress.setText(naviMsg.getEndAddress());
        return convertView;
    }

    static class ViewHolder {
        private TextView tvStartAddress;
        private TextView tvEndAddress;
    }

}
