package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telewave.battlecommand.bean.ImportUnit;
import com.telewave.business.twe.R;

import java.util.List;


/**
 * 重点单位 适配器
 *
 * @author zhangjun
 * @date 2019/8/12
 */
public class GridImportUnitAdapter extends RecyclerView.Adapter<GridImportUnitAdapter.MyViewHolder> {

    private Context mContext;
    private List<ImportUnit> importUnits;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public GridImportUnitAdapter(Context mContext, List<ImportUnit> importUnits) {
        super();
        this.mContext = mContext;
        this.importUnits = importUnits;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.from(mContext).inflate(R.layout.grid_import_unit_item, null);
        final MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ImportUnit importUnit = importUnits.get(position);

        holder.importUnitName.setText(importUnit.getObjectname());
        holder.importUnitAddress.setText(importUnit.getObjectaddr());

        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.importUnits.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private TextView importUnitName;
        private TextView importUnitAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView.findViewById(R.id.ll_unit_item);
            importUnitName = (TextView) itemView.findViewById(R.id.import_unit_name);
            importUnitAddress = (TextView) itemView.findViewById(R.id.import_unit_address);
        }
    }

}
