package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.activity.PolicyGuiFanDetailActivity;
import com.telewave.battlecommand.bean.PolicyGuiFan;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * 政策规范 适配器
 *
 * @author liwh
 * @date 2019/1/9
 */
public class PolicyGuiFanAdapter extends BaseAdapter {
    private Context mContext;
    private List<PolicyGuiFan> policyGuiFanList;

    public PolicyGuiFanAdapter(Context mContext, List<PolicyGuiFan> policyGuiFanList) {
        super();
        this.mContext = mContext;
        this.policyGuiFanList = policyGuiFanList;
    }

    @Override
    public int getCount() {
        return this.policyGuiFanList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.policyGuiFanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.policy_guifan_item, null);
            holder = new ViewHolder();
            holder.policyGuiFanTitle = (TextView) convertView.findViewById(R.id.policy_gui_fan_title);
            holder.policyGuiFanContent = (TextView) convertView.findViewById(R.id.policy_gui_fan_content);
            holder.policyGuiFanTime = (TextView) convertView.findViewById(R.id.policy_gui_fan_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PolicyGuiFan policyGuiFan = policyGuiFanList.get(position);
        holder.policyGuiFanTitle.setText(policyGuiFan.getTitle());
        holder.policyGuiFanContent.setText(policyGuiFan.getContent());
        holder.policyGuiFanTime.setText(policyGuiFan.getUpdateDate());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PolicyGuiFanDetailActivity.class);
                intent.putExtra("id", policyGuiFanList.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        return convertView;

    }

    static class ViewHolder {
        private TextView policyGuiFanTitle;
        private TextView policyGuiFanContent;
        private TextView policyGuiFanTime;
    }

}
