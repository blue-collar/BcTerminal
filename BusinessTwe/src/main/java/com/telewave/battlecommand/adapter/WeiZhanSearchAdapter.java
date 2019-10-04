package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.WeiZhanSearch;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * 注册 微站搜索 适配器
 *
 * @author liwh
 * @date 2019/1/11
 */
public class WeiZhanSearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<WeiZhanSearch> weiZhanSearchList;

    public WeiZhanSearchAdapter(Context mContext, List<WeiZhanSearch> weiZhanSearchList) {
        super();
        this.mContext = mContext;
        this.weiZhanSearchList = weiZhanSearchList;
    }

    @Override
    public int getCount() {
        return this.weiZhanSearchList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.weiZhanSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.weizhan_search_item, null);
            holder = new ViewHolder();
            holder.weizhanSearchName = (TextView) convertView.findViewById(R.id.weizhan_search_name);
            holder.weizhanSearchSjjg = (TextView) convertView.findViewById(R.id.weizhan_search_sjjg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WeiZhanSearch weiZhanSearch = weiZhanSearchList.get(position);

        holder.weizhanSearchName.setText(weiZhanSearch.getXfzmc());
        holder.weizhanSearchSjjg.setText("所属机构：" + weiZhanSearch.getSzdxfjg().getName());

        return convertView;

    }

    static class ViewHolder {
        private TextView weizhanSearchName;
        private TextView weizhanSearchSjjg;
    }

}
