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
import com.telewave.battlecommand.bean.FightPicInfo;
import com.telewave.business.twe.R;

import java.util.List;


public class PhotoWallAdapter extends RecyclerView.Adapter<PhotoWallAdapter.MyViewHolder> {

    Context mContext;
    List<FightPicInfo> mDatas;
    int mMaxNum;
    LayoutInflater mInflater;
    boolean mIsDelete = false;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;

    public PhotoWallAdapter(Context context, List<FightPicInfo> datas, int maxNum) {
        mContext = context;
        mDatas = datas;
        mMaxNum = maxNum;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<FightPicInfo> list) {
        this.mDatas = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_photo_wall_recyview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mDatas.size() < mMaxNum) {
            if (mDatas.size() == 0) {
                holder.mIvAddPhoto.setVisibility(View.VISIBLE);
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
//                holder.mIvUploadingBg.setVisibility(View.GONE);

            } else {
                //判断是不是最后一张，最后一第为添加的图片
                if (position < mDatas.size()) {
                    //获取路径
//                    String filePath = mDatas.get(position);
                    String filePath = mDatas.get(position).getFilePhonePath();
                    String url = mDatas.get(position).getUrl();
                    holder.mIvAddPhoto.setVisibility(View.GONE);
                    holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
                    if (url != null) {
                        Glide.with(mContext).load(url)
                                .apply(new RequestOptions().placeholder(R.mipmap.pictures_no).error(R.mipmap.pictures_no))
                                .listener(mRequestListener).into(holder.mIvDisPlayItemPhoto);
                    } else if (filePath != null) {
                        Glide.with(mContext).load(filePath)
                                .apply(new RequestOptions().placeholder(R.mipmap.pictures_no).error(R.mipmap.pictures_no))
                                .listener(mRequestListener).into(holder.mIvDisPlayItemPhoto);
                    }
//                    Glide.with(mContext).load(url).error(R.mipmap.pictures_no).into(holder.mIvDisPlayItemPhoto);
                    if (mIsDelete) {
                        holder.mIvDelete.setVisibility(View.VISIBLE);
                    } else {
                        holder.mIvDelete.setVisibility(View.GONE);
                    }

                } else {

                    holder.mIvAddPhoto.setVisibility(View.VISIBLE);
                    holder.mIvDelete.setVisibility(View.GONE);
                    holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
                    holder.mTvProgress.setVisibility(View.GONE);
                    holder.mIvError.setVisibility(View.GONE);
//                    holder.mIvUploadingBg.setVisibility(View.GONE);
                }
            }

        } else {

            String picUrl = (String) mDatas.get(position).getFilePhonePath();
            holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
            holder.mIvAddPhoto.setVisibility(View.GONE);

            Glide.with(mContext).load(picUrl).listener(mRequestListener).into(holder.mIvDisPlayItemPhoto);
            if (mIsDelete) {
                holder.mIvDelete.setVisibility(View.VISIBLE);
//                holder.mIvUploadingBg.setVisibility(View.VISIBLE);
            } else {
                holder.mIvDelete.setVisibility(View.GONE);
//                holder.mIvUploadingBg.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) {
            return 1;
        } else if (mDatas.size() < mMaxNum) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIvDisPlayItemPhoto;
        ImageView mIvAddPhoto;
        //        ImageView mIvUploadingBg;//加载进度条(灰色)
        ImageView mIvError;
        TextView mTvProgress;
        ImageView mIvDelete;
        View mRootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mIvDisPlayItemPhoto = itemView.findViewById(R.id.ivDisPlayItemPhoto);
            mIvAddPhoto = itemView.findViewById(R.id.ivAddPhoto);
//            mIvUploadingBg = itemView.findViewById(R.id.ivUploadingBg);
            mIvError = itemView.findViewById(R.id.ivError);
            mTvProgress = itemView.findViewById(R.id.tvProgress);
            mIvDelete = itemView.findViewById(R.id.ivDelete);
            mRootView = itemView.findViewById(R.id.rootView);

            mIvAddPhoto.setOnClickListener(this);
            mIvDelete.setOnClickListener(this);
            mIvDisPlayItemPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mIvDelete.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            mIvDisPlayItemPhoto.setOnClickListener(this);
            //ButterKnife.bind(this, itemView);
        }


        @Override
        public void onClick(View view) {
            final int vId = view.getId();
            if (R.id.ivAddPhoto == vId) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            } else if (R.id.ivDelete == vId) {
                //删除
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(view, getAdapterPosition());
                    mIvDelete.setVisibility(View.GONE);//隐藏
                }
//                    mDatas.remove(getAdapterPosition());
//                    notifyDataSetChanged();
            } else if (R.id.ivDisPlayItemPhoto == vId) {
//                    ToastUtil.showMessage(mContext, "" + getAdapterPosition());
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onPreViewItemClick(view, getAdapterPosition());
                }
            } else {
            }
        }

    }


    public interface OnItemClickListener {

        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);

        void onPreViewItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
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
