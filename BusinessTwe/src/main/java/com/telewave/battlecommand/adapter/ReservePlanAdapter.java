package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.telewave.battlecommand.bean.ReservePlan;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ToastUtils;

import java.util.List;


/**
 * 重点单位预案 适配器
 *
 * @author liwh
 * @date 2019/1/11
 */
public class ReservePlanAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReservePlan> reservePlanList;

    public ReservePlanAdapter(Context mContext, List<ReservePlan> reservePlanList) {
        super();
        this.mContext = mContext;
        this.reservePlanList = reservePlanList;
    }

    @Override
    public int getCount() {
        return this.reservePlanList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.reservePlanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.import_unit_item, null);
            holder = new ViewHolder();
            holder.importUnitNname = (TextView) convertView.findViewById(R.id.import_unit_name);
            holder.importUnitAddress = (TextView) convertView.findViewById(R.id.import_unit_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ReservePlan reservePlan = reservePlanList.get(position);
        holder.importUnitNname.setText("预案名称:" + reservePlan.getYamc());
        holder.importUnitAddress.setText("创建时间:" + reservePlan.getCjsj());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.fujian_dialog, null);
                ListView fujian_listView = (ListView) view.findViewById(R.id.fujian_list);
                List<ReservePlan.FilesBean> filesBeanList = reservePlan.getFiles();
                if (filesBeanList != null && !filesBeanList.isEmpty()) {
                    ReservePlanFuJianAdapter policyGuiFanFuJianAdapter = new ReservePlanFuJianAdapter(mContext, filesBeanList);
                    fujian_listView.setAdapter(policyGuiFanFuJianAdapter);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    final AlertDialog dialog = builder.create();
                    dialog.setView(view, 0, 0, 0, 0);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    ToastUtils.toastShortMessage(reservePlan.getYamc() + "暂无附件信息");
                }
            }
        });
        return convertView;

    }

    static class ViewHolder {
        private TextView importUnitNname;
        private TextView importUnitAddress;
    }

}
