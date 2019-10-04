package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.activity.NotifyDetailActivity;
import com.telewave.battlecommand.bean.NotifyInfo;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * 重点单位 适配器
 *
 * @author liwh
 * @date 2019/1/9
 */
public class NotifyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<NotifyInfo> notifyInfoList;

    public NotifyListAdapter(Context mContext, List<NotifyInfo> notifyInfoList) {
        super();
        this.mContext = mContext;
        this.notifyInfoList = notifyInfoList;
    }

    @Override
    public int getCount() {
        return this.notifyInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.notifyInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.notify_list_item, null);
            holder = new ViewHolder();
            holder.notifyTitle = (TextView) convertView.findViewById(R.id.notify_title);
            holder.notifyFrom = (TextView) convertView.findViewById(R.id.notify_from);
            holder.notifyContent = (TextView) convertView.findViewById(R.id.notify_content);
            holder.notifyTime = (TextView) convertView.findViewById(R.id.notify_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NotifyInfo notifyInfo = notifyInfoList.get(position);
        holder.notifyTitle.setText(notifyInfo.getTitle());
        holder.notifyFrom.setText("来源:" + notifyInfo.getOrganname());
        holder.notifyContent.setText(notifyInfo.getContent());
        holder.notifyTime.setText(notifyInfo.getCreateDate());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotifyDetailActivity.class);
                intent.putExtra("id", notifyInfoList.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        return convertView;

    }

    static class ViewHolder {
        private TextView notifyTitle;
        private TextView notifyFrom;
        private TextView notifyContent;
        private TextView notifyTime;
    }

}
