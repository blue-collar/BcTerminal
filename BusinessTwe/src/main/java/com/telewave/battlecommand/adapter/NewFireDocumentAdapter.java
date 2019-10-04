package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telewave.battlecommand.bean.NewFireDocument;
import com.telewave.business.twe.R;

import java.util.List;


public class NewFireDocumentAdapter extends RecyclerView.Adapter<NewFireDocumentAdapter.MyRecycleViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private List<NewFireDocument> traceList;

    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    private static final int TYPE_THREE = 3;
    private static final int TYPE_FOUR = 4;
    private static final int TYPE_FIVE = 5;
    private static final int TYPE_SIX = 6;
    private static final int TYPE_SERVEN = 7;

    public NewFireDocumentAdapter(Context context, List<NewFireDocument> traceList) {
        mInflater = LayoutInflater.from(context);
        this.traceList = traceList;
        this.context = context;
    }

    @Override
    public MyRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyRecycleViewHolder vh = new MyRecycleViewHolderCommand(mInflater.inflate(R.layout.fire_document_command, parent, false));
        return vh;
    }


    @Override
    public void onBindViewHolder(MyRecycleViewHolder holder, int position) {
        if (position == 0) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            holder.tvAcceptTime.setTextColor(0xff555555);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tvAcceptTime.setTextColor(0xff999999);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }
        holder.bindHolder(traceList.get(position));
    }

    @Override
    public int getItemCount() {
        return traceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        NewFireDocument newFireDocument = traceList.get(position);
//        String type = newFireDocument.getType();
//        String typeCode = newFireDocument.getTypecode();
        int value = TYPE_SERVEN;
        return value;
    }


    public abstract class MyRecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAcceptTime;
        public TextView tvTopLine, tvDot;

        public MyRecycleViewHolder(View itemView) {
            super(itemView);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
        }

        public abstract void bindHolder(NewFireDocument newFireDocument);
    }

    public class MyRecycleViewHolderCommand extends MyRecycleViewHolder {
        private TextView tvFkr;//反馈人
        private TextView tvAcceptStation;

        public MyRecycleViewHolderCommand(View itemView) {
            super(itemView);
            tvFkr = (TextView) itemView.findViewById(R.id.tv_fkr);
            tvAcceptStation = (TextView) itemView.findViewById(R.id.tvAcceptStation);
        }

        @Override
        public void bindHolder(NewFireDocument newFireDocument) {
            if (newFireDocument.getFkr() != null) {
                tvFkr.setText(newFireDocument.getFkr());
            } else {
                tvFkr.setText("暂无反馈人信息");
            }
            tvAcceptStation.setText(newFireDocument.getFknr());
            tvAcceptTime.setText(newFireDocument.getFksj());
        }
    }


}
