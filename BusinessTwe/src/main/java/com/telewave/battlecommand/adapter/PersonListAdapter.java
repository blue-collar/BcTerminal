package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.bean.ExpertInfo;
import com.telewave.business.twe.R;

import java.util.List;

public class PersonListAdapter extends BaseAdapter {

    private List<ExpertInfo> personList;
    private Context context;

    public PersonListAdapter(Context context, List<ExpertInfo> personList) {
        this.personList = personList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expert_info_item, null);
            holder.mItemView = convertView.findViewById(R.id.ll_expert_item);
            holder.tv_position = convertView.findViewById(R.id.tv_position);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpertInfo tempInfo = personList.get(position);

        holder.tv_name.setText(tempInfo.getXm());
        if (tempInfo.getShzjly() != null && tempInfo.getShzjly().getZjlydm() != null) {
            holder.tv_position.setText(tempInfo.getShzjly().getZjlydm().getCodeName());
        }

//        holder.mItemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(personItemClickLisener != null){
//                    personItemClickLisener.onPersonItemClick(v, position);
//                }
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        View mItemView;
        TextView tv_name, tv_position;

    }

//    private PersonItemClickLisener personItemClickLisener;
//
//    public  void setPersonItemClickLisener(PersonItemClickLisener lisener){
//        this.personItemClickLisener = lisener;
//    }
//   public interface  PersonItemClickLisener{
//
//        void onPersonItemClick(View view, int position);
//    }
}
