package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.telewave.battlecommand.bean.FjxxListBean;
import com.telewave.business.twe.R;

import java.util.List;


public class PlanUnitPhotoAdapter extends RecyclerView.Adapter<PlanUnitPhotoAdapter.MyViewHolder> {

    Context mContext;
    List<FjxxListBean> mDatas;
    int mMaxNum;
    LayoutInflater mInflater;
    boolean mIsDelete = false;
    OnItemClickListener mOnItemClickListener;

    public PlanUnitPhotoAdapter(Context context, List<FjxxListBean> datas, int maxNum) {
        mContext = context;
        mDatas = datas;
        mMaxNum = maxNum;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_photo_detail_recyview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mDatas.size() < mMaxNum) {
            if (mDatas.size() == 0) {
                holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);

            } else {
                //判断是不是最后一张，最后一第为添加的图片
                if (position < mDatas.size()) {
                    FjxxListBean info = mDatas.get(position);
                    //获取路径
                    String filePath = info.getFilePhonePath();
                    String url = info.getUrl();
                    holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
                    if (url != null) {
                        Glide.with(mContext).load(url)
                                .apply(new RequestOptions().placeholder(R.mipmap.pictures_no).error(R.mipmap.pictures_no))
                                .listener(mRequestListener).into(holder.mIvDisPlayItemPhoto);
                    } else if (filePath != null) {
                        Glide.with(mContext).load(filePath).listener(mRequestListener).into(holder.mIvDisPlayItemPhoto);
                    }
//                    Glide.with(mContext).load(url).error(R.mipmap.pictures_no).into(holder.mIvDisPlayItemPhoto);

                } else {

                    holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
                    holder.mTvProgress.setVisibility(View.GONE);
                    holder.mIvError.setVisibility(View.GONE);
                }
            }

        } else {

            String picUrl = (String) mDatas.get(position).getFilePhonePath();
            holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(picUrl).listener(mRequestListener).into(holder.mIvDisPlayItemPhoto);
        }

    }

    @Override
    public int getItemCount() {
//        if (mDatas == null || mDatas.size() == 0) {
//            return 1;
//        } else if (mDatas.size() < mMaxNum) {
//            return mDatas.size() + 1;
//        } else {
//            return mDatas.size();
//        }
        return mDatas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIvDisPlayItemPhoto;
        ImageView mIvError;
        TextView mTvProgress;
        View mRootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mIvDisPlayItemPhoto = itemView.findViewById(R.id.ivDisPlayItemPhoto);
            mIvError = itemView.findViewById(R.id.ivError);
            mTvProgress = itemView.findViewById(R.id.tvProgress);
            mRootView = itemView.findViewById(R.id.rootView);

            mIvDisPlayItemPhoto.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            final int vId = view.getId();
            if (R.id.ivDisPlayItemPhoto == vId) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onPreViewItemClick(view, getAdapterPosition());
                }
            } else {
            }
        }

    }


    public interface OnItemClickListener {


        void onPreViewItemClick(View v, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;

    }

    public void setIsDelete(boolean isDelete) {
        mIsDelete = isDelete;

    }


    RequestListener mRequestListener = new RequestListener() {


        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    };

}
