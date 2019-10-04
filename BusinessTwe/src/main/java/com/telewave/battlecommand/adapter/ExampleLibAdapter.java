package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.activity.ExampleLibDetailActivity;
import com.telewave.battlecommand.bean.ExampleBean;
import com.telewave.business.twe.R;

import java.util.List;

/**
 * 案例库 适配器
 *
 * @author liwh
 * @date 2019/1/28
 */
public class ExampleLibAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExampleBean> exampleBeanList;

    public ExampleLibAdapter(Context mContext, List<ExampleBean> exampleBeanList) {
        super();
        this.mContext = mContext;
        this.exampleBeanList = exampleBeanList;
    }

    @Override
    public int getCount() {
        return this.exampleBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.exampleBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.example_lib_item, null);
            holder = new ViewHolder();
            holder.exampleLibTitle = (TextView) convertView.findViewById(R.id.example_lib_title);
            holder.exampleLibTime = (TextView) convertView.findViewById(R.id.example_lib_time);
            holder.exampleLibContent = (TextView) convertView.findViewById(R.id.example_lib_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ExampleBean exampleBean = exampleBeanList.get(position);
        holder.exampleLibTitle.setText(exampleBean.getTitle());
        holder.exampleLibTime.setText(exampleBean.getUpdateDate());
        holder.exampleLibContent.setText(exampleBean.getContent());

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View view = LayoutInflater.from(mContext).inflate(R.layout.fujian_dialog, null);
//                ListView fujian_listView = (ListView) view.findViewById(R.id.fujian_list);
//                List<ExampleBean.FileListBean> filesBeanList = exampleBean.getFileList();
//                if (filesBeanList != null && !filesBeanList.isEmpty()) {
//                    ExampleLibFuJianAdapter exampleLibFuJianAdapter = new ExampleLibFuJianAdapter(mContext, filesBeanList);
//                    fujian_listView.setAdapter(exampleLibFuJianAdapter);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    final AlertDialog dialog = builder.create();
//                    dialog.setView(view, 0, 0, 0, 0);
//                    dialog.setCanceledOnTouchOutside(true);
//                    dialog.show();
//                } else {
//                    ToastUtil.showMessage(mContext, "暂无附件信息");
//                }
//
//            }
//        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExampleLibDetailActivity.class);
                intent.putExtra("id", exampleBeanList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;

    }

    static class ViewHolder {
        private TextView exampleLibTitle;
        private TextView exampleLibTime;
        private TextView exampleLibContent;
    }

}
