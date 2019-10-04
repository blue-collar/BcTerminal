package com.telewave.battlecommand.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.telewave.battlecommand.bean.FightPicInfo;
import com.telewave.business.twe.R;

import java.util.List;

public class PhotoVideoAdapter extends RecyclerView.Adapter<PhotoVideoAdapter.MyViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;

    Context mContext;
    //    List<String> mDatas;
    List<FightPicInfo> mDatas;
    int mMaxNum;
    LayoutInflater mInflater;
    //    OnItemClickListener mOnItemClickListener;
    OnItemPlayListener mOnItemPlayListener;

    public PhotoVideoAdapter(Context context, List<FightPicInfo> datas, int maxNum) {
        mContext = context;
        mDatas = datas;
        mMaxNum = maxNum;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_photo_video_recyview, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mDatas.size() < mMaxNum) {
            if (mDatas.size() == 0) {
                holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
                holder.mIvPlay.setVisibility(View.GONE);
            } else {

                FightPicInfo info = mDatas.get(position);
                //获取路径
                String filePath = info.getFilePhonePath();
                String url = info.getUrl();
                if (position < mDatas.size()) {

                    holder.mIvPlay.setVisibility(View.VISIBLE);
                    holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
                    if (url != null) {
                        //取第一帧制作图片
                        Glide.with(mContext).load(url)
                                .apply(new RequestOptions().placeholder(R.mipmap.pictures_no).error(R.mipmap.pictures_no))
                                .into(holder.mIvDisPlayItemPhoto);
                    } else if (filePath != null) {
                        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                        holder.mIvDisPlayItemPhoto.setImageBitmap(thumbnail);
                    }
                } else {

                    holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
                    holder.mIvPlay.setVisibility(View.GONE);
                    holder.mIvError.setVisibility(View.GONE);
                }
            }

        } else {
            FightPicInfo info = mDatas.get(position);

            String filePath = info.getFilePhonePath();
            holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
            holder.mIvPlay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(filePath)) {
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                holder.mIvDisPlayItemPhoto.setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    public int getItemCount() {

        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvDisPlayItemPhoto;
        ImageView mIvPlay;
        ImageView mIvError;
        View mRootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mIvDisPlayItemPhoto = itemView.findViewById(R.id.ivDisPlayItemPhoto);
            mIvPlay = itemView.findViewById(R.id.ivPlay);
            mIvError = itemView.findViewById(R.id.ivError);
            mRootView = itemView.findViewById(R.id.rootView);

            mIvPlay.setOnClickListener(this);
            mIvDisPlayItemPhoto.setOnClickListener(this);
            //ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            final int vId = view.getId();
            if (R.id.ivPlay == vId) {
                //ToastUtil.showMessage(mContext, "切换播放页面播放视频~");
                if (mOnItemPlayListener != null) {
                    mOnItemPlayListener.onItemPlay(view, getAdapterPosition());
                }
            } else if (R.id.ivDisPlayItemPhoto == vId) {
                if (mOnItemPlayListener != null) {
                    mOnItemPlayListener.onItemPlay(view, getAdapterPosition());
                }
            } else {
            }
        }

    }


    public interface OnItemPlayListener {
        void onItemPlay(View view, int position);

        //void onItemLongClick(View v, int position);
    }

    public void setOnItemPlayListener(OnItemPlayListener onItemPlayListener) {
        mOnItemPlayListener = onItemPlayListener;
    }
}
