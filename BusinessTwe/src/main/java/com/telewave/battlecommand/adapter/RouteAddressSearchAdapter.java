package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * 签到 位置微调 附件位置显示 适配器
 *
 * @author liwh
 * @date 2019/1/10
 */
public class RouteAddressSearchAdapter extends BaseAdapter {

    private List<PoiInfo> locationlist;
    private Context mcontext;

    public RouteAddressSearchAdapter(Context context, List<PoiInfo> locationlist) {
        this.mcontext = context;
        this.locationlist = locationlist;
    }

    @Override
    public int getCount() {
        return locationlist == null ? 0 : locationlist.size();
    }

    @Override
    public Object getItem(int position) {
        return this.locationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_location_nearby, null);
            holder = new ViewHolder();
            holder.tv_locationName = (TextView) convertView.findViewById(R.id.tv_location_name);
            holder.tv_locationAddress = (TextView) convertView.findViewById(R.id.tv_location_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PoiInfo poiInfo = locationlist.get(position);
        holder.tv_locationName.setText(poiInfo.name);
        holder.tv_locationAddress.setText(poiInfo.address);

        convertView.setOnClickListener(new OnClickTextListener(position));

        return convertView;
    }

    class OnClickTextListener implements View.OnClickListener {
        private int position;

        public OnClickTextListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            selectedListener.onItemSelected(position);
        }

    }

    static class ViewHolder {
        private TextView tv_locationName;
        private TextView tv_locationAddress;
    }

    /**
     * 将选择传递给Activity
     */
    public interface OnSelectedListener {
        void onItemSelected(int position);
    }

    private OnSelectedListener selectedListener;

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.selectedListener = listener;
    }
}
