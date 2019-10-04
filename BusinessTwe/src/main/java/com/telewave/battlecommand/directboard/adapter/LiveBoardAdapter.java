package com.telewave.battlecommand.directboard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.telewave.battlecommand.directboard.activity.AudienceLiveBoardActivity;
import com.telewave.battlecommand.directboard.bean.LiveBoardBean;
import com.telewave.business.twe.R;

import java.util.List;

import static com.baidu.navisdk.util.jar.JarUtils.getResources;

/**
 * 正在直播 适配器
 *
 * @author liwh
 * @date 2019/08/08
 */
public class LiveBoardAdapter extends BaseAdapter {
    private Context mContext;
    private List<LiveBoardBean> liveBoardList;

    public LiveBoardAdapter(Context mContext, List<LiveBoardBean> liveBoardList) {
        super();
        this.mContext = mContext;
        this.liveBoardList = liveBoardList;
    }

    @Override
    public int getCount() {
        return this.liveBoardList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.liveBoardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.live_board_member_item, null);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.tv_userName);
            holder.onlineState = (TextView) convertView.findViewById(R.id.tv_onlineState);
            holder.userDes = (TextView) convertView.findViewById(R.id.tv_userDes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LiveBoardBean liveBoard = liveBoardList.get(position);
        holder.userName.setText(liveBoard.getLiveUsername());
        if (TextUtils.equals(liveBoard.getIsOpenLive(), "1")) {
            holder.onlineState.setText("(直播中)");
            holder.onlineState.setTextColor(getResources().getColor(R.color.top_bar));
        } else {
            holder.onlineState.setText("(未直播)");
            holder.onlineState.setTextColor(getResources().getColor(R.color.dark_gray));
        }
        holder.userDes.setText(liveBoard.getLiveOfficename());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roomId = Integer.parseInt(liveBoard.getRoomNumber());
                String liveUserId = liveBoard.getLiveUserid();
                String liveUserName = liveBoard.getLiveUsername();
                Intent intent = new Intent(mContext, AudienceLiveBoardActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("liveUserId", liveUserId);
                intent.putExtra("liveUserName", liveUserName);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }
        });
        return convertView;

    }

    static class ViewHolder {
        private TextView userName;
        private TextView onlineState;
        private TextView userDes;
    }

}
